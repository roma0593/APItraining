package com.coherent.training.api.kapitsa.util.plainobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private int age;
    private String name;
    private String sex;
    private String zipCode;

    public User() {
    }

    public User(String name, int age, String sex, String zipCode) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.zipCode = zipCode;
    }

    public User(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public User(int age, String zipCode) {
        this.age = age;
        this.zipCode = zipCode;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, sex, zipCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getAge() == user.getAge() && Objects.equals(getName(), user.getName()) && Objects.equals(getSex(), user.getSex()) && Objects.equals(getZipCode(), user.getZipCode());
    }
}
