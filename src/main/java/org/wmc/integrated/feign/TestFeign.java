package org.wmc.integrated.feign;

public class TestFeign {

    public static void main(String[] args) {
        //DemoClient client = Feign.builder().target(DemoClient.class, "http://localhost:8081");
        //String result = client.getDemo1("YourBatman");
        //System.out.println(result);


        RequestLineClient requestLineClient = FeignClientFactory.create(RequestLineClient.class);
        //System.err.println(" ------------------ ");
        //
        //requestLineClient.testRequestLine2("YourBatman2");
        //System.err.println(" ------------------ ");

        // 使用Map一次传多个请求参数
        //Map<String, Object> map = new HashMap<>();
        //map.put("name", "YourBatman3");
        //map.put("age", Arrays.asList(16, 18, 20));
        //requestLineClient.testRequestLine3(map);
        //System.err.println(" ------------------ ");

        //try {
        //    requestLineClient.testRequestLine4("YourBatman4");
        //} catch (Exception e) {
        //}
        //System.err.println(" ------------------ ");

        //try {
        //    requestLineClient.testRequestLine5("YourBatman4");
        //} catch (Exception e) {
        //}
        //System.err.println(" ------------------ ");

        //try {
        //    requestLineClient.testRequestLine8("YourBatman4", 18);
        //} catch (Exception e) {
        //}

        //ParamClient client = FeignClientFactory.create(ParamClient.class);
        //client.testParam(new String[]{"YourBatman", "fsx"});
        //System.err.println(" ------------------ ");
        //client.testParam2(Arrays.asList("1", "2", "3"));
        //System.err.println(" ------------------ ");
        //
        //client.testParam3("/?YourBatman/");
        //System.err.println(" ------------------ ");

        BodyClient client = FeignClientFactory.create(BodyClient.class);
        client.testBody();
        System.err.println(" ------------------ ");
        client.testBody2("my name is YourBatman");
        System.err.println(" ------------------ ");
        client.testBody3(new Person());
    }
}
