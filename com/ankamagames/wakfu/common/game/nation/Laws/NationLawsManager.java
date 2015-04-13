package com.ankamagames.wakfu.common.game.nation.Laws;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.diplomacy.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class NationLawsManager
{
    private static final TLongHashSet EMPTY_SET;
    private final TLongObjectProcedure<NationLaw> FILL_ACTIVE_LAWS_WITH_LOCKED;
    private final TLongProcedure FILL_ACTIVE_LAW_PROCEDURE;
    protected Nation m_nation;
    private List<NationJusticeEventHandler> m_eventHandlers;
    protected final TLongObjectHashMap<NationLaw> m_laws;
    protected final TIntObjectHashMap<TLongHashSet> m_lawsByEventType;
    protected final TLongHashSet m_activeLaws;
    private boolean m_firstChange;
    
    public NationLawsManager() {
        super();
        this.FILL_ACTIVE_LAWS_WITH_LOCKED = new TLongObjectProcedure<NationLaw>() {
            @Override
            public boolean execute(final long id, final NationLaw law) {
                if (law.isLocked()) {
                    NationLawsManager.this.m_activeLaws.add(id);
                }
                return true;
            }
        };
        this.FILL_ACTIVE_LAW_PROCEDURE = new TLongProcedure() {
            @Override
            public boolean execute(final long value) {
                NationLawsManager.this.m_activeLaws.add(value);
                return true;
            }
        };
        this.m_laws = new TLongObjectHashMap<NationLaw>();
        this.m_lawsByEventType = new TIntObjectHashMap<TLongHashSet>();
        this.m_activeLaws = new TLongHashSet();
        this.m_firstChange = false;
    }
    
    public void register(final NationLaw law) {
        final long lawId = law.getId();
        if (this.m_laws.containsKey(lawId)) {
            return;
        }
        this.m_laws.put(lawId, law);
        final int eventTypeIdx = law.getModel().listenedEventType.ordinal();
        TLongHashSet laws = this.m_lawsByEventType.get(eventTypeIdx);
        if (laws == null) {
            this.m_lawsByEventType.put(eventTypeIdx, laws = new TLongHashSet());
        }
        laws.add(lawId);
        if (law.isLocked()) {
            this.m_activeLaws.add(lawId);
        }
    }
    
    public void onGovernorElected() {
        this.m_firstChange = true;
    }
    
    public boolean isFirstChange() {
        return this.m_firstChange;
    }
    
    public boolean setFirstLawChangeUsed() {
        final boolean r = this.m_firstChange;
        this.m_firstChange = false;
        return r;
    }
    
    public void onEvent(final NationLawEvent event) {
        throw new UnsupportedOperationException();
    }
    
    public ArrayList<NationLaw> getTriggeringLaws(final NationLawEvent event) {
        final ArrayList<NationLaw> triggered = new ArrayList<NationLaw>();
        final TLongHashSet laws = this.m_lawsByEventType.get(event.getEventType().ordinal());
        if (laws == null) {
            return triggered;
        }
        for (final long lawId : laws) {
            if (!this.m_activeLaws.contains(lawId)) {
                continue;
            }
            final NationLaw law = this.m_laws.get(lawId);
            if (!this.isLawApplicable(law, event)) {
                continue;
            }
            if (!law.isTriggering(event)) {
                continue;
            }
            triggered.add(law);
        }
        return triggered;
    }
    
    protected boolean isLawApplicable(final NationLaw law, final NationLawEvent event) {
        final BasicCharacterInfo citizen = (BasicCharacterInfo)event.getCitizen();
        final int citizenNationId = citizen.getCitizenComportment().getNationId();
        if (citizenNationId == 0) {
            return false;
        }
        final boolean isCitizen = citizen.getTravellingNationId() == citizenNationId;
        final NationDiplomacyManager citizenDiplomacy = citizen.getCitizenComportment().getNation().getDiplomacyManager();
        final NationAlignement alignment = citizenDiplomacy.getAlignment(citizen.getTravellingNationId());
        switch (alignment) {
            case ENEMY: {
                final boolean isNegativeLaw = law.getBasePointsModification() <= 0;
                return !NationPvpHelper.isPvpActive(citizen.getCitizenComportment()) && law.isApplicableToNeutralForeigner() && isNegativeLaw;
            }
            case ALLIED: {
                return isCitizen ? law.isApplicableToCitizen() : law.isApplicableToAlliedForeigner();
            }
            default: {
                throw new UnsupportedOperationException("Type d'alignement non reconnu " + alignment);
            }
        }
    }
    
    public TLongObjectIterator<NationLaw> lawsIterator() {
        return (TLongObjectIterator<NationLaw>)this.m_laws.iterator();
    }
    
    public int getCitizenPointsCost(final long lawId) {
        return this.m_laws.get(lawId).getBasePointsModification();
    }
    
    public int getCitizenPointsPercentCost(final long lawId) {
        return this.m_laws.get(lawId).getPercentPointsModification();
    }
    
    public boolean isActive(final long lawId) {
        return this.m_activeLaws.contains(lawId);
    }
    
    public void setActiveLawsToLocked() {
        this.setActiveLaws(NationLawsManager.EMPTY_SET);
    }
    
    public void removeAllLaws() {
        this.setActiveLaws(NationLawsManager.EMPTY_SET);
    }
    
    public void setActiveLaws(final TLongHashSet activatedLaws) {
        this.m_activeLaws.clear();
        this.m_laws.forEachEntry(this.FILL_ACTIVE_LAWS_WITH_LOCKED);
        activatedLaws.forEach(this.FILL_ACTIVE_LAW_PROCEDURE);
        if (this.m_nation != null && this.m_eventHandlers != null) {
            for (int i = 0; i < this.m_eventHandlers.size(); ++i) {
                this.m_eventHandlers.get(i).onLawsChanged(this.m_nation);
            }
        }
    }
    
    public void finishInitialization(final Nation nation, final List<NationJusticeEventHandler> eventHandlers) {
        this.m_nation = nation;
        this.m_eventHandlers = eventHandlers;
    }
    
    public int getActivationCost(final TLongHashSet laws) {
        int cost = 0;
        final TLongIterator it = laws.iterator();
        while (it.hasNext()) {
            cost += this.m_laws.get(it.next()).getLawPointCost();
        }
        return cost;
    }
    
    public void toRaw(final RawNationLaw raw) {
        raw.firstChange = this.m_firstChange;
        final TLongIterator it = this.m_activeLaws.iterator();
        while (it.hasNext()) {
            final RawNationLaw.Law rawLaw = new RawNationLaw.Law();
            rawLaw.lawId = it.next();
            raw.laws.add(rawLaw);
        }
    }
    
    public void fromRaw(final RawNationLaw raw) {
        this.m_firstChange = raw.firstChange;
        final TLongHashSet laws = new TLongHashSet();
        for (int i = 0, size = raw.laws.size(); i < size; ++i) {
            laws.add(raw.laws.get(i).lawId);
        }
        this.setActiveLaws(laws);
    }
    
    static {
        EMPTY_SET = new TLongHashSet();
    }
}
