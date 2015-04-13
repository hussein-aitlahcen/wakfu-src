package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;

public final class ToExternalFightMigrator
{
    private final Fight m_fightToMigrate;
    
    public ToExternalFightMigrator(final Fight fightToMigrate) {
        super();
        this.m_fightToMigrate = fightToMigrate;
    }
    
    public ExternalFightInfo migrateFightToExternal() {
        final ExternalFightInfo info = new ExternalFightInfo(this.m_fightToMigrate.getModel());
        info.setId(this.m_fightToMigrate.getId());
        info.setStatus(this.m_fightToMigrate.getStatus());
        final FightMap fightMap = this.m_fightToMigrate.getFightMap();
        info.setFightMap(fightMap);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        for (final CharacterInfo charac : this.m_fightToMigrate.getAllFighters()) {
            if (charac != localPlayer) {
                info.addFighter(charac, charac.getTeamId());
            }
            charac.getActor().clearActiveParticleSystem();
        }
        final Collection<BasicEffectArea> areas = this.m_fightToMigrate.getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(area);
        }
        StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(this.m_fightToMigrate.getBattlegroundBorderEffectArea());
        final Timeline timeline = this.m_fightToMigrate.getTimeline();
        if (timeline != null) {
            timeline.stop();
            timeline.clearTimeEvents();
        }
        this.m_fightToMigrate.destroyFight();
        fightMap.blockFightingGroundInTopology(true, false);
        FightManager.getInstance().addFight(info);
        for (final CharacterInfo charac2 : info.getFighters()) {
            charac2.setFight(-1);
            final TimedRunningEffectManager rem = charac2.getRunningEffectManager();
            for (final RunningEffect effect : rem) {
                effect.setContext(info.getContext());
            }
            charac2.setFighterFieldProvider(null);
            charac2.setCurrentExternalFightInfo(info);
            charac2.setFight(info.getId());
        }
        return info;
    }
}
