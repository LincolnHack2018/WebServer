package com.hack.collective.hockey.utils;

import com.hack.collective.hockey.domain.Device;
import com.hack.collective.hockey.domain.Pair;
import com.hack.collective.hockey.domain.ScreenResponse;
import com.hack.collective.hockey.enums.Direction;

import java.util.ArrayList;

import static com.hack.collective.hockey.enums.Direction.*;

public class ScreenResponseUtil {
    private static final int TOLERANCE = 1;

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
        float first;
        float second;
        if(previous) {
            switch (getUpDirection(otherDevice)) {
                case BOTTOM:
                    first = otherDevice.getTouchUpX() - left;
                    second = otherDevice.getTouchUpX() + right;
                    pairs.add(new Pair<>(first, second));
                    break;
                case TOP:
                    first = otherDevice.getTouchUpX() + left;
                    second = otherDevice.getTouchUpX() - right;
                    pairs.add(new Pair<>(first, second));
                    break;
                case RIGHT:
                    first = otherDevice.getTouchUpY() - left;
                    second = otherDevice.getTouchUpY() + right;
                    pairs.add(new Pair<>(first, second));
                    break;
                case LEFT:
                    first = otherDevice.getTouchUpY() + left;
                    second = otherDevice.getTouchUpY() - right;
                    pairs.add(new Pair<>(first, second));
                    break;
            }
        }else{
            switch (getDownDirection(otherDevice)) {
                case BOTTOM:
                    first = otherDevice.getTouchDownX() + left;
                    second = otherDevice.getTouchDownX() - right;
                    pairs.add(new Pair<>(first, second));
                    break;
                case TOP:
                    first = otherDevice.getTouchDownX() - left;
                    second = otherDevice.getTouchDownX() + right;
                    pairs.add(new Pair<>(first, second));
                    break;
                case RIGHT:
                    first = otherDevice.getTouchDownY() + left;
                    second = otherDevice.getTouchDownY() - right;
                    pairs.add(new Pair<>(first, second));
                    break;
                case LEFT:
                    first = otherDevice.getTouchDownY() - left;
                    second = otherDevice.getTouchDownY() + right;
                    pairs.add(new Pair<>(first, second));
                    break;
            }
        }
        return pairs;
    }

    public Direction getDownDirection(Device device) {
        if(device.getTouchDownX() >= 0 && device.getTouchDownX() < TOLERANCE){
            return LEFT;
        }else if(device.getTouchDownX() < device.getDeviceWidth() && device.getTouchDownX() > device.getDeviceWidth() - TOLERANCE){
            return RIGHT;
        }else if(device.getTouchDownY() >= 0 && device.getTouchDownY() < TOLERANCE){
            return BOTTOM;
        }else if(device.getTouchDownY() < device.getDeviceWidth() && device.getTouchDownY() > device.getDeviceWidth() - TOLERANCE){
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