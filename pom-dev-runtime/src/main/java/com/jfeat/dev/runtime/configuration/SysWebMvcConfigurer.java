package com.jfeat.dev.runtime.configuration;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.Charset;
import java.util.List;


@Configuration
@EnableSwagger2
public class SysWebMvcConfigurer extends WebMvcConfigurationSupport {
    public SysWebMvcConfigurer() {
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//classpath对应resources目录  file对应项目目录
        //registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");
        //file 相对路径  此处attachments对应 项目文件夹下的attachments
        // registry.addResourceHandler(new String[]{"/**"}).addResourceLocations(new String[]{"classpath:/static/"});
        // registry.addResourceHandler("/images/**").addResourceLocations("file:./images/");
        // registry.addResourceHandler("/attachments/**").addResourceLocations("file:./attachments/");
        // registry.addResourceHandler("/snapshots/**").addResourceLocations("file:./snapshots/");
        //registry.addResourceHandler("/dev/logs/dbsnapshot/**").addResourceLocations("file:./.dbsnapshot/");

        // swagger-ui.html
        registry.addResourceHandler(new String[]{"/swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});

        // dependency-ui.html
        registry.addResourceHandler("dependency-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/dependency-ui/**").addResourceLocations("classpath:/META-INF/resources/dependency-ui/");

        // logs-ui.html
        registry.addResourceHandler("logs-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/logs-ui/**").addResourceLocations("classpath:/META-INF/resources/logs-ui/");

        registry.addResourceHandler("nav-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/nav-ui/**").addResourceLocations("classpath:/META-INF/resources/nav-ui/");

        registry.addResourceHandler("connection-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/connection-ui/**").addResourceLocations("classpath:/META-INF/resources/connection-ui/");
    }


    /**返回的json格式配置**/
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //调用父类的配置
        super.configureMessageConverters(converters);
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                //    输出key是包含双引号
//                SerializerFeature.QuoteFieldNames,
                //    是否输出为null的字段,若为null 则显示该字段
                SerializerFeature.WriteMapNullValue,
                //    数值字段如果为null，则输出为0
                // SerializerFeature.WriteNullNumberAsZero,
                //     List字段如果为null,输出为[],而非null
                // SerializerFeature.WriteNullListAsEmpty,
                //    字符类型字段如果为null,输出为"",而非null
                // SerializerFeature.WriteNullStringAsEmpty,
                //    Boolean字段如果为null,输出为false,而非null
                // SerializerFeature.WriteNullBooleanAsFalse,
                //    Date的日期转换器
                SerializerFeature.WriteDateUseDateFormat,
                //    循环引用
                // SerializerFeature.DisableCircularReferenceDetect,
        };

        ValueFilter valueFilter = (object, name, value) -> {return value instanceof Long ? value.toString() : value;};
        fastJsonConfig.setSerializeFilters(new SerializeFilter[]{valueFilter});

        fastJsonConfig.setSerializerFeatures(serializerFeatures);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));

        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.extendMessageConverters(converters);

        System.out.println("--------------------------MVC已注册的消息转换器-------------------");
        for (HttpMessageConverter<?> messageConverter : converters) {
            System.out.println(messageConverter);
        }
        System.out.println("----------------------------------------------------------");
    }
}

