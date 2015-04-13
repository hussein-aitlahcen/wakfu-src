package com.ankamagames.wakfu.client.console.command.climate;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class TemperatureCommand implements Command
{
    protected static final Logger m_logger;
    private static final TIntArrayList m_systemIds;
    private static final ArrayList<IsoLightSource> m_lights;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        try {
            final IsoWorldScene worldScene = EffectFunctionsLibrary.getInstance().getWorldScene();
            final int argIndex = 2;
            if (!args.get(argIndex).equalsIgnoreCase("start")) {
                if (!args.get(argIndex).equalsIgnoreCase("stop")) {
                    manager.err("Arguments incorrects");
                }
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
        m_logger = Logger.getLogger((Class)TemperatureCommand.class);
        m_systemIds = new TIntArrayList();
        m_lights = new ArrayList<IsoLightSource>();
    }
}
