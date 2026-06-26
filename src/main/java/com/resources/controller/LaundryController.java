package com.resources.controller;

import com.resources.model.Customer;
import com.resources.model.Order;
import com.resources.service.LaundryService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/*")
public class LaundryController extends HttpServlet {

    private LaundryService service = new LaundryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String path = req.getPathInfo();

        try {
            if (path.equals("/customers")) {
                String json = "[";
                for (Customer c : service.getAllCustomers()) {
                    json += "{\"id\":" + c.getId() +
                            ",\"firstName\":\"" + c.getFirstName() + "\"" +
                            ",\"middleInitial\":\"" + c.getMiddleInitial() + "\"" +
                            ",\"surname\":\"" + c.getSurname() + "\"" +
                            ",\"address\":\"" + c.getAddress() + "\"" +
                            ",\"contactNumber\":\"" + c.getContactNumber() + "\"" +
                            ",\"nickname\":\"" + c.getNickname() + "\"},";
                }
                if (json.endsWith(",")) {
                    json = json.substring(0, json.length() - 1);
                }
                json += "]";
                out.write(json);

            } else if (path.equals("/orders")) {
                String json = "[";
                for (Order o : service.getAllOrders()) {
                    json += "{\"id\":" + o.getId() +
                            ",\"customerId\":" + o.getCustomerId() +
                            ",\"services\":\"" + o.getServices() + "\"" +
                            ",\"status\":\"" + o.getStatus() + "\"},";
                }
                if (json.endsWith(",")) {
                    json = json.substring(0, json.length() - 1);
                }
                json += "]";
                out.write(json);

            } else {
                out.write("{\"error\":\"Endpoint not found\"}");
            }
        } catch (Exception e) {
            out.write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String path = req.getPathInfo();

        try {
            if (path.equals("/customers")) {
                String firstName = req.getParameter("firstName");
                String middleInitial = req.getParameter("middleInitial");
                String surname = req.getParameter("surname");
                String address = req.getParameter("address");
                String contactNumber = req.getParameter("contactNumber");
                String nickname = req.getParameter("nickname");

                Customer customer = new Customer();
                customer.setFirstName(firstName);
                customer.setMiddleInitial(middleInitial);
                customer.setSurname(surname);
                customer.setAddress(address);
                customer.setContactNumber(contactNumber);
                customer.setNickname(nickname);

                boolean success = service.registerCustomer(customer);
                out.write("{\"success\":" + success + ",\"message\":\"" +
                        (success ? "Customer registered!" : "Registration failed! Contact number may already exist.") + "\"}");

            } else if (path.equals("/orders")) {
                String customerId = req.getParameter("customerId");
                String services = req.getParameter("services");
                String serviceType = req.getParameter("serviceType");

                Order order = new Order();
                order.setCustomerId(Long.parseLong(customerId));
                order.setServices(services);
                order.setServiceType(serviceType);

                boolean success = service.createOrder(order);
                out.write("{\"success\":" + success + ",\"message\":\"" +
                        (success ? "Order created!" : "Order creation failed! Customer may not exist.") + "\"}");

            } else {
                out.write("{\"error\":\"Endpoint not found\"}");
            }
        } catch (Exception e) {
            out.write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}