package com.ankamagames.wakfu.common.game.climate;

import com.ankamagames.baseImpl.common.clientAndServer.world.climate.*;

public class ClimateBonusList
{
    private final int m_id;
    private final ClimateBonus[] m_bonus;
    
    public ClimateBonusList(final int id, final ClimateBonus[] bonus) {
        super();
        this.m_id = id;
        this.m_bonus = bonus;
    }
}
