package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import java.text.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.buff.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ProtectorInListView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String TERRITORY_NAME = "territoryName";
    public static final String CASH = "cash";
    public static final String TAXES = "taxes";
    public static final String SATISFACTION = "satisfaction";
    public static final String ANIMATION_FIELD = "animation";
    public static final String ANIM_NAME_FIELD = "animName";
    public static final String BUFFS_FIELD = "buffs";
    public static final String SHUKRUTE_BUFFS_FIELD = "shukruteBuffs";
    public static final String IN_CHAOS = "inChaos";
    private NationProtectorInfo m_nationProtectorInfo;
    private Protector m_protector;
    private AnimatedElementWithDirection m_animation;
    private ArrayList m_satisfaction;
    public static final String[] FIELDS;
    
    public ProtectorInListView(final NationProtectorInfo nationProtectorInfo) {
        super();
        this.m_nationProtectorInfo = nationProtectorInfo;
        this.m_protector = ProtectorManager.INSTANCE.getStaticProtector(nationProtectorInfo.getId());
        if (this.m_protector.getCurrentNation() != this.m_nationProtectorInfo.getNation()) {
            this.m_protector.setCurrentNation(this.m_nationProtectorInfo.getNation());
        }
    }
    
    @Override
    public String[] getFields() {
        return ProtectorInListView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            final String protectorName = WakfuTranslator.getInstance().getString(48, this.getProtectorId(), new Object[0]);
            return protectorName + (this.m_nationProtectorInfo.isInChaos() ? (" " + WakfuTranslator.getInstance().getString("chaos.label")) : "");
        }
        if (fieldName.equals("territoryName")) {
            return this.m_protector.getTerritoryName();
        }
        if (fieldName.equals("cash")) {
            return NumberFormat.getIntegerInstance().format(this.getCash()) + " §";
        }
        if (fieldName.equals("satisfaction")) {
            if (this.m_satisfaction == null) {
                final int totalSatisfaction = this.m_nationProtectorInfo.getTotalSatisfaction();
                final int currentSatisfaction = this.m_nationProtectorInfo.getCurrentSatisfaction();
                this.m_satisfaction = new ArrayList(totalSatisfaction);
                for (int i = 0; i < totalSatisfaction; ++i) {
                    this.m_satisfaction.add(i < currentSatisfaction);
                }
            }
            return this.m_satisfaction;
        }
        if (fieldName.equals("animation")) {
            return this.getAnimation();
        }
        if (fieldName.equals("animName")) {
            return this.getAnimName();
        }
        if (fieldName.equals("taxes")) {
            return (int)(this.getTaxValue() * 100.0f) + " %";
        }
        if (fieldName.equals("buffs")) {
            final TIntArrayList knownBuffs = this.m_protector.getKnownBuffsList();
            final ArrayList<ProtectorBuffView> views = new ArrayList<ProtectorBuffView>();
            final ProtectorSatisfactionLevel satisfactionLevel = ProtectorSatisfactionLevel.fromTargets(this.m_nationProtectorInfo.getCurrentSatisfaction(), this.m_nationProtectorInfo.getTotalSatisfaction());
            this.m_protector.getSatisfactionManager().setGlobalSatisfaction(satisfactionLevel);
            if (satisfactionLevel == ProtectorSatisfactionLevel.UNDEFINED) {
                if (this.m_protector.getNationBuffs() == null) {
                    return null;
                }
                for (final ProtectorBuff buff : this.m_protector.getNationBuffs()) {
                    if (buff.getOrigin() == BuffOrigin.MDC) {
                        views.add(new ProtectorBuffView(buff, this.m_protector, false));
                    }
                }
            }
            else {
                final ArrayList<ProtectorBuff> buffs = new ArrayList<ProtectorBuff>();
                for (int j = 0, size = knownBuffs.size(); j < size; ++j) {
                    final ProtectorBuff buff = ProtectorBuffManager.INSTANCE.getBuff(knownBuffs.get(j));
                    if (buff != null && buff.getOrigin() == BuffOrigin.MDC) {
                        buffs.add(buff);
                    }
                }
                views.add(new ProtectorBuffView(null, this.m_protector, knownBuffs.size() != 0));
                for (int j = 0, size = buffs.size(); j < size; ++j) {
                    final ProtectorBuff buff = buffs.get(j);
                    views.add(new ProtectorBuffView(buff, this.m_protector));
                }
                Collections.sort(views, ProtectorBuffView.ProtectorBuffViewComparator.INSTANCE);
            }
            return views;
        }
        if (fieldName.equals("shukruteBuffs")) {
            final ArrayList<ProtectorBuffView> views2 = new ArrayList<ProtectorBuffView>();
            final ProtectorSatisfactionLevel satisfactionLevel2 = ProtectorSatisfactionLevel.fromTargets(this.m_nationProtectorInfo.getCurrentSatisfaction(), this.m_nationProtectorInfo.getTotalSatisfaction());
            this.m_protector.getSatisfactionManager().setGlobalSatisfaction(satisfactionLevel2);
            if (this.m_protector.getNationBuffs() == null) {
                return null;
            }
            for (final ProtectorBuff buff2 : this.m_protector.getNationBuffs()) {
                if (buff2.getOrigin() == BuffOrigin.SHUKRUTE) {
                    views2.add(new ProtectorBuffView(buff2, this.m_protector, false));
                }
            }
            return views2;
        }
        else {
            if (fieldName.equals("inChaos")) {
                return this.m_nationProtectorInfo.isInChaos();
            }
            return null;
        }
    }
    
    public int getCash() {
        return this.m_nationProtectorInfo.getCash();
    }
    
    public float getSatisfactionRatio() {
        return (this.m_nationProtectorInfo.getTotalSatisfaction() == 0) ? -1.0f : (this.m_nationProtectorInfo.getCurrentSatisfaction() / this.m_nationProtectorInfo.getTotalSatisfaction());
    }
    
    public int getTotalSatisfaction() {
        return this.m_nationProtectorInfo.getTotalSatisfaction();
    }
    
    public boolean isSatisfied() {
        return this.getTotalSatisfaction() == 0 || this.getTotalSatisfaction() == this.m_nationProtectorInfo.getCurrentSatisfaction();
    }
    
    public int getCurrentSatisfaction() {
        return this.m_nationProtectorInfo.getCurrentSatisfaction();
    }
    
    public float getTaxValue() {
        return this.m_nationProtectorInfo.getFleaTaxValue();
    }
    
    public int getProtectorId() {
        return this.m_nationProtectorInfo.getId();
    }
    
    public ProtectorMood getCurrentMood() {
        return ProtectorMood.getMoodFromSatisfaction(ProtectorSatisfactionLevel.fromTargets(this.m_nationProtectorInfo.getCurrentSatisfaction(), this.m_nationProtectorInfo.getTotalSatisfaction()));
    }
    
    public String getAnimName() {
        return this.getCurrentMood().getAnimation();
    }
    
    public AnimatedElementWithDirection getAnimation() {
        if (this.m_animation == null) {
            final AnimatedElementWithDirection element = new AnimatedElementWithDirection(GUIDGenerator.getGUID(), 0.0f, 0.0f, 0.0f);
            final String fileName = this.m_nationProtectorInfo.getId() + ".anm";
            try {
                element.load(WakfuConfiguration.getInstance().getString("ANMGUIPath") + fileName, true);
            }
            catch (IOException e) {
                return null;
            }
            catch (PropertyException e2) {
                return null;
            }
            element.setGfxId(fileName);
            this.m_animation = element;
        }
        this.m_animation.setStaticAnimationKey(this.getAnimName());
        this.m_animation.setAnimation(this.getAnimName());
        return this.m_animation;
    }
    
    public String getTerritoryName() {
        return this.m_protector.getTerritoryName();
    }
    
    static {
        FIELDS = new String[] { "name", "cash", "taxes", "satisfaction", "buffs", "shukruteBuffs", "inChaos" };
    }
}
