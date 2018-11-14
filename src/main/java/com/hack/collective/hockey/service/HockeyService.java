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
                System.out.println(devices);
                if (devices.size() < 1) {
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
        List<ScreenResponse> screenResponseList = new ArrayList<>();
        devices.add(new Device());
        devices.get(1).setId(devices.get(0).getId());
        devices.get(1).setDeviceHeight(devices.get(0).getDeviceHeight());
        devices.get(1).setDeviceWidth(devices.get(0).getDeviceWidth());
        devices.get(1).setTouchDownX(0f);
        devices.get(1).setTouchDownY(6f);
        devices.get(1).setTouchUpX(6f);
        devices.get(1).setTouchUpY(6f);

        for(int i = 0; i < devices.size(); i++){
            Direction touchDownDirection = screenResponseUtil.getDownDirection(devices.get(i));
            Direction touchUpDirection = screenResponseUtil.getUpDirection(devices.get(i));

            if(touchDownDirection != CENTER){
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != 0) {
                    int previousId = i - 1;
                    screenResponse.setId(devices.get(previousId).getId());
                    screenResponseUtil.setScreenResponseOpeningPointsUpOrDown(devices.get(previousId), devices.get(i), touchDownDirection, screenResponse, true);
                    screenResponse.setDirection(screenResponseUtil.getUpDirection(devices.get(previousId)));

                    if (previousId == 0) {
                        screenResponse.setMainDevice(true);
                    }

                    screenResponseList.add(screenResponse);
                }
            }
            if(touchUpDirection != CENTER){
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != devices.size() -1){
                    int nextId = i;
                    nextId++;
                    screenResponse.setId(devices.get(nextId).getId());
                    screenResponseUtil.setScreenResponseOpeningPointsUpOrDown(devices.get(nextId), devices.get(i), touchDownDirection, screenResponse, false);
                    screenResponse.setDirection(screenResponseUtil.getDownDirection(devices.get(nextId)));
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