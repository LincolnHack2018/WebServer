package com.hack.collective.hockey.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Pair<T> implements Serializable {

    T first;
    T second;
}