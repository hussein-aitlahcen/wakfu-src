package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildStorage.*;

public final class NetGuildStorageFrame extends MessageRunnerFrame
{
    private static final Logger m_logger;
    public static final NetGuildStorageFrame INSTANCE;
    
    private NetGuildStorageFrame() {
        super(new MessageRunner[] { new GuildStorageConsultResultRunner(), new GuildStorageCompartmentConsultResultRunner(), new GuildStorageMoneyResultRunner() });
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetGuildStorageFrame.class);
        INSTANCE = new NetGuildStorageFrame();
    }
    
    private static class GuildStorageConsultResultRunner implements MessageRunner<GuildStorageConsultResultMessage>
    {
        @Override
        public boolean run(final GuildStorageConsultResultMessage msg) {
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            final AbstractOccupation occupation = player.getCurrentOccupation();
            if (occupation.getOccupationTypeId() != 23) {
                NetGuildStorageFrame.m_logger.warn((Object)"[GUILD_STORAGE_BOX] Reception d'un message de taille dans un contexte ne correspondant pas");
                return false;
            }
            final GuildStorageHistory history = msg.getHistory();
            UIStorageBoxFrame.getInstance().setGuildStorageBox(msg.getCompartments(), history, msg.getMoney());
            NetGuildStorageFrame.m_logger.info((Object)"[GUILD_STORAGE_BOX]  Taille re\u00e7ue");
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 20076;
        }
    }
    
    private static class GuildStorageCompartmentConsultResultRunner implements MessageRunner<GuildStorageCompartmentConsultResultMessage>
    {
        @Override
        public boolean run(final GuildStorageCompartmentConsultResultMessage msg) {
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            final AbstractOccupation occupation = player.getCurrentOccupation();
            if (occupation == null || occupation.getOccupationTypeId() != 23) {
                NetGuildStorageFrame.m_logger.warn((Object)"[GUILD_STORAGE_BOX] Reception d'un message de contenu dans un contexte ne correspondant pas");
                return false;
            }
            final ArrayList<RawGuildStorageCompartment> compartments = msg.getCompartments();
            final RawGuildStorageHistory rawHistory = msg.getHistory();
            DragNDropManager.getInstance().cancel();
            UIStorageBoxFrame.getInstance().clearCompartmentContent();
            for (int i = 0, size = compartments.size(); i < size; ++i) {
                final BrowseGuildStorageBoxOccupation browseGuildStorageBoxOccupation = (BrowseGuildStorageBoxOccupation)occupation;
                final GuildStorageCompartment storageBoxCompartment = BrowseGuildStorageBoxOccupation.onContentMessage(compartments.get(i));
                UIStorageBoxFrame.getInstance().onCompartmentContent(storageBoxCompartment);
            }
            UIStorageBoxFrame.getInstance().onGuildStorageHistory(rawHistory);
            NetGuildStorageFrame.m_logger.info((Object)"[GUILD_STORAGE_BOX]  Contenu re\u00e7u");
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 20078;
        }
    }
    
    private static class GuildStorageMoneyResultRunner implements MessageRunner<GuildStorageMoneyResultMessage>
    {
        @Override
        public boolean run(final GuildStorageMoneyResultMessage msg) {
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            final AbstractOccupation occupation = player.getCurrentOccupation();
            if (occupation == null || occupation.getOccupationTypeId() != 23) {
                NetGuildStorageFrame.m_logger.warn((Object)"[GUILD_STORAGE_BOX] Reception d'un message de contenu dans un contexte ne correspondant pas");
                return false;
            }
            final int money = msg.getMoney();
            final RawGuildStorageHistory rawHistory = msg.getHistory();
            UIStorageBoxFrame.getInstance().onMoney(money);
            UIStorageBoxFrame.getInstance().onGuildStorageHistory(rawHistory);
            NetGuildStorageFrame.m_logger.info((Object)"[GUILD_STORAGE_BOX]  Contenu re\u00e7u");
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 20080;
        }
    }
}
