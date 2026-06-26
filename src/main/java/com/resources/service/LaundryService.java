package com.resources.service;

import com.resources.dao.CustomerDAO;
import com.resources.dao.OrderDAO;
import com.resources.dao.PricingDAO;
import com.resources.model.Customer;
import com.resources.model.Order;
import com.resources.model.Pricing;
import java.util.List;

public class LaundryService {

    private CustomerDAO customerDAO = new CustomerDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private PricingDAO pricingDAO = new PricingDAO();

    // ===== CUSTOMER =====
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public boolean registerCustomer(Customer customer) {
        if (customerDAO.getCustomerByContact(customer.getContactNumber()) != null) {
            System.out.println("❌ Customer already exists!");
            return false;
        }
        customer.setRole("customer");
        return customerDAO.saveCustomer(customer);
    }

    public Customer login(String contact, String password) {
        return customerDAO.login(contact, password);
    }

    public Customer getCustomerById(Long id) {
        return customerDAO.getCustomerById(id);
    }

    public Customer getCustomerByContact(String contact) {
        return customerDAO.getCustomerByContact(contact);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerDAO.searchCustomers(keyword);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(Long id) {
        return customerDAO.deleteCustomer(id);
    }

    // ===== ORDER =====
    public boolean createOrder(Order order) {
        Customer customer = customerDAO.getCustomerById(order.getCustomerId());
        if (customer == null) {
            System.out.println("❌ Customer not found!");
            return false;
        }

        double totalPrice = 0;
        int totalTime = 0;
        String[] services = order.getServices().split(",");
        for (String service : services) {
            Double price = pricingDAO.getPriceByService(service.trim());
            if (price != null) {
                totalPrice += price;
            }
            if (service.trim().equals("Wash")) totalTime += 30;
            else if (service.trim().equals("Dry")) totalTime += 20;
            else if (service.trim().equals("Fold")) totalTime += 15;
        }

        order.setQueueNumber(orderDAO.getTodayQueueCount());
        order.setPrice(totalPrice);
        order.setProcessingTime(totalTime);
        order.setRemainingTime(totalTime);
        order.setStatus("pending");

        return orderDAO.saveOrder(order);
    }

    public Order getOrderById(Long id) {
        return orderDAO.getOrderById(id);
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderDAO.getOrdersByCustomer(customerId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.getOrdersByStatus(status);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public boolean updateOrderStatus(Long id, String status) {
        return orderDAO.updateOrderStatus(id, status);
    }

    public boolean updateOrderDetails(Long id, Double weight, Double price) {
        return orderDAO.updateOrderDetails(id, weight, price);
    }

    public boolean deleteOrder(Long id) {
        return orderDAO.deleteOrder(id);
    }

    // ===== PRICING =====
    public List<Pricing> getAllPricing() {
        return pricingDAO.getAllPricing();
    }

    public boolean updatePricing(String serviceType, Double price) {
        return pricingDAO.updatePricing(serviceType, price);
    }

    public Double getPriceByService(String serviceType) {
        return pricingDAO.getPriceByService(serviceType);
    }
}