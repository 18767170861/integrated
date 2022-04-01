package org.wmc.integrated.test;

import org.springframework.stereotype.Service;

@Service
public class OneTestService extends TestService {
    @Override
    protected void invoke() {
        System.out.println("i am one");
    }
}
