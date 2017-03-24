# let spring boot autoload property files with  wildcards
(spring boot 支持通配符方式加载配置文件)
# jdk 1.8 (需要jdk 1.8)
# how to use(如何使用)


1、import project
   

2、add  `@ImportPropertie` on the class which has  `@SpringBootApplication`
```java
@SpringBootApplication
@ImportResource({"classpath:config/spring*.xml", "classpath:spring*.xml"})
@ImportProperties(locations = {"classpath:spring-autoload-*.properties"})
```



