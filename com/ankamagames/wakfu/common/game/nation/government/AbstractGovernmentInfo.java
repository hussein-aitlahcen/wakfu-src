package com.ankamagames.wakfu.common.game.nation.government;

import com.ankamagames.wakfu.common.game.characterInfo.*;

public abstract class AbstractGovernmentInfo extends AbstractCharacterData
{
    protected int m_citizenScore;
    protected float m_wakfuGauge;
    
    public AbstractGovernmentInfo(final long id, final String name, final short breedId, final byte sex, final long guildId, final String guildName, final int citizenScore, final float wakfuGauge, final CharacterDataAppearance appearance) {
        super(id, name, breedId, sex, guildId, guildName, appearance);
        this.m_citizenScore = citizenScore;
        this.m_wakfuGauge = wakfuGauge;
    }
    
    public int getCitizenScore() {
        return this.m_citizenScore;
    }
    
    public float getWakfuGauge() {
        return this.m_wakfuGauge;
    }
}
