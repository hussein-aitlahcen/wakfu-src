package com.ankamagames.wakfu.common.datas.guild;

import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;

public class GuildDataManager
{
    public static final GuildDataManager INSTANCE;
    
    public int getCreationKamaCost() {
        return 10000;
    }
    
    public int getCreationItemId() {
        return 16942;
    }
    
    public void forEachQuest(final TIntProcedure procedure) {
        for (final int questId : GuildConstantAGT.GUILD_QUESTS) {
            if (!procedure.execute(questId)) {
                return;
            }
        }
    }
    
    public int getDefaultMaxSimultaneousBonus() {
        return 1;
    }
    
    public int getBonusMaxSimultaneousBonus() {
        return 2;
    }
    
    public double getBonusLearningFactor() {
        return 0.9;
    }
    
    static {
        INSTANCE = new GuildDataManager();
    }
}
