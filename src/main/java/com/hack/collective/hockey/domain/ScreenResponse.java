package com.hack.collective.hockey.domain;

import lombok.Data;

@Data
public class ScreenResponse {
    private String id;
    private float intersectX;
    private float intersectY;
    private float intersectPlus;
    private float intersectMinus;
}
