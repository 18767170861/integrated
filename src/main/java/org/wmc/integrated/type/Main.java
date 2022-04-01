package org.wmc.integrated.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //Map<String, Integer> map = new HashMap<String, Integer>();
        //
        //Type type = map.getClass().getGenericSuperclass(); // 获取HashMap父类AbstractMap<K,V>  请注意：此处为<K,V>
        //ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
        //
        //Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); // 两个类型  一个是K，一个是V
        //for (Type typeArgument : actualTypeArguments) {
        //    System.out.println(typeArgument.getTypeName()); //k,v（泛型消失了）
        //}

        // 此处必须用匿名内部类的方式写，如果使用new HashMapEx<String,Integer> 效果同上
        Map<String, Integer> map = new HashMap<String, Integer>() {
        };

        Type type = map.getClass().getGenericSuperclass(); // 获取HashMapEx父类HashMap<K,V>
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); // 两个类型  一个是K，一个是V
        for (Type typeArgument : actualTypeArguments) {
            System.out.println(typeArgument.getTypeName()); //k,v（泛型消失了）
        }
    }

    private static class HashMapEx<K, V> extends HashMap<K, V> {
        public HashMapEx() {
            super();
        }
    }
}
