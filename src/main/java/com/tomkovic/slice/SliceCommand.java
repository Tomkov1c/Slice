package com.tomkovic.slice;

import com.tomkovic.slice.handlers.ConfigHandler;
import com.tomkovic.slice.handlers.RadialMenuHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class SliceCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "slice";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/slice reloadConfig";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: /slice reloadConfig"));
            return;
        }

        if (args[0].equalsIgnoreCase("reloadConfig")) {
            try {
                
                ConfigHandler.getCurrentConfig();

                Config.syncConfig();
                
                sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GREEN + "Slice config reloaded successfully!"
                ));
            } catch (Exception e) {
                sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "Failed to reload config: " + e.getMessage()
                ));
                Constants.LOG.error("Failed to reload config", e);
            }
        } else {
            sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.RED + "Unknown subcommand. Usage: /slice reloadConfig"
            ));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            List<String> options = new ArrayList<String>();
            options.add("reloadConfig");
            return getListOfStringsMatchingLastWord(args, options.toArray(new String[0]));
        }
        return null;
    }
}