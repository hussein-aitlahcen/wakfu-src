package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.protector.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class FightLawEvent extends NationLawEvent
{
    private final FightModel m_fightModel;
    private final BasicCharacterInfo m_attacker;
    private final BasicCharacterInfo m_defender;
    @Nullable
    private final AbstractProtectorEcosystemHandler m_protectorEcosystemHandler;
    private final Collection<? extends BasicCharacterInfo> m_opponents;
    
    public FightLawEvent(final Citizen citizen, final FightModel fightModel, final BasicCharacterInfo attacker, final BasicCharacterInfo defender, final AbstractProtectorEcosystemHandler protectorEcosystemHandler, final Collection<? extends BasicCharacterInfo> opponents) {
        super(citizen);
        this.m_fightModel = fightModel;
        this.m_attacker = attacker;
        this.m_defender = defender;
        this.m_protectorEcosystemHandler = protectorEcosystemHandler;
        this.m_opponents = opponents;
    }
    
    @Nullable
    public AbstractProtectorEcosystemHandler getProtectorEcosystemHandler() {
        return this.m_protectorEcosystemHandler;
    }
    
    public FightModel getFightModel() {
        return this.m_fightModel;
    }
    
    public BasicCharacterInfo getAttacker() {
        return this.m_attacker;
    }
    
    public BasicCharacterInfo getDefender() {
        return this.m_defender;
    }
    
    public Collection<? extends BasicCharacterInfo> getOpponents() {
        return this.m_opponents;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.FIGHT;
    }
}
