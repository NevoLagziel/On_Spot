package com.example.onspot.Enums;

public enum StainType {
    BLEACH,
    BLOOD,
    CARROT,
    CHARCOAL,
    CHOCOLATE,
    COFFEE,
    GLUE,
    HAIR_DYE,
    MARKER,
    OIL,
    PEN,
    RUST,
    WINE;

    public String getString() {
        String temp = this.toString();
        temp = temp.replace("_", " ");
        return temp;
    }
}
