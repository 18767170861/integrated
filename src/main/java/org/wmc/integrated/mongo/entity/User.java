package org.wmc.integrated.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "user")
public class User implements Serializable {

    @Id
    private String id;

    private String userName;

    private String password;

    private Integer age;

    private Long createTime;

    private List<String> sports;

    private DemoEntity demoEntity;

    public void addSports(String sport) {
        this.sports = Optional.ofNullable(this.sports).orElse(new ArrayList<>());
        this.sports.add(sport);
    }

    public DemoEntity getDemoEntity() {
        return demoEntity;
    }

    public void setDemoEntity(DemoEntity demoEntity) {
        this.demoEntity = demoEntity;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                ", demoEntity=" + demoEntity +
                '}';
    }
}