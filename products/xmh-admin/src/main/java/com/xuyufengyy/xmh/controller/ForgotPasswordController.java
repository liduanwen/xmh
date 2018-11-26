package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.Admin;
import com.xuyufengyy.xmh.AdminService;
import com.xuyufengyy.xmh.redis.Redis;
import com.xuyufengyy.xmh.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * controller - 忘记密码
 *
 * @author Xu minghua 2017/08/03
 */
@Controller
@RequestMapping(value = "/reset_password")
public class ForgotPasswordController extends BaseController {

    /**
     * 提示信息
     */
    private static final String ERROR_MESSAGE = "重置密码链接失效，请重新找回密码。";
    private static final String SUCCESS_MESSAGE = "重置密码成功，请返回登录。";

    @Resource
    private AdminService adminService;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private Redis redis;

    @Value(value = "${mail.from}")
    private String mailFrom;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 检查email是否存在
     */
    @RequestMapping(value = "/check_email", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean checkUsername(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        if (adminService.emailExists(email)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 填写email
     */
    @RequestMapping(value = "/add_email", method = RequestMethod.GET)
    public String addEmail() {
        return "reset_password/add_email";
    }

    /**
     * 发送邮件
     */
    @RequestMapping(value = "/send_email", method = RequestMethod.POST)
    public String sendEmail(String email, RedirectAttributes redirectAttributes) {
        if (StringUtils.isBlank(email)) {

        }

        try {
            Admin admin = adminService.findByEmail(email);
            if (admin == null) {
                redirectAttributes.addFlashAttribute("message", "请注册管理员账号。");
                return "redirect:add_email";
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(mailFrom);//发送者.
            mimeMessageHelper.setTo(email);//接收者.
            mimeMessageHelper.setSubject("[" + admin.getName() + "]找回您的帐户密码");//邮件主题.

            List<String> list = new ArrayList<String>();

            list.add(admin.getUsername());
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());//时间戳
            String url = "/reset_password/set_new_password?action=resetpassword&username=" + admin.getUsername() + "&active=" + currentTimeMillis;
            list.add("http://192.168.17.10:8086" + url);
            list.add("http://192.168.17.10:8086" + url);
            list.add("10");
            list.add(DateUtils.toString(org.apache.commons.lang3.time.DateUtils.addMinutes(new Date(), 10), "yyyy-MM-dd HH:mm:ss"));

            String content = "<html>" +
                    "<head></head>" +
                    "<body>" +
                    "<p></p>" +
                    "<p>亲爱的用户 %s：您好！</p>" +
                    "<p></p>" +
                    "<p style=\"text-indent: 2em;\">您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。</p>" +
                    "<p></p>" +
                    "<p style=\"text-indent: 2em;\">要使用新的密码, 请使用以下链接启用密码。</p>" +
                    "<p></p>" +
                    "<p style=\"text-indent: 2em;\"><a href=\"%s\"/>%s</p>" +
                    "<p></p>" +
                    "<p style=\"text-indent: 2em;\">(如果无法点击该URL链接地址，请将它复制并粘帖到浏览器的地址输入框，然后单击回车即可。该链接使用后将立即失效。)</p>" +
                    "<p></p>" +
                    "<p style=\"text-indent: 2em;\">注意:请您在收到邮件%s分钟内(%s前)使用，否则该链接将会失效。</p>" +
                    "</body>" +
                    "</html>";

            mimeMessageHelper.setText(messageTemplate(content, list), true);//邮件内容.

            mailSender.send(mimeMessage);//发送邮件

            redis.save(admin.getUsername(), url, 600, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("message", "您的申请已提交成功，请查看您的邮箱。");
        return "redirect:prompt";
    }

    /**
     * 设置新密码
     */
    @RequestMapping(value = "/set_new_password", method = RequestMethod.GET)
    public String setNewPassword(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        String username = request.getParameter("username");
        if (StringUtils.isBlank(username)) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:add_email";
        }

        if (!redis.isExists(username, 0)) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:add_email";
        }

        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:add_email";
        }

        String servletPath = request.getServletPath();
        String parameter = request.getQueryString();
        String value = servletPath + "?" + parameter;
        String val = redis.getData(username, 0);

        if (!value.equals(val)) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:add_email";
        }

        redis.save(username, admin.getId().toString(), 600, 1);
        model.addAttribute("username", username);
        return "reset_password/password";
    }

    /**
     * 保存新密码
     */
    @RequestMapping(value = "/save_new_password", method = RequestMethod.POST)
    public String saveNewPassword(Admin admin, RedirectAttributes redirectAttributes) {
        String username = admin.getUsername();

        if (!redis.isExists(username, 1)) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:prompt";
        }

        Admin pAdmin = adminService.findByUsername(username);
        if (pAdmin == null) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:add_email";
        }
        Long id = pAdmin.getId();
        if (!redis.getData(username, 1).equals(id.toString())) {
            redirectAttributes.addFlashAttribute("message", ERROR_MESSAGE);
            return "redirect:add_email";
        }

        adminService.update(id, passwordEncoder.encode(admin.getPassword()));

        if (redis.isExists(username, 0)) {
            redis.remove(username, 0);
        }
        if (redis.isExists(username, 1)) {
            redis.remove(username, 1);
        }
        redirectAttributes.addFlashAttribute("message", SUCCESS_MESSAGE);
        return "redirect:prompt";
    }

    /**
     * 操作完成
     */
    @RequestMapping(value = "/prompt", method = RequestMethod.GET)
    public String prompt() {
        return "reset_password/prompt";
    }

    /**
     * 查询消息模版中需要替换的参数个数
     *
     * @param content 消息模版
     * @return
     */
    public int stringNumbers(String content) {
        int counter = 0;
        while (content.indexOf("%s") != -1) {
            counter++;
            content = content.substring(content.indexOf("%s") + 2);
        }
        return counter;
    }

    /**
     * 将模版中的%s全部替换掉生成新的模版
     *
     * @param content          原始模版
     * @param replaceParameter 需要替换的参数值集合
     * @return
     */
    public String messageTemplate(String content, List<String> replaceParameter) {
        if (replaceParameter != null && !replaceParameter.isEmpty()) {
//            if(stringNumbers(content) != replaceParameter.size()){
//                throw new ServiceException("replaceParameter size error", ErrorCode.BAD_REQUEST);
//            }
            for (String str : replaceParameter) {
                content = content.replaceFirst("%s", str);
            }
        }
        return content;
    }
}
