package song.pan.toolkit.spring.boot.template.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 如果在 {@link EnableConfigurationProperties} 添加了这个类，就不用再使用 {@link Component} 注入进容器了
 */
//@Component
@Getter
@Setter
/**
 * 注意 {@link ConfigurationProperties} 属性注入是配合 get/set 方法使用的
 */
@ConfigurationProperties(prefix = "application.properties")
public class AppProperties {

    private String name;

    private String version;

}
