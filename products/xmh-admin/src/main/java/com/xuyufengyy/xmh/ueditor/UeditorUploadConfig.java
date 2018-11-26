package com.xuyufengyy.xmh.ueditor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ueditor 使用的配置
 *
 * @author Xu minghua 2017/08/12
 */
@Component
public class UeditorUploadConfig {

    @Value("${remote.server.upload.image.address}")
    private String imagePath;

    /* 上传图片配置项 */

    /**
     * 执行上传图片的action名称
     */
    private String imageActionName = "uploadimage";

    /**
     * 提交的图片表单名称
     */
    private String imageFieldName = "upfile";

    /**
     * 上传大小限制，单位B
     */
    private Integer imageMaxSize = 2048000;

    /**
     * 上传图片格式显示
     */
    private String[] imageAllowFiles = new String[]{".png", ".jpg", ".jpeg", ".gif"};

    /**
     * 是否压缩图片
     */
    private Boolean imageCompressEnable = true;

    /**
     * 图片压缩最长边限制
     */
    private Integer imageCompressBorder = 1600;

    /**
     * 插入的图片浮动方式
     */
    private String imageInsertAlign = "none";

    /**
     * 图片保存目录
     */
    private String imagePathFormat = "";

    /**
     * 图片访问路径前缀
     */
    private String imageUrlPrefix = "";

    public String getImageActionName() {
        return imageActionName;
    }

    public void setImageActionName(String imageActionName) {
        this.imageActionName = imageActionName;
    }

    public String getImageFieldName() {
        return imageFieldName;
    }

    public void setImageFieldName(String imageFieldName) {
        this.imageFieldName = imageFieldName;
    }

    public Integer getImageMaxSize() {
        return imageMaxSize;
    }

    public void setImageMaxSize(Integer imageMaxSize) {
        this.imageMaxSize = imageMaxSize;
    }

    public String[] getImageAllowFiles() {
        return imageAllowFiles;
    }

    public void setImageAllowFiles(String[] imageAllowFiles) {
        this.imageAllowFiles = imageAllowFiles;
    }

    public Boolean getImageCompressEnable() {
        return imageCompressEnable;
    }

    public void setImageCompressEnable(Boolean imageCompressEnable) {
        this.imageCompressEnable = imageCompressEnable;
    }

    public Integer getImageCompressBorder() {
        return imageCompressBorder;
    }

    public void setImageCompressBorder(Integer imageCompressBorder) {
        this.imageCompressBorder = imageCompressBorder;
    }

    public String getImageInsertAlign() {
        return imageInsertAlign;
    }

    public void setImageInsertAlign(String imageInsertAlign) {
        this.imageInsertAlign = imageInsertAlign;
    }

    public String getImagePathFormat() {
        return imagePath;
    }

    public void setImagePathFormat(String imagePathFormat) {
        this.imagePathFormat = imagePathFormat;
    }

    public String getImageUrlPrefix() {
        return imageUrlPrefix;
    }

    public void setImageUrlPrefix(String imageUrlPrefix) {
        this.imageUrlPrefix = imageUrlPrefix;
    }
}
