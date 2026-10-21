# VersionPo

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/common/VersionPo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `GIT_Revision` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | git的全版本号 | `"<GIT_Revision>"` |
| `Create_By` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | maven版本 | `"<Create_By>"` |
| `GIT_BRANCH` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | git的分支 | `"<GIT_BRANCH>"` |
| `GIT_URL` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | git的url | `"<GIT_URL>"` |
| `BUILD_DATE` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 构建日期 | `"<BUILD_DATE>"` |
| `GIT_DATE` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 构建日期 | `"<GIT_DATE>"` |
| `artifactId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 项目名称 配合pom使用 | `"<artifactId>"` |
| `GIT_Revision_SHORT` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | git局部版本号 | `"<GIT_Revision_SHORT>"` |
| `version` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 项目的版本如2.0.1.0 配合pom使用 | `"<version>"` |
| `project` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 子系统名称 | `"<project>"` |
| `Build_Jdk` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | jdk版本 | `"<Build_Jdk>"` |
