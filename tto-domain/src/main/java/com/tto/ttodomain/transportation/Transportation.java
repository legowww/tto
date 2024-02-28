package com.tto.ttodomain.transportation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Transportation {

    private final TransportationType transportationType;
    private final int time;
}
