package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.protector.*;

public class ClimateCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId());
        if (WakfuGameEntity.getInstance().hasFrame(UIWeatherInfoFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoFrame.getInstance());
        }
        else if (ProtectorView.getInstance().getProtector() != null && worldInfo.m_isExterior) {
            WakfuGameEntity.getInstance().pushFrame(UIWeatherInfoFrame.getInstance());
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
