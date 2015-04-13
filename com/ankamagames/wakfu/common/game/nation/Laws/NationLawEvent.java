package com.ankamagames.wakfu.common.game.nation.Laws;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;

public abstract class NationLawEvent
{
    private static final Logger m_logger;
    private final Citizen m_citizen;
    
    protected NationLawEvent(final Citizen citizen) {
        super();
        this.m_citizen = citizen;
    }
    
    public Citizen getCitizen() {
        return this.m_citizen;
    }
    
    public List<NationLaw> getTriggeringLaws() {
        final BasicCharacterInfo citizen = (BasicCharacterInfo)this.m_citizen;
        final Nation travellingNation = citizen.getTravellingNation();
        final NationAlignement alignment = citizen.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(travellingNation.getNationId());
        if (alignment == NationAlignement.ENEMY && NationPvpHelper.isPvpActive(citizen.getCitizenComportment())) {
            return (List<NationLaw>)Collections.emptyList();
        }
        return travellingNation.getLawManager().getTriggeringLaws(this);
    }
    
    public void fire() {
        final BasicCharacterInfo citizen = (BasicCharacterInfo)this.m_citizen;
        final Nation travellingNation = citizen.getTravellingNation();
        if (travellingNation == null) {
            return;
        }
        final NationAlignement alignment = citizen.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(travellingNation.getNationId());
        if (alignment == NationAlignement.ENEMY && NationPvpHelper.isPvpActive(citizen.getCitizenComportment())) {
            return;
        }
        try {
            travellingNation.getLawManager().onEvent(this);
        }
        catch (Exception e) {
            NationLawEvent.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public abstract NationLawEventType getEventType();
    
    static {
        m_logger = Logger.getLogger((Class)NationLawEvent.class);
    }
}
