package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.IOException;

/**
 * JsonParser的Feature它是JsonParser的一个内部枚举类，共15个枚举值：
 *
 * public enum Feature {
 *     AUTO_CLOSE_SOURCE(true),
 *
 *     ALLOW_COMMENTS(false),
 *     ALLOW_YAML_COMMENTS(false),
 *     ALLOW_UNQUOTED_FIELD_NAMES(false),
 *     ALLOW_SINGLE_QUOTES(false),
 *     @Deprecated
 *     ALLOW_UNQUOTED_CONTROL_CHARS(false),
 *     @Deprecated
 *     ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false),
 *     @Deprecated
 *     ALLOW_NUMERIC_LEADING_ZEROS(false),
 *     @Deprecated
 *     ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS(false),
 *     @Deprecated
 *     ALLOW_NON_NUMERIC_NUMBERS(false),
 *     @Deprecated
 *     ALLOW_MISSING_VALUES(false),
 *     @Deprecated
 *     ALLOW_TRAILING_COMMA(false),
 *
 *     STRICT_DUPLICATE_DETECTION(false),
 *     IGNORE_UNDEFINED(false),
 *     INCLUDE_SOURCE_IN_LOCATION(true);
 * }
 */
public class JsonParserFeature {

    // 底层I/O流相关
    /**
     * AUTO_CLOSE_SOURCE(true) 原理和JsonGenerator的AUTO_CLOSE_TARGET(true)一样, 自动关闭目标（流）
     *
     */

    /**
     * 支持非标准格式
     *
     * JSON是有规范的，在它的规范里并没有描述到对注释的规定、对控制字符的处理等等，也就是说这些均属于非标准行为。比如这个JSON串：
     * {
     *     "name" : "YourBarman", // 名字
     *     "age" : 18 // 年龄
     * }
     *
     * ALLOW_COMMENTS(false) 自2.10版本后，使用JsonReadFeature#ALLOW_JAVA_COMMENTS代替
     * 是否允许/*、 * / 或者这种类型的注释出现。
     *
     * 运行程序，抛出异常：
     * com.fasterxml.jackson.core.JsonParseException: Unexpected character ('/' (code 47)): maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)
     * at [Source: (String)"{
     *     "name" : "YourBarman", // 名字
     *     "age" : 18 // 年龄
     * }"; line: 2, column: 26]
     *
     * 放开注释的代码，再次运行程序，正常work
     *
     */
    @Test
    public void test4() throws IOException {
        String jsonStr = "{\n" + "\t\"name\" : \"YourBarman\", // 名字\n" + "\t\"age\" : 18 // 年龄\n" + "}";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // 开启注释支持
            //jsonParser.enable(JsonParser.Feature.ALLOW_COMMENTS);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("name".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getText());
                } else if ("age".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getIntValue());
                }
            }
        }
    }
    // 支持非标准格式
    /**
     * ALLOW_YAML_COMMENTS(false) 自2.10版本后，使用JsonReadFeature#ALLOW_YAML_COMMENTS代替
     * 顾名思义，开启后将支持Yaml格式的的注释，也就是#形式的注释语法。
     *
     * ALLOW_UNQUOTED_FIELD_NAMES(false) 自2.10版本后，使用JsonReadFeature#ALLOW_UNQUOTED_FIELD_NAMES代替
     * 是否允许属性名不带双引号""，比较简单，示例略。
     *
     * ALLOW_SINGLE_QUOTES(false) 自2.10版本后，使用JsonReadFeature#ALLOW_SINGLE_QUOTES代替
     * 是否允许属性名支持单引号，也就是使用''包裹，形如这样：
     * {
     *     'age' : 18
     * }
     *
     * ALLOW_UNQUOTED_CONTROL_CHARS(false) 自2.10版本后，使用JsonReadFeature#ALLOW_UNESCAPED_CONTROL_CHARS代替
     * 是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 由于JSON规范要求对所有控制字符使用引号，这是一个非标准的特性，因此默认禁用。
     *
     * ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false) 自2.10版本后，使用JsonReadFeature#ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER代替
     * 是否允许 反斜杠 转义任何字符。这句话不是非常好理解，看下面这个例子：
     * 运行程序，报错：
     * com.fasterxml.jackson.core.JsonParseException: Unrecognized character escape ''' (code 39)
     * at [Source: (String)"{"name" : "YourB\'atman" }"; line: 1, column: 19]
     * ...
     * 放开注释掉的代码，再次运行程序，一切正常，输出：YourB'atman。
     *
     */
    @Test
    public void test5() throws IOException {
        String jsonStr = "{\"name\" : \"YourB\\'atman\" }";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // jsonParser.enable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("name".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getText());
                }
            }
        }
    }

    /**
     * ALLOW_NUMERIC_LEADING_ZEROS(false) 自2.10版本后，使用JsonReadFeature#ALLOW_LEADING_ZEROS_FOR_NUMBERS代替
     * 是否允许像00001这样的“数字”出现（而不报错）。看例子：
     *
     * 运行程序，输出：
     * com.fasterxml.jackson.core.JsonParseException: Invalid numeric value: Leading zeroes not allowed
     * at [Source: (String)"{"age" : 00018 }"; line: 1, column: 11]
     * ...
     * 放开注掉的代码，再次运行程序，一切正常。输出18。
     *
     */
    @Test
    public void test6() throws IOException {
        String jsonStr = "{\"age\" : 00018 }";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // jsonParser.enable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("age".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getIntValue());
                }
            }
        }
    }

    /**
     * ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS(false) 自2.10版本后，使用JsonReadFeature#ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS代替
     * 是否允许小数点.打头，也就是说.1这种小数格式是否合法。默认是不合法的，需要开启此特征才能支持，例子就略了，基本同上。
     *
     * 运行程序，输出：
     * com.fasterxml.jackson.core.JsonParseException: Invalid numeric value: Leading zeroes not allowed
     * at [Source: (String)"{"age" : 00018 }"; line: 1, column: 11]
     * ...
     * 放开注掉的代码，再次运行程序，一切正常。输出8。
     */
    @Test
    public void test7() throws IOException {
        String jsonStr = "{\"age\" : 8.1}";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            jsonParser.enable(JsonParser.Feature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("age".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getIntValue());
                }
            }
        }
    }

    /**
     * ALLOW_NON_NUMERIC_NUMBERS(false) 自2.10版本后，使用JsonReadFeature#ALLOW_NON_NUMERIC_NUMBERS代替
     * 是否允许一些解析器识别一组**“非数字”(如NaN)**作为合法的浮点数值。这个属性和上篇文章的JsonGenerator#QUOTE_NON_NUMERIC_NUMBERS特征值是遥相呼应的。
     *
     * 运行程序，抛错：
     * com.fasterxml.jackson.core.JsonParseException: Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow
     * at [Source: (String)"{"percent" : NaN }"; line: 1, column: 17]
     *
     * 放开注释掉的代码，再次运行，一切正常。输出：NaN
     *
     */
    @Test
    public void test8() throws IOException {
        String jsonStr = "{\"percent\" : NaN }";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            jsonParser.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("percent".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getFloatValue());
                }
            }
        }
    }

    /**
     * ALLOW_MISSING_VALUES(false) 自2.10版本后，使用JsonReadFeature#ALLOW_MISSING_VALUES代替
     * 是否允许支持JSON数组中“缺失”值。怎么理解：数组中缺失了值表示两个逗号之间，啥都没有，形如这样[value1, , value3]。
     *
     * 运行程序，抛错：
     * YourBatman // 能输出一个，毕竟第一个part（JsonToken）是正常的嘛
     *
     * com.fasterxml.jackson.core.JsonParseException: Unexpected character (',' (code 44)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false')
     * at [Source: (String)"{"names" : ["YourBatman",,"A哥",,] }"; line: 1, column: 27]
     *
     * 放开注释掉的代码，再次运行，一切正常，结果为：
     * YourBatman
     * null
     * A哥
     * null
     * null
     *
     * 小贴士：此处用的String类型展示结果，是因为null可以作为String类型（jsonParser.getText()得到null是合法的）。
     * 但如果你使用的int类型（或者bool类型），那么如果是null的话就报错喽Current token (VALUE_NULL) not of boolean type
     */
    @Test
    public void test9() throws IOException {
        String jsonStr = "{\"names\" : [\"YourBatman\",,\"A哥\",,] }";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // jsonParser.enable(JsonParser.Feature.ALLOW_MISSING_VALUES);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("names".equals(fieldname)) {
                    jsonParser.nextToken();

                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        System.out.println(jsonParser.getText());
                    }
                }
            }
        }
    }

    /**
     * ALLOW_TRAILING_COMMA(false) 自2.10版本后，使用JsonReadFeature#ALLOW_TRAILING_COMMA代替
     * 是否允许最后一个多余的逗号（一定是最后一个）。这个特征是非常重要的，若开关打开，有如下效果：
     * [true,true,]等价于[true, true]
     * {“a”: true,}等价于{“a”: true}
     *
     * 当这个特征和上面的ALLOW_MISSING_VALUES特征同时使用时，本特征优先级更高。也就是说：会先去除掉最后一个逗号后，再进行数组长度的计算。
     *
     * 举个例子：当然这两个特征开关都打开时，[true,true,]等价于[true, true]好理解；并且呢，[true,true,,]是等价于[true, true, null]的哦，可千万别忽略最后的这个null。
     *
     * 运行程序，输出：
     * YourBatman
     * null
     * A哥
     * null
     * null
     *
     * 现在我放开注释掉的代码，再次运行，结果为：
     * YourBatman
     * null
     * A哥
     * null
     *
     */
    @Test
    public void test10() throws IOException {
        String jsonStr = "{\"results\" : [true,true,,] }";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            jsonParser.enable(JsonParser.Feature.ALLOW_MISSING_VALUES);
            // jsonParser.enable(JsonParser.Feature.ALLOW_TRAILING_COMMA);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("results".equals(fieldname)) {
                    jsonParser.nextToken();

                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        System.out.println(jsonParser.getBooleanValue());
                    }
                }
            }
        }
    }

    // 校验相关 Jackson在JSON标准之外，给出了两个校验相关的特征
    /**
     * STRICT_DUPLICATE_DETECTION(false) 自2.10版本后，使用StreamReadFeature#STRICT_DUPLICATE_DETECTION代替
     * 是否允许JSON串有两个相同的属性key，默认是允许的。
     *
     * 运行程序，正常输出：
     * 18
     * 28
     * 若放开注释代码，再次运行，则抛错：
     * 18 // 第一个数字还是能正常输出的哟
     *
     * com.fasterxml.jackson.core.JsonParseException: Duplicate field 'age'
     * at [Source: (String)"{"age":18, "age": 28 }"; line: 1, column: 17]
     *
     */
    @Test
    public void test11() throws IOException {
        String jsonStr = "{\"age\":18, \"age\": 28 }";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // jsonParser.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("age".equals(fieldname)) {
                    jsonParser.nextToken();
                    System.out.println(jsonParser.getIntValue());
                }
            }
        }
    }

    /**
     * IGNORE_UNDEFINED(false) 自2.10版本后，使用StreamReadFeature#IGNORE_UNDEFINED代替
     * 是否忽略没有定义的属性key。和JsonGenerator.Feature#IGNORE_UNKNOWN的这个特征一样，它作用于预先定义了格式的数据类型，如Avro、protobuf等等，JSON是不需要预先定义的哦~
     *
     * 同样的，你可以通过这个API预先设置格式：
     * JsonParser:
     *
     * public void setSchema(FormatSchema schema) {
     *     ...
     * }
     *
     */

    // 其它
    /**
     * INCLUDE_SOURCE_IN_LOCATION(true) 自2.10版本后，使用StreamReadFeature#INCLUDE_SOURCE_IN_LOCATION代替
     * 是否构建JsonLocation对象来表示每个part的来源，你可以通过JsonParser#getCurrentLocation()来访问。作用不大，就此略过。
     *
     */



}
