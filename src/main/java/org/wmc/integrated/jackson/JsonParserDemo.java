package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.IOException;

public class JsonParserDemo {

    @Test
    public void test1() throws IOException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18}";
        Person person = new Person();

        JsonFactory factory = new JsonFactory();
        /**
         * // 获取字符串类型
         * public abstract String getText() throws IOException;
         *
         * // 数字Number类型值 标量值（支持的Number类型参照NumberType枚举）
         * public abstract Number getNumberValue() throws IOException;
         * public enum NumberType {
         * INT, LONG, BIG_INTEGER, FLOAT, DOUBLE, BIG_DECIMAL
         * };
         *
         * public abstract int getIntValue() throws IOException;
         * public abstract long getLongValue() throws IOException;
         * ...
         * public abstract byte[] getBinaryValue(Base64Variant bv) throws IOException;
         *
         * 小贴士：如果value值是null，像getIntValue()、getBooleanValue()等这种直接获取方法是会抛出异常的，但getText()不会
         *
         * 带默认值的值获取，具有更好安全性：
         *
         * public String getValueAsString() throws IOException {
         *     return getValueAsString(null);
         * }
         * public abstract String getValueAsString(String def) throws IOException;
         * ...
         * public long getValueAsLong() throws IOException {
         *     return getValueAsLong(0);
         * }
         * public abstract long getValueAsLong(long def) throws IOException;
         *
         * 此类方法若碰到数据的转换失败时，不会抛出异常，把def作为默认值返回。
         *
         */
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // 只要还没结束"}"，就一直读
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jsonParser.getCurrentName();
                if ("name".equals(fieldname)) {
                    jsonParser.nextToken();
                    person.setName(jsonParser.getText());
                } else if ("age".equals(fieldname)) {
                    jsonParser.nextToken();
                    person.setAge(jsonParser.getIntValue());
                }
            }
            System.out.println(person);// Person(name=YourBatman, age=18)
        }
    }

    /**
     * 组合方法
     */
    @Test
    public void test2() throws IOException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18}";
        Person person = new Person();

        JsonFactory factory = new JsonFactory();
        /**
         * START_OBJECT
         * name
         * YourBatman
         * age
         * 18
         * END_OBJECT
         */
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            System.out.println(jsonParser.nextToken());
            System.out.println(jsonParser.nextFieldName());
            System.out.println(jsonParser.nextTextValue());
            System.out.println(jsonParser.nextFieldName());
            System.out.println(jsonParser.nextIntValue(0));
            System.out.println(jsonParser.nextToken());
        }
    }

    /**
     * 自定义一个ObjectCodec，Person类专用：用于把JSON串自动绑定到实例属性。
     *
     */
    @Test
    public void test3() throws IOException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18, \"pickName\":null}";

        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            jsonParser.setCodec(new PersonObjectCodec());

            System.out.println(jsonParser.readValueAs(Person.class));
        }
    }

    /**
     * JsonToken 它表示解析JSON内容时，用于返回结果的基本标记类型的枚举。
     * public enum JsonToken {
     *     NOT_AVAILABLE(null, JsonTokenId.ID_NOT_AVAILABLE),
     *
     *     START_OBJECT("{", JsonTokenId.ID_START_OBJECT),
     *     END_OBJECT("}", JsonTokenId.ID_END_OBJECT),
     *     START_ARRAY("[", JsonTokenId.ID_START_ARRAY),
     *     END_ARRAY("]", JsonTokenId.ID_END_ARRAY),
     *
     *     // 属性名（key）
     *     FIELD_NAME(null, JsonTokenId.ID_FIELD_NAME),
     *
     *     // 值（value）
     *     VALUE_EMBEDDED_OBJECT(null, JsonTokenId.ID_EMBEDDED_OBJECT),
     *     VALUE_STRING(null, JsonTokenId.ID_STRING),
     *     VALUE_NUMBER_INT(null, JsonTokenId.ID_NUMBER_INT),
     *     VALUE_NUMBER_FLOAT(null, JsonTokenId.ID_NUMBER_FLOAT),
     *     VALUE_TRUE("true", JsonTokenId.ID_TRUE),
     *     VALUE_FALSE("false", JsonTokenId.ID_FALSE),
     *     VALUE_NULL("null", JsonTokenId.ID_NULL),
     * }
     * 运行程序，输出：
     *
     * {"name":"YourBatman","age":18, "pickName":null}
     * START_OBJECT -> 值为:null
     * FIELD_NAME -> 值为:name
     * VALUE_STRING -> 值为:YourBatman
     * FIELD_NAME -> 值为:age
     * VALUE_NUMBER_INT -> 值为:18
     * FIELD_NAME -> 值为:pickName
     * VALUE_NULL -> 值为:null
     * END_OBJECT -> 值为:null
     */
    @Test
    public void test4() throws IOException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18, \"pickName\":null}";
        System.out.println(jsonStr);
        JsonFactory factory = new JsonFactory();
        try (JsonParser jsonParser = factory.createParser(jsonStr)) {

            while (true) {
                JsonToken token = jsonParser.nextToken();
                System.out.println(token + " -> 值为:" + jsonParser.getValueAsString());

                if (token == JsonToken.END_OBJECT) {
                    break;
                }
            }
        }
    }
}
