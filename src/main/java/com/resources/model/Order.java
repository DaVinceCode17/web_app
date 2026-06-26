package com.resources.model;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long customerId;
    private String serviceType;
    private String services;
    private String status;
    private Integer queueNumber;
    private Double weight;
    private Double price;
    private Integer processingTime;
    private Integer remainingTime;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String customerName;

    public Order() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getServices() { return services; }
    public void setServices(String services) { this.services = services; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getQueueNumber() { return queueNumber; }
    public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getProcessingTime() { return processingTime; }
    public void setProcessingTime(Integer processingTime) { this.processingTime = processingTime; }
    public Integer getRemainingTime() { return remainingTime; }
    public void setRemainingTime(Integer remainingTime) { this.remainingTime = remainingTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}