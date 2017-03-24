package com.github.yanglikun.autoloadproperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;


/**
 * Created by yanglikun on 2017/3/19.
 */
public class PropertiesAutoLoadRunListener implements SpringApplicationRunListener, Ordered {

    private Log logger = LogFactory.getLog(getClass());
    ;

    private final SpringApplication application;

    private final String[] args;

    private ImportProperties importProperties;

    private Set<String> propertyLocations;

    public PropertiesAutoLoadRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        resolveImportProperties();
        if (!isImportPropertiesPresent()) return;

        resolvePropertyLocations();

        try {
            logger.info("自动加载properties文件-开始:" + propertyLocations.stream().collect(joining(",")));
            ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
            List<Resource> resources = propertyLocations.stream().flatMap(location -> {
                try {
                    return Stream.of(resourceResolver.getResources(location));
                } catch (IOException e) {
                    throw new RuntimeException("自动加载properties文件-异常:" + location, e);
                }
            }).collect(toList());

            for (Resource resource : resources) {
                String filePath = resource.getURI().getPath();
                logger.info("自动加载properties文件:" + filePath);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                if (!properties.isEmpty()) {
                    context.getEnvironment().getPropertySources().addLast(new PropertiesPropertySource(filePath,
                            properties));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("自动加载properties文件-异常", e);
        }
        logger.info("自动加载properties文件-完成");
    }

    private void resolvePropertyLocations() {
        propertyLocations = Stream.of(importProperties.locations()).collect(toSet());
    }

    private void resolveImportProperties() {
        if (application == null) {
            logger.info("自动加载properties文件-application为null");
        }

        Class<?> mainApplicationClass = application.getMainApplicationClass();
        if (mainApplicationClass == null) {
            logger.info("自动加载properties文件-没有获取到mainApplicationClass");
        }

        ImportProperties importProperties = mainApplicationClass.getAnnotation(ImportProperties.class);
        if (importProperties == null) {
            logger.info("自动加载properties文件-" +
                    "在类(" + mainApplicationClass.getName() + ")上没找到ImportProperties注解");
        }

        this.importProperties = importProperties;
    }

    private boolean isImportPropertiesPresent() {
        return importProperties != null;
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
