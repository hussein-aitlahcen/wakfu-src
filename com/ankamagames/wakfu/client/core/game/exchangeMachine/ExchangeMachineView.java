package com.ankamagames.wakfu.client.core.game.exchangeMachine;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.constants.*;
import gnu.trove.*;

public class ExchangeMachineView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String CONTENT_LIST = "contentList";
    public static final String USABLE = "usable";
    private static final String[] FIELDS;
    private final long m_machineId;
    private final String m_name;
    private final boolean m_usable;
    private final ArrayList<ExchangeEntryView> m_exchangeEntryViews;
    private final QuestInventoryListener m_questInventoryListener;
    private final InventoryObserver m_bagObserver;
    
    public ExchangeMachineView(final long exchangeMachineId, final String name, final boolean usable, final IEExchangeParameter param) {
        super();
        this.m_exchangeEntryViews = new ArrayList<ExchangeEntryView>();
        this.m_questInventoryListener = new QuestListener();
        this.m_bagObserver = new BagListener();
        this.m_machineId = exchangeMachineId;
        this.m_name = name;
        this.m_usable = usable;
        param.forEachExchange(new TObjectProcedure<IEExchangeParameter.Exchange>() {
            @Override
            public boolean execute(final IEExchangeParameter.Exchange object) {
                ExchangeMachineView.this.m_exchangeEntryViews.add(new ExchangeEntryView(object));
                return true;
            }
        });
        switch (param.getSort()) {
            case BY_AGT_ORDER: {
                Collections.sort(this.m_exchangeEntryViews, ExchangeEntryComparator.AGT_ORDER);
                break;
            }
            default: {
                Collections.sort(this.m_exchangeEntryViews, ExchangeEntryComparator.BY_COST);
                break;
            }
        }
    }
    
    public void addInventoryListeners() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final QuestInventory inventory = (QuestInventory)localPlayer.getInventory(InventoryType.QUEST);
        inventory.addListener(this.m_questInventoryListener);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag bag = it.value();
            bag.addObserver(this.m_bagObserver);
        }
    }
    
    public void removeInventoryListeners() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final QuestInventory inventory = (QuestInventory)localPlayer.getInventory(InventoryType.QUEST);
        inventory.removeListener(this.m_questInventoryListener);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag bag = it.value();
            bag.removeObserver(this.m_bagObserver);
        }
    }
    
    @Override
    public String[] getFields() {
        return ExchangeMachineView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("contentList")) {
            return this.m_exchangeEntryViews;
        }
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("usable")) {
            return this.m_usable;
        }
        return null;
    }
    
    public long getMachineId() {
        return this.m_machineId;
    }
    
    static {
        FIELDS = new String[] { "name", "contentList", "usable" };
    }
    
    private class QuestListener implements QuestInventoryListener
    {
        @Override
        public void itemAdded(final QuestItem item) {
            PropertiesProvider.getInstance().firePropertyValueChanged(ExchangeMachineView.this, ExchangeMachineView.FIELDS);
        }
        
        @Override
        public void itemRemoved(final QuestItem item) {
            PropertiesProvider.getInstance().firePropertyValueChanged(ExchangeMachineView.this, ExchangeMachineView.FIELDS);
        }
        
        @Override
        public void itemQuantityChanged(final QuestItem item, final int delta) {
            PropertiesProvider.getInstance().firePropertyValueChanged(ExchangeMachineView.this, ExchangeMachineView.FIELDS);
        }
    }
    
    private class BagListener implements InventoryObserver
    {
        @Override
        public void onInventoryEvent(final InventoryEvent event) {
            PropertiesProvider.getInstance().firePropertyValueChanged(ExchangeMachineView.this, ExchangeMachineView.FIELDS);
        }
    }
}
