import fs from "node:fs";
import http from "node:http";
import https from "node:https";
import path from "node:path";
import { fileURLToPath } from "node:url";
import {
  chooseStream,
  discoverChannelOwner,
  loadDeviceMap,
  loadEnvFile,
  parseChannelIds,
  resolveChannel,
  toLocalMediaProxyPath,
} from "./lib.js";

const ROOT_DIR = path.dirname(fileURLToPath(import.meta.url));
loadEnvFile(path.join(ROOT_DIR, ".env"));

const CONFIG = {
  port: Number(process.env.PORT || 8088),
  wvpBaseUrl: String(process.env.WVP_BASE_URL || "http://127.0.0.1:18978").replace(/\/$/, ""),
  wvpApiKey: String(process.env.WVP_API_KEY || "").trim(),
  wvpAuthHeader: String(process.env.WVP_AUTH_HEADER || "api-key").trim(),
  zlmBaseUrl: String(process.env.ZLM_BASE_URL || "http://127.0.0.1:8080").replace(/\/$/, ""),
  defaultDeviceId: String(process.env.DEFAULT_GB_DEVICE_ID || "").trim(),
  deviceMapFile: path.resolve(
    ROOT_DIR,
    process.env.DEVICE_MAP_FILE || "./config/device-map.json"
  ),
  proxyMedia: String(process.env.PROXY_MEDIA || "true").toLowerCase() !== "false",
  maxChannels: Math.max(1, Math.min(4, Number(process.env.MAX_CHANNELS || 4))),
  playTimeoutMs: Math.max(3000, Number(process.env.PLAY_TIMEOUT_MS || 45000)),
  vendorMpegtsFile: process.env.VENDOR_MPEGTS_FILE
    ? path.resolve(process.env.VENDOR_MPEGTS_FILE)
    : path.join(ROOT_DIR, "node_modules/mpegts.js/dist/mpegts.js"),
};

const MIME_TYPES = {
  ".html": "text/html; charset=utf-8",
  ".js": "text/javascript; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".svg": "image/svg+xml",
  ".png": "image/png",
  ".ico": "image/x-icon",
};

function writeJson(response, statusCode, body) {
  response.writeHead(statusCode, {
    "Content-Type": "application/json; charset=utf-8",
    "Cache-Control": "no-store",
  });
  response.end(JSON.stringify(body));
}

function getWvpHeaders() {
  return CONFIG.wvpApiKey
    ? { [CONFIG.wvpAuthHeader]: CONFIG.wvpApiKey }
    : {};
}

async function requestWvp(pathname) {
  const abortController = new AbortController();
  const timer = setTimeout(() => abortController.abort(), CONFIG.playTimeoutMs);

  try {
    const response = await fetch(`${CONFIG.wvpBaseUrl}${pathname}`, {
      headers: getWvpHeaders(),
      signal: abortController.signal,
    });
    const text = await response.text();
    let body;

    try {
      body = text ? JSON.parse(text) : {};
    } catch {
      throw new Error(`WVP 返回了非 JSON 内容（HTTP ${response.status}）`);
    }

    if (!response.ok) {
      throw new Error(body.msg || body.message || `WVP HTTP ${response.status}`);
    }

    if (typeof body.code === "number" && body.code !== 0) {
      throw new Error(body.msg || `WVP 点播失败，错误码 ${body.code}`);
    }

    return body.data ?? body;
  } finally {
    clearTimeout(timer);
  }
}

// 自动发现结果做短时缓存，避免每次播放、停止或云台命令都遍历WVP目录。
// 明确配置的device-map和DEFAULT_GB_DEVICE_ID始终优先于缓存。
const discoveredChannelCache = new Map();
const CHANNEL_CACHE_TTL_MS = 5 * 60 * 1000;

async function getResolvedChannel(channelId) {
  const deviceMap = loadDeviceMap(CONFIG.deviceMapFile);
  try {
    return resolveChannel(channelId, deviceMap, CONFIG.defaultDeviceId);
  } catch (error) {
    // 只有“缺少父设备编号”时才自动发现；通道编号为空等配置错误直接返回。
    if (!String(error.message).includes("未配置所属")) throw error;
  }

  const normalizedChannelId = String(channelId).trim();
  const cached = discoveredChannelCache.get(normalizedChannelId);
  if (cached && Date.now() - cached.cachedAt < CHANNEL_CACHE_TTL_MS) {
    return cached.channel;
  }

  const channel = await discoverChannelOwner(normalizedChannelId, requestWvp);
  const mapped = deviceMap.channels?.[normalizedChannelId];
  if (mapped && typeof mapped === "object" && mapped.name) {
    channel.name = String(mapped.name).trim();
  }
  discoveredChannelCache.set(normalizedChannelId, {
    channel,
    cachedAt: Date.now(),
  });
  return channel;
}

