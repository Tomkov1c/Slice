package com.tomkovic.slice.classes;

public class SlotPosition {
    public final int slotIndex;
    public final double angle;
    public final int baseX;
    public final int baseY;

    public SlotPosition(int slotIndex, double angle, int baseX, int baseY) {
        this.slotIndex = slotIndex;
        this.angle = angle;
        this.baseX = baseX;
        this.baseY = baseY;
    }
}
