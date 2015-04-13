package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class LawView extends ImmutableFieldProvider implements Comparable
{
    public static final String NAME = "name";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String POINTS = "points";
    public static final String PERCENT_POINTS = "percentPoints";
    public static final String ICON_URL = "iconUrl";
    public static final String COST = "cost";
    public static final String DESCRIPTION = "description";
    public static final String ACTIVATED = "activated";
    public static final String IS_RIGHT = "isRight";
    public static final String LOCKED = "locked";
    public static final String[] FIELDS;
    private NationLaw m_law;
    private ActivationStatut m_activationStatut;
    
    @Override
    public int compareTo(final Object o) {
        if (o instanceof LawView) {
            final int pointCost = this.getLaw().getBasePointsModification();
            final LawView lawView = (LawView)o;
            final int citizenPointCost = lawView.getLaw().getBasePointsModification();
            return (pointCost == citizenPointCost) ? this.getName().compareTo(lawView.getName()) : (pointCost - citizenPointCost);
        }
        return 0;
    }
    
    public LawView(final NationLaw law) {
        super();
        this.m_activationStatut = ActivationStatut.NONE;
        this.m_law = law;
    }
    
    @Override
    public String[] getFields() {
        return LawView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("categoryName")) {
            if (this.m_law.getBasePointsModification() >= -5) {
                return WakfuTranslator.getInstance().getString("nation.lawMinorDeliquency");
            }
            if (this.m_law.getBasePointsModification() >= -20) {
                return WakfuTranslator.getInstance().getString("nation.lawMajorDeliquency");
            }
            return WakfuTranslator.getInstance().getString("nation.lawCrimeDeliquency");
        }
        else {
            if (fieldName.equals("points")) {
                final int pointCost = this.m_law.getBasePointsModification();
                return (pointCost > 0) ? ("+" + pointCost) : pointCost;
            }
            if (fieldName.equals("percentPoints")) {
                final int pointPercent = this.m_law.getPercentPointsModification();
                if (pointPercent == 0) {
                    return null;
                }
                return pointPercent + "%";
            }
            else {
                if (fieldName.equals("iconUrl")) {
                    return WakfuConfiguration.getInstance().getNationLawsIconUrl(this.m_law.getId());
                }
                if (fieldName.equals("cost")) {
                    return this.m_law.getLawPointCost();
                }
                if (fieldName.equals("description")) {
                    return WakfuTranslator.getInstance().getString(98, (int)this.m_law.getId(), new Object[0]);
                }
                if (fieldName.equals("activated")) {
                    if (ActivationStatut.NONE == this.m_activationStatut) {
                        return this.isActivated();
                    }
                    return this.isGovernorActivated();
                }
                else {
                    if (fieldName.equals("locked")) {
                        return this.m_law.isLocked();
                    }
                    if (fieldName.equals("isRight")) {
                        return this.m_law.getBasePointsModification() > 0;
                    }
                    return null;
                }
            }
        }
    }
    
    private String getName() {
        return WakfuTranslator.getInstance().getString(97, (int)this.m_law.getId(), new Object[0]);
    }
    
    private boolean isActivated() {
        return NationDisplayer.getInstance().getNation().getLawManager().isActive(this.m_law.getId());
    }
    
    public NationLaw getLaw() {
        return this.m_law;
    }
    
    public void setLaw(final NationLaw law) {
        this.m_law = law;
    }
    
    public boolean isGovernorActivated() {
        return this.m_activationStatut == ActivationStatut.ACTIVATED || (this.m_activationStatut == ActivationStatut.NONE && this.isActivated());
    }
    
    public void setActivationStatut(final boolean activate) {
        this.m_activationStatut = (activate ? ActivationStatut.ACTIVATED : ActivationStatut.DISACTIVATED);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "activated");
    }
    
    public void clean() {
        this.m_activationStatut = ActivationStatut.NONE;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "activated");
    }
    
    static {
        FIELDS = new String[] { "name", "categoryName", "points", "percentPoints", "iconUrl", "cost", "description", "activated", "isRight", "locked" };
    }
    
    private enum ActivationStatut
    {
        NONE, 
        DISACTIVATED, 
        ACTIVATED;
    }
}
