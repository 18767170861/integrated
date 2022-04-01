package org.wmc.integrated.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/testScope")
public class TestScope {

    @Autowired
    private Order order;

    private String name;

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String userProfile(@PathVariable("username") String username) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("order:" + order); //order:org.wmc.scope.Order@aebd4a5
        System.out.println("order1:" + order); //order1:org.wmc.scope.Order@22674af3
        // order class:class org.wmc.scope.Order$$EnhancerBySpringCGLIB$$93094b9e
        System.out.println("order class:" + order.getClass());
        // order origin class:class org.wmc.scope.Order
        System.out.println("order origin class:" + order.getClass().getSuperclass());
        name = username;
        order.setOrderNum(name);
        try {
            for (int i = 0; i < 2; i++) {
                System.out.println(
                        Thread.currentThread().getId()
                                + "name:" + name
                                + "--order:"
                                + order.getOrderNum());
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }
}
