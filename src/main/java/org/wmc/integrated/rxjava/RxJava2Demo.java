package org.wmc.integrated.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class RxJava2Demo {

    public static void main(String[] args) {
        //// 创建连载小说（被观察者）
        //Observable novel= Observable.create(new ObservableOnSubscribe<String>() {
        //    @Override
        //    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        //        emitter.onNext("连载1");
        //        emitter.onNext("连载2");
        //        emitter.onNext("连载3");
        //        emitter.onComplete();
        //    }
        //});
        ////
        ///**
        // * 创建读者（观察者）
        // * Disposable英文意思是可随意使用的，这里就相当于读者和连载小说的订阅关系，如果读者不想再订阅该小说了，可以调用 mDisposable.dispose()取消订阅，此时连载小说更新的时候就不会再推送给读者了
        // */
        //final Disposable[] mDisposable = new Disposable[1];
        //Observer<String> reader=new Observer<String>() {
        //    @Override
        //    public void onSubscribe(Disposable d) {
        //        System.out.println("onSubscribe");
        //        mDisposable[0] =d;
        //    }
        //
        //    @Override
        //    public void onNext(String value) {
        //        if ("连载2".equals(value)){
        //            mDisposable[0].dispose();
        //            return;
        //        }
        //        System.out.println("onNext:"+value);
        //    }
        //
        //    @Override
        //    public void onError(Throwable e) {
        //        System.out.println("onError="+e.getMessage());
        //    }
        //
        //    @Override
        //    public void onComplete() {
        //        System.out.println("onComplete()");
        //    }
        //};
        //// 读者和连载小说建立订阅关系
        //novel.subscribe(reader);//一行代码搞定

        /**
         * RxJava2.0的异步和链式编程
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("Observable threadNme:" + Thread.currentThread().getName());
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        })
                /**
                 * subscribeOn(): 指定Observable(被观察者)所在的线程，或者叫做事件产生的线程
                 * observeOn(): 指定 Observer(观察者)所运行在的线程，或者叫做事件消费的线程
                 *
                 * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
                 * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
                 * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
                 * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
                 * Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
                 *
                 * onSubscribe运行在当前线程（main）
                 * 当subscribeOn()与observeOn()都不配置 则都运行在当前线程（main）
                 * 当subscribeOn()不配置与observeOn()配置，subscribe运行在当前线程（main），Observer方法运行在新的线程
                 * 当subscribeOn()配置与observeOn()不配置，subscribe与Observer方法都运行在新的线程并且是同一个
                 * 当subscribeOn()配置与observeOn()配置，subscribe与Observer方法分别运行在不同的新线程
                 */
                .observeOn(Schedulers.io())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe threadNme:" + Thread.currentThread().getName());
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        System.out.println("onNext threadNme:" + Thread.currentThread().getName());
                        System.out.println("onNext:"+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError threadNme:" + Thread.currentThread().getName());
                        System.out.println("onError="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete threadNme:" + Thread.currentThread().getName());
                        System.out.println("onComplete()");
                    }
                });
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
