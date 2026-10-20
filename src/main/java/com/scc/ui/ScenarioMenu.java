package com.scc.ui;

import com.scc.dao.ScenarioDao;
import com.scc.model.ScenarioEntity;
import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final ScenarioDao dao = new ScenarioDao();

    public void show() {
        while (true) {
            printHeader();
            System.out.println("[1] Add New Asset");
            System.out.println("[2] View Issued Assets");
            System.out.println("[3] Exit System");
            printSeparator();
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> addAssetFlow();
                case "2" -> viewAssetsFlow();
                case "3" -> {
                    System.out.println("\nExiting system... Thank you!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please enter 1, 2, or 3.\n");
            }
        }
    }

    private void printHeader() {
        System.out.println("------------------------------------------------------------");
        System.out.println("          IT ASSET EQUIPMENT TRACKER");
        System.out.println("------------------------------------------------------------");
    }

    private void printSeparator() {
        System.out.println("------------------------------------------------------------");
    }

    private void addAssetFlow() {
        System.out.println("\n--- ADD NEW IT ASSET ---");

        System.out.print("Enter Device Name: ");
        String deviceName = scanner.nextLine().trim();
        if (deviceName.isBlank()) {
            System.out.println("❌ Device Name cannot be empty.\n");
            return;
        }

        System.out.print("Enter Assigned Employee Name: ");
        String assignedTo = scanner.nextLine().trim();
        if (assignedTo.isBlank()) {
            System.out.println("❌ Employee Name cannot be empty.\n");
            return;
        }

        System.out.print("Enter Serial Number: ");
        String serialNo = scanner.nextLine().trim();
        if (serialNo.isBlank()) {
            System.out.println("❌ Serial Number cannot be empty.\n");
            return;
        }

        System.out.print("Enter Condition (NEW / GOOD / REPAIR_NEEDED): ");
        String condition = scanner.nextLine().trim().toUpperCase();
        if (!condition.equals("NEW") && !condition.equals("GOOD") && !condition.equals("REPAIR_NEEDED")) {
            System.out.println("❌ Invalid condition! Use only: NEW, GOOD, or REPAIR_NEEDED.\n");
            return;
        }

        boolean success = dao.addAsset(deviceName, assignedTo, serialNo, condition);
        if (success) {
            System.out.println("✅ SUCCESS: IT asset logged into system!\n");
        }
    }

    private void viewAssetsFlow() {
        List<ScenarioEntity> assets = dao.getAllAssets();

        System.out.println("\n--------------------------------------------------------------");
        System.out.println("            IT EQUIPMENT ASSETS");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("| %-3s | %-18s | %-16s | %-12s | %-14s |%n",
                "ID", "DEVICE NAME", "ASSIGNED TO", "SERIAL NO", "CONDITION");
        System.out.println("+-----+--------------------+------------------+--------------+----------------+");

        if (assets.isEmpty()) {
            System.out.println("|                     No records found.                          |");
        } else {
            for (ScenarioEntity a : assets) {
                System.out.printf("| %-3d | %-18s | %-16s | %-12s | %-14s |%n",
                        a.id(), a.deviceName(), a.assignedTo(), a.serialNo(), a.condition());
            }
        }
        System.out.println("+-----+--------------------+------------------+--------------+----------------+");
        System.out.printf("Total Records Displayed: %d%n%n", assets.size());
    }
}