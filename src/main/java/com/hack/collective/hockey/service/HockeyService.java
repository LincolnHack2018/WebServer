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
            if (System.currentTimeMillis() - timer > 1000) {
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
        ScreenResponseUtil screenResponseUtil = new ScreenResponseUtil();
        List<ScreenResponse> screenResponseList = new ArrayList<>();

        for(int i = 0; i < devices.size(); i++){
            Direction touchDownDirection = screenResponseUtil.getDownDirection(devices.get(i));
            Direction touchUpDirection = screenResponseUtil.getUpDirection(devices.get(i));

            if(touchDownDirection != CENTER){
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != 0) {
                    int previousId = i - 1;
                    screenResponse.setId(devices.get(previousId).getId());
                    screenResponseUtil.setScreenResponsePlusMinusDown(devices.get(i), touchDownDirection, screenResponse);
                    screenResponse.setIntersectX(devices.get(previousId).getTouchUpX());
                    screenResponse.setIntersectY(devices.get(previousId).getTouchUpY());
                    screenResponse.setDirection(screenResponseUtil.getUpDirection(devices.get(previousId)));
                    screenResponseList.add(screenResponse);
                }
            }
            if(touchUpDirection != CENTER){
                ScreenResponse screenResponse = new ScreenResponse();
                if(i != devices.size() -1){
                    int nextId = i;
                    nextId++;
                    screenResponse.setId(devices.get(nextId).getId());
                    screenResponseUtil.setScreenResponsePlusMinusUp(devices.get(i),touchUpDirection,screenResponse);
                    screenResponse.setIntersectX(devices.get(nextId).getTouchDownX());
                    screenResponse.setIntersectY(devices.get(nextId).getTouchDownY());
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