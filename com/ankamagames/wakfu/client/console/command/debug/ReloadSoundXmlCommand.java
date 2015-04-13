package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.sound.*;

public class ReloadSoundXmlCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        WakfuSoundManager.getInstance().reloadRollOffPresets();
        try {
            WakfuSoundManager.getInstance().reloadBarks();
        }
        catch (Exception e) {
            manager.err("Probl\u00e8me au rechargement des barks");
        }
        try {
            WakfuSoundManager.getInstance().reloadGrounds();
        }
        catch (Exception e) {
            manager.err("Probl\u00e8me au rechargement des grounds");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReloadSoundXmlCommand.class);
    }
}
