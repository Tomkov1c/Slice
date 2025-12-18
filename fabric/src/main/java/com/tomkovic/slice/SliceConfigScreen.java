package com.tomkovic.slice;

import com.tomkovic.slice.SliceConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SliceConfigScreen {

    private SliceConfigScreen() {}

    public static Screen create(Screen parent) {
        SliceConfig config = AutoConfig.getConfigHolder(SliceConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("slice.configuration.title"))
                .setSavingRunnable(() -> AutoConfig.getConfigHolder(SliceConfig.class).save());

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        /* =========================
         * Display Category
         * ========================= */
        ConfigCategory display = builder.getOrCreateCategory(
                Component.translatable("slice.configuration.category.display")
        );

        // ---- Size Section
        display.addEntry(entryBuilder
                .startTextDescription(Component.translatable("slice.configuration.category.display.size"))
                .build()
        );

        display.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.display.size.radialMenuRadius"),
                        config.display.size.radialMenuRadius)
                .setDefaultValue(75).setMin(40).setMax(200)
                .setTooltip(Component.translatable("slice.configuration.display.size.radialMenuRadius.tooltip"))
                .setSaveConsumer(v -> config.display.size.radialMenuRadius = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.display.size.itemSize"),
                        config.display.size.itemSize)
                .setDefaultValue(16).setMin(1).setMax(64)
                .setTooltip(Component.translatable("slice.configuration.display.size.itemSize.tooltip"))
                .setSaveConsumer(v -> config.display.size.itemSize = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.display.size.slotSize"),
                        config.display.size.slotSize)
                .setDefaultValue(32).setMin(16).setMax(64)
                .setTooltip(Component.translatable("slice.configuration.display.size.slotSize.tooltip"))
                .setSaveConsumer(v -> config.display.size.slotSize = v)
                .build()
        );

        // ---- Visibility Section
        display.addEntry(entryBuilder
                .startTextDescription(Component.translatable("slice.configuration.category.display.visibility"))
                .build()
        );

        display.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.display.visibility.backgroundDarkenOpacity"),
                        config.display.visibility.backgroundDarkenOpacity)
                .setDefaultValue(0).setMin(0).setMax(255)
                .setTooltip(Component.translatable("slice.configuration.display.visibility.backgroundDarkenOpacity.tooltip"))
                .setSaveConsumer(v -> config.display.visibility.backgroundDarkenOpacity = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.display.visibility.hideUnusedSlots"),
                        config.display.visibility.hideUnusedSlots)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.display.visibility.hideUnusedSlots.tooltip"))
                .setSaveConsumer(v -> config.display.visibility.hideUnusedSlots = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.display.visibility.hideSlotNumber"),
                        config.display.visibility.hideSlotNumber)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.display.visibility.hideSlotNumber.tooltip"))
                .setSaveConsumer(v -> config.display.visibility.hideSlotNumber = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.display.visibility.hideSlotSprite"),
                        config.display.visibility.hideSlotSprite)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.display.visibility.hideSlotSprite.tooltip"))
                .setSaveConsumer(v -> config.display.visibility.hideSlotSprite = v)
                .build()
        );

        // ---- Angles Section

        display.addEntry(entryBuilder
                .startTextDescription(Component.translatable("slice.configuration.category.other"))
                .build()
        );

        display.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.display.maxAngle"),
                        config.display.startAngle)
                .setDefaultValue(360).setMin(0).setMax(360)
                .setTooltip(Component.translatable("slice.configuration.display.maxAngle.tooltip"))
                .setSaveConsumer(v -> config.display.startAngle = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.display.endAngle"),
                        config.display.endAngle)
                .setDefaultValue(360).setMin(0).setMax(360)
                .setTooltip(Component.translatable("slice.configuration.display.endAngle.tooltip"))
                .setSaveConsumer(v -> config.display.endAngle = v)
                .build()
        );

        display.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.display.counterclockwiseRotation"),
                        config.display.counterclockwiseRotation)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.display.counterclockwiseRotation.tooltip"))
                .setSaveConsumer(v -> config.display.counterclockwiseRotation = v)
                .build()
        );

        /* =========================
         * Behaviour Category
         * ========================= */
        ConfigCategory behaviour = builder.getOrCreateCategory(
                Component.translatable("slice.configuration.category.behaviour")
        );

        behaviour.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.behaviour.toggleKeybind"),
                        config.behaviour.toggleKeybind)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.behaviour.toggleKeybind.tooltip"))
                .setSaveConsumer(v -> config.behaviour.toggleKeybind = v)
                .build()
        );

        behaviour.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.behaviour.clickToSelect"),
                        config.behaviour.clickToSelect)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.behaviour.clickToSelect.tooltip"))
                .setSaveConsumer(v -> config.behaviour.clickToSelect = v)
                .build()
        );

        behaviour.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.behaviour.closeOnSelect"),
                        config.behaviour.closeOnSelect)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("slice.configuration.behaviour.closeOnSelect.tooltip"))
                .setSaveConsumer(v -> config.behaviour.closeOnSelect = v)
                .build()
        );

        behaviour.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.behaviour.innerDeadzone"),
                        config.behaviour.innerDeadzone)
                .setDefaultValue(72).setMin(0).setMax(1000)
                .setTooltip(Component.translatable("slice.configuration.behaviour.innerDeadzone.tooltip"))
                .setSaveConsumer(v -> config.behaviour.innerDeadzone = v)
                .build()
        );

        behaviour.addEntry(entryBuilder
                .startIntField(Component.translatable("slice.configuration.behaviour.outerDeadzone"),
                        config.behaviour.outerDeadzone)
                .setDefaultValue(47).setMin(0).setMax(1000)
                .setTooltip(Component.translatable("slice.configuration.behaviour.outerDeadzone.tooltip"))
                .setSaveConsumer(v -> config.behaviour.outerDeadzone = v)
                .build()
        );

        /* =========================
         * Misc Category
         * ========================= */
        ConfigCategory misc = builder.getOrCreateCategory(
                Component.translatable("slice.configuration.category.misc")
        );

        misc.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("slice.configuration.misc.disableScrollingOnHotbar"),
                        config.misc.disableScrollingOnHotbar)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("slice.configuration.misc.disableScrollingOnHotbar.tooltip"))
                .setSaveConsumer(v -> config.misc.disableScrollingOnHotbar = v)
                .build()
        );

        return builder.build();
    }
}
