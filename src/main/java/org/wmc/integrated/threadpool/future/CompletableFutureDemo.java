package org.wmc.integrated.threadpool.future;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 多线程并发任务,取结果归集
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        whenComplete();
    }

    /**
     * 多线程并发任务,取结果归集
     */
    public static void test1() {
        Long start = System.currentTimeMillis();
        //定长10线程池
        ExecutorService exs = Executors.newFixedThreadPool(10);
        //结果集
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        final List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
        try {
            //方式一：循环创建CompletableFuture list, 然后组装 组装返回一个有返回值的CompletableFuture，返回结果get()获取
            for (int i = 0; i < taskList.size(); i++) {
                final int j = i;
                //异步执行 拿到每个有返回值的CompletableFuture对象
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> calc(taskList.get(j)), exs)
                        //Integer转换字符串 thenAccept只接受不返回不影响结果
                        .thenApply(e -> Integer.toString(e))
                        //如需获取任务完成先后顺序，此处代码即可
                        .whenComplete((v, e) -> {
                            System.out.println("任务" + v + "完成!result=" + v + "，异常 e=" + e + "," + new Date());
                            list2.add(v);
                        });
                futureList.add(future);
            }
            //流式获取结果：此处是根据任务添加顺序获取的结果========================
            //1.构造一个空CompletableFuture，子任务数为入参任务list size
            CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futureList.stream().filter(f -> f != null).collect(toList()).toArray(new CompletableFuture[futureList.size()]));
            //2.流式（总任务完成后，每个子任务join取结果，后转换为list）
            list = allDoneFuture.thenApply(v -> futureList.stream().map(CompletableFuture::join).collect(toList())).get();
            //流式获取结果：此处是根据任务添加顺序获取的结果========================
            System.out.println("任务完成先后顺序，结果list2=" + list2 + "；任务提交顺序，结果list=" + list + ",耗时=" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exs.shutdown();
        }
    }

    /**
     * 多线程并发任务,取结果归集
     */
    public static void test2() {
        Long start = System.currentTimeMillis();
        //定长10线程池
        ExecutorService exs = Executors.newFixedThreadPool(10);
        //结果集
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        final List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
        try {
            //方式二：全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
            CompletableFuture[] cfs = taskList.stream().map(i ->
                    //把计算任务 交给CompletableFuture异步去处理执行
                    CompletableFuture.supplyAsync(() -> calc(i), exs)
                            // 把计算完成结果做Function处理：此处是转换成了字符串
                            .thenApply(h -> Integer.toString(h))
                            //如需获取任务完成先后顺序，此处代码即可 会先处理先完成的任务 后处理后完成的任务 使用起来比CompletionService确实方便不少
                            .whenComplete((v, e) -> {
                                System.out.println("任务" + v + "完成!result=" + v + "，异常 e=" + e + "," + new Date());
                                list2.add(v);
                            })).toArray(CompletableFuture[]::new); //此处直接toArray 不toList了
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            //等待总任务完成，但是封装后无返回值，必须自己whenComplete()获取 此处使用join来获取结果
            CompletableFuture.allOf(cfs).join();
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            System.out.println("任务完成先后顺序，结果list2=" + list2 + "；任务提交顺序，结果list=" + list + ",耗时=" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exs.shutdown();
        }
    }

    /**
     * CompletableFuture不使用线程池
     */
    public static void test3() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        //自己开个线程去执行 执行完把结果告诉completableFuture即可
        new Thread(() -> {
            // 模拟执行耗时任务
            System.out.println("task doing...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 告诉completableFuture任务已经完成 并且把结果告诉completableFuture
            completableFuture.complete("ok"); //这里把你信任的结果set进去后，所有阻塞的get()方法都能立马苏醒，获得到结果
        }).start();
        // 获取任务结果，如果没有完成会一直阻塞等待
        System.out.println("准备打印结果...");
        System.out.println("是否异常结束：" + completableFuture.isCompletedExceptionally());
        String result = completableFuture.get();
        System.out.println("计算结果:" + result);
    }

    /**
     * CompletableFuture获取异常
     */
    public static void test4() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture();

        //自己开个线程去执行 执行完把结果告诉completableFuture即可
        new Thread(() -> {
            // 模拟执行耗时任务
            System.out.println("task doing...");
            try {
                Thread.sleep(3000);
                System.out.println(1 / 0);
                //} catch (InterruptedException e) {
            } catch (Exception e) {
                // 告诉completableFuture任务发生异常了
                completableFuture.completeExceptionally(e);
                //e.printStackTrace();
            }
            // 告诉completableFuture任务已经完成 并且把结果告诉completableFuture
            completableFuture.complete("ok");
        }).start();
        // 获取任务结果，如果没有完成会一直阻塞等待
        System.out.println("准备打印结果...");
        System.out.println("是否异常结束：" + completableFuture.isCompletedExceptionally());
        String result = "";
        try {
            result = completableFuture.get();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        System.out.println("是否异常结束：" + completableFuture.isCompletedExceptionally());
        System.out.println("计算结果:" + result + "...............");
    }

    /**
     * whenComplete计算结果完成时的处理
     * 当CompletableFuture的计算结果完成，或者抛出异常的时候，我们可以执行特定的Action。主要是下面的方法：
     * public CompletableFuture<T> 	whenComplete(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> 	whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> 	whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
     * public CompletableFuture<T>     exceptionally(Function<Throwable,? extends T> fn)
     * 可以看到Action的类型是BiConsumer<? super T,? super Throwable>，它可以处理正常的计算结果，或者异常情况。
     * 方法不以Async结尾，意味着Action使用相同的线程执行，而Async可能会使用其它的线程去执行(如果使用相同的线程池，也可能会被同一个线程选中执行)。
     * <p>
     * 注意这几个方法都会返回CompletableFuture
     */
    public static void whenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> 100)
                .thenApplyAsync(i -> i * 10)
                .thenApply(i -> i.toString())
                .whenComplete((r, e) -> System.out.println(r + "_____" + e));//1000_____null
        System.out.println("result:" + completableFuture.get());
        //若有异常
        CompletableFuture.supplyAsync(() -> 1 / 0)
                .thenApplyAsync(i -> i * 10)
                .thenApply(i -> i.toString())
                .whenComplete((r, e) -> System.out.println(r + "_____" + e)); //null_____java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero

    }

    /**
     * handle、 thenApply相当于回调函数（callback） 当然也有转换的作用
     * public <U> CompletableFuture<U> 	handle(BiFunction<? super T,Throwable,? extends U> fn)
     * public <U> CompletableFuture<U> 	handleAsync(BiFunction<? super T,Throwable,? extends U> fn)
     * public <U> CompletableFuture<U> 	handleAsync(BiFunction<? super T,Throwable,? extends U> fn, Executor executor)
     * <p>
     * thenApply与handle方法的区别在于handle方法会处理正常计算值和异常，因此它可以屏蔽异常，避免异常继续抛出。而thenApply方法只是用来处理正常值，因此一旦有异常就会抛出。
     */
    public static void handle() {
        CompletableFuture.supplyAsync(() -> 100)
                .handleAsync((i, e) -> i * 10)
                .handle((i, e) -> i.toString())
                .whenComplete((r, e) -> System.out.println(r + "_____" + e));

        //若有异常
        CompletableFuture.supplyAsync(() -> 1 / 0)
                .handleAsync((i, e) -> {
                    System.out.println(i + "_____" + e);
                    return i * 10;
                }).handle((i, e) -> i.toString())
                .whenComplete((r, e) -> System.out.println(r + "_____" + e));
    }

    /**
     * thenAccept与thenRun（纯消费(执行Action)）
     * public CompletableFuture<Void> thenAccept(Consumer<? super T> action) {
     * return uniAcceptStage(null, action);
     * }
     * public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
     * return uniAcceptStage(asyncPool, action);
     * }
     * public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
     * return uniAcceptStage(screenExecutor(executor), action);
     * }
     * public CompletableFuture<Void> thenRun(Runnable action) {
     * return uniRunStage(null, action);
     * }
     * public CompletableFuture<Void> thenRunAsync(Runnable action) {
     * return uniRunStage(asyncPool, action);
     * }
     * public CompletableFuture<Void> thenRunAsync(Runnable action, nExecutor executor) {
     * return uniRunStage(screenExecutor(executor), action);
     * }
     * <p>
     * 可以看到，thenAccept和thenRun都是无返回值的。如果说thenApply是不停的输入输出的进行生产，那么thenAccept和thenRun就是在进行消耗。它们是整个计算的最后两个阶段。
     * 同样是执行指定的动作，同样是消耗，二者也有区别：
     * thenAccept接收上一阶段的输出作为本阶段的输入
     * thenRun根本不关心前一阶段的输出，根本不不关心前一阶段的计算结果，因为它不需要输入参数（thenRun使用的是Runnable，若你只是单纯的消费，不需要启用线程时，就用thenRun更合适）
     */
    public static void thenAcceptAndThenRun() {
        CompletableFuture<Void> f = CompletableFuture.supplyAsync(() -> 100)
                .thenAccept(x -> System.out.println(x)); //100
        //如果此句话get不调用  也是能够输出100的 上面也会有输出的
        System.out.println(f.join()); //null 返回null，所以thenAccept是木有返回值的

        //thenRun的案例演示
        CompletableFuture<Void> f2 = CompletableFuture.supplyAsync(() -> 100)
                .thenRun(() -> System.out.println("不需要入参")); //不需要入参
        System.out.println(f2.join()); //null 返回null，所以thenRun是木有返回值的
    }

    /**
     * thenAcceptBoth以及相关方法提供了类似的功能，当两个CompletionStage都正常完成计算的时候，就会执行提供的action，它用来组合另外一个异步的结果。
     * runAfterBoth是当两个CompletionStage都正常完成计算的时候,执行一个Runnable，这个Runnable并不使用计算的结果。
     * <p>
     * public <U> CompletableFuture<Void> 	thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action)
     * public <U> CompletableFuture<Void> 	thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action)
     * public <U> CompletableFuture<Void> 	thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action, Executor executor)
     * public     CompletableFuture<Void> 	runAfterBoth(CompletionStage<?> other,  Runnable action)
     */
    public static void thenAcceptBoth() {
        CompletableFuture<Void> f = CompletableFuture.supplyAsync(() -> 100)
                // 第二个消费者：x,y显然是可以把前面几个的结果都拿到，然后再做处理
                .thenAcceptBoth(CompletableFuture.completedFuture(10), (x, y) -> System.out.println(x * y)); //1000
        System.out.println(f.join()); //null
    }

    /**
     * thenCombine、thenCompose整合两个计算结果
     * public <U,V> CompletableFuture<V> 	thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
     * public <U,V> CompletableFuture<V> 	thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
     * public <U,V> CompletableFuture<V> 	thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn, Executor executor)
     * <p>
     * public <U> CompletableFuture<U> 	thenCompose(Function<? super T,? extends CompletionStage<U>> fn)
     * public <U> CompletableFuture<U> 	thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn)
     * public <U> CompletableFuture<U> 	thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn, Executor executor)
     * <p>
     * 先说：thenCompose
     * 这一组方法接受一个Function作为参数，这个Function的输入是当前的CompletableFuture的计算值，返回结果将是一个新的CompletableFuture，这个新的CompletableFuture会组合原来的CompletableFuture和函数返回的CompletableFuture。
     * <p>
     * 而下面的一组方法thenCombine用来复合另外一个CompletionStage的结果。它的功能类似：
     * 两个CompletionStage是并行执行的，它们之间并没有先后依赖顺序，other并不会等待先前的CompletableFuture执行完毕后再执行。
     * <p>
     * 其实从功能上来讲,它们的功能更类似thenAcceptBoth，只不过thenAcceptBoth是纯消费，它的函数参数没有返回值，而thenCombine的函数参数fn有返回值
     */
    public static void thenCombineAndThenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "abc");
        CompletableFuture<String> f = future.thenCombine(future2, (x, y) -> y + "-" + x);
        System.out.println(f.join()); //abc-100

        CompletableFuture<Integer> completableFuture = future.thenCompose(i -> CompletableFuture.completedFuture(999 + i));
        System.out.println("result:" + completableFuture.get());
    }

    /**
     * Either：任意一个计算完了就可以执行
     * public CompletableFuture<Void> 	acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action)
     * public CompletableFuture<Void> 	acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action)
     * public CompletableFuture<Void> 	acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor)
     * public <U> CompletableFuture<U> 	applyToEither(CompletionStage<? extends T> other, Function<? super T,U> fn)
     * public <U> CompletableFuture<U> 	applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T,U> fn)
     * public <U> CompletableFuture<U> 	applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T,U> fn, Executor executor)
     * acceptEither方法是当任意一个CompletionStage完成的时候，action这个消费者就会被执行。这个方法返回CompletableFuture
     * <p>
     * applyToEither方法是当任意一个CompletionStage完成的时候，fn会被执行，它的返回值会当作新的CompletableFuture的计算结果。
     */
    public static void acceptEither() {
        Random rand = new Random();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 + rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<String> f = future.applyToEither(future2, i -> i.toString());
        System.out.println(f.join()); //有时候输出100  有时候输出200

    }

    /**
     * allOf方法是当所有的CompletableFuture都执行完后执行计算。
     * anyOf方法是当任意一个CompletableFuture执行完后就会执行计算，计算的结果返回
     */
    public static void allOfAndAnyOf() throws ExecutionException, InterruptedException {
        Random rand = new Random();
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1...");
//            try {
//                Thread.sleep(10000 + rand.nextInt(1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return 100;
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2...");
//            try {
//                Thread.sleep(10000 + rand.nextInt(1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return "abc";
        });
        CompletableFuture<Void> f = CompletableFuture.allOf(future1, future2);
        f.join();
        //CompletableFuture<Object> f = CompletableFuture.anyOf(future1, future2);
        //System.out.println(f.join());
        CompletableFuture<CompletableFuture<Integer>> completableFutureCompletableFuture = f.thenApply(v -> {
            System.out.println("v:" + v);
            return CompletableFuture.completedFuture(999);
        });
        CompletableFuture<Integer> join = completableFutureCompletableFuture.join();
        CompletableFuture<Integer> completableFuture1 = completableFutureCompletableFuture.get();
        System.out.println("join:" + join);
        System.out.println("completableFuture1:" + completableFuture1);
        System.out.println("result:" + completableFuture1.get());
        System.out.println("result2:" + join.get());


        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 999).thenApply(v -> {
            System.out.println("aaa:" + v);
            return 9999999;
        });
        System.out.println(completableFuture.get());
        System.out.println(completableFuture.join());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 手写sequence方法
     * 如果你用过Guava的Future类，你就会知道它的Futures辅助类提供了很多便利方法，用来处理多个Future，而不像Java的CompletableFuture，只提供了allOf、anyOf两个方法。
     *
     * 比如有这样一个需求，将多个CompletableFuture组合成一个CompletableFuture，这个组合后的CompletableFuture的计算结果是个List,它包含前面所有的CompletableFuture的计算结果，guava的Futures.allAsList可以实现这样的功能，但是对于java CompletableFuture，我们需要一些辅助方法：
     */
    /**
     * 可以把多个futures序列化起来  最终返回一个装载有结果的CompletableFuture即可  调用join方法就够了
     * 当然只能是同一类型哦（返回的结果）
     *
     * @param <T>     the type parameter
     * @param futures the futures
     * @return the completable future
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        //通过allOf方法把所有的futures放到一起  返回Void
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        //遍历把每一个futures通过join方法把结果拿到  最终给返回出去 并且是用CompletableFuture包装着的
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }

    public static <T> CompletableFuture<T> toCompletable(Future<T> future, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public static void sequenceTest() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<List<Integer>> resultList = sequence(Arrays.asList(future1, future2));
        System.out.println(resultList.join()); //[100, 200]
    }


    //模拟任务的耗时方法
    public static Integer calc(Integer i) {
        try {
            if (i == 1) {
                //任务1耗时3秒
                Thread.sleep(3000);
            } else if (i == 5) {
                //任务5耗时5秒
                Thread.sleep(5000);
            } else {
                //其它任务耗时1秒
                Thread.sleep(1000);
            }
            System.out.println("task线程：" + Thread.currentThread().getName() + "任务i=" + i + ",完成！+" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }
}