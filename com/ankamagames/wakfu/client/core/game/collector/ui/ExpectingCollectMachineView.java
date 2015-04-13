package com.ankamagames.wakfu.client.core.game.collector.ui;

import com.ankamagames.wakfu.client.core.game.collector.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.limited.*;
import gnu.trove.*;

public class ExpectingCollectMachineView extends AbstractCollectorView
{
    private ArrayList<ExpectedCollectorItemView> m_expectedItemViews;
    
    public ExpectingCollectMachineView(final CollectorOccupationProvider Collector) {
        super(Collector);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("content")) {
            return this.getContent();
        }
        if (fieldName.equals("kama")) {
            if (this.m_kamaContentView == null) {
                this.m_kamaContentView = new ExpectedCollectorKamaView();
            }
            return this.m_kamaContentView;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    protected boolean isValid() {
        if (this.m_kamaContentView.getCurrentPlayerQuantity() > 0) {
            return true;
        }
        for (final ExpectedCollectorItemView expectedCollectorItemView : this.m_expectedItemViews) {
            if (expectedCollectorItemView.getCurrentPlayerQuantity() > 0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void validForm() {
        final TIntIntHashMap items = new TIntIntHashMap();
        for (final ExpectedCollectorItemView expectedCollectorItemView : this.m_expectedItemViews) {
            items.put(expectedCollectorItemView.getRefId(), expectedCollectorItemView.getCurrentPlayerQuantity());
        }
        ((BrowseCollectorOccupation)WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation()).sendCollectorInventoryModificationRequest(items, this.m_kamaContentView.getCurrentPlayerQuantity());
    }
    
    @Override
    public int getKamaQuantity() {
        return this.m_kamaContentView.getCurrentPlayerQuantity();
    }
    
    private ArrayList<ExpectedCollectorItemView> getContent() {
        if (this.m_expectedItemViews == null) {
            this.m_expectedItemViews = new ArrayList<ExpectedCollectorItemView>();
            final TIntIntIterator it = this.getCollector().getInfo().iterator();
            while (it.hasNext()) {
                it.advance();
                final int refId = it.key();
                this.m_expectedItemViews.add(new ExpectedCollectorItemView(refId));
            }
        }
        return this.m_expectedItemViews;
    }
    
    public class ExpectedCollectorKamaView extends AbstractExpectedCollectorContentView
    {
        @Override
        public Object getFieldValue(final String fieldName) {
            return super.getFieldValue(fieldName);
        }
        
        @Override
        public int getTotalPlayerQuantity() {
            return WakfuGameEntity.getInstance().getLocalPlayer().getKamasCount();
        }
        
        @Override
        public int getMaxQuantity() {
            return ExpectingCollectMachineView.this.getCollector().getInfo().getCashQty();
        }
        
        @Override
        public int getCurrentQuantity() {
            return ExpectingCollectMachineView.this.getCollector().getInventory().getAmountOfCash();
        }
    }
    
    public class ExpectedCollectorItemView extends AbstractExpectedCollectorContentView
    {
        private int m_refId;
        
        private ExpectedCollectorItemView(final int refId) {
            super();
            this.m_refId = refId;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            final Object value = super.getFieldValue(fieldName);
            if (value != null) {
                return value;
            }
            return ReferenceItemManager.getInstance().getReferenceItem(this.m_refId).getFieldValue(fieldName);
        }
        
        @Override
        public int getMaxPlayerQuantity() {
            return Math.min(this.getNeededQuantity(), this.getTotalPlayerQuantity());
        }
        
        @Override
        public int getTotalPlayerQuantity() {
            return WakfuGameEntity.getInstance().getLocalPlayer().getBags().getItemQuantity(this.m_refId);
        }
        
        @Override
        public int getMaxQuantity() {
            final IECollectorParameter info = ExpectingCollectMachineView.this.getCollector().getInfo();
            return (info == null) ? 0 : info.getQty(this.m_refId);
        }
        
        @Override
        public int getCurrentQuantity() {
            final ClientCollectorInventoryLimited inventory = (ClientCollectorInventoryLimited)ExpectingCollectMachineView.this.getCollector().getInventory();
            return (inventory == null) ? 0 : inventory.getQuantity(this.m_refId);
        }
        
        public int getRefId() {
            return this.m_refId;
        }
    }
}
