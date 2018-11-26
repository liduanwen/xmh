package com.xuyufengyy.xmh.ueditor;

/**
 * Ueditor 返回给Ueditor
 *
 * @author Xu minghua 2017/08/12
 */
public class State {

    private String state = "";

    private String original = "";

    private String type = "";

    private String url = "";

    private Integer size = 0;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
