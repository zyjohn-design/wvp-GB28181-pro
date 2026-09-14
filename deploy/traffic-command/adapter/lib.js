import fs from "node:fs";
import path from "node:path";

/**
 * 读取 JSON 通道映射。映射文件每次请求都会重新读取，现场修改后无需重启服务。
 */
export function loadDeviceMap(filePath) {
  if (!filePath || !fs.existsSync(filePath)) {
    return { defaultDeviceId: "", channels: {} };
  }

  const parsed = JSON.parse(fs.readFileSync(filePath, "utf8"));
  return {
    defaultDeviceId: String(parsed.defaultDeviceId || "").trim(),
    channels:
      parsed.channels && typeof parsed.channels === "object"
        ? parsed.channels
        : {},
  };
}

/**
 * 把 index2.html 传来的通道编号解析为 WVP 所需的设备编号 + 通道编号。
 */
export function resolveChannel(channelId, deviceMap, envDefaultDeviceId = "") {
  const normalizedChannelId = String(channelId || "").trim();
  if (!normalizedChannelId) {
    throw new Error("通道编号不能为空");
  }

  const mapped = deviceMap.channels?.[normalizedChannelId];
  const mappedObject =
    mapped && typeof mapped === "object" ? mapped : { deviceId: mapped };
  const deviceId = String(
    mappedObject.deviceId ||
      deviceMap.defaultDeviceId ||
      envDefaultDeviceId ||
      ""
  ).trim();

  if (!deviceId) {
    throw new Error(
      `通道 ${normalizedChannelId} 未配置所属的 GB28181 设备编号`
    );
  }

  return {
    deviceId,
    channelId: normalizedChannelId,
    name: String(mappedObject.name || normalizedChannelId).trim(),
  };
}

/**
 * 兼容WVP分页接口的常见返回结构，提取当前页数据。
 */
export function getPageItems(pageData) {
  if (Array.isArray(pageData)) return pageData;
  if (Array.isArray(pageData?.list)) return pageData.list;
  if (Array.isArray(pageData?.records)) return pageData.records;
  if (Array.isArray(pageData?.data?.list)) return pageData.data.list;
  if (Array.isArray(pageData?.data?.records)) return pageData.data.records;
  return [];
}

/**
 * 只知道通道国标编号时，自动遍历WVP已经注册的下级设备/平台，
 * 找到该通道所属的父设备编号。这样平台级联时无需手工填写
 * DEFAULT_GB_DEVICE_ID，也能支持多个宇视下级平台。
 */
export async function discoverChannelOwner(channelId, requestWvp) {
  const normalizedChannelId = String(channelId || "").trim();
  if (!normalizedChannelId) throw new Error("通道编号不能为空");

  const devicePage = await requestWvp(
    "/api/device/query/devices?page=1&count=10000"
  );
  const devices = getPageItems(devicePage);

  for (const device of devices) {
    const parentDeviceId = String(device?.deviceId || "").trim();
    if (!parentDeviceId) continue;

    const query = new URLSearchParams({
      page: "1",
      count: "50",
      query: normalizedChannelId,
    });
    const channelPage = await requestWvp(
      `/api/device/query/devices/${encodeURIComponent(parentDeviceId)}/channels?${query}`
    );
    const channel = getPageItems(channelPage).find(
      (item) => String(item?.deviceId || "").trim() === normalizedChannelId
    );

    if (channel) {
      return {
        deviceId: parentDeviceId,
        channelId: normalizedChannelId,
        name: String(channel.name || normalizedChannelId).trim(),
      };
    }
  }

  throw new Error(
    `WVP中未找到通道 ${normalizedChannelId}，请确认宇视下级平台已注册并完成目录同步`
  );
}

/** 解析逗号分隔的 deviceIds，去重并限制最大数量。 */
export function parseChannelIds(value, maxChannels = 4) {
  return [...new Set(String(value || "").split(",").map((id) => id.trim()))]
    .filter(Boolean)
    .slice(0, maxChannels);
}

/**
 * 从 WVP 返回值中选择低延迟 H5 播放地址。
 * 默认优先 HTTP-FLV；服务端媒体代理可以把它变成同源地址。
 */
export function chooseStream(streamData, preferSecure = false) {
  const secureCandidates = [
    ["https_flv", "flv"],
    ["wss_flv", "flv"],
    ["https_ts", "mpegts"],
  ];
  const normalCandidates = [
    ["flv", "flv"],
    ["ws_flv", "flv"],
    ["https_flv", "flv"],
    ["wss_flv", "flv"],
    ["ts", "mpegts"],
    ["https_ts", "mpegts"],
  ];
  const candidates = preferSecure
    ? [...secureCandidates, ...normalCandidates]
    : normalCandidates;

  for (const [key, format] of candidates) {
    if (streamData?.[key]) {
      return { url: streamData[key], format, sourceKey: key };
    }
  }

  throw new Error("WVP 未返回 HTTP-FLV 或 HTTP-TS 播放地址");
}

/** 将 WVP 返回的媒体路径改为本适配服务的同源代理路径。 */
export function toLocalMediaProxyPath(streamUrl) {
  const parsed = new URL(streamUrl);
  // 使用相对地址，保证页面既能部署在站点根路径，也能部署在
  // /HiatmpView/video/ 这样的现有业务子路径下。
  return `./media${parsed.pathname}${parsed.search}`;
}

/** 简单加载 .env 文件，不覆盖操作系统已经设置的环境变量。 */
export function loadEnvFile(filePath = path.resolve(".env")) {
  if (!fs.existsSync(filePath)) return;

  for (const rawLine of fs.readFileSync(filePath, "utf8").split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith("#")) continue;

    const separatorIndex = line.indexOf("=");
    if (separatorIndex < 1) continue;

    const key = line.slice(0, separatorIndex).trim();
    let value = line.slice(separatorIndex + 1).trim();
    value = value.replace(/^(["'])(.*)\1$/, "$2");

    if (!(key in process.env)) {
      process.env[key] = value;
    }
  }
}
