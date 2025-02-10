package com.example.crypto.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {
    public static Connection connect() throws Exception {
        Connection con = null;
        Class.forName("org.postgresql.Driver");
       con = DriverManager.getConnection("jdbc:postgresql://db:5432/crypto", "postgres", "root");
        // con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/crypto", "postgres", "root");
        return con;
    }
}
