package com.ankamagames.wakfu.client.core.game.exchangeMachine;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import java.util.*;

public class ExchangeEntryView extends ImmutableFieldProvider
{
    public static final String COST = "cost";
    public static final String KAMAS = "kamas";
    public static final String RESULT = "result";
    public static final String CRITERION = "criterion";
    public static final String LEVEL = "level";
    public static final String ENABLED = "enabled";
    public static final String PVP_MONEY = "pvpMoney";
    public static final String PVP_MONEY_DESC = "pvpMoneyDesc";
    public static final String[] FIELDS;
    private static final IntIntLightWeightMap m_tokenCost;
    private static final int BRONZE_TOKEN_ID = 13126;
    private static final int SILVER_TOKEN_ID = 13127;
    private static final int GOLD_TOKEN_ID = 13128;
    private ArrayList<IngredientView> m_consumables;
    private ArrayList<IngredientView> m_resultings;
    private IEExchangeParameter.Exchange m_exchange;
    
    public ExchangeEntryView(final IEExchangeParameter.Exchange exchange) {
        super();
        this.m_consumables = new ArrayList<IngredientView>();
        this.m_resultings = new ArrayList<IngredientView>();
        exchange.forEachConsumable(new TIntShortProcedure() {
            @Override
            public boolean execute(final int a, final short b) {
                final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(a);
                if (referenceItem != null) {
                    ExchangeEntryView.this.m_consumables.add(new IngredientView(b, (ReferenceItem)referenceItem));
                }
                return true;
            }
        });
        exchange.forEachResulting(new TObjectProcedure<IEExchangeParameter.Exchange.Resulting>() {
            @Override
            public boolean execute(final IEExchangeParameter.Exchange.Resulting object) {
                final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(object.getItemId());
                if (referenceItem != null) {
                    ExchangeEntryView.this.m_resultings.add(new IngredientView(object.getQuantity(), (ReferenceItem)referenceItem));
                }
                return true;
            }
        });
        this.m_exchange = exchange;
    }
    
    @Override
    public String[] getFields() {
        return ExchangeEntryView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("cost")) {
            return this.getCost();
        }
        if (fieldName.equals("kamas")) {
            final int kamas = this.m_exchange.getConsumableKamas();
            return (kamas > 0) ? kamas : null;
        }
        if (fieldName.equals("pvpMoney")) {
            final int pvpMoney = this.m_exchange.getConsumablePvpMoney();
            return (pvpMoney > 0) ? pvpMoney : null;
        }
        if (fieldName.equals("pvpMoneyDesc")) {
            final int pvpMoney = this.m_exchange.getConsumablePvpMoney();
            return pvpMoney + "x " + WakfuTranslator.getInstance().getString("pvpCoins");
        }
        if (fieldName.equals("criterion")) {
            return this.getCriterionText();
        }
        if (fieldName.equals("level")) {
            final IngredientView resulting = this.getResulting();
            if (resulting == null) {
                return null;
            }
            final short level = resulting.getReferenceItem().getLevel();
            return WakfuTranslator.getInstance().getString("levelShort.custom", level);
        }
        else {
            if (fieldName.equals("enabled")) {
                return this.canAffordCost();
            }
            if (fieldName.equals("result")) {
                return this.getResulting();
            }
            return null;
        }
    }
    
    @Nullable
    private String getCriterionText() {
        final SimpleCriterion criterion = this.m_exchange.getCriterion();
        if (criterion == null) {
            return null;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean invalid = !criterion.isValid(localPlayer, this, this.m_exchange, localPlayer.getAppropriateContext());
        final String criterionString = CriterionDescriptionGenerator.getDescription(criterion);
        final TextWidgetFormater desc = new TextWidgetFormater();
        if (invalid) {
            desc.openText().addColor(new Color(0.8f, 0.0f, 0.0f, 1.0f));
        }
        desc.append(criterionString);
        if (invalid) {
            desc.closeText();
        }
        return desc.finishAndToString();
    }
    
    private ArrayList<IngredientView> getCost() {
        return this.m_consumables;
    }
    
    private boolean canAffordCost() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final SimpleCriterion criterion = this.m_exchange.getCriterion();
        if (criterion != null && !criterion.isValid(localPlayer, this, this.m_exchange, localPlayer.getAppropriateContext())) {
            return false;
        }
        final boolean cantAffordCost = !this.m_exchange.forEachConsumable(new TIntShortProcedure() {
            @Override
            public boolean execute(final int a, final short b) {
                if (localPlayer.getBags().getQuantityForRefId(a) >= b) {
                    return true;
                }
                final QuestInventory questInventory = (QuestInventory)localPlayer.getInventory(InventoryType.QUEST);
                final QuestItem questItem = questInventory.getItem(a);
                return questItem != null && questItem.getQuantity() >= b;
            }
        });
        return !cantAffordCost && localPlayer.getWallet().getAmountOfCash() >= this.m_exchange.getConsumableKamas() && localPlayer.getCitizenComportment().getPvpMoneyAmount() >= this.m_exchange.getConsumablePvpMoney();
    }
    
    @Nullable
    public IngredientView getResulting() {
        if (this.m_resultings.isEmpty()) {
            return null;
        }
        return this.m_resultings.get(0);
    }
    
    public IEExchangeParameter.Exchange getExchange() {
        return this.m_exchange;
    }
    
    public int getOrder() {
        return this.m_exchange.getOrder();
    }
    
    public int getCostTotalSize() {
        int total = 0;
        for (final IngredientView ingredientView : this.m_consumables) {
            final int tokenCost = ExchangeEntryView.m_tokenCost.get(ingredientView.getReferenceItem().getId());
            total += ingredientView.getQuantity() * ((tokenCost == 0) ? 1 : tokenCost);
        }
        return total;
    }
    
    static {
        FIELDS = new String[] { "cost", "kamas", "result", "criterion", "level", "pvpMoney", "pvpMoneyDesc", "enabled" };
        (m_tokenCost = new IntIntLightWeightMap()).put(13126, 1);
        ExchangeEntryView.m_tokenCost.put(13127, 10);
        ExchangeEntryView.m_tokenCost.put(13128, 100);
    }
}
