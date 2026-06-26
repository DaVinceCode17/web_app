package com.resources.dao;

import com.resources.DatabaseConnection;
import com.resources.model.Pricing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PricingDAO {

    public List<Pricing> getAllPricing() {
        List<Pricing> list = new ArrayList<>();
        String sql = "SELECT * FROM pricing ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pricing p = new Pricing();
                p.setId(rs.getLong("id"));
                p.setServiceType(rs.getString("service_type"));
                p.setPrice(rs.getDouble("price"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting pricing: " + e.getMessage());
        }
        return list;
    }

    public boolean updatePricing(String serviceType, Double price) {
        String sql = "UPDATE pricing SET price = ? WHERE service_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, price);
            pstmt.setString(2, serviceType);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating pricing: " + e.getMessage());
            return false;
        }
    }

    public Double getPriceByService(String serviceType) {
        String sql = "SELECT price FROM pricing WHERE service_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting price: " + e.getMessage());
        }
        return 0.0;
    }
}