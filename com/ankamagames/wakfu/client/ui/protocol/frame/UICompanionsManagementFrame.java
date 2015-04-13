package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.companion.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import gnu.trove.*;

public class UICompanionsManagementFrame implements MessageFrame, WebShopListener, CompanionModelListener, PartyModelListener, CompanionManagerListener, InventoryObserver
{
    public static final UICompanionsManagementFrame INSTANCE;
    protected static final Logger m_logger;
    private CompanionsSavedSearch m_companionsSavedSearch;
    private final TIntObjectHashMap<CharacteristicCompanionView> m_companionViews;
    TLongObjectHashMap<CharacterView> m_heroes;
    private DialogUnloadListener m_dialogUnloadListener;
    
    private UICompanionsManagementFrame() {
        super();
        this.m_companionViews = new TIntObjectHashMap<CharacteristicCompanionView>();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19375: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.COMPANIONS_ENABLE)) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("companionsManagementDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UICompanionsManagementFrame.INSTANCE);
                    }
                }
            };
            this.m_companionsSavedSearch = new CompanionsSavedSearch();
            final WakfuKeyPreferenceStoreEnum companionsListSavedSearchKey = WakfuKeyPreferenceStoreEnum.COMPANIONS_LIST_SAVED_SEARCH_KEY;
            final String stringValue = WakfuClientInstance.getInstance().getGamePreferences().getStringValue(companionsListSavedSearchKey);
            this.m_companionsSavedSearch.fromSavedInfos(stringValue);
            PropertiesProvider.getInstance().setPropertyValue("companionCurrentPageIndex", 0);
            PropertiesProvider.getInstance().setPropertyValue("companionsListSavedSearch", this.m_companionsSavedSearch);
            CompanionManager.INSTANCE.addListener(this);
            this.loadCompanionsList();
            this.loadShopData();
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("companionsManagementDialog", Dialogs.getDialogPath("companionsManagementDialog"), 32768L, (short)10000);
            this.reflowCompanionsList();
            this.reflowHeroesList();
            PropertiesProvider.getInstance().setPropertyValue("overHero", null);
            PropertiesProvider.getInstance().setPropertyValue("heroesDisplayer", HeroesDisplayer.INSTANCE);
            PropertiesProvider.getInstance().setPropertyValue("companionManagementPage", 0);
            PropertiesProvider.getInstance().setLocalPropertyValue("mainCharacterSheet", false, "companionsManagementDialog");
            final PartyComportment partyComportment = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment();
            if (partyComportment.isInParty()) {
                partyComportment.getParty().addListener(this);
            }
            Xulor.getInstance().putActionClass("wakfu.companionsManagement", CompanionsManagementDialogActions.class);
            Xulor.getInstance().putActionClass("wakfu.companionsEmbedded", CompanionsEmbeddedActions.class);
            UIWebShopFrame.getInstance().requestLockForUI("companionsManagementDialog");
        }
    }
    
    public void reflowHeroesList() {
        this.m_heroes = new TLongObjectHashMap<CharacterView>();
        final ArrayList<CharacterView> characterViews = new ArrayList<CharacterView>();
        HeroesManager.INSTANCE.forEachHero(WakfuGameEntity.getInstance().getLocalAccount().getAccountId(), new TObjectProcedure<BasicCharacterInfo>() {
            @Override
            public boolean execute(final BasicCharacterInfo object) {
                final CharacterInfo characterInfo = (CharacterInfo)object;
                final CharacterView characterView = new CharacterView(characterInfo, null);
                if (HeroesLeaderManager.INSTANCE.isLeader(characterInfo)) {
                    return true;
                }
                UICompanionsManagementFrame.this.m_heroes.put(characterView.getCharacterInfo().getId(), characterView);
                characterViews.add(characterView);
                return true;
            }
        });
        this.m_companionViews.forEachValue(new TObjectProcedure<CharacteristicCompanionView>() {
            @Override
            public boolean execute(final CharacteristicCompanionView companionView) {
                if (!companionView.isFree() && !companionView.isOwned()) {
                    return true;
                }
                UICompanionsManagementFrame.this.m_heroes.put(-companionView.getCharacterInfo().getBreedId(), companionView);
                characterViews.add(companionView);
                return true;
            }
        });
        Collections.sort(characterViews, new Comparator<CharacterView>() {
            @Override
            public int compare(final CharacterView o1, final CharacterView o2) {
                if (!o1.isCompanion() && !o2.isCompanion()) {
                    return (int)(o1.getCharacterInfo().getId() - o2.getCharacterInfo().getId());
                }
                if (!o1.isCompanion() && o2.isCompanion()) {
                    return -1;
                }
                if (o1.isCompanion() && !o2.isCompanion()) {
                    return 1;
                }
                final CharacteristicCompanionView comp1 = (CharacteristicCompanionView)o1;
                final CharacteristicCompanionView comp2 = (CharacteristicCompanionView)o2;
                final String name = comp1.getName();
                final String name2 = comp2.getName();
                if (name == null || name2 == null) {
                    return 0;
                }
                return name.compareTo(name2);
            }
        });
        PropertiesProvider.getInstance().setPropertyValue("heroesList", characterViews);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null && localPlayer.getPartyComportment().isInParty()) {
                localPlayer.getPartyComportment().getParty().removeListener(this);
            }
            this.clearCompanionsListeners();
            this.m_companionViews.clear();
            CompanionManager.INSTANCE.removeListener(this);
            final WakfuKeyPreferenceStoreEnum companionsListSavedSearchKey = WakfuKeyPreferenceStoreEnum.COMPANIONS_LIST_SAVED_SEARCH_KEY;
            WakfuClientInstance.getInstance().getGamePreferences().setValue(companionsListSavedSearchKey, this.m_companionsSavedSearch.getSavedInfos());
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("companionsManagementDialog");
            Xulor.getInstance().removeActionClass("wakfu.companionsManagement");
            PropertiesProvider.getInstance().removeProperty("companionsList");
        }
    }
    
    public void forEachHeroView(final TLongObjectProcedure<CharacterView> procedure) {
        this.m_heroes.forEachEntry(procedure);
    }
    
    public CharacterView getCharacterFromHeroList(final long id) {
        return this.m_heroes.get(id);
    }
    
    private void clearCompanionsListeners() {
        final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
        for (final CompanionModel companionModel : CompanionManager.INSTANCE.getCompanions(accountId)) {
            companionModel.removeListener(this);
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void loadCompanionsList() {
        this.clearCompanionsListeners();
        for (final int breedId : CompanionMonsterBreedManager.INSTANCE.getMonsterBreedIds().toArray()) {
            final MonsterBreed breedFromId = MonsterBreedManager.getInstance().getBreedFromId((short)breedId);
            Label_0122: {
                if (breedFromId != null) {
                    if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.COMPANIONS_ENABLE)) {
                        boolean freeCompanion = false;
                        final int[] worldProperties = breedFromId.getBaseWorldProperties();
                        for (int i = 0; i < worldProperties.length; ++i) {
                            final int worldProperty = worldProperties[i];
                            if (worldProperty == WorldPropertyType.COMPANION_FREE.getId()) {
                                freeCompanion = true;
                            }
                        }
                        if (!freeCompanion) {
                            break Label_0122;
                        }
                    }
                    this.addCompanionView(breedId);
                }
            }
        }
    }
    
    public CompanionsSavedSearch getCompanionsSavedSearch() {
        return this.m_companionsSavedSearch;
    }
    
    public void reflowCompanionsList() {
        final CharacteristicCompanionView selectedCompanionView = (CharacteristicCompanionView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", "companionsManagementDialog");
        final ArrayList<CharacteristicCompanionView> companionViews = new ArrayList<CharacteristicCompanionView>();
        final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
        byte count = 0;
        CharacteristicCompanionView newSelectedCompanionView = null;
        final TIntObjectIterator<CharacteristicCompanionView> it = this.m_companionViews.iterator();
        while (it.hasNext()) {
            it.advance();
            final CharacteristicCompanionView companionView = it.value();
            final short breedId = companionView.getCharacterInfo().getBreedId();
            final CompanionModel companionModel = CompanionManager.INSTANCE.getCompanionWithBreed(accountId, breedId);
            if (companionModel == null || !companionModel.isUnlocked()) {
                if (this.m_companionsSavedSearch.getCompanionFilterType() == CompanionsSavedSearch.CompanionFilterType.OWNED) {
                    continue;
                }
            }
            else {
                ++count;
                if (this.m_companionsSavedSearch.getCompanionFilterType() == CompanionsSavedSearch.CompanionFilterType.UNOWNED) {
                    continue;
                }
            }
            companionViews.add(companionView);
            if (selectedCompanionView != null && selectedCompanionView.getCharacterInfo().getBreedId() == breedId) {
                newSelectedCompanionView = companionView;
            }
        }
        if (this.m_companionsSavedSearch.isAlphabeticalSorted()) {
            Collections.sort(companionViews, new Comparator<CharacteristicCompanionView>() {
                @Override
                public int compare(final CharacteristicCompanionView o1, final CharacteristicCompanionView o2) {
                    final String name = o1.getName();
                    final String name2 = o2.getName();
                    if (name == null || name2 == null) {
                        return 0;
                    }
                    return name.compareTo(name2);
                }
            });
        }
        if (this.m_companionsSavedSearch.isLevelSorted()) {
            Collections.sort(companionViews, new Comparator<CharacteristicCompanionView>() {
                @Override
                public int compare(final CharacteristicCompanionView o1, final CharacteristicCompanionView o2) {
                    return o2.getLevel() - o1.getLevel();
                }
            });
        }
        int freeIndex = -1;
        for (final CharacteristicCompanionView companionView2 : companionViews) {
            if (companionView2.isFree()) {
                freeIndex = companionViews.indexOf(companionView2);
                break;
            }
        }
        if (freeIndex != -1) {
            final CharacteristicCompanionView companionView = companionViews.remove(freeIndex);
            companionViews.add(0, companionView);
        }
        PropertiesProvider.getInstance().setPropertyValue("companionsList", companionViews);
        if (!companionViews.isEmpty()) {
            PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", (newSelectedCompanionView == null) ? companionViews.get(0) : newSelectedCompanionView, "companionsManagementDialog");
        }
        final TextWidgetFormater countText = new TextWidgetFormater();
        countText.b().addColor(new Color(0.01f, 0.51f, 0.81f, 1.0f)).append(count)._b();
        final TextWidgetFormater totalCountText = new TextWidgetFormater();
        totalCountText.b().append(this.m_companionViews.size())._b();
        final String text = WakfuTranslator.getInstance().getString("companionsOwnedCount", countText.finishAndToString(), totalCountText.finishAndToString());
        PropertiesProvider.getInstance().setPropertyValue("companionsOwnedCount", text);
    }
    
    @Override
    public void onInitialize() {
        this.loadShopData();
    }
    
    @Override
    public void onHome() {
    }
    
    @Override
    public void onSearch() {
    }
    
    private void loadShopData() {
        this.fillCompanions(this.m_companionViews);
    }
    
    private void fillCompanions(final TIntObjectHashMap<CharacteristicCompanionView> companions) {
        CategoriesKeyLoader.INSTANCE.getCategoriesKey("companions", new CategoriesKeyListener() {
            @Override
            public void onCategoriesKey(final ArrayList<Category> categories) {
                if (categories.isEmpty()) {
                    return;
                }
                ArticlesListLoader.INSTANCE.getArticlesList(categories.get(0).getId(), 1, 50, new ArticlesListListener() {
                    @Override
                    public void onArticlesList(final ArrayList<Article> articles, final int count) {
                        for (final Article article : articles) {
                            final ArrayList<SubArticle> contentIds = article.getSubArticles();
                            if (contentIds.size() != 1) {
                                continue;
                            }
                            final SubArticle subArticle = contentIds.get(0);
                            final int itemId = subArticle.getContentId();
                            final int breedId = CompanionItemManager.INSTANCE.getBreedId(itemId);
                            if (breedId == 0) {
                                continue;
                            }
                            final CharacteristicCompanionView view = companions.get(breedId);
                            if (view == null) {
                                UICompanionsManagementFrame.m_logger.error((Object)("Le compagnon de breed " + breedId + " n'existe pas !"));
                            }
                            else {
                                view.setShopArticle(article);
                            }
                        }
                    }
                    
                    @Override
                    public void onError() {
                    }
                });
            }
            
            @Override
            public void onError() {
            }
        });
    }
    
    @Override
    public void xpChanged(final CompanionModel model, final long previousXp) {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("companionsManagementDialog");
        PropertiesProvider.getInstance().firePropertyValueChanged("characterSheet", "xpRatio", elementMap);
        PropertiesProvider.getInstance().firePropertyValueChanged("characterSheet", "currentLevelPercentage", elementMap);
    }
    
    @Override
    public void nameChanged(final CompanionModel model) {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("companionsManagementDialog");
        final CharacteristicCompanionView characteristicCompanionView = (CharacteristicCompanionView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", elementMap);
        final ShortCharacterView shortCharacterView = characteristicCompanionView.getShortCharacterView();
        shortCharacterView.setName(model.getName());
        PropertiesProvider.getInstance().firePropertyValueChanged(shortCharacterView, "name");
    }
    
    @Override
    public void idChanged(final CompanionModel model) {
    }
    
    @Override
    public void onCurrentHpChanged(final CompanionModel model) {
    }
    
    @Override
    public void onMaxHpChanged(final CompanionModel model) {
    }
    
    @Override
    public void onUnlockedChanged(final CompanionModel companionModel) {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("companionsManagementDialog");
        final CharacteristicCompanionView characteristicCompanionView = (CharacteristicCompanionView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", elementMap);
        PropertiesProvider.getInstance().firePropertyValueChanged(characteristicCompanionView, "isOwned");
    }
    
    public int getCurrentCompanionIndex() {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("companionsManagementDialog");
        final CharacteristicCompanionView characteristicCompanionView = (CharacteristicCompanionView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", "companionsManagementDialog");
        if (characteristicCompanionView == null) {
            return 0;
        }
        final com.ankamagames.xulor2.component.List l = (com.ankamagames.xulor2.component.List)elementMap.getElement("companionsList");
        return l.getItemIndex(characteristicCompanionView);
    }
    
    @Override
    public void onMemberAdded(final PartyModelInterface party, final PartyMemberInterface member) {
        if (!member.isCompanion() || member.getClientId() != WakfuGameEntity.getInstance().getLocalAccount().getAccountId()) {
            return;
        }
        this.reflowCompanionsList();
        UICompanionsEmbeddedFrame.recomputeRealCompanionList();
    }
    
    @Override
    public void onMemberRemoved(final PartyModelInterface party, final PartyMemberInterface member) {
        if ((!member.isCompanion() && !member.isHero()) || member.getClientId() != WakfuGameEntity.getInstance().getLocalAccount().getAccountId()) {
            return;
        }
        if (member.isHero()) {
            HeroesManager.INSTANCE.removeHeroFromParty(member.getClientId(), member.getCharacterId());
        }
        else {
            this.reflowCompanionsList();
            UICompanionsEmbeddedFrame.removeCompanionView(member.getBreedId());
            UICompanionsEmbeddedFrame.recomputeRealCompanionList();
        }
    }
    
    @Override
    public void onLeaderChange(final PartyModelInterface party, final long previousLeader, final long newLeader) {
    }
    
    @Override
    public void companionRemoved(final CompanionModel companion) {
    }
    
    @Override
    public void companionAdded(final CompanionModel companion) {
        this.addCompanionView(companion.getBreedId());
    }
    
    private void addCompanionView(final int breedId) {
        final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
        CompanionModel companionModel = CompanionManager.INSTANCE.getCompanionWithBreed(accountId, breedId);
        if (companionModel == null) {
            companionModel = new CompanionModel((short)breedId);
        }
        else {
            companionModel.addListener(this);
            companionModel.getItemEquipment().addObserver(this);
        }
        final NonPlayerCharacter npc = UICompanionsEmbeddedFrame.createNonPlayerCharacterCompanion(companionModel, false);
        this.m_companionViews.put(breedId, new CharacteristicCompanionView(npc, new CompanionViewShort(companionModel), companionModel));
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        final Inventory inventory = event.getInventory();
        final TIntObjectIterator<CharacteristicCompanionView> it = this.m_companionViews.iterator();
        while (it.hasNext()) {
            it.advance();
            final CharacteristicCompanionView companionView = it.value();
            if (companionView.getItemEquipment().equals(inventory)) {
                PropertiesProvider.getInstance().firePropertyValueChanged(companionView, "removeDisabledText");
                final CharacterInfo characterInfo = companionView.getCharacterInfo();
                if (characterInfo == null) {
                    continue;
                }
                this.reloadItemEffects(event, characterInfo);
            }
        }
    }
    
    public void reloadItemEffects(final CompanionModel companion) {
        final CharacteristicCompanionView companionView = this.m_companionViews.get(companion.getBreedId());
        if (companionView == null) {
            return;
        }
        final CharacterInfo characterInfo = companionView.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        characterInfo.reloadItemEffectsWithoutCheck();
    }
    
    public void reloadItemEffects(final InventoryEvent event, final CharacterInfo characterInfo) {
        if (event.getAction() == InventoryEvent.Action.ITEM_REMOVED || event.getAction() == InventoryEvent.Action.ITEM_REMOVED_AT) {
            final Item removedItem = (Item)((InventoryItemModifiedEvent)event).getConcernedItem();
            if (removedItem != null) {
                if (removedItem.getReferenceItem().getSetId() != 0) {
                    final ItemSet set = ItemSetManager.getInstance().getItemSet(removedItem.getReferenceItem().getSetId());
                    characterInfo.unapplyItemOnEquipEffect(removedItem, set);
                }
                else {
                    characterInfo.unapplyItemOnEquipEffect(removedItem);
                }
            }
        }
        characterInfo.reloadItemEffectsWithoutCheck();
    }
    
    static {
        INSTANCE = new UICompanionsManagementFrame();
        m_logger = Logger.getLogger((Class)UICompanionsManagementFrame.class);
    }
}
