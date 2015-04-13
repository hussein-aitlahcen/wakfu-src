package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class MRURecycleAction extends AbstractMRUAction implements MobileEndPathListener
{
    private int m_gfxId;
    private static final String TRANSLATION_KEY = "recycle";
    
    public MRURecycleAction() {
        super();
        this.m_gfxId = MRUGfxConstants.HAMMER_MAGIC.m_id;
    }
    
    @Override
    public MRURecycleAction getCopy() {
        return new MRURecycleAction();
    }
    
    @Override
    public String getTranslatorKey() {
        return "recycle";
    }
    
    @Override
    public boolean isRunnable() {
        return !WakfuGameEntity.getInstance().getLocalPlayer().isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRURecycleAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final ClientMapInteractiveElement element = (ClientMapInteractiveElement)this.m_source;
        final List<Point3> destinations = element.getApproachPoints();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        localPlayer.getActor().addEndPositionListener(this);
        if (!WakfuGameEntity.getInstance().getLocalPlayer().moveTo(false, true, destinations)) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeEndPositionListener(this);
            if (Math.abs(localPlayer.getActor().getWorldCoordinates().getX() - element.getWorldCellX()) <= 1 && Math.abs(localPlayer.getActor().getWorldCoordinates().getY() - element.getWorldCellY()) <= 1) {
                this.loadRecycleFrame();
            }
            else {
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("too.far.to.interact"));
                chatMessage.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage);
            }
        }
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.RECYCLE;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeEndPositionListener(this);
        this.loadRecycleFrame();
    }
    
    @Override
    protected int getGFXId() {
        return this.m_gfxId;
    }
    
    protected void loadRecycleFrame() {
        final CharacterActor localActor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        final WakfuClientMapInteractiveElement interactiveElement = (WakfuClientMapInteractiveElement)this.m_source;
        if (interactiveElement.hasMobileInRange(localActor)) {
            UIRecycleFrame.getInstance().setInteractiveElement(interactiveElement);
            if (WakfuGameEntity.getInstance().getFrames().contains(UIRecycleFrame.getInstance())) {
                UIRecycleFrame.getInstance().reinitialize();
            }
            else {
                WakfuGameEntity.getInstance().pushFrame(UIRecycleFrame.getInstance());
            }
            final Direction8 direction = localActor.getWorldCoordinates().getDirectionTo(interactiveElement.getWorldCellX(), interactiveElement.getWorldCellY(), interactiveElement.getWorldCellAltitude());
            if (direction != null) {
                localActor.setDirectionWithNotification(direction);
            }
            interactiveElement.setState((short)2);
            interactiveElement.notifyViews();
        }
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
}
