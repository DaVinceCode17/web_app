package com.resources.dao;

import com.resources.DatabaseConnection;
import com.resources.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public boolean saveCustomer(Customer customer) {
        String sql = "INSERT INTO customers (first_name, middle_initial, surname, address, contact_number, nickname, password, role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getMiddleInitial());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getContactNumber());
            pstmt.setString(6, customer.getNickname());
            pstmt.setString(7, customer.getPassword());
            pstmt.setString(8, customer.getRole() != null ? customer.getRole() : "customer");

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    customer.setId(rs.getLong(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Error saving customer: " + e.getMessage());
            return false;
        }
    }

    public Customer getCustomerById(Long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }

    public Customer getCustomerByContact(String contact) {
        String sql = "SELECT * FROM customers WHERE contact_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }

    public Customer login(String contact, String password) {
        String sql = "SELECT * FROM customers WHERE contact_number = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }

    public List<Customer> searchCustomers(String keyword) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE first_name LIKE ? OR surname LIKE ? OR contact_number LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            pstmt.setString(1, search);
            pstmt.setString(2, search);
            pstmt.setString(3, search);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET first_name=?, middle_initial=?, surname=?, address=?, contact_number=?, nickname=?, password=?, role=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getMiddleInitial());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getContactNumber());
            pstmt.setString(6, customer.getNickname());
            pstmt.setString(7, customer.getPassword());
            pstmt.setString(8, customer.getRole());
            pstmt.setLong(9, customer.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCustomer(Long id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return false;
        }
    }

    private Customer mapResultSet(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getLong("id"));
        c.setFirstName(rs.getString("first_name"));
        c.setMiddleInitial(rs.getString("middle_initial"));
        c.setSurname(rs.getString("surname"));
        c.setAddress(rs.getString("address"));
        c.setContactNumber(rs.getString("contact_number"));
        c.setNickname(rs.getString("nickname"));
        c.setPassword(rs.getString("password"));
        c.setRole(rs.getString("role"));
        return c;
    }
}