package com.affle.bluetoothchatting;

import java.io.Serializable;

/**
 * Created by akash on 11/7/17.
 */

public class BlueToothDevices implements Serializable{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String name;
    public String className;
    public String address;
}
