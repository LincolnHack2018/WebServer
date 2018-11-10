package com.hack.collective.hockey.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinate {
    private float x;
    private float y;
}
