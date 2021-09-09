package com.jasonlee.practice.multhreaded.bo;

import java.io.Serializable;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/11 6:53 下午
 */
public class Address implements Serializable {
    private String province;
    private String city;
    private String region;
    private String street;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Address(String province, String city, String region, String street) {
        this.province = province;
        this.city = city;
        this.region = region;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
