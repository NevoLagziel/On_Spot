package com.example.onspot.Enums;

public enum StainType {
    BEET,
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
    POMEGRANATE,
    RUST,
    WINE;

    public String getString() {
        String temp = this.toString();
        temp = temp.replace("_", " ");
        return temp;
    }
}
