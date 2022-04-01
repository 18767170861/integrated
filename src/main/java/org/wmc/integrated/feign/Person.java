package org.wmc.integrated.feign;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {
    private String name = "YourBatman";
    private Integer age = 18;
}