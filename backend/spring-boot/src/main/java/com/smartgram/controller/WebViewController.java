package com.smartgram.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class WebViewController {

    private final Path templatesDir;

    public WebViewController() {
        Path base = Paths.get("").toAbsolutePath();
        if (!base.resolve("frontend").toFile().exists()) {
            base = Paths.get("D:\\SmartGram-main").toAbsolutePath();
        }
        this.templatesDir = base.resolve("frontend").resolve("templates");
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<Resource> index() {
        return serveTemplate("index.html");
    }

    @GetMapping("/dashboard")
    @ResponseBody
    public ResponseEntity<Resource> dashboard() {
        return serveTemplate("dashboard.html");
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<Resource> loginGet() {
        return serveTemplate("login.html");
    }

    @PostMapping("/login")
    public String loginPost() {
        return "redirect:/dashboard";
    }

    @GetMapping("/signup")
    @ResponseBody
    public ResponseEntity<Resource> signupGet() {
        return serveTemplate("register.html");
    }

    @PostMapping("/signup")
    public String signupPost() {
        return "redirect:/login";
    }

    @GetMapping("/{page}.html")
    @ResponseBody
    public ResponseEntity<Resource> htmlPage(@PathVariable String page) {
        return serveTemplate(page + ".html");
    }

    private ResponseEntity<Resource> serveTemplate(String fileName) {
        File file = templatesDir.resolve(fileName).toFile();
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }
}
