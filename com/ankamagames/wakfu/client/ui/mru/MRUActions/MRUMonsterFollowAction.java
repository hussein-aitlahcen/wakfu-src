package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUMonsterFollowAction extends AbstractMRUAction implements MobileEndPathListener
{
    private int m_eventId;
    private Point3 m_pathDestination;
    
    public MRUMonsterFollowAction() {
        super();
    }
    
    public MRUMonsterFollowAction(final int eventId) {
        super();
        this.m_eventId = eventId;
    }
    
    @Override
    public MRUMonsterFollowAction getCopy() {
        return new MRUMonsterFollowAction(this.m_eventId);
    }
    
    @Override
    public String getTranslatorKey() {
        return "followMonster";
    }
    
    @Override
    public boolean isRunnable() {
        return !WakfuGameEntity.getInstance().getLocalPlayer().isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUMonsterFollowAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final NonPlayerCharacter monster = (NonPlayerCharacter)this.m_source;
        this.m_pathDestination = monster.getPosition();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        localPlayer.getActor().addEndPositionListener(this);
        if (!WakfuGameEntity.getInstance().getLocalPlayer().moveTo(this.m_pathDestination, true, true)) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeEndPositionListener(this);
            if (Math.abs(localPlayer.getActor().getWorldCoordinates().getX() - this.m_pathDestination.getX()) <= 1 && Math.abs(localPlayer.getActor().getWorldCoordinates().getY() - this.m_pathDestination.getY()) <= 1) {
                final TriggerServerEvent netMessage = new TriggerServerEvent();
                netMessage.setEventId(this.m_eventId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
            }
        }
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.FOLLOW_MONSTER_ACTION;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        final TriggerServerEvent netMessage = new TriggerServerEvent();
        netMessage.setEventId(this.m_eventId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.VIEWFINDER.m_id;
    }
    
    public void setEventId(final int eventId) {
        this.m_eventId = eventId;
    }
}
