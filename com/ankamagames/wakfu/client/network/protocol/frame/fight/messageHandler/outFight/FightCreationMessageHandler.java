package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.handler.*;
import com.ankamagames.wakfu.client.core.game.fight.join.*;
import com.ankamagames.wakfu.client.core.game.fight.handler.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import gnu.trove.*;

final class FightCreationMessageHandler extends UsingFightMessageHandler<FightCreationMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final FightCreationMessage msg) {
        boolean concernLocalPlayer = false;
        for (int i = 0; i < msg.getFightersCount(); ++i) {
            if (WakfuGameEntity.getInstance().getLocalPlayer().getId() == msg.getFighterId(i)) {
                concernLocalPlayer = true;
                break;
            }
        }
        if (concernLocalPlayer) {
            if (NetInFightManagementFrame.getInstance().getAssociatedFight() != null) {
                FightActionGroupManager.getInstance().executeAllActionInPendingGroup(NetInFightManagementFrame.getInstance().getAssociatedFight().getId());
            }
            final TIntObjectIterator<FightInfo> it = FightManager.getInstance().getFightsIterator();
            while (it.hasNext()) {
                it.advance();
                final FightInfo fightinf = it.value();
                if (fightinf instanceof ExternalFightInfo) {
                    ((ExternalFightInfo)fightinf).cleanExternalFightAllFighterDisplay();
                }
            }
            FightManager.getInstance().destroyFight(msg.getFightId());
            for (int j = 0; j < msg.getFightersCount(); ++j) {
                final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(msg.getFighterId(j));
                fighter.setFight(msg.getFightId());
            }
            final FightMap fightMap = new WakfuClientFightMap();
            new FightMapSerializer(fightMap).unserialize(ByteBuffer.wrap(msg.getSerializedMap()));
            final Fight fight = FightManager.getInstance().createFight(msg.getFightType(), msg.getFightId(), fightMap, msg.getLockedTeams(), msg.getSerializedEffectArea());
            fight.registerLocalFightHandler(RunnableFightListener.INSTANCE);
            RunnableFightListener.INSTANCE.addEventListener(FightEventType.FIGHTER_JOIN_FIGHT, new SummonForceCharacDisplayFightRunnable());
            NetInFightManagementFrame.getInstance().associateFight(fight);
            return NetInFightManagementFrame.getInstance().onMessage(msg);
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightCreationMessageHandler.class);
    }
}
