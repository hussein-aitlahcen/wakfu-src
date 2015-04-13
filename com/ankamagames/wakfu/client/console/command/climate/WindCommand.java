package com.ankamagames.wakfu.client.console.command.climate;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;

public class WindCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        try {
            final int argIndex = 2;
            if (!args.get(argIndex).equalsIgnoreCase("start")) {
                manager.err("Arguments incorrects");
            }
        }
        catch (Exception e) {
            manager.err("Probl\u00e8me lors de l'execution de la commande : " + e.toString());
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WindCommand.class);
    }
}
