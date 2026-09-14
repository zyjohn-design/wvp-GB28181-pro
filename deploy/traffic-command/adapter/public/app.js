const params = new URLSearchParams(window.location.search);
const channelIds = [...new Set(
  (params.get("deviceIds") || params.get("deviceId") || "")
    .split(",")
    .map((id) => id.trim())
    .filter(Boolean)
)].slice(0, 4);

const grid = document.getElementById("video-grid");
const globalStatus = document.getElementById("global-status");
const ptzPanel = document.getElementById("ptz-panel");
const ptzDeviceName = document.getElementById("ptz-device-name");
const ptzSpeed = document.getElementById("ptz-speed");
const ptzSpeedValue = document.getElementById("ptz-speed-value");
const activePlayers = [];
let activePtzChannel = null;

function showGlobalStatus(message) {
  globalStatus.textContent = message;
  globalStatus.hidden = false;
}

function hideGlobalStatus() {
  globalStatus.hidden = true;
}

function createVideoCard(stream, index) {
  const card = document.createElement("section");
  const title = document.createElement("div");
  const video = document.createElement("video");
  const message = document.createElement("div");
  const ptzButton = document.createElement("button");

  card.className = "video-card";
  title.className = "video-card__title";
  title.textContent = stream.name || stream.channelId || `画面 ${index + 1}`;

  video.autoplay = true;
  video.muted = true;
  video.controls = true;
  video.playsInline = true;
  video.title = title.textContent;

  // 保持 index2.html 现有体验：双击视频直接进入该画面的浏览器全屏。
  video.addEventListener("dblclick", () => {
    const requestFullscreen = video.requestFullscreen || video.webkitRequestFullscreen;
    requestFullscreen?.call(video);
  });

  message.className = "video-card__message";
  message.textContent = stream.ok ? "正在连接国标视频……" : stream.message;
  message.classList.toggle("is-error", !stream.ok);

  ptzButton.className = "video-card__ptz";
  ptzButton.type = "button";
  ptzButton.textContent = "云台";
  ptzButton.hidden = !stream.ok;
  ptzButton.addEventListener("click", () => openPtz(stream));

  card.append(video, title, message, ptzButton);
  grid.append(card);

  if (stream.ok) {
    startMpegtsPlayer(stream, video, message);
  }
}

function startMpegtsPlayer(stream, video, message) {
  if (!window.mpegts?.isSupported()) {
    message.textContent = "当前浏览器不支持 MSE H5 视频播放，请使用最新版 Chrome 或 Edge";
    message.classList.add("is-error");
    return;
  }

  const player = window.mpegts.createPlayer(
    {
      type: stream.format === "mpegts" ? "mpegts" : "flv",
      isLive: true,
      hasVideo: true,
      url: stream.streamUrl,
    },
    {
      enableWorker: true,
      enableStashBuffer: false,
      stashInitialSize: 128,
      lazyLoad: false,
      liveBufferLatencyChasing: true,
      liveBufferLatencyMaxLatency: 2,
      liveBufferLatencyMinRemain: 0.5,
    }
  );

  player.attachMediaElement(video);
  player.load();
  player.play().catch(() => {
    // 某些浏览器仍可能阻止自动播放；controls 保留后可由用户手动点击播放。
  });
  player.on(window.mpegts.Events.LOADING_COMPLETE, () => {
    message.hidden = true;
  });
  player.on(window.mpegts.Events.MEDIA_INFO, () => {
    message.hidden = true;
  });
  player.on(window.mpegts.Events.ERROR, (_type, detail) => {
    message.hidden = false;
    message.classList.add("is-error");
    message.textContent = `视频播放失败：${detail || "请检查编码、网络和流媒体端口"}`;
  });

  video.addEventListener("playing", () => {
    message.hidden = true;
  });
  activePlayers.push(player);
}

async function initialize() {
  if (channelIds.length === 0) {
    showGlobalStatus(
      "未传入 deviceIds。示例：index.html?deviceIds=34020000001320000001&deviceType=IPC"
    );
    return;
  }

  grid.dataset.count = String(channelIds.length);
  showGlobalStatus("正在向 GB28181 平台请求实时视频……");

  try {
    const query = new URLSearchParams({ deviceIds: channelIds.join(",") });
    // 使用相对路径，适配 /HiatmpView/video/ 等 Nginx 子路径部署。
    const response = await fetch(`./api/streams/start?${query}`, {
      cache: "no-store",
    });
    const result = await response.json();

    if (!Array.isArray(result.streams)) {
      throw new Error(result.message || "点播接口返回格式不正确");
    }

    result.streams.forEach(createVideoCard);
    hideGlobalStatus();
  } catch (error) {
    showGlobalStatus(`国标视频初始化失败：${error.message}`);
  }
}

function openPtz(stream) {
  activePtzChannel = stream.channelId;
  ptzDeviceName.textContent = stream.name || stream.channelId;
  ptzPanel.hidden = false;

  // 通知新版 index2.html 自动放大当前 iframe，防止云台面板被小画面裁切。
  window.parent.postMessage(
    { type: "PTZ_OPEN", payload: { channelId: stream.channelId } },
    "*"
  );
}

function closePtz() {
  sendPtzCommand("stop");
  activePtzChannel = null;
  ptzPanel.hidden = true;
  window.parent.postMessage({ type: "PTZ_CLOSE" }, "*");
}

async function sendPtzCommand(command) {
  if (!activePtzChannel) return;

  try {
    const response = await fetch("./api/ptz", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        channelId: activePtzChannel,
        command,
        speed: Number(ptzSpeed.value),
      }),
    });
    const result = await response.json();
    if (!response.ok || !result.ok) {
      throw new Error(result.message || "云台命令失败");
    }
  } catch (error) {
    showGlobalStatus(error.message);
    setTimeout(hideGlobalStatus, 2500);
  }
}

const directions = [
  ["↖", "upleft"], ["↑", "up"], ["↗", "upright"],
  ["←", "left"], ["■", "stop"], ["→", "right"],
  ["↙", "downleft"], ["↓", "down"], ["↘", "downright"],
];

for (const [label, command] of directions) {
  const button = document.createElement("button");
  button.type = "button";
  button.textContent = label;
  button.dataset.ptzCommand = command;
  document.getElementById("ptz-directions").append(button);
}

for (const button of document.querySelectorAll("[data-ptz-command]")) {
  const command = button.dataset.ptzCommand;
  button.addEventListener("pointerdown", (event) => {
    event.preventDefault();
    button.setPointerCapture?.(event.pointerId);
    button.classList.add("is-pressing");
    sendPtzCommand(command);
  });

  const release = () => {
    if (!button.classList.contains("is-pressing")) return;
    button.classList.remove("is-pressing");
    if (command !== "stop") sendPtzCommand("stop");
  };

  button.addEventListener("pointerup", release);
  button.addEventListener("pointercancel", release);
  button.addEventListener("lostpointercapture", release);
}

document.getElementById("ptz-close").addEventListener("click", closePtz);
ptzSpeed.addEventListener("input", () => {
  ptzSpeedValue.value = ptzSpeed.value;
});

function cleanup() {
  for (const player of activePlayers) {
    try {
      player.pause();
      player.unload();
      player.detachMediaElement();
      player.destroy();
    } catch {
      // 页面卸载阶段忽略播放器清理异常。
    }
  }

  if (channelIds.length > 0) {
    navigator.sendBeacon(
      "./api/streams/stop",
      JSON.stringify({ channelIds })
    );
  }
}

window.addEventListener("pagehide", cleanup, { once: true });
initialize();
