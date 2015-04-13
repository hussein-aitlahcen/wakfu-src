package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class BrowseFleaOccupation extends AbstractOccupation implements MerchantDisplayOwner
{
    protected static final Logger m_logger;
    private final DimensionalBagView m_browsedFlea;
    private final long m_merchantUidFiler;
    private final MerchantDisplay m_merchantDisplay;
    
    @Override
    public MerchantDisplay getMerchantDisplay() {
        return this.m_merchantDisplay;
    }
    
    public BrowseFleaOccupation(final DimensionalBagView browsedFlea, final MerchantDisplay merchantDisplay) {
        super();
        this.m_browsedFlea = browsedFlea;
        this.m_merchantUidFiler = merchantDisplay.getMerchantInventoryUid();
        this.m_merchantDisplay = merchantDisplay;
    }
    
    public DimensionalBagView getBrowsedFlea() {
        return this.m_browsedFlea;
    }
    
    public long getMerchantUidFiler() {
        return this.m_merchantUidFiler;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 5;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    @Override
    public void begin() {
        BrowseFleaOccupation.m_logger.info((Object)"Lancement de l'occupation BROWSE_FLEA");
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        UIAbstractBrowseFleaFrame.setBrowsingDimentionalBag(this.m_browsedFlea);
        if (WakfuGameEntity.getInstance().hasFrame(UIBrowseFleaFrame.getInstance())) {
            UIBrowseFleaFrame.getInstance().clean();
            UIBrowseFleaFrame.getInstance().init();
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIBrowseFleaFrame.getInstance());
        }
        final FleaBrowseRequestMessage message = new FleaBrowseRequestMessage();
        final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
        message.setFleaOwnerId(bag.getOwnerId());
        message.setMerchantInventoryUidFilter(this.m_merchantUidFiler);
        message.setStartBrowsing(true);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        this.m_localPlayer.setCurrentOccupation(this);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        BrowseFleaOccupation.m_logger.info((Object)("On arr\u00eate l'occupation BROWSE_FLEA (fromServer=" + fromServeur + ", sendMessage=" + sendMessage + ")"));
        WakfuGameEntity.getInstance().removeFrame(UIBrowseFleaFrame.getInstance());
        if (sendMessage) {
            final FleaBrowseRequestMessage message = new FleaBrowseRequestMessage();
            message.setFleaOwnerId(UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().getOwnerId());
            message.setMerchantInventoryUidFilter(this.m_merchantUidFiler);
            message.setStartBrowsing(false);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
        UIAbstractBrowseFleaFrame.setBrowsingDimentionalBag(null);
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BrowseFleaOccupation.class);
    }
}
