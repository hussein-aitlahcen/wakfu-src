package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class HavenWorldTravelMachine extends TravelMachine
{
    private static final Logger m_logger;
    private IEZaapOnlyOutParameter m_params;
    private long m_guildId;
    private BinarSerialPart TRAVEL_COST_BINAR_PART;
    
    public HavenWorldTravelMachine() {
        super();
        this.TRAVEL_COST_BINAR_PART = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("On ne serialize pas ces infos dans le client");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final RawZaapOutTravelMachine data = new RawZaapOutTravelMachine();
                data.unserialize(buffer);
                HavenWorldTravelMachine.this.m_guildId = data.guildId;
            }
        };
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.TRAVEL_COST_BINAR_PART;
    }
    
    public int getTravelCost(final long guildId) {
        if (guildId == this.m_guildId) {
            return this.m_params.getGuildCost();
        }
        return this.m_params.getVisitorCost();
    }
    
    @Override
    public void initializeWithParameter() throws IllegalArgumentException {
        if (this.m_travelType == TravelType.ZAAP_OUT_ONLY) {
            (this.m_params = new IEZaapOnlyOutParameter(0, 957, ChaosInteractiveCategory.NO_CHAOS, 0, 50, 100)).addAction(MRUActions.ZAAP_OUT_ONLY.getActionId());
        }
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final TIntArrayList actions = this.m_params.getActions();
        final AbstractMRUAction[] res = new AbstractMRUAction[actions.size()];
        for (int i = 0, n = actions.size(); i < n; ++i) {
            final int actionId = actions.get(i);
            final MRUActions action = MRUActions.getById(actionId);
            if (action != null) {
                res[i] = action.getMRUAction();
            }
            else {
                HavenWorldTravelMachine.m_logger.error((Object)("Action d'id inconnu " + actionId));
            }
        }
        return res;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldTravelMachine.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        private final TravelType m_travelType;
        
        public Factory(final TravelType type) {
            super();
            this.m_travelType = type;
        }
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            HavenWorldTravelMachine travelMachine;
            try {
                travelMachine = (HavenWorldTravelMachine)Factory.m_pool.borrowObject();
                travelMachine.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                HavenWorldTravelMachine.m_logger.error((Object)"Erreur lors de l'extraction du pool", (Throwable)e);
                travelMachine = new HavenWorldTravelMachine();
            }
            travelMachine.m_travelType = this.m_travelType;
            return travelMachine;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<HavenWorldTravelMachine>() {
                @Override
                public HavenWorldTravelMachine makeObject() {
                    return new HavenWorldTravelMachine();
                }
            });
        }
    }
}
