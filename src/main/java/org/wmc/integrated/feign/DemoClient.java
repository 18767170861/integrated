package org.wmc.integrated.feign;

import feign.Param;
import feign.RequestLine;

public interface DemoClient {

    @RequestLine("GET /feign/demo1?name={name}")
    String getDemo1(@Param("name") String name);
}
