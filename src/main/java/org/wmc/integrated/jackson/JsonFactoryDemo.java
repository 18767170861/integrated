package org.wmc.integrated.jackson;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ServiceLoader;

import static com.fasterxml.jackson.core.JsonFactory.Feature.*;
import static com.fasterxml.jackson.core.json.JsonReadFeature.*;
import static com.fasterxml.jackson.core.json.JsonWriteFeature.*;


/**
 * JsonFactory是Jackson的（最）主要工厂类，用于配置和构建JsonGenerator和JsonParser，这个工厂实例是线程安全的，因此可以重复使用。
 * 作为一个实例工厂，它最重要的职责当然是创建实例对象。本工厂职责并不单一，它负责读、写两种实例的创建工作。
 *
 * 本文围绕JsonFactory工厂为核心，讲解了它是如何创建、定制读/写实例的。对于自己的实例的创建共有三种方式：
 * 直接new实例
 * 使用JsonFactoryBuilder构建（需要2.10或以上版本）
 * SPI方式创建实例
 * 其中方式2是被推荐的，如果你的版本较低，就老老实实使用方式1呗。至于方式3嘛，玩玩就行，别当真。
 */
public class JsonFactoryDemo {

    /**
     * 创建JsonGenerator实例
     * JsonGenerator它负责向目的地写数据，因此强调的是目的地在哪？如何写？
     * 一共有六个重载方法用于构建JsonGenerator实例，多个重载方法目的是对使用者友好，我们可以认为最终效果是一样的。比如，底层实现是：
     *
     *     public JsonGenerator createGenerator(DataOutput var1, JsonEncoding var2) throws IOException;
     *
     *     public JsonGenerator createGenerator(DataOutput var1) throws IOException;
     *
     *     public JsonGenerator createGenerator(File var1, JsonEncoding var2) throws IOException;
     *
     *     public JsonGenerator createGenerator(OutputStream var1) throws IOException;
     *
     *     public JsonGenerator createGenerator(OutputStream var1, JsonEncoding var2) throws IOException;
     *
     *     public JsonGenerator createGenerator(Writer var1) throws IOException;
     *
     * @Override
     * public JsonGenerator createGenerator(OutputStream out, JsonEncoding enc) throws IOException {
     *     IOContext ctxt = _createContext(out, false);
     *     ctxt.setEncoding(enc);
     *
     *     // 如果编码是UTF-8
     *     if (enc == JsonEncoding.UTF8) {
     *         return _createUTF8Generator(_decorate(out, ctxt), ctxt);
     *     }
     *     // 使用指定的编码把OutputStream包装为一个writer
     *     Writer w = _createWriter(out, enc, ctxt);
     *     return _createGenerator(_decorate(w, ctxt), ctxt);
     * }
     * 即使你自己不显示的指定编码集，默认情况下Jackson也是使用UTF-8：
     * @Override
     * public JsonGenerator createGenerator(OutputStream out) throws IOException {
     *     return createGenerator(out, JsonEncoding.UTF8);
     * }
     *
     * 运行程序，输出：
     * com.fasterxml.jackson.core.json.UTF8JsonGenerator@7f9a81e8
     * com.fasterxml.jackson.core.json.UTF8JsonGenerator@9629756
     */
    @Test
    public void test1() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();

        JsonGenerator jsonGenerator1 = jsonFactory.createGenerator(System.out);
        JsonGenerator jsonGenerator2 = jsonFactory.createGenerator(System.out, JsonEncoding.UTF8);

        System.out.println(jsonGenerator1);
        System.out.println(jsonGenerator2);
    }

    /**
     *
     * 创建JsonParser实例
     * JsonParser它负责从一个JSON字符串中提取出值，因此它强调的是数据从哪来？如何解析？
     *
     * 一共11个重载方法（其实最后一个不属于重载）用于构建JsonParser实例，它的底层实现是根据不同的数据媒介，使用了不同的处理方式，最终生成UTF8StreamJsonParser/ReaderBasedJsonParser
     *     public JsonParser createParser(byte[] var1) throws IOException;
     *
     *     public JsonParser createParser(byte[] var1, int var2, int var3) throws IOException;
     *
     *     public JsonParser createParser(char[] var1) throws IOException;
     *
     *     public JsonParser createParser(char[] var1, int var2, int var3) throws IOException;
     *
     *     public JsonParser createParser(DataInput var1) throws IOException;
     *
     *     public JsonParser createParser(File var1) throws IOException;
     *
     *     public JsonParser createParser(InputStream var1) throws IOException;
     *
     *     public JsonParser createParser(Reader var1) throws IOException;
     *
     *     public JsonParser createParser(String var1) throws IOException;
     *
     *     public JsonParser createParser(URL var1) throws IOException;
     *
     *     public JsonParser createNonBlockingByteArrayParser() throws IOException;
     *
     * 这几个重载方法均无需我们指定编码集，那它是如何确定使用何种编码去解码形如byte[]数组这种数据来源的呢？
     * 这得益于其内部的编码自动发现机制实现，也就是ByteSourceJsonBootstrapper#detectEncoding()这个方法
     *
     * 创建非阻塞实例值得注意的是，上面截图的11个方法中，最后一个并非重载。它创建的是一个非阻塞JSON解析器，也就是NonBlockingJsonParser，并且它还没有指定入参（数据源）。
     * NonBlockingJsonParser是Jackson在2.9版本新增的的一个解析器，目标是进一步提升效率、性能。但它也有局限的地方：只能解析使用UTF-8编码的内容，否则抛出异常。
     * 当然喽，现在UTF-8编码几乎成为了标准编码手段，问题不大。但是呢，我自己玩了玩NonBlockingJsonParser，发现复杂度增加不少（玩半天才玩明白），效果却并不显著，因此这里了解一下便可，至少目前不建议深入探究。
     * 小贴士：不管是Spring还是Redis的反序列化，使用的均是普通的解析器（阻塞IO）。因为JSON解析过程从来都不会是性能瓶颈（特殊场景除外）
     *
     * 运行程序，输出：
     *
     * com.fasterxml.jackson.core.json.ReaderBasedJsonParser@5419f379
     * com.fasterxml.jackson.core.json.async.NonBlockingJsonParser@7dc7cbad
     */
    @Test
    public void test2() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();

        JsonParser jsonParser1 = jsonFactory.createParser("{}");
        // JsonParser jsonParser2 = jsonFactory.createParser(new FileReader("..."));
        JsonParser jsonParser3 = jsonFactory.createNonBlockingByteArrayParser();

        System.out.println(jsonParser1);
        // System.out.println(jsonParser2);
        System.out.println(jsonParser3);
    }

    /**
     * JsonFactory的Feature
     * 除了JsonGenerator和JsonParser有Feature来控制行为外，JsonFactory也有自己的Feature特征，来控制自己的行为，可以理解为它对读/写均生效。
     * 同样的也是一个内部枚举类：
     * public enum Feature {
     *     INTERN_FIELD_NAMES(true),
     *     CANONICALIZE_FIELD_NAMES(true),
     *     FAIL_ON_SYMBOL_HASH_OVERFLOW(true),
     *     USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING(true)
     * }
     * 小贴士：枚举值均为bool类型，括号内为默认值
     * 每个枚举值都控制着JsonFactory不同的行为。
     *
     */

    /**
     * INTERN_FIELD_NAMES(true) 这是Jackson所谓的key缓存：对JSON的字段名是否调用String#intern方法，放进字符串常量池里，以提高效率，默认是true
     *
     * 小贴士：Jackson在调用String#intern之前使用InternCache（继承自ConcurrentHashMap）挡了一层，以防止高并发条件下intern效果不显著问题
     *
     * intern()方法的作用这个老生常谈的话题了，解释为：当调用intern方法时，如果字符串池已经包含一个等于此String对象的字符串(内容相等)，则返回池中的字符串。
     * 否则，将此 String放进池子里。下面写个例子增加感受感受：
     *
     * 可想而知，开启这个小功能的意义还是蛮大的。因为同一个格式的JSON串被多次解析的可能性是非常之大的，想想你的Rest API接口，被调用多少次就会进行了多少次JSON解析（想想高并发场景）。这是一种用空间换时间的思想，所以小小功能，大大能量。
     * 小贴士：如果你的应用对内存很敏感，你可以关闭此特征。但，真的有这种应用吗？有吗？
     *
     * 值得注意的是：此特征必须是CANONICALIZE_FIELD_NAMES也为true（开启）的情况下才有效，否则是无效的。
     * CANONICALIZE_FIELD_NAMES(true)
     * 是否需要规范化属性名。所谓的规范化处理，就是去字符串池里尝试找一个字符串出来，默认值为true。
     * 规范化借助的是ByteQuadsCanonicalizer去处理，简而言之会根据Hash值来计算每个属性名存放的位置~
     *
     * 小贴士：ByteQuadsCanonicalizer拥有一套优秀的Hash算法来规范化属性存储，提高效率，抵御攻击（见下特征）
     * 此特征开启了，INTERN_FIELD_NAMES特征的开启才有意义~
     */
    @Test
    public void test3() {
        String str1 = "a";
        String str2 = "b";
        String str3 = "ab";
        String str4 = str1 + str2;
        String str5 = new String("ab");

        System.out.println(str5.equals(str3)); // true
        System.out.println(str5 == str3); // false

        // str5.intern()去常量池里找到了ab，所以直接返回常量池里的地址值了，因此是true
        System.out.println(str5.intern() == str3); // true
        System.out.println(str5.intern() == str4); // false
    }

    /**
     * FAIL_ON_SYMBOL_HASH_OVERFLOW(true) 当ByteQuadsCanonicalizer处理hash碰撞达到一个阈值时，是否快速失败。
     * 什么时候能达到阈值？官方的说明是：若触发了阈值，这基本可以确定是Dos（denial-of-service）攻击，制造了非常多的相同Hash值的key，
     * 这在正常情况下几乎是没有发生的可能性的。
     * 所以，开启此特征值，可以防止攻击，在提高性能的同时也确保了安全。
     *
     * USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING(true) 是否使用BufferRecycler、ThreadLocal、SoftReference来有效的重用底层的输入/输出缓冲区。这个特性在后端服务（JavaEE）环境下是很有意义的，提效明显。
     * 但是对于在Android环境下就不见得了~
     *
     * 总而言之言而总之，JsonFactory的这几个特征值都建议开启，也就是维持默认即可。
     *
     */

    // 定制读/写实例
    /**
     * 读写行为的控制是通过各自的Feature来控制的，JsonFactory作为一个功能并非单一的工厂类，需要既能够定制化读JsonParser，也能定制化写JsonGenerator。
     * 为此，对应的API它都提供了三份（一份定制化自己的Feature）：
     * public JsonFactory enable(JsonFactory.Feature f);
     * public JsonFactory enable(JsonParser.Feature f);
     * public JsonFactory enable(JsonGenerator.Feature f);
     *
     * public JsonFactory disable(JsonFactory.Feature f);
     * public JsonFactory disable(JsonParser.Feature f);
     * public JsonFactory disable(JsonGenerator.Feature f);
     *
     * // 合二为一的Configure方法
     * public JsonFactory configure(JsonFactory.Feature f, boolean state);
     * public JsonFactory configure(JsonParser.Feature f, boolean state);
     * public JsonFactory configure(JsonGenerator.Feature f, boolean state);
     *
     * 运行程序，抛出异常。证明特征开启成功，符合预期。
     * com.fasterxml.jackson.core.JsonParseException: Duplicate field 'age'
     * at [Source: (String)"{"age":18, "age": 28 }"; line: 1, column: 17]
     *
     * 在使用JsonFactory定制化读/写实例的时需要特别注意：请务必确保在factory.createXXX()之前配置好对应的Feature特征，若在实例创建好之后再弄的话，对已经创建的实例无效。
     * 小贴士：实例创建好后若你还想定制，可以使用实例自己的对应API操作
     */
    @Test
    public void test4() throws IOException {
        String jsonStr = "{\"age\":18, \"age\": 28 }";

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

        try (JsonParser jsonParser = factory.createParser(jsonStr)) {
            // 使用factory定制将不生效
            // factory.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

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
     * JsonFactoryBuilder
     * JsonFactory负责基类和实现类的双重任务，是比较重的，分离得也不彻底。
     * 同时，现在都2020年了，对于这种构建类工厂如果还不用Builder模式就现在太out了，书写起来也非常不便：
     *
     */
    @Test
    public void test6() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        // jsonFactory自己的特征
        jsonFactory.enable(INTERN_FIELD_NAMES);
        jsonFactory.enable(JsonFactory.Feature.CANONICALIZE_FIELD_NAMES);
        jsonFactory.enable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);

        // JsonParser的特征
        jsonFactory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        jsonFactory.enable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);

        // JsonGenerator的特征
        jsonFactory.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        jsonFactory.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

        // 创建读/写实例
        // jsonFactory.createParser(...);
        // jsonFactory.createGenerator(...);
    }

    /**
     * 功能实现上没毛病，但总显得不够优雅。同时上面也说了：定制化操作一定得在create创建动作之前执行，这全靠程序员自行控制。
     * Jackson在2.10版本新增了一个JsonFactoryBuilder构件类，让我们能够基于builder模式优雅的构建出一个JsonFactory实例。
     * 小贴士：2.10版本是2019.09发布的
     * 比如上面例子的代码使用JsonFactoryBuilder可重构为：
     *
     * 对比起来，使用Builder模式优雅太多了。
     * 因为JsonFactory是线程安全的，因此一般情况下全局我们只需要一个JsonFactory实例即可，推荐使用JsonFactoryBuilder去完成你的构建。
     * 小贴士：使用JsonFactoryBuilder确保你的Jackson版本至少是2.10版本哦~
     *
     */
    @Test
    public void test7() throws IOException {
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                // jsonFactory自己的特征
                .enable(INTERN_FIELD_NAMES)
                .enable(CANONICALIZE_FIELD_NAMES)
                .enable(USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING)
                // JsonParser的特征
                .enable(ALLOW_SINGLE_QUOTES, ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                // JsonGenerator的特征
                .enable(QUOTE_FIELD_NAMES, ESCAPE_NON_ASCII)
                .build();

        // 创建读/写实例
        // jsonFactory.createParser(...);
        // jsonFactory.createGenerator(...);
    }

    /**
     * SPI方式 从源码包里发现，JsonFactory是支持Java SPI方式构建实例的。
     *
     * 文件内容为：com.fasterxml.jackson.core.JsonFactory
     *
     * 因此，我可以使用Java SPI的方式得到一个JsonFactory实例：
     *
     */
    @Test
    public void test5() {
        ServiceLoader<JsonFactory> jsonFactories = ServiceLoader.load(JsonFactory.class);
        System.out.println(jsonFactories.iterator().next());
    }


}
