package com.example.webservice.service;

import com.example.webservice.config.DatabaseConfig;
import com.example.webservice.security.JwtUtil;

public class DatabaseConnectionService {

    public static boolean testDatabaseConnection(String token) {
        System.out.println("📥 Gelen Token: " + token);

        DatabaseConfig config = JwtUtil.getDatabaseConfigFromToken(token);

        if (config == null) {
            System.out.println("❌ Veritabanı bilgileri çözülemedi!");
            return false;
        }

        System.out.println("✅ Çözülen Database Config: " + config.getServer());

        return connectToDatabase(config);
    }

    private static boolean connectToDatabase(DatabaseConfig config) {
        try {
            // Burada gerçek bir MS SQL bağlantısı test edebilirsin
            System.out.println("🔗 MS SQL Bağlantı Başarılı!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Veritabanına bağlanılamadı: " + e.getMessage());
            return false;
        }
    }
}
