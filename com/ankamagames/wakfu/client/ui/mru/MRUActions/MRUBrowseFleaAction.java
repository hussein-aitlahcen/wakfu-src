package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUBrowseFleaAction extends AbstractMRUAction implements MobileEndPathListener
{
    @Override
    public MRUActions tag() {
        return MRUActions.BROWSE_FLEA_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final MerchantDisplay merchantDisplay = (MerchantDisplay)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Actor localActor = localPlayer.getActor();
        if ((!merchantDisplay.hasToFinishOnIE() && merchantDisplay.isInApproachPoint(localActor.getWorldCoordinates())) || (merchantDisplay.hasToFinishOnIE() && localActor.getWorldCoordinates().equals(merchantDisplay.getWorldCellX(), merchantDisplay.getWorldCellY(), merchantDisplay.getWorldCellAltitude()))) {
            this.startBrowsing();
        }
        else {
            final PathFindResult path = UIWorldInteractionFrame.getPathForModel(localActor, merchantDisplay);
            if (path.isPathFound()) {
                localActor.addEndPositionListener(this);
                localPlayer.applyPathResult(path, true);
            }
            else {
                MRUBrowseFleaAction.m_logger.warn((Object)("Impossible de trouver un chemin jusqu'\u00e0 la vitrine " + this.m_source));
            }
        }
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof MerchantDisplay)) {
            return false;
        }
        final LocalPlayerCharacter localplayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (ClientTradeHelper.INSTANCE.isTradeRunning() || localplayer.isWaitingForResult()) {
            return false;
        }
        final MerchantDisplay merchantDisplay = (MerchantDisplay)this.m_source;
        if (merchantDisplay.getOwnerId() == localplayer.getId()) {
            return false;
        }
        this.setEnabled(true);
        final DimensionalBagView visitingBag = localplayer.getVisitingDimentionalBag();
        if (visitingBag != null && !visitingBag.canPlayerInteractWithContentInRoom(localplayer, merchantDisplay.getWorldCellX(), merchantDisplay.getWorldCellY())) {
            this.setEnabled(false);
        }
        final AbstractOccupation currentOccupation = localplayer.getCurrentOccupation();
        if (currentOccupation != null && currentOccupation instanceof BrowseFleaOccupation) {
            final BrowseFleaOccupation browseOccupation = (BrowseFleaOccupation)currentOccupation;
            if (browseOccupation.getMerchantUidFiler() == merchantDisplay.getMerchantInventoryUid()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        return "browseFlea";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUBrowseFleaAction();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.startBrowsing();
    }
    
    private void startBrowsing() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final MerchantDisplay merchantDisplay = (MerchantDisplay)this.m_source;
        final Actor localActor = localPlayer.getActor();
        if (merchantDisplay.hasToFinishOnIE()) {
            if (!localActor.getWorldCoordinates().equals(merchantDisplay.getWorldCellX(), merchantDisplay.getWorldCellY(), merchantDisplay.getWorldCellAltitude())) {
                return;
            }
        }
        else if (!merchantDisplay.isInApproachPoint(localActor.getWorldCoordinates())) {
            return;
        }
        final BrowseFleaOccupation occupation = new BrowseFleaOccupation(localPlayer.getVisitingDimentionalBag(), merchantDisplay);
        if (occupation.isAllowed()) {
            final int dx = merchantDisplay.getWorldCellX() - localActor.getWorldCellX();
            final int dy = merchantDisplay.getWorldCellY() - localActor.getWorldCellY();
            final Direction8 towardFlea = Direction8.getDirectionFromVector(dx, dy);
            if (localActor.getDirection() != towardFlea) {
                localActor.setDirection(towardFlea);
            }
            occupation.begin();
            UIBrowseFleaFrame.getInstance().addDestructionListener(this.m_source, occupation);
        }
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FLEA.m_id;
    }
}
