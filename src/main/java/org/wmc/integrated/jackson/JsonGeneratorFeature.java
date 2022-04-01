package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;

/**
 * JsonGenerator的Feature它是JsonGenerator的一个内部枚举类，共10个枚举值：
 *
 * public enum Feature {
 *
 *     // Low-level I/O
 *     AUTO_CLOSE_TARGET(true),
 *     AUTO_CLOSE_JSON_CONTENT(true),
 *     FLUSH_PASSED_TO_STREAM(true),
 *
 *     // Quoting-related features
 *     @Deprecated
 *     QUOTE_FIELD_NAMES(true),
 *     @Deprecated
 *     QUOTE_NON_NUMERIC_NUMBERS(true),
 *     @Deprecated
 *     ESCAPE_NON_ASCII(false),
 *     @Deprecated
 *     WRITE_NUMBERS_AS_STRINGS(false),
 *
 *     // Schema/Validity support features
 *     WRITE_BIGDECIMAL_AS_PLAIN(false),
 *     STRICT_DUPLICATE_DETECTION(false),
 *     IGNORE_UNKNOWN(false);
 *
 *     ...
 * }
 *
 */
public class JsonGeneratorFeature {

    /**
     * AUTO_CLOSE_TARGET(true) 自动关闭目标（流）
     *
     * 含义即为字面意：自动关闭目标（流）。
     * true：调用JsonGenerator#close()便会自动关闭底层的I/O流，你无需再关心
     * false：底层I/O流请手动关闭
     *
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            // doSomething
        }
    }

    /**
     * AUTO_CLOSE_TARGET(true)
     *
     * 例子均采用try-with-resources方式关流，所以不需要显示调用close()方法
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (PrintStream err = System.err; JsonGenerator jg = factory.createGenerator(err, JsonEncoding.UTF8)) {
            // 特征置为false 采用手动关流的方式
            jg.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
            // doSomething
        }
    }


    /**
     * AUTO_CLOSE_JSON_CONTENT(true)
     *
     * true：自动补齐（闭合）JsonToken#START_ARRAY和JsonToken#START_OBJECT类型的内容
     * false：啥都不做（不会主动抛错哦）
     *
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            jg.writeStartObject();
            jg.writeFieldName("names");

            // 写数组
            jg.writeStartArray();
            jg.writeString("A哥");
            jg.writeString("YourBatman"); // {"names":["A哥","YourBatman"]}
        }
    }

    /**
     * FLUSH_PASSED_TO_STREAM(true)
     *
     * true：当JsonGenerator调用close()/flush()方法时，自动强刷I/O流里面的数据
     * false：请手动处理
     *
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8);

        jg.writeStartObject();
        jg.writeStringField("name","A哥");
        jg.writeEndObject();

        // 控制台没有任何输出。把注释代码放开任何一行，再次运行程序，控制台正常输出：
        // jg.flush();
        // jg.close();
    }

    /**
     * QUOTE_FIELD_NAMES(true) 此属性自2.10版本后已过期，使用JsonWriteFeature#QUOTE_FIELD_NAMES代替，应用在JsonFactory上
     *
     * JSON对象字段名是否为使用""双引号括起来，这是JSON规范（RFC4627）规定的。
     * true：字段名使用""括起来 -> 遵循JSON规范
     * false：字段名不使用""括起来 -> 不遵循JSON规范
     *
     * @throws IOException
     */
    @Test
    public void test5() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            //jg.disable(QUOTE_FIELD_NAMES);

            jg.writeStartObject();
            jg.writeStringField("name","A哥");
            jg.writeEndObject();
            // {"name":"A哥"} 打开注释掉的语句，再次运行程序，输出：{name:"A哥"}
        }
    }

    /**
     * QUOTE_NON_NUMERIC_NUMBERS(true) 此属性自2.10版本后已过期，使用JsonWriteFeature#WRITE_NAN_AS_STRINGS代替，应用在JsonFactory上
     *
     * @throws IOException
     */
    @Test
    public void test6() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            // jg.disable(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS);

            jg.writeNumber(0.9);
            jg.writeNumber(1.9);

            jg.writeNumber(Float.NaN);
            jg.writeNumber(Float.NEGATIVE_INFINITY);
            jg.writeNumber(Float.POSITIVE_INFINITY);
            // 0.9 1.9 "NaN" "-Infinity" "Infinity" 放开注释的语句（禁用此特征），再次运行程序，输出：0.9 1.9 NaN -Infinity Infinity
        }
    }

    /**
     * ESCAPE_NON_ASCII(false) 此属性自2.10版本后已过期，使用JsonWriteFeature#ESCAPE_NON_ASCII代替，应用在JsonFactory上
     *
     *
     * @throws IOException
     */
    @Test
    public void test7() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            // jg.enable(ESCAPE_NON_ASCII);
            jg.writeString("A哥");
        }
        // "A哥" 放开注掉的代码（开启此属性），再次运行，输出："A\u54E5"
    }

    /**
     * WRITE_NUMBERS_AS_STRINGS(false) 此属性自2.10版本后已过期，使用JsonWriteFeature#WRITE_NUMBERS_AS_STRINGS代替，应用在JsonFactory上
     *
     * 该特性强制将所有Java数字写成字符串，即使底层数据格式真的是数字。
     * true：所有数字强制写为字符串
     * false：不做处理
     *
     */
    @Test
    public void test8() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            // jg.enable(WRITE_NUMBERS_AS_STRINGS);

            Long num = Long.MAX_VALUE;
            jg.writeNumber(num);
            // 9223372036854775807 放开注释代码（开启此特征），再次运行程序，输出："9223372036854775807"
        }
    }

    /**
     * WRITE_BIGDECIMAL_AS_PLAIN(false)
     *
     * 控制写java.math.BigDecimal的行为：
     * true：使用BigDecimal#toPlainString()方法输出
     * false： 使用默认输出方式（取决于BigDecimal是如何构造的）
     *
     */
    @Test
    public void test9() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            // jg.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);

            BigDecimal bigDecimal1 = new BigDecimal(1.0);
            BigDecimal bigDecimal2 = new BigDecimal("1.0");
            BigDecimal bigDecimal3 = new BigDecimal("1E11");
            jg.writeNumber(bigDecimal1);
            jg.writeNumber(bigDecimal2);
            jg.writeNumber(bigDecimal3);
            // 1 1.0 1E+11 放开注释代码，再次运行程序，输出：1 1.0 100000000000
        }
    }

    /**
     * STRICT_DUPLICATE_DETECTION(false)
     *
     * 是否去严格的检测重复属性名。
     * true：检测是否有重复字段名，若有，则抛出JsonParseException异常
     * false：不检测JSON对象重复的字段名，即：相同字段名都要解析
     *
     */
    @Test
    public void test10() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.err, JsonEncoding.UTF8)) {
            // jg.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);

            jg.writeStartObject();
            jg.writeStringField("name","YourBatman");
            jg.writeStringField("name","A哥");
            jg.writeEndObject();
            // {"name":"YourBatman","name":"A哥"} 打开注释掉的哪行代码：开启此特征值为true。再次运行程序，报错输出：com.fasterxml.jackson.core.JsonGenerationException: Duplicate field 'name'
        }
    }

    /**
     * IGNORE_UNKNOWN(false)
     *
     * 强调：JsonGenerator不是只能写JSON格式，毕竟底层是I/O流嘛，理论上啥都能写
     * true：启动该功能
     * 可以预先调用（在写数据之前）这个API设定好模式信息即可：
     * JsonGenerator：
     *
     *     public void setSchema(FormatSchema schema) {
     *         ...
     *     }
     * false：禁用该功能。如果底层数据格式需要所有属性的知识才能输出，那就抛出JsonProcessingException异常
     *
     */

    /**
     * 定制Feature
     *
     * // 开启
     * public abstract JsonGenerator enable(Feature f);
     * // 关闭
     * public abstract JsonGenerator disable(Feature f);
     * // 开启/关闭
     * public final JsonGenerator configure(Feature f, boolean state) { ... };
     *
     * public abstract boolean isEnabled(Feature f);
     * public boolean isEnabled(StreamWriteFeature f) { ... };
     *
     */

    /**
     * 替换者：StreamWriteFeature
     *
     * 本类是2.10版本新增的，用于完全替换上面的Feature。目的：完全独立的属性配置，不依赖于任何后端格式，因为JsonGenerator并不局限于写JSON，因此把Feature放在JsonGenerator作为内部类是不太合适的，所以单独摘出来。
     * StreamWriteFeature用在JsonFactory里，后面再讲解到它的构建器JsonFactoryBuilder时再详细探讨。
     *
     */

    /**
     * 序列化POJO对象
     *
     * public abstract JsonGenerator setCodec(ObjectCodec oc);
     *
     * ObjectCodec可谓是Jackson里极其重要的一个基础组件，我们最熟悉的ObjectMapper它就是一个解码器，实现了序列化和反序列化、树模型等操作。这将在后面章节里重点介绍~
     *
     */

    /**
     * 输出漂亮的JSON格式
     *
     * // 自己指定漂亮格式打印器
     * public JsonGenerator setPrettyPrinter(PrettyPrinter pp) { ... }
     *
     * // 应用默认的漂亮格式打印器
     * public abstract JsonGenerator useDefaultPrettyPrinter();
     *
     * 什么都不设置：
     * MinimalPrettyPrinter：
     * {"zhName":"A哥","enName":"YourBatman","age":18}
     *
     * DefaultPrettyPrinter：
     * useDefaultPrettyPrinter():
     * {
     * "zhName" : "A哥",
     * "enName" : "YourBatman",
     * "age" : 18
     * }
     *
     */
















}
