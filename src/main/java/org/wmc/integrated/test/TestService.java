package org.wmc.integrated.test;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class TestService {

    @Autowired
    private CommonService commonService;


    public String test() {
        commonService.common();
        invoke();
        return "test";
    }

    protected abstract void invoke();


}
