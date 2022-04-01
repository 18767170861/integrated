package org.wmc.integrated.jackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private Integer age;
    private Dog dog;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dog{
        private String name;
        private Integer age;
    }
}