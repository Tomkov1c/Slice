package com.tomkovic.slice;

import java.lang.reflect.Field;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class RadialMenuRenderer {
    private static int itemSize = Config.itemSize;
    private static int slotSize = Config.slotSize;
    private static int slotRadius = Config.radialMenuRadius;
    private static boolean counterclockwise = Config.counterclockwiseRotation;
    private static boolean hideUnusedSlots = Config.hideUnusedSlots;
    private static boolean hideSlotNumber = Config.hideSlotNumber;
    private static boolean hideSlotSprite = Config.hideSlotSprite;
    private static boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];
    private static int startAngle = Config.startAngle;
    private static int endAngle = Config.endAngle;
    private static int backgroundDarkenOpacity = Config.backgroundDarkenOpacity;
    private static int innerDeadzoneRadius = Config.innerDeadzone;
    private static int outerDeadzoneRadius = Config.outerDeadzone;
    public static boolean clickToSelect = Config.clickToSelect;
    private int hoveredSlot = -1;
    private int activeSlot = -1;
    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    public RadialMenuRenderer() {
        try {
            selectedField = InventoryPlayer.class.getDeclaredField("currentItem");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Constants.LOG.error("Failed to access InventoryPlayer.currentItem field", e);
        }
    }
    
    public static void updateFromConfig() {
        itemSize = Config.itemSize;
        slotSize = Config.slotSize;
        slotRadius = Config.radialMenuRadius;
        counterclockwise = Config.counterclockwiseRotation;
        hideUnusedSlots = Config.hideUnusedSlots;
        hideSlotNumber = Config.hideSlotNumber;
        hideSlotSprite = Config.hideSlotSprite;

        startAngle = Config.startAngle;
        endAngle = Config.endAngle;
        
        backgroundDarkenOpacity = Config.backgroundDarkenOpacity;
        
        innerDeadzoneRadius = Config.innerDeadzone;
        outerDeadzoneRadius = Config.outerDeadzone;

        disabledSlots[0] = Config.disableSlot1;
        disabledSlots[1] = Config.disableSlot2;
        disabledSlots[2] = Config.disableSlot3;
        disabledSlots[3] = Config.disableSlot4;
        disabledSlots[4] = Config.disableSlot5;
        disabledSlots[5] = Config.disableSlot6;
        disabledSlots[6] = Config.disableSlot7;
        disabledSlots[7] = Config.disableSlot8;
        disabledSlots[8] = Config.disableSlot9;

        clickToSelect = Config.clickToSelect;
    }

    public void onMenuOpen() {
        Minecraft mc = Minecraft.getMinecraft();
        mouseStartX = Mouse.getX();
        mouseStartY = Mouse.getY();
        hoveredSlot = -1;
        
        mc.mouseHelper.ungrabMouseCursor();
        mc.inGameHasFocus = false;
    }

    public void onMenuClose() {
        if (!clickToSelect) selectHoveredSlot();
        hoveredSlot = -1;
        
        Minecraft mc = Minecraft.getMinecraft();
        mc.mouseHelper.grabMouseCursor();
        mc.inGameHasFocus = true;
    }

    public int getHoveredSlot() {
        return hoveredSlot;
    }

    public void render(float partialTick) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.isGamePaused()) return;

        if (mc.thePlayer == null) return;

        JsonObject json = ResourceHelper.readJsonFromResources("textures/texture_config.json");

        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        if (backgroundDarkenOpacity > 0) renderBackground(json, screenWidth, screenHeight);

        InventoryPlayer inventory = mc.thePlayer.inventory;
        int[] visibleSlots = getVisibleSlots(inventory, hideUnusedSlots, disabledSlots);
        if (visibleSlots.length == 0) return;

        SlotPosition[] slotPositions = calculateSlotPositions(visibleSlots, centerX, centerY, startAngle, endAngle, counterclockwise, slotRadius);

        double mouseX = Mouse.getX() * screenWidth / (double)mc.displayWidth - centerX;
        double mouseY = (mc.displayHeight - Mouse.getY()) * screenHeight / (double)mc.displayHeight - centerY;

        hoveredSlot = getHoveredSlotInternal(mouseX, mouseY, slotPositions, slotRadius, innerDeadzoneRadius, outerDeadzoneRadius);

        activeSlot = inventory.currentItem;

        for (SlotPosition pos : slotPositions) {
            boolean isActive = (pos.slotIndex == activeSlot);
            boolean isHovered = (pos.slotIndex == hoveredSlot);

            String state = isActive ? "_active" : (isHovered ? "_hovered" : "");
            int xOffset = ResourceHelper.getIntOrDefault(json, Constants.JSON_X_OFFSET + state, 0);
            int yOffset = ResourceHelper.getIntOrDefault(json, Constants.JSON_Y_OFFSET + state, 0);
            int x = pos.baseX + xOffset;
            int y = pos.baseY + yOffset;

            if (!hideSlotSprite) renderSlot(x, y, isActive, isHovered);
            ItemStack stack = inventory.getStackInSlot(pos.slotIndex);
            if (stack != null) renderItem(mc, json, stack, x, y, isActive, isHovered);
            if (!hideSlotNumber) renderSlotNumber(mc, json, pos.slotIndex, x, y, isActive, isHovered);
        }
    }

    private void renderSlot(int x, int y, boolean active, boolean hovered) {
        ResourceLocation tex = active ? Constants.SLOT_ACTIVE_TEXTURE :
            hovered ? Constants.SLOT_HOVERED_TEXTURE :
            Constants.SLOT_TEXTURE;
        
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(tex);
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        
        drawScaledTexture(x - slotSize / 2, y - slotSize / 2, slotSize, slotSize);
        
        GlStateManager.disableBlend();
    }

    private void renderItem(Minecraft mc, JsonObject json, ItemStack stack, int x, int y, boolean active, boolean hovered) {
        String state = active ? "_active" : (hovered ? "_hovered" : "");
        int ix = x + ResourceHelper.getIntOrDefault(json, Constants.JSON_ITEM_X_OFFSET + state, 0);
        int iy = y + ResourceHelper.getIntOrDefault(json, Constants.JSON_ITEM_Y_OFFSET + state, 0);
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(ix + 8, iy + 8, 0);
        float scale = itemSize / 16f;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(-(ix + 8), -(iy + 8), 0);
        
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, ix, iy);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, ix, iy, null);
        RenderHelper.disableStandardItemLighting();
        
        GlStateManager.popMatrix();
    }

    private void renderSlotNumber(Minecraft mc, JsonObject json, int index, int x, int y, boolean active, boolean hovered) {
        String num = String.valueOf(index + 1);
        String state = active ? "_active" : (hovered ? "_hovered" : "");
        int xOffset = ResourceHelper.getIntOrDefault(json, Constants.JSON_SLOT_NUMBER_X_OFFSET + state, 0);
        int yOffset = ResourceHelper.getIntOrDefault(json, Constants.JSON_SLOT_NUMBER_Y_OFFSET + state, 0);
        int tx = x - mc.fontRendererObj.getStringWidth(num) / 2 + xOffset;
        int ty = y + itemSize / 2 + yOffset;
        int col = active ? ResourceHelper.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_ACTIVE, Constants.DEFAULT_SLOT_NUMBER_COLOR_ACTIVE) :
            hovered ? ResourceHelper.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_HOVERED, Constants.DEFAULT_SLOT_NUMBER_COLOR_HOVERED) :
            ResourceHelper.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR, Constants.DEFAULT_SLOT_NUMBER_COLOR);
        mc.fontRendererObj.drawStringWithShadow(num, tx, ty, col);
    }

    private void renderBackground(JsonObject json, int screenWidth, int screenHeight) {
        int baseColor = ResourceHelper.parseColor(json, Constants.JSON_BACKGROUND_OVERLAY_COLOR, 0x000000);
        int colorWithAlpha = (backgroundDarkenOpacity << 24) | (baseColor & 0xFFFFFF);
        drawRect(0, 0, screenWidth, screenHeight, colorWithAlpha);
    }

    public void selectHoveredSlot() {
        if (hoveredSlot < 0 || hoveredSlot >= Constants.SLOT_COUNT || activeSlot == hoveredSlot) return;

        selectSlot(hoveredSlot);
    }

    private static int[] getVisibleSlots(InventoryPlayer inventory, boolean hideUnusedSlots, boolean[] disabledSlots) {
        int[] temp = new int[Constants.SLOT_COUNT];
        int count = 0;
        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            if (!disabledSlots[i] && (!hideUnusedSlots || inventory.getStackInSlot(i) != null)) {
                temp[count++] = i;
            }
        }
        int[] result = new int[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    private static SlotPosition[] calculateSlotPositions(int[] visibleSlots, int centerX, int centerY, int startAngle, int endAngle, boolean counterclockwise, int slotRadius) {
        SlotPosition[] positions = new SlotPosition[visibleSlots.length];
        
        boolean fullCircle = (startAngle == endAngle) || ((startAngle == 0 && endAngle == 360) || (startAngle == 360 && endAngle == 0));
        double startRad = Math.toRadians(startAngle) - Math.PI / 2;
        double endRad = Math.toRadians(endAngle) - Math.PI / 2;
        double angleRange = fullCircle ? Math.PI * 2 : (endRad - startRad + 2 * Math.PI) % (2 * Math.PI);
        double angleStep = fullCircle ? angleRange / visibleSlots.length : 
                              (visibleSlots.length > 1 ? angleRange / (visibleSlots.length - 1) : 0);
        
        for (int i = 0; i < visibleSlots.length; i++) {
            int slotIndex = visibleSlots[i];
            int displayIndex = counterclockwise ? (visibleSlots.length - 1 - i) : i;
            double angle = (fullCircle || visibleSlots.length > 1) ? 
                startRad + displayIndex * angleStep : 
                startRad + angleRange / 2;
            
            int baseX = centerX + (int)(Math.cos(angle) * slotRadius);
            int baseY = centerY + (int)(Math.sin(angle) * slotRadius);
            
            positions[i] = new SlotPosition(slotIndex, angle, baseX, baseY);
        }
        
        return positions;
    }

    private static int getHoveredSlotInternal(double mx, double my, SlotPosition[] slotPositions, int slotRadius, int innerDeadzoneRadius, int outerDeadzoneRadius) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.isGamePaused()) return -1;
        if (slotPositions == null || slotPositions.length == 0) return -1;
        
        double dist = Math.sqrt(mx * mx + my * my);
        double innerBoundary = slotRadius - innerDeadzoneRadius;
        double outerBoundary = slotRadius + outerDeadzoneRadius;
        if (dist < innerBoundary || dist > outerBoundary) return -1;
        
        double mouseAngle = (Math.atan2(my, mx) + 2 * Math.PI) % (2 * Math.PI);
        int bestSlot = -1;
        double bestAngularDistance = Double.MAX_VALUE;
        
        for (SlotPosition pos : slotPositions) {
            double slotAngle = (pos.angle + 2 * Math.PI) % (2 * Math.PI);
            double angularDistance = Math.abs(mouseAngle - slotAngle);
            if (angularDistance > Math.PI) angularDistance = 2 * Math.PI - angularDistance;
            
            if (angularDistance < bestAngularDistance) {
                bestAngularDistance = angularDistance;
                bestSlot = pos.slotIndex;
            }
        }
        
        double maxAcceptable = (Math.PI * 2 / slotPositions.length) / 2 + 0.2;
        if (bestAngularDistance > maxAcceptable) return -1;
        
        return bestSlot;
    }

    private static void selectSlot(int index) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.inventory.currentItem = index;
        }
    }

    private void drawScaledTexture(int x, int y, int width, int height) {
        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
    }

    private void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}