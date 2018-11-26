package com.xuyufengyy.xmh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller - 服务内容
 *
 * @author Xu minghua 2017/11/28
 */
@Controller
@RequestMapping(value = "/service")
public class ServiceController {

    /**
     * 最新动态
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "/service/index";
    }

    /**
     * 合作流程
     */
    @RequestMapping(value = "/cooperation", method = RequestMethod.GET)
    public String cooperation(Model model) {
        return "/service/cooperation";
    }

    /**
     * 付款方式
     */
    @RequestMapping(value = "/payment_method", method = RequestMethod.GET)
    public String paymentMethod(Model model) {
        return "/service/payment_method";
    }
}
