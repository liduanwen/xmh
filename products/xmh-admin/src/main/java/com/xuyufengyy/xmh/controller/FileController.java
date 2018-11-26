package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.utils.FtpJSch;
import com.xuyufengyy.xmh.utils.Setting;
import com.xuyufengyy.xmh.utils.SettingUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 文件处理
 */
@Controller
public class FileController extends BaseController {

    @Resource
    private SettingUtils settingUtils;

    @Resource
    private FtpJSch ftpJSch;

    @Value(value = "${remote.server.upload.image.address}")
    private String imageAddress;

    //图片验证
    public boolean isValid(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return false;
        }
        Setting setting = settingUtils.get();
        if (setting.getUploadMaxSize() != null && setting.getUploadMaxSize() != 0 && multipartFile.getSize() > setting.getUploadMaxSize() * 1024L * 1024L) {
            return false;
        }
        String[] uploadExtensions = setting.getUploadImageExtensions();
        if (ArrayUtils.isNotEmpty(uploadExtensions)) {
            return FilenameUtils.isExtension(multipartFile.getOriginalFilename(), uploadExtensions);
        }
        return false;
    }

    public String uploadLocal(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }
        Setting setting = settingUtils.get();
        String uploadPath = setting.getImageUploadPath();
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
//            String path = FreemarkerUtils.process(uploadPath, model);
            String path = "/Users/xuyufengyy/Desktop/";
            String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            File destFile = new File(destPath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            multipartFile.transferTo(destFile);
            return destPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // uploadFile
//	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
//	public @ResponseBody Map<String, Object> uploadFile(MultipartFile file) throws IllegalStateException, IOException {
//        Map<String, Object> data = new HashMap<String, Object>();
//        if (!isValid(file)) {
//            data.put("message", Message.warn("admin.upload.invalid"));
//        } else {
//            String url = uploadLocal(file);
//            if (url == null) {
//                data.put("message", Message.warn("admin.upload.error"));
//            } else {
//                data.put("message", SUCCESS_MESSAGE);
//                data.put("url", url);
//            }
//        }
//
//
//		// 原始名称
//		String oldFileName = file.getOriginalFilename(); // 获取上传文件的原名
////      System.out.println(oldFileName);
//		// 存储图片的虚拟本地路径（这里需要配置tomcat的web模块路径，双击猫进行配置）
//		String saveFilePath = "/Users/xuyufengyy/Desktop/";
//		// 上传图片
//		if (file != null && oldFileName != null && oldFileName.length() > 0) {
//			// 新的图片名称
//			String newFileName = UUID.randomUUID() + oldFileName.substring(oldFileName.lastIndexOf("."));
//			// 新图片
//			File newFile = new File(saveFilePath + "\\" + newFileName);
//			// 将内存中的数据写入磁盘
//            file.transferTo(newFile);
//			// 将新图片名称返回到前端
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("success", "成功啦");
//			map.put("url", newFileName);
//			return map;
//		} else {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("error", "图片不合法");
//			return map;
//		}
//	}

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
        String fileName = "";
        try {
            ftpJSch.getConnect();
            fileName = ftpJSch.upload(imageAddress, uploadfile.getOriginalFilename(), uploadfile.getInputStream());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>(fileName, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(fileName, HttpStatus.OK);
    }

}