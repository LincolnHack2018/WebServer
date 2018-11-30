package com.hack.collective.hockey.domain;

import lombok.Data;

@Data
public class Device {
    private String id;
    private float touchDownX;
    private float touchDownY;
    private float touchUpX;
    private float touchUpY;
    private float deviceWidth;
    private float deviceHeight;
    private long touchDownTime;
}