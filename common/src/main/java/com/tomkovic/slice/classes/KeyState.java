package com.tomkovic.slice.classes;


public class KeyState {

    public boolean pressed;
    public boolean released;

    public boolean isPressed() {
        return pressed && !released;
    }

    public boolean isReleased() {
        return released && !pressed;
    }

    public void setPressed() {
        pressed = true;
        released = false;
    }

    public void setReleased() {
        pressed = false;
        released = true;
    }
}
