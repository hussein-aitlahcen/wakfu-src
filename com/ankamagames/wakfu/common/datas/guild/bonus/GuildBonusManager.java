package com.ankamagames.wakfu.common.datas.guild.bonus;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;

public class GuildBonusManager
{
    private static final Logger m_logger;
    public static final GuildBonusManager INSTANCE;
    private final TIntObjectHashMap<GuildBonusDefinition> m_bonuses;
    
    private GuildBonusManager() {
        super();
        this.m_bonuses = new TIntObjectHashMap<GuildBonusDefinition>();
    }
    
    public void add(final GuildBonusDefinition bonus) {
        if (this.m_bonuses.contains(bonus.getId())) {
            throw new IllegalArgumentException("bonus d\u00e9j\u00e0 ins\u00e9r\u00e9 avec l'id " + bonus.getId());
        }
        this.m_bonuses.put(bonus.getId(), bonus);
    }
    
    public GuildBonusDefinition getBonus(final int id) {
        return this.m_bonuses.get(id);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildBonusManager.class);
        INSTANCE = new GuildBonusManager();
    }
}
