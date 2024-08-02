package com.project.we_go_jim;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeGoJimApplication {

    public static void main(String[] args) {
        setUpdDBConnection();
        SpringApplication.run(WeGoJimApplication.class, args);
    }

    /**
     * Initialize database url, username and password from
     * environment variable.
     */
    public static void setUpdDBConnection() {
        Dotenv dotenv = Dotenv.load();
        final String datasource_url = dotenv.get("DATASOURCE_URL");
        final String datasource_username = dotenv.get("DATASOURCE_USERNAME");
        final String datasource_password = dotenv.get("DATASOURCE_PASSWORD");

        System.setProperty("datasource_url", datasource_url);
        System.setProperty("datasource_username", datasource_username);
        System.setProperty("datasource_password", datasource_password);
    }
}
