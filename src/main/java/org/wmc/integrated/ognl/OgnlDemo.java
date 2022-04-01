package org.wmc.integrated.ognl;

import ognl.*;

/**
 * OgnlContext用法
 * 1.使用Ognl表达式语言取值，如果取非根元素的值，必须用#号
 * 2.使用Ognl表达式语言取值，如果取根元素的值，不用#号
 * 3.Ognl可以调用静态方法
 */
public class OgnlDemo {

    public static void main(String[] args) {

        ADemo a = new ADemo();
        a.setName("yihui");
        a.setAge(10);

        PrintDemo print = new PrintDemo();
        print.setPrefix("ognl");
        print.setADemo(a);

        OgnlDemo ognlDemo = new OgnlDemo();
        // 构建一个OgnlContext对象
        OgnlContext context = (OgnlContext) Ognl.createDefaultContext(ognlDemo,
                new DefaultMemberAccess(true),
                new DefaultClassResolver(),
                new DefaultTypeConverter());

        context.setRoot(print);
        context.put("print", print);
        context.put("a", a);
        Object ans;
        Object ex;
        try {
            /**
             * 实例方法调用
             * name: 一灰灰blog age: 18
             * 实例方法执行： null
             */
            ans = Ognl.getValue(Ognl.parseExpression("#print.sayHello(\"一灰灰blog\", 18)"), context, context.getRoot());
            System.out.println("实例方法执行： " + ans);

            /**
             * 实例成员属性访问
             * 实例属性设置： 一灰灰Blog
             * 实例属性访问： 一灰灰Blog
             */
            ans = Ognl.getValue(Ognl.parseExpression("#a.name=\"一灰灰Blog\""), context, context.getRoot());
            System.out.println("实例属性设置： " + ans);

            ans = Ognl.getValue(Ognl.parseExpression("#a.name"), context, context.getRoot());
            System.out.println("实例属性访问： " + ans);

            // 注册到ognlContext
            BDemo b = new BDemo();
            b.setName("b name");
            b.setAge(20);
            b.setAddress("测试ing");
            context.put("b", b);

            // 测试case
            ans = Ognl.getValue(Ognl.parseExpression("#b.name"), context, context.getRoot());
            // 实例父类属性访问：b name
            System.out.println("实例父类属性访问：" + ans);

            /**
             * 静态类方法调用
             */
            ans = Ognl.getValue(Ognl.parseExpression("@org.wmc.integrated.ognl.StaticDemo@showDemo(20)"), context,
                    context.getRoot());
            // 静态类方法执行：20
            System.out.println("静态类方法执行：" + ans);

            /**
             * 静态类成员访问
             * 直接设置静态变量，抛出了移仓，提示InappropriateExpressionException
             */
            ans = Ognl.getValue(Ognl.parseExpression("@org.wmc.integrated.ognl.StaticDemo@num"), context,
                    context.getRoot());
            // 静态类成员访问：56
            System.out.println("静态类成员访问：" + ans);

            //ans = Ognl.getValue(Ognl.parseExpression("@org.wmc.integrated.ognl.StaticDemo@num=1314"), context,
            //        context.getRoot());
            System.out.println("静态类成员设置：" + ans);

            /**
             * class类型参数
             * class: ADemo(name=xx, age=20)
             * class 参数方法执行：ADemo(name=xx, age=20)
             * class: ADemo(name=haha, age=10)
             * class 参数方法执行：ADemo(name=haha, age=10)
             */
            ans = Ognl.getValue(Ognl.parseExpression(
                    "#print.print(\"{'name':'xx', 'age': 20}\", @org.wmc.integrated.ognl.ADemo@class)"), context,
                    context.getRoot());
            System.out.println("class 参数方法执行：" + ans);

            ans = Ognl.getValue(Ognl.parseExpression("#print.print(\"{'name':'haha', 'age': 10}\", #a.getClass())"),
                    context, context.getRoot());
            System.out.println("class 参数方法执行：" + ans);

            /**
             * 枚举参数
             *
             * enum: print enum:CONSOLE
             * 枚举参数方法执行：null
             */
            ans = Ognl.getValue(
                    Ognl.parseExpression("#print.print(\"print enum\", @org.wmc.integrated.ognl.OgnlEnum@CONSOLE)"),
                    context, context.getRoot());
            System.out.println("枚举参数方法执行：" + ans);

            /**
             * null传参
             *
             * ognl => null
             * null 传参：null
             */
            ans = Ognl.getValue(Ognl.parseExpression("#print.print(null)"), context, context.getRoot());
            System.out.println("null 传参：" + ans);

            /**
             * 有多个重载的case，那么两个参数都传null
             *
             * class: null
             * null 传参：null
             */
            ans = Ognl.getValue(Ognl.parseExpression("#print.print(null, null)"), context, context.getRoot());
            System.out.println("null 传参：" + ans);

            /**
             * 对象传递
             *
             * obj: 对象构建:ADemo(name=test, age=20)
             * 对象传参：null
             * obj: 对象构建:ADemo(name=一灰灰, age=null)
             * 对象传参：null
             */
            ex = Ognl.parseExpression("#print.print(\"对象构建\", new org.wmc.integrated.ognl.ADemo(\"test\", 20))");
            ans = Ognl.getValue(ex, context, context.getRoot());
            System.out.println("对象传参：" + ans);

            ex = Ognl.parseExpression("#print.print(\"对象构建\", (#demo=new org.wmc.integrated.ognl.ADemo(), #demo.setName(\"一灰灰\"), #demo))");
            ans = Ognl.getValue(ex, context, context.getRoot());
            System.out.println("对象传参：" + ans);


            /**
             * 容器传参
             *
             * [1, 3, 5]
             * List传参：null
             * {A=1, b=3, c=5}
             * Map传参：null
             */
            ex = Ognl.parseExpression("#print.print({1, 3, 5})");
            ans = Ognl.getValue(ex, context, context.getRoot());
            System.out.println("List传参：" + ans);

            ex = Ognl.parseExpression("#print.print(#{\"A\": 1, \"b\": 3, \"c\": 5})");
            ans = Ognl.getValue(ex, context, context.getRoot());
            System.out.println("Map传参：" + ans);

            /**
             * 表达式执行
             * 表达式执行: 8
             */
            ans = Ognl.getValue(Ognl.parseExpression("1 + 3 + 4"), context, context.getRoot());
            System.out.println("表达式执行: " + ans);

            // 阶乘
            // lambda执行: 6
            ans = Ognl.getValue(Ognl.parseExpression("#fact = :[#this<=1? 1 : #this*#fact(#this-1)], #fact(3)"), context, context.getRoot());
            System.out.println("lambda执行: " + ans);

        } catch (OgnlException e) {
            e.printStackTrace();
        }

    }
}
