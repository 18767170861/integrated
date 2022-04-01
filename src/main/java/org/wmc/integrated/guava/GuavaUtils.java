package org.wmc.integrated.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.RateLimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuavaUtils {

    public static void main(String[] args) throws InterruptedException {
        List<String> joiner = Lists.newArrayList("a", "b", "g", "8", "9");
        String result = Joiner.on(",").join(joiner);
        /*
         * a,b,g,8,9
         * */
        System.out.println(result);

        List<String> joiner1 = Lists.newArrayList("a", "b", "g", null, "8", "9");
        String result1 = Joiner.on(",").skipNulls().join(joiner1);
        /*
         * a,b,g,8,9
         * */
        System.out.println(result1);

        List<String> joiner2 = Lists.newArrayList("a", "b", "g", null, "8", "9");
        String result2 = Joiner.on(",").useForNull("哈哈").join(joiner2);
        /*
         * a,b,g,哈哈,8,9
         * */
        System.out.println(result2);

        Map<Integer, String> maps = Maps.newHashMap();
        maps.put(1, "哈哈");
        maps.put(2, "压压");
        String result3 = Joiner.on(",").withKeyValueSeparator(":").join(maps);
        /*
         * 1:哈哈,2:压压
         * */
        System.out.println(result3);
        System.out.println(maps);

        // 把字符串按固定长度分割
        String test = "343443434哈哈";
        List<String> lists1 = Splitter.fixedLength(3).splitToList(test);
        /*
         * [343, 443, 434, 哈哈]
         * */
        System.out.println(lists1);

        List<String> lists = Lists.newArrayList("a", "b", "g", null, "8", "9");
        Multimap<Integer, String> bMultimap = ArrayListMultimap.create();
        lists.forEach(v -> {
            bMultimap.put(1, v);
        });
        /*
         * {1=[a, b, g, null, 8, 9]}
         * */
        System.out.println(bMultimap);
        // checkArgument
        try {
            // 校验表达式是否正确，并使用占位符输出错误信息
            Preconditions.checkArgument(1 > 2, "%s is wrong", "1 > 2");
        } catch (IllegalArgumentException e) {
            print(e.getMessage()); // 1 > 2 is wrong
        }
        // checkState
        try {
            // 校验表达式是否正确，并使用占位符输出错误信息，使用方法作为表达式,一般用作校验方法返回是否为真
            Preconditions.checkState(testMethod(), "%s is wrong", "testMethod()");
        } catch (IllegalStateException e) {
            print(e.getMessage()); // testMethod() is wrong
        }
        // checkNotNull
        try {
            // 校验对象是否为空，并使用占位符输出错误信息
            Preconditions.checkNotNull(testObject(), "%s is null", "testObject()");
        } catch (NullPointerException e) {
            print(e.getMessage()); // testObject() is null
        }
        // 初始化测试用list
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        // checkElementIndex
        try {
            // 校验元素索引是否有效 ，使用checkPositionIndex校验
            Preconditions.checkElementIndex(10, list.size());
            // 在临界值size处产生异常
        } catch (IndexOutOfBoundsException e) {
            print(e.getMessage()); // index (10) must be less than size (10)
        }
        // checkPositionIndex
        try {
            // 校验元素索引是否有效，使用checkPositionIndex校验
            Preconditions.checkPositionIndex(10, list.size());
            // 在临界size处不产生异常
            // print("checkPositionIndex does not throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            print(e.getMessage()); // checkPositionIndex does not throw
            // IndexOutOfBoundsException
        }
        // checkPositionIndexes
        try {
            // 校验是否是有效的索引区间
            Preconditions.checkPositionIndexes(3, 11, list.size());
        } catch (IndexOutOfBoundsException e) {
            print(e.getMessage()); // end index (11) must not be greater than
            // size (10)
        }

        /*
         * 限流
         * get 1 tokens: 0.0s
         * get 2 tokens: 0.999026s
         * */
        RateLimiter r = RateLimiter.create(1);
        while (true) {
            System.out.println("get 1 tokens: " + r.acquire() + "s");
            System.out.println("get 2 tokens: " + r.acquire() + "s");
        }
    }

    // 打印输出方法
    private static void print(Object obj) {
        System.out.println(String.valueOf(obj));
    }

    // 测试方法
    private static boolean testMethod() {
        return 1 > 2;
    }

    // 测试对象
    private static Object testObject() {
        return null;
    }
}
