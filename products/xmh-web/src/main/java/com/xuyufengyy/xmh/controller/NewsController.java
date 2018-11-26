package com.xuyufengyy.xmh.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * controller - 最新动态
 *
 * @author Xu minghua 2017/11/28
 */
@Controller
@RequestMapping(value = "/news")
public class NewsController {

//    @Resource
//    private NewsService newsService;
//
//    /**
//     * 最新动态
//     */
//    @RequestMapping(value = "/index", method = RequestMethod.GET)
//    public String index(Model model, Pageable pageable){
//        Page page = newsService.findAll(pageable);
//        model.addAttribute("page", page);
//        return "/news/index";
//    }
//
//    /**
//     * 最新动态查看
//     */
//    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
//    public String view(@PathVariable("id") Long id, Model model){
//        News news = newsService.update(id);
//        model.addAttribute("news", news);
//        return "/news/view";
//    }
}
