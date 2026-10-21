# JTMediaAttribute

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTMediaAttribute.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `audioEncoder` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 输入音频编码方式: 1 G. 721 2 G. 722 3 G. 723 4 G. 728 5 G. 729 6 G. 711A 7 G. 711U 8 G. 726 9 G. 729A 10 DVI4_3 11 DVI4_4 12 DVI4_8K 13 DVI4_16K 14 LPC 15 S16BE_STEREO 16 S16BE_MONO 17 MPEGAUDIO 18 LPCM 19 AAC 20 WMA9STD 21 HEAAC 22 PCM_VOICE 23 PCM_AUDIO 24 AACLC 25 MP3 26 ADPCMA 27 MP4AUDIO 28 AMR | `1` |
| `audioChannels` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 输入音频声道数 | `1` |
| `audioSamplingRate` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 输入音频采样率: 0:8 kHz; 1:22. 05 kHz; 2:44. 1 kHz; 3:48 kHz | `1` |
| `audioSamplingBits` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 输入音频采样位数: 0:8 位; 1:16 位; 2:32 位 | `1` |
| `audioFrameLength` | integer | `int` | 由具体接口决定 | 整数；1 ~ 4；音频帧长度: 范围 1 ~ 4 294 967 295 | 源码未声明默认值 | 音频帧长度: 范围 1 ~ 4 294 967 295 | `1` |
| `audioOutputEnable` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 是否支持音频输出: 0:不支持;1:支持 | `1` |
| `videoEncoder` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 视频编码方式: 98 H. 264 99 H. 265 100 AVS 101 SVAC | `1` |
| `audioChannelMax` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 终端支持的最大音频物理通道数量: | `1` |
| `videoChannelMax` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 终端支持的最大视频物理通道数量: | `1` |
