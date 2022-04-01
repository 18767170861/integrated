package org.wmc.integrated.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "demo_collection")
public class DemoEntity {

    @Id
    private Long id;

    private String title;

    private String description;

    private String by;

    private String url;

}
