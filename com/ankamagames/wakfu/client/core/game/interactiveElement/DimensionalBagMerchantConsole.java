package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.flea.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagMerchantConsole extends WakfuClientMapInteractiveElement implements FieldProvider
{
    protected static final Logger m_logger;
    public static final String TAX_PERCENTAGE = "taxPercentage";
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(true);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.dimensionalBagLogConsole");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return BinarSerialPart.EMPTY;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        switch (action) {
            case ACTIVATE: {
                DimensionalBagMerchantConsole.m_logger.info((Object)"Demande de r\u00e9cup\u00e9ration du log de transactions local");
                WakfuClientInstance.getInstance();
                final WakfuGameEntity entity = WakfuClientInstance.getGameEntity();
                final FetchTransactionLogRequestMessage logRequestMsg = new FetchTransactionLogRequestMessage();
                entity.getNetworkEntity().sendMessage(logRequestMsg);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean ownBag = player.getVisitingDimentionalBag() == player.getOwnedDimensionalBag();
        final AbstractMRUAction action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setEnabled(ownBag);
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("taxPercentage")) {
            return String.format("%.2f%%", 0.1f);
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagMerchantConsole.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DimensionalBagMerchantConsole console;
            try {
                console = (DimensionalBagMerchantConsole)Factory.m_pool.borrowObject();
                console.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagMerchantConsole.m_logger.error((Object)("Erreur lors de l'extraction d'un " + DimensionalBagMerchantConsole.class + " du pool"), (Throwable)e);
                console = new DimensionalBagMerchantConsole();
            }
            return console;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagMerchantConsole>() {
                @Override
                public DimensionalBagMerchantConsole makeObject() {
                    return new DimensionalBagMerchantConsole();
                }
            });
        }
    }
}
