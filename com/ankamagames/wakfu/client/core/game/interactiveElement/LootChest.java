package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.*;

public class LootChest extends WakfuClientMapInteractiveElement implements ChaosInteractiveElement, OccupationInteractiveElement
{
    protected static final Logger m_logger;
    protected IELootChestParameter m_param;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        LootChest.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.sendActionMessage(action);
        this.runScript(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.CHALLENGE_ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.CHALLENGE_ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.m_param == null) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        if (this.m_state == 1) {
            final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_param.getVisualId());
            final MRULootChestAction action = MRUActions.LOOT_CHEST_ACTION.getMRUAction();
            action.setGfxId(visual.getMruGfx());
            action.setTextKey("desc.mru." + visual.getMruLabelKey());
            this.checkCriterionValidity(action);
            action.setCostText(this.getCostText());
            action.setCanPay(this.canPay());
            final AbstractMRUAction[] mRUActions = { action };
            return mRUActions;
        }
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    public static boolean canPay(final int itemId, final int consumedItemQty, final int consumedKamas) {
        final AbstractReferenceItem ref = ReferenceItemManager.getInstance().getReferenceItem(itemId);
        final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
        if (ref != null && user.getBags().getQuantityForRefId(itemId) < consumedItemQty) {
            final QuestInventory questInventory = (QuestInventory)user.getInventory(InventoryType.QUEST);
            final QuestItem questItem = questInventory.getItem(itemId);
            if (questItem == null || questItem.getQuantity() < consumedItemQty) {
                return false;
            }
        }
        return user.getKamasCount() >= consumedKamas;
    }
    
    public static String getCostText(final int itemId, final int consumedItemQty, final int consumedKamas) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final AbstractReferenceItem ref = ReferenceItemManager.getInstance().getReferenceItem(itemId);
        final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
        if (ref != null) {
            boolean badQuantity = (consumedItemQty != 1 || ((ArrayInventoryWithoutCheck<Item, R>)user.getEquipmentInventory()).getFirstWithReferenceId(itemId) == null) && user.getBags().getQuantityForRefId(itemId) < consumedItemQty;
            final QuestInventory questInventory = (QuestInventory)user.getInventory(InventoryType.QUEST);
            final QuestItem questItem = questInventory.getItem(itemId);
            badQuantity &= (questItem == null || questItem.getQuantity() < consumedItemQty);
            sb.openText().addColor((badQuantity ? Color.RED : Color.GREEN).getRGBtoHex());
            sb.append("[").append(ref.getName()).append("]").append(" x").append(consumedItemQty);
            sb.closeText();
        }
        if (consumedKamas > 0) {
            if (ref != null) {
                sb.newLine();
            }
            sb.openText().addColor(((user.getKamasCount() < consumedKamas) ? Color.RED : Color.GREEN).getRGBtoHex());
            sb.append(WakfuTranslator.getInstance().getString("kama.shortGain", consumedKamas)).closeText();
        }
        return sb.finishAndToString();
    }
    
    public static void checkCriterionValidity(final WakfuClientMapInteractiveElement ie, final MRULootChestAction action, final SimpleCriterion criterion) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (criterion != null && !criterion.isValid(localPlayer, ie, null, localPlayer.getAppropriateContext())) {
            action.setEnabled(false);
            action.addErrorText(CriterionDescriptionGenerator.getDescription(criterion));
        }
    }
    
    private boolean canPay() {
        return this.m_param != null && canPay(this.m_param.getItemIdCost(), this.m_param.getItemQuantityCost(), this.m_param.getCost());
    }
    
    private String getCostText() {
        if (this.m_param == null) {
            return null;
        }
        return getCostText(this.m_param.getItemIdCost(), this.m_param.getItemQuantityCost(), this.m_param.getCost());
    }
    
    private void checkCriterionValidity(final MRULootChestAction action) {
        if (this.m_param == null) {
            return;
        }
        checkCriterionValidity(this, action, this.m_param.getCriterion());
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final QuestInventory questInventory = (QuestInventory)localPlayer.getInventory(InventoryType.QUEST);
        if (this.getId() == 14641L && questInventory.getItem(12787) != null) {
            action.setEnabled(false);
            action.addErrorText(WakfuTranslator.getInstance().getString("lootChest.cantDrop"));
        }
    }
    
    @Override
    public ActionVisual getVisual() {
        return ActionVisualManager.getInstance().get(this.m_param.getVisualId());
    }
    
    @Override
    public short getUsedState() {
        return 3;
    }
    
    @Override
    public short getMRUHeight() {
        return (short)(this.getHeight() * 10.0f);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setOldState((short)(-32768));
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setUseSpecificAnimTransition(true);
        this.setUsingSpecificOldState(true);
        this.setOverHeadable(true);
        assert this.m_param == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_param = null;
    }
    
    @Override
    public String getName() {
        if (this.m_param == null) {
            return null;
        }
        String name = WakfuTranslator.getInstance().getString(85, this.m_param.getId(), new Object[0]);
        if (this.m_param.getCost() > 0) {
            name = name + " (" + this.m_param.getCost() + "§)";
        }
        if (this.m_param.hasItemCost()) {
            name = name + " (" + this.m_param.getItemQuantityCost() + " x " + WakfuTranslator.getInstance().getString(15, this.m_param.getItemIdCost(), new Object[0]) + ')';
        }
        return name;
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        this.m_param = (IELootChestParameter)IEParametersManager.INSTANCE.getParam(IETypes.LOOT_CHEST, Integer.parseInt(this.m_parameter));
        if (this.m_param == null) {
            LootChest.m_logger.error((Object)("pas de param(paramId=" + this.m_parameter + ") trouv\u00e9 pour le lootChest " + this.getId()), (Throwable)new Exception());
        }
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public void runDespawnAnimation() {
        super.runDespawnAnimation();
        this.m_state = 0;
        this.notifyViews();
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_param;
    }
    
    @Override
    public String toString() {
        return "LootChest{m_param=" + this.m_param + '}';
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new LootChestItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LootChest.class);
    }
    
    public static class LootChestFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool POOL;
        
        @Override
        public LootChest makeObject() {
            LootChest element;
            try {
                element = (LootChest)LootChestFactory.POOL.borrowObject();
                element.setPool(LootChestFactory.POOL);
            }
            catch (Exception e) {
                LootChest.m_logger.error((Object)"Erreur lors de l'extraction d'un LootChest du pool", (Throwable)e);
                element = new LootChest();
            }
            return element;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<LootChest>() {
                @Override
                public LootChest makeObject() {
                    return new LootChest();
                }
            });
        }
    }
}
