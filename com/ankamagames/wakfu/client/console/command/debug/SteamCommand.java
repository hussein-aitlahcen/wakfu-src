package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.steam.*;

public class SteamCommand implements Command
{
    private static final String HELP = "help";
    private static final String RESET_USER_STATS = "resetUserStats";
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() == 3) {
            final String param = args.get(2);
            if (param.equals("help")) {
                this.help(manager);
            }
            else if (param.equals("resetUserStats")) {
                this.resetUserStats(manager);
            }
        }
    }
    
    private void resetUserStats(final ConsoleManager manager) {
        final SteamClientContext context = SteamClientContext.INSTANCE;
        if (!context.isInit()) {
            return;
        }
        if (context.resetUserStats()) {
            manager.log("Stats r\u00e9initialis\u00e9es");
        }
        else {
            manager.err("Probl\u00e8me \u00e0 la r\u00e9initialisation des stats");
        }
    }
    
    private void help(final ConsoleManager manager) {
        manager.log("steam help : ce message");
        manager.log("steam resetUserStats : remet \u00e0 zero toutes les stats du joueur");
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
