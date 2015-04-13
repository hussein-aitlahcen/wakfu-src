package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.xulor2.core.dialogclose.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.market.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.market.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.regex.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.component.table.*;
import java.util.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import gnu.trove.*;

public class UIMarketFrame extends UIMarketTaxFrame implements DialogCloseRequestListener
{
    private static final Logger m_logger;
    public static final UIMarketFrame m_instance;
    private MarketBoard m_board;
    private int m_lastMarketId;
    private MarketEntryComparator m_requestSortingType;
    private TIntObjectHashMap<AbstractReferenceItem> m_itemsRefCache;
    private ArrayList<Integer> m_refIds;
    private int m_currentPageIndex;
    private boolean m_errorDisplayed;
    private Runnable m_runnable;
    private DecoratorAppearance m_appearance;
    private TIntObjectHashMap<ArrayList<String>> m_itemDetailedDialogIds;
    private DialogUnloadListener m_dialogUnloadListener;
    private short m_currentSearchIndex;
    private MarketRequestView m_lastRequest;
    public static final int BUY_PAGE_INDEX = 0;
    public static final int SELLS_PAGE_INDEX = 1;
    public static final int HISTORY_PAGE_INDEX = 2;
    
    public static UIMarketFrame getInstance() {
        return UIMarketFrame.m_instance;
    }
    
    private UIMarketFrame() {
        super();
        this.m_refIds = new ArrayList<Integer>();
        this.m_itemDetailedDialogIds = new TIntObjectHashMap<ArrayList<String>>();
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19264: {
                final UIMessage uiMessage = (UIMessage)message;
                if (uiMessage.getIntValue() == this.m_currentPageIndex) {
                    return false;
                }
                Message request = null;
                switch (this.m_currentPageIndex = uiMessage.getIntValue()) {
                    case 0: {
                        request = this.m_lastRequest.getLastRequestMessage();
                        break;
                    }
                    case 1: {
                        request = new MarketSellsListRequestMessage();
                        break;
                    }
                    case 2: {
                        request = new MarketHistoryRequestMessage();
                        break;
                    }
                    default: {
                        request = null;
                        break;
                    }
                }
                if (request == null) {
                    return false;
                }
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
                return false;
            }
            case 19263: {
                final UIMessage uiMessage = (UIMessage)message;
                final long marketEntryId = uiMessage.getLongValue();
                final MarketRemoveItemRequestMessage marketRemoveItemRequestMessage = new MarketRemoveItemRequestMessage(marketEntryId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(marketRemoveItemRequestMessage);
                switch (this.m_currentPageIndex) {
                    case 0:
                    case 1: {
                        MarketView.INSTANCE.onMarketEntryRemoved(marketEntryId);
                        break;
                    }
                    case 2: {
                        MarketView.INSTANCE.onMarketUnsoldEntryRemoved(marketEntryId);
                        break;
                    }
                }
                return false;
            }
            case 19258: {
                final UIMarketAddItem uiMarketAddItem = (UIMarketAddItem)message;
                final Item item = uiMarketAddItem.getItem();
                if (this.canBeAddedToMarket(item)) {
                    UIMarketFrame.m_logger.warn((Object)("L'objet " + item.getReferenceId() + " n'a pas le droit d'\u00eatre mis en vente"));
                    final String errorMsg = WakfuTranslator.getInstance().getString("market.addItem.unauthorized");
                    final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                    chatErrorMsg.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                    this.highLightSellDropZone(true);
                    return false;
                }
                this.m_item = item;
                final short quantity = uiMarketAddItem.getShortValue();
                this.addMerchantItem(item.getCopy(false), (quantity == -1) ? item.getQuantity() : quantity);
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_merchantItem, MerchantInventoryItem.FIELDS);
                return false;
            }
            case 19262: {
                final UIMessage uiMessage2 = (UIMessage)message;
                final long entryId = uiMessage2.getLongValue();
                final MarketPurchaseRequestMessage marketPurchaseRequestMessage = new MarketPurchaseRequestMessage();
                marketPurchaseRequestMessage.setEntryId(entryId);
                final short qty = uiMessage2.getShortValue();
                marketPurchaseRequestMessage.setPackQuantity(qty);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(marketPurchaseRequestMessage);
                if (MarketView.INSTANCE.onMarketEntryDecreased(entryId, qty)) {
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(this.m_lastRequest.getLastRequestMessage());
                }
                return false;
            }
            case 19242: {
                this.m_currentSearchIndex += 10;
                if (this.m_currentSearchIndex < 0) {
                    this.m_currentSearchIndex = 32757;
                }
                this.m_lastRequest.getLastRequestMessage().setFirstIndex(this.m_currentSearchIndex);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(this.m_lastRequest.getLastRequestMessage());
                return false;
            }
            case 19243: {
                this.m_currentSearchIndex = (short)Math.max(0, this.m_currentSearchIndex - 10);
                this.m_lastRequest.getLastRequestMessage().setFirstIndex(this.m_currentSearchIndex);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(this.m_lastRequest.getLastRequestMessage());
                return false;
            }
            case 19244: {
                this.m_currentSearchIndex = 0;
                this.m_lastRequest.getLastRequestMessage().setFirstIndex(this.m_currentSearchIndex);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(this.m_lastRequest.getLastRequestMessage());
                return false;
            }
            case 19245: {
                this.m_currentSearchIndex = (short)(MarketView.INSTANCE.getTotalResultSize() - MarketView.INSTANCE.getTotalResultSize() % 10);
                this.m_lastRequest.getLastRequestMessage().setFirstIndex(this.m_currentSearchIndex);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(this.m_lastRequest.getLastRequestMessage());
                return false;
            }
            case 19259: {
                if (this.m_merchantItem == null) {
                    return false;
                }
                final MarketAddItemRequestMessage marketAddItemRequestMessage = new MarketAddItemRequestMessage(this.m_item.getUniqueId(), this.m_merchantItem.getPackType().id, (short)(this.m_merchantItem.getQuantity() / this.m_merchantItem.getPackType().qty), this.m_merchantItem.getPrice(), this.m_merchantItem.getAuctionDuration().id);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(marketAddItemRequestMessage);
                this.clearMerchantItem();
                return false;
            }
            case 19266: {
                this.clearMerchantItem();
                return false;
            }
            case 19253: {
                MarketView.INSTANCE.clearEntries();
                PropertiesProvider.getInstance().setPropertyValue("marketSearchDirty", false);
                PropertiesProvider.getInstance().firePropertyValueChanged(MarketView.INSTANCE, "results");
                final UIMarketRequestMessage uiMarketRequestMessage = (UIMarketRequestMessage)message;
                final ItemType searchItemType = MarketView.INSTANCE.getCurrentSearchItemType();
                final short typeId = (short)((searchItemType == null) ? -1 : searchItemType.getId());
                final String name = uiMarketRequestMessage.getName();
                final short minLevel = uiMarketRequestMessage.getMinLevel();
                final short maxLevel = uiMarketRequestMessage.getMaxLevel();
                final short minPrice = uiMarketRequestMessage.getMinPrice();
                final short maxPrice = uiMarketRequestMessage.getMaxPrice();
                final boolean lowestMode = uiMarketRequestMessage.isLowestPrices();
                final byte elementsMask = uiMarketRequestMessage.getElementsMask();
                this.m_refIds.clear();
                this.m_currentSearchIndex = 0;
                final boolean localSearch = (name != null && name.length() > 0) || minLevel != -1 || maxLevel != -1;
                if (localSearch) {
                    this.collectRefIdsFromString(name, searchItemType, minLevel, maxLevel, elementsMask);
                    final String errorKey = this.checkLastRequestValidity(minPrice, maxPrice, lowestMode);
                    if (errorKey != null) {
                        PropertiesProvider.getInstance().setPropertyValue("marketBoard.SearchError", WakfuTranslator.getInstance().getString(errorKey));
                        this.launchSearchErrorProcess();
                        return false;
                    }
                }
                final MarketConsultRequestMessage lastRequestMessage = this.m_lastRequest.getLastRequestMessage();
                lastRequestMessage.setItemType(typeId);
                lastRequestMessage.setMinPrice(minPrice);
                lastRequestMessage.setMaxPrice(maxPrice);
                lastRequestMessage.setSortingType((byte)((this.m_requestSortingType == null) ? -1 : this.m_requestSortingType.idx));
                lastRequestMessage.setFirstIndex(this.m_currentSearchIndex);
                lastRequestMessage.setItemRefIds(this.m_refIds);
                lastRequestMessage.setLowestMode(lowestMode);
                this.m_lastRequest.setMinLevel(minLevel);
                this.m_lastRequest.setMaxLevel(maxLevel);
                this.m_lastRequest.setName(name);
                this.m_lastRequest.setLastRequestMessage(lastRequestMessage);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(lastRequestMessage);
                return false;
            }
            case 19265: {
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new MarketGetMoneyBackRequestMessage());
                MarketView.INSTANCE.clearSoldEntries();
                PropertiesProvider.getInstance().firePropertyValueChanged(MarketView.INSTANCE, "history", "moneyStocked", "hasMoneyStocked");
                return false;
            }
            case 19267: {
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new MarketGetBackUnsoldItemsRequestMessage());
                MarketView.INSTANCE.clearUnsoldEntries();
                PropertiesProvider.getInstance().firePropertyValueChanged(MarketView.INSTANCE, "history", "hasUnsoldStocked");
                return false;
            }
            default: {
                return super.onMessage(message);
            }
        }
    }
    
    public boolean canBeAddedToMarket(final Item item) {
        return (item.hasBind() && item.getBind().getType().isShop()) || item.isBound() || item.isRent() || (item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE).isValid(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getPosition(), item, WakfuGameEntity.getInstance().getLocalPlayer().getEffectContext()));
    }
    
    private String checkLastRequestValidity(final short minPrice, final short maxPrice, final boolean lowestMode) {
        if (this.m_refIds.size() > 200) {
            UIMarketFrame.m_logger.error((Object)("Recherche trop vague ! (" + this.m_refIds + " )"));
            return "marketBoard.searchError";
        }
        if (this.m_refIds.isEmpty()) {
            UIMarketFrame.m_logger.error((Object)"Aucun item ne correspond \u00e0 votre recherche");
            return "marketBoard.noResult";
        }
        return null;
    }
    
    public void launchSearchErrorProcess() {
        if (this.m_errorDisplayed) {
            return;
        }
        this.m_errorDisplayed = true;
        final ModulationColorListTween colorListTween = this.fade(true);
        if (colorListTween == null) {
            return;
        }
        colorListTween.addTweenEventListener(new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                switch (e) {
                    case TWEEN_ENDED: {
                        UIMarketFrame.this.m_runnable = new Runnable() {
                            @Override
                            public void run() {
                                final ModulationColorListTween colorListTween2 = UIMarketFrame.this.fade(false);
                                if (colorListTween2 == null) {
                                    return;
                                }
                                colorListTween2.addTweenEventListener(new TweenEventListener() {
                                    @Override
                                    public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                                        switch (e) {
                                            case TWEEN_ENDED: {
                                                UIMarketFrame.this.m_errorDisplayed = false;
                                                colorListTween2.removeTweenEventListener(this);
                                                break;
                                            }
                                        }
                                    }
                                });
                            }
                        };
                        ProcessScheduler.getInstance().schedule(UIMarketFrame.this.m_runnable, 2000L, 1);
                        colorListTween.removeTweenEventListener(this);
                        break;
                    }
                }
            }
        });
    }
    
    private ModulationColorListTween fade(final boolean fadeIn) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("marketDialog");
        if (map == null) {
            return null;
        }
        final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
        Widget w = null;
        w = (Widget)map.getElement("errorContainer");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("errorLabel");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        if (w != null) {
            Color c1;
            Color c2;
            if (fadeIn) {
                c1 = new Color(Color.WHITE_ALPHA.get());
                c2 = new Color(Color.WHITE.get());
            }
            else {
                c1 = new Color(Color.WHITE.get());
                c2 = new Color(Color.WHITE_ALPHA.get());
            }
            w.removeTweensOfType(ModulationColorListTween.class);
            final ModulationColorListTween tween = new ModulationColorListTween(c1, c2, mcc, 0, 1000, 1, TweenFunction.PROGRESSIVE);
            w.addTween(tween);
            return tween;
        }
        return null;
    }
    
    private void collectRefIdsFromString(final String search, final ItemType type, final short minLevel, final short maxLevel, final byte elementMask) {
        this.m_refIds.clear();
        if (this.m_itemsRefCache == null) {
            this.m_itemsRefCache = ((ItemManagerImpl)ReferenceItemManager.getInstance()).getFullList();
        }
        final ArrayList<String> includes = new ArrayList<String>();
        final ArrayList<String> excludes = new ArrayList<String>();
        includes.add(search);
        Pattern patternIncludes = null;
        Pattern patternExcludes = null;
        try {
            if (includes.size() > 0) {
                String regexIncludes = "";
                for (int i = 0; i < includes.size(); ++i) {
                    if (i > 0) {
                        regexIncludes += "|";
                    }
                    final String include = StringUtils.cleanSentence(includes.get(i).trim().toLowerCase());
                    regexIncludes = regexIncludes + ".*" + include + ".*";
                }
                patternIncludes = Pattern.compile(regexIncludes);
            }
            if (excludes.size() > 0) {
                String regexExcludes = "";
                for (int i = 0; i < excludes.size(); ++i) {
                    if (i > 0) {
                        regexExcludes += "|";
                    }
                    final String include = StringUtils.cleanSentence(excludes.get(i).trim().toLowerCase());
                    regexExcludes = regexExcludes + ".*" + include + ".*";
                }
                patternExcludes = Pattern.compile(regexExcludes);
            }
        }
        catch (Exception e) {
            UIMarketFrame.m_logger.error((Object)e.getMessage());
            PropertiesProvider.getInstance().setPropertyValue("marketBoard.SearchError", WakfuTranslator.getInstance().getString("marketBoard.noResult"));
            this.launchSearchErrorProcess();
            return;
        }
        final long t = System.currentTimeMillis();
        final TIntObjectIterator<AbstractReferenceItem> it = (TIntObjectIterator<AbstractReferenceItem>)this.m_itemsRefCache.iterator();
        while (it.hasNext()) {
            if (System.currentTimeMillis() - t > 10000L) {
                System.err.println("fail");
                return;
            }
            it.advance();
            final AbstractReferenceItem referenceItem = it.value();
            if (referenceItem == null) {
                UIMarketFrame.m_logger.error((Object)"RefItem null dans la liste ! Cela ne devrait pas arriver.");
            }
            else {
                try {
                    final short level = referenceItem.getLevel();
                    if (type != null && !referenceItem.getItemType().isChildOf(type)) {
                        continue;
                    }
                    if ((minLevel != -1 && level < minLevel) || (maxLevel != -1 && level > maxLevel)) {
                        continue;
                    }
                    if (includes.size() > 0 || excludes.size() > 0) {
                        final String itemName = StringUtils.cleanSentence(referenceItem.getName().toLowerCase());
                        if (patternIncludes != null) {
                            final Matcher matcherIncludes = patternIncludes.matcher(itemName);
                            if (matcherIncludes.matches()) {
                                this.m_refIds.add(referenceItem.getId());
                            }
                        }
                        if (patternExcludes == null) {
                            continue;
                        }
                        final Matcher matcherExcludes = patternExcludes.matcher(itemName);
                        if (!matcherExcludes.matches()) {
                            continue;
                        }
                        this.m_refIds.remove((Object)referenceItem.getId());
                    }
                    else {
                        this.m_refIds.add(referenceItem.getId());
                    }
                }
                catch (Exception e2) {
                    UIMarketFrame.m_logger.error((Object)("Erreur en parcourant les refItems dans la recherche du march\u00e9 sur le refItme d'id=" + referenceItem.getId() + "\n" + e2.getMessage()));
                    PropertiesProvider.getInstance().setPropertyValue("marketBoard.SearchError", WakfuTranslator.getInstance().getString("marketBoard.noResult"));
                    this.launchSearchErrorProcess();
                }
            }
        }
    }
    
    public void highLightSellDropZone() {
        this.highLightSellDropZone(false);
    }
    
    public void highLightSellDropZone(final boolean error) {
        if (this.m_appearance == null) {
            ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("marketDialog");
            if (map == null) {
                return;
            }
            final RenderableContainer renderableContainer = (RenderableContainer)map.getElement("dropRenderable");
            map = renderableContainer.getInnerElementMap();
            if (map == null) {
                return;
            }
            final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
            Widget w = null;
            w = (Widget)map.getElement("dropContainer");
            if (w == null) {
                return;
            }
            this.m_appearance = w.getAppearance();
        }
        final Color c1 = new Color(Color.WHITE.get());
        final Color c2 = new Color(error ? WakfuClientConstants.ERROR_HIGHLIGHT_COLOR.get() : WakfuClientConstants.HIGHLIGHT_COLOR.get());
        this.m_appearance.removeTweensOfType(ModulationColorTween.class);
        this.m_appearance.setModulationColor(c1);
        final ModulationColorTween tween = new ModulationColorTween(c1, c2, this.m_appearance, 0, 500, error ? 3 : -1, TweenFunction.PROGRESSIVE);
        this.m_appearance.addTween(tween);
    }
    
    public void stopDropZoneHighlight() {
        if (this.m_appearance != null) {
            this.m_appearance.removeTweensOfType(ModulationColorTween.class);
        }
    }
    
    public void addItemDetailedDialogId(final int referenceItemId, final String id) {
        if (!this.m_itemDetailedDialogIds.containsKey(referenceItemId)) {
            this.m_itemDetailedDialogIds.put(referenceItemId, new ArrayList<String>());
        }
        final ArrayList<String> ids = this.m_itemDetailedDialogIds.get(referenceItemId);
        if (!ids.contains(id)) {
            ids.add(id);
        }
    }
    
    @Override
    public int onDialogCloseRequest(final String id) {
        if (this.checkAndRemoveItemDetailsDialogIds(id)) {
            return 0;
        }
        if (!this.m_itemDetailedDialogIds.isEmpty()) {
            return 3;
        }
        return 0;
    }
    
    private boolean checkAndRemoveItemDetailsDialogIds(final String id) {
        final TIntObjectIterator<ArrayList<String>> it = this.m_itemDetailedDialogIds.iterator();
        while (it.hasNext()) {
            it.advance();
            final ArrayList<String> list = it.value();
            if (list.contains(id)) {
                list.remove(id);
                if (list.isEmpty()) {
                    it.remove();
                }
                return true;
            }
        }
        return false;
    }
    
    private TableModel getTableModel() {
        final TableModel model = new TableModel();
        model.setChangeListener(new TableModel.TableModelChangeListener() {
            @Override
            public void onColumnSortChanged(final String columnId, final boolean direct) {
                if ("name".equals(columnId)) {
                    if (UIMarketFrame.this.m_refIds.size() > 0) {
                        Collections.sort((List<Object>)UIMarketFrame.this.m_refIds, (Comparator<? super Object>)(direct ? ItemNameComparator.INSTANCE : ItemNameComparatorIndirect.INSTANCE));
                        UIMarketFrame.this.m_requestSortingType = null;
                    }
                    else {
                        UIMarketFrame.this.m_requestSortingType = null;
                    }
                }
                else if ("level".equals(columnId)) {
                    if (UIMarketFrame.this.m_refIds.size() > 0) {
                        Collections.sort((List<Object>)UIMarketFrame.this.m_refIds, (Comparator<? super Object>)(direct ? ItemLevelComparator.INSTANCE : ItemLevelComparatorIndirect.INSTANCE));
                        UIMarketFrame.this.m_requestSortingType = null;
                    }
                    else {
                        UIMarketFrame.this.m_requestSortingType = null;
                    }
                }
                else if ("duration".equals(columnId)) {
                    UIMarketFrame.this.m_requestSortingType = (direct ? MarketEntryComparator.REMAINING_TIME_CRESCENT : MarketEntryComparator.REMAINING_TIME_DESCENDING);
                }
                else if ("rarity".equals(columnId)) {
                    Collections.sort((List<Object>)UIMarketFrame.this.m_refIds, (Comparator<? super Object>)(direct ? ItemRarityComparator.INSTANCE : ItemRarityComparatorIndirect.INSTANCE));
                    UIMarketFrame.this.m_requestSortingType = null;
                }
                else if ("seller".equals(columnId)) {
                    UIMarketFrame.this.m_requestSortingType = (direct ? MarketEntryComparator.SELLER_CRESCENT : MarketEntryComparator.SELLER_DESCENDING);
                }
                else if ("price".equals(columnId)) {
                    UIMarketFrame.this.m_requestSortingType = (direct ? MarketEntryComparator.PRICE_CRESCENT : MarketEntryComparator.PRICE_DESCENDING);
                }
                else if ("quantity".equals(columnId)) {
                    UIMarketFrame.this.m_requestSortingType = (direct ? MarketEntryComparator.PACK_NUMBER_CRESCENT : MarketEntryComparator.PACK_NUMBER_DESCENDING);
                }
                else if ("packType".equals(columnId)) {
                    UIMarketFrame.this.m_requestSortingType = (direct ? MarketEntryComparator.PACK_TYPE_CRESCENT : MarketEntryComparator.PACK_TYPE_DESCENDING);
                }
                final MarketConsultRequestMessage lastRequestMessage = UIMarketFrame.this.m_lastRequest.getLastRequestMessage();
                lastRequestMessage.setSortingType((byte)((UIMarketFrame.this.m_requestSortingType == null) ? -1 : UIMarketFrame.this.m_requestSortingType.idx));
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(lastRequestMessage);
            }
        });
        return model;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            ProtectorUtils.requestTaxUpdate();
            this.m_taxContext = TaxContext.MARKET_ADD_ITEM_CONTEXT;
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("marketDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIMarketFrame.getInstance());
                    }
                    else if (!UIMarketFrame.this.m_itemDetailedDialogIds.isEmpty()) {
                        UIMarketFrame.this.checkAndRemoveItemDetailsDialogIds(id);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            NetEnabledWidgetManager.INSTANCE.createGroup("marketBoardSearchLock");
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            Xulor.getInstance().load("marketDialog", Dialogs.getDialogPath("marketDialog"), 32768L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("marketDialog");
            if (map != null) {
                final TableModel model = this.getTableModel();
                final Table table = (Table)map.getElement("results");
                if (table != null) {
                    table.setTableModel(model);
                }
            }
            Xulor.getInstance().putActionClass("wakfu.market", MarketDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("market", MarketView.INSTANCE);
            PropertiesProvider.getInstance().setPropertyValue("marketSearchDirty", false);
            PropertiesProvider.getInstance().setPropertyValue("marketSearchInvalid", false);
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", 0, "marketDialog");
            if (this.m_lastRequest == null) {
                this.m_lastRequest = new MarketRequestView();
            }
            final MarketConsultRequestMessage lastRequestMessage = this.m_lastRequest.getLastRequestMessage();
            lastRequestMessage.setFirstIndex((short)0);
            lastRequestMessage.setSortingType((byte)(-1));
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(lastRequestMessage);
            PropertiesProvider.getInstance().setPropertyValue("marketSavedSearch", this.m_lastRequest);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            super.onFrameAdd(frameHandler, isAboutToBeAdded);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_itemDetailedDialogIds.clear();
            if (this.m_runnable != null) {
                ProcessScheduler.getInstance().remove(this.m_runnable);
            }
            MarketView.INSTANCE.clean();
            if (this.m_lastRequest.getLastRequestMessage().getItemType() == -1) {
                MarketView.INSTANCE.cleanItemTypes();
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                localPlayer.finishCurrentOccupation();
            }
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this);
            Xulor.getInstance().unload("marketDialog");
            NetEnabledWidgetManager.INSTANCE.destroyGroup("marketBoardSearchLock");
            MarketDialogActions.clean();
            Xulor.getInstance().removeActionClass("wakfu.market");
            PropertiesProvider.getInstance().removeProperty("market");
            this.m_errorDisplayed = false;
            this.m_board = null;
            this.m_itemsRefCache = null;
            this.m_refIds.clear();
            this.m_currentPageIndex = 0;
            this.m_currentSearchIndex = 0;
            this.m_appearance = null;
            this.m_requestSortingType = null;
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
            WakfuSoundManager.getInstance().playGUISound(600013L);
            super.onFrameRemove(frameHandler, isAboutToBeRemoved);
        }
    }
    
    public void setMarketBoard(final MarketBoard marketBoard) {
        if (this.m_lastMarketId != marketBoard.getMarketId()) {
            this.m_lastMarketId = marketBoard.getMarketId();
        }
        this.m_board = marketBoard;
    }
    
    private void clearMerchantItem() {
        this.m_merchantItem.setItem(null);
        this.m_merchantItemFirstQuantity = -1;
        this.m_merchantItem.setPackType(PackType.ONE);
        this.m_merchantItem.setPrice(0);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_merchantItem, MerchantInventoryItem.FIELDS);
    }
    
    public short getCurrentSearchIndex() {
        return this.m_currentSearchIndex;
    }
    
    public MarketBoard getBoard() {
        return this.m_board;
    }
    
    public int getCurrentPageIndex() {
        return this.m_currentPageIndex;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIMarketFrame.class);
        m_instance = new UIMarketFrame();
    }
    
    private static class ItemNameComparator implements Comparator<Integer>
    {
        private static final ItemNameComparator INSTANCE;
        
        @Override
        public int compare(final Integer o1, final Integer o2) {
            final AbstractReferenceItem ref1 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            final AbstractReferenceItem ref2 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            if (ref1 == null || ref2 == null) {
                return 0;
            }
            return ref1.getName().compareTo(ref2.getName());
        }
        
        static {
            INSTANCE = new ItemNameComparator();
        }
    }
    
    private static class ItemNameComparatorIndirect implements Comparator<Integer>
    {
        private static final ItemNameComparatorIndirect INSTANCE;
        
        @Override
        public int compare(final Integer o1, final Integer o2) {
            final AbstractReferenceItem ref1 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            final AbstractReferenceItem ref2 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            if (ref1 == null || ref2 == null) {
                return 0;
            }
            return ref2.getName().compareTo(ref1.getName());
        }
        
        static {
            INSTANCE = new ItemNameComparatorIndirect();
        }
    }
    
    private static class ItemLevelComparator implements Comparator<Integer>
    {
        private static final ItemLevelComparator INSTANCE;
        
        @Override
        public int compare(final Integer o1, final Integer o2) {
            final AbstractReferenceItem ref1 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            final AbstractReferenceItem ref2 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            if (ref1 == null || ref2 == null) {
                return 0;
            }
            return ref1.getLevel() - ref2.getLevel();
        }
        
        static {
            INSTANCE = new ItemLevelComparator();
        }
    }
    
    private static class ItemLevelComparatorIndirect implements Comparator<Integer>
    {
        private static final ItemLevelComparatorIndirect INSTANCE;
        
        @Override
        public int compare(final Integer o1, final Integer o2) {
            final AbstractReferenceItem ref1 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            final AbstractReferenceItem ref2 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            if (ref1 == null || ref2 == null) {
                return 0;
            }
            return ref2.getLevel() - ref1.getLevel();
        }
        
        static {
            INSTANCE = new ItemLevelComparatorIndirect();
        }
    }
    
    private static class ItemRarityComparator implements Comparator<Integer>
    {
        private static final ItemRarityComparator INSTANCE;
        
        @Override
        public int compare(final Integer o1, final Integer o2) {
            final AbstractReferenceItem ref1 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            final AbstractReferenceItem ref2 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            if (ref1 == null || ref2 == null) {
                return 0;
            }
            return ref1.getRarity().getSortOrder() - ref2.getRarity().getSortOrder();
        }
        
        static {
            INSTANCE = new ItemRarityComparator();
        }
    }
    
    private static class ItemRarityComparatorIndirect implements Comparator<Integer>
    {
        private static final ItemRarityComparatorIndirect INSTANCE;
        
        @Override
        public int compare(final Integer o1, final Integer o2) {
            final AbstractReferenceItem ref1 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            final AbstractReferenceItem ref2 = ReferenceItemManager.getInstance().getReferenceItem((int)o1);
            if (ref1 == null || ref2 == null) {
                return 0;
            }
            return ref2.getRarity().getSortOrder() - ref1.getRarity().getSortOrder();
        }
        
        static {
            INSTANCE = new ItemRarityComparatorIndirect();
        }
    }
}
