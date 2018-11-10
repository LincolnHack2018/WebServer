package com.hack.collective.hockey.service;

import com.hack.collective.hockey.domain.ScreenResponse;
import com.hack.collective.hockey.domain.Device;
import com.hack.collective.hockey.enums.Direction;
import com.hack.collective.hockey.utils.ScreenResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hack.collective.hockey.enums.Direction.*;
import static java.util.Collections.emptyList;

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
        timer = System.currentTimeMillis();

        while (true) {
            if (System.currentTimeMillis() - timer > 2000) {
                if (devices.size() <= 1) {
                    sendData(emptyList());
                    tearDown();
                    return;
                }

                sendData(createScreenResponses());
                tearDown();
                return;
            }
        }
    }

    private List<ScreenResponse> createScreenResponses() {
        ScreenResponseUtil screenResponseUtil = new ScreenResponseUtil();
        List<ScreenResponse> screenResponseList = new ArrayList<ScreenResponse>();

        for(int i = 0; i < devices.size(); i++){
            Direction downDirection = screenResponseUtil.getDownDirection(devices.get(i));
            Direction upDirection = screenResponseUtil.getUpDirection(devices.get(i));

            if(downDirection != CENTER){
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != 0) {
                    int previousId = i;
                    previousId--;
                    screenResponse.setId(devices.get(previousId).getId());
                    screenResponseUtil.setScreenResponsePlusMinus(devices.get(i), downDirection, screenResponse);
                    screenResponse.setIntersectX(devices.get(previousId).getTouchUpX());
                    screenResponse.setIntersectY(devices.get(previousId).getTouchUpY());
                    screenResponseList.add(screenResponse);
                }
            }
            if(upDirection != CENTER){
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != devices.size() -1){
                    int nextId = i;
                    nextId++;
                    screenResponse.setId(devices.get(nextId).getId());
                    screenResponseUtil.setScreenResponsePlusMinus(devices.get(i),upDirection,screenResponse);
                    screenResponse.setIntersectX(devices.get(nextId).getTouchDownX());
                    screenResponse.setIntersectY(devices.get(nextId).getTouchDownY());
                    screenResponseList.add(screenResponse);
                }

            }
        }

        return screenResponseList;
    }



    private void sendData(List<ScreenResponse> coordinates) {
        template.convertAndSend("/init", coordinates);
    }

    public void tearDown() {
        isInitialising = false;
        devices.clear();
    }
}