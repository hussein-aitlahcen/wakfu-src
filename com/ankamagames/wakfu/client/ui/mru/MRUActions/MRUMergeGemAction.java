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

public class MRUMergeGemAction extends AbstractMRUAction implements MobileEndPathListener
{
    private static final String POWDER_TRANSLATION_KEY = "mergePowder";
    private static final String GEM_TRANSLATION_KEY = "mergeGem";
    private int m_gfxId;
    private boolean m_powder;
    
    public MRUMergeGemAction() {
        super();
        this.setPowder(true);
    }
    
    public MRUMergeGemAction(final boolean powder) {
        super();
        this.setPowder(powder);
    }
    
    public void setPowder(final boolean powder) {
        this.m_powder = powder;
        this.m_gfxId = (this.m_powder ? MRUGfxConstants.GEM.m_id : MRUGfxConstants.GEM_MERGE.m_id);
    }
    
    @Override
    public MRUMergeGemAction getCopy() {
        return new MRUMergeGemAction(this.m_powder);
    }
    
    @Override
    public String getTranslatorKey() {
        return this.m_powder ? "mergePowder" : "mergeGem";
    }
    
    @Override
    public boolean isRunnable() {
        return !WakfuGameEntity.getInstance().getLocalPlayer().isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUMergeGemAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
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
                this.loadMergeGemFrame();
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
        return MRUActions.MERGE_GEM;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeEndPositionListener(this);
        this.loadMergeGemFrame();
    }
    
    @Override
    protected int getGFXId() {
        return this.m_gfxId;
    }
    
    protected void loadMergeGemFrame() {
        final CharacterActor localActor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        final WakfuClientMapInteractiveElement interactiveElement = (WakfuClientMapInteractiveElement)this.m_source;
        if (interactiveElement.hasMobileInRange(localActor)) {
            UIMergeGemFrame.getInstance().setInteractiveElement(interactiveElement);
            if (WakfuGameEntity.getInstance().hasFrame(UIMergeGemFrame.getInstance())) {
                UIMergeGemFrame.getInstance().setPowderMerge(this.m_powder);
                UIMergeGemFrame.getInstance().reinitialize();
            }
            else {
                UIMergeGemFrame.getInstance().setPowderMerge(this.m_powder);
                WakfuGameEntity.getInstance().pushFrame(UIMergeGemFrame.getInstance());
            }
            final Direction8 direction = localActor.getWorldCoordinates().getDirectionTo(interactiveElement.getWorldCellX(), interactiveElement.getWorldCellY(), interactiveElement.getWorldCellAltitude());
            if (direction != null) {
                localActor.setDirectionWithNotification(direction);
            }
        }
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
}
