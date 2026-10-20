package com.scc.ui;

import com.scc.dao.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        new ScenarioMenu().show();
    }
}