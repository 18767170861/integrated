package org.wmc.integrated.rxjava;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RxJava1Demo {

    public static void main(String[] args) {
        /**
         * 创建观察者
         *
         */
        Observer<String> observer = new Observer<String>() {
            //被观察者调用onCompleted时触发
            @Override
            public void onCompleted() {
                System.err.println("observer onCompleted");
            }
            //被观察者调用onError时触发
            @Override
            public void onError(Throwable e) {
                System.err.println("observer onError");
            }
            //被观察者调用onNext时触发
            @Override
            public void onNext(String s) {
                System.err.println("observer onNext:" + s);
            }
        };
        /**
         * 另外一个观察者就是Subscriber，而实际上在调用subscribe方法订阅的时候也是把Observer转换为了Subscriber处理的
         *
         */
        Subscriber<String> subscriber = new Subscriber<String>() {
            //新增onStart方法，用来做一些初始化操作
            @Override
            public void onStart() {
                System.out.println("subscriber onStart");
                super.onStart();
            }

            @Override
            public void onCompleted() {
                System.err.println("subscriber onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.err.println("subscriber onError");
            }

            @Override
            public void onNext(String s) {
                System.err.println("subscriber onNext:" + s);
            }
        };

        /**
         * 使用create创建被观察者
         * 使用create( )创建Observable最基本的创建方式。可以看到，这里传入了一个 Observable.OnSubscribe对象作为参数，
         * 当 Observable被订阅的时候，Observable.OnSubscribe的call()方法会自动被调用，事件序列就会依照设定依次触发。
         * 这样，由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式
         *
         */
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("Observable Test");
                //定义事件队列
                subscriber.onNext("aaaa");
                subscriber.onNext("bbbb");
                subscriber.onCompleted();
            }
        });
        //observable.subscribe(observer);
        //observable.subscribe(subscriber);


        /**
         * 使用just()创建被观察者
         * 使用just( )，将为你创建一个Observable并自动为你调用onNext( )发射数据。通过just( )方式 直接触发onNext()，just中传递的参数将直接在Observer的onNext()方法中接收到
         *
         * 打印结果：
         * subscriber onStart
         * subscriber onNext:hello
         * subscriber onCompleted
         */
        Observable<String> observableJust = Observable.just("hello");
        //observableJust.subscribe(subscriber);

        /**
         * 使用from()创建被观察者
         * from()方法将传入的数组或Iterable拆分成具体对象后，自动调用onNext方法依次发送，再发送结束后发送onCompleted结束整个事件
         *
         * 打印结果：
         * subscriber onStart
         * subscriber onNext:aaaa
         * subscriber onNext:bbbb
         * subscriber onNext:cccc
         * subscriber onNext:dddd
         * subscriber onCompleted
         */
        //定义要发送的事件集合
        List<String> mList = new ArrayList<String>();
        mList.add("aaaa");
        mList.add("bbbb");
        mList.add("cccc");
        mList.add("dddd");
        //定义Observable
        Observable<String> observableFrom = Observable.from(mList);
        //进行订阅，开始发送事件
        observableFrom.subscribe(subscriber);
    }

}
