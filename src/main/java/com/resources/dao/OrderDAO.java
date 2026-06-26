package com.resources.dao;

import com.resources.DatabaseConnection;
import com.resources.model.Order;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean saveOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, service_type, services, status, queue_number, weight, price, processing_time, remaining_time, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, order.getCustomerId());
            pstmt.setString(2, order.getServiceType());
            pstmt.setString(3, order.getServices());
            pstmt.setString(4, order.getStatus() != null ? order.getStatus() : "pending");
            pstmt.setInt(5, order.getQueueNumber() != null ? order.getQueueNumber() : 0);
            pstmt.setDouble(6, order.getWeight() != null ? order.getWeight() : 0.0);
            pstmt.setDouble(7, order.getPrice() != null ? order.getPrice() : 0.0);
            pstmt.setInt(8, order.getProcessingTime() != null ? order.getProcessingTime() : 0);
            pstmt.setInt(9, order.getRemainingTime() != null ? order.getRemainingTime() : 0);
            pstmt.setTimestamp(10, Timestamp.valueOf(order.getCreatedAt() != null ? order.getCreatedAt() : LocalDateTime.now()));

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    order.setId(rs.getLong(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Error saving order: " + e.getMessage());
            return false;
        }
    }

    public Order getOrderById(Long id) {
        String sql = "SELECT o.*, CONCAT(c.first_name, ' ', c.surname) as customer_name "
                    + "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id "
                    + "WHERE o.id = ?";
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

    public List<Order> getOrdersByCustomer(Long customerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(c.first_name, ' ', c.surname) as customer_name "
                    + "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id "
                    + "WHERE o.customer_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(c.first_name, ' ', c.surname) as customer_name "
                    + "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id "
                    + "WHERE o.status = ? ORDER BY o.queue_number ASC, o.created_at ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return list;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(c.first_name, ' ', c.surname) as customer_name "
                    + "FROM orders o LEFT JOIN customers c ON o.customer_id = c.id "
                    + "ORDER BY o.created_at DESC";
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

    public int getTodayQueueCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return 1;
    }

    public boolean updateOrderStatus(Long id, String status) {
        String sql = "UPDATE orders SET status = ?, completed_at = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, "completed".equals(status) ? Timestamp.valueOf(LocalDateTime.now()) : null);
            pstmt.setLong(3, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOrderDetails(Long id, Double weight, Double price) {
        String sql = "UPDATE orders SET weight = ?, price = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, weight != null ? weight : 0.0);
            pstmt.setDouble(2, price != null ? price : 0.0);
            pstmt.setLong(3, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteOrder(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return false;
        }
    }

    private Order mapResultSet(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setCustomerId(rs.getLong("customer_id"));
        o.setServiceType(rs.getString("service_type"));
        o.setServices(rs.getString("services"));
        o.setStatus(rs.getString("status"));
        o.setQueueNumber(rs.getInt("queue_number"));
        o.setWeight(rs.getDouble("weight"));
        o.setPrice(rs.getDouble("price"));
        o.setProcessingTime(rs.getInt("processing_time"));
        o.setRemainingTime(rs.getInt("remaining_time"));
        o.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        o.setCompletedAt(rs.getTimestamp("completed_at") != null ? rs.getTimestamp("completed_at").toLocalDateTime() : null);
        o.setCustomerName(rs.getString("customer_name"));
        return o;
    }
}