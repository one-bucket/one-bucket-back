package com.onebucket.domain.PushMessageManage.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.service
 * <br>file name      : FirebaseInitializer
 * <br>date           : 11/25/24
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@Component
public class FirebaseInitializer {

    // JSON 데이터를 문자열로 저장
    private static final String FIREBASE_CONFIG = """
            {
              "type": "service_account",
              "project_id": "one-bucket",
              "private_key_id": "8394ba0f6e1a5712263f776136a10dd105d6a388",
              "private_key": "-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDSMkQMmxbVi2mc\\nqkyPKAk7/NRvwr8mFjq/u2QE8XX95jOINHknUiX7Np0tDGYC7RhRO9v7hMXKRhyP\\nsXT7eP4AyySO4nEpwTmN8RLnoGZT4xM4M4DyaMrmB3tzEhtNyFxewHiCTraBjftu\\nP4OvvTR49x1O5UfwIvuQh5/cyPUPuyTnSE+tRSVPRpxVifYOzsTsfCc7PElHCaTm\\njhhVA4DmC5t8m/AR/GEr7eskreDCTnP4Q/cTuJJuoxz/TCVBxMhxFW6JNlFQaD/i\\nNjPzDpo+Mdr/aHac8O0L4E8uW/P5XwR1V15pZ743B2YfCgxtjMNO4U3m73S5jQ/j\\nvJwwYjKTAgMBAAECggEAPE4clOtH9VNOpRp85ZOMdq/2eOwMME6vY0PNkdd+zS3m\\n+fxoB/57FCRamsqUV+GTGPdOCU7R+lL8LcOxdtaDNVIS9oWEBwE/VHhmPpsX0Jh0\\n8qpgI+wpzi42XlDDEaFa0vpweJ5BoVDNYpeJXfmwHMZ1G6YPahd3SkOG+26SVIUM\\ny7Je27pDeArYMt29/uA8ODxPe9IS/9MVaBgh6C5BSverAXuGzlqxJ7riPNbeQT/A\\nXso3ikCSPzs2Nvm5plBNaDOZhe67vWgynQduGvtRiNaYuY3cR6W6+WKqaxhxMLSk\\nV8+VCkVBKCXIq3sWpwVNTAXaI/+H0/I+y1F0V++HsQKBgQD4NXfer4OE5nKa4AgQ\\ndSfaNPfd/sjLiKZI6tok6qI02+nP7hI8bqz6ETb2dC/HiFI2dXb9mqM2BKk4u0+4\\nWY3246IhgcjJg30AlLFUmfVBhOsrseZBKlGfWIAF6AEi1XrVaw948NhiedkpuI3U\\nU+r4+KCWEVbI/8rZwArjTpAiwwKBgQDYy1cpww1+o7ylQQIJzuldVj/P7D4+msNG\\ne3zrC3PeWHl2vt8XpmvYRKtepZJdDk7A7mMLLhNjBSD7TogmWj+9hkZsDsGTJ1Tj\\nLeBu04qosMdHkr9kF+Wty5a/70RXViW6CdpdMQWL0osuwtcqvD00JKMO/MPXFyM3\\nchSWO58T8QKBgQCctR1OUh/GK7zVvXY59c3IfcK0vSTHJCpgRS6fOihZGTNAnT2U\\nPAgoBfBen7J+1rmZOoZ6zUQYWc8BS3mkmZ4A720wnIFJgSPN1xMyp9VpnWLg/nuy\\nTuajpTuG/xighon77pEhOWZUnjuWlLevA6CJumeg96eEVcMbwZzeMH7eAwKBgEh7\\n9DCqXqCw7inyilPtMmlapQtH2afk5tVyBFoyAhOdUT86oiyuH/C3RPToNv+WAQ7Q\\npZzfUuwQku5zW2I3WohrlLAcXuxsgh1TAW8Mm5e7Q2XS0tsMgmaRY13ZCeiJjSOe\\nl7wA3/HX+2EvE/Hd7ji45yvjpzcLLWMkaW2xkYaBAoGASxF+5z1oEFJb9enROht1\\n/4o6ht7BqkUQH3HUmf3vD0PRJNP+2Ru9Dsx/Gdc200MRLNYCx0/ZlZS6ls7sH/uG\\npcha+zvFn2+HRU785zP2xXCBIIyD3cU2BPj/bm5cLADyQuSiYX4b2oIgVYk5sD0s\\nMmFkURqHcxe2e2cIaiVNrc0=\\n-----END PRIVATE KEY-----\\n",
              "client_email": "firebase-adminsdk-ce8if@one-bucket.iam.gserviceaccount.com",
              "client_id": "118215730778304377984",
              "auth_uri": "https://accounts.google.com/o/oauth2/auth",
              "token_uri": "https://oauth2.googleapis.com/token",
              "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
              "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ce8if%40one-bucket.iam.gserviceaccount.com",
              "universe_domain": "googleapis.com"
            }
            """;


    @PostConstruct
    public void initialize() {
        try {
            // 문자열을 InputStream으로 변환
            InputStream serviceAccount = new ByteArrayInputStream(FIREBASE_CONFIG.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
