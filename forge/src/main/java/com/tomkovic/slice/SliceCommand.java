package com.tomkovic.slice;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.tomkovic.slice.handlers.ConfigHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SliceCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_SUBCOMMANDS = 
        (context, builder) -> SharedSuggestionProvider.suggest(
            new String[]{"reloadConfig"}, builder
        );

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(
            Commands.literal("slice")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("reloadConfig")
                    .executes(SliceCommand::reloadConfig)
                )
        );
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        try {
            ConfigHandler.getCurrentConfig();
            
            source.sendSuccess(
                () -> Component.literal("§aSlice config reloaded successfully!"),
                true
            );
            return 1;
        } catch (Exception e) {
            source.sendFailure(
                Component.literal("§cFailed to reload config: " + e.getMessage())
            );
            return 0;
        }
    }
}