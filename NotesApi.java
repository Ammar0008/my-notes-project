package com.notes;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.net.InetSocketAddress;

public class NotesApi {

    private static final Path NOTES_FILE = Path.of("notes.json");

    public static void main(String[] args) throws Exception {

        // Create server on port 8081
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Allow GET and POST for /notes
        server.createContext("/notes", exchange -> {
            try {
                if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    handleGetNotes(exchange);
                } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    handleSaveNotes(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
            }
        });

        server.start();
        System.out.println("Notes API running on http://localhost:8081/notes");
    }

    // Handle GET request (load notes)
    private static void handleGetNotes(HttpExchange exchange) throws IOException {
        String json = "[]";

        if (Files.exists(NOTES_FILE)) {
            json = Files.readString(NOTES_FILE);
        }

        byte[] response = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    // Handle POST request (save notes)
    private static void handleSaveNotes(HttpExchange exchange) throws IOException {

        InputStream is = exchange.getRequestBody();
        String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        // Save to notes.json file
        Files.writeString(NOTES_FILE, json);

        String msg = "{\"status\":\"saved\"}";
        byte[] response = msg.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}

