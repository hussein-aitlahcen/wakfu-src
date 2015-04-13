package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.krosmoz.collection.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.krosmoz.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class KrosmozGameCollection extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEKrosmozGameCollectionParameter m_boardParameters;
    
    @Override
    public boolean isUsable() {
        return SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE) && super.isUsable();
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE)) {
            return false;
        }
        if (WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return false;
        }
        if (action != InteractiveElementAction.ACTIVATE) {
            return false;
        }
        if (KrosmozCollectionView.INSTANCE.needsToBeFilled()) {
            final KrosmozFigureListRequestMessage request = new KrosmozFigureListRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
        }
        if (!WakfuGameEntity.getInstance().hasFrame(UIKrosmozGameCollectionFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIKrosmozGameCollectionFrame.getInstance());
        }
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE)) {
            return null;
        }
        if (WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return null;
        }
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE)) {
            return InteractiveElementAction.EMPTY_ACTIONS;
        }
        if (WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return InteractiveElementAction.EMPTY_ACTIONS;
        }
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE)) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        if (WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        return new AbstractMRUAction[] { MRUActions.INTERACTIF_ACTION.getMRUAction() };
    }
    
    @Override
    public byte getHeight() {
        return 4;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setBlockingLineOfSight(true);
        this.setOverHeadable(true);
        assert this.m_boardParameters == null;
    }
    
    @Override
    public boolean isBlockingMovements() {
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_boardParameters = null;
    }
    
    @Override
    public String getName() {
        if (this.m_boardParameters != null) {
            return WakfuTranslator.getInstance().getString(136, this.m_boardParameters.getId(), new Object[0]);
        }
        return "#ERROR#";
    }
    
    @Override
    public int getOverheadDelayPreference() {
        return 0;
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        this.m_state = 1;
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            KrosmozGameCollection.m_logger.error((Object)("[LD] La borne de krosmoz " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        int parameterId;
        try {
            parameterId = Integer.valueOf(params[0]);
        }
        catch (NumberFormatException e) {
            KrosmozGameCollection.m_logger.error((Object)("[LD] La borne de krosmoz " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui n'est pas du bon type (id attendu)"));
            return;
        }
        final IEKrosmozGameCollectionParameter param = (IEKrosmozGameCollectionParameter)IEParametersManager.INSTANCE.getParam(IETypes.KROSMOZ_GAME_COLLECTION, parameterId);
        if (param == null) {
            KrosmozGameCollection.m_logger.error((Object)("[LD] La borne de krosmoz " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_boardParameters = param;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        KrosmozGameCollection.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public void initializeActivationPattern() {
        super.initializeActivationPattern();
        this.m_hasToFinishOnIE = false;
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new KrosmozGameCollectionItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)KrosmozGameCollection.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            KrosmozGameCollection board;
            try {
                board = (KrosmozGameCollection)Factory.m_pool.borrowObject();
                board.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                KrosmozGameCollection.m_logger.error((Object)"Erreur lors de l'extraction d'un Board du pool", (Throwable)e);
                board = new KrosmozGameCollection();
            }
            return board;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<KrosmozGameCollection>() {
                @Override
                public KrosmozGameCollection makeObject() {
                    return new KrosmozGameCollection();
                }
            });
        }
    }
}
