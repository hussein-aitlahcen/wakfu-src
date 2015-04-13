package com.ankamagames.wakfu.common.datas.guild.level;

import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import org.apache.commons.lang3.*;
import gnu.trove.*;

public class GuildLevel
{
    private final int m_id;
    private final int[] m_bonusesUnlocked;
    
    public GuildLevel(final int id, final int... bonusesUnlocked) {
        super();
        this.m_id = id;
        this.m_bonusesUnlocked = GuildHelper.checkBonusListIntegrity(bonusesUnlocked);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean containsBonusId(final int bonus) {
        return ArrayUtils.contains(this.m_bonusesUnlocked, bonus);
    }
    
    public void addAllBonusesToList(final TIntArrayList bonuses) {
        bonuses.add(this.m_bonusesUnlocked);
    }
}
