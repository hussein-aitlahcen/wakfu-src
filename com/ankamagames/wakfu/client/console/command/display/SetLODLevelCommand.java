package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;

public class SetLODLevelCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGamePreferences gamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final int lodLevel = gamePreferences.getIntValue(KeyPreferenceStoreEnum.LOD_LEVEL_KEY);
        final int level = (lodLevel + 1) % 3;
        gamePreferences.setValue(KeyPreferenceStoreEnum.LOD_LEVEL_KEY, level);
        setLODLevel((byte)level);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static void setLODLevel(final byte level) {
        WakfuClientInstance.getInstance().setLODLevel(level);
    }
}
