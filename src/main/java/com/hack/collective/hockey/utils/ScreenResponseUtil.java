package com.hack.collective.hockey.utils;

import com.hack.collective.hockey.domain.Device;
import com.hack.collective.hockey.domain.ScreenResponse;
import com.hack.collective.hockey.enums.Direction;

import static com.hack.collective.hockey.enums.Direction.*;

public class ScreenResponseUtil {
    private static final int TOLERANCE = 30;

    public void setScreenResponsePlusMinus(Device device, Direction direction, ScreenResponse screenResponse){
        switch (direction){
            case BOTTOM:
            case TOP:
                screenResponse.setIntersectMinus(device.getTouchDownX());
                screenResponse.setIntersectPlus(device.getDeviceWidth() - device.getTouchDownX());
                break;
            case RIGHT:
            case LEFT:
                screenResponse.setIntersectMinus(device.getTouchDownY());
                screenResponse.setIntersectPlus(device.getDeviceWidth() - device.getTouchDownY());
                break;
        }
    }

    public Direction getDownDirection(Device device) {
        if(device.getTouchDownX() > 0 && device.getTouchDownX() < TOLERANCE){
            return LEFT;
        }else if(device.getTouchDownX() < device.getDeviceWidth() && device.getTouchDownX() > device.getDeviceWidth() - TOLERANCE){
            return RIGHT;
        }else if(device.getTouchDownY() > 0 && device.getTouchDownY() < TOLERANCE){
            return BOTTOM;
        }else if(device.getTouchDownY() < device.getDeviceWidth() && device.getTouchDownY() > device.getDeviceWidth() - TOLERANCE){
            return TOP;
        }
        return CENTER;
    }

    public Direction getUpDirection(Device device) {
        if(device.getTouchUpX() > 0 && device.getTouchUpX() < TOLERANCE){
            return LEFT;
        }else if((device.getTouchUpX() < device.getDeviceWidth() && device.getTouchUpX() > device.getDeviceWidth() - TOLERANCE)){
            return RIGHT;
        }else if(device.getTouchUpY() > 0 && device.getTouchUpY() < TOLERANCE){
            return BOTTOM;
        }else if(device.getTouchUpY() < device.getDeviceHeight() && device.getTouchUpY() > device.getDeviceHeight() - TOLERANCE){
            return TOP;
        }
        return CENTER;
    }
}