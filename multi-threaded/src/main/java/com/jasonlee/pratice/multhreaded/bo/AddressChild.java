package com.jasonlee.pratice.multhreaded.bo;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/11 6:53 下午
 */
public class AddressChild extends Address {

    public AddressChild(String province, String city, String region, String street) {
        super(province, city, region, street);
    }

    public AddressChild() {
        super(null, null, null, null);
    }
}
