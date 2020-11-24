# SSM框架整合过程记录

## 思路

先创建Web项目，因为表现层及持久层都是和业务层进行交流，所以我们通过Spring整合SpringMVC，再通过Spring整合Mybatis。

## SSM之搭建环境

创建测试所需要的数据库表

```sql
	CREATE TABLE account(
        id  INT PRIMARY KEY AUTO_INCREMENT,
        NAME VARCHAR(20),
        money DOUBLE
    );

```

使用webapp模板搭建maven项目
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093557856.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)


导入所需要的坐标依赖

```xml
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <spring.version>5.0.2.RELEASE</spring.version>
    <mysql.version>5.1.6</mysql.version>
    <mybatis.version>3.4.5</mybatis.version>
  </properties>

  <dependencies>
    <!--    spring-->
    <!--AOP相关技术-->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.6.8</version>
    </dependency>

    <!--AOPjar包-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!--context容器-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!--mvc相关jar包-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!--单元测试-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!--事务-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!--JDBC技术-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <!--单元测试-->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>compile</scope>
    </dependency>

    <!--Mysql驱动jar包-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>${mysql.version}</version>
    </dependency>

    <!--Servlet相关jar包-->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
      <scope>provided</scope>
    </dependency>


    <!--      log  strat-->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.12</version>
    </dependency>
    <!--      log  end-->

    <!--Mybatis相关jar包组件-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>${mybatis.version}</version>
    </dependency>

    <!--spring整合mybatis所需jar包-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.0</version>
    </dependency>

   <!--数据库连接池：c3p0-->
    <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
    <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.5.2</version>
    </dependency>

  </dependencies>


  <build>
    <finalName>ssm</finalName>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- see http://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_war_packaging -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-war-plugin</artifactId>
          <version>3.2.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>2.5.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>2.8.2</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```

创建好maven项目目录

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093621346.png#pic_center)


在domain包下创建Account的JavaBean类

```java
package cn.swingz.domain;

import java.io.Serializable;

/**
 * 账户的JavaBean
 */
public class Account implements Serializable {
    private Integer id;
    private String name;
    private Double money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
```

在dao包下创建Account的dao接口

```java
package cn.swingz.dao;

import cn.swingz.domain.Account;

import java.util.List;

/**
 * 账户dao接口
 */
public interface AccountDao {
    
    //查询所有账户信息
    List<Account> findAll();
    
    //保存账户信息
    void saveAccount(Account account);
    
    
}
```

在service包下创建Account业务层接口

```java
package cn.swingz.service;

import cn.swingz.domain.Account;

import java.util.List;

/**
 * Account的业务层
 */
public interface AccountService {
    //查询所有账户信息
    List<Account> findAll();

    //保存账户信息
    void saveAccount(Account account);
}
```

在service子包impl下实现接口

```java
package cn.swingz.service.impl;

import cn.swingz.domain.Account;
import cn.swingz.service.AccountService;

import java.util.List;

/**
 * 业务层实现类
 */
public class AccountServiceImpl implements AccountService {
    @Override
    public List<Account> findAll() {
        System.out.println("业务层查询所有账户");
        return null;
    }

    @Override
    public void saveAccount(Account account) {
        System.out.println("业务层保存账户信息");
    }
}
```

在controller包下创建表现层AccountController

```java
package cn.swingz.controller;

/**
 * 账户web
 */
public class AccountController {
}
```

配置log4.properties日记配置

```properties
log4j.rootLogger=INFO, console, file

log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%d %p [%c] - %m%n

log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
log4j.appender.file.File=logs/log.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.A3.MaxFileSize=1024KB
log4j.appender.A3.MaxBackupIndex=10
log4j.appender.file.layout.ConversionPattern=%d %p [%c] - %m%n
```

## SSM之编写Spring框架

创建Spring配置文件：applicationContext.xml

开启注解扫描，并且Spring只负责业务层和持久层，而表现层由SpringMVC来处理。所以扫描要忽略Controller包

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
      http://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/context
      http://www.springframework.org/schema/context/spring-context.xsd
      http://www.springframework.org/schema/aop
      http://www.springframework.org/schema/aop/spring-aop.xsd
      http://www.springframework.org/schema/tx
      http://www.springframework.org/schema/tx/spring-tx.xsd">
    
    <!--    开启注解的扫描-->
    <!--    开启注解扫描 只需要处理service和dao-->
    <context:component-scan base-package="cn.swingz">
        <!--        配置哪些注解不扫描-->
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    
</beans>
```

测试Spring

```java
import cn.swingz.service.AccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {
    @Test
    public void run1(){
        //加载配置文件
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        //获取对象
        AccountService as = (AccountService)ac.getBean("accountService");
        //调用方法
        as.findAll();
    }
}
```

结果如下

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093740764.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)


## SSM之编写SpringMVC框架

在web.xml下配置中文乱码过滤器及前端控制器

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>

  <!--  解决中文乱码过滤器-->
  <filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  
<!--  配置前端控制器-->
  <servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
<!--    加载springmvc.xml配置文件-->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
<!--    启动服务器就创建该servlet-->
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>
```

编写springMVC配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">

<!--    开启注解扫描-->
    <context:component-scan base-package="cn.swingz">
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
<!--    配置试图解析器对象-->
    <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/" />
        <property name="suffix" value=".jsp" />
    </bean>
<!--    过滤静态资源-->
    <mvc:resources mapping="/css/**" location="/css/" />
    <mvc:resources mapping="/images/**" location="/images/" />
    <mvc:resources mapping="/js/**" location="/js/" />
    
<!--    开启springMVC注解的支持-->
    <mvc:annotation-driven></mvc:annotation-driven>
</beans>
```

编写业务层

```JAVA
package cn.swingz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 账户web
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @RequestMapping("/findAll")
    public String findAll(){
        System.out.println("表现层:查询所有用户信息");
        return "list";
        //跳转到pages下的list.jsp页面
    }
}
```

测试用例index.jsp

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020112409382552.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)


运行结果：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093848948.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093848940.png#pic_center)


## SSM之Spring整合SpringMVC框架

思考：怎么算整合成功？

答案：Controller成功调用service方法

问题：启动Tomcat，只web.xm只加载了springmvc.properties文件，只扫描了Controller，没有使用到applicationContext.xml。没人加载就是service没有注入到容器中。

解决方案：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093908803.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)


web.xml配置监听器及默认配置文件加载路径

```xml
<!--  设置配置文件的路径-->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:applIcationContext.xml</param-value>
</context-param>

 <!--  配置spring的监听器,默认只加载WEB-INF目录下的applicationContext.xml配置文件-->
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
```

Controller使用依赖注入获取AccountService

```java
@Autowired
private AccountService accountService;

@RequestMapping("/findAll")
public String findAll(){
    System.out.println("表现层:查询所有用户信息");
    accountService.findAll();
    return "list";
}
```

结果显示

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124093933198.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)


## SSM之编写MyBatis框架

使用注解开发

```java
//查询所有账户信息
@Select("select * from account")
List<Account> findAll();

//保存账户信息
@Insert("insert into account(name,money) values (#{name},#{money})")
void saveAccount(Account account);
```

编写MyBatis配置文件sqlMapConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--    配置环境-->
    <environments default="mysql">
        <!--        配置mysql的环境-->
        <environment id="mysql">
            <!--            配置事务类型-->
            <transactionManager type="JDBC"></transactionManager>
            <!--            配置数据源（连接池）-->
            <dataSource type="POOLED">
                <!--                配置连接数据库的4个基本信息-->
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/ssm"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <!--    指定映射配置文件  映射配置文件的是每个dao独立的配置文件-->
    <mappers>
<!--        配置文件的写法-->
<!--        <mapper resource="com/mybatis/dao/IUserDao.xml"></mapper>-->
<!--        一个dao接口写法-->
<!--        <mapper class="cn.swingz.dao.AccountDao" />-->
<!--        所有包的写法-->
        <package name="cn.swingz.dao"/>
    </mappers>

</configuration>
```

测试Mybatis是否配置成功

```java
import cn.swingz.dao.AccountDao;
import cn.swingz.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestMyBatis {

    @Test
    public void run1() throws IOException {
        //加载配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        //创建SqlSession对象
//        ture自动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        //获取代理对象
        AccountDao mapper = sqlSession.getMapper(AccountDao.class);
        Account account = new Account();
        account.setName("hello");
        account.setMoney(1010d);
        mapper.saveAccount(account);
//        提交事务
//        sqlSession.commit();
        List<Account> all = mapper.findAll();
        sqlSession.close();
        in.close();
        for (Account account1 : all) {
            System.out.println(account1);
        }

    }
}

```

测试结果

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124094010677.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)

​	

## SSM之Spring整合MyBatis框架

思考：怎么算整合成功？

结果：service能成功的调用dao对象，进行查询或者把数据存进数据库。

如何把生成的代理对象放入到 IOC 容器中？

在Spring配置文件applicationContext.xml中配置

```xml
<!--    Spring整合MyBatis框架-->
<!--    配置连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql://139.199.69.163:3307/ssm"/>
        <property name="user" value="root"/>
        <property name="password" value="root"/>
    </bean>
<!--    配置工厂对象SqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
    </bean>
<!--    配置AccountDao接口所在包-->
    <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="cn.swingz.dao" />
    </bean>
```

将Dao交给Ioc容器管理

```java
@Repository
public interface AccountDao {}
```

编写Controller测试配置是否成功

```java
package cn.swingz.controller;

import cn.swingz.domain.Account;
import cn.swingz.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 账户web
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/findAll")
    public String findAll(){
        System.out.println("表现层:查询所有用户信息");
        List<Account> all = accountService.findAll();
        for (Account account : all) {
            System.out.println(account);
        }
        return "list";
    }
}
```

测试结果
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124094034781.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)


## SSM之Spring整合MyBatis框架配置事务

* 声明式事务管理

在Spring配置文件中配置如下内容

```xml
<!--    配置Spring框架声明式事务管理-->
<!--    配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
<!--        传入连接池对象-->
        <property name="dataSource" ref="dataSource" />
    </bean>
<!--    配置事务通知-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
<!--            find方法只读-->
            <tx:method name="find*" read-only="true"/>
            <tx:method name="*" isolation="DEFAULT" />
        </tx:attributes>
    </tx:advice>
<!--    配置AOP增强-->
    <aop:config>
        <aop:advisor advice-ref="txAdvice" pointcut="execution(* cn.swingz.service.impl.*ServiceImpl.*(..))" />
    </aop:config>
```

页面测试编辑

```html
<form method="post" action="/account/save">
    姓名：<input name="name" type="text" /><br>
    金钱：<input name="money" type="text" /><br>
    <input type="submit" value="提交">
</form>
```

Controller方法编写

```java
@RequestMapping("/save")
public String save(Account account){
    accountService.saveAccount(account);
    return "list";
}
```

测试结果

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124094148301.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124094148298.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkwODg2Nw==,size_16,color_FFFFFF,t_70#pic_center)
## SSM项目源码
**GitHub：https://github.com/swingz/SSM**