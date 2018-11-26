package com.xuyufengyy.xmh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller - IT外包
 *
 * @author Xu minghua 2017/12/06
 */
@Controller
@RequestMapping(value = "/outsource")
public class OutsourceController {

    /**
     * it外包
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "/outsource/index";
    }
}
