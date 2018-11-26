/**
 * @(#)LoggerInterceptor.java Description:
 * Version :	1.0
 * Copyright:	Copyright (c) 苗方清颜 版权所有
 */
package com.xuyufengyy.xmh.aop;

import com.xuyufengyy.xmh.AdminLog;
import com.xuyufengyy.xmh.AdminLogService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Aop - 管理平台操作日志
 *
 * @author Xu minghua 2017/07/25
 * @version 1.0
 */
@Aspect//定义为切面类
@Component
@Order(5)
public class AdminLoggerInterceptor {

    private Logger logger = LoggerFactory.getLogger(AdminLoggerInterceptor.class);

    /**
     * 默认忽略参数
     */
    private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[]{"password", "rePassword", "currentPassword"};

    /**
     * 忽略参数
     */
    private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

    @Autowired
    private AdminLogService adminLogService;

    /**
     * 定义拦截规则：拦截com.xuyufengyy.growup.controller包下面的所有类中
     */
    @Pointcut("execution(public * com.xuyufengyy.xmh.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 前置通知
     * 在切入点开始处切入内容
     */
    @Before(value = "webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) { //socket 获取attributes为null
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String servletPath = request.getServletPath();

        StringBuffer parameter = new StringBuffer();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String parameterName = entry.getKey();
                if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
                    String[] parameterValues = entry.getValue();
                    if (parameterValues != null) {
                        for (String parameterValue : parameterValues) {
                            parameter.append(parameterName + " = " + parameterValue + "\n");
                        }
                    }
                }
            }
        }

//        logger.info(classMethod + ": " + servletPath + ":" + " ==in parameter== " + parameter.toString());

        //保存入日志
        AdminLog adminLog = new AdminLog();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser") && authentication.isAuthenticated()) {
            adminLog.setOperator(authentication.getName());
        }
        adminLog.setIp(ip);
        adminLog.setClassMethod(classMethod);
        adminLog.setRequestUrl(servletPath);
        adminLog.setParameter(parameter.toString());

        adminLogService.save(adminLog);

        //菜单
        String menuId = request.getParameter("menuId");
        if (StringUtils.isNotBlank(menuId)) {
            request.getSession().setAttribute("menuId", menuId);
        }
    }

    /**
     * 设置忽略参数
     *
     * @return 忽略参数
     */
    public String[] getIgnoreParameters() {
        return ignoreParameters;
    }

    /**
     * 设置忽略参数
     *
     * @param ignoreParameters 忽略参数
     */
    public void setIgnoreParameters(String[] ignoreParameters) {
        this.ignoreParameters = ignoreParameters;
    }
}
