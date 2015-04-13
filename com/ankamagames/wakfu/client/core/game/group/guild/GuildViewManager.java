package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;

public class GuildViewManager
{
    public static final GuildViewManager INSTANCE;
    private Guild m_guild;
    private final TIntObjectHashMap<GuildBonusView> m_bonusViews;
    
    private GuildViewManager() {
        super();
        this.m_bonusViews = new TIntObjectHashMap<GuildBonusView>();
    }
    
    public void init() {
        final GuildLocalInformationHandler guildHandler = (GuildLocalInformationHandler)WakfuGameEntity.getInstance().getLocalPlayer().getGuildHandler();
        this.m_guild = guildHandler.getGuild();
        this.createBonuses();
    }
    
    public void clean() {
        this.m_bonusViews.forEachValue(new TObjectProcedure<GuildBonusView>() {
            @Override
            public boolean execute(final GuildBonusView object) {
                object.removeFromTimeManager();
                return true;
            }
        });
        this.m_bonusViews.clear();
    }
    
    private void createBonuses() {
        for (final GuildBonusDataAGT data : GuildBonusDataAGT.values()) {
            final GuildBonusDefinition bonus = data.get();
            this.m_bonusViews.put(bonus.getId(), new GuildBonusView(bonus));
        }
    }
    
    public GuildBonusView getBonusView(final int bonusId) {
        return this.m_bonusViews.get(bonusId);
    }
    
    public boolean forEachBonusView(final TObjectProcedure<GuildBonusView> procedure) {
        return this.m_bonusViews.forEachValue(procedure);
    }
    
    static {
        INSTANCE = new GuildViewManager();
    }
}
