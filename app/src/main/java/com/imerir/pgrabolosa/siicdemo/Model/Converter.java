package com.imerir.pgrabolosa.siicdemo.Model;

public abstract class Converter {
    public static class Conversion {
        public final float inputValue;
        public final float outputValue;

        public Conversion(float input, float output) {
            inputValue = input;
            outputValue = output;
        }

        public String describe(String pattern, String inputPlaceholder, String outputPlaceholder) {
         return pattern.replace(inputPlaceholder, "" + inputValue).replace(outputPlaceholder, "" + outputValue);
        }

        @Override
        public String toString() {
            return describe("OLD -> NEW", "OLD", "NEW");
        }
    }

    public abstract Conversion convert(float value);
}

