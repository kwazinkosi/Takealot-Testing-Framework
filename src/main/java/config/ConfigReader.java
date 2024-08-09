package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            // Define the path to the config.properties file relative to the current working directory
            String configFilePath = Paths.get(System.getProperty("user.dir"), "config.properties").toString();
            FileInputStream input = new FileInputStream(configFilePath);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTestDataPath() {
        return properties.getProperty("testDataPath");
    }
    
    public static String getProperty(String prop) {
        return properties.getProperty(prop);
    }
}
