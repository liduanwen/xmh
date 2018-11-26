package com.xuyufengyy.xmh;


/**
 * 消息
 *
 * @author Xu minghua 2017/07/20
 */
public class Message {
    /**
     * 类型
     */
    public enum Type {

        /** 成功 */
        success,

        /** 警告 */
        warn,

        /** 错误 */
        error
    }

    /** 类型 */
    private Type type;

    /** 内容 */
    private String content;

    /** 消息类型(用于外部) */
    private String messageType;

    /**
     * 初始化一个新创建的 Message 对象，使其表示一个空消息。
     */
    public Message() {

    }

    /**
     * 初始化一个新创建的 Message 对象
     *
     * @param type
     *            类型
     * @param content
     *            内容
     */
    public Message(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    public Type getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type
     *            类型
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * 获取内容
     *
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content
     *            内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取 消息类型
     * @return
     */
    public String getMessageType() {
        return messageType = getType().toString();
    }

    /**
     * 返回成功消息
     *
     * @param content 内容
     * @return 成功消息
     */
    public static Message success(String content) {
        return new Message(Type.success, content);
    }

    /**
     * 返回警告消息
     *
     * @param content 内容
     * @return 警告消息
     */
    public static Message warn(String content) {
        return new Message(Type.warn, content);
    }

    /**
     * 返回错误消息
     *
     * @param content 内容
     * @return 错误消息
     */
    public static Message error(String content) {
        return new Message(Type.error, content);
    }
}
