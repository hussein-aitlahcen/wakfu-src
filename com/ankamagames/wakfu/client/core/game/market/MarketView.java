package com.ankamagames.wakfu.client.core.game.market;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.market.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class MarketView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String RESULTS_FIELD = "results";
    public static final String HISTORY_FIELD = "history";
    public static final String MONEY_STOCKED_FIELD = "moneyStocked";
    public static final String HAS_MONEY_STOCKED_FIELD = "hasMoneyStocked";
    public static final String HAS_UNSOLD_STOCKED_FIELD = "hasUnsoldStocked";
    public static final String REQUEST_SORT_TYPES_FIELD = "requestSortTypes";
    public static final String NUM_RESULTS_FIELD = "numResults";
    public static final String HAS_PREVIOUS_RESULTS_FIELD = "hasPreviousResults";
    public static final String HAS_NEXT_RESULTS_FIELD = "hasNextResults";
    public static final String CURRENT_SEARCH_CATEGORY1_FIELD = "currentSearchCategory1";
    public static final String CURRENT_SEARCH_CATEGORY2_FIELD = "currentSearchCategory2";
    public static final String CURRENT_SEARCH_CATEGORY3_FIELD = "currentSearchCategory3";
    public static final String MARKET_SELLS_FULL_TEXT_FIELD = "marketSellsFullText";
    public static final String ELEMENTS_SEARCH_FIELD = "elementsSearch";
    public static String[] FIELDS;
    public static final MarketView INSTANCE;
    private Market m_market;
    private ItemTypeFilterFieldProvider m_currentSearchItemType1;
    private ItemTypeFilterFieldProvider m_currentSearchItemType2;
    private ItemTypeFilterFieldProvider m_currentSearchItemType3;
    private TLongArrayList m_entriesIds;
    private TLongArrayList m_historyIds;
    private TLongArrayList m_unsoldIds;
    private int m_totalResultSize;
    private int m_outdatedResultSize;
    private static final ArrayList<ElementFilterView> m_elementReferences;
    
    private MarketView() {
        super();
        this.m_market = new Market(0);
        this.m_currentSearchItemType1 = new ItemTypeFilterFieldProvider(null, null);
        this.m_currentSearchItemType2 = null;
        this.m_currentSearchItemType3 = null;
        this.m_entriesIds = new TLongArrayList();
        this.m_historyIds = new TLongArrayList();
        this.m_unsoldIds = new TLongArrayList();
    }
    
    @Override
    public String[] getFields() {
        return MarketView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("results")) {
            if (this.m_market == null) {
                return null;
            }
            final ArrayList<MarketEntryView> items = new ArrayList<MarketEntryView>();
            for (int i = 0; i < this.m_entriesIds.size(); ++i) {
                final MarketEntry marketEntry = this.m_market.getEntry(this.m_entriesIds.get(i));
                if (ReferenceItemManager.getInstance().getReferenceItem(marketEntry.getItemRefId()) != null) {
                    final MarketEntryView marketEntryView = new MarketEntryView(marketEntry);
                    items.add(marketEntryView);
                }
            }
            return items;
        }
        else {
            if (fieldName.equals("name")) {
                final MarketBoard board = UIMarketFrame.getInstance().getBoard();
                return board.getMarketName();
            }
            if (fieldName.equals("hasMoneyStocked")) {
                return !this.m_historyIds.isEmpty();
            }
            if (fieldName.equals("hasUnsoldStocked")) {
                return !this.m_unsoldIds.isEmpty();
            }
            if (fieldName.equals("moneyStocked")) {
                if (this.m_market == null) {
                    return null;
                }
                long count = 0L;
                for (int j = 0; j < this.m_historyIds.size(); ++j) {
                    final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                    final MarketHistoryEntry marketEntry2 = this.m_market.getSaleEntry(localPlayer.getId(), this.m_historyIds.get(j));
                    count += marketEntry2.getTotalPrice();
                }
                return WakfuTranslator.getInstance().formatNumber(count);
            }
            else {
                if (!fieldName.equals("history")) {
                    if (!fieldName.equals("requestSortTypes")) {
                        if (fieldName.equals("numResults")) {
                            if (this.m_entriesIds == null || this.m_entriesIds.isEmpty()) {
                                return WakfuTranslator.getInstance().getString("marketBoard.noResults");
                            }
                            final short currentSearchIndex = UIMarketFrame.getInstance().getCurrentSearchIndex();
                            return WakfuTranslator.getInstance().getString("marketBoard.numResults", currentSearchIndex + 1, Math.min(currentSearchIndex + 10, this.m_totalResultSize), this.m_totalResultSize);
                        }
                        else {
                            if (fieldName.equals("hasNextResults")) {
                                return UIMarketFrame.getInstance().getCurrentSearchIndex() + 10 < this.m_totalResultSize;
                            }
                            if (fieldName.equals("hasPreviousResults")) {
                                return UIMarketFrame.getInstance().getCurrentSearchIndex() > 0;
                            }
                            if (fieldName.equals("currentSearchCategory1")) {
                                return this.m_currentSearchItemType1;
                            }
                            if (fieldName.equals("currentSearchCategory2")) {
                                return this.m_currentSearchItemType2;
                            }
                            if (fieldName.equals("currentSearchCategory3")) {
                                return this.m_currentSearchItemType3;
                            }
                            if (fieldName.equals("elementsSearch")) {
                                return MarketView.m_elementReferences;
                            }
                            if (fieldName.equals("marketSellsFullText")) {
                                return (this.m_outdatedResultSize + this.m_entriesIds.size() < 10) ? null : WakfuTranslator.getInstance().getString("desc.cantDropItemToSell", this.m_outdatedResultSize);
                            }
                        }
                    }
                    return null;
                }
                if (this.m_market == null) {
                    return null;
                }
                final TLongObjectHashMap<MarketHistoryEntryView> items2 = new TLongObjectHashMap<MarketHistoryEntryView>();
                for (int i = 0; i < this.m_historyIds.size(); ++i) {
                    final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
                    final long entryId = this.m_historyIds.get(i);
                    final MarketHistoryEntry marketHistoryEntry = this.m_market.getSaleEntry(localPlayer2.getId(), entryId);
                    if (ReferenceItemManager.getInstance().getReferenceItem(marketHistoryEntry.getItemRefId()) != null) {
                        final MarketHistoryEntryView marketEntryView2 = new MarketHistoryEntryView(marketHistoryEntry);
                        items2.put(entryId, marketEntryView2);
                    }
                }
                for (int i = 0; i < this.m_unsoldIds.size(); ++i) {
                    final long entryId2 = this.m_unsoldIds.get(i);
                    final MarketEntry marketEntry3 = this.m_market.getEntry(entryId2);
                    if (ReferenceItemManager.getInstance().getReferenceItem(marketEntry3.getItemRefId()) != null) {
                        MarketHistoryEntryView marketEntryView3 = items2.get(entryId2);
                        if (marketEntryView3 != null) {
                            marketEntryView3.setUnsoldQuantity(marketEntry3.getPackNumber());
                        }
                        else {
                            marketEntryView3 = new MarketHistoryEntryView(marketEntry3);
                        }
                        items2.put(entryId2, marketEntryView3);
                    }
                }
                return items2.getValues();
            }
        }
    }
    
    public Market getMarket() {
        return this.m_market;
    }
    
    public void updateResults() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "history", "results", "numResults", "hasNextResults", "hasPreviousResults", "hasMoneyStocked", "moneyStocked", "hasUnsoldStocked", "marketSellsFullText");
    }
    
    private void updateCategories() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentSearchCategory1");
        if (this.m_currentSearchItemType1 != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_currentSearchItemType1, this.m_currentSearchItemType1.getFields());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentSearchCategory2");
        if (this.m_currentSearchItemType2 != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_currentSearchItemType2, this.m_currentSearchItemType2.getFields());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentSearchCategory3");
        if (this.m_currentSearchItemType3 != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_currentSearchItemType3, this.m_currentSearchItemType3.getFields());
        }
    }
    
    public void setCurrentSearchItemType1(final ItemType currentSearchItemType) {
        if (this.m_currentSearchItemType1.getSelectedItemType() == currentSearchItemType) {
            return;
        }
        this.m_currentSearchItemType1 = new ItemTypeFilterFieldProvider(currentSearchItemType, null);
        this.m_currentSearchItemType2 = ((currentSearchItemType == null) ? null : new ItemTypeFilterFieldProvider(null, currentSearchItemType));
        this.m_currentSearchItemType3 = null;
        this.updateCategories();
    }
    
    public void setCurrentSearchItemType2(final ItemType currentSearchItemType) {
        if (this.m_currentSearchItemType2 == null || this.m_currentSearchItemType2.getSelectedItemType() == currentSearchItemType) {
            return;
        }
        this.m_currentSearchItemType2 = new ItemTypeFilterFieldProvider(currentSearchItemType, this.m_currentSearchItemType1.getSelectedItemType());
        this.m_currentSearchItemType3 = ((currentSearchItemType == null) ? null : new ItemTypeFilterFieldProvider(null, currentSearchItemType));
        this.updateCategories();
    }
    
    public void setCurrentSearchItemType3(final ItemType currentSearchItemType) {
        if (this.m_currentSearchItemType3 == null || this.m_currentSearchItemType3.getSelectedItemType() == currentSearchItemType) {
            return;
        }
        this.m_currentSearchItemType3 = new ItemTypeFilterFieldProvider(currentSearchItemType, this.m_currentSearchItemType2.getSelectedItemType());
        this.updateCategories();
    }
    
    public void clean() {
        this.clearAll();
    }
    
    public void cleanItemTypes() {
        this.m_currentSearchItemType1 = new ItemTypeFilterFieldProvider(null, null);
        this.m_currentSearchItemType2 = null;
        this.m_currentSearchItemType3 = null;
    }
    
    private void clearAll() {
        this.m_entriesIds.clear();
        this.m_historyIds.clear();
        this.m_unsoldIds.clear();
        this.m_market.clear();
    }
    
    public void setTotalResultSize(final int totalResultSize) {
        if (UIMarketFrame.getInstance().getCurrentPageIndex() == 0 && totalResultSize == 0) {
            PropertiesProvider.getInstance().setPropertyValue("marketBoard.SearchError", WakfuTranslator.getInstance().getString("marketBoard.noResult"));
            UIMarketFrame.getInstance().launchSearchErrorProcess();
        }
        this.m_totalResultSize = totalResultSize;
    }
    
    public int getTotalResultSize() {
        return this.m_totalResultSize;
    }
    
    public boolean onMarketEntryDecreased(final long entryId, final short qty) {
        final MarketEntry entry = this.m_market.getEntry(entryId);
        entry.decreasePackNumber(qty);
        if (entry.getPackNumber() == 0) {
            this.onMarketEntryRemoved(entryId);
            return true;
        }
        this.updateResults();
        return false;
    }
    
    public void onMarketUnsoldEntryRemoved(final long entryId) {
        TroveUtils.removeFirstValue(this.m_unsoldIds, entryId);
        this.m_market.removeEntry(entryId);
        this.updateResults();
    }
    
    public void onMarketEntryRemoved(final long entryId) {
        this.m_market.removeEntry(entryId);
        TroveUtils.removeFirstValue(this.m_entriesIds, entryId);
        this.updateResults();
    }
    
    private void addEntry(final MarketEntry marketEntry) {
        this.m_entriesIds.add(marketEntry.getId());
        this.m_market.addEntry(marketEntry);
    }
    
    public void setMarketEntries(final Iterator<MarketEntry> marketEntries) {
        this.clearAll();
        while (marketEntries.hasNext()) {
            this.addEntry(marketEntries.next());
        }
    }
    
    public void setHistoryEntries(final ArrayList<MarketHistoryEntry> marketHistoryEntries, final ArrayList<MarketEntry> marketEntries) {
        this.clearAll();
        for (final MarketHistoryEntry marketEntry : marketHistoryEntries) {
            this.m_historyIds.add(marketEntry.getId());
            this.m_market.addSaleEntry(WakfuGameEntity.getInstance().getLocalPlayer().getId(), marketEntry);
        }
        for (final MarketEntry marketEntry2 : marketEntries) {
            this.m_unsoldIds.add(marketEntry2.getId());
            this.m_market.addEntry(marketEntry2);
        }
    }
    
    public void clearEntries() {
        this.m_entriesIds.clear();
    }
    
    public void clearSoldEntries() {
        this.m_historyIds.clear();
    }
    
    public void clearUnsoldEntries() {
        this.m_unsoldIds.clear();
    }
    
    public ItemType getCurrentSearchItemType() {
        if (this.m_currentSearchItemType1.getSelectedItemType() == null) {
            return null;
        }
        if (this.m_currentSearchItemType2 == null || this.m_currentSearchItemType2.getSelectedItemType() == null) {
            return this.m_currentSearchItemType1.getSelectedItemType();
        }
        if (this.m_currentSearchItemType3 == null || this.m_currentSearchItemType3.getSelectedItemType() == null) {
            return this.m_currentSearchItemType2.getSelectedItemType();
        }
        return this.m_currentSearchItemType3.getSelectedItemType();
    }
    
    public void setOutdatedResultSize(final int outdatedResultSize) {
        this.m_outdatedResultSize = outdatedResultSize;
    }
    
    public byte getElementMask() {
        byte elemMask = 0;
        for (final ElementFilterView elementFilterView : MarketView.m_elementReferences) {
            if (elementFilterView.isSelected()) {
                elemMask |= (byte)(1 << elementFilterView.getElementReference().getElement().getId());
            }
        }
        return elemMask;
    }
    
    public void clearSelectedElements() {
        for (final ElementFilterView elementFilterView : MarketView.m_elementReferences) {
            elementFilterView.setSelected(false);
        }
    }
    
    static {
        MarketView.FIELDS = new String[] { "name", "results", "history", "moneyStocked", "hasMoneyStocked", "requestSortTypes", "numResults", "hasPreviousResults", "hasNextResults", "currentSearchCategory1", "currentSearchCategory2", "currentSearchCategory3", "marketSellsFullText", "elementsSearch" };
        INSTANCE = new MarketView();
        m_elementReferences = new ArrayList<ElementFilterView>();
        for (final Elements e : MultiElementsInfo.ELEMENTS_LIST) {
            MarketView.m_elementReferences.add(new ElementFilterView(ElementReference.getElementReferenceFromElements(e)));
        }
    }
}
