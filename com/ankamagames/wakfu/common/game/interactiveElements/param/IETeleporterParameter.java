package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;

public class IETeleporterParameter extends IEParameter
{
    public static final IETeleporterParameter FAKE_PARAM;
    private final DropTable<Exit> m_destinations;
    private final TShortHashSet m_instanceIds;
    private final int m_lockId;
    
    public IETeleporterParameter(final int paramId, final int lockId) {
        super(paramId, 0, ChaosInteractiveCategory.NO_CHAOS, 0);
        this.m_destinations = new DropTable<Exit>();
        this.m_instanceIds = new TShortHashSet();
        this.m_lockId = lockId;
    }
    
    @Override
    public int getVisualId() {
        throw new UnsupportedOperationException("On essaye de r\u00e9cup\u00e9rer un visuel qu'on ne doit pas r\u00e9cup\u00e9rer");
    }
    
    public int getLockId() {
        return this.m_lockId;
    }
    
    public void addDestination(final Exit exit) {
        this.m_destinations.addDrop(exit);
        this.m_instanceIds.add(exit.getWorldId());
    }
    
    public Exit dropDestination(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        return this.m_destinations.drop(dropUser, dropTarget, dropContent, dropContext);
    }
    
    public Exit getExit(final int id) {
        return this.m_destinations.getDrop(id);
    }
    
    public TIntObjectHashMap<Exit> getExits() {
        return this.m_destinations.getDrops();
    }
    
    public boolean hasDestinations() {
        return this.m_destinations.size() != 0;
    }
    
    public short[] getDestinationInstanceIds() {
        return this.m_instanceIds.toArray();
    }
    
    public boolean forEachDestination(final TObjectProcedure<Exit> procedure) {
        return this.m_destinations.getDrops().forEachValue(procedure);
    }
    
    @Override
    public String toString() {
        return "IETeleporterParameter{m_destinations=" + this.m_destinations + ", m_instanceIds=" + this.m_instanceIds + ", m_lockId=" + this.m_lockId + '}';
    }
    
    static {
        FAKE_PARAM = new IETeleporterParameter(0, 0);
    }
    
    public static class Exit implements Dropable
    {
        private final int m_id;
        private final int m_visualId;
        private final Direction8 m_direction;
        private final int[] m_destination;
        private final short m_worldId;
        private final SimpleCriterion m_criterion;
        private boolean m_hasLoading;
        private TravelLoadingInfo m_loading;
        private final int m_apsId;
        private final short m_delay;
        private final int m_consumedItemRefId;
        private final short m_consumedItemQty;
        private final boolean m_consumeItem;
        private final short m_consumedKamas;
        private final boolean m_isInvisibleIfFalse;
        
        public boolean isInvisibleIfFalse() {
            return this.m_isInvisibleIfFalse;
        }
        
        public boolean isHasLoading() {
            return this.m_hasLoading;
        }
        
        public int getVisualId() {
            return this.m_visualId;
        }
        
        public int getApsId() {
            return this.m_apsId;
        }
        
        public short getDelay() {
            return this.m_delay;
        }
        
        public int getConsumedItemRefId() {
            return this.m_consumedItemRefId;
        }
        
        public short getConsumedItemQty() {
            return this.m_consumedItemQty;
        }
        
        public boolean isConsumeItem() {
            return this.m_consumeItem;
        }
        
        public short getConsumedKamas() {
            return this.m_consumedKamas;
        }
        
        public Exit(final int id, final int visualId, final int x, final int y, final int z, final short worldId, final Direction8 direction, final SimpleCriterion criterion, final int apsId, final short delay, final int consumedItemRefId, final short consumedItemQty, final boolean consumeItem, final short consumedKamas, final boolean isInvisibleIfFalse) {
            super();
            this.m_id = id;
            this.m_visualId = visualId;
            this.m_direction = direction;
            this.m_destination = new int[] { x, y, z };
            this.m_worldId = worldId;
            this.m_criterion = criterion;
            this.m_apsId = apsId;
            this.m_delay = delay;
            this.m_consumedItemRefId = consumedItemRefId;
            this.m_consumedItemQty = consumedItemQty;
            this.m_consumeItem = consumeItem;
            this.m_consumedKamas = consumedKamas;
            this.m_isInvisibleIfFalse = isInvisibleIfFalse;
        }
        
        public int[] getDestination() {
            return this.m_destination;
        }
        
        public short getWorldId() {
            return this.m_worldId;
        }
        
        public Direction8 getDirection() {
            return this.m_direction;
        }
        
        @Override
        public int getId() {
            return this.m_id;
        }
        
        @Override
        public SimpleCriterion getCriterion() {
            return this.m_criterion;
        }
        
        @Override
        public short getDropWeight() {
            return 100;
        }
        
        public void setLoadingInfo(final TravelLoadingInfo loading) {
            this.m_hasLoading = true;
            this.m_loading = loading;
        }
        
        public void setHasLoading(final boolean hasLoading) {
            this.m_hasLoading = hasLoading;
        }
        
        public boolean hasLoading() {
            return this.m_hasLoading;
        }
        
        public TravelLoadingInfo getLoading() {
            return this.m_loading;
        }
    }
}
