package com.caskalexa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.http.util.TextUtils.isBlank;

@Slf4j
public class GoogleCredentialsSupport {

    public static final String TMPDIR = "java.io.tmpdir";
    public static final String GOOGLE_APPLICATION_CREDENTIALS = "GOOGLE_APPLICATION_CREDENTIALS";

    /**
     * If ENV var 'google_creds' is set then that is the content of the credentials needed
     * else if GOOGLE_APPLICATION_CREDENTIALS system property is set then that is the location to file that contains the credential
     *
     * This method reads the content of both and places them in a new temporary file, then points to the
     */
    public static void configure() {
        String credential = getContentFromEnvProperty()
                .orElseGet(GoogleCredentialsSupport::getContentFromSystemProperty);

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

    private static Optional<String> getContentFromEnvProperty() {
        return Optional.ofNullable(System.getenv("google_creds"));
    }

    private static String getContentFromSystemProperty() {
        log.info("ENVIRONMENT variable 'google_creds' is empty, trying java system property 'GOOGLE_APPLICATION_CREDENTIALS'");
        String google_application_credentials_file = System.getProperty(GOOGLE_APPLICATION_CREDENTIALS);
        String creds_content = null;
        try {
            creds_content = new String(Files.readAllBytes(Paths.get(google_application_credentials_file)));
        } catch (IOException e) {
            log.error("Problem registering google creds. Either specify an ENVIRONMENT variable 'google_creds' with the json " +
                    "representation of the credential or the java system property 'GOOGLE_APPLICATION_CREDENTIALS' which is " +
                    "a path to a file which contains the json conent");
        }
        return creds_content;
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

//    @Value("${spring.rabbitmq.host}")
}
