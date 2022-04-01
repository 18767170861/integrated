package org.wmc.integrated.type;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;

public class TypeVariableTest<T extends Number & Serializable, V> {

    private T key;
    private V value;
    // 显然它本身是个GenericArrayType类型，里面是TypeVariable类型
    private V[] values;
    //ParameterizedType 和 TypeVariable的结合
    private List<T> tList;

    private String str;


    private static void printTypeVariable(String fieldName, TypeVariable typeVariable) {
        for (Type type : typeVariable.getBounds()) {
            System.out.println("\t\t" + fieldName + ": TypeVariable getBounds " + type);
        }
        System.out.println("\t\t定义Class getGenericDeclaration: " + typeVariable.getGenericDeclaration());
        System.out.println("\t\tgetName: " + typeVariable.getName());
    }

    public static void main(String[] args) {
        Field f = null;
        try {
            Field[] fields = TypeVariableTest.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                f = fields[i];
                if (f.getName().equals("log")) {
                    continue;
                }
                System.out.println("开始 ******当前field:" + f.getName() + " *************************");
                Type genericType = f.getGenericType();

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    for (Type type : parameterizedType.getActualTypeArguments()) {
                        System.out.println("\t获取ParameterizedType:" + type);
                        if (type instanceof TypeVariable) {
                            printTypeVariable(f.getName(), (TypeVariable) type);
                        }
                    }
                    System.out.println("\tgetOwnerType:" + parameterizedType.getOwnerType());
                    System.out.println("\tgetRawType:" + parameterizedType.getRawType());

                } else if (genericType instanceof GenericArrayType) {
                    GenericArrayType genericArrayType = (GenericArrayType) genericType;
                    System.out.println("GenericArrayType type :" + genericArrayType);
                    Type genericComponentType = genericArrayType.getGenericComponentType();
                    if (genericComponentType instanceof TypeVariable) {
                        TypeVariable typeVariable = (TypeVariable) genericComponentType;
                        printTypeVariable(f.getName(), typeVariable);
                    }
                } else if (genericType instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) genericType;
                    printTypeVariable(f.getName(), typeVariable);
                } else {
                    System.out.println("type :" + genericType);
                }
                System.out.println("结束 ******当前field:" + f.getName() + " *************************");
                System.out.println();
            }
        } catch (Exception e) {
        }
    }

}