package org.example.http;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;

public abstract class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(HttpURLConnection.HTTP_OK, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Resource Not Found\"}");
        h.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, -1);
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Not Acceptable\"}");
        h.sendResponseHeaders(HttpURLConnection.HTTP_NOT_ACCEPTABLE, -1);
    }

    protected void sendInternalServerError(HttpExchange h, String message) throws IOException {
        sendText(h, "{\"error\":\"Internal Server Error: " + message + "\"}");
        h.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
    }
}