package com.ankamagames.wakfu.common.game.nation.Laws;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public abstract class NationLaw<E extends NationLawEvent>
{
    protected static final Logger m_logger;
    private final long m_id;
    private final int m_basePointsModification;
    private final int m_lawPointCost;
    private final boolean m_locked;
    private final int m_lawApplication;
    private int m_percentPointsModification;
    
    protected NationLaw(final long id, final int basePointsModification, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super();
        this.m_id = id;
        this.m_basePointsModification = basePointsModification;
        this.m_lawPointCost = lawPointCost;
        this.m_locked = lawLocked;
        this.m_lawApplication = NationLawApplication.bitSet(lawApplications);
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public int getBasePointsModification() {
        return this.m_basePointsModification;
    }
    
    public int getLawPointCost() {
        return this.m_lawPointCost;
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    public void setPercentPointsModification(final int percentPointsModification) {
        this.m_percentPointsModification = percentPointsModification;
    }
    
    public int getPercentPointsModification() {
        return this.m_percentPointsModification;
    }
    
    public boolean isApplicableToCitizen() {
        return this.isApplicableTo(NationLawApplication.CITIZEN);
    }
    
    public boolean isApplicableToAlliedForeigner() {
        return this.isApplicableTo(NationLawApplication.ALLIED_FOREIGNER);
    }
    
    public boolean isApplicableToNeutralForeigner() {
        return this.isApplicableTo(NationLawApplication.NEUTRAL_FOREIGNER);
    }
    
    private boolean isApplicableTo(final NationLawApplication application) {
        return (this.m_lawApplication & application.mask) == application.mask;
    }
    
    public abstract void initialize(final List<ParserObject> p0);
    
    public abstract NationLawModelConstant getModel();
    
    public abstract boolean isTriggering(final E p0);
    
    public static List<NationLaw> getGoodLaws(final List<NationLaw> laws) {
        if (laws == null || laws.isEmpty()) {
            return (List<NationLaw>)Collections.emptyList();
        }
        for (int i = laws.size() - 1; i >= 0; --i) {
            final NationLaw law = laws.get(i);
            if (law.getBasePointsModification() < 0) {
                laws.remove(law);
            }
        }
        return laws;
    }
    
    public static List<NationLaw> getBadLaws(final List<NationLaw> laws) {
        if (laws == null || laws.isEmpty()) {
            return (List<NationLaw>)Collections.emptyList();
        }
        for (int i = laws.size() - 1; i >= 0; --i) {
            final NationLaw law = laws.get(i);
            if (law.getBasePointsModification() > 0) {
                laws.remove(law);
            }
        }
        return laws;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationLaw.class);
    }
}
