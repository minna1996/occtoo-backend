package com.valoriz.occtoo.config;

import com.valoriz.occtoo.controller.OcctooDataImportController;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;
@Component
public class OcctooConfiguration {
    public static   String DATA_PROVIDER_ID;
    public  static String DATA_PROVIDER_SECRET;
    public static  String DATA_SOURCE;

    static {
        loadConfig();
    }

    public  static void loadConfig() {
        try (InputStream input = OcctooDataImportController.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            DATA_PROVIDER_ID = prop.getProperty("data.provider.id");
            DATA_PROVIDER_SECRET = prop.getProperty("data.provider.secret");
            DATA_SOURCE = prop.getProperty("data.source");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
