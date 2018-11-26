package com.xuyufengyy.xmh.dto.in;

public class HomeIn extends PageIn {

    /** 当前城市 */
    private String cityName;

    /** 景区名称 */
    private String name;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
