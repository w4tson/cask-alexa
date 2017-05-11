package com.caskalexa.service.ocr;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public class GoogleCredentialsSupport {

    public static final String TMPDIR = "java.io.tmpdir";
    public static final String GOOGLE_APPLICATION_CREDENTIALS = "GOOGLE_APPLICATION_CREDENTIALS";

    /**
     * If ENV var 'google_creds' is set then that is the content of the credentials needed
     *
     *
     * This method reads the content of both and places them in a new temporary file, then points to the
     */
    public static void configure() {
        String credential = getContentFromJavaSystemProperty()
                .orElseThrow(() -> new RuntimeException("No Envrionment variable 'google_creds' found. "));

        log.info("Google Credential {} ", credential);

        String temporaryGoogleCredentialsFile = writeCredentialsToTempFile(credential);
        setEnv(GOOGLE_APPLICATION_CREDENTIALS, temporaryGoogleCredentialsFile);
        log.info("Creds file = {}", temporaryGoogleCredentialsFile);
    }

    private static String writeCredentialsToTempFile(String credentialsContent) {
        String tmpDir = System.getProperty(TMPDIR);

        Path path = Paths.get(tmpDir, "creds.json");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(credentialsContent);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return path.toAbsolutePath().toString();
    }

    private static Optional<String> getContentFromJavaSystemProperty() {
        return Optional.ofNullable(System.getProperty("google_creds"));
    }

    /**
     * Places an  key/value pair in the environment
     * @param key
     * @param value
     */
    private static void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }

}
