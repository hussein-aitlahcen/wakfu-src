package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;

public class ProtectorChallengeMerchantInventoryView extends ImmutableFieldProvider
{
    public static final String CATEGORIES = "categories";
    public static final String SELECTED_CATEGORY = "selectedCategory";
    public static final String HAS_CHALLENGES = "hasChallenges";
    private final ProtectorMerchantInventory m_challengeInventory;
    private final IntObjectLightWeightMap<ProtectorChallengeCategoryView> m_categoryViews;
    private ChallengeCategory m_selected;
    
    public ProtectorChallengeMerchantInventoryView(final ProtectorMerchantInventory challengeInventory) {
        super();
        this.m_categoryViews = new IntObjectLightWeightMap<ProtectorChallengeCategoryView>();
        this.m_selected = null;
        this.m_challengeInventory = challengeInventory;
        for (final ChallengeCategory cat : ChallengeCategory.values()) {
            this.m_categoryViews.put(cat.getId(), new ProtectorChallengeCategoryView(cat));
        }
        this.update();
    }
    
    public void setSelected(final ChallengeCategory cat) {
        this.m_selected = cat;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedCategory");
    }
    
    public void update() {
        for (int i = 0, size = this.m_categoryViews.size(); i < size; ++i) {
            final ProtectorChallengeCategoryView value = this.m_categoryViews.getQuickValue(i);
            value.clear();
        }
        for (final ProtectorMerchantInventoryItem item : this.m_challengeInventory) {
            if (item.getStartDate() != -1L) {
                continue;
            }
            final ProtectorChallengeItemView view = (ProtectorChallengeItemView)item.getView();
            final AbstractChallengeView challengeDataView = ChallengeViewManager.INSTANCE.getChallengeView(item.getFeatureReferenceId());
            final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(challengeDataView.getChallengeId());
            final ChallengeCategory category = model.getCategory();
            final ProtectorChallengeCategoryView cat = this.m_categoryViews.get(category.getId());
            cat.addItem(view);
        }
        final ProtectorChallengeCategoryView oldSelectedView = (this.m_selected != null) ? this.m_categoryViews.get(this.m_selected.getId()) : null;
        final ChallengeCategory oldSelected = this.m_selected;
        this.m_selected = null;
        if (oldSelectedView != null && oldSelectedView.isEnabled()) {
            this.m_selected = oldSelected;
            PropertiesProvider.getInstance().firePropertyValueChanged(oldSelectedView, "items");
        }
        else {
            for (int j = 0, size2 = this.m_categoryViews.size(); j < size2; ++j) {
                final ProtectorChallengeCategoryView value2 = this.m_categoryViews.getQuickValue(j);
                if (value2.isEnabled()) {
                    this.m_selected = value2.getCategory();
                    break;
                }
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "categories", "selectedCategory");
    }
    
    public void updateTime() {
        for (final ProtectorChallengeCategoryView cat : this.m_categoryViews) {
            cat.updateTime();
        }
    }
    
    public void onWalletUpdated() {
        for (int i = 0, size = this.m_categoryViews.size(); i < size; ++i) {
            this.m_categoryViews.getQuickValue(i).onWalletUpdated();
        }
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("selectedCategory")) {
            return (this.m_selected != null) ? this.m_categoryViews.get(this.m_selected.getId()) : null;
        }
        if (fieldName.equals("categories")) {
            return this.m_categoryViews;
        }
        if (fieldName.equals("hasChallenges")) {
            return this.m_challengeInventory.size() != 0;
        }
        return null;
    }
}
