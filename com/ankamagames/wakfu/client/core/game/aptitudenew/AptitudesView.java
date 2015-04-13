package com.ankamagames.wakfu.client.core.game.aptitudenew;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.aptitude.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class AptitudesView extends ImmutableFieldProvider implements AptitudeBonusInventoryListener
{
    public static final String CATEGORIES = "categories";
    public static final String CURRENT_CATEGORY = "currentCategory";
    public static final String[] FIELDS;
    public static final AptitudesView INSTANCE;
    private InactiveAptitudeBonusInventory m_inventory;
    private final ArrayList<AptitudeBonusCategoryView> m_categories;
    private final TIntObjectHashMap<AptitudeBonusCategoryView> m_categoriesById;
    private AptitudeBonusCategoryView m_currentCategory;
    private TIntShortHashMap m_modifications;
    private Comparator<AptitudeBonusCategoryView> m_categoryViewComparator;
    
    public AptitudesView() {
        super();
        this.m_categories = new ArrayList<AptitudeBonusCategoryView>();
        this.m_categoriesById = new TIntObjectHashMap<AptitudeBonusCategoryView>();
        this.m_modifications = new TIntShortHashMap();
        this.m_categoryViewComparator = new Comparator<AptitudeBonusCategoryView>() {
            @Override
            public int compare(final AptitudeBonusCategoryView o1, final AptitudeBonusCategoryView o2) {
                return Integer.valueOf(o1.getFirstActivationLevel()).compareTo(Integer.valueOf(o2.getFirstActivationLevel()));
            }
        };
    }
    
    public void reset() {
        this.reset(WakfuGameEntity.getInstance().getLocalPlayer().getAptitudeBonusInventory());
    }
    
    public void reset(final AptitudeBonusInventory inventory) {
        this.m_categories.clear();
        this.m_categoriesById.clear();
        (this.m_inventory = inventory.getInactiveCopy()).addListener(this);
        this.m_modifications.clear();
        AptitudeCategoryModelManager.INSTANCE.forEachCategory(new TObjectProcedure<AptitudeCategoryModel>() {
            @Override
            public boolean execute(final AptitudeCategoryModel object) {
                final AptitudeBonusCategoryView category = new AptitudeBonusCategoryView(object, AptitudesView.this.m_inventory);
                AptitudesView.this.m_categories.add(category);
                AptitudesView.this.m_categoriesById.put(category.getId(), category);
                return true;
            }
        });
        Collections.sort(this.m_categories, this.m_categoryViewComparator);
        this.m_currentCategory = this.m_categories.get(0);
    }
    
    @Override
    public String[] getFields() {
        return AptitudesView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("categories")) {
            return this.m_categories;
        }
        if (fieldName.equals("currentCategory")) {
            return this.m_currentCategory;
        }
        return null;
    }
    
    @Override
    public void onLevelChanged(final int bonusId, final short level) {
    }
    
    private void resetPoints() {
        this.m_inventory.reset();
        this.m_modifications.clear();
        for (final AptitudeBonusCategoryView c : this.m_categories) {
            c.fireAllChanged();
        }
    }
    
    public void selectCategory(final AptitudeBonusCategoryView category) {
        if (this.m_currentCategory == category) {
            return;
        }
        this.m_currentCategory = category;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentCategory");
    }
    
    public void incrementBonus(final AptitudeBonusView bonus) {
        if (!bonus.canBeIncreased()) {
            return;
        }
        this.m_inventory.addLevel(bonus.getId(), (short)1);
        this.m_inventory.removePointsFor(bonus.getId(), (short)1);
        this.m_modifications.adjustOrPutValue(bonus.getId(), (short)1, (short)1);
        bonus.fireLevelChanged();
        final AptitudeBonusCategoryView category = this.m_categoriesById.get(AptitudeCategoryModelManager.INSTANCE.getBonusCategoryId(bonus.getId()));
        category.fireAvailablePointsChanged();
    }
    
    public void decrementBonus(final AptitudeBonusView bonus) {
        if (!bonus.canBeDecreased()) {
            return;
        }
        final int categoryId = AptitudeCategoryModelManager.INSTANCE.getBonusCategoryId(bonus.getId());
        this.m_inventory.addLevel(bonus.getId(), (short)(-1));
        this.m_inventory.incPointFor(categoryId);
        final short previousValue = this.m_modifications.get(bonus.getId());
        if (previousValue == 1) {
            this.m_modifications.remove(bonus.getId());
        }
        else {
            this.m_modifications.put(bonus.getId(), (short)(previousValue - 1));
        }
        bonus.fireLevelChanged();
        final AptitudeBonusCategoryView category = this.m_categoriesById.get(categoryId);
        category.fireAvailablePointsChanged();
    }
    
    public void validateChanges() {
        final LevelUpNewAptitudeRequestMessage msg = new LevelUpNewAptitudeRequestMessage();
        msg.setAptitudeModifications(this.m_modifications);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void resetChanges() {
        this.resetPoints();
    }
    
    public boolean hasNoModifications() {
        return this.m_modifications.isEmpty();
    }
    
    public boolean hasAvailablePoints() {
        for (final AptitudeBonusCategoryView categoryView : this.m_categories) {
            if (categoryView.availablePointsForCategory() > 0) {
                return true;
            }
        }
        return false;
    }
    
    static {
        FIELDS = new String[] { "categories", "currentCategory" };
        INSTANCE = new AptitudesView();
    }
}
