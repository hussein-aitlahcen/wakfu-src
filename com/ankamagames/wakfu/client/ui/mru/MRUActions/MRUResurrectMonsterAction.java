package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.game.world.action.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUResurrectMonsterAction extends AbstractMRUAction implements MobileEndPathListener
{
    private MonsterResurrectionType m_resurrectionType;
    
    public MRUResurrectMonsterAction(final MonsterResurrectionType resurrectionType) {
        super();
        this.m_resurrectionType = resurrectionType;
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.RESURRECT_MONSTER_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        this.resurrectMonster();
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof NonPlayerCharacter)) {
            MRUResurrectMonsterAction.m_logger.error((Object)("[RESURRECTION] On essaye de rez un " + this.m_source.getClass().getSimpleName()));
            return false;
        }
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        if (npc.getTypeId() == 132) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return npc.isActiveProperty(WorldPropertyType.DEAD) && !npc.isOnFight() && !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    private void resurrectMonster() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor actor = localPlayer.getActor();
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        final int distance = actor.getWorldCoordinates().getDistance(npc.getPosition());
        if (distance <= 1) {
            final Direction8 direction = actor.getWorldCoordinates().getDirectionTo(npc.getPosition());
            if (direction != null) {
                actor.setDirection(direction);
            }
            final ActorResurrectMonsterRequestMessage message = new ActorResurrectMonsterRequestMessage(npc.getId(), this.m_resurrectionType);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
        else {
            actor.addEndPositionListener(this);
            if (!localPlayer.moveTo(npc.getPosition(), true, false)) {
                actor.removeEndPositionListener(this);
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
            }
        }
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.resurrectMonster();
    }
    
    @Override
    public String getTranslatorKey() {
        switch (this.m_resurrectionType) {
            case NORMAL_RESURRECTION: {
                return "resurrect";
            }
            case PEST_RESURRECTION: {
                return "resurrectPest";
            }
            default: {
                return "unknown resurrection type";
            }
        }
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUResurrectMonsterAction(this.m_resurrectionType);
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.HAND_BOTH.m_id;
    }
}
