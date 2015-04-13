package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.skill.*;

public class MRUMarketAction extends AbstractMRUAction implements MobileEndPathListener
{
    private ActionVisual m_actionVisual;
    
    @Override
    public MRUActions tag() {
        return MRUActions.MARKET_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUMarketAction();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final MarketBoard board = (MarketBoard)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Actor localActor = localPlayer.getActor();
        if (board.isInApproachPoint(localActor.getWorldCoordinates())) {
            this.startManaging();
            return;
        }
        localActor.addEndPositionListener(this);
        MRUMarketAction.m_logger.info((Object)("Searching path to " + board));
        localPlayer.applyPathResult(UIWorldInteractionFrame.getPathForModel(localActor, board), true);
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TextWidgetFormater twf = new TextWidgetFormater();
        twf.append(label);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
        if (currentOccupation != null && currentOccupation.getOccupationTypeId() != 14) {
            twf.newLine().openText().addColor(MRUMarketAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.localPlayerBusy")).closeText();
        }
        else if (!localPlayer.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_MARKET_PLACE)) {
            twf.newLine().openText().addColor(MRUMarketAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight")).closeText();
        }
        if (localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0) {
            twf.newLine().openText().addColor(MRUMarketAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNoNation")).closeText();
        }
        return twf.finishAndToString();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
        return (currentOccupation == null || currentOccupation.getOccupationTypeId() == 14) && localPlayer.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_MARKET_PLACE) && (localPlayer.getNationId() > 0 || localPlayer.getTravellingNationId() <= 0) && super.isEnabled();
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof MarketBoard)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isWaitingForResult() && !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public String getTranslatorKey() {
        return this.m_actionVisual.getMruLabelKey();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.startManaging();
    }
    
    private void startManaging() {
        final MarketOccupation occupation = new MarketOccupation((MarketBoard)this.m_source);
        if (occupation.isAllowed()) {
            occupation.begin();
        }
    }
    
    public void setVisual(final int visualId) {
        this.m_actionVisual = ActionVisualManager.getInstance().get(visualId);
    }
    
    @Override
    protected int getGFXId() {
        return this.m_actionVisual.getMruGfx();
    }
    
    @Override
    public String toString() {
        return "MRUMarketAction{m_actionVisual=" + this.m_actionVisual + '}';
    }
}
