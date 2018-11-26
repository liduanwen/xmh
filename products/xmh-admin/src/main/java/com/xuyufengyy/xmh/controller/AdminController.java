package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.Admin;
import com.xuyufengyy.xmh.AdminService;
import com.xuyufengyy.xmh.BaseEntity.Save;
import com.xuyufengyy.xmh.Message;
import com.xuyufengyy.xmh.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * controller - 管理员
 *
 * @author Xu minghua 2017/07/19
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BaseController {

    @Resource
    private AdminService adminService;

    @Resource
    private RoleService roleService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, Model model) {
        Page page = adminService.findAll(pageable);
        model.addAttribute("page", page);
        return "admin/list";
    }

    /**
     * 检查用户名是否存在
     */
    @RequestMapping(value = "/check_username", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        if (adminService.usernameExists(username)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
        admin.setRoles(roleService.findList(roleIds));
//        admin.setSalt(MD5.generateSalt());
        if (!isValid(admin, Save.class)) {
            return ERROR_VIEW;
        }
        if (adminService.usernameExists(admin.getUsername())) {
            return ERROR_VIEW;
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setIsLocked(false);
        admin.setLoginFailureCount(0);
        admin.setLockedDate(null);
        admin.setLoginDate(null);
        admin.setLoginIp(null);
        adminService.save(admin);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }


    /**
     * 编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String add(@PathVariable("id") Long id, Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("admin", adminService.getOne(id));
        return "admin/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
        admin.setRoles(roleService.findList(roleIds));
        if (!isValid(admin)) {
            return ERROR_VIEW;
        }
        adminService.update(admin, "username", "salt", "password", "isLocked", "loginFailureCount", "lockedDate", "loginDate", "loginIp");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids.length >= adminService.count()) {
            return Message.error("删除失败，必须至少保留一项");
        }
        adminService.delete(ids);
        return SUCCESS_MESSAGE;
    }
}
