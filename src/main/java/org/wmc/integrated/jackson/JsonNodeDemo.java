package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

/**
 * JsonNodeFactory：顾名思义，用来构造各种JsonNode节点的工厂。例如对象节点ObjectNode、数组节点ArrayNode等等
 * JsonNode：表示json节点。可以往里面塞值，从而最终构造出一颗json树
 * ObjectMapper：实现JsonNode和JSON字符串的互转
 */
public class JsonNodeDemo {

    /**
     * 值类型节点（ValueNode）
     * 此类节点均为ValueNode的子类，特点是：一个节点表示一个值。
     *
     * 运行程序，输出：
     * ------ValueNode值节点示例------
     * true:1
     * true:null
     * true_
     * true:Person(name=YourBatman, age=18)
     * ---true---
     */
    @Test
    public void test1() {
        JsonNodeFactory factory = JsonNodeFactory.instance;

        System.out.println("------ValueNode值节点示例------");
        // 数字节点
        JsonNode node = factory.numberNode(1);
        System.out.println(node.isNumber() + ":" + node.intValue());

        // null节点
        node = factory.nullNode();
        System.out.println(node.isNull() + ":" + node.asText());

        // missing节点
        node = factory.missingNode();
        System.out.println(node.isMissingNode() + "_" + node.asText());

        // POJONode节点
        node = factory.pojoNode(new Person("YourBatman", 18));
        System.out.println(node.isPojo() + ":" + node.asText());

        System.out.println("---" + node.isValueNode() + "---");
    }

    /**
     * 容器类型节点（ContainerNode）
     * 此类节点均为ContainerNode的子类，特点是：本节点代表一个容器，里面可以装任何其它节点。
     * Java中容器有两种：Map和Collection。对应的Jackson也提供了两种容器节点用于表述此类数据结构：
     * ObjectNode：类比Map，采用K-V结构存储。比如一个JSON结构，根节点 就是一个ObjectNode
     * ArrayNode：类比Collection、数组。里面可以放置任何节点
     *
     * 运行程序，输出：
     * ------构建一个JSON结构数据------
     * {"zhName":"A哥","enName":"YourBatman","age":18,"languages":["java","javascript","python"],"dog":{"name":"大黄","age":3}}
     * "大黄"
     */
    @Test
    public void test2() {
        JsonNodeFactory factory = JsonNodeFactory.instance;

        System.out.println("------构建一个JSON结构数据------");
        ObjectNode rootNode = factory.objectNode();

        // 添加普通值节点
        rootNode.put("zhName", "A哥"); // 效果完全同：rootNode.set("zhName", factory.textNode("A哥"))
        rootNode.put("enName", "YourBatman");
        rootNode.put("age", 18);

        // 添加数组容器节点
        ArrayNode arrayNode = factory.arrayNode();
        arrayNode.add("java")
                .add("javascript")
                .add("python");
        rootNode.set("languages", arrayNode);

        // 添加对象节点
        ObjectNode dogNode = factory.objectNode();
        dogNode.put("name", "大黄")
                .put("age", 3);
        rootNode.set("dog", dogNode);

        System.out.println(rootNode);
        System.out.println(rootNode.get("dog").get("name"));
    }

    /**
     * ObjectMapper中的树模型
     * 树模型其实是底层流式API所提出和支持的，典型API便是com.fasterxml.jackson.core.TreeNode。但通过前面文章的示例讲解可以知道：底层流式API仅定义了接口而并未提供任何实现，甚至半成品都算不上。所以说要使用Jackson的树模型还得看ObjectMapper，它提供了TreeNode等API的完整实现。
     * 不乏很多小伙伴对ObjectMapper的树模型是一知半解的，甚至从来都没有用过，其实它是非常灵活和强大的。有了上面的基础示例做支撑，再来了解它的实现就得心应手多了。
     *
     * ObjectMapper中提供了树模型(tree model) API 来生成和解析 json 字符串。如果你不想为你的 json 结构单独建类与之对应的话，则可以选择该 API，如下图所示：
     *     public JsonNode readTree(InputStream in)
     *     public JsonNode readTree(Reader r)
     *     public JsonNode readTree(String content)
     *     public JsonNode readTree(byte[] content)
     *     public JsonNode readTree(byte[] content, int offset, int len)
     *     public JsonNode readTree(File file)
     *     public JsonNode readTree(URL source) throws IOException
     * ObjectMapper在读取JSON后提供指向树的根节点的指针， 根节点可用于遍历完整的树。 同样的，我们可从读（反序列化）、写（序列化）两个方面来展开。
     *
     */

