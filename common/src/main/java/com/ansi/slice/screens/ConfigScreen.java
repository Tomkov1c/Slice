package com.ansi.slice.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ConfigScreen extends Screen {

    private final Screen parent;

    private double leftSubScrollAmount = 0.0;
    private double rightSubScrollAmount = 0.0;

    private int leftSubX, leftSubY, leftSubWidth, leftSubHeight;
    private int rightSubX, rightSubY, rightSubWidth, rightSubHeight;

    private int mainLeftX, mainTopY, mainWidth, mainHeight;

    private final int leftContentHeight = 600;
    private final int rightContentHeight = 600;

    private static final int LEFT_SUB_BG_COLOR = 0x552266AA;
    private static final int RIGHT_SUB_BG_COLOR = 0x55AA6622;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("slice.configscreen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int bottomY = this.height - 36;

        int buttonWidth = 98;
        int buttonHeight = 20;
        int spacing = 4;
        int startX = centerX - 152;

        Button resetButton = Button.builder(Component.translatable("controls.reset"), button -> {})
                .bounds(startX, bottomY, buttonWidth, buttonHeight).build();
        resetButton.setTooltip(Tooltip.create(Component.literal("Reset all options to default values")));
        this.addRenderableWidget(resetButton);

        Button cancelButton = Button.builder(Component.translatable("gui.cancel"), button -> this.onClose())
                .bounds(startX + buttonWidth + spacing, bottomY, buttonWidth, buttonHeight).build();
        cancelButton.setTooltip(Tooltip.create(Component.literal("Discard changes and exit")));
        this.addRenderableWidget(cancelButton);

        Button doneButton = Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .bounds(startX + (buttonWidth + spacing) * 2, bottomY, buttonWidth, buttonHeight).build();
        doneButton.setTooltip(Tooltip.create(Component.literal("Save settings and exit")));
        this.addRenderableWidget(doneButton);

        int topY = 50;
        int bottomLimit = this.height - 46;
        int margin = 20;
        int halfWidth = this.width / 2;

        int leftX = margin;
        int leftWidth = halfWidth - margin - 6;
        int mainSectionHeight = bottomLimit - topY;

        this.mainLeftX = leftX;
        this.mainTopY = topY;
        this.mainWidth = leftWidth;
        this.mainHeight = mainSectionHeight;

        int innerMargin = 10;
        int subBoxTotalWidth = leftWidth - (innerMargin * 3);
        int subBoxWidth = subBoxTotalWidth / 2;
        int subBoxHeight = mainSectionHeight - (innerMargin * 2);

        this.leftSubX = leftX + innerMargin;
        this.leftSubY = topY + innerMargin;
        this.leftSubWidth = subBoxWidth;
        this.leftSubHeight = subBoxHeight;

        this.rightSubX = leftSubX + leftSubWidth + innerMargin;
        this.rightSubY = topY + innerMargin;
        this.rightSubWidth = subBoxWidth;
        this.rightSubHeight = subBoxHeight;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isInside(mouseX, mouseY, leftSubX, leftSubY, leftSubWidth, leftSubHeight)) {
            int maxScroll = Math.max(0, leftContentHeight - leftSubHeight);
            this.leftSubScrollAmount = Mth.clamp(this.leftSubScrollAmount - scrollY * 12, 0, maxScroll);
            return true;
        }

        if (isInside(mouseX, mouseY, rightSubX, rightSubY, rightSubWidth, rightSubHeight)) {
            int maxScroll = Math.max(0, rightContentHeight - rightSubHeight);
            this.rightSubScrollAmount = Mth.clamp(this.rightSubScrollAmount - scrollY * 12, 0, maxScroll);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private static boolean isInside(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        int textWidth = this.font.width(this.title);
        graphics.text(this.font, this.title, (this.width - textWidth) / 2, 20, 0xFFFFFFFF, true);

        graphics.fill(mainLeftX, mainTopY, mainLeftX + mainWidth, mainTopY + mainHeight, 0x55000000);
        graphics.outline(mainLeftX, mainTopY, mainWidth, mainHeight, 0xFF555555);

        graphics.fill(leftSubX, leftSubY, leftSubX + leftSubWidth, leftSubY + leftSubHeight, LEFT_SUB_BG_COLOR);
        graphics.outline(leftSubX, leftSubY, leftSubWidth, leftSubHeight, 0xFF555555);

        renderScrollableContent(graphics, leftSubX, leftSubY, leftSubWidth, leftSubHeight, leftSubScrollAmount);

        graphics.fill(rightSubX, rightSubY, rightSubX + rightSubWidth, rightSubY + rightSubHeight, RIGHT_SUB_BG_COLOR);
        graphics.outline(rightSubX, rightSubY, rightSubWidth, rightSubHeight, 0xFF555555);

        renderScrollableContent(graphics, rightSubX, rightSubY, rightSubWidth, rightSubHeight, rightSubScrollAmount);
    }

    private void renderScrollableContent(GuiGraphicsExtractor graphics, int x, int y, int w, int h, double scrollAmount) {
        if (scrollAmount == 0.0)
            return;

        graphics.enableScissor(x, y, x + w, y + h);
        graphics.pose().pushMatrix();
        graphics.pose().translate(0.0f, (float) -scrollAmount);

        graphics.pose().popMatrix();
        graphics.disableScissor();
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}