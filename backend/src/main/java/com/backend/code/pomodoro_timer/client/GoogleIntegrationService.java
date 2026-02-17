package com.backend.code.pomodoro_timer.client;

import com.backend.code.pomodoro_timer.dto.JSONWebToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleIntegrationService {

    @Value("${app.google.client.id}")
    private String CLIENT_ID;

    public JSONWebToken verifyToken(JSONWebToken jsonWebToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(jsonWebToken.getJwt());

            jsonWebToken.setValid(idToken != null);

            return jsonWebToken;
        } catch (Exception e) {
            System.err.println("Error while the validation: " + e.getMessage());
        }
        return null;
    }
}
