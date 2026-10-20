package com.scc.dao;

import com.scc.model.ScenarioEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public boolean addAsset(String deviceName, String assignedTo, String serialNo, String condition) {
        String sql = "INSERT INTO assets (device_name, assigned_to, serial_no, condition) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("❌ No database connection");
                return false;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, deviceName);
                pstmt.setString(2, assignedTo);
                pstmt.setString(3, serialNo);
                pstmt.setString(4, condition);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Serial number already exists!");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Error adding asset: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> getAllAssets() {
        List<ScenarioEntity> list = new ArrayList<>();
        String sql = "SELECT id, device_name, assigned_to, serial_no, condition FROM assets ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("❌ No database connection");
                return list;
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(new ScenarioEntity(
                            rs.getInt("id"),
                            rs.getString("device_name"),
                            rs.getString("assigned_to"),
                            rs.getString("serial_no"),
                            rs.getString("condition")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching assets: " + e.getMessage());
        }
        return list;
    }
}