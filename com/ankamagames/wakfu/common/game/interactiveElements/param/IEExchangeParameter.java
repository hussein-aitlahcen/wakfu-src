package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import gnu.trove.*;

public class IEExchangeParameter extends IEParameter
{
    private final TIntObjectHashMap<Exchange> m_exchanges;
    private final ExchangeMachineSort m_sort;
    
    public IEExchangeParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorId, final byte sortTypeId, final int capacity) {
        super(paramId, visualId, chaosCategory, chaosCollectorId);
        this.m_sort = ExchangeMachineSort.getById(sortTypeId);
        this.m_exchanges = new TIntObjectHashMap<Exchange>(capacity, 1.0f);
    }
    
    public void add(final Exchange exchange) throws IllegalArgumentException {
        final int exchangeId = exchange.getId();
        if (this.m_exchanges.contains(exchangeId)) {
            throw new IllegalArgumentException("La d\u00e9finition de l'\u00e9change " + exchange + " est d\u00e9j\u00e0 enregistr\u00e9e");
        }
        this.m_exchanges.put(exchangeId, exchange);
    }
    
    public Exchange get(final int exchangeId) {
        return this.m_exchanges.get(exchangeId);
    }
    
    public boolean forEachExchange(final TObjectProcedure<Exchange> procedure) {
        return this.m_exchanges.forEachValue(procedure);
    }
    
    public ExchangeMachineSort getSort() {
        return this.m_sort;
    }
    
    @Override
    public String toString() {
        return "IEExchangeParameter{m_id=" + this.getId() + '}';
    }
    
    public static class Exchange
    {
        private final int m_id;
        private final int m_order;
        private final IntShortLightWeightMap m_consumableItems;
        private final int m_consumableKamas;
        private final int m_consumablePvpMoney;
        private final IntObjectLightWeightMap<Resulting> m_resultingItems;
        private final int m_resultingKamas;
        private final SimpleCriterion m_criterion;
        
        public Exchange(final int id, final int consumableKamas, final int resultingKamas, final SimpleCriterion criterion, final int consumablePvpMoney, final int order) {
            super();
            this.m_consumableItems = new IntShortLightWeightMap(1);
            this.m_resultingItems = new IntObjectLightWeightMap<Resulting>(1);
            this.m_id = id;
            this.m_consumableKamas = consumableKamas;
            this.m_resultingKamas = resultingKamas;
            this.m_criterion = criterion;
            this.m_consumablePvpMoney = consumablePvpMoney;
            this.m_order = order;
        }
        
        public void addConsumable(final int refId, final short qty) throws IllegalArgumentException {
            if (this.m_consumableItems.contains(refId)) {
                throw new IllegalArgumentException("Une d\u00e9finition de l'item " + refId + " existe d\u00e9j\u00e0");
            }
            this.m_consumableItems.put(refId, qty);
        }
        
        public void addResulting(final int refId, final short qty, final ItemBindType forcedBindType) throws IllegalArgumentException {
            if (this.m_resultingItems.contains(refId)) {
                throw new IllegalArgumentException("Une d\u00e9finition de l'item " + refId + " existe d\u00e9j\u00e0");
            }
            this.m_resultingItems.put(refId, new Resulting(refId, qty, forcedBindType));
        }
        
        public boolean forEachConsumable(final TIntShortProcedure procedure) {
            for (int i = 0; i < this.m_consumableItems.size(); ++i) {
                if (!procedure.execute(this.m_consumableItems.getQuickKey(i), this.m_consumableItems.getQuickValue(i))) {
                    return false;
                }
            }
            return true;
        }
        
        public int getConsumableKamas() {
            return this.m_consumableKamas;
        }
        
        public int getConsumablePvpMoney() {
            return this.m_consumablePvpMoney;
        }
        
        public boolean forEachResulting(final TObjectProcedure<Resulting> procedure) {
            for (int i = 0; i < this.m_resultingItems.size(); ++i) {
                if (!procedure.execute(this.m_resultingItems.getQuickValue(i))) {
                    return false;
                }
            }
            return true;
        }
        
        public int getResultingKamas() {
            return this.m_resultingKamas;
        }
        
        public SimpleCriterion getCriterion() {
            return this.m_criterion;
        }
        
        public int getId() {
            return this.m_id;
        }
        
        public int getOrder() {
            return this.m_order;
        }
        
        @Override
        public String toString() {
            return "Exchange{m_id=" + this.m_id + '}';
        }
        
        public static class Resulting
        {
            private int m_itemId;
            private short m_quantity;
            private ItemBindType m_forcedType;
            
            public Resulting(final int itemId, final short quantity, final ItemBindType forcedType) {
                super();
                this.m_itemId = itemId;
                this.m_quantity = quantity;
                this.m_forcedType = forcedType;
            }
            
            public int getItemId() {
                return this.m_itemId;
            }
            
            public short getQuantity() {
                return this.m_quantity;
            }
            
            public ItemBindType getForcedType() {
                return this.m_forcedType;
            }
        }
    }
}
