package com.hack.collective.hockey.domain;

import com.hack.collective.hockey.enums.Direction;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ScreenResponse {
    private String id;
    private ArrayList<Pair<Float>> intersectDistances;
    private Direction direction;
    private boolean mainDevice;
    private float screenOneX;
    private float screenOneY;
    private float screenTwoX;
    private float screenTwoY;
    private int orderNumber;
}
