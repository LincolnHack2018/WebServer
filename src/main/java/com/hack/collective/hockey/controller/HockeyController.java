package com.hack.collective.hockey.controller;

import com.hack.collective.hockey.domain.Device;
import com.hack.collective.hockey.service.HockeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HockeyController {
    @Autowired
    private HockeyService hockeyService;

    @PostMapping("/init")
    public ResponseEntity initialiseDevice(@RequestBody Device device) {
        hockeyService.init(device);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/teardown")
    public ResponseEntity teardown() {
        hockeyService.tearDown();
        return ResponseEntity.ok().build();
    }
}
