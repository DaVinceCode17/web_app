package com.resources.controller;

import com.resources.model.Order;
import com.resources.service.LaundryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order/*")
public class OrderController extends HttpServlet {

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

        if (path.equals("/create")) {
            String customerId = req.getParameter("customerId");
            String serviceType = req.getParameter("serviceType");
            String services = req.getParameter("services");

            Order order = new Order();
            order.setCustomerId(Long.parseLong(customerId));
            order.setServiceType(serviceType);
            order.setServices(services);

            boolean success = service.createOrder(order);
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/customer/dashboard.html?order=success");
            } else {
                resp.sendRedirect(req.getContextPath() + "/customer/dashboard.html?order=failed");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path.equals("/status")) {
            String status = req.getParameter("status");
            req.setAttribute("orders", service.getOrdersByStatus(status));
            req.getRequestDispatcher("/admin/orders-table.jsp").forward(req, resp);
        }
    }
}