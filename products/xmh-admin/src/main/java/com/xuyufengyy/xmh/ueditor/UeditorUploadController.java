package com.xuyufengyy.xmh.ueditor;

import com.xuyufengyy.xmh.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于代替 Ueditor 中的 上传文件功能
 *
 * @author Xu minghua 2017/08/12
 */
@RestController
@RequestMapping(value = "/ueditor")
public class UeditorUploadController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(UeditorUploadController.class);

    @Autowired
    private UeditorUploadConfig ueConfig;

    @Autowired
    private FileProcesser fileProcesser;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadConfig(String action, HttpServletRequest request) {
        if ("config".equals(action))
            return gson.toJson(ueConfig);
        else
            return null;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(String action, MultipartFile upfile, HttpServletRequest request) {
        if ("uploadimage".equals(action)) {
            return gson.toJson(fileProcesser.uploadFileToAliyunOSS(FileProcesser.FileType.image, upfile));
        } else {//暂时不从处理抓去来的图片和其他文件
            State state = new State();
            state.setState("暂不支持该方式上传文件");
            return gson.toJson(state);
        }
    }


}
