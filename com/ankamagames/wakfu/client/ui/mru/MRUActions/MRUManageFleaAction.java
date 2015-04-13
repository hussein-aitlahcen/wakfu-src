package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUManageFleaAction extends AbstractMRUAction implements MobileEndPathListener
{
    @Override
    public MRUActions tag() {
        return MRUActions.MANAGE_FLEA_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUManageFleaAction();
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
            this.startManaging();
        }
        else {
            localActor.addEndPositionListener(this);
            MRUManageFleaAction.m_logger.info((Object)("Serching path to " + merchantDisplay.getPosition()));
            localPlayer.applyPathResult(UIWorldInteractionFrame.getPathForModel(localActor, merchantDisplay), true);
        }
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof MerchantDisplay)) {
            return false;
        }
        final MerchantDisplay source = (MerchantDisplay)this.m_source;
        final LocalPlayerCharacter localplayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (source.getOwnerId() != localplayer.getId()) {
            return false;
        }
        if (localplayer.isWaitingForResult() || localplayer.isOnFight() || ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        final AbstractOccupation currentOccupation = localplayer.getCurrentOccupation();
        if (currentOccupation != null && currentOccupation instanceof ManageFleaOccupation) {
            final ManageFleaOccupation manageOccupation = (ManageFleaOccupation)currentOccupation;
            if (manageOccupation.getMerchantDisplay() == this.m_source) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        return "manageFlea";
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.startManaging();
    }
    
    private void startManaging() {
        final ManageFleaOccupation occupation = new ManageFleaOccupation((MerchantDisplay)this.m_source);
        if (occupation.isAllowed()) {
            occupation.begin();
        }
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.BAG.m_id;
    }
}
