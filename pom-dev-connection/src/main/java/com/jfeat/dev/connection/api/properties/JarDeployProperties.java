package com.jfeat.dev.connection.api.properties;

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
@ConfigurationProperties(prefix = "jarDependency")
public class JarDeployProperties {
    /**
     * 是否加密，用于生产环境
     */
    String signatureOpt;

    public String getSignatureOpt(){
        return this.signatureOpt;
    }
    public void setSignatureOpt(String opt){
        this.signatureOpt = opt;
    }

}