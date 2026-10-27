package com.smartgram;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * SmartGram Java HTTP Server (Java 21+)
 * Full drop-in Java replacement for backend/app.py and app.py.
 * Provides REST API (/api/villages) and serves frontend templates & static assets.
 */
public class SmartGramServer {

    private static final int DEFAULT_PORT = 5002;
    private static Path BASE_DIR;
    private static Path FRONTEND_DIR;
    private static Path TEMPLATES_DIR;
    private static Path STATIC_DIR;

    // In-memory Village Database matching backend/app.py
    private static final Map<String, String> VILLAGES_DB = new LinkedHashMap<>();

    static {
        // Nagazari
        VILLAGES_DB.put("nagazari", """
            {
              "name": "Nagazari",
              "population": 8500,
              "totalArea": "28800 sq meter",
              "houses": 1200,
              "mainCrops": ["Sugarcane", "Cotton", "Jowar"],
              "IncomesSources": ["Agriculture", "Sugar Mill", "Dairy"],
              "communityHall": 1,
              "healthFacility": 1,
              "bank": 1,
              "WaterSupply": {
                "boreWell": 45,
                "well": 12,
                "lake": 1,
                "own": 2
              },
              "Power Supply": {
                "agricultural": "8 hours",
                "domestic": "24 hours"
              },
              "Tanks": ["Navapur Tank", "Mutha Tank"],
              "location": "[21.08266, 78.60331]"
            }
            """.stripIndent().trim());

        // Ahmednagar
        VILLAGES_DB.put("ahmednagar", """
            {
              "name": "Ahmednagar",
              "population": 12000,
              "totalArea": "35000 sq meter",
              "houses": 1800,
              "mainCrops": ["Wheat", "Cotton", "Onion"],
              "IncomesSources": ["Agriculture", "Trade", "Services"],
              "communityHall": 1,
              "healthFacility": 2,
              "bank": 2,
              "WaterSupply": {
                "boreWell": 60,
                "well": 20,
                "lake": 2,
                "own": 5
              },
              "Power Supply": {
                "agricultural": "10 hours",
                "domestic": "24 hours"
              },
              "Tanks": ["Ahmednagar Tank", "Mula Tank"],
              "location": "[19.0919, 75.3235]"
            }
            """.stripIndent().trim());

        // Manjara
        VILLAGES_DB.put("manjara", """
            {
              "name": "Manjara",
              "population": 5200,
              "totalArea": "18000 sq meter",
              "houses": 750,
              "mainCrops": ["Rice", "Sugarcane", "Maize"],
              "IncomesSources": ["Agriculture", "Animal Husbandry"],
              "communityHall": 1,
              "healthFacility": 1,
              "bank": 0,
              "WaterSupply": {
                "boreWell": 25,
                "well": 8,
                "lake": 1,
                "own": 1
              },
              "Power Supply": {
                "agricultural": "6 hours",
                "domestic": "20 hours"
              },
              "Tanks": ["Manjara Tank"],
              "location": "[19.4, 75.5]"
            }
            """.stripIndent().trim());

        // Phaltan
        VILLAGES_DB.put("phaltan", """
            {
              "name": "Phaltan",
              "population": 9800,
              "totalArea": "32000 sq meter",
              "houses": 1400,
              "mainCrops": ["Sugarcane", "Jowar", "Gram"],
              "IncomesSources": ["Agriculture", "Sugar Factory", "Petty Trade"],
              "communityHall": 2,
              "healthFacility": 2,
              "bank": 1,
              "WaterSupply": {
                "boreWell": 50,
                "well": 15,
                "lake": 0,
                "own": 3
              },
              "Power Supply": {
                "agricultural": "9 hours",
                "domestic": "22 hours"
              },
              "Tanks": ["Phaltan Tank", "Bhima Tank"],
              "location": "[18.0, 74.8]"
            }
            """.stripIndent().trim());

        // Indapur
        VILLAGES_DB.put("indapur", """
            {
              "name": "Indapur",
              "population": 15000,
              "totalArea": "45000 sq meter",
              "houses": 2200,
              "mainCrops": ["Sugarcane", "Cotton", "Wheat"],
              "IncomesSources": ["Agriculture", "Food Processing", "Commerce"],
              "communityHall": 2,
              "healthFacility": 3,
              "bank": 2,
              "WaterSupply": {
                "boreWell": 80,
                "well": 25,
                "lake": 2,
                "own": 8
              },
              "Power Supply": {
                "agricultural": "12 hours",
                "domestic": "24 hours"
              },
              "Tanks": ["Indapur Tank", "Bhima Left Bank Canal"],
              "location": "[18.74, 75.49]"
            }
            """.stripIndent().trim());
    }

    public static void main(String[] args) throws IOException {
        initPaths();

        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        } else if (System.getenv("PORT") != null) {
            try { port = Integer.parseInt(System.getenv("PORT")); } catch (NumberFormatException ignored) {}
        }

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor()); // Java 21 Virtual Threads

        // REST API Context
        server.createContext("/api/villages", new VillagesApiHandler());

        // Static Files Context
        server.createContext("/static", new StaticFileHandler());

        // Web Routes & Template Fallback Context
        server.createContext("/", new WebRouteHandler());

        server.start();
        System.out.println("=================================================");
        System.out.println("🌾 SmartGram Java Server (JDK 21) started!");
        System.out.println("Running on: http://localhost:" + port);
        System.out.println("REST API:   http://localhost:" + port + "/api/villages");
        System.out.println("Dashboard:  http://localhost:" + port + "/dashboard");
        System.out.println("=================================================");
    }

    private static void initPaths() {
        // Resolve BASE_DIR either from current working directory or class location
        Path current = Paths.get("").toAbsolutePath();
        if (Files.exists(current.resolve("frontend"))) {
            BASE_DIR = current;
        } else if (Files.exists(current.resolve("..").resolve("frontend"))) {
            BASE_DIR = current.resolve("..").normalize().toAbsolutePath();
        } else {
            BASE_DIR = Paths.get("D:\\SmartGram-main").toAbsolutePath();
        }

        FRONTEND_DIR = BASE_DIR.resolve("frontend");
        TEMPLATES_DIR = FRONTEND_DIR.resolve("templates");
        STATIC_DIR = FRONTEND_DIR.resolve("static");

        System.out.println("[SmartGramServer] Base Directory: " + BASE_DIR);
        System.out.println("[SmartGramServer] Templates: " + TEMPLATES_DIR);
        System.out.println("[SmartGramServer] Static: " + STATIC_DIR);
    }

    // --- Handlers ---

    /**
     * Handles /api/villages and /api/villages/{village_id}
     */
    static class VillagesApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            URI uri = exchange.getRequestURI();
            String path = uri.getPath(); // e.g. /api/villages or /api/villages/nagazari

            if (path.equals("/api/villages") || path.equals("/api/villages/")) {
                // Return village summary list
                StringBuilder json = new StringBuilder("{\"villages\":[");
                int index = 0;
                for (Map.Entry<String, String> entry : VILLAGES_DB.entrySet()) {
                    String id = entry.getKey();
                    // Extract name from entry or map
                    String name = Character.toUpperCase(id.charAt(0)) + id.substring(1);
                    if (index > 0) json.append(",");
                    json.append("{\"id\":\"").append(id).append("\",\"name\":\"").append(name).append("\"}");
                    index++;
                }
                json.append("]}");
                sendJsonResponse(exchange, 200, json.toString());
                return;
            }

            // Specific village ID: /api/villages/{id}
            String villageId = path.substring("/api/villages/".length()).toLowerCase().trim();
            if (VILLAGES_DB.containsKey(villageId)) {
                sendJsonResponse(exchange, 200, VILLAGES_DB.get(villageId));
            } else {
                sendJsonResponse(exchange, 404, "{\"error\":\"Village not found\"}");
            }
        }
    }

    /**
     * Serves static assets: /static/css/*, /static/js/*, /static/images/*
     */
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String path = exchange.getRequestURI().getPath();
            // Remove "/static" prefix
            String relative = path.startsWith("/static/") ? path.substring(8) : path.substring(7);
            Path filePath = STATIC_DIR.resolve(relative).normalize();

            // Prevent path traversal
            if (!filePath.startsWith(STATIC_DIR) || !Files.exists(filePath) || Files.isDirectory(filePath)) {
                send404(exchange);
                return;
            }

            String mime = getMimeType(filePath.getFileName().toString());
            serveFile(exchange, filePath, mime);
        }
    }

    /**
     * Serves Web Routes (/, /dashboard, /login, /signup, /<page>.html)
     */
    static class WebRouteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // POST handler for form redirects (matching Flask routes)
            if ("POST".equalsIgnoreCase(method)) {
                if ("/login".equals(path)) {
                    redirect(exchange, "/dashboard");
                    return;
                } else if ("/signup".equals(path)) {
                    redirect(exchange, "/login");
                    return;
                }
            }

            // Route mapping
            String templateName;
            if ("/".equals(path) || path.isEmpty()) {
                templateName = "index.html";
            } else if ("/dashboard".equals(path)) {
                templateName = "dashboard.html";
            } else if ("/login".equals(path)) {
                templateName = "login.html";
            } else if ("/signup".equals(path)) {
                templateName = "register.html";
            } else if (path.endsWith(".html")) {
                templateName = path.startsWith("/") ? path.substring(1) : path;
            } else {
                templateName = (path.startsWith("/") ? path.substring(1) : path) + ".html";
            }

            Path templatePath = TEMPLATES_DIR.resolve(templateName).normalize();
            if (templatePath.startsWith(TEMPLATES_DIR) && Files.exists(templatePath) && !Files.isDirectory(templatePath)) {
                serveFile(exchange, templatePath, "text/html; charset=UTF-8");
            } else {
                send404(exchange);
            }
        }
    }

    // --- Helper Methods ---

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void serveFile(HttpExchange exchange, Path file, String contentType) throws IOException {
        byte[] bytes = Files.readAllBytes(file);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }

    private static void send404(HttpExchange exchange) throws IOException {
        String msg = "<h1>404 Not Found</h1><p>The requested resource was not found on SmartGram server.</p>";
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(404, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String getMimeType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=UTF-8";
        if (lower.endsWith(".css")) return "text/css; charset=UTF-8";
        if (lower.endsWith(".js") || lower.endsWith(".mjs")) return "application/javascript; charset=UTF-8";
        if (lower.endsWith(".json")) return "application/json; charset=UTF-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        if (lower.endsWith(".woff")) return "font/woff";
        if (lower.endsWith(".woff2")) return "font/woff2";
        if (lower.endsWith(".ttf")) return "font/ttf";
        return "application/octet-stream";
    }
}
