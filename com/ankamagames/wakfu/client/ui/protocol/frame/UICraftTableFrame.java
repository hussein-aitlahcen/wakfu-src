package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.common.game.craft.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.craft.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import gnu.trove.*;

public class UICraftTableFrame implements MessageFrame, CraftListener, MobileStartPathListener
{
    protected static final Logger m_logger;
    private static final UICraftTableFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private TIntObjectHashMap<IngredientView> m_recipeIngredientStack;
    private CraftView m_craftView;
    private ElementMap m_dialogElementMap;
    private int m_remainingCrafts;
    private ProgressBar m_progressBar;
    private ToggleButton m_button;
    private boolean m_freeMode;
    private final Runnable m_progressBarRunnable;
    
    public UICraftTableFrame() {
        super();
        this.m_progressBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (UICraftTableFrame.this.m_progressBar != null) {
                    UICraftTableFrame.this.m_progressBar.setValue(1.0f);
                }
            }
        };
    }
    
    public static UICraftTableFrame getInstance() {
        return UICraftTableFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIExchangeFrame.getInstance());
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("craftTableDialog") || id.equals("craftTableFreeDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UICraftTableFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            localPlayer.getCraftHandler().addListener(this);
            localPlayer.getActor().addStartPathListener(this);
            final String dialogId = this.m_freeMode ? "craftTableFreeDialog" : "craftTableDialog";
            this.m_dialogElementMap = Xulor.getInstance().load(dialogId, Dialogs.getDialogPath(dialogId), 1L, (short)10000).getElementMap();
            PropertiesProvider.getInstance().setLocalPropertyValue("craft", this.m_craftView, dialogId);
            this.m_remainingCrafts = 0;
            PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", this.m_remainingCrafts);
            PropertiesProvider.getInstance().setPropertyValue("selectedRecipe", null);
            PropertiesProvider.getInstance().setPropertyValue("craftRunning", false);
            PropertiesProvider.getInstance().setPropertyValue("recipeIngredientStack", null);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            Xulor.getInstance().putActionClass("wakfu.craftTable", CraftTableDialogActions.class);
            if (!WakfuGameEntity.getInstance().hasFrame(UICraftFrame.getInstance())) {
                Xulor.getInstance().putActionClass("wakfu.crafts", CraftDialogsActions.class);
            }
            if (!Xulor.getInstance().isActionClassLoaded("wakfu.equipment")) {
                Xulor.getInstance().putActionClass("wakfu.equipment", EquipmentDialogActions.class);
            }
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (PropertiesProvider.getInstance().getBooleanProperty("craftRunning") && localPlayer.getCurrentOccupation() != null) {
                this.requestCancelCraft();
            }
            localPlayer.cancelCurrentOccupation(false, true);
            localPlayer.getCraftHandler().removeListener(this);
            localPlayer.getActor().removeStartListener(this);
            this.clearIngredientStackAndRefresh();
            this.m_recipeIngredientStack = null;
            this.m_dialogElementMap = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("craftTableDialog");
            Xulor.getInstance().unload("craftTableFreeDialog");
            Xulor.getInstance().unload("splitStackDialog");
            PropertiesProvider.getInstance().removeProperty("currentCraftNb");
            this.m_progressBar = null;
            this.m_button = null;
            PropertiesProvider.getInstance().removeProperty("selectedRecipe");
            PropertiesProvider.getInstance().removeProperty("craftRunning");
            PropertiesProvider.getInstance().removeProperty("recipeIngredientStack");
            PropertiesProvider.getInstance().removeProperty("craftTableName");
            WakfuSoundManager.getInstance().playGUISound(600013L);
            Xulor.getInstance().removeActionClass("wakfu.craftTable");
            if (!WakfuGameEntity.getInstance().hasFrame(UICraftFrame.getInstance())) {
                Xulor.getInstance().removeActionClass("wakfu.crafts");
            }
            if (!WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
                Xulor.getInstance().removeActionClass("wakfu.equipment");
            }
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19992: {
                return false;
            }
            case 16851: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final boolean freeMode = msg.getBooleanValue();
                this.m_craftView.setInFreeMode(freeMode);
                this.m_recipeIngredientStack.clear();
                this.resetRecipeIngredientStackProperty();
                this.checkMatchingRecipe();
                for (final Item item : WakfuGameEntity.getInstance().getLocalPlayer().getBags().getAllItems()) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(item, "usedInCurrentRecipe", "movable", "deletable");
                }
                return false;
            }
            case 16846: {
                final UIDragItemToCraftMessage msg2 = (UIDragItemToCraftMessage)message;
                final int referenceId = msg2.getRefId();
                final IngredientView ingredientView = this.m_recipeIngredientStack.get(referenceId);
                if (ingredientView != null) {
                    this.m_recipeIngredientStack.remove(referenceId);
                    this.resetRecipeIngredientStackProperty();
                    this.checkMatchingRecipe();
                    refreshDisplayedInventoryItem(referenceId);
                }
                this.highLightIngredientSlots(true);
                return false;
            }
            case 16845: {
                if (PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
                    return false;
                }
                final UIDropOutIngredientFromCraftMessage msg3 = (UIDropOutIngredientFromCraftMessage)message;
                final IngredientView ingredientView2 = msg3.getIngredientView();
                final int refId = ingredientView2.getReferenceItem().getId();
                final short quantity = msg3.getShortValue();
                if (quantity == -1 || ingredientView2.getQuantity() == 1) {
                    this.m_recipeIngredientStack.remove(refId);
                }
                else {
                    this.m_recipeIngredientStack.put(refId, new IngredientView((short)(ingredientView2.getQuantity() - 1), ingredientView2.getReferenceItem()));
                }
                this.checkMatchingRecipe();
                this.resetRecipeIngredientStackProperty();
                refreshDisplayedInventoryItem(refId);
                return false;
            }
            case 16844: {
                final UIItemMessage itemMessage = (UIItemMessage)message;
                final Item item2 = itemMessage.getItem();
                if (!item2.getReferenceItem().isCraftEnabled() || PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
                    return false;
                }
                final int refId = item2.getReferenceId();
                final IngredientView ingredientView3 = this.m_recipeIngredientStack.get(refId);
                short quantity2 = itemMessage.getQuantity();
                if (ingredientView3 != null) {
                    if (quantity2 != 1) {
                        UICraftTableFrame.m_logger.error((Object)"on a encore un item de m\u00eame type dans la liste des ingr\u00e9dients !");
                        return false;
                    }
                    final int quantityToAdd = ingredientView3.getQuantity() + 1;
                    if (WakfuGameEntity.getInstance().getLocalPlayer().getBags().getQuantityForRefId(refId, false) < quantityToAdd) {
                        return false;
                    }
                    quantity2 = (short)quantityToAdd;
                }
                this.m_recipeIngredientStack.put(refId, new IngredientView((quantity2 == -1) ? item2.getQuantity() : quantity2, (ReferenceItem)item2.getReferenceItem()));
                this.highLightIngredientSlots(false);
                this.checkMatchingRecipe();
                this.resetRecipeIngredientStackProperty();
                refreshDisplayedInventoryItem(refId);
                return false;
            }
            case 16847: {
                if (PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
                    return false;
                }
                final UISelectRecipeMessage msg4 = (UISelectRecipeMessage)message;
                RecipeView recipeView = msg4.getRecipeView();
                if (!recipeView.getCraftRecipe().isValid(WakfuGameEntity.getInstance().getLocalPlayer())) {
                    return false;
                }
                recipeView.setCanAfford(WakfuGameEntity.getInstance().getLocalPlayer().canAffordRecipe(recipeView.getCraftRecipe()));
                final RecipeView oldSelected = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
                this.clearIngredientStackAndRefresh();
                for (final IngredientView ingredientView4 : recipeView.getIngredients()) {
                    final int refId2 = ingredientView4.getReferenceItem().getId();
                    this.m_recipeIngredientStack.put(refId2, ingredientView4);
                    refreshDisplayedInventoryItem(refId2);
                }
                this.resetRecipeIngredientStackProperty();
                if (recipeView == oldSelected) {
                    recipeView = null;
                }
                PropertiesProvider.getInstance().setPropertyValue("selectedRecipe", recipeView);
                this.m_remainingCrafts = 1;
                PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", this.m_remainingCrafts);
                return false;
            }
            case 16840: {
                final UIStartCraftMessage msg5 = (UIStartCraftMessage)message;
                if (PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
                    return false;
                }
                if (!this.isThereEnoughSpaceForRecipe()) {
                    msg5.getButton().setSelected(false);
                    final String errMsg = WakfuTranslator.getInstance().getString("craft.error.inventoryFull");
                    final MessageBoxData data = new MessageBoxData(102, 0, errMsg, 2L);
                    Xulor.getInstance().msgBox(data);
                }
                else {
                    (this.m_progressBar = msg5.getBar()).setValue(0.0f);
                    this.m_button = msg5.getButton();
                    this.sendCurrentCraftRequest();
                }
                return false;
            }
            case 16850: {
                if (!PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
                    return false;
                }
                this.requestCancelCraft();
                return false;
            }
            case 16848: {
                PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", null);
                final AbstractUIMessage uiMessage = (AbstractUIMessage)message;
                this.m_remainingCrafts = uiMessage.getIntValue();
                final RecipeView recipeView2 = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
                if (recipeView2 != null) {
                    final int maxNumber = WakfuGameEntity.getInstance().getLocalPlayer().getMaxRecipeNumber(recipeView2.getCraftRecipe());
                    if (this.m_remainingCrafts > maxNumber) {
                        this.m_remainingCrafts = maxNumber;
                    }
                }
                if (this.m_remainingCrafts == 0) {
                    this.m_remainingCrafts = 1;
                }
                PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", this.m_remainingCrafts);
                return false;
            }
            case 16849: {
                this.setCraftNumberToMax();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void clearIngredientStackAndRefresh() {
        final int[] refIds = this.m_recipeIngredientStack.keys();
        this.m_recipeIngredientStack.clear();
        for (final int refId : refIds) {
            refreshDisplayedInventoryItem(refId);
        }
    }
    
    private static void refreshDisplayedInventoryItem(final int referenceId) {
        for (final Item item : WakfuGameEntity.getInstance().getLocalPlayer().getBags().getAllWithReferenceId(referenceId)) {
            PropertiesProvider.getInstance().firePropertyValueChanged(item, "usedInCurrentRecipe", "movable", "deletable");
        }
    }
    
    private void checkMatchingRecipe() {
        PropertiesProvider.getInstance().setPropertyValue("selectedRecipe", this.getRecipeMatchingIngredientStack());
    }
    
    private void setCraftNumberToMax() {
        PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", null);
        final Object obj = PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientBagContainer bags = localPlayer.getBags();
        if (obj != null) {
            final RecipeView recipeView = (RecipeView)obj;
            this.m_remainingCrafts = Math.max(1, localPlayer.getMaxRecipeNumber(recipeView.getCraftRecipe()));
        }
        else if (this.m_recipeIngredientStack.isEmpty()) {
            this.m_remainingCrafts = 0;
        }
        else {
            int min = Integer.MAX_VALUE;
            final TIntObjectIterator<IngredientView> it = this.m_recipeIngredientStack.iterator();
            while (it.hasNext()) {
                it.advance();
                final int value = bags.getItemQuantity(it.key()) / it.value().getQuantity();
                if (value < min) {
                    min = value;
                }
            }
            this.m_remainingCrafts = min;
        }
        PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", this.m_remainingCrafts);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setCraftTable(final CraftInteractiveElement wakfuClientMapInteractiveElement, final boolean freeMode) {
        PropertiesProvider.getInstance().setPropertyValue("craftTableName", wakfuClientMapInteractiveElement.getName());
        (this.m_craftView = new CraftView(wakfuClientMapInteractiveElement.getCraftId(), wakfuClientMapInteractiveElement)).setRecipeFilter(new RecipeByLevelFilter(this.m_craftView, 10));
        this.m_recipeIngredientStack = new TIntObjectHashMap<IngredientView>();
        this.resetRecipeIngredientStackProperty();
        this.m_freeMode = freeMode;
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    private void resetRecipeIngredientStackProperty() {
        PropertiesProvider.getInstance().setPropertyValue("recipeIngredientStack", this.m_recipeIngredientStack.isEmpty() ? null : this.m_recipeIngredientStack.getValues());
    }
    
    public boolean isItemUsedInCurrentRecipe(final int referenceId) {
        return this.m_recipeIngredientStack.containsKey(referenceId);
    }
    
    public void highLightIngredientSlots(final boolean highlight) {
        final EditableRenderableCollection list = (EditableRenderableCollection)this.m_dialogElementMap.getElement("ingredientList");
        for (final RenderableContainer renderableContainer : list.getRenderables()) {
            final Widget container = (Widget)renderableContainer.getInnerElementMap().getElement("ingredientBackgroundContainer");
            container.setStyle(highlight ? "itemSelectedBackground" : "itemBackground");
        }
    }
    
    private RecipeView getRecipeMatchingIngredientStack() {
        for (final RecipeView recipeView : this.m_craftView.getRecipeViews()) {
            if (this.m_recipeIngredientStack.size() != recipeView.getIngredients().size()) {
                continue;
            }
            boolean match = true;
            for (final IngredientView ingredientView : recipeView.getIngredients()) {
                final IngredientView ingredientView2 = this.m_recipeIngredientStack.get(ingredientView.getReferenceItem().getId());
                if (ingredientView2 == null || ingredientView2.getQuantity() != ingredientView.getQuantity()) {
                    match = false;
                    break;
                }
            }
            if (match) {
                PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", 1);
                return recipeView;
            }
        }
        if (!this.m_recipeIngredientStack.isEmpty()) {
            PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", 1);
        }
        return null;
    }
    
    public void startCraft(final long duration) {
        if (this.m_progressBar != null) {
            this.m_progressBar.setTweenDuration(duration);
            ProcessScheduler.getInstance().schedule(this.m_progressBarRunnable, 100L, 1);
        }
        this.m_craftView.getCraftTable().setState((short)2);
        this.m_craftView.getCraftTable().notifyViews();
        final RecipeView recipeView = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
        if (recipeView != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final CraftHandler craftHandler = localPlayer.getCraftHandler();
            final short craftLevel = craftHandler.getLevel(this.m_craftView.getCraftReferenceId());
            final long xp = craftHandler.getXp(this.m_craftView.getCraftReferenceId());
            final long xpGain = recipeView.getCraftRecipe().getXPGain(craftLevel, localPlayer);
            final float percentModificator = (100 + localPlayer.getCharacteristicValue(FighterCharacteristicType.OCCUPATION_QUICK_LEARNER_CRAFT) + localPlayer.getSkillCharacteristics().getCraftCharacteristicEfficiency(CraftSkillType.CRAFT_CRAFT_XP_BOOST, this.m_craftView.getCraftReferenceId())) / 100.0f;
            this.m_craftView.setNextXp(xp + Math.round(xpGain * percentModificator));
        }
    }
    
    public void stopCraft() {
        if (this.m_progressBar != null) {
            this.m_progressBar.setValue(0.0f);
        }
        RecipeView recipeView = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
        if (recipeView != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(recipeView, recipeView.getFields());
            final CraftRecipe craftRecipe = recipeView.getCraftRecipe();
            final ArrayList<RecipeView> views = this.m_craftView.getRecipeViews();
            for (int i = 0, size = views.size(); i < size; ++i) {
                final RecipeView testView = views.get(i);
                if (testView.getCraftRecipe().getId() == craftRecipe.getId()) {
                    recipeView = testView;
                    PropertiesProvider.getInstance().setPropertyValue("selectedRecipe", testView);
                }
            }
        }
        this.m_craftView.onRecipeDone();
        --this.m_remainingCrafts;
        final boolean continueCrafting = this.m_remainingCrafts > 0 && this.isThereEnoughSpaceForRecipe();
        if (continueCrafting) {
            if (recipeView != null) {
                recipeView.setCanAfford(true);
            }
            this.sendCurrentCraftRequest();
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("craftRunning", false);
            final ClientBagContainer bagContainer = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
            final Collection<Integer> refIdsToRemove = new ArrayList<Integer>();
            boolean recipeValid = true;
            final TIntObjectIterator<IngredientView> it = this.m_recipeIngredientStack.iterator();
            while (it.hasNext()) {
                it.advance();
                final int refId = it.key();
                final short quantity = it.value().getQuantity();
                if (bagContainer.getItemQuantity(refId) - quantity < quantity) {
                    refIdsToRemove.add(refId);
                    recipeValid = false;
                }
            }
            if (recipeView != null) {
                recipeView.setCanAfford(recipeValid);
            }
            final Iterator i$ = refIdsToRemove.iterator();
            while (i$.hasNext()) {
                final int refId = i$.next();
                this.m_recipeIngredientStack.remove(refId);
                refreshDisplayedInventoryItem(refId);
            }
            this.resetRecipeIngredientStackProperty();
            if (recipeValid && !this.m_recipeIngredientStack.isEmpty()) {
                this.m_remainingCrafts = 1;
            }
            else {
                this.m_remainingCrafts = 0;
            }
            this.m_craftView.getCraftTable().setState((short)1);
            this.m_craftView.getCraftTable().notifyViews();
        }
        PropertiesProvider.getInstance().setPropertyValue("currentCraftNb", this.m_remainingCrafts);
    }
    
    private boolean isThereEnoughSpaceForRecipe() {
        final RecipeView recipeView = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
        final CraftRecipe craftRecipe = (recipeView != null) ? recipeView.getCraftRecipe() : null;
        final ClientBagContainer bagContainer = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
        final boolean bagsAreFull = bagContainer.getNbFreePlaces() == 0;
        boolean canStackItem = false;
        if (bagsAreFull) {
            final RecipeResultItem[] resultItems = (RecipeResultItem[])((craftRecipe != null) ? craftRecipe.getResultItems() : null);
            if (resultItems == null || resultItems.length != 1) {
                canStackItem = false;
            }
            else {
                final RecipeResultItem resultItem = resultItems[0];
                final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(resultItem.getItemId());
                if (referenceItem == null) {
                    canStackItem = false;
                }
                else {
                    final Item item = new Item();
                    item.initializeWithReferenceItem(referenceItem);
                    item.setQuantity(resultItem.getQuantity());
                    final AbstractBag bag = bagContainer.getFirstBagWithStackablePlace(item);
                    canStackItem = (bag != null);
                }
            }
        }
        final boolean recipeIsAutoConsume = craftRecipe != null && craftRecipe.hasProperty(CraftRecipeProperty.AUTO_CONSUME_ITEM);
        return !bagsAreFull || canStackItem || recipeIsAutoConsume;
    }
    
    public void requestCancelCraft() {
        final AbstractOccupation occ = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
        if (occ != null) {
            ((CraftOccupation)occ).requestCancel();
        }
        else {
            UICraftTableFrame.m_logger.error((Object)"On tente d'annuler le craft alors que le joueur n'a pas d'occupation !");
        }
        this.initializeUi();
    }
    
    public static void cancelCraft() {
        WakfuGameEntity.getInstance().getLocalPlayer().cancelCurrentOccupation(false, false);
        getInstance().m_craftView.setNextXp(-1L);
    }
    
    public void initializeUi() {
        if (this.m_progressBar != null) {
            this.m_progressBar.setValue(0.0f);
        }
        ProcessScheduler.getInstance().remove(this.m_progressBarRunnable);
        PropertiesProvider.getInstance().setPropertyValue("craftRunning", false);
        this.m_craftView.getCraftTable().setState((short)1);
        this.m_craftView.getCraftTable().notifyViews();
    }
    
    private void sendCurrentCraftRequest() {
        final RecipeView recipeView = (RecipeView)PropertiesProvider.getInstance().getObjectProperty("selectedRecipe");
        if (recipeView != null) {
            final Message startCraftRequestMessage = new StartCraftRequestMessage(this.m_craftView.getCraftTable().getId(), recipeView.getCraftRecipe().getId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(startCraftRequestMessage);
        }
        else {
            final CraftRecipe recipe = this.getRecipeFromIngredients();
            byte recipeType;
            int recipeId;
            if (recipe == null) {
                recipeType = -1;
                recipeId = 0;
            }
            else if (!this.m_craftView.getCraftTable().containsRecipe(recipe.getId())) {
                recipeType = -2;
                recipeId = recipe.getId();
            }
            else {
                recipeType = 0;
                recipeId = recipe.getId();
            }
            final StartSecretCraftRequestMessage startSecretCraftRequestMessage = new StartSecretCraftRequestMessage(this.m_craftView.getCraftTable().getId(), recipeId, recipeType);
            if (recipeType == -1) {
                final TIntObjectIterator<IngredientView> it = this.m_recipeIngredientStack.iterator();
                while (it.hasNext()) {
                    it.advance();
                    startSecretCraftRequestMessage.addIngredient(it.key(), it.value().getQuantity());
                }
            }
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(startSecretCraftRequestMessage);
        }
        PropertiesProvider.getInstance().setPropertyValue("craftRunning", true);
    }
    
    public CraftRecipe getRecipeFromIngredients() {
        final TIntObjectIterator<CraftRecipe> it = CraftManager.INSTANCE.getCraft(this.m_craftView.getCraftReferenceId()).recipesIterator();
        while (it.hasNext()) {
            it.advance();
            final CraftRecipe craftRecipe = it.value();
            if (!craftRecipe.hasProperty(CraftRecipeProperty.SECRET)) {
                continue;
            }
            boolean found = true;
            final TIntShortIterator it2 = craftRecipe.ingredientsIterator();
            while (it2.hasNext()) {
                it2.advance();
                final IngredientView ingredientView = this.m_recipeIngredientStack.get(it2.key());
                if (ingredientView == null || ingredientView.getQuantity() != it2.value()) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return craftRecipe;
            }
        }
        return null;
    }
    
    public int getRecipeIdFromIngredients() {
        final TIntObjectIterator<CraftRecipe> it = CraftManager.INSTANCE.getCraft(this.m_craftView.getCraftReferenceId()).recipesIterator();
        while (it.hasNext()) {
            it.advance();
            final CraftRecipe craftRecipe = it.value();
            if (!craftRecipe.hasProperty(CraftRecipeProperty.SECRET)) {
                continue;
            }
            boolean found = true;
            final TIntShortIterator it2 = craftRecipe.ingredientsIterator();
            while (it2.hasNext()) {
                it2.advance();
                final IngredientView ingredientView = this.m_recipeIngredientStack.get(it2.key());
                if (ingredientView == null || ingredientView.getQuantity() != it2.value()) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return craftRecipe.getId();
            }
        }
        return -1;
    }
    
    private void highLightLackingIngredients(final RecipeView recipeView) {
        final EditableRenderableCollection list = (EditableRenderableCollection)this.m_dialogElementMap.getElement("recipeList");
        final RenderableContainer renderable = list.getRenderables().get(list.getItemIndex(recipeView));
        final List ingredients = (List)renderable.getInnerElementMap().getElement("ingredientList");
        for (final Object item : ingredients.getItems()) {
            final IngredientView ingredientView = (IngredientView)item;
            if (!ingredientView.isPossessed()) {
                final Image image = (Image)ingredients.getRenderableByOffset(ingredients.getItemIndex(ingredientView)).getInnerElementMap().getElement("icon");
                final Color c = new Color(1.0f, 0.5f, 0.5f, 1.0f);
                final Color c2 = image.getModulationColor();
                final AbstractTween t = new ModulationColorTween(c, c2, image, 0, 250, 3, TweenFunction.PROGRESSIVE);
                image.addTween(t);
            }
        }
    }
    
    @Override
    public void onCraftLearned(final ReferenceCraft refCraft) {
        throw new UnsupportedOperationException("aucune raison de notifier la crafttableFrame d'un m\u00e9tier appris");
    }
    
    @Override
    public void onCraftXpGained(final int craftId, final long xpAdded) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final long xp = localPlayer.getCraftHandler().getXp(craftId);
        final long previousXp = xp - xpAdded;
        if (CraftXPUtil.getCurrentLevel(xp) != CraftXPUtil.getCurrentLevel(previousXp)) {
            localPlayer.displayCraftLevelGainedParticle();
            this.m_craftView.onCraftLevelChanged();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_craftView, "currentXpPercentage", "level", "levelText");
    }
    
    @Override
    public void onRecipeLearned(final int craftId, final int recipeId) {
    }
    
    public void applyCraftButtonAps(final String fileName) {
        if (this.m_button == null) {
            return;
        }
        final ParticleDecorator particleDecorator = new ParticleDecorator();
        particleDecorator.onCheckOut();
        particleDecorator.setAlignment(Alignment9.CENTER);
        particleDecorator.setLevel(1);
        particleDecorator.setFile(fileName);
        particleDecorator.setUseParentScissor(true);
        particleDecorator.setRemovable(false);
        this.m_button.getAppearance().add(particleDecorator);
    }
    
    public int getCraftId() {
        return this.m_craftView.getCraftReferenceId();
    }
    
    public void onCraftSuccess(final boolean perfect) {
        WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler().onRecipeSuccess(UICraftTableFrame.m_instance.getCraftId());
        this.applyCraftButtonAps(perfect ? "6001045.xps" : "6001038.xps");
        this.stopCraft();
    }
    
    public void onDespawn(final CraftInteractiveElement elem) {
        if (elem == this.m_craftView.getCraftTable()) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    @Override
    public void pathStarted(final PathMobile mobile, final PathFindResult path) {
        WakfuGameEntity.getInstance().removeFrame(UICraftTableFrame.m_instance);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UICraftTableFrame.class);
        m_instance = new UICraftTableFrame();
    }
}
