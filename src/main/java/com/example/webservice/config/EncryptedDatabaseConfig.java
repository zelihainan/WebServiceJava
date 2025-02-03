package com.example.webservice.config;
import com.example.webservice.security.AESUtil;
import lombok.Data;

@Data
public class EncryptedDatabaseConfig {
    private String server;
    private String username;
    private String password;
    private String databaseName;

    public EncryptedDatabaseConfig(DatabaseConfig config) throws Exception {
        this.server = AESUtil.encrypt(config.getServer());
        this.username = AESUtil.encrypt(config.getUsername());
        this.password = AESUtil.encrypt(config.getPassword());
        this.databaseName = AESUtil.encrypt(config.getDatabaseName());
    }

    public DatabaseConfig decrypt() throws Exception {
        DatabaseConfig config = new DatabaseConfig();
        config.setServer(AESUtil.decrypt(this.server));
        config.setUsername(AESUtil.decrypt(this.username));
        config.setPassword(AESUtil.decrypt(this.password));
        config.setDatabaseName(AESUtil.decrypt(this.databaseName));
        return config;
    }
}
