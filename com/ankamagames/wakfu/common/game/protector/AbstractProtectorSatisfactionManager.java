package com.ankamagames.wakfu.common.game.protector;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;

public abstract class AbstractProtectorSatisfactionManager implements SatisfactionManager
{
    protected final ProtectorBase m_protector;
    protected final TIntObjectHashMap<Interval> m_monsterTargets;
    protected final TIntObjectHashMap<Interval> m_resourceTargets;
    private ProtectorSatisfactionListener m_satisfactionListener;
    protected int m_monsterSatisfaction;
    protected int m_resourceSatisfaction;
    protected ProtectorSatisfactionLevel m_globalSatisfaction;
    private static final Accumulator m_satisfactionHelper;
    
    public AbstractProtectorSatisfactionManager(final ProtectorBase protector) {
        super();
        this.m_protector = protector;
        this.m_monsterSatisfaction = -1;
        this.m_resourceSatisfaction = -1;
        this.m_globalSatisfaction = ProtectorSatisfactionLevel.UNDEFINED;
        this.m_monsterTargets = new TIntObjectHashMap<Interval>();
        this.m_resourceTargets = new TIntObjectHashMap<Interval>();
    }
    
    public void addMonsterTarget(final int familyId, final Interval interval) {
        this.m_monsterTargets.put(familyId, interval);
    }
    
    public void addResourceTarget(final int familyId, final Interval interval) {
        this.m_resourceTargets.put(familyId, interval);
    }
    
    public TIntObjectHashMap<Interval> getMonsterTargets() {
        return this.m_monsterTargets;
    }
    
    public TIntObjectHashMap<Interval> getResourceTargets() {
        return this.m_resourceTargets;
    }
    
    @Override
    public int getMonsterSatisfaction() {
        return this.m_monsterSatisfaction;
    }
    
    @Override
    public int getResourceSatisfaction() {
        return this.m_resourceSatisfaction;
    }
    
    @Override
    public ProtectorSatisfactionLevel getGlobalSatisfaction() {
        return this.m_globalSatisfaction;
    }
    
    public void setGlobalSatisfaction(final ProtectorSatisfactionLevel globalSatisfaction) {
        this.m_globalSatisfaction = globalSatisfaction;
    }
    
    public void setSatisfactionListener(final ProtectorSatisfactionListener satisfactionListener) {
        this.m_satisfactionListener = satisfactionListener;
    }
    
    public void updateMonsterSatisfaction(final TIntIntHashMap currentMonsterCount) {
        AbstractProtectorSatisfactionManager.m_satisfactionHelper.reset();
        this.m_monsterTargets.forEachEntry(new TIntObjectProcedure<Interval>() {
            @Override
            public boolean execute(final int familyId, final Interval target) {
                final int familyCount = currentMonsterCount.get(familyId);
                if (target.isIn(familyCount)) {
                    AbstractProtectorSatisfactionManager.m_satisfactionHelper.accumulate(1);
                }
                return true;
            }
        });
        final int satisfactionValue = AbstractProtectorSatisfactionManager.m_satisfactionHelper.getValue();
        if (satisfactionValue != this.m_monsterSatisfaction) {
            this.m_monsterSatisfaction = satisfactionValue;
            if (this.m_satisfactionListener != null) {
                this.m_satisfactionListener.onSatisfactionChanged(this.m_protector);
            }
            this.updateGlobalSatisfaction();
        }
    }
    
    public void updateResourceSatisfaction(final TIntIntHashMap currentResourceCount) {
        AbstractProtectorSatisfactionManager.m_satisfactionHelper.reset();
        this.m_resourceTargets.forEachEntry(new TIntObjectProcedure<Interval>() {
            @Override
            public boolean execute(final int familyId, final Interval target) {
                final int familyCount = currentResourceCount.get(familyId);
                if (target.isIn(familyCount)) {
                    AbstractProtectorSatisfactionManager.m_satisfactionHelper.accumulate(1);
                }
                return true;
            }
        });
        final int satisfactionValue = AbstractProtectorSatisfactionManager.m_satisfactionHelper.getValue();
        if (satisfactionValue != this.m_resourceSatisfaction) {
            this.m_resourceSatisfaction = satisfactionValue;
            if (this.m_satisfactionListener != null) {
                this.m_satisfactionListener.onSatisfactionChanged(this.m_protector);
            }
            this.updateGlobalSatisfaction();
        }
    }
    
    public void setMonsterSatisfaction(final int monsterSatisfaction) {
        this.m_monsterSatisfaction = monsterSatisfaction;
    }
    
    public void setResourceSatisfaction(final int resourceSatisfaction) {
        this.m_resourceSatisfaction = resourceSatisfaction;
    }
    
    protected abstract void updateGlobalSatisfaction();
    
    static {
        m_satisfactionHelper = new Accumulator();
    }
}
