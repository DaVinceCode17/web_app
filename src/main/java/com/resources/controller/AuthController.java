package com.resources.controller;

import com.resources.model.Customer;
import com.resources.service.LaundryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth/*")
public class AuthController extends HttpServlet {

    private LaundryService service = new LaundryService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path.equals("/register")) {
            String firstName = req.getParameter("firstName");
            String middleInitial = req.getParameter("middleInitial");
            String surname = req.getParameter("surname");
            String address = req.getParameter("address");
            String contact = req.getParameter("contactNumber");
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setMiddleInitial(middleInitial);
            customer.setSurname(surname);
            customer.setAddress(address);
            customer.setContactNumber(contact);
            customer.setNickname(nickname);
            customer.setPassword(password);

            boolean success = service.registerCustomer(customer);
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/login.html?registered=true");
            } else {
                resp.sendRedirect(req.getContextPath() + "/register.html?error=contact_exists");
            }

        } else if (path.equals("/login")) {
            String contact = req.getParameter("contactNumber");
            String password = req.getParameter("password");

            Customer customer = service.login(contact, password);
            if (customer != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", customer);

                if ("admin".equals(customer.getRole())) {
                    resp.sendRedirect(req.getContextPath() + "/admin/dashboard.html");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/customer/dashboard.html");
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.html?error=invalid");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path.equals("/logout")) {
            HttpSession session = req.getSession();
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }
    }
}