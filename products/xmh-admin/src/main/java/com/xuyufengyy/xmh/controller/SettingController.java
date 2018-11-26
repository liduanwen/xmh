/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.xuyufengyy.xmh.controller;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPSenderFailedException;
import com.xuyufengyy.xmh.Message;
import com.xuyufengyy.xmh.utils.Setting;
import com.xuyufengyy.xmh.utils.SettingUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Controller - 系统设置
 *
 * @author SHOP++ Team
 * @version 3.0
 */
@Controller
@RequestMapping("/setting")
public class SettingController extends BaseController {

//	@Autowired
//	private FileService fileService;
//
//	@Autowired
//	private MailService mailService;
//
//	@Autowired
//	private CacheService cacheService;

    @Resource
    private SettingUtils settingUtils;

    /**
     * 邮件测试
     */
    @RequestMapping(value = "/mail_test", method = RequestMethod.POST)
    public @ResponseBody
    Message mailTest(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail) {
        if (StringUtils.isEmpty(toMail)) {
            return ERROR_MESSAGE;
        }
        Setting setting = settingUtils.get();
        if (StringUtils.isEmpty(smtpPassword)) {
            smtpPassword = setting.getSmtpPassword();
        }
        try {
            if (!isValid(Setting.class, "smtpFromMail", smtpFromMail) || !isValid(Setting.class, "smtpHost", smtpHost) || !isValid(Setting.class, "smtpPort", smtpPort) || !isValid(Setting.class, "smtpUsername", smtpUsername)) {
                return ERROR_MESSAGE;
            }
//			mailService.sendTestMail(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, toMail);
        } catch (MailSendException e) {
            Exception[] messageExceptions = e.getMessageExceptions();
            if (messageExceptions != null) {
                for (Exception exception : messageExceptions) {
                    if (exception instanceof SMTPSendFailedException) {
                        SMTPSendFailedException smtpSendFailedException = (SMTPSendFailedException) exception;
                        Exception nextException = smtpSendFailedException.getNextException();
                        if (nextException instanceof SMTPSenderFailedException) {
                            return Message.error("admin.setting.mailTestSenderFailed");
                        }
                    } else if (exception instanceof MessagingException) {
                        MessagingException messagingException = (MessagingException) exception;
                        Exception nextException = messagingException.getNextException();
                        if (nextException instanceof UnknownHostException) {
                            return Message.error("admin.setting.mailTestUnknownHost");
                        } else if (nextException instanceof ConnectException) {
                            return Message.error("admin.setting.mailTestConnect");
                        }
                    }
                }
            }
            return Message.error("admin.setting.mailTestError");
        } catch (MailAuthenticationException e) {
            return Message.error("admin.setting.mailTestAuthentication");
        } catch (Exception e) {
            return Message.error("admin.setting.mailTestError");
        }
        return Message.success("admin.setting.mailTestSuccess");
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(ModelMap model) {
        model.addAttribute("watermarkPositions", Setting.WatermarkPosition.values());
        model.addAttribute("roundTypes", Setting.RoundType.values());
        model.addAttribute("captchaTypes", Setting.CaptchaType.values());
        model.addAttribute("accountLockTypes", Setting.AccountLockType.values());
        model.addAttribute("stockAllocationTimes", Setting.StockAllocationTime.values());
        model.addAttribute("reviewAuthorities", Setting.ReviewAuthority.values());
        model.addAttribute("consultationAuthorities", Setting.ConsultationAuthority.values());
        model.addAttribute("setting", settingUtils.get());
        return "setting/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Setting setting, MultipartFile watermarkImageFile, RedirectAttributes redirectAttributes) {
        if (!isValid(setting)) {
            return ERROR_VIEW;
        }
        if (setting.getUsernameMinLength() > setting.getUsernameMaxLength() || setting.getPasswordMinLength() > setting.getPasswordMinLength()) {
            return ERROR_VIEW;
        }
        Setting srcSetting = settingUtils.get();
        if (StringUtils.isEmpty(setting.getSmtpPassword())) {
            setting.setSmtpPassword(srcSetting.getSmtpPassword());
        }
//		if (watermarkImageFile != null && !watermarkImageFile.isEmpty()) {
//			if (!fileService.isValid(FileInfo.FileType.image, watermarkImageFile)) {
//				addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
//				return "redirect:edit.jhtml";
//			}
//			String watermarkImage = fileService.uploadLocal(FileInfo.FileType.image, watermarkImageFile);
//			setting.setWatermarkImage(watermarkImage);
//		} else {
//			setting.setWatermarkImage(srcSetting.getWatermarkImage());
//		}
        setting.setCnzzSiteId(srcSetting.getCnzzSiteId());
        setting.setCnzzPassword(srcSetting.getCnzzPassword());
        SettingUtils.set(setting);
//		cacheService.clear();

        OutputStream outputStream = null;
        try {
            org.springframework.core.io.Resource resource = new ClassPathResource("");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            String templateUpdateDelay = properties.getProperty("template.update_delay");
            String messageCacheSeconds = properties.getProperty("message.cache_seconds");
            if (setting.getIsDevelopmentEnabled()) {
                if (!templateUpdateDelay.equals("0") || !messageCacheSeconds.equals("0")) {
                    outputStream = new FileOutputStream(resource.getFile());
                    properties.setProperty("template.update_delay", "0");
                    properties.setProperty("message.cache_seconds", "0");
                    properties.store(outputStream, "SHOP++ PROPERTIES");
                }
            } else {
                if (templateUpdateDelay.equals("0") || messageCacheSeconds.equals("0")) {
                    outputStream = new FileOutputStream(resource.getFile());
                    properties.setProperty("template.update_delay", "3600");
                    properties.setProperty("message.cache_seconds", "3600");
                    properties.store(outputStream, "SHOP++ PROPERTIES");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:edit.jhtml";
    }

}