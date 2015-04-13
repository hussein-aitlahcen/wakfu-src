package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public final class DisassembleOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private static final Runnable SCHEDULER;
    private final TLongShortHashMap m_itemsToRecycle;
    private final long m_duration;
    private final long m_interactiveElementId;
    
    public DisassembleOccupation(final TLongShortHashMap itemsToRecycle, final long duration, final long interactiveElementId) {
        super();
        this.m_itemsToRecycle = itemsToRecycle;
        this.m_duration = duration;
        this.m_interactiveElementId = interactiveElementId;
    }
    
    @Override
    public boolean isAllowed() {
        if (this.m_localPlayer.isDead() || this.m_localPlayer.isOnFight()) {
            DisassembleOccupation.m_logger.warn((Object)("Le joueur " + this.m_localPlayer + " est mort ou en combat et ne peut utiliser de machine de craft"));
            return false;
        }
        return true;
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        ProcessScheduler.getInstance().remove(DisassembleOccupation.SCHEDULER);
        return this.finish();
    }
    
    @Override
    public void begin() {
        final Message recycleMessage = new ActorRecycleRequestMessage(this.m_interactiveElementId, this.m_itemsToRecycle);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(recycleMessage);
        ProcessScheduler.getInstance().schedule(DisassembleOccupation.SCHEDULER, this.m_duration, 1);
    }
    
    @Override
    public boolean finish() {
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)2);
        netMsg.setOccupationType((short)27);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        return true;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 27;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisassembleOccupation.class);
        SCHEDULER = new Runnable() {
            @Override
            public void run() {
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                ProcessScheduler.getInstance().remove(this);
                player.finishCurrentOccupation();
            }
        };
    }
}
