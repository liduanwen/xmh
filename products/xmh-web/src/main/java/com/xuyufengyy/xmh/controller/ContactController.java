package com.xuyufengyy.xmh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller - 联系我们
 *
 * @author Xu minghua 2017/11/28
 */
@Controller
@RequestMapping(value = "/contact")
public class ContactController {

    /**
     * 联系我们
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "/contact/index";
    }
}
