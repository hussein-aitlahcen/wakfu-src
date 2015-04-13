package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.dialog.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.protector.*;

public abstract class AbstractProtectorView implements ProtectorEventListener<ProtectorEvent>, FieldProvider
{
    public static final String ANIMATION_FIELD = "animation";
    public static final String ANIM_NAME_FIELD = "animName";
    public static final String CURRENT_PROTECTOR_FIELD = "currentProtector";
    public static final String CHALLENGE_INVENTORY_FIELD = "challengeInventory";
    public static final String BUFF_INVENTORY_FIELD = "buffInventory";
    public static final String CLIMATE_INVENTORY_FIELD = "climateInventory";
    public static final String CHALLENGE_INVENTORY_OBJ_FIELD = "challengeObjInventory";
    public static final String BUFF_INVENTORY_OBJ_FIELD = "buffObjInventory";
    public static final String CLIMATE_INVENTORY_OBJ_FIELD = "climateObjInventory";
    public static final String TAX_FIELD = "tax";
    public static final String WALLET_HANDLER_FIELD = "walletHandler";
    public static final String ZOOLOGY = "zoology";
    public static final String HAS_MONEY_BASE_CHALLENGES = "hasMoneyBaseChallenges";
    public static final String BASE_CHALLENGES_FIELD = "baseChallenges";
    public static final String AVAILABLE_CHALLENGES_FIELD = "availableChallenges";
    public static final String BOUGHT_BUFFS_FIELD = "boughtBuffs";
    public static final String BOUGHT_CHALLENGES_FIELD = "boughtChallenges";
    public static final String NUM_BOUGHT_CHALLENGES_FIELD = "numBoughtChallenges";
    public static final String NUM_AVAILABLE_CHALLENGES_FIELD = "numAvailableChallenges";
    public static final String BOUGHT_CLIMATES_FIELD = "boughtClimates";
    public static final String BUFF_LIST = "buffList";
    public static final String NUM_BUFF_LIST = "numBuffList";
    public static final String BUFF_TOOLTIP = "buffTooltip";
    public static final String BUFF_TITLE = "buffTitle";
    public static final String UNSATISFIED_REGIONAL_STATE_FIELD = "unsatisfiedRegionalState";
    public static final String HALF_SATISFIED_REGIONAL_STATE_FIELD = "halfSatisfiedRegionalState";
    public static final String SATISFIED_REGIONAL_STATE_FIELD = "satisfiedRegionalState";
    public static final String UNSATISFIED_GLOBAL_STATE_FIELD = "unsatisfiedGlobalState";
    public static final String HALF_SATISFIED_GLOBAL_STATE_FIELD = "halfSatisfiedGlobalState";
    public static final String SATISFIED_GLOBAL_STATE_FIELD = "satisfiedGlobalState";
    public static final String UNSATISFIED_STATE_ICON_FIELD = "unsatisfiedStateIcon";
    public static final String HALF_SATISFIED_STATE_ICON_FIELD = "halfSatisfiedStateIcon";
    public static final String SATISFIED_STATE_ICON_FIELD = "satisfiedStateIcon";
    public static final String GLOBAL_STATE_ICON_FIELD = "globalStateIcon";
    public static final String NATION_FIELD = "nation";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String IS_STATIC_FIELD = "isStatic";
    public static final String NAME_FIELD = "name";
    public static final String JOB_FIELD = "job";
    public static final String SEX_FIELD = "sex";
    public static final String HEIGHT_FIELD = "height";
    public static final String WEIGHT_FIELD = "weight";
    public static final String CUSTOM_DESCRIPTION_FIELD = "customDescription";
    public static final String SECRETS = "secrets";
    public static final String NUM_SECRETS = "numSecrets";
    public static final String UNLOCKED_SECRETS = "unlockedSecrets";
    public static final String TERRITORY_NAME = "territoryName";
    public static final String TERRITORY_RECOMMENDED_LEVEL = "territoryRecommendedLevel";
    public static final String ILLUSTRATION_URL = "illustrationUrl";
    public static final String[] FIELDS;
    public static final byte EVENT_TRANSLATION_TYPE_ID = 2;
    
    public void initialize() {
        ProtectorEventDispatcher.INSTANCE.addListener(this);
    }
    
    @Override
    public void onProtectorEvent(final ProtectorEvent event) {
    }
    
    @Override
    public String[] getFields() {
        return AbstractProtectorView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isStatic")) {
            return this.isStaticProtector();
        }
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(48, this.getProtectorId(), new Object[0]);
        }
        if (fieldName.equals("illustrationUrl")) {
            return WakfuConfiguration.getInstance().getInteractiveDialogPortraitIconUrl(DialogSourceType.PROTECTOR.getIllustrationId(this.getProtectorId()));
        }
        return null;
    }
    
    public abstract int getProtectorId();
    
    public abstract boolean isStaticProtector();
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    protected void updateProtectorFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, AbstractProtectorView.FIELDS);
    }
    
    @Nullable
    public AnimatedElement getAnimation() {
        return this.getAnimation(this.getProtectorId(), ProtectorMood.NEUTRAL);
    }
    
    @Nullable
    public abstract AnimatedElement getAnimation(final int p0, final ProtectorMood p1);
    
    @Nullable
    public abstract Territory getTerritory();
    
    static {
        FIELDS = new String[] { "boughtClimates", "boughtChallenges", "boughtBuffs", "currentProtector", "animation", "challengeInventory", "buffInventory", "climateInventory", "walletHandler", "challengeObjInventory", "buffObjInventory", "climateObjInventory", "nation", "description", "isStatic", "job", "sex", "height", "weight", "customDescription", "secrets", "buffList", "illustrationUrl", "territoryRecommendedLevel" };
    }
}
