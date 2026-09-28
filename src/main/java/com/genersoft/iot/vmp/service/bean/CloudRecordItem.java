package com.genersoft.iot.vmp.service.bean;

import com.genersoft.iot.vmp.media.event.media.MediaRecordMp4Event;
import com.genersoft.iot.vmp.media.event.media.MediaRecordProcessEvent;
import com.genersoft.iot.vmp.utils.MediaServerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 云端录像数据
 */
@Data
@Schema(description = "云端录像记录")
public class CloudRecordItem {
    /**
     * 主键
     */
    @Schema(description = "录像记录数据库 ID")
    private int id;

    /**
     * 应用名
     */
    @Schema(description = "流应用名")
    private String app;

    /**
     * 流
     */
    @Schema(description = "流 ID")
    private String stream;

    /**
     * 健全ID
     */
    @Schema(description = "该次录像关联的会话 Call-ID")
    private String callId;

    /**
     * 开始时间
     */
    @Schema(description = "媒体节点上报的录像开始时间戳")
    private long startTime;

    /**
     * 结束时间
     */
    @Schema(description = "录像结束时间戳；由开始时间与录像时长计算")
    private long endTime;

    /**
     * ZLM Id
     */
    @Schema(description = "流媒体节点 ID")
    private String mediaServerId;

    /**
     * 文件名称
     */
    @Schema(description = "录像文件名")
    private String fileName;

    /**
     * 文件路径
     */
    @Schema(description = "录像文件路径")
    private String filePath;

    /**
     * 文件夹
     */
    @Schema(description = "录像所在文件夹")
    private String folder;

    /**
     * 收藏，收藏的文件不移除
     */
    @Schema(description = "是否收藏")
    private Boolean collect;

    /**
     * 保留，收藏的文件不移除
     */
    @Schema(description = "是否保留")
    private Boolean reserve;

    /**
     * 文件大小
     */
    @Schema(description = "录像文件大小，字节")
    private long fileSize;

    /**
     * 文件时长
     */
    @Schema(description = "媒体节点上报的录像时长；单位需按所用媒体节点核对")
    private double timeLen;

    /**
     * 所属服务ID
     */
    @Schema(description = "所属 WVP 服务节点 ID")
    private String serverId;

    public static CloudRecordItem getInstance(MediaRecordMp4Event param) {
        CloudRecordItem cloudRecordItem = new CloudRecordItem();
        cloudRecordItem.setApp(param.getApp());
        cloudRecordItem.setStream(param.getStream());
        cloudRecordItem.setStartTime(param.getRecordInfo().getStartTime());
        cloudRecordItem.setFileName(param.getRecordInfo().getFileName());
        cloudRecordItem.setFolder(param.getRecordInfo().getFolder());
        cloudRecordItem.setFileSize(param.getRecordInfo().getFileSize());
        cloudRecordItem.setFilePath(param.getRecordInfo().getFilePath());
        cloudRecordItem.setMediaServerId(param.getMediaServer().getId());
        cloudRecordItem.setTimeLen(param.getRecordInfo().getTimeLen());
        cloudRecordItem.setEndTime((param.getRecordInfo().getStartTime() + (long)param.getRecordInfo().getTimeLen()));
        Map<String, String> paramsMap = MediaServerUtils.urlParamToMap(param.getRecordInfo().getParams());
        if (paramsMap.get("callId") != null) {
            cloudRecordItem.setCallId(paramsMap.get("callId"));
        }
        return cloudRecordItem;
    }

    public static CloudRecordItem getInstance(MediaRecordProcessEvent event) {
        CloudRecordItem cloudRecordItem = new CloudRecordItem();
        cloudRecordItem.setApp(event.getApp());
        cloudRecordItem.setStream(event.getStream());
        cloudRecordItem.setFileName(event.getFileName());
        cloudRecordItem.setMediaServerId(event.getMediaServer().getId());
        cloudRecordItem.setTimeLen(event.getCurrentFileDuration() * 1000);
        return cloudRecordItem;
    }

}
