package com.jfeat.dev.logs.service.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 存储桶关键属性配置
 *
 * @author vincent
 * @date 2021-08-20
 */
@Data
@Component
@Accessors(chain = true)
@ConfigurationProperties(prefix = "dev.logs")
public class LogsProperties {
    /**
     * 容器内应用路径
     */
    String rootPath;

    /**
     * 是否加密，用于生产环境 [enable, disable]
     */
    String signatureOpt;


    public String getRootPath(){
        return this.rootPath;
    }
    public void setRootPath(String rootPath){
        this.rootPath = rootPath;
    }
    public String getSignatureOpt(){
        return this.signatureOpt;
    }
    public void setSignatureOpt(String opt){
        this.signatureOpt = opt;
    }
}