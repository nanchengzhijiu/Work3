## **Mybatis**

好理解的文档：[柏码知识库 | Mybatis 快速上手](https://www.itbaima.cn/zh-CN/document/ijay2hay19kn1k031?segment=1)

基本配置

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/xxx"/>
                <property name="username" value=""/>
                <property name="password" value=""/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper url=""/>
    </mappers>
</configuration>
```

${}为直接拼接、#{}经过预编译的语句

```
//自动命名返回类型entity下包的类
<typeAliases>
    <package name="entity"/>
</typeAliases>

<typeAliases>
    <typeAlias type="entity.Order" alias="Order"/>
    <typeAlias type="entity.Commodity" alias="Commodity"/>
</typeAliases>
```

resultType可以返回hashmap形式返回

传入多个参数使用Map.of()传入参数,也可以传入包含所有参数的实体类

返回类的成员变量和数据库列名尽量保持一致，或者采用驼峰命名

```java
//开启驼峰命名
<settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

或者使用resultMap(\<result>必须有column和property)

```java
//mapper
<select id="selectOrderById" resultMap="xx">//必须有id、resultMap或resultType
        select * from orders where id=#{id}
</select>
<resultMap id="xx" type="">//必须有id和type
	<id column="id" property="id">//主键
	<result column="name" property="username">
</resultMap>
```

让Mybatis完全使用构造方法进行对象构建与赋值工作(参数的顺序必须和构造方法的顺序一致)

```java
//maper
<resultMap id="" type="">
    <constructor>
        <idArg column="id" javaType="_int"/> //主键,必须有javaType
    	<arg column="name" javaType="String"/>//必须有javaType
    </constructor>
</resultMap>
```

可以通过接口绑定轻松完成查询(通过namespace定位)

```java
//xml
<mapper namespace="mapper.TestMapper">
</mapper>
```

```java
package Interface;

import entity.Order;

import java.util.List;

public interface TestMapper {
    List<Order> selectAllOrder();
    Order selectOrderById(int id);
}
```

使用

```java
TestMapper mapper = session.getMapper(TestMapper.class);
mapper.selectAllOrder().forEach(System.out::println);
```

多个参数时解决方法(mybatis默认参数为param1.....)

```java
select * from user where id = #{param1} and age = #{param2}
```

或者接口处

```
User selectUserByIdAndAge(@Param("id") int id, @Param("age") int age);
```

复杂查询

一对一、多对一

```
<resultMap id="test" type="com.test.entity.User">
    <id property="id" column="id"/>
    <result property="name" column="name"/>
    <result property="age" column="age"/>
    <association property="detail" column="id" javaType="com.test.entity.UserDetail">
        <id property="id" column="id"/>
        <result property="description" column="description"/>
        <result property="register" column="register"/>
        <result property="avatar" column="avatar"/>
    </association>
</resultMap>
```

或者不用左连接，但是column必须填写

```
<select id="selectUserById" resultMap="test">
    select * from user where id = #{id}
</select>
<resultMap id="test" type="com.test.entity.User">
    <id property="id" column="id"/>
    <result property="name" column="name"/>
    <result property="age" column="age"/>
    <association property="detail" column="id" select="selectUserDetailById" javaType="com.test.entity.UserDetail"/>
</resultMap>

<select id="selectUserDetailById" resultType="com.test.entity.UserDetail">
    select * from user_detail where id = #{id}
</select>
```

一对多

```
<resultMap id="test" type="com.test.entity.User">
    <id column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="age" property="age"/>
    <collection property="books" ofType="com.test.entity.Book">
        <id column="bid" property="bid"/>
        <result column="title" property="title"/>
    </collection>
</resultMap>
```

insert和update可以通过 useGeneratedKeys="true" keyProperty="id" keyColumn="id"讲自增值回写到实体类

开启批处理

```
factory.openSession(ExecutorType.BATCH, autoCommit);
```

开启批处理后，无论是否处于事务模式下，都需要session.flushStatements()来一次性提交之前是所有批处理操作

动态sql：可以通过各种提供的标签拼接sql语句

一级缓存：每个session的操作是独立的

二级缓存：所有session操作的结果会放在同一个mapper空间下共享的(读的多写少的环境下可以考虑开启)

flushCache可以清楚所有缓存

## 任务一

表结构

```
commodity（商品表）
id: int(11)                    （主键）
commodity_number: varchar(255)  （商品编号）
name: varchar(255)              （商品名称）
price: decimal(10, 2)           （商品单价）

orders（订单表）
id: int(11)                    （主键）
order_number: varchar(255)     （订单号，唯一标识订单）
price: decimal(10, 2)          （订单总金额）
order_time: datetime           （下单时间）

order_item（订单明细表）
id: int(11)                    （主键）
order_number: varchar(255)     （外键，关联 orders.order_number）
commodity_number: varchar(...) （外键，关联 commodity.commodity_number）
number: int(11)                （购买数量）
total_price: decimal(10, 2)    （该商品项的总价 = 单价 × 数量）
```

数据库设计时多设计了order_item用于存储订单商品列表信息，其使用外键将其与订单表关联,同时设置为**CASCADE**，这样再删除订单时，即可联动删除order_item中对应的商品条目

order_item也通过外键与商品表关联，同时设置为**RESTRICT**，以限制商品表的删除操作

- **orders** 与 **order_item** 是**一对多关系**：
- 一个订单可包含多个订单明细（商品项），通过 `order_number` 关联。
- **commodity** 与 **order_item** 是**一对多关系**：
- 一个商品可出现在多个订单明细中，通过 `commodity_number` 关联。
- **order_item** 是**关联表（或中间表）**，存储订单与商品的多对多关系的具体信息（数量、小计金额）。

### 异常处理：

插入、更新时，商品编号，订单编号不存在的情况

插入时，商品编号，订单编号重复的情况

插入时，必填值为空的情况

删除时，数据不存在，导致返回集合为空情况

输入的数据格式不对的情况（例如：价格不能为复数，必须为数字）

### 存在的问题:

在更新商品价格是，订单对应的价格本人并未更新，应为本人觉得不能因为后续价格的改变而更改先前订单的价格，这不合理，

同时，在更新商品项的数目是，价格是按下单时的版本进行计算的
