package com.santorini;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class HealthController {
    
    // Serve React app at root
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    // Health check endpoint for AWS Beanstalk
    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\"}");
    }
    
    // Fallback for React Router - serve index.html for any unmatched routes
    @GetMapping("/{path:[^\\.]*}")
    public String forward() {
        return "index.html";
    }
}
