package com.newconf.mqttclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: mqtt连接信息类
 * @project: VoipConfServer
 * @date: 2024/7/3 17:36
 * @author: hhhhhtao
 */
@EnableAutoConfiguration
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfiguration {
    /**
     * 端口
     */
    private String brk;
    /**
     * 用户名
     */
    private String usr;
    /**
     * 密码
     */
    private String pw;
    /**
     * 主题
     */
    private String tp;

    public String getBrk() {
        return ep;
    }

    public void setBrk(String brk) {
        this.brk = brk;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }
}
