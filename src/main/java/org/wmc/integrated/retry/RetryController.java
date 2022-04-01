package org.wmc.integrated.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {

    @Autowired
    TestRetryService testRetryServiceImpl;

    @GetMapping("/testRetry/{code}")
    public String testRetry(@PathVariable("code") int code) throws Exception {
        int result = testRetryServiceImpl.dignifiedTest(code);
        return "result：" + result;
    }

}