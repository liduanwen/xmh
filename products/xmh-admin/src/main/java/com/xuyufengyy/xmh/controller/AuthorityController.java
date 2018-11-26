package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.*;
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
 * controller - 权限
 *
 * @author Xu minghua 2017/07/19
 */
@Controller
@RequestMapping(value = "/authority")
public class AuthorityController extends BaseController {

    @Resource
    private AuthorityService authorityService;

    @Resource
    private AuthorityCategoryService authorityCategoryService;

    /**
     * 权限列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, Model model) {
        Page page = authorityService.findAll(pageable);
        model.addAttribute("page", page);
        return "authority/list";
    }

    /**
     * 权限添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("authorityCategoryTree", authorityCategoryService.findAll());
        return "authority/add";
    }

    /**
     * 权限保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Authority authority, Long authorityCategoryId, RedirectAttributes redirectAttributes) {
        AuthorityCategory authorityCategory = authorityCategoryService.getOne(authorityCategoryId);
        authorityService.save(authorityCategory, authority);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 权限编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("authorityCategoryTree", authorityCategoryService.findAll());
        model.addAttribute("authority", authorityService.getOne(id));
        return "authority/edit";
    }

    /**
     * 权限更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Authority authority, Long authorityCategoryId, RedirectAttributes redirectAttributes) {
        if (!isValid(authority)) {
            return ERROR_VIEW;
        }
        authorityService.update(authority, authorityCategoryId);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        Authority authority = authorityService.delete(ids);

        if (authority != null) {
            return Message.error("删除失败，请先修改" + authority.getName() + "相关的角色");
        }
        return SUCCESS_MESSAGE;
    }
}
