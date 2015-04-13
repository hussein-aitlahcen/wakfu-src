package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class DimensionalBagBrowseFleaOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final DimensionalBagInteractiveElement m_browsedBag;
    
    public DimensionalBagBrowseFleaOccupation(final DimensionalBagInteractiveElement dimBag) {
        super();
        this.m_browsedBag = dimBag;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 18;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    @Override
    public void begin() {
        DimensionalBagBrowseFleaOccupation.m_logger.info((Object)"Lancement de l'occupation DIM_BAG_BROWSE_FLEA");
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        UIAbstractBrowseFleaFrame.setBrowsingDimentionalBag(this.m_browsedBag.getInfoProvider());
        if (WakfuGameEntity.getInstance().hasFrame(UIBrowseDimensionalBagFleaFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIBrowseDimensionalBagFleaFrame.getInstance());
        }
        WakfuGameEntity.getInstance().pushFrame(UIBrowseDimensionalBagFleaFrame.getInstance());
        final DimensionalBagAllFleasBrowseRequestMessage message = new DimensionalBagAllFleasBrowseRequestMessage();
        message.setBagIeId(this.m_browsedBag.getId());
        message.setStartBrowsing(true);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        this.m_localPlayer.setCurrentOccupation(this);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        DimensionalBagBrowseFleaOccupation.m_logger.info((Object)("On arr\u00eate l'occupation DIM_BAG_BROWSE_FLEA (fromServer=" + fromServeur + ", sendMessage=" + sendMessage + ")"));
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        if (WakfuGameEntity.getInstance().hasFrame(UIBrowseDimensionalBagFleaFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIBrowseDimensionalBagFleaFrame.getInstance());
        }
        final DimensionalBagAllFleasBrowseRequestMessage message = new DimensionalBagAllFleasBrowseRequestMessage();
        message.setBagIeId(this.m_browsedBag.getId());
        message.setStartBrowsing(false);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagBrowseFleaOccupation.class);
    }
}
