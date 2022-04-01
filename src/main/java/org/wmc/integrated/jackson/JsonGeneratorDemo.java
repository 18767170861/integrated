package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class JsonGeneratorDemo {

    public static void main(String[] args) throws IOException {

        JsonFactory factory = new JsonFactory();
        // 本处只需演示，向控制台写（当然你可以向文件等任意地方写都是可以的）
        try (JsonGenerator jsonGenerator = factory.createGenerator(System.out, JsonEncoding.UTF8)) {
            // test1 组合写JSON Key和Value
            jsonGenerator.writeStartObject(); //开始写，也就是这个符号 {
            jsonGenerator.writeStringField("name", "YourBatman");
            jsonGenerator.writeNumberField("age", 18);
            jsonGenerator.writeEndObject(); //结束写，也就是这个符号 } {"name":"YourBatman","age":18}

            // test2 字符串
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("zhName");
            jsonGenerator.writeString("A哥");
            jsonGenerator.writeFieldName("enName");
            jsonGenerator.writeString("YourBatman");
            jsonGenerator.writeEndObject(); // {"zhName":"A哥","enName":"YourBatman"}

            // test3
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("zhName");
            jsonGenerator.writeString("A哥");
            // 写对象（记得先写key 否则无效）
            jsonGenerator.writeFieldName("person");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("enName");
            jsonGenerator.writeString("YourBatman");
            jsonGenerator.writeFieldName("age");
            jsonGenerator.writeNumber(18);
            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject(); // {"zhName":"A哥","person":{"enName":"YourBatman","age":18}}

            // test4
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("zhName");
            jsonGenerator.writeString("A哥");
            // 写数组（记得先写key 否则无效）
            jsonGenerator.writeFieldName("objects");
            jsonGenerator.writeStartArray();
            // 1、写字符串
            jsonGenerator.writeString("YourBatman");
            // 2、写对象
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("enName", "YourBatman");
            jsonGenerator.writeEndObject();
            // 3、写数字
            jsonGenerator.writeNumber(18);
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();// {"zhName":"A哥","objects":["YourBatman",{"enName":"YourBatman"},18]}\

            // test5 一次性便捷的写入单个数组
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("zhName");
            jsonGenerator.writeString("A哥");
            // 快捷写入数组（从第index = 2位开始，取3个）
            jsonGenerator.writeFieldName("values");
            jsonGenerator.writeArray(new int[]{1, 2, 3, 4, 5, 6}, 2, 3);
            jsonGenerator.writeEndObject();// {"zhName":"A哥","values":[3,4,5]}

            // test6 布尔和null
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("success");
            jsonGenerator.writeBoolean(true);
            jsonGenerator.writeFieldName("myName");
            jsonGenerator.writeNull();
            jsonGenerator.writeEndObject();// {"success":true,"myName":null}

            // test7 writeRaw()和writeRawValue()：
            jsonGenerator.writeRaw("{'name':'YourBatman'}"); //{'name':'YourBatman'}

            // test8 自定义ObjectCodec解码器 用于把User写为JSON
            jsonGenerator.setCodec(new UserObjectCodec());
            jsonGenerator.writeObject(new User()); // {"name":"YourBatman","age":18}

            // test9 自定义ObjectCodec解码器 用于把TreeNode写为JSON
            jsonGenerator.setCodec(new UserTreeNodeCodec());
            jsonGenerator.writeObject(new UserTreeNode(new User()));// {"name":"YourBatman","age":18}


        }
    }
}
