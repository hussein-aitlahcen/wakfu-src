package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class EnableMiniMapCommand implements Command
{
    private static boolean m_enableMiniMap;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGamePreferences wgp = WakfuClientInstance.getInstance().getGamePreferences();
        final boolean enable = wgp.getBooleanValue(WakfuKeyPreferenceStoreEnum.MINIMAP_ENABLE);
        wgp.setValue(WakfuKeyPreferenceStoreEnum.MINIMAP_ENABLE, !enable);
        enableMiniMap(!enable);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static void enableMiniMap(final boolean enable) {
        final boolean newEnable = enable && MiniMapManager.getInstance().isMapAvailable();
        if (newEnable == EnableMiniMapCommand.m_enableMiniMap) {
            return;
        }
        EnableMiniMapCommand.m_enableMiniMap = newEnable;
        final UIMessage msg = new UIMessage();
        msg.setId(16606);
        msg.setBooleanValue(enable);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static boolean isEnableMiniMap() {
        return EnableMiniMapCommand.m_enableMiniMap;
    }
    
    static {
        EnableMiniMapCommand.m_enableMiniMap = true;
    }
}
