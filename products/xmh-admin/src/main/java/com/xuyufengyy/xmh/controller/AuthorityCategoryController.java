package com.xuyufengyy.xmh.controller;


import com.xuyufengyy.xmh.AuthorityCategory;
import com.xuyufengyy.xmh.AuthorityCategoryService;
import com.xuyufengyy.xmh.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * controller - 权限分类
 *
 * @author Xu minghua 2017/07/21
 */
@Controller
@RequestMapping(value = "/authority_category")
public class AuthorityCategoryController extends BaseController {

    @Resource
    private AuthorityCategoryService authorityCategoryService;

    /**
     * 权限分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, Model model) {
        Page page = authorityCategoryService.findAll(pageable);
        model.addAttribute("page", page);
        return "authority_category/list";
    }

    /**
     * 权限分类添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "authority_category/add";
    }

    /**
     * 权限分类保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(AuthorityCategory authorityCategory, RedirectAttributes redirectAttributes) {
        if (!isValid(authorityCategory)) {
            return ERROR_VIEW;
        }
        authorityCategoryService.save(authorityCategory);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 权限分类编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) {
        AuthorityCategory authorityCategory = authorityCategoryService.getOne(id);
        model.addAttribute("authorityCategory", authorityCategory);
        return "authority_category/edit";
    }

    /**
     * 权限分类更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(AuthorityCategory authorityCategory, RedirectAttributes redirectAttributes) {
        if (!isValid(authorityCategory)) {
            return ERROR_VIEW;
        }
        authorityCategoryService.update(authorityCategory);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        AuthorityCategory authorityCategory = authorityCategoryService.delete(ids);

        if (authorityCategory != null) {
            return Message.error("删除失败，请先删除" + authorityCategory.getName() + "下的权限");
        }
        return SUCCESS_MESSAGE;
    }
}
