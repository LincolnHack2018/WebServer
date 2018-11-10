package com.hack.collective.hockey.service;

import com.hack.collective.hockey.domain.Coordinate;
import com.hack.collective.hockey.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class HockeyService {
    @Autowired
    private SimpMessagingTemplate template;

    private static List<Device> devices;
    private static long timer;
    private static boolean isInitialising;

    static {
        devices = new ArrayList<>();
    }

    public void init(Device device) {
        devices.add(device);

        if (!isInitialising) {
            runTimer();
        }

        timer = System.currentTimeMillis();
    }

    private void runTimer() {
        isInitialising = true;

        while (true) {
            if (System.currentTimeMillis() - timer > 2000) {
                if (devices.size() <= 1) {
                    sendData(Optional.empty());
                    return;
                }

                sendData(Optional.of(createCoordinates()));
                isInitialising = false;
            }
        }
    }

    private List<Coordinate> createCoordinates() {

        return null;
    }

    private void sendData(Optional<List<Coordinate>> coordinates) {
        template.convertAndSend("/init", coordinates);
    }

    public void tearDown() {
        devices.clear();
    }
}