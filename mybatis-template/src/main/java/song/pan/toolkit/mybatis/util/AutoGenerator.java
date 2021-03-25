package song.pan.toolkit.mybatis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.NullProgressCallback;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Slf4j
public class AutoGenerator {


    public static void main(String[] args) throws Exception {

        // 如果输入路径没有带上根路径 /， class 的 getResourceAsStream 会在路径前面补上 package 的路径，
        // 所以 class.getResourceAsStream 的当前路径就是 .class 字节码文件的路径
        // 比如 generatorConfig.xml 会变成 song/pan/toolkit/mybatis/util/generatorConfig.xml
        // 然后再调用 ClassLoader 的 getResourceAsStream,
        // 如果输入路径带上了根路径 /， class 的 getResourceAsStream 则会将 classpath 视为根路径, 按照相对路径查找资源
        // 可以通过下面的代码查看 resource 的根路径和当前路径
        URL classRootPath = AutoGenerator.class.getResource("/");
        log.info("class.getResourceAsStream root path: {}", classRootPath);
        URL classCurrentPath = AutoGenerator.class.getResource("");
        log.info("class.getResourceAsStream current path: {}", classCurrentPath);

        // 如果是 classloader.getResourceAsStream 则会直接在 classpath 下面查找资源, classloader 的当前路径就是 classpath
        // 需要注意 classloader 没有根路径 /, 如果在 path 前加上根路径, 会找不到资源, 即使是绝对路径也会报错
        URL classLoaderRootPath = AutoGenerator.class.getClassLoader().getResource("/");
        log.info("ClassLoader.getResourceAsStream root path: {}", classLoaderRootPath);
        URL classLoaderCurrentPath = AutoGenerator.class.getClassLoader().getResource("");
        log.info("ClassLoader.getResourceAsStream current path: {}", classLoaderCurrentPath);


//        InputStream resourceAsStream = AutoGenerator.class.getResourceAsStream("/generatorConfig.xml");
        InputStream resourceAsStream = AutoGenerator.class.getClassLoader().getResourceAsStream("generatorConfig.xml");
        List<String> warnings = new LinkedList<>();
        Configuration config = new ConfigurationParser(warnings).parseConfiguration(resourceAsStream);
        DefaultShellCallback callback = new DefaultShellCallback(true);

        new MyBatisGenerator(config, callback, warnings).generate(new NullProgressCallback());

        log.info("warnings: {}", warnings);
    }

}
