package com.xuyufengyy.xmh.ueditor;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * upload file processer
 *
 * @author Xu minghua 2017/08/12
 */
@Component
public class FileProcesser {

    private static Logger logger = LoggerFactory.getLogger(FileProcesser.class);

    @Autowired
    private UeditorUploadConfig ueConfig;

    public enum FileType {
        /**
         * 图片
         */
        image,
        /**
         * Flash
         */
        flash,
        /**
         * media
         */
        media,
        /**
         * 文件
         */
        file
    }

    public State uploadFileToAliyunOSS(FileType fileType, MultipartFile multipartFile) {
        logger.info("FileProcesser--开始上传文件到阿里云OSS");
        State state = new State();
        String originalFilename = multipartFile.getOriginalFilename();
        String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        //上传图片文件
        if (fileType == FileType.image) {
            //判断文件扩展名是否正确
            if (!isValid(fileType, multipartFile)) {
                state.setState("不允许的图片类型");
                return state;
            }
            if (multipartFile.getSize() > ueConfig.getImageMaxSize()) {
                state.setState("文件大小超出限制");
                return state;
            }
            try {
                String fileExtName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String uuid = UUID.randomUUID().toString();
                String newFileName = uuid + "." + fileExtName;

                String path = ueConfig.getImagePathFormat();
                if (multipartFile != null) {// 判断上传的文件是否为空
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File fileSource = new File(path, newFileName);
                    // 转存文件到指定的路径
                    multipartFile.transferTo(fileSource);
                    logger.info("文件成功上传到指定目录下");
                } else {
                    logger.info("没有找到相对应的文件");
                    return null;
                }

//                String url = UploadHanlder.uploadFile(originalFilename, multipartFile.getBytes());
                String url = ueConfig.getImageUrlPrefix() + path + newFileName;
                logger.info("url :" + url);
                state.setState("SUCCESS");
                state.setOriginal(originalFilename);
                state.setType(extName);
                state.setUrl(url);
                return state;
            } catch (IOException ioe) {
                logger.error("upload io error:", ioe.getMessage());
                state.setState("上传文件错误");
                return state;
            }
        }
        logger.info("FileProcesser--开始上传完成");
        return state;
    }

    /**
     * 判断文件类型是否正确
     *
     * @param fileType
     * @param multipartFile
     * @return
     */
    public boolean isValid(FileType fileType, MultipartFile multipartFile) {
        logger.info("valid file extend name ");
        if (multipartFile == null) {
            return false;
        }
        String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if (fileType == FileType.image) {
            for (String s : ueConfig.getImageAllowFiles()) {
                if (s.equalsIgnoreCase("." + extName)) ;
                return true;
            }
        }
        return false;

    }
}
