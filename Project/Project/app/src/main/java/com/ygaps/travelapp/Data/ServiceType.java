package com.ygaps.travelapp.Data;

public class ServiceType {
    int id;
    String service_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public ServiceType(int id, String service_name) {
        this.id = id;
        this.service_name = service_name;
    }
}
