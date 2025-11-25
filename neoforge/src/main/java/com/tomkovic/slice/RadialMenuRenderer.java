package com.tomkovic.slice;

import java.lang.reflect.Field;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {

    private static int itemSize = Constants.DEFAULT_ITEM_SIZE;
    private static int slotSize = Constants.DEFAULT_SLOT_SIZE;
    private static int slotRadius = Constants.DEFAULT_SLOT_RADIUS;
    private static boolean counterclockwise = false;
    private static boolean hideUnusedSlots = false;

    private static boolean hideSlotNumber = false;
    private static boolean hideSlotSprite = false;
    private static boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];

    private static int startAngle = 0;
    private static int endAngle = 360;

    private int hoveredSlot = -1;
    private int activeSlot = -1;
    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    static {
        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Constants.LOG.error("Failed to access Inventory.selected field", e);
        }
    }

    public RadialMenuRenderer() {}

    public static void updateFromConfig() {
        itemSize = Config.CONFIG.itemSize.get();
        slotSize = Config.CONFIG.slotSize.get();
        slotRadius = Config.CONFIG.radialMenuRadius.get();
        counterclockwise = Config.CONFIG.counterclockwiseRotation.get();
        hideUnusedSlots = Config.CONFIG.hideUnusedSlots.get();
        hideSlotNumber = Config.CONFIG.hideSlotNumber.get();
        hideSlotSprite = Config.CONFIG.hideSlotSprite.get();

        startAngle = Config.CONFIG.startAngle.get();
        endAngle = Config.CONFIG.endAngle.get();

        if (startAngle == 360) startAngle = 360;
        if (endAngle == 360) endAngle = 360;

        disabledSlots[0] = Config.CONFIG.disableSlot1.get();
        disabledSlots[1] = Config.CONFIG.disableSlot2.get();
        disabledSlots[2] = Config.CONFIG.disableSlot3.get();
        disabledSlots[3] = Config.CONFIG.disableSlot4.get();
        disabledSlots[4] = Config.CONFIG.disableSlot5.get();
        disabledSlots[5] = Config.CONFIG.disableSlot6.get();
        disabledSlots[6] = Config.CONFIG.disableSlot7.get();
        disabledSlots[7] = Config.CONFIG.disableSlot8.get();
        disabledSlots[8] = Config.CONFIG.disableSlot9.get();
    }

    public void onMenuOpen() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.mouseHandler != null) {
            mouseStartX = mc.mouseHandler.xpos();
            mouseStartY = mc.mouseHandler.ypos();
        }
        hoveredSlot = -1;
    }

    public void onMenuClose() {
        if (hoveredSlot >= 0 && hoveredSlot < Constants.SLOT_COUNT && (activeSlot != hoveredSlot)) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player != null) {
                Inventory inventory = player.getInventory();

                try {
                    if (selectedField != null) {
                        selectedField.setInt(inventory, hoveredSlot);
                    }
                } catch (Exception e) {
                    Constants.LOG.error("Failed to set selected slot", e);
                }

                if (mc.getConnection() != null) {
                    mc.getConnection().send(
                        new net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket(hoveredSlot)
                    );
                }
            }
        }
        hoveredSlot = -1;
    }

    public void render(GuiGraphics graphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        JsonObject json = ResourceHelper.readJsonFromResources(mc.getResourceManager(), "textures/texture_config.json");

        LocalPlayer player = mc.player;
        if (player == null) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        double mouseX = mc.mouseHandler.xpos() * screenWidth / mc.getWindow().getScreenWidth() - centerX;
        double mouseY = mc.mouseHandler.ypos() * screenHeight / mc.getWindow().getScreenHeight() - centerY;

        hoveredSlot = getHoveredSlot(mouseX, mouseY);

        Inventory inventory = player.getInventory();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        boolean offsetFromCenter =
            Utils.getBooleanOrDefault(json, Constants.JSON_OFFSET_FROM_CENTER, false);

        // Visible slots
        int[] visibleSlots = new int[Constants.SLOT_COUNT];
        int visibleCount = 0;
        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            if (!disabledSlots[i] && (!hideUnusedSlots || !inventory.getItem(i).isEmpty())) {
                visibleSlots[visibleCount++] = i;
            }
        }

        if (visibleCount == 0) return;

        // Angle range
        double startRad = Math.toRadians(startAngle) - Math.PI / 2;
        double endRad = Math.toRadians(endAngle) - Math.PI / 2;
        boolean fullCircle = (startAngle == endAngle);
        double angleRange = fullCircle ? Math.PI * 2 : (endRad - startRad + 2*Math.PI) % (2*Math.PI);

        // Evenly distribute
        double angleStep;
        if (fullCircle) {
            angleStep = angleRange / visibleCount;
        } else {
            angleStep = visibleCount > 1 ? angleRange / (visibleCount - 1) : 0;
        }

        activeSlot = inventory.getSelectedSlot();
        for (int i = 0; i < visibleCount; i++) {
            int slotIndex = visibleSlots[i];
            ItemStack stack = inventory.getItem(slotIndex);

            int displayIndex = counterclockwise ? (visibleCount - 1 - i) : i;

            double angle;
            if (fullCircle || visibleCount > 1) {
                angle = startRad + displayIndex * angleStep;
            } else {
                angle = startRad + angleRange / 2;
            }

            int baseX = centerX + (int)(Math.cos(angle) * slotRadius);
            int baseY = centerY + (int)(Math.sin(angle) * slotRadius);

            boolean isActive = (slotIndex == activeSlot);
            boolean isHovered = (slotIndex == hoveredSlot);

            // Offsets
            String xOffsetKey, yOffsetKey;
            if (isActive) {
                xOffsetKey = Constants.JSON_X_OFFSET_ACTIVE;
                yOffsetKey = Constants.JSON_Y_OFFSET_ACTIVE;
            } else if (isHovered) {
                xOffsetKey = Constants.JSON_X_OFFSET_HOVERED;
                yOffsetKey = Constants.JSON_Y_OFFSET_HOVERED;
            } else {
                xOffsetKey = Constants.JSON_X_OFFSET;
                yOffsetKey = Constants.JSON_Y_OFFSET;
            }

            int xOffset = Utils.getIntOrDefault(json, xOffsetKey, 0);
            int yOffset = Utils.getIntOrDefault(json, yOffsetKey, 0);

            int x = baseX + xOffset;
            int y = baseY + yOffset;

            if (!hideSlotSprite)
                renderSlot(graphics, x, y, isActive, isHovered);

            if (!stack.isEmpty())
                renderItem(graphics, mc, json, stack, x, y, isActive, isHovered);

            if (!hideSlotNumber)
                renderSlotNumber(graphics, mc, json, slotIndex, x, y, isActive, isHovered);
        }

        bufferSource.endBatch();
    }

    // --- Rendering helpers ---

    private void renderSlot(GuiGraphics g, int x, int y, boolean active, boolean hovered) {
        ResourceLocation tex =
            active ? Constants.SLOT_ACTIVE_TEXTURE :
            hovered ? Constants.SLOT_HOVERED_TEXTURE :
                      Constants.SLOT_TEXTURE;

        g.blit(
            RenderPipelines.GUI_TEXTURED,
            tex,
            x - slotSize / 2,
            y - slotSize / 2,
            0F, 0F,
            slotSize,
            slotSize,
            slotSize,
            slotSize
        );
    }

    private void renderItem(GuiGraphics g, Minecraft mc, JsonObject json,
                            ItemStack stack, int x, int y,
                            boolean active, boolean hovered) {

        String xKey = active ? Constants.JSON_ITEM_X_OFFSET_ACTIVE :
                      hovered ? Constants.JSON_ITEM_X_OFFSET_HOVERED :
                                Constants.JSON_ITEM_X_OFFSET;

        String yKey = active ? Constants.JSON_ITEM_Y_OFFSET_ACTIVE :
                      hovered ? Constants.JSON_ITEM_Y_OFFSET_HOVERED :
                                Constants.JSON_ITEM_Y_OFFSET;

        int ix = x + Utils.getIntOrDefault(json, xKey, 0);
        int iy = y + Utils.getIntOrDefault(json, yKey, 0);

        g.pose().pushMatrix();
        g.pose().translate(ix + 8, iy + 8);

        float scale = itemSize / 16f;
        g.pose().scale(scale, scale);
        g.pose().translate(-(ix + 8), -(iy + 8));

        g.renderItem(stack, ix, iy);
        g.renderItemDecorations(mc.font, stack, ix, iy);

        g.pose().popMatrix();
    }

    private void renderSlotNumber(GuiGraphics g, Minecraft mc, JsonObject json,
                                  int index, int x, int y,
                                  boolean active, boolean hovered) {

        String num = String.valueOf(index + 1);

        String xKey = active ? Constants.JSON_SLOT_NUMBER_X_OFFSET_ACTIVE :
                       hovered ? Constants.JSON_SLOT_NUMBER_X_OFFSET_HOVERED :
                                 Constants.JSON_SLOT_NUMBER_X_OFFSET;

        String yKey = active ? Constants.JSON_SLOT_NUMBER_Y_OFFSET_ACTIVE :
                       hovered ? Constants.JSON_SLOT_NUMBER_Y_OFFSET_HOVERED :
                                 Constants.JSON_SLOT_NUMBER_Y_OFFSET;

        int xOffset = Utils.getIntOrDefault(json, xKey, 0);
        int yOffset = Utils.getIntOrDefault(json, yKey, 0);

        int tx = x - mc.font.width(num) / 2 + xOffset;
        int ty = y + itemSize / 2 + yOffset;

        int colDefault = Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR, Constants.DEFAULT_SLOT_NUMBER_COLOR);
        int colHover   = Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_HOVERED, Constants.DEFAULT_SLOT_NUMBER_COLOR_HOVERED);
        int colActive  = Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_ACTIVE, Constants.DEFAULT_SLOT_NUMBER_COLOR_ACTIVE);

        int col = active ? colActive : hovered ? colHover : colDefault;

        g.drawString(mc.font, num, tx, ty, col);
    }

    // -------------------------
    //       HOVER LOGIC
    // -------------------------

    private int getHoveredSlot(double mx, double my) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) return -1;

        double dist = Math.sqrt(mx*mx + my*my);
        if (dist < Constants.MIN_MOUSE_DISTANCE) return -1;

        double angle = Math.atan2(my, mx) + Math.PI / 2;
        if (counterclockwise) angle = -angle;

        angle = Utils.normalizeAngle(angle);

        // Get visible
        Inventory inv = p.getInventory();
        int[] vis = new int[Constants.SLOT_COUNT];
        int count = 0;
        for (int i = 0; i < Constants.SLOT_COUNT; i++)
            if (!disabledSlots[i] && (!hideUnusedSlots || !inv.getItem(i).isEmpty()))
                vis[count++] = i;

        if (count == 0) return -1;

        double startRad = Math.toRadians(startAngle);
        double endRad = Math.toRadians(endAngle);

        boolean fullCircle = startAngle == endAngle;

        double range = fullCircle ? Math.PI * 2 :
            (endRad - startRad + Math.PI * 2) % (Math.PI * 2);

        double step = count > 1 ? range / count : 0;

        int best = -1;
        double bestDiff = Double.MAX_VALUE;

        for (int i = 0; i < count; i++) {
            double slotAngle = Utils.normalizeAngle(startRad + i * step);
            double diff = Math.abs(Utils.normalizeAngle(angle - slotAngle));

            if (diff > Math.PI) diff = Math.PI * 2 - diff;

            if (diff < bestDiff) {
                bestDiff = diff;
                best = vis[i];
            }
        }
        return best;
    }
}
