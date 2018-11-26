package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.Product;
import com.xuyufengyy.xmh.ProductService;
import com.xuyufengyy.xmh.ScenicSpot;
import com.xuyufengyy.xmh.ScenicSpotService;
import com.xuyufengyy.xmh.constants.MediaTypes;
import com.xuyufengyy.xmh.dto.in.HomeIn;
import com.xuyufengyy.xmh.dto.in.ProductIn;
import com.xuyufengyy.xmh.dto.in.VoicePlayIn;
import com.xuyufengyy.xmh.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * controller - 主页
 */
@RestController
public class HomeController extends BaseController {

    @Resource
    private ScenicSpotService scenicSpotService;

    @Resource
    private ProductService productService;

    /**
     * 景区推荐
     * @param
     * @return String
     */
    @PostMapping(value = "/recommend", produces = MediaTypes.JSON_UTF_8)
    public Result recommend(){

        List<ScenicSpot> list = scenicSpotService.findlist(true);

        return Result.success(list);
    }

    /**
     * 景区列表
     * @param
     * @return String
     */
    @PostMapping(value = "/home", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public Result home(@RequestBody(required = false) HomeIn homeIn){

        if(StringUtils.isBlank(homeIn.getCityName())){//如果当前城市为空，定位到北京
            homeIn.setCityName("北京");
        }

        Pageable pageable = PageRequest.of(homeIn.getPageNumber(), homeIn.getPageSize());
        Page<Map<String, Object>> page = scenicSpotService.findPage(homeIn.getCityName(), homeIn.getName(), pageable);

        return Result.success(page);
    }

    /**
     * 产品列表
     * @param
     * @return String
     */
    @PostMapping(value = "/product_list", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public Result product(@RequestBody(required = false) ProductIn productIn){

        if(productIn.getId() == null){
            //TODO 验证数据
        }

        Pageable pageable = PageRequest.of(productIn.getPageNumber(), productIn.getPageSize());
        Page<Product> page = productService.findPage(productIn.getId(), pageable);

        return Result.success(page);
    }

    /**
     * 点击播放
     * @param
     * @return String
     */
    @PostMapping(value = "/voice_play", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public Result voicePlay(@RequestBody(required = false) VoicePlayIn voicePlayIn){

        if(voicePlayIn.getId() == null){
            //TODO 验证数据
        }

        productService.update(voicePlayIn.getId());

        return Result.success();
    }
}
