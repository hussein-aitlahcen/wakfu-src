package com.ankamagames.wakfu.client.core.game.fight;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import gnu.trove.*;

public class FightManager
{
    protected static final Logger m_logger;
    private static final FightManager m_instance;
    protected final TIntObjectHashMap<FightInfo> m_fights;
    
    public FightManager() {
        super();
        this.m_fights = new TIntObjectHashMap<FightInfo>();
    }
    
    public static FightManager getInstance() {
        return FightManager.m_instance;
    }
    
    public Fight createFight(final int typeId, final int fightId, final FightMap fightMap, final TByteHashSet lockedTeams, final List<byte[]> serializedEffectArea) {
        if (fightId == -1) {
            throw new IllegalArgumentException("fightId = FightConstants.NO_FIGHT_ID - On doit toujours passer l'id du fight sur le client.");
        }
        final FightModel model = FightModel.getFromTypeId(typeId);
        final Fight fight = new Fight(fightId, model, fightMap, lockedTeams);
        this.initFight(fight);
        return fight;
    }
    
    public Fight createFight(final int typeId, final int fightId, final FightMap fightMap, final TByteHashSet lockedTeams, final byte fightStatus) {
        if (fightId == -1) {
            throw new IllegalArgumentException("fightId = FightConstants.NO_FIGHT_ID - On doit toujours passer l'id du fight sur le client.");
        }
        final FightModel model = FightModel.getFromTypeId(typeId);
        final AbstractFight.FightStatus status = AbstractFight.FightStatus.getStatusFromId(fightStatus);
        final Fight fight = new Fight(fightId, model, fightMap, lockedTeams, status);
        this.initFight(fight);
        return fight;
    }
    
    private void initFight(final Fight fight) {
        this.addFight(fight);
        fight.onFightCreatedAndInitialized();
    }
    
    public void addFight(final FightInfo info) {
        if (this.m_fights.contains(info.getId())) {
            FightManager.m_logger.error((Object)("On ajoute un fight (class=" + info.getClass().getSimpleName() + " id=" + info.getId() + ") alors qu'un fight existe d\u00c3©j\u00c3  avec cet id (class=" + this.m_fights.get(info.getId()).getClass().getSimpleName() + ") : leaks possibles"));
        }
        this.m_fights.put(info.getId(), info);
    }
    
    public void destroyFight(final FightInfo info) {
        if (info == null) {
            return;
        }
        this.m_fights.remove(info.getId());
        if (info instanceof ExternalFightInfo) {
            final ExternalFightInfo externalFight = (ExternalFightInfo)info;
            externalFight.onFightDestroyed();
        }
    }
    
    public FightInfo getFightById(final int fightId) {
        return this.m_fights.get(fightId);
    }
    
    public TIntObjectIterator<FightInfo> getFightsIterator() {
        return this.m_fights.iterator();
    }
    
    public void clear() {
        this.m_fights.clear();
    }
    
    public void cleanUp() {
        TIntObjectIterator<FightInfo> it = this.getFightsIterator();
        while (it.hasNext()) {
            it.advance();
            final FightInfo fi = it.value();
            if (fi instanceof Fight) {
                ((Fight)fi).endFight();
                it = this.getFightsIterator();
            }
            else {
                it.remove();
            }
            FightActionGroupManager.getInstance().removePendingGroups(fi.getId());
        }
    }
    
    public FightInfo getFightOnWorldCell(final int worldCellX, final int worldCellY) {
        final TIntObjectIterator<FightInfo> fightIterator = this.getFightsIterator();
        while (fightIterator.hasNext()) {
            fightIterator.advance();
            final FightInfo fightInfo = fightIterator.value();
            if (fightInfo.getFightMap().isInsideOrBorder(worldCellX, worldCellY)) {
                return fightInfo;
            }
        }
        return null;
    }
    
    public boolean hasFightOnPosition(final int worldCellX, final int worldCellY) {
        return this.getFightOnWorldCell(worldCellX, worldCellY) != null;
    }
    
    public void destroyFight(final int fightId) {
        final FightInfo fight = this.m_fights.get(fightId);
        this.destroyFight(fight);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightManager.class);
        m_instance = new FightManager();
    }
}
