package com.ankamagames.baseImpl.client.proxyclient.base.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;

public class HelpCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final ArrayList<CommandPattern> commandSets = new ArrayList<CommandPattern>();
        final ArrayList<CommandPattern> commands = new ArrayList<CommandPattern>();
        final ArrayList<CommandPattern> customCommands = manager.getCommandDescriptorSet().getChildren();
        for (final CommandPattern commandPattern : customCommands) {
            if (commandPattern.getLevel() <= manager.getUserLevel()) {
                if (commandPattern instanceof CommandDescriptorSet) {
                    commandSets.add(commandPattern);
                }
                else {
                    commands.add(commandPattern);
                }
            }
        }
        final ArrayList<CommandPattern> globalCommands = manager.getNativeCommandDescriptorSet().getChildren();
        for (final CommandPattern commandPattern2 : globalCommands) {
            if (commandPattern2.getLevel() <= manager.getUserLevel()) {
                if (commandPattern2 instanceof CommandDescriptorSet) {
                    commandSets.add(commandPattern2);
                }
                else {
                    commands.add(commandPattern2);
                }
            }
        }
        final StringBuilder builder = new StringBuilder("# Liste des commandes #\n");
        for (final CommandPattern commandPattern3 : commandSets) {
            builder.append("[").append(commandPattern3.getName()).append("] ");
        }
        for (final CommandPattern commandPattern3 : commands) {
            builder.append(commandPattern3.getName()).append(" ");
        }
        manager.trace(builder.toString());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
