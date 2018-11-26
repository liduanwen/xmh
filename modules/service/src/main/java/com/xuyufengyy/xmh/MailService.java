/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.xuyufengyy.xmh;


import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service - 邮件
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Service
public class MailService {

	/**
	 * 发送邮件
	 * 
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param async
	 *            是否异步
	 */
	public void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail, String subject, String templatePath, Map<String, Object> model, boolean async){

	}

	/**
	 * 发送邮件
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param async
	 *            是否异步
	 */
	public void send(String toMail, String subject, String templatePath, Map<String, Object> model, boolean async){

	}

	/**
	 * 发送邮件(异步)
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 */
	public void send(String toMail, String subject, String templatePath, Map<String, Object> model){

	}

	/**
	 * 发送邮件(异步)
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 */
	public void send(String toMail, String subject, String templatePath){

	}

	/**
	 * 发送测试邮件
	 * 
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param toMail
	 *            收件人邮箱
	 */
	public void sendTestMail(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail){

	}

//	/**
//	 * 发送找回密码邮件
//	 *
//	 * @param toMail
//	 *            收件人邮箱
//	 * @param username
//	 *            用户名
//	 * @param safeKey
//	 *            安全密匙
//	 */
//	void sendFindPasswordMail(String toMail, String username, SafeKey safeKey);
//
//	/**
//	 * 发送到货通知邮件
//	 *
//	 * @param productNotify
//	 *            到货通知
//	 */
//	void sendProductNotifyMail(ProductNotify productNotify);

}