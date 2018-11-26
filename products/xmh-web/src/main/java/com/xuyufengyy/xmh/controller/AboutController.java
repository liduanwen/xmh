package com.xuyufengyy.xmh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller - 关于我们
 *
 * @author Xu minghua 2017/11/28
 */
@Controller
@RequestMapping(value = "/about")
public class AboutController {

    /**
     * 关于我们
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "/about/index";
    }
}
