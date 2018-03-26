package com.imerir.pgrabolosa.siicdemo.Model;

public class LinearConverter extends Converter {
    private float mRate;

    public LinearConverter(float unitValueInUsd) {
        mRate = unitValueInUsd;
    }

    public Conversion convert(float value) {
        float newValue = value * mRate;
        return new Conversion(value, newValue);
    }
}
