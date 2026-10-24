package es.codelearnacademy.filelab.config;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class PropertiesConfig {

    private final Path path;

    public PropertiesConfig(Path path) {
        this.path = path;
    }

    public Optional<String> get(String key) {
        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
            String value_result = props.getProperty(key);

            if (value_result == null) {
                return Optional.empty();
            }
            return Optional.of(value_result);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public String getOrDefault(String key, String defaultValue) {
        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
            String value_result = props.getProperty(key);
            if (value_result == null) {
                return defaultValue;
            }
            return value_result;
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public Map<String, String> findAll() {
        Properties props = new Properties();
        Map<String, String> finalMap = new HashMap<>();
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
            for (String key : props.stringPropertyNames()) {
                finalMap.put(key, props.getProperty(key));
            }
            return finalMap;
        } catch (IOException e) {
            return Map.of();
        }
    }

    public boolean put(String key, String value) {
        Properties props = new Properties();

        if (Files.exists(path)) {
            try (InputStream input = Files.newInputStream(path)) {
                props.load(input);
            } catch (IOException e) {
                return false;
            }
        }

        props.setProperty(key, value);

        try (OutputStream output = Files.newOutputStream(path)) {
            props.store(output, null);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean remove(String key) {
        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
        } catch (IOException e) {
            return false;
        }
        props.remove(key);
        try (OutputStream output = Files.newOutputStream(path)) {
            props.store(output, null);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
