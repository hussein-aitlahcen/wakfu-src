package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.*;

public class ConsoleLoader implements ContentInitializer
{
    private static ConsoleLoader m_instance;
    
    public static ConsoleLoader getInstance() {
        return ConsoleLoader.m_instance;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.console");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        ConsoleManager.getInstance().addView(WakfuConsoleView.getInstance());
        ConsoleManager.getInstance().addView(new StandardConsoleView());
        if (WakfuClientConstants.DEBUG_CONSOLE_COMMANDS_PATH != null) {
            ConsoleManager.getInstance().addCommandListFromXmlFile(WakfuClientConstants.DEBUG_CONSOLE_COMMANDS_PATH);
            ConsoleManager.getInstance().addCommandListFromXmlFile(WakfuClientConstants.CONSOLE_COMMANDS_PATH);
            clientInstance.fireContentInitializerDone(this);
            return;
        }
        throw new Exception("Impossible de trouver la d\u00e9finition des commandes de console.");
    }
    
    static {
        ConsoleLoader.m_instance = new ConsoleLoader();
    }
}
