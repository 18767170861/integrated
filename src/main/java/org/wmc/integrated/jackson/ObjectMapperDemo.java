package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectMapperDemo {

    /**
     * 简单数据绑定：比如绑定int类型、List、Map等…
     *
     * 运行程序，输出：
     * 1
     * {name=YourBatman}
     */
    @Test
    public void test1() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 绑定简单类型 和 Map类型
        Integer age = objectMapper.readValue("1", int.class);
        Map map = objectMapper.readValue("{\"name\": \"YourBatman\"}", Map.class);
        System.out.println(age);
        System.out.println(map);
    }

    /**
     * 完全数据绑定：绑定到任意的Java Bean对象
     *
     * 运行程序，输出：Person(name=YourBatman, age=18)
     */
    @Test
    public void test2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue("{\"name\": \"YourBatman\", \"age\": 18}", Person.class);
        System.out.println(person);
    }

    /**
     * 写（序列化）提供writeValue()系列方法用于写数据（可写任何类型），也就是我们常说的序列化。
     *
     * writeValue(File resultFile, Object value)：写到目标文件里
     * writeValue(OutputStream out, Object value)：写到输出流
     * String writeValueAsString(Object value)：写成字符串形式，此方法最为常用
     * writeValueAsBytes(Object value)：写成字节数组byte[]
     *
     * 运行程序，输出：
     * ----------写简单类型----------
     * 18
     * "YourBatman"
     * ----------写集合类型----------
     * [1,2,3]
     * {"zhName":"A哥","enName":"YourBatman"}
     * ----------写POJO----------
     * {"name":"A哥","age":18}
     */
    @Test
    public void test3() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("----------写简单类型----------");
        System.out.println(objectMapper.writeValueAsString(18));
        System.out.println(objectMapper.writeValueAsString("YourBatman"));

        System.out.println("----------写集合类型----------");
        System.out.println(objectMapper.writeValueAsString(Arrays.asList(1, 2, 3)));
        System.out.println(objectMapper.writeValueAsString(new HashMap<String, String>() {{
            put("zhName", "A哥");
            put("enName", "YourBatman");
        }}));

        System.out.println("----------写POJO----------");
        System.out.println(objectMapper.writeValueAsString(new Person("A哥", 18)));
    }

    /**
     * 读（反序列化） 提供readValue()系列方法用于读数据（一般读字符串类型），也就是我们常说的反序列化。
     * readValue(String content, Class<T> valueType)：读为指定class类型的对象，此方法最常用
     * readValue(String content, TypeReference<T> valueTypeRef)：T表示泛型类型，如List<T>这种类型，一般用于集合/Map的反序列化
     * readValue(String content, JavaType valueType)：Jackson内置的JavaType类型，后再详解（使用并不多）
     *
     * 运行程序，输出：
     * ----------读简单类型----------
     * 18
     * ----------读集合类型----------
     * [1, 2, 3]
     * {zhName=A哥, enName=YourBatman}
     * ----------读POJO----------
     * Person(name=A哥, age=18)
     */
    @Test
    public void test4() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("----------读简单类型----------");
        System.out.println(objectMapper.readValue("18", Integer.class));
        // 抛错：JsonParseException 单独的一个串，解析会抛错
        // System.out.println(objectMapper.readValue("YourBatman", String.class));

        System.out.println("----------读集合类型----------");
        System.out.println(objectMapper.readValue("[1,2,3]", List.class));
        System.out.println(objectMapper.readValue("{\"zhName\":\"A哥\",\"enName\":\"YourBatman\"}", Map.class));

        System.out.println("----------读POJO----------");
        System.out.println(objectMapper.readValue("{\"name\":\"A哥\",\"age\":18}", Person.class));
    }

    /**
     * 泛型擦除问题
     * Java 在编译时会在字节码里指令集之外的地方保留部分泛型信息
     * 泛型接口、类、方法定义上的所有泛型、成员变量声明处的泛型都会被保留类型信息，其它地方的泛型信息都会被擦除
     *
     * @Test
     * public void test5() throws JsonProcessingException {
     *     ObjectMapper objectMapper = new ObjectMapper();
     *
     *     System.out.println("----------读集合类型----------");
     *     List<Long> list = objectMapper.readValue("[1,2,3]", List.class);
     *
     *     Long id = list.get(0);
     *     System.out.println(id);
     * }
     * 运行程序，抛错：
     * ----------读集合类型----------
     * java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
     * 异常栈里指出：Long id = list.get(0);这一句出现了类型转换异常，这便是问题原因所在：泛型擦除，参考图示如下（明明泛型类型是Long，但实际装的是Integer类型）：
     *
     * 方案一：利用成员变量保留泛型
     * 理论依据：成员变量的泛型类型不会被擦除
     * 运行程序，一切正常：
     * ----------读集合类型----------
     * 1
     */
    @Test
    public void test6() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("----------读集合类型----------");
        Data data = objectMapper.readValue("{\"ids\" : [1,2,3]}", Data.class);

        Long id = data.getIds().get(0);
        System.out.println(id);
    }

    @lombok.Data
    private static class Data {
        private List<Long> ids;
    }

    /**
     * 方案二：使用官方推荐的TypeReference<T>
     * 官方早早就为我们考虑好了这类泛型擦除的问题，所以它提供了TypeReference<T>方便我们把泛型类型保留下来，使用起来是非常的方便的：
     *
     * 运行程序，一切正常：
     * ----------读集合类型----------
     * 1
     */
    @Test
    public void test7() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("----------读集合类型----------");
        List<Long> ids = objectMapper.readValue("[1,2,3]", new TypeReference<List<Long>>() {
        });

        Long id = ids.get(0);
        System.out.println(id);
    }

    /**
     * JsonMapper
     * 自2.10版本起，给ObjectMapper提供了一个子类：JsonMapper，使得语义更加明确，专门用于处理JSON格式。
     * 严格意义上讲，ObjectMapper不局限于处理JSON格式，比如后面会讲到的它的另外一个子类YAMLMapper用于对Yaml格式的支持（需额外导包，后面见~）
     * 另外，由于构建一个ObjectMapper实例属于高频动作，因此Jackson也顺应潮流的提供了MapperBuilder构建器（2.10版本起）。
     * 我们可以通过此构建起很容易的得到一个ObjectMapper（以JsonMapper为例）实例来使用：
     *
     * 运行程序，正常输出：
     * Person(name=YourBatman, age=18)
     */
    @Test
    public void test8() throws JsonProcessingException {
        JsonMapper jsonMapper = JsonMapper.builder()
                .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true)
                .build();

        Person person = jsonMapper.readValue("{'name': 'YourBatman', 'age': 18}", Person.class);
        System.out.println(person);
    }

}