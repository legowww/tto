package com.tto.ttodomain.transportation.walk;


import com.tto.ttodomain.transportation.Transportation;
import com.tto.ttodomain.transportation.TransportationType;
import lombok.Getter;

@Getter
public class Walk extends Transportation {

    public Walk(int time) {
        super(TransportationType.WALK, time);
    }

    @Override
    public String toString() {
        return "도보로 " +
                super.getTime() +
                "분 이동";
    }
}
