package com.ankamagames.wakfu.common.datas.guild.level;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;
import gnu.trove.*;

public class GuildLevelManager
{
    private static final Logger m_logger;
    public static final int MAX_LEVEL = 10;
    public static final GuildLevelManager INSTANCE;
    private final GuildLevel[] m_levels;
    
    private GuildLevelManager() {
        super();
        this.m_levels = new GuildLevel[11];
    }
    
    public void add(@NotNull final GuildLevel level) {
        this.m_levels[level.getId()] = level;
    }
    
    public int getCost(final int level) {
        assert isLevelValid(level) : "Level de guilde invalide !";
        final GuildBonusDataAGT bonus = GuildHelper.getLevelBonusFor(level);
        if (bonus == null) {
            return 0;
        }
        return bonus.get().getCost();
    }
    
    public static boolean isLevelValid(final int level) {
        return level >= 0 && level <= 10;
    }
    
    public int getBonusUnlockLevel(final int bonus) {
        for (int i = 0; i <= 10; ++i) {
            final GuildLevel guildLevel = this.m_levels[i];
            if (guildLevel != null && guildLevel.containsBonusId(bonus)) {
                return guildLevel.getId();
            }
        }
        return 0;
    }
    
    public boolean isBonusUnlocked(final int bonusId, final int level) {
        final TIntArrayList bonusesUnlocked = this.getBonusesUnlocked(level);
        return bonusesUnlocked.contains(bonusId);
    }
    
    public TIntArrayList getBonusesUnlocked(final int level) {
        final TIntArrayList list = new TIntArrayList();
        for (int i = 0; i <= level; ++i) {
            if (this.m_levels[i] != null) {
                this.m_levels[i].addAllBonusesToList(list);
            }
        }
        return list;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildLevelManager.class);
        INSTANCE = new GuildLevelManager();
    }
}
