# spring boot 正则模式加载属性文件(properites)
# jdk 1.8 
# 使用方式

1、引入工程依赖
   
2、在标注有@SpringBootApplication注解的上面添加@ImportPropertie注解
```java
@SpringBootApplication
@ImportResource({"classpath:config/spring*.xml", "classpath:spring*.xml"})
@ImportProperties(locations = {"classpath:spring-autoload-*.properties"})
```



