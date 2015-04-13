package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;

public class WorldEditorCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        if (localAccount != null && AdminRightHelper.checkRight(localAccount.getAdminRights(), AdminRightsEnum.DEBUG_WORLD_EDITOR)) {
            if (WakfuGameEntity.getInstance().hasFrame(UIWorldEditorFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIWorldEditorFrame.getInstance());
            }
            else {
                final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo(MapManagerHelper.getWorldId());
                if (info != null && info.isHavenWorld()) {
                    final HavenWorld world = HavenWorldFactory.create(HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(info.m_worldId), this.createGuildInfo());
                    UIWorldEditorFrame.getInstance().setWorld(world);
                    WakfuGameEntity.getInstance().pushFrame(UIWorldEditorFrame.getInstance());
                }
                else {
                    manager.trace("You must be in a havenworld to open worldEditor.");
                }
            }
        }
    }
    
    private GuildInfo createGuildInfo() {
        final ClientGuildInformationHandler guild = WakfuGameEntity.getInstance().getLocalPlayer().getGuildHandler();
        return GuildInfo.create(guild.getGuildId(), guild.getName(), guild.getBlazon());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldEditorCommand.class);
    }
}
