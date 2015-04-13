package com.ankamagames.wakfu.client.console.command.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;

public class DisableFightBorderCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        FightVisibilityManager.getInstance().doNotShowExternalFightsBorders();
        FightVisibilityManager.getInstance().updateFightVisibility();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisableFightBorderCommand.class);
    }
}