    // 写（序列化） 将Object写为JsonNode，ObjectMapper给我们提供了三个实用API俩操作它：
    /**
     * 1、valueToTree(Object)
     * 运行程序，控制台输出：
     * Person(name=YourBatman, age=18, dog=Person.Dog(name=旺财, age=3))
     * YourBatman
     * 18
     * 狗的属性：：：
     * "旺财"
     * 3
     * ---------------------------------------
     * "旺财"
     * 3
     */
    @Test
    public void test3() {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setName("YourBatman");
        person.setAge(18);
        person.setDog(new Person.Dog("旺财", 3));
        JsonNode node = mapper.valueToTree(person);
        System.out.println(person);
        // 遍历打印所有属性
        Iterator<JsonNode> it = node.iterator();
        while (it.hasNext()) {
            JsonNode nextNode = it.next();
            if (nextNode.isContainerNode()) {
                if (nextNode.isObject()) {
                    System.out.println("狗的属性：：：");

                    System.out.println(nextNode.get("name"));
                    System.out.println(nextNode.get("age"));
                }
            } else {
                System.out.println(nextNode.asText());
            }
        }

        // 直接获取
        System.out.println("---------------------------------------");
        System.out.println(node.get("dog").get("name"));
        System.out.println(node.get("dog").get("age"));
    }

    /**
     * 2.writeTree(JsonGenerator, JsonNode)
     * 将一个JsonNode使用JsonGenerator写到输出流里，此方法直接使用到了JsonGenerator这个API，灵活度杠杠的，但相对偏底层
     * 3.writeTree(JsonGenerator,TreeNode)
     * JsonNode是TreeNode的实现类，
     *
     * 运行程序，控制台输出：
     * {"name":"YourBatman","age":18,"dog":null}
     */
    @Test
    public void test4() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jsonGenerator = factory.createGenerator(System.err, JsonEncoding.UTF8)) {

            // 1、得到一个jsonNode（为了方便我直接用上面API生成了哈）
            Person person = new Person();
            person.setName("YourBatman");
            person.setAge(18);
            JsonNode jsonNode = mapper.valueToTree(person);

            // 使用JsonGenerator写到输出流
            mapper.writeTree(jsonGenerator, jsonNode);
        }
    }

    // 读（反序列化） 将一个资源（如字符串）读取为一个JsonNode树模型。
    /**
     * public JsonNode readTree(InputStream in)
     * public JsonNode readTree(Reader r)
     * public JsonNode readTree(String content)
     * public JsonNode readTree(byte[] content)
     * public JsonNode readTree(byte[] content, int offset, int len)
     * public JsonNode readTree(File file)
     * public JsonNode readTree(URL source) throws IOException
     * 这是典型的方法重载设计，API更加友好，所有方法底层均为_readTreeAndClose()这个protected方法，可谓“万剑归宗”。
     *
     */
    @Test
    public void test5() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18,\"dog\":null}";
        // 直接映射为一个实体对象
        // mapper.readValue(jsonStr, Person.class);
        // 读取为一个树模型
        JsonNode node = mapper.readTree(jsonStr);
        // {"name":"YourBatman","age":18,"dog":null}
        System.out.println(node);
        // ... 略
    }

    // 场景演练
    /**
     * 1.偌大JSON串中仅需1个值
     * 这种场景其实还蛮常见的，比如有个很经典的场景便是在MQ消费中：生产者一般会恨不得把它能吐出来的属性尽可能都扔出来，但对于不同的消费者而言它们的所需往往是不一样的：
     * 需要较多的属性值，这时候用完全数据绑定转换成POJO来操作更为方便和合理
     * 需要1个(较少)的属性值，这时候“杀鸡岂能用牛刀”呢，这种case使用树模型来做就显得更为优雅和高效了
     * 譬如，生产者生产的消息JSON串如下（模拟数据，总之你就当做它属性很多、嵌套很深就对了）：
     * {"name":"YourBatman","age":18,"dog":{"name":"旺财","color":"WHITE"},"hobbies":["篮球","football"]}
     * 这时候，我仅关心狗的颜色，肿么办呢？相信你已经想到了：树模型
     *
     * 运行程序，控制台输出：WHITE，目标达成。值得注意的是：如果node.get("dog")没有这个节点(或者值为null)，是会抛出NPE异常的，因此请你自己保证代码的健壮性。
     * 当你不想创建一个Java Bean与JSON属性相对应时，树模型的所见即所得特性就很好解决了这个问题。
     */
    @Test
    public void test6() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18,\"dog\":{\"name\":\"旺财\",\"color\":\"WHITE\"},\"hobbies\":[\"篮球\",\"football\"]}";
        JsonNode node = mapper.readTree(jsonStr);

        System.out.println(node.get("dog").get("color").asText());
    }

    /**
     * 2、数据结构高度动态化
     * 当数据结构高度动态化（随时可能新增、删除节点）时，使用树模型去处理是一个较好的方案（稳定之后再转为Java Bean即可）。这主要是利用了树模型它具有动态可扩展的特性，满足我们日益变化的结构：
     * 运行程序，控制台输出：
     * -------------向结构里动态添加节点------------
     * {"name":"YourBatman","age":18,"myDiy":{"contry":"China"},"aaa":"aaa"}
     */
    @Test
    public void test7() throws JsonProcessingException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18}";

        JsonNode node = new ObjectMapper().readTree(jsonStr);

        System.out.println("-------------向结构里动态添加节点------------");
        // 动态添加一个myDiy节点，并且该节点还是ObjectNode节点
        ((ObjectNode) node).with("myDiy").put("contry", "China");
        ((ObjectNode) node).put("aaa", "aaa");
        System.out.println(node);
    }
}
