package com.coherent.training.api.kapitsa.util.plainobjects;

public enum Conditions {
    YOUNGER_THAN("youngerThan"),
    OLDER_THAN("olderThan");

    private final String condition;

    Conditions(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
