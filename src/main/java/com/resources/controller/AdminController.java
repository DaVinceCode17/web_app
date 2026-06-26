package com.resources.controller;

import com.resources.model.Order;
import com.resources.service.LaundryService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {

    private LaundryService service = new LaundryService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object user = session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String path = req.getPathInfo();

        if (path.equals("/update-pricing")) {
            String washPrice = req.getParameter("washPrice");
            String dryPrice = req.getParameter("dryPrice");
            String foldPrice = req.getParameter("foldPrice");

            if (washPrice != null && !washPrice.isEmpty()) {
                service.updatePricing("Wash", Double.parseDouble(washPrice));
            }
            if (dryPrice != null && !dryPrice.isEmpty()) {
                service.updatePricing("Dry", Double.parseDouble(dryPrice));
            }
            if (foldPrice != null && !foldPrice.isEmpty()) {
                service.updatePricing("Fold", Double.parseDouble(foldPrice));
            }

            resp.sendRedirect(req.getContextPath() + "/admin/pricing.html?updated=true");
        }

        if (path.equals("/update-order-status")) {
            String orderId = req.getParameter("orderId");
            String status = req.getParameter("status");

            if (orderId != null && status != null) {
                service.updateOrderStatus(Long.parseLong(orderId), status);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/dashboard.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path.equals("/data")) {
            String type = req.getParameter("type");
            String json = "[]";

            if ("pending".equals(type)) {
                json = convertOrdersToJson(service.getOrdersByStatus("pending"));
            } else if ("ongoing".equals(type)) {
                json = convertOrdersToJson(service.getOrdersByStatus("ongoing"));
            } else if ("completed".equals(type)) {
                json = convertOrdersToJson(service.getOrdersByStatus("completed"));
            }

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        }
    }

    private String convertOrdersToJson(List<Order> orders) {
        StringBuilder json = new StringBuilder("[");
        for (Order o : orders) {
            json.append("{");
            json.append("\"id\":").append(o.getId()).append(",");
            json.append("\"queueNumber\":").append(o.getQueueNumber()).append(",");
            json.append("\"customerName\":\"").append(o.getCustomerName() != null ? o.getCustomerName() : "Unknown").append("\",");
            json.append("\"services\":\"").append(o.getServices()).append("\",");
            json.append("\"status\":\"").append(o.getStatus()).append("\"");
            json.append("},");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("]");
        return json.toString();
    }
}