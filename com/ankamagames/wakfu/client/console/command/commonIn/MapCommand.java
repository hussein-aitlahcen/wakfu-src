package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;

public class MapCommand implements Command
{
    protected static Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        openCloseMapDialog();
    }
    
    public static void openCloseMapDialog() {
        final UIMapFrame frame = UIMapFrame.getInstance();
        if (!WakfuGameEntity.getInstance().hasFrame(frame)) {
            MapManagerHelper.loadMap();
            if (MapManager.getInstance().isMapAvailable()) {
                WakfuGameEntity.getInstance().pushFrame(frame);
            }
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(frame);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        MapCommand.m_logger = Logger.getLogger((Class)MapCommand.class);
    }
}
