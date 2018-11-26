package com.xuyufengyy.xmh.controller;

import com.google.gson.Gson;
import com.xuyufengyy.xmh.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 瞬时消息
 *
 * @author Xu minghua 2017/7/20
 */
public class BaseController {

    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 错误视图
     */
    protected static final String ERROR_VIEW = "common/error";

    /**
     * 错误消息
     */
    protected static final Message ERROR_MESSAGE = Message.error("操作失败！");

    /**
     * 成功消息
     */
    protected static final Message SUCCESS_MESSAGE = Message.success("操作成功！");

    /**
     * "瞬时消息"属性名称
     */
    public final String FLASH_MESSAGE_ATTRIBUTE_NAME = "flashMessage";

    /**
     * "验证结果"参数名称
     */
    private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";

    @Autowired
    private Validator validator;

    protected Gson gson = new Gson();

    /**
     * 数据验证
     *
     * @param target 验证对象
     * @param groups 验证组
     * @return 验证结果
     */
    protected boolean isValid(Object target, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target, groups);
        if (constraintViolations.isEmpty()) {
            return true;
        } else {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
            return false;
        }
    }

    /**
     * 数据验证
     *
     * @param type     类型
     * @param property 属性
     * @param value    值
     * @param groups   验证组
     * @return 验证结果
     */
    protected boolean isValid(Class<?> type, String property, Object value, Class<?>... groups) {
        Set<?> constraintViolations = validator.validateValue(type, property, value, groups);
        if (constraintViolations.isEmpty()) {
            return true;
        } else {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
            return false;
        }
    }

    /**
     * 图片格式验证
     *
     * @param fileName 图片名
     * @return 验证结果
     */
    protected boolean isValidImg(String fileName) {
        String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.PNG|.png)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(fileName.toLowerCase());
        return matcher.find();
    }

    /**
     * 添加瞬时消息
     *
     * @param redirectAttributes RedirectAttributes
     * @param message            消息
     */
    protected void addFlashMessage(RedirectAttributes redirectAttributes, Message message) {
        if (redirectAttributes != null && message != null) {
            redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ATTRIBUTE_NAME, message);
        }
    }
}
