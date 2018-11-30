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
            if (System.currentTimeMillis() - timer > 4000) {
                System.out.println(devices);
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
        sortDevices();
        ScreenResponseUtil screenResponseUtil = new ScreenResponseUtil();
        List<ScreenResponse> screenResponseList = new ArrayList<>();

        for(int i = 0; i < devices.size(); i++){
            Direction touchDownDirection = screenResponseUtil.getDownDirection(devices.get(i));
            Direction touchUpDirection = screenResponseUtil.getUpDirection(devices.get(i));

            if(touchDownDirection != CENTER) {
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != 0) {
                    int previousId = i - 1;
                    screenResponse.setId(devices.get(previousId).getId());
                    screenResponse.setOrderNumber(previousId);
                    screenResponse.setIntersectDistances(screenResponseUtil.setScreenResponseOpeningPointsUpOrDown(devices.get(previousId), devices.get(i), touchDownDirection, true));
                    screenResponse.setDirection(screenResponseUtil.getUpDirection(devices.get(previousId)));

                    if (previousId == 0) {
                        screenResponse.setMainDevice(true);
                    }

                    screenResponseList.add(screenResponse);
                }
            }
            if(touchUpDirection != CENTER) {
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != devices.size() -1){
                    int nextId = i + 1;
                    screenResponse.setId(devices.get(nextId).getId());
                    screenResponse.setOrderNumber(nextId);
                    screenResponse.setIntersectDistances(screenResponseUtil.setScreenResponseOpeningPointsUpOrDown(devices.get(nextId), devices.get(i), touchUpDirection, false));
                    screenResponse.setDirection(screenResponseUtil.getDownDirection(devices.get(nextId)));
                    screenResponseList.add(screenResponse);
                }

            }
        }

        return screenResponseList;
    }

    private void sortDevices(){
        devices.sort(Comparator.comparing(Device::getTouchDownTime));
    }

    private void sendData(List<ScreenResponse> coordinates) {
        template.convertAndSend("/init", coordinates);
    }

    public void tearDown() {
        isInitialising = false;
        devices.clear();
    }
}