package com.hack.collective.hockey.utils;

import com.hack.collective.hockey.domain.Device;
import com.hack.collective.hockey.domain.Pair;
import com.hack.collective.hockey.domain.ScreenResponse;
import com.hack.collective.hockey.enums.Direction;

import java.util.ArrayList;

import static com.hack.collective.hockey.enums.Direction.*;

public class ScreenResponseUtil {
    private static final float TOLERANCE = 2.5f;

    public ArrayList<Pair<Float>> setScreenResponseOpeningPointsUpOrDown(Device otherDevice, Device device, Direction direction, boolean previous){
        float left;
        float right;
        switch (direction){
            case BOTTOM:
                left = device.getTouchDownX();
                right = device.getDeviceWidth() - device.getTouchDownX();
                return createPairs(otherDevice, left, right, previous);
            case TOP:
                left = device.getDeviceWidth() - device.getTouchDownX();
                right = device.getTouchDownX();
                return createPairs(otherDevice, left, right, previous);
            case RIGHT:
                left = device.getTouchDownY();
                right = device.getDeviceHeight() - device.getTouchDownY();
                return createPairs(otherDevice, left, right, previous);
            case LEFT:
                left = device.getDeviceHeight() - device.getTouchDownY();
                right = device.getTouchDownY();
                return createPairs(otherDevice, left, right, previous);
            default:
                throw new RuntimeException("wtf");
        }
    }

    private ArrayList<Pair<Float>> createPairs(Device otherDevice, float left, float right, boolean previous){
        ArrayList<Pair<Float>> pairs = new ArrayList<>();
        if(previous) {
            getPairs(getUpDirection(otherDevice), otherDevice, right, left);
        }else{
            getPairs(getDownDirection(otherDevice), otherDevice, right, left);
        }
        return pairs;
    }

    private Pair<Float> getPairs(Direction direction,
                                 Device otherDevice, float right, float left){
        float first;
        float second;
        switch (direction) {
            case BOTTOM:
                first = otherDevice.getTouchUpX() - right;
                second = otherDevice.getTouchUpX() + left;
                return new Pair<>(first, second);
            case TOP:
                first = otherDevice.getTouchUpX() - left;
                second = otherDevice.getTouchUpX() + right;
                return new Pair<>(first, second);
            case RIGHT:
                first = otherDevice.getTouchUpY() + left;
                second = otherDevice.getTouchUpY() - right;
                return new Pair<>(first, second);
            case LEFT:
                first = otherDevice.getTouchUpY() - left;
                second = otherDevice.getTouchUpY() + right;
                return new Pair<>(first, second);
            default:
                throw new RuntimeException("you gone done fucked up");
        }
    }

    public Direction getDownDirection(Device device) {
        if(device.getTouchDownX() >= 0 && device.getTouchDownX() < TOLERANCE){
            return LEFT;
        }else if(device.getTouchDownX() < device.getDeviceWidth() && device.getTouchDownX() > device.getDeviceWidth() - TOLERANCE){
            return RIGHT;
        }else if(device.getTouchDownY() >= 0 && device.getTouchDownY() < TOLERANCE){
            return BOTTOM;
        }else if(device.getTouchDownY() < device.getDeviceHeight() && device.getTouchDownY() > device.getDeviceHeight() - TOLERANCE){
            return TOP;
        }
        return CENTER;
    }

    public Direction getUpDirection(Device device) {
        if(device.getTouchUpX() >= 0 && device.getTouchUpX() < TOLERANCE){
            return LEFT;
        }else if((device.getTouchUpX() < device.getDeviceWidth() && device.getTouchUpX() > device.getDeviceWidth() - TOLERANCE)){
            return RIGHT;
        }else if(device.getTouchUpY() >= 0 && device.getTouchUpY() < TOLERANCE){
            return BOTTOM;
        }else if(device.getTouchUpY() < device.getDeviceHeight() && device.getTouchUpY() > device.getDeviceHeight() - TOLERANCE){
            return TOP;
        }
        return CENTER;
    }
}