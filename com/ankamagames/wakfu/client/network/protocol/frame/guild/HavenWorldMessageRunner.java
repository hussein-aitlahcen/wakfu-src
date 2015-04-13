package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.framework.kernel.core.common.message.*;

class HavenWorldMessageRunner implements MessageRunner<HavenWorldInfoResult>
{
    @Override
    public boolean run(final HavenWorldInfoResult msg) {
        final ArrayList<Building> buildings = msg.getBuildings();
        HavenWorldViewManager.INSTANCE.refreshElements(buildings);
        PropertiesProvider.getInstance().setPropertyValue("havenWorld", new HavenWorldView(msg.getGuildName(), buildings));
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.getInstance(), "havenWorldPageWarning");
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20090;
    }
}
