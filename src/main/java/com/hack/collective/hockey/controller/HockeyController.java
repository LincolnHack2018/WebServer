package com.hack.collective.hockey.controller;

import com.hack.collective.hockey.domain.Coordinate;
import com.hack.collective.hockey.domain.Device;
import com.hack.collective.hockey.service.HockeyService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.util.Collections.singletonList;

@RestController
public class HockeyController {
    @Autowired
    private HockeyService hockeyService;

    @Autowired
    private SimpMessagingTemplate template;

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

    @GetMapping("/test")
    public ResponseEntity testMessage() {
        Coordinate  c = new Coordinate();

        template.convertAndSend(Optional.of(singletonList(c)));
        return ResponseEntity.ok("Test message sent!");
    }
}