async function startOneChannel(channelId, requestIsHttps) {
  const channel = await getResolvedChannel(channelId);
  const streamData = await requestWvp(
    `/api/play/start/${encodeURIComponent(channel.deviceId)}/${encodeURIComponent(channel.channelId)}`
  );
  const selectedStream = chooseStream(streamData, requestIsHttps && !CONFIG.proxyMedia);

  return {
    ok: true,
    ...channel,
    streamUrl:
      CONFIG.proxyMedia && selectedStream.url.startsWith("http")
        ? toLocalMediaProxyPath(selectedStream.url)
        : selectedStream.url,
    format: selectedStream.format,
    sourceKey: selectedStream.sourceKey,
  };
}

async function handleStartStreams(request, response, url) {
  const rawDeviceIds =
    url.searchParams.get("deviceIds") || url.searchParams.get("deviceId") || "";
  const channelIds = parseChannelIds(rawDeviceIds, CONFIG.maxChannels);

  if (channelIds.length === 0) {
    writeJson(response, 400, { ok: false, message: "缺少 deviceIds 参数" });
    return;
  }

  const isHttps =
    request.headers["x-forwarded-proto"] === "https" ||
    Boolean(request.socket.encrypted);
  const settled = await Promise.allSettled(
    channelIds.map((channelId) => startOneChannel(channelId, isHttps))
  );
  const streams = settled.map((result, index) =>
    result.status === "fulfilled"
      ? result.value
      : {
          ok: false,
          channelId: channelIds[index],
          name: channelIds[index],
          message: result.reason?.message || "点播失败",
        }
  );

  writeJson(response, streams.some((stream) => stream.ok) ? 200 : 502, {
    ok: streams.some((stream) => stream.ok),
    streams,
  });
}

async function readJsonBody(request) {
  const chunks = [];
  let size = 0;

  for await (const chunk of request) {
    size += chunk.length;
    if (size > 64 * 1024) throw new Error("请求内容过大");
    chunks.push(chunk);
  }

  const text = Buffer.concat(chunks).toString("utf8");
  return text ? JSON.parse(text) : {};
}

async function handleStopStreams(request, response) {
  const body = await readJsonBody(request);
  const channelIds = Array.isArray(body.channelIds) ? body.channelIds : [];

  await Promise.allSettled(
    channelIds.map(async (channelId) => {
      const channel = await getResolvedChannel(channelId);
      await requestWvp(
        `/api/play/stop/${encodeURIComponent(channel.deviceId)}/${encodeURIComponent(channel.channelId)}`
      );
    })
  );

  writeJson(response, 200, { ok: true });
}

async function handlePtz(request, response) {
  const body = await readJsonBody(request);
  const command = String(body.command || "stop").toLowerCase();
  const allowedCommands = new Set([
    "left", "right", "up", "down", "upleft", "upright",
    "downleft", "downright", "zoomin", "zoomout", "stop",
  ]);

  if (!allowedCommands.has(command)) {
    writeJson(response, 400, { ok: false, message: "不支持的云台命令" });
    return;
  }

  const channel = await getResolvedChannel(body.channelId);
  const speed = Math.max(0, Math.min(255, Number(body.speed || 100)));
  const zoomSpeed = Math.max(0, Math.min(15, Math.round(speed / 17)));
  const query = new URLSearchParams({
    command,
    horizonSpeed: String(speed),
    verticalSpeed: String(speed),
    zoomSpeed: String(zoomSpeed),
  });

  await requestWvp(
    `/api/front-end/ptz/${encodeURIComponent(channel.deviceId)}/${encodeURIComponent(channel.channelId)}?${query}`
  );
  writeJson(response, 200, { ok: true });
}

function proxyMedia(request, response) {
  const incomingUrl = new URL(request.url, "http://adapter.local");
  const upstreamUrl = new URL(CONFIG.zlmBaseUrl);
  upstreamUrl.pathname = incomingUrl.pathname.slice("/media".length) || "/";
  upstreamUrl.search = incomingUrl.search;

  // 路径始终固定代理到配置的 ZLM 地址，不接受任意上游 URL，避免 SSRF。
  const transport = upstreamUrl.protocol === "https:" ? https : http;
  const proxyHeaders = {
    accept: request.headers.accept || "*/*",
    "user-agent": request.headers["user-agent"] || "gb28181-h5-adapter",
  };
  if (request.headers.range) {
    proxyHeaders.range = request.headers.range;
  }

  const upstreamRequest = transport.request(
    upstreamUrl,
    {
      method: request.method,
      headers: proxyHeaders,
    },
    (upstreamResponse) => {
      const headers = { ...upstreamResponse.headers };
      delete headers["content-security-policy"];
      response.writeHead(upstreamResponse.statusCode || 502, headers);
      upstreamResponse.pipe(response);
    }
  );

  upstreamRequest.on("error", (error) => {
    if (!response.headersSent) {
      writeJson(response, 502, { ok: false, message: `媒体代理失败：${error.message}` });
    } else {
      response.destroy(error);
    }
  });
  response.on("close", () => upstreamRequest.destroy());
  upstreamRequest.end();
}

function serveStatic(response, pathname) {
  const requestedPath = pathname === "/" ? "/index.html" : pathname;
  // 代码通过宿主机挂载时，npm依赖仍保留在镜像内，避免每次启动联网安装。
  const vendorFile = CONFIG.vendorMpegtsFile;
  const filePath =
    requestedPath === "/vendor/mpegts.js"
      ? vendorFile
      : path.resolve(path.join(ROOT_DIR, "public"), `.${requestedPath}`);
  const publicRoot = path.resolve(ROOT_DIR, "public");

  if (
    requestedPath !== "/vendor/mpegts.js" &&
    filePath !== publicRoot &&
    !filePath.startsWith(`${publicRoot}${path.sep}`)
  ) {
    writeJson(response, 403, { ok: false, message: "禁止访问" });
    return;
  }

  if (!fs.existsSync(filePath) || !fs.statSync(filePath).isFile()) {
    writeJson(response, 404, { ok: false, message: "资源不存在" });
    return;
  }

  response.writeHead(200, {
    "Content-Type": MIME_TYPES[path.extname(filePath)] || "application/octet-stream",
    "Cache-Control": requestedPath === "/index.html" ? "no-store" : "public, max-age=3600",
  });
  fs.createReadStream(filePath).pipe(response);
}

export function createAdapterServer() {
  return http.createServer(async (request, response) => {
    const url = new URL(request.url, `http://${request.headers.host || "localhost"}`);

    try {
      if (request.method === "GET" && url.pathname === "/api/health") {
        writeJson(response, 200, {
          ok: true,
          service: "gb28181-h5-adapter",
          proxyMedia: CONFIG.proxyMedia,
        });
      } else if (request.method === "GET" && url.pathname === "/api/streams/start") {
        await handleStartStreams(request, response, url);
      } else if (request.method === "POST" && url.pathname === "/api/streams/stop") {
        await handleStopStreams(request, response);
      } else if (request.method === "POST" && url.pathname === "/api/ptz") {
        await handlePtz(request, response);
      } else if (CONFIG.proxyMedia && url.pathname.startsWith("/media/")) {
        proxyMedia(request, response);
      } else if (request.method === "GET" || request.method === "HEAD") {
        serveStatic(response, url.pathname);
      } else {
        writeJson(response, 405, { ok: false, message: "不支持的请求方法" });
      }
    } catch (error) {
      const message = error.name === "AbortError" ? "WVP 点播请求超时" : error.message;
      console.error("[adapter]", error);
      if (!response.headersSent) {
        writeJson(response, 500, { ok: false, message });
      } else {
        response.destroy(error);
      }
    }
  });
}

if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) {
  createAdapterServer().listen(CONFIG.port, "0.0.0.0", () => {
    console.log(`GB28181 H5 adapter listening on http://0.0.0.0:${CONFIG.port}`);
    console.log(`WVP: ${CONFIG.wvpBaseUrl}`);
    console.log(`ZLM: ${CONFIG.zlmBaseUrl}, media proxy: ${CONFIG.proxyMedia}`);
  });
}
