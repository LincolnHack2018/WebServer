package com.hack.collective.hockey.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T> {

    T first;
    T second;
}