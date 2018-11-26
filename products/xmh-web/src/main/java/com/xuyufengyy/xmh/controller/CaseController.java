package com.xuyufengyy.xmh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller - 工程案例
 *
 * @author Xu minghua 2017/11/28
 */
@Controller
@RequestMapping(value = "/case")
public class CaseController {

    /**
     * 工程案例
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "/case/index";
    }
}
