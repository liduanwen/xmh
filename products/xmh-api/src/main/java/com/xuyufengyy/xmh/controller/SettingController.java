package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.constants.MediaTypes;
import com.xuyufengyy.xmh.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * controller - 系统设置
 */
@RestController
public class SettingController extends BaseController {

    @Value(value = "${image.view}")
    private String imageView;

    @Value(value = "${voice.view}")
    private String voiceView;

    /**
     * 系统设置
     * @param
     * @return String
     */
    @PostMapping(value = "/setting", produces = MediaTypes.JSON_UTF_8)
    public Result setting(){
        Map<String, String> map = new HashMap<>();
        map.put("imageUrl", imageView);
        map.put("voiceUrl", voiceView);
        return Result.success(map);
    }
}
