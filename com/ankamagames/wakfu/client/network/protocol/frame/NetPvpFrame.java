package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pvp.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.pvp.*;
import com.google.common.collect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.pvp.fight.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp.*;

public class NetPvpFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final NetPvpFrame INSTANCE;
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 20400: {
                final PvpRankUpdateMessage msg = (PvpRankUpdateMessage)message;
                final StringBuilder sb = new StringBuilder();
                sb.append("[PVP] Score : ").append(msg.getScore()).append(", Classement : ").append(msg.getRanking()).append(" (").append(msg.getRank().name()).append(")");
                ChatHelper.pushInformationMessage(sb.toString(), new Object[0]);
                return false;
            }
            case 20402: {
                final PvpFightReportMessage msg2 = (PvpFightReportMessage)message;
                final PvpFightReport report = msg2.getReport();
                FightHistoryFieldProvider.INSTANCE.setPvpReport(report);
                this.requestPvpLadderEntryUpdate();
                return false;
            }
            case 20404: {
                final NationPvpLadderPageResponse msg3 = (NationPvpLadderPageResponse)message;
                PvpLadderPageView.INSTANCE.setEntries(msg3.getPageNum(), (int)Math.ceil(msg3.getCount() / 10.0), msg3.getEntries());
                return false;
            }
            case 20406: {
                final NationPvpLadderEntryResponse msg4 = (NationPvpLadderEntryResponse)message;
                if (msg4.getEntry() != null) {
                    PvpLadderEntryView.getOrCreate(msg4.getEntry(), false);
                }
                return false;
            }
            case 20412: {
                final NationPvpLadderEntryRefresh msg5 = (NationPvpLadderEntryRefresh)message;
                if (msg5.getEntry() != null) {
                    PvpLadderEntryView.getOrCreate(msg5.getEntry(), true);
                }
                return false;
            }
            case 20408: {
                final NationPvpLadderEntryByNameResponse msg6 = (NationPvpLadderEntryByNameResponse)message;
                final ImmutableList<PvpLadderEntry> list = (ImmutableList<PvpLadderEntry>)((msg6.getEntry() != null) ? ImmutableList.of((Object)msg6.getEntry()) : ImmutableList.of());
                PvpLadderPageView.INSTANCE.setEntries(0, 1, (List<PvpLadderEntry>)list);
                return false;
            }
            default: {
                return true;
            }
        }
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
    
    public void requestPvpLadderEntryUpdate() {
        final Message msg = new NationPvpLadderEntryByIdRequest(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetPvpFrame.class);
        INSTANCE = new NetPvpFrame();
    }
}
