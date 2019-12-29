package com.ygaps.travelapp.Data;

 public class Province {
    int id;

    String name;

    String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Province(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
