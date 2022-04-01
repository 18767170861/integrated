package org.wmc.integrated.util;

import org.wmc.integrated.mongo.entity.User;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

public class SpringFrameworkUtils {

    public static void main(String[] args) throws IOException {
        //socketUtils();
        //serializationUtils();
        //uuid();
        //propertyPlaceholderHelper();
        //systemPropertyUtils();
        version();
    }

    /**
     * SocketUtils
     * 提供给我们去系统找可用的Tcp、Udp端口来使用。有的时候确实还蛮好用的，必进端口有时候不用写死了，提高灵活性
     */
    public static void socketUtils() {
        System.out.println(SocketUtils.PORT_RANGE_MAX); //65535 最大端口号
        System.out.println(SocketUtils.findAvailableTcpPort()); //45569 随便找一个可用的Tcp端口 每次执行值都不一样哦
        System.out.println(SocketUtils.findAvailableTcpPort(1000, 2000)); //1325 从指定范围内随便找一个端口

        //找一堆端口出来 并且是排好序的
        System.out.println(SocketUtils.findAvailableTcpPorts(10, 1000, 2000)); //[1007, 1034, 1287, 1483, 1494, 1553, 1577, 1740, 1963, 1981]

        //UDP端口的找寻 同上
        System.out.println(SocketUtils.findAvailableUdpPort()); //12007
    }

    /**
     * SerializationUtils
     * 提供了两个方法：对象<–>二进制的相互转化。（基于源生JDK的序列化方式）
     */
    public static void serializationUtils() {
        User user = new User();
        byte[] serialize = SerializationUtils.serialize(user);
        System.out.println("serialize:" + new String(serialize, Charset.defaultCharset()));
        Object deserialize = SerializationUtils.deserialize(serialize);
        System.out.println("deserialize:" + deserialize);

    }

    /**
     * UUID生成
     * JdkIdGenerator
     * JDK的工具类包util包中就为我们提供了一个很好的工具类，即UUID。UUID（Universally Unique Identifier）通用唯一识别码。底层字节调用JDK的UUID方法
     * <p>
     * AlternativeJdkIdGenerator
     * 它使用了SecureRandom作为种子，来替换调用UUID#randomUUID()。它提供了一个更好、更高性能的表现（关于性能比较，下面会给出一个例子）
     * <p>
     * SimpleIdGenerator
     * 类似于自增的Id生成器。每调用一次，自增1
     * <p>
     * 发现仅仅循环生成100万次，Spring提供的算法性能远远高于JDK的。因此建议大家以后使用AlternativeJdkIdGenerator去生成UUID，性能会更好一点
     * 缺点是：还需要new对象才能使用，不能通过类名直接调用静态方法，当然我们可以二次封装。另外，一般输出串我们都会进一步这么处理：.toString().replace("-", "")
     */
    public static void uuid() {
        JdkIdGenerator jdkIdGenerator = new JdkIdGenerator();
        AlternativeJdkIdGenerator alternativeJdkIdGenerator = new AlternativeJdkIdGenerator();
        SimpleIdGenerator simpleIdGenerator = new SimpleIdGenerator();

        Instant start;
        Instant end;
        int count = 1000000;

        //jdkIdGenerator
        start = Instant.now();
        for (int i = 0; i < count; i++) {
            jdkIdGenerator.generateId();
        }
        end = Instant.now();
        System.out.println("jdkIdGenerator循环" + count + "次耗时：" + Duration.between(start, end).toMillis() + "ms");

        //alternativeJdkIdGenerator
        start = Instant.now();
        for (int i = 0; i < count; i++) {
            alternativeJdkIdGenerator.generateId();
        }
        end = Instant.now();
        System.out.println("alternativeJdkIdGenerator循环" + count + "次耗时：" + Duration.between(start, end).toMillis() + "ms");

        //simpleIdGenerator
        start = Instant.now();
        for (int i = 0; i < count; i++) {
            simpleIdGenerator.generateId();
        }
        end = Instant.now();
        /**
         * jdkIdGenerator循环1000000次耗时：379ms
         * alternativeJdkIdGenerator循环1000000次耗时：65ms
         * simpleIdGenerator循环1000000次耗时：11ms
         */
        System.out.println("simpleIdGenerator循环" + count + "次耗时：" + Duration.between(start, end).toMillis() + "ms");
    }

    /**
     * PropertyPlaceholderHelper
     * 作用：将字符串里的占位符内容，用我们配置的properties里的替换。这个是一个单纯的类，没有继承没有实现，而且也没简单，没有依赖Spring框架其他的任何类
     */
    public static void propertyPlaceholderHelper() throws IOException {

        String a = "{name}{age}{sex}";
        String b = "{name{age}{sex}}";
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("{", "}");
        InputStream in = SpringFrameworkUtils.class.getClassLoader().getResourceAsStream("user.properties");
        Properties properties = new Properties();
        properties.load(in);

        //==============开始解析此字符串==============
        System.out.println("替换前:" + a); //替换前:{name}{age}{sex}
        System.out.println("替换后:" + propertyPlaceholderHelper.replacePlaceholders(a, properties)); //替换后:wangzha18man
        System.out.println("====================================================");
        System.out.println("替换前:" + b); //替换前:{name{age}{sex}}
        System.out.println("替换后:" + propertyPlaceholderHelper.replacePlaceholders(b, properties)); //替换后:love 最后输出love，证明它是从内往外一层一层解析的
    }

    /**
     * SystemPropertyUtils:占位符解析工具类
     * 该类依赖于上面已经说到的PropertyPlaceholderHelper来处理。本类只处理系统默认属性值哦~
     */
    public static void systemPropertyUtils() {
        //Windows 10/logs/app.log
        System.out.println(SystemPropertyUtils.resolvePlaceholders("${os.name}/logs/app.log"));

        //备注：这里如果不传true，如果找不到app.root这个key就会报错哦。传true后找不到也原样输出
        //${app.root}/logs/app.log
        System.out.println(SystemPropertyUtils.resolvePlaceholders("${app.root}/logs/app.log", true));
    }

    /**
     * SpringVersion、SpringBootVersion 获取当前Spring版本号
     */
    public static void version() {
        System.out.println(SpringVersion.getVersion()); //5.3.4
        System.out.println(SpringBootVersion.getVersion()); //2.4.3
    }
}
