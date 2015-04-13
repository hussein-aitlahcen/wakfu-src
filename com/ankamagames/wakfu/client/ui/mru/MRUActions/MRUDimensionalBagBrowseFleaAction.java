package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUDimensionalBagBrowseFleaAction extends AbstractMRUAction implements MobileEndPathListener
{
    @Override
    public MRUActions tag() {
        return MRUActions.DIMENSIONAL_BAG_BROWSE_FLEA_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final DimensionalBagInteractiveElement dimBag = (DimensionalBagInteractiveElement)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Actor localActor = localPlayer.getActor();
        if (dimBag.isInApproachPoint(localActor.getWorldCoordinates())) {
            this.startBrowsing();
        }
        else {
            final PathFindResult path = UIWorldInteractionFrame.getPathForModel(localActor, dimBag);
            if (path.isPathFound()) {
                localActor.addEndPositionListener(this);
                localPlayer.applyPathResult(path, true);
            }
            else {
                MRUDimensionalBagBrowseFleaAction.m_logger.warn((Object)("Impossible de trouver un chemin jusqu'au sac " + this.m_source));
            }
        }
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof DimensionalBagInteractiveElement)) {
            return false;
        }
        final LocalPlayerCharacter localplayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !ClientTradeHelper.INSTANCE.isTradeRunning() && !localplayer.isWaitingForResult();
    }
    
    @Override
    public String getTranslatorKey() {
        return "browseDimBagFlea";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUDimensionalBagBrowseFleaAction();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.startBrowsing();
    }
    
    private void startBrowsing() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final DimensionalBagInteractiveElement dimBag = (DimensionalBagInteractiveElement)this.m_source;
        final Actor localActor = localPlayer.getActor();
        if (!dimBag.isInApproachPoint(localActor.getWorldCoordinates())) {
            return;
        }
        final DimensionalBagBrowseFleaOccupation occupation = new DimensionalBagBrowseFleaOccupation(dimBag);
        if (occupation.isAllowed()) {
            final int dx = dimBag.getWorldCellX() - localActor.getWorldCellX();
            final int dy = dimBag.getWorldCellY() - localActor.getWorldCellY();
            final Direction8 towardFlea = Direction8.getDirectionFromVector(dx, dy);
            if (localActor.getDirection() != towardFlea) {
                localActor.setDirection(towardFlea);
            }
            occupation.begin();
            UIBrowseDimensionalBagFleaFrame.getInstance().addDestructionListener(this.m_source, occupation);
        }
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FLEA.m_id;
    }
}
