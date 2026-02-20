package com.tomkovic.slice.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.Objects;

public class ReloadConfigCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slice")
            .then(Commands.literal("reloadConfig")
                .executes(ReloadConfigCommand::reloadConfig)
            )
        );
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        try {
            Path configPath = FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-common.toml");

            if (!configPath.toFile().exists()) {
                context.getSource().sendFailure(
                    Objects.requireNonNull(Component.literal("§cConfig file not found at: " + configPath))
                );
                return 0;
            }

            com.electronwill.nightconfig.core.file.CommentedFileConfig fileConfig =
                com.electronwill.nightconfig.core.file.CommentedFileConfig.of(configPath);
            fileConfig.load();

            Config.CONFIG_SPEC.acceptConfig(fileConfig);

            Config.pushConfigToGlobal();

            context.getSource().sendSuccess(
                () -> Component.literal("§aConfig reloaded from file and pushed to global!"),
                true
            );

            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(
                Objects.requireNonNull(Component.literal("§cFailed to reload config: " + e.getMessage()))
            );
            e.printStackTrace();
            return 0;
        }
    }
}
