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

    private static int itemSize = Config.CONFIG.itemSize.getDefault();
    private static int slotSize = Config.CONFIG.slotSize.getDefault();
    private static int slotRadius = Config.CONFIG.radialMenuRadius.getDefault();
    private static boolean counterclockwise = Config.CONFIG.counterclockwiseRotation.getDefault();
    private static boolean hideUnusedSlots = Config.CONFIG.hideUnusedSlots.getDefault();

    private static boolean hideSlotNumber = Config.CONFIG.hideSlotNumber.getDefault();
    private static boolean hideSlotSprite = Config.CONFIG.hideSlotSprite.getDefault();
    private static boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];

    private static int startAngle = Config.CONFIG.startAngle.getDefault();
    private static int endAngle = Config.CONFIG.endAngle.getDefault();
    
    private static int backgroundDarkenOpacity = Config.CONFIG.backgroundDarkenOpacity.getDefault();
    
    private static int innerDeadzoneRadius = Config.CONFIG.innerDeadzone.getDefault();
    private static int outerDeadzoneRadius = Config.CONFIG.outerDeadzone.getDefault();

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
        
        // Load background darkening opacity
        backgroundDarkenOpacity = Config.CONFIG.backgroundDarkenOpacity.get();
        
        // Load deadzone settings
        innerDeadzoneRadius = Config.CONFIG.innerDeadzone.get();
        outerDeadzoneRadius = Config.CONFIG.outerDeadzone.get();

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

        if (backgroundDarkenOpacity > 0) {
            int baseColor = Utils.parseColor(json, Constants.JSON_BACKGROUND_OVERLAY_COLOR, 0x000000);
            
            int r = (baseColor >> 16) & 0xFF;
            int g = (baseColor >> 8) & 0xFF;
            int b = baseColor & 0xFF;
            
            int colorWithAlpha = (backgroundDarkenOpacity << 24) | (r << 16) | (g << 8) | b;
            
            graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
        }

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

        double dist = Math.sqrt(mx * mx + my * my);
        
        double innerBoundary = slotRadius - innerDeadzoneRadius;
        
        double outerBoundary = slotRadius + outerDeadzoneRadius;
        
        if (dist < innerBoundary || dist > outerBoundary) {
            return -1;
        }

        double mouseAngle = Math.atan2(my, mx);
        
        Inventory inv = p.getInventory();
        int[] visibleSlots = new int[Constants.SLOT_COUNT];
        int visibleCount = 0;
        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            if (!disabledSlots[i] && (!hideUnusedSlots || !inv.getItem(i).isEmpty())) {
                visibleSlots[visibleCount++] = i;
            }
        }

        if (visibleCount == 0) return -1;

        double startRad = Math.toRadians(startAngle) - Math.PI / 2;
        double endRad = Math.toRadians(endAngle) - Math.PI / 2;
        
        boolean fullCircle = (startAngle == endAngle);
        
        double angleRange;
        if (fullCircle) {
            angleRange = Math.PI * 2;
        } else {
            angleRange = (endRad - startRad + 2 * Math.PI) % (2 * Math.PI);
        }

        double angleStep;
        if (fullCircle) {
            angleStep = angleRange / visibleCount;
        } else {
            angleStep = visibleCount > 1 ? angleRange / (visibleCount - 1) : 0;
        }

        mouseAngle = (mouseAngle + 2 * Math.PI) % (2 * Math.PI);
        
        int bestSlot = -1;
        double bestAngularDistance = Double.MAX_VALUE;

        for (int i = 0; i < visibleCount; i++) {
            int slotIndex = visibleSlots[i];
            
            int displayIndex = counterclockwise ? (visibleCount - 1 - i) : i;
            
            double slotAngle;
            if (fullCircle || visibleCount > 1) {
                slotAngle = startRad + displayIndex * angleStep;
            } else {
                slotAngle = startRad + angleRange / 2;
            }
            
            slotAngle = (slotAngle + 2 * Math.PI) % (2 * Math.PI);
            
            double angularDistance = Math.abs(mouseAngle - slotAngle);
            if (angularDistance > Math.PI) {
                angularDistance = 2 * Math.PI - angularDistance;
            }
            
            if (!fullCircle) {
                double normalizedStart = (startRad + 2 * Math.PI) % (2 * Math.PI);
                double relativeAngle = (mouseAngle - normalizedStart + 2 * Math.PI) % (2 * Math.PI);
                
                if (relativeAngle > angleRange) {
                    // Mouse is outside the arc
                    double distToStart = Math.abs((mouseAngle - normalizedStart + Math.PI) % (2 * Math.PI) - Math.PI);
                    double distToEnd = Math.abs((mouseAngle - (normalizedStart + angleRange)) % (2 * Math.PI));
                    if (distToEnd > Math.PI) distToEnd = 2 * Math.PI - distToEnd;
                    
                    // Reasonably close to the arc
                    double minDistToArc = Math.min(distToStart, distToEnd);
                    if (minDistToArc > Math.PI / 4) {
                        continue;
                    }
                }
            }
            
            if (angularDistance < bestAngularDistance) {
                bestAngularDistance = angularDistance;
                bestSlot = slotIndex;
            }
        }
        
        // For non-full circles
        if (!fullCircle && bestSlot != -1) {
            double maxAcceptableDistance = fullCircle ? Math.PI : angleStep / 2 + 0.2;
            if (bestAngularDistance > maxAcceptableDistance) {
                return -1;
            }
        }

        return bestSlot;
    }
}