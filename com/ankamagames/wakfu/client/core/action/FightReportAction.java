package com.ankamagames.wakfu.client.core.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.client.core.game.fightChallenge.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.fight.fightHistory.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class FightReportAction extends AbstractFightTimedAction
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private final FightHistoryReader m_fightHistory;
    private final PlayerXpModificationCollection m_playerXpModifications;
    private final FightModel m_model;
    
    public FightReportAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final FightHistoryReader fightHistory, final PlayerXpModificationCollection playerXpModifications, final FightModel model) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_fightHistory = fightHistory;
        this.m_playerXpModifications = playerXpModifications;
        this.m_model = model;
    }
    
    @Override
    protected long onRun() {
        this.logHistory();
        UIFightEndFrame.getInstance().setPvpResult(this.m_model.isUseInPvpRanking());
        if (!WakfuGameEntity.getInstance().hasFrame(UIFightEndFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIFightEndFrame.getInstance());
        }
        FightHistoryFieldProvider.INSTANCE.init(this.m_fightHistory, this.m_playerXpModifications, FightChallengeEventListener.INSTANCE.getLastViews(), this.getFightId());
        PropertiesProvider.getInstance().setPropertyValue("fight.resultDescription", FightHistoryFieldProvider.INSTANCE);
        return 0L;
    }
    
    private void logHistory() {
        final Iterator<PlayerFightHistory> it = this.m_fightHistory.iterator();
        final StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            final PlayerFightHistory history = it.next();
            sb.append("Historique du fighter ").append(history.getName()).append(", ID = ").append(history.getCharacterId()).append("\n").append("A gagn\u00e9 : ").append(history.hasWon()).append(", A fuis : ").append(history.hasFled()).append("\n");
        }
        final List<LongIntPair> collectedLoots = this.m_fightHistory.getCollectedLoots();
        for (final LongIntPair collectedLoot : collectedLoots) {
            sb.append("Loot ").append(collectedLoot.getSecond()).append("  pris a ").append(collectedLoot.getFirst()).append('\n');
        }
        sb.append("Kamas collect\u00e9s ").append(this.m_fightHistory.getCollectedKamas()).append('\n');
        FightReportAction.m_logger.info((Object)sb.toString());
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightReportAction.class);
    }
}
