package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;

public class ManageFleaOccupation extends AbstractOccupation implements MerchantDisplayOwner
{
    protected static final Logger m_logger;
    private final MerchantDisplay m_merchantDisplay;
    
    public ManageFleaOccupation(final MerchantDisplay merchantDisplay) {
        super();
        this.m_merchantDisplay = merchantDisplay;
    }
    
    @Override
    public MerchantDisplay getMerchantDisplay() {
        return this.m_merchantDisplay;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 7;
    }
    
    @Override
    public boolean isAllowed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Actor localActor = localPlayer.getActor();
        return (!this.m_merchantDisplay.hasToFinishOnIE() && this.m_merchantDisplay.isInApproachPoint(localActor.getWorldCoordinates())) || (this.m_merchantDisplay.hasToFinishOnIE() && localActor.getWorldCoordinates().equals(this.m_merchantDisplay.getWorldCellX(), this.m_merchantDisplay.getWorldCellY(), this.m_merchantDisplay.getWorldCellAltitude()));
    }
    
    @Override
    public void begin() {
        ManageFleaOccupation.m_logger.info((Object)("Lancement de l'occupation MANAGE_FLEA sur la vitrine uid=" + this.m_merchantDisplay.getMerchantInventoryUid()));
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        final DimensionalBagView bag = this.m_localPlayer.getOwnedDimensionalBag();
        UIAbstractBrowseFleaFrame.setBrowsingDimentionalBag(bag);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int dx = this.m_merchantDisplay.getWorldCellX() - localPlayer.getWorldCellX();
        final int dy = this.m_merchantDisplay.getWorldCellY() - localPlayer.getWorldCellY();
        localPlayer.getActor().setDirection(Direction8.getDirectionFromVector(dx, dy));
        this.m_merchantDisplay.fireAction(InteractiveElementAction.START_MANAGE, WakfuGameEntity.getInstance().getLocalPlayer());
        if (WakfuGameEntity.getInstance().hasFrame(UIManageFleaFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIManageFleaFrame.getInstance());
        }
        UIManageFleaFrame.getInstance().setMerchantDisplay(this.m_merchantDisplay);
        WakfuGameEntity.getInstance().pushFrame(UIManageFleaFrame.getInstance());
        this.m_localPlayer.setCurrentOccupation(this);
        EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.DIMENSIONNAL_FLEA_MANAGE, "dimensionalBagFleaDialog");
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        ManageFleaOccupation.m_logger.info((Object)("On arrete l'occupation MANAGE_FLEA sur la vitrine uid=" + this.m_merchantDisplay.getMerchantInventoryUid() + " (fromServer=" + fromServer + ", sendMessage=" + sendMessage + ')'));
        WakfuGameEntity.getInstance().removeFrame(UIManageFleaFrame.getInstance());
        UIAbstractBrowseFleaFrame.setBrowsingDimentionalBag(null);
        if (sendMessage) {
            this.m_merchantDisplay.fireAction(InteractiveElementAction.STOP_MANAGE, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.DIMENSIONAL_FLEA_CLOSE, null);
        return true;
    }
    
    @Override
    public boolean finish() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ManageFleaOccupation.class);
    }
}
