package org.wmc.integrated.builder;

import lombok.Data;

import java.util.*;

/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: GirlFriend
 * Author: 86187
 * Date: 2021/3/27 21:44
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class GirlFriend1 {
    private String name;
    private int age;
    private int bust;
    private int waist;
    private int hips;
    private List<String> hobby;
    private String birthday;
    private String address;
    private String mobile;
    private String email;
    private String hairColor;
    private Map<String, String> gift;

    // 为了演示方便，加几个聚合方法
    public void addHobby(String hobby) {
        this.hobby = Optional.ofNullable(this.hobby).orElse(new ArrayList<>());
        this.hobby.add(hobby);
    }

    public void addGift(String day, String gift) {
        this.gift = Optional.ofNullable(this.gift).orElse(new HashMap<>());
        this.gift.put(day, gift);
    }

    public void setVitalStatistics(int bust, int waist, int hips) {
        this.bust = bust;
        this.waist = waist;
        this.hips = hips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBust() {
        return bust;
    }

    public void setBust(int bust) {
        this.bust = bust;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public int getHips() {
        return hips;
    }

    public void setHips(int hips) {
        this.hips = hips;
    }

    public List<String> getHobby() {
        return hobby;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Map<String, String> getGift() {
        return gift;
    }

    public void setGift(Map<String, String> gift) {
        this.gift = gift;
    }

    @Override
    public String toString() {
        return "GirlFriend{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", bust=" + bust +
                ", waist=" + waist +
                ", hips=" + hips +
                ", hobby=" + hobby +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", hairColor='" + hairColor + '\'' +
                ", gift=" + gift +
                '}';
    }

    public static void main(String[] args) {
        GirlFriend1 myGirlFriend = Builder.of(GirlFriend1::new)
                .with(GirlFriend1::setName, "小美")
                .with(GirlFriend1::setAge, 18)
                .with(GirlFriend1::setVitalStatistics, 33, 23, 33)
                .with(GirlFriend1::setBirthday, "2001-10-26")
                .with(GirlFriend1::setAddress, "上海浦东")
                .with(GirlFriend1::setMobile, "18688888888")
                .with(GirlFriend1::setEmail, "xxxx@qq.com")
                .with(GirlFriend1::setHairColor, "浅棕色带点微卷")
                .with(GirlFriend1::addHobby, "逛街")
                .with(GirlFriend1::addHobby, "购物")
                .with(GirlFriend1::addHobby, "买东西")
                .with(GirlFriend1::addGift, "情人节礼物", "LBR 1912女王时代")
                .with(GirlFriend1::addGift, "生日礼物", "迪奥烈焰蓝金")
                .with(GirlFriend1::addGift, "纪念日礼物", "阿玛尼红管唇釉")
                // 等等等等 ...
                .build();
        System.out.println(myGirlFriend);
    }
}

