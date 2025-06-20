package ru.bmstu.testapp.dto;

public class TokenChangeRequest {
    private int delta;

    public TokenChangeRequest() {}

    public TokenChangeRequest(int delta) {
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
} 