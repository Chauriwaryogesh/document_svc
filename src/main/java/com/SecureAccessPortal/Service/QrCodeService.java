package com.SecureAccessPortal.Service;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QrCodeService {
    private final Map<String, QrToken> tokens = new ConcurrentHashMap<>();
    private static final long TOKEN_VALIDITY_MINUTES = 5;

    public static class QrToken {
        private final String token;
        private final String sessionId;
        private final LocalDateTime expiry;
        private String userId;
        private boolean authenticated;

        public QrToken(String token, String sessionId, LocalDateTime expiry) {
            this.token = token;
            this.sessionId = sessionId;
            this.expiry = expiry;
            this.authenticated = false;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiry);
        }

        // Getters and setters
        public String getToken() { return token; }
        public String getSessionId() { return sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public boolean isAuthenticated() { return authenticated; }
        public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
    }

    public QrToken generateToken(String sessionId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES);
        QrToken qrToken = new QrToken(token, sessionId, expiry);
        tokens.put(token, qrToken);
        return qrToken;
    }

    public QrToken validateToken(String token) {
        QrToken qrToken = tokens.get(token);
        if (qrToken == null || qrToken.isExpired()) {
            return null;
        }
        return qrToken;
    }

    public void authenticateToken(String token, String userId) {
        QrToken qrToken = tokens.get(token);
        if (qrToken != null && !qrToken.isExpired()) {
            qrToken.setUserId(userId);
            qrToken.setAuthenticated(true);
        }
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }
}
