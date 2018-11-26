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
 * controller - 角色
 *
 * @author Xu minghua 2017/07/21
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Resource
    private AuthorityCategoryService authorityCategoryService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 管理员列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, Model model) {
        Page page = roleService.findAll(pageable);
        model.addAttribute("page", page);
        return "role/list";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("authorityCategoryTree", authorityCategoryService.findAll());
        return "role/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Role role, Long[] authorityIds, RedirectAttributes redirectAttributes) {
        role.setAuthorities(authorityService.findList(authorityIds));
        if (!isValid(role)) {
            return ERROR_VIEW;
        }
        role.setIsSystem(false);
        role.setAdmins(null);
        roleService.save(role);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("role", roleService.getOne(id));
        model.addAttribute("authorityCategoryTree", authorityCategoryService.findAll());
        return "role/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Role role, Long[] authorityIds, RedirectAttributes redirectAttributes) {
        role.setAuthorities(authorityService.findList(authorityIds));
        if (!isValid(role)) {
            return ERROR_VIEW;
        }
        roleService.update(role);
//        ShiroAuthorizationHelper.reloadAuthorizing();
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }


    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        Role role = roleService.delete(ids);

        if (role != null) {
            return Message.error("删除失败，请先修改" + role.getName() + "相关的管理员");
        }
        return SUCCESS_MESSAGE;
    }
}
