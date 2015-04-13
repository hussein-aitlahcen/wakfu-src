package com.ankamagames.wakfu.client.core.game.aptitudenew;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class AptitudeBonusCategoryView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String REMAINING_POINTS = "remainingPoints";
    public static final String NEXT_LEVEL_GAIN = "nextLevelGain";
    public static final String BONUS_LIST = "bonusList";
    public static final String BUTTON_STYLE = "buttonStyle";
    private final AptitudeCategoryModel m_model;
    private final InactiveAptitudeBonusInventory m_inventory;
    private final ArrayList<AptitudeBonusView> m_aptitudes;
    private static final Comparator<AptitudeBonusView> APTITUDE_BONUS_VIEW_COMPARATOR;
    
    public AptitudeBonusCategoryView(final AptitudeCategoryModel model, final InactiveAptitudeBonusInventory inventory) {
        super();
        this.m_aptitudes = new ArrayList<AptitudeBonusView>();
        this.m_model = model;
        this.m_inventory = inventory;
        for (final ClientAptitudeBonusModel bonus : this.m_model.getBonusSet()) {
            this.m_aptitudes.add(new AptitudeBonusView(inventory, bonus));
        }
        Collections.sort(this.m_aptitudes, AptitudeBonusCategoryView.APTITUDE_BONUS_VIEW_COMPARATOR);
    }
    
    public int getId() {
        return this.m_model.getId();
    }
    
    public int getFirstActivationLevel() {
        return this.m_model.getFirstActivationLevel();
    }
    
    @Override
    public String[] getFields() {
        return AptitudeBonusCategoryView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(147, this.m_model.getId(), new Object[0]);
        }
        if (fieldName.equals("remainingPoints")) {
            return this.availablePointsForCategory();
        }
        if (fieldName.equals("nextLevelGain")) {
            final short level = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
            final int nextLevel = this.m_model.getNextActivationLevel(level);
            if (nextLevel == -1) {
                return null;
            }
            return WakfuTranslator.getInstance().getString("aptitude.nextLevelActivation", nextLevel);
        }
        else {
            if (fieldName.equals("buttonStyle")) {
                return "Aptitude" + this.m_model.getId();
            }
            if (fieldName.equals("bonusList")) {
                return this.m_aptitudes;
            }
            return null;
        }
    }
    
    public int availablePointsForCategory() {
        return this.m_inventory.getAvailablePointsForCategory(this.m_model.getId());
    }
    
    public void fireAllChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingPoints");
        for (final AptitudeBonusView bonus : this.m_aptitudes) {
            bonus.fireLevelChanged();
        }
    }
    
    public void fireAvailablePointsChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingPoints");
        for (final AptitudeBonusView bonus : this.m_aptitudes) {
            bonus.fireAvailablePointsChanged();
        }
    }
    
    static {
        APTITUDE_BONUS_VIEW_COMPARATOR = new Comparator<AptitudeBonusView>() {
            @Override
            public int compare(final AptitudeBonusView o1, final AptitudeBonusView o2) {
                return Integer.valueOf(o1.getId()).compareTo(Integer.valueOf(o2.getId()));
            }
        };
    }
}
