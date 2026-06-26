package com.resources.model;

public class Pricing {
    private Long id;
    private String serviceType;
    private Double price;

    public Pricing() {}

    public Pricing(String serviceType, Double price) {
        this.serviceType = serviceType;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}