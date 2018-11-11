package com.hack.collective.hockey.domain;

import com.hack.collective.hockey.enums.Direction;
import lombok.Data;

@Data
public class ScreenResponse {
    private String id;
    private float intersectX;
    private float intersectY;
    private float intersectPlus;
    private float intersectMinus;
    private Direction direction;
    private boolean mainDevice;
}
