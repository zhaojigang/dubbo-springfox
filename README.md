# dubbo-springfox
extent springfox to support generate dubbo-api document 扩展springfox支持dubbo接口的文档化与图形化调用

> github上有些图片丢掉了，所以我在简书上也把这篇文章贴了一下 https://www.jianshu.com/p/31dd7e329f4d

* 一、dubbo-springfox特性简介
* 二、dubbo-springfox入门案例
* 三、dubbo-springfox基本原理
* 附1、dubbo-springfox注解附表
* 附2、Q&A

## 一、dubbo-springfox特性简介
```
dubbo-springfox的两个主要特点：
（1）动态文档
（2）dubbo接口的图形化调用
```
### 1.1 动态文档
dubbo-springfox基于SpringFox进行开发，将文档以注解的形式优雅的集成到代码中；而且，当接口参数发生变化时，也会在UI界面上动态的进行同步。
### 1.2 dubbo接口的图形化调用
dubbo-springfox在服务启动的时候将```<dubbo:service>```接口扫描出来，并且动态生成对应的Http接口，并且以漂亮的格式展示在一个UI界面上（包括文档说明），开发人员与测试人员可以非常方便的进行接口测试。

说明：由于dubbo-springfox是基于SpringFox开发的，所以也天然的支持SpringMVC接口文档的动态生成与图形化调用。
## 二、dubbo-springfox入门案例
项目以maven多模块形式进行组织：

* dubbo-springfox-core是核心代码，提供jar包，供使用；
* dubbo-springfox-demo-springboot提供了在使用springboot的情况下怎样使用dubbo-springfox；
* dubbo-springfox-demo-springmvc提供了在使用springmvc的情况下怎样使用dubbo-springfox。

以dubbo-springfox-demo-springboot为例来看dubbo-springfox的使用姿势。
### 2.1 构建增强版的springfox到nexus或者本地maven仓库
dubbo-springfox基于增强版的springfox：[https://github.com/zhaojigang/springfox](https://github.com/zhaojigang/springfox)。所以需要首先编译发布增强版的springfox（注意：需要将版本改为6.6.6）
增强版的springfox，相较于原本的springfox：
* 添加了自定义注解@RequestModel（用来打破@RequestBody单体限制）
* 让springfox内核去支持@RequestModel注解
* 获取swagger-ui的位置改成了：[https://github.com/zhaojigang/swagger-ui](https://github.com/zhaojigang/swagger-ui)，增强版的swagger-ui支持@RequestModel注解的参数解析和构造，支持axios进行ajax调用

可以从[这里](https://pan.baidu.com/s/1r5hQ9fnILEM-nHWgZTOI-A)直接获取的springfox的相关jar包，之后上传到nexus或者本地maven仓库即可。

### 2.2 引入dubbo-springfox jar包
```
        <dependency>
            <groupId>io.hulk</groupId>
            <artifactId>dubbo-springfox-core</artifactId>
            <version>1.0.0</version>
        </dependency>
```
### 2.3 在启动类中添加启动注解与扫描范围
```
@SpringBootApplication
@EnableSwagger2
@ImportResource({ "classpath:*.xml" })
@ComponentScan({ "io.hulk.dubbo.springfox.demo.springboot", "io.hulk.dubbo.springfox.core" })
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
说明：

* 添加了```@EnableSwagger2```注解：用于启动Swagger。
* 在@ComponentScan注解中，添加```"io.hulk.dubbo.springfox.core"```，扫描dubbo-springfox中的相关Bean。（在实际开发中，springboot的启动类会放在基包下，基包通常会命名为groupId.artifactId，当然，其中的中划线会去掉；此时，默认@ComponentScan会扫描基包下的所有类，所以不加入@ComponentScan也行，但是此处由于需要扫描dubbo-springfox中的相关Bean，所以这里的@ComponentScan需要扫描两个包：基包 + ```"io.hulk.dubbo.springfox.core"```）

对于如果只是想进行在UI界面上调用dubbo和springmvc接口的需要来讲，到这里就ok了！！！就是这么简单。

```
使用方式总结：引入一个jar包 + 添加一个启动注解 + 添加一个扫描范围注解
```
看一下dubbo-springfox-demo-springboot类组织图：

 ![Alt pic](http://chuantu.biz/t6/346/1532152288x-1404792235.png) 

说明：

* BookService：Dubbo服务接口；在该接口中，提供了在如下情景下的dubbo-springfox测试用例；
	* 测试基础类型、对象类型、默认值设置、是否必须
	* 测试一层泛型
	* 测试嵌套泛型
	* 测试Map
	* 测试方法重载
	* 测试入参和返回值为空
	* 测试Long型
	* 测试接口抛异常
* BookServiceImpl：BookService的实现类；
* UserController：controller接口；
* Book和User：两个基本的model类。

此处为了简便，仅以BookService中的一个接口为例来看Dubbo接口的图像化调用，以UserController中的一个方法为例来看springmvc方法的图形化调用。
#### BookService
```
public interface BookService {
    String testGenericField(List<Book> books);
}
```
#### BookServiceImpl
```
public class BookServiceImpl implements BookService {
    @Override
    public String testGenericField(List<Book> books) {
        if (!CollectionUtils.isEmpty(books)) {
            return books.get(0).getTitle();
        }
        return "书籍列表为空";
    }
}
```
#### Book
```
public class Book {
    private Long   id;
    private String title;
    private String content;
    ...省略setter和getter
}
```
#### UserController
```
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/testCommonField2", method = RequestMethod.POST)
    public String testCommonField2(@RequestParam("name") String name,
                                   @RequestBody Map<String, Book> bookMap) {
        return name;
    }
}
```
启动服务后，访问：本机ip:port/swagger-ui.html。就可以看到下图（这里只看BookService的，UserController的同理）：

 ![Alt pic](http://chuantu.biz/t6/346/1532152412x-1566661044.png) 

### 2.3 接口文档
上述只是完成了接口的图形化调用功能，下面来看一下接口文档的生成。
#### BookService
```
@Api("Book相关的Dubbo服务")
public interface BookService {
    @ApiOperation("测试一层泛型")
    String testGenericField(@ApiParam List<Book> books);
}
```
说明：

* 在接口上添加注解@Api描述该接口作用；
* 在方法上添加注解@ApiOperation描述该接口作用；
* 在参数上添加@ApiParam对参数进行描述；

#### BookServiceImpl
```
public class BookServiceImpl implements BookService {
    @Override
    public String testGenericField(List<Book> books) {
        if (!CollectionUtils.isEmpty(books)) {
            return books.get(0).getTitle();
        }
        return "书籍列表为空";
    }
}
```
#### Book
```
@ApiModel("书籍")
public class Book {
    @ApiModelProperty("ID")
    private Long   id;
    @ApiModelProperty("书标题")
    private String title;
    @ApiModelProperty("书的内容")
    private String content;
    ...省略setter和getter
}
```
说明：

* 在模型上添加注解@ApiModel描述该模型作用；
* 在属性上添加注解@ApiModelProperty描述该属性作用。

#### UserController
```
@Api(tags = "user相关api")
@RestController
@RequestMapping("/user")
public class UserController {
	 @ApiOperation("普通2")
    @RequestMapping(value = "/testCommonField2", method = RequestMethod.POST)
    public String testCommonField2(@RequestParam("name") String name,
                                   @RequestBody Map<String, Book> bookMap) {
        return name;
    }
}
```

注意：

* 以上的注解都不强制添加，根据需求自行添加。注解相互之间并无影响。
* Dubbo服务的注解只能添加在接口上，不支持添加在实现上：因为对外的model通常会写在xxx-api中，所以在接口上添加注解是必须支持的；其次接口上添加注解描述，也可以替代javadoc。
* dubbo-springfox支持的注解附表见附录

之后再启动服务看一下UI图：

 ![Alt pic](http://chuantu.biz/t6/346/1532152462x-1566661044.png) 

其中Long型为什么展示为int64而非Long，这与js对Long型的是否支持有关系，有兴趣的同学可以自行谷歌。

## 三、dubbo-springfox基本原理
dubbo-springfox基于SpringFox实现，SpringFox只实现了对springmvc方法的扫描；dubbo-springfox在此基础上增加了对Dubbo接口的扫描。为了最小化的实现，只需要在服务启动的时候动态的生成Dubbo接口对应的controller，之后由SpringFox的内核去统一扫描所有的controller即可。

```
关于SpringFox和Swagger，可自行去谷歌。
```
流程图如下所示：

 ![Alt pic](http://chuantu.biz/t6/346/1532152493x-1566661044.png) 

### 3.1 dubbo-springfox扫描接口的时机
编写一个流程主控类：ApiDocumentBootstrap

```
@Configuration
public class ApiDocumentBootstrap implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiDocumentBootstrap.class);

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        try {
            /** 1 获取dubboXmlApi扫描器 */
            final ApiScanner<ApplicationContext, Map<String, ServiceBean>> dubboXmlApiScanner = new XmlDubboApiScanner();

            /** 2 使用dubboXmlApi扫描器从ApplicationContext中获取ServiceBean（dubbo:service配置）*/
            final Map<String, ServiceBean> serviceBeanMap = dubboXmlApiScanner.scanFromSpringContext(context);

            /** 3 获取JdkCompiler实例 */
            final JdkCompiler jdkCompiler = new JdkCompiler();

            String serviceGroup = "";

            for (ServiceBean sb : serviceBeanMap.values()) {

                /** 4 根据ServiceBean的相关信息生成dubbo:service对应的controller类字符串 */
                final String code = ClassCodeGenerator.generateClassCode(sb);

                /** 5 使用JdkCompiler实例编译加载controller类字符串为Class对象 */
                final Class<?> dubboControllerClazz = jdkCompiler.compile(code,
                    ApiDocumentBootstrap.class.getClassLoader());

                /** 6 将生成的controller类Class对象注册到BeanDefinitionRegistry中，接下来供mvc扫描器进行扫描 */
                SpringHelper.registerBeanDefinition(dubboControllerClazz, context);

                serviceGroup = serviceGroup == "" ? sb.getGroup() : serviceGroup;
            }

            /**
             * 7 设置系统属性
             */
            System.setProperty(DubboSpringfoxContants.SERVICE_GROUP, serviceGroup == null ? "" : serviceGroup);
        } catch (Exception e) {
            LOGGER.error("dubbo-springfox generate api-document error, msg：", e);
        }
    }
}
```
该类实现了ApplicationContextAware的setApplicationContext(ApplicationContext context)方法，该方法实现流程图中的所有步骤。setApplicationContext(ApplicationContext context)方法发生在Spring容器创建后 + springmvc容器扫描controller之前 + 可以获取到ApplicationContext；

而以xml形式进行配置的Dubbo的每一个服务接口都会生成一个ServiceBean，最后放置在ApplicationContext中；这样，我们就可以通过在setApplicationContext(ApplicationContext context)中获取到的ApplicationContext中去获取所有的ServiceBean；

### 3.2 获取所有的ServiceBean
定义扫描器接口：方便后需扩展，扫描注解形式的Dubbo服务等。

```
public interface ApiScanner<T, R> {
    R scanFromSpringContext(T t);
}
```
Xml形式的扫描器：

```
public class XmlDubboApiScanner implements ApiScanner<ApplicationContext, Map<String, ServiceBean>> {
    @Override
    public Map<String, ServiceBean> scanFromSpringContext(ApplicationContext context) {
        if (context == null) {
            return new HashMap<>(0);
        }
        return context.getBeansOfType(ServiceBean.class);
    }
}
```
从ApplicationContext中获取所有类型是ServiceBean的Bean。

### 3.3 创建JdkCompiler编译器
JdkCompiler继承于com.alibaba.dubbo.common.compiler.support.AbstractCompiler，Dubbo为该抽象类提供了两个实现：com.alibaba.dubbo.common.compiler.support.JavassistCompiler和com.alibaba.dubbo.common.compiler.support.JdkCompiler。前者不具有通用性，与Dubbo中构造动态类的写法强耦合；后者在编译参数中没有打印本地变量表，导致无法直接获取方法参数名。

dubbo-springfox的JdkCompiler与com.alibaba.dubbo.common.compiler.support.JdkCompiler代码几乎相同，只是添加了编译参数"-g"，打印本地变量表，后续就可以获取方法参数名。不使用Javassist的原因是因为Javassist要逐行进行构造代码或者像com.alibaba.dubbo.common.compiler.support.JavassistCompiler那样将构造好的完整代码进行拆解构造，比较麻烦。

具体代码见：
```
io.hulk.dubbo.springfox.core.compiler.JdkCompiler
```

### 3.4 使用ClassCodeGenerator生成DubboService的controller代码
```
public class ClassCodeGenerator {

    public static String generateClassCode(ServiceBean sb) throws NoSuchMethodException, ClassNotFoundException {
        StringBuilder codeBuilder = new StringBuilder();

        /**
         * 1 获取接口类和实现类
         */
        final Class<?> dubboInterface = sb.getInterfaceClass();
        final Class<?> rawDubboInterfaceImpl = sb.getRef().getClass();
        Class<?> dubboInterfaceImpl = rawDubboInterfaceImpl;

        String dubboInterfaceImplCanonicalName = rawDubboInterfaceImpl.getCanonicalName();
        // 保护性判断：防止dubbo接口实现类被代理，这里转换为真实类
        if (dubboInterfaceImplCanonicalName.contains("$$")) {
            dubboInterfaceImpl = ClassLoader.getSystemClassLoader()
                .loadClass(dubboInterfaceImplCanonicalName.substring(0, dubboInterfaceImplCanonicalName.indexOf("$$")));
        }

        /**
         * 2 获取包名
         */
        final String packageName = dubboInterfaceImpl.getPackage().getName();
        // 保护性措施：包名尾部添加DubboApi，防止被意外AOP
        codeBuilder.append("package ").append(packageName).append("DubboApi;\n");

        /**
         * 4 获取重组@Api注解
         */
        final Api api = dubboInterface.getAnnotation(Api.class);
        if (api != null) {
            String apiTag = api.value() != "" ? api.value() : api.tags()[0];
            codeBuilder.append("@io.swagger.annotations.Api(tags = \"" + apiTag + "\")").append("\n");
        }

        /**
         * 5 创建@RestController @RequestMapping注解
         */
        codeBuilder.append("@org.springframework.web.bind.annotation.RestController\n")
            .append("@org.springframework.web.bind.annotation.RequestMapping(\"/dubbo-api\")\n");

        /**
         * 6 创建类定义
         * public class BookServiceImplDubboApi
         */
        String classDefinition = dubboInterfaceImpl.toGenericString();
        codeBuilder.append(classDefinition.substring(0, classDefinition.lastIndexOf(" ") + 1))
            .append(dubboInterfaceImpl.getSimpleName()).append("DubboApi {\n");

        /**
         * 7 注入实现类
         */
        String dubboInterfaceImplName = dubboInterfaceImpl.getSimpleName().substring(0, 1).toLowerCase()
                                        + dubboInterfaceImpl.getSimpleName().substring(1);
        codeBuilder.append("@org.springframework.beans.factory.annotation.Autowired\n").append("private ")
            .append(dubboInterfaceImpl.getCanonicalName()).append(" ").append(dubboInterfaceImplName).append(";\n");

        /**
         * 8 获取全部方法
         */
        for (Method method : dubboInterface.getMethods()) {

            /**
             * 8.1 获取重组ApiOperation注解
             */
            final ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                codeBuilder.append("@io.swagger.annotations.ApiOperation(value = \"" + apiOperation.value() + "\")\n");
            }

            /**
             * 8.2 创建@RequestMapping注解
             * 支持多版本
             * 支持方法重写
             */
            String serviceVersion = sb.getVersion() != null && sb.getVersion().length() > 0 ? sb.getVersion() : "0.0.0";
            codeBuilder.append("@org.springframework.web.bind.annotation.RequestMapping(value = \"/" + serviceVersion
                               + "/" + dubboInterfaceImpl.getCanonicalName() + "/" + method.getName());
            if (apiOperation != null && apiOperation.nickname().trim().length() > 0) {
                codeBuilder.append("/").append(apiOperation.nickname().trim());
            }
            codeBuilder.append("\", method = org.springframework.web.bind.annotation.RequestMethod.POST)\n");

            /**
             * 8.3 创建方法定义
             */
            codeBuilder.append("public ").append(method.getGenericReturnType().getTypeName()).append(" ")
                .append(method.getName()).append("(");

            /**
             * 8.4 组建方法参数
             */
            final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            final String[] parameterNames = parameterNameDiscoverer
                .getParameterNames(dubboInterfaceImpl.getMethod(method.getName(), method.getParameterTypes()));
            final Parameter[] parameters = method.getParameters();
            StringBuilder parameterBuilder = new StringBuilder();

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                String parameterName = parameterNames[i];
                final ApiParam apiParam = parameter.getAnnotation(ApiParam.class);
                if (apiParam != null) {
                    parameterBuilder.append("@io.swagger.annotations.ApiParam(value = \"").append(apiParam.value())
                        .append("\"").append(", required = ").append(apiParam.required()).append(", defaultValue = \"")
                        .append(apiParam.defaultValue()).append("\") ");
                }

                if (PrimitiveTypeHelper.primitive(parameter.getType())) {
                    parameterBuilder.append("@org.springframework.web.bind.annotation.RequestParam(value = \"")
                        .append(parameterName).append("\"");
                    if (apiParam == null || apiParam.required() == false) {
                        parameterBuilder.append(", required = false");
                    } else {
                        parameterBuilder.append(", required = true");
                    }
                    parameterBuilder.append(")");
                } else {
                    parameterBuilder.append("@springfox.documentation.spi.annotations.RequestModel(value = \"")
                        .append(parameterName).append("\"");
                    if (apiParam == null || apiParam.required() == false) {
                        parameterBuilder.append(", required = false");
                    } else {
                        parameterBuilder.append(", required = true");
                    }
                    parameterBuilder.append(")");
                }

                parameterBuilder.append(parameter.getParameterizedType().getTypeName()).append(" ");
                parameterBuilder.append(parameterName).append(",");
            }

            if (parameterBuilder != null && parameterBuilder.length() > 0) {
                codeBuilder.append(parameterBuilder.substring(0, parameterBuilder.length() - 1));
            }

            String parameterNamesStr = "";
            if (parameterNames != null && parameterNames.length > 0) {
                parameterNamesStr = String.join(", ", parameterNames);
            }

            /**
             * 8.5 组建方法异常标识
             */
            codeBuilder.append(") ");
            final Class<?>[] exceptionTypes = method.getExceptionTypes();
            if (exceptionTypes != null && exceptionTypes.length > 0) {
                codeBuilder.append("throws ");
                for (int i = 0; i < exceptionTypes.length - 1; i++) {
                    codeBuilder.append(exceptionTypes[i].getCanonicalName()).append(",");
                }
                codeBuilder.append(exceptionTypes[exceptionTypes.length - 1].getCanonicalName());
            }

            /**
             * 8.6 组建方法内容
             */
            codeBuilder.append(" {");
            if (!method.getGenericReturnType().getTypeName().equalsIgnoreCase("void")) {
                codeBuilder.append("return ");
            }
            codeBuilder.append(dubboInterfaceImplName).append(".").append(method.getName()).append("(")
                .append(parameterNamesStr != "" ? parameterNamesStr : "").append(");");

            codeBuilder.append("}");
        }

        /**
         * 9 定义类结尾
         */
        codeBuilder.append("}");
        return codeBuilder.toString();
    }
}
```
代码较为简单，就是使用反射获取各种类信息、方法信息、注解信息等，最后进行拼接。

假设Dubbo服务代码如下：

```
package io.hulk.dubbo.springfox.demo.springboot.api;

...import省略

@Api("Book相关的Dubbo服务")
public interface BookService {
    @ApiOperation("测试一层泛型")
    String testGenericField(@ApiParam List<Book> books);
}
```
其实现如下：

```
package io.hulk.dubbo.springfox.demo.springboot.apiimpl;

...import省略

public class BookServiceImpl implements BookService {
    @Override
    public String testGenericField(List<Book> books) {
        if (!CollectionUtils.isEmpty(books)) {
            return books.get(0).getTitle();
        }
        return "书籍列表为空";
    }
}
```

最后生成的Controller代码：

```
package io.hulk.dubbo.springfox.demo.springboot.apiimplDubboApi;

@io.swagger.annotations.Api(tags = "Book相关的Dubbo服务")
@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/dubbo-api")
public class BookServiceImplDubboApi {
    @org.springframework.beans.factory.annotation.Autowired
    private io.hulk.dubbo.springfox.demo.springboot.apiimpl.BookServiceImpl bookServiceImpl;

    @io.swagger.annotations.ApiOperation(value = "测试一层泛型")
    @org.springframework.web.bind.annotation.RequestMapping(value = "/0.0.0/io.hulk.dubbo.springfox.demo.springboot.apiimpl.BookServiceImpl/testGenericField", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public java.lang.String testGenericField(@io.swagger.annotations.ApiParam(value = "", required = false, defaultValue = "") @springfox.documentation.spi.annotations.RequestModel(value = "books", required = false) java.util.List<io.hulk.dubbo.springfox.demo.springboot.model.Book> books) {
        return bookServiceImpl.testGenericField(books);
    }
}
```
说明：

* 包名：Dubbo服务实现类的包名+"DubboApi"后缀；
* 类注解：添加Dubbo接口上配置的@Api注解 + @RestController + 类上下文匹配路径（"```/dubbo-api```"）；
* 类：Dubbo服务实现类的类名+"DubboApi"后缀；
* 注入Dubbo服务实现类，之后的实现都通过该实现类进行调用；
* 方法注解：添加Dubbo接口上配置的@ApiOperation注解 + 方法匹配路径（"```/{Dubbo服务版本号}/{Dubbo服务实现类的全类名}/{方法名}```"）；
	*  对于重载的方法，方法注解需要添加nickname tag进行区分，具体见io.hulk.dubbo.springfox.demo.springboot.api.BookService。
* 方法：与Dubbo服务方法相同；
* 方法参数：添加@ApiParam注解 + 相关的参数接收注解；
	* 参数接收注解：对于普通类型的参数，使用@RequestParam；对于对象类型的参数，使用@RequestModel，该注解是自定义的一个注解，用于打破@RequestBody的单体限制，关于单体限制与自定义mvc参数注解的方式见作者的另一篇文章《[自定义spring参数注解 - 打破@RequestBody单体限制](https://www.cnblogs.com/java-zhao/p/9119258.html)》
* 方法体：调用注入的Dubbo服务实现类调用同名方法。

### 3.5 使用JdkCompiler编译生成的controller并使用classLoader加载其到JVM中
使用JdkCompiler将4.4小节中生成的code进行javac编译，之后使用ApiDocumentBootstrap的类加载器加载编译好的class文件，返回Class对象；

```
Class<?> dubboControllerClazz = jdkCompiler.compile(code, ApiDocumentBootstrap.class.getClassLoader());
```

### 3.6 注册Class对象到BeanDefinitionRegistry中
然后创建Class对象的BeanDefinition对象，最后注册到BeanDefinitionRegistry中。

```
    /**
     * 获取clazz的BeanDefinition并注册到BeanDefinitionRegistry中
     *
     * @param clazz
     * @param context
     */
    public static void registerBeanDefinition(Class<?> clazz, ApplicationContext context) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanDefinitonRegistry.registerBeanDefinition(clazz.getCanonicalName(), beanDefinitionBuilder.getBeanDefinition());
    }
```
这样之后，springmvc在扫描controller的时候，就会将生成的一并扫描了，SpringFox也会统一扫描所有的controller，生成相关的文档和图形化调用界面。
### 3.7 设置系统属性
最后，设置系统属性。

```
System.setProperty(DubboSpringfoxContants.SERVICE_GROUP, serviceGroup);
```
将当前Dubbo服务的group读取出来并存储到系统属性中。在构建springfox.documentation.spring.web.plugins.Docket实例的时候使用。通过Docket实例可以配置一系列与SpringFox相关的信息，其中最重要的就是指定图形化UI界面在何时可以显示接口信息。

```
    @Bean
    public Docket docket() {
        final String serviceGroup = System.getProperty(DubboSpringfoxContants.SERVICE_GROUP, "dev");
        if (serviceGroup.equals(DubboSpringfoxContants.SERVICE_GROUP_PRODUCT)) {
            return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.none()).build();
        }
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
            //                .paths(PathSelectors.regex("^(/error/)"))
            .build().useDefaultResponseMessages(false)
            .apiInfo(new ApiInfo("", "", "", "", new Contact("", "", ""), "", "", new ArrayList<VendorExtension>()));
    }
```
这里读取刚刚存放到系统属性中的Dubbo服务的group信息，然后判断如果是"product"则不在UI上显示接口信息，其他的group或者没有配置group都会显示。

假设配置为这样：

```
<dubbo:service interface="io.hulk.dubbo.springfox.demo.springboot.api.BookService" ref="bookService" group="product"/>
```

UI界面为：
 
![Alt pic](http://chuantu.biz/t6/346/1532152562x-1566661044.png) 

查看返回结果：http://localhost:8081/v2/api-docs

```
{
	swagger: "2.0",
	info: {
		description: "Api Documentation",
		version: "1.0",
		title: "Api Documentation",
		termsOfService: "urn:tos",
		contact:{},
		license: {
			name: "Apache 2.0",
			url: "http://www.apache.org/licenses/LICENSE-2.0"
		}
	},
	host: "localhost:8081",
	basePath: "/"
}
```
也没有相关的接口信息，这样就保证了在生产环境的安全性。

## 附1、dubbo-springfox注解附表
对于springmvc controller接口来讲，支持全部的Swagger注解；对于Dubbo服务接口来讲，仅支持如下注解：

### @Api("xxx")
用于类上，注解内属性仅支持value和tags。默认不写属性名就是value。
### @ApiOperation(value="xxx", nickname ="xxx")
用于方法上，注解内属性仅支持value和nickname。nickname用于区分重载的方法。默认不写属性名就是value。
### @ApiParam(value="xxx", required="xxx", defaultValue="xxx")
用于参数上，注解内属性仅支持value、required和defaultValue。默认不写属性名就是value。

## 附2、Q&A
### Q1：dubbo-springfox是否支持注解形式配置的Dubbo服务？
不支持。
### Q2：方法重载时是否必须填写nickname？
方法重载时必须添加@ApiOperation(nickname="xxx")注解和注解属性，否则会报出springmvc匹配路径重复的错误。
### Q3：为什么没有看到前台代码，就可以显示出UI界面？
SpringFox-UI采用webjar技术，将swagger-ui打包成jar包，是的后端可以以jar包的形式来引入前端代码。关于webjar技术，参考：[https://www.webjars.org/](https://www.webjars.org/)
### Q4：其他关于SpringFox的疑问？
[http://springfox.github.io/springfox/docs/current/](http://springfox.github.io/springfox/docs/current/)
