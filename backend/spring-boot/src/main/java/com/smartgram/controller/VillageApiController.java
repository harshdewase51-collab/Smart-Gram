package com.smartgram.controller;

import com.smartgram.model.Village;
import com.smartgram.repository.VillageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/villages")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class VillageApiController {

    private final VillageRepository repository;

    public VillageApiController(VillageRepository repository) {
        this.repository = repository;
    }

    /**
     * GET /api/villages
     * Equivalent to get_villages() in backend/app.py
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVillages() {
        return ResponseEntity.ok(Map.of("villages", repository.findAllSummary()));
    }

    /**
     * GET /api/villages/{villageId}
     * Equivalent to get_village(village_id) in backend/app.py
     */
    @GetMapping("/{villageId}")
    public ResponseEntity<?> getVillageById(@PathVariable String villageId) {
        return repository.findById(villageId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Village not found")));
    }
}
