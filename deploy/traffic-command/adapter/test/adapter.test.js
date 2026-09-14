import assert from "node:assert/strict";
import { test } from "node:test";
import {
  chooseStream,
  discoverChannelOwner,
  parseChannelIds,
  resolveChannel,
  toLocalMediaProxyPath,
} from "../lib.js";
import { createAdapterServer } from "../server.js";

test("解析 deviceIds 时去重并限制最多四路", () => {
  assert.deepEqual(
    parseChannelIds(" ch1, ch2,ch1,ch3,ch4,ch5 ", 4),
    ["ch1", "ch2", "ch3", "ch4"]
  );
});

test("优先使用通道级 GB28181 设备映射和名称", () => {
  const result = resolveChannel(
    "channel-1",
    {
      defaultDeviceId: "default-device",
      channels: {
        "channel-1": { deviceId: "device-1", name: "一号门" },
      },
    },
    "env-device"
  );

  assert.deepEqual(result, {
    deviceId: "device-1",
    channelId: "channel-1",
    name: "一号门",
  });
});

test("没有通道级映射时使用默认设备编号", () => {
  assert.equal(
    resolveChannel(
      "channel-2",
      { defaultDeviceId: "default-device", channels: {} },
      "env-device"
    ).deviceId,
    "default-device"
  );
});

test("缺少所属设备编号时返回明确错误", () => {
  assert.throws(
    () => resolveChannel("channel-3", { defaultDeviceId: "", channels: {} }),
    /未配置所属的 GB28181 设备编号/
  );
});

test("可以从WVP目录自动发现级联通道所属的下级平台", async () => {
  const requestedPaths = [];
  const result = await discoverChannelOwner("channel-2", async (pathname) => {
    requestedPaths.push(pathname);
    if (pathname.startsWith("/api/device/query/devices?")) {
      return { list: [{ deviceId: "platform-1" }, { deviceId: "platform-2" }] };
    }
    if (pathname.includes("platform-1/channels")) {
      return { list: [{ deviceId: "channel-1", name: "一号通道" }] };
    }
    return { list: [{ deviceId: "channel-2", name: "二号通道" }] };
  });

  assert.deepEqual(result, {
    deviceId: "platform-2",
    channelId: "channel-2",
    name: "二号通道",
  });
  assert.equal(requestedPaths.length, 3);
});

test("自动发现失败时提示先完成宇视目录同步", async () => {
  await assert.rejects(
    () =>
      discoverChannelOwner("missing-channel", async (pathname) =>
        pathname.startsWith("/api/device/query/devices?")
          ? { list: [{ deviceId: "platform-1" }] }
          : { list: [] }
      ),
    /已注册并完成目录同步/
  );
});

test("优先选择 HTTP-FLV 并可改写为本地媒体代理路径", () => {
  const stream = chooseStream({
    flv: "http://172.18.0.2:80/rtp/device_channel.flv?token=abc",
    ts: "http://172.18.0.2:80/rtp/device_channel.live.ts",
  });

  assert.equal(stream.format, "flv");
  assert.equal(
    toLocalMediaProxyPath(stream.url),
    "./media/rtp/device_channel.flv?token=abc"
  );
});

test("健康检查接口可以独立响应", async (context) => {
  const server = createAdapterServer();
  await new Promise((resolve) => server.listen(0, "127.0.0.1", resolve));
  context.after(() => new Promise((resolve) => server.close(resolve)));

  const address = server.address();
  const response = await fetch(`http://127.0.0.1:${address.port}/api/health`);
  const body = await response.json();

  assert.equal(response.status, 200);
  assert.equal(body.ok, true);
  assert.equal(body.service, "gb28181-h5-adapter");
});
