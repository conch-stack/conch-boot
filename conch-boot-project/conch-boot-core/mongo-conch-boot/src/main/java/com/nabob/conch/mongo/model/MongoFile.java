package com.nabob.conch.mongo.model;


import java.util.Arrays;

/**
 * @Description: [Mongo文件映射]
 * @Author: [toming]
 */
public class MongoFile {
    private String gridId;
    private String filename;
    private byte[] content;
    private String contentType;
    private Long createTime;

    //region Fields-------------------------------------------------------------

    public static final String FILENAME = "filename";

    public static final String CONTENT = "content";

    public static final String CONTENT_TYPE = "contentType";

    public static final String GRIDID = "gridId";

    public static final String CREATE_TIME = "createTime";

    //endregion Fields-------------------------------------------------------------


    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MongoFile{" +
                "gridId='" + gridId + '\'' +
                ", filename='" + filename + '\'' +
                ", content=" + Arrays.toString(content) +
                ", contentType='" + contentType + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
