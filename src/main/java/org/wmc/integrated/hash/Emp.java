package org.wmc.integrated.hash;

import lombok.Data;

@Data
public class Emp {

    private int id;

    private String name;

    private Emp next;

    public Emp(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
