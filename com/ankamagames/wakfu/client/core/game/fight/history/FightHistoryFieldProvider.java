package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.fight.fightHistory.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.common.game.pvp.fight.*;
import gnu.trove.*;

public class FightHistoryFieldProvider extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final FightHistoryFieldProvider INSTANCE;
    public static final String VICTORY_FIELD = "victory";
    public static final String LOSERS_FIELD = "losers";
    public static final String WINNERS_FIELD = "winners";
    public static final String SPELLS_FIELD = "spells";
    public static final String SPELL_LEVEL_GAIN_FIELD = "spellLevelGain";
    public static final String SUMMONS_FIELD = "summons";
    public static final String SUMMON_LEVEL_GAIN_FIELD = "summonLevelGain";
    public static final String CONTAINS_KAMAS_FIELD = "containsKamas";
    public static final String CONTAINS_TAXES_FIELD = "containsTaxes";
    public static final String CHALLENGES = "challenges";
    public static final String[] FIELDS;
    private long m_fightId;
    private boolean m_victory;
    private final TLongObjectHashMap<PlayerHistoryFieldProvider> m_winnerHistoryFieldProviders;
    private final TLongObjectHashMap<PlayerHistoryFieldProvider> m_loserHistoryFieldProviders;
    private long m_localPlayerId;
    private final List<SpellXpGainFieldProvider> m_spells;
    private final List<SummonsXpGainFieldProvider> m_summons;
    private boolean m_containsKamas;
    private boolean m_containsTaxes;
    private PvpFightReport m_report;
    private Collection<AbstractFightChallengeView> m_challenges;
    
    private FightHistoryFieldProvider() {
        super();
        this.m_winnerHistoryFieldProviders = new TLongObjectHashMap<PlayerHistoryFieldProvider>();
        this.m_loserHistoryFieldProviders = new TLongObjectHashMap<PlayerHistoryFieldProvider>();
        this.m_spells = new ArrayList<SpellXpGainFieldProvider>();
        this.m_summons = new ArrayList<SummonsXpGainFieldProvider>();
    }
    
    public void init(final FightHistoryReader history, final PlayerXpModificationCollection xpModifications, final Collection<AbstractFightChallengeView> challenges, final int fightId) {
        this.clearButReport();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_localPlayerId = localPlayer.getId();
        this.m_fightId = fightId;
        this.m_challenges = challenges;
        for (final PlayerFightHistory playerFightHistory : history) {
            final long controllerId = playerFightHistory.getCompanionId();
            final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
            if (controllerId != -1L && CompanionManager.INSTANCE.getCompanion(accountId, controllerId) != null) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCompanionEndFight());
            }
            if (playerFightHistory.getKamas() > 0L && !this.m_containsKamas) {
                this.m_containsKamas = true;
            }
            if ((!playerFightHistory.getCollectedLoots().isEmpty() || playerFightHistory.getCollectedKamas() > 0L) && !this.m_containsTaxes) {
                this.m_containsTaxes = true;
            }
            final long playerId = playerFightHistory.getCharacterId();
            final PlayerXpModification playerXpModification = getXpModification(xpModifications, playerId);
            if (playerId == this.m_localPlayerId) {
                this.m_victory = playerFightHistory.hasWon();
                for (final SkillOrSpellXpModification skillOrSpellXpModification : playerXpModification) {
                    this.m_spells.add(new SpellXpGainFieldProvider(skillOrSpellXpModification, WakfuGameEntity.getInstance().getLocalPlayer()));
                }
                Collections.sort(this.m_spells);
                final TLongObjectIterator<SummonFightHistory> it = playerFightHistory.getSummonsHistory().iterator();
                while (it.hasNext()) {
                    it.advance();
                    final SummonFightHistory summon = it.value();
                    this.m_summons.add(new SummonsXpGainFieldProvider(summon.getIndex(), summon.getXpAtEndOfFight(), summon.getLevelEarned()));
                }
            }
            if (playerFightHistory.hasWon()) {
                this.m_winnerHistoryFieldProviders.put(playerId, new PlayerHistoryFieldProvider(playerFightHistory, playerXpModification));
            }
            else {
                this.m_loserHistoryFieldProviders.put(playerId, new PlayerHistoryFieldProvider(playerFightHistory, playerXpModification));
            }
        }
        this.fillPvpReportInfo(this.m_report);
    }
    
    public void clear() {
        this.clearButReport();
        this.m_report = null;
    }
    
    public void clearButReport() {
        this.m_winnerHistoryFieldProviders.clear();
        this.m_loserHistoryFieldProviders.clear();
        this.m_containsKamas = false;
        this.m_containsTaxes = false;
        this.m_localPlayerId = 0L;
        this.m_fightId = 0L;
        this.m_spells.clear();
        this.m_summons.clear();
        this.m_victory = false;
        this.m_challenges = null;
    }
    
    private static PlayerXpModification getXpModification(final PlayerXpModificationCollection xpModifications, final long playerId) {
        if (xpModifications == null) {
            return PlayerXpModification.none(playerId);
        }
        final PlayerXpModification playerXpModification = xpModifications.getPlayerXpModification(playerId);
        if (playerXpModification == null) {
            return PlayerXpModification.none(playerId);
        }
        return playerXpModification;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("victory".equals(fieldName)) {
            return this.m_victory;
        }
        if ("losers".equals(fieldName)) {
            final Object[] objects = this.m_loserHistoryFieldProviders.getValues();
            return (objects.length > 0) ? objects : null;
        }
        if ("winners".equals(fieldName)) {
            final Object[] objects = this.m_winnerHistoryFieldProviders.getValues();
            return (objects.length > 0) ? objects : null;
        }
        if ("spells".equals(fieldName)) {
            return (this.m_spells.size() > 0) ? this.m_spells : null;
        }
        if ("spellLevelGain".equals(fieldName)) {
            return this.isSpellLevelgain();
        }
        if ("summons".equals(fieldName)) {
            return (this.m_summons.size() > 0) ? this.m_summons : null;
        }
        if ("summonLevelGain".equals(fieldName)) {
            return this.isSummonLevelgain();
        }
        if ("containsKamas".equals(fieldName)) {
            return this.m_containsKamas;
        }
        if ("containsTaxes".equals(fieldName)) {
            return this.m_containsTaxes;
        }
        if ("challenges".equals(fieldName)) {
            return this.m_challenges;
        }
        return null;
    }
    
    private boolean isSpellLevelgain() {
        for (final SpellXpGainFieldProvider spellXpGainFieldProvider : this.m_spells) {
            if (spellXpGainFieldProvider.getXpModification().getLevelDifference() > 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSummonLevelgain() {
        for (final SummonsXpGainFieldProvider summonsXpGainFieldProvider : this.m_summons) {
            if (summonsXpGainFieldProvider.getLevelGained() > 0) {
                return true;
            }
        }
        return false;
    }
    
    public void setPvpReport(final PvpFightReport report) {
        this.fillPvpReportInfo(this.m_report = report);
    }
    
    private void fillPvpReportInfo(final PvpFightReport report) {
        if (this.m_report == null || this.m_report.getFightId() != this.m_fightId) {
            return;
        }
        final TLongObjectHashMap<PlayerReportInfo> playersReportsInfos = report.getPlayersReportsInfos();
        playersReportsInfos.forEachEntry(new FillPvpReportInfoProcedure());
    }
    
    public long getFightId() {
        return this.m_fightId;
    }
    
    public void setFightId(final long fightId) {
        this.m_fightId = fightId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightHistoryFieldProvider.class);
        INSTANCE = new FightHistoryFieldProvider();
        FIELDS = new String[] { "victory", "losers", "winners", "spells", "spellLevelGain", "summons", "summonLevelGain", "containsTaxes", "containsKamas", "challenges" };
    }
    
    private class FillPvpReportInfoProcedure implements TLongObjectProcedure<PlayerReportInfo>
    {
        @Override
        public boolean execute(final long playerId, final PlayerReportInfo reportInfo) {
            PlayerHistoryFieldProvider fieldProvider = FightHistoryFieldProvider.this.m_winnerHistoryFieldProviders.get(playerId);
            if (fieldProvider == null) {
                fieldProvider = FightHistoryFieldProvider.this.m_loserHistoryFieldProviders.get(playerId);
            }
            if (fieldProvider == null) {
                return true;
            }
            fieldProvider.setPvpReportInfo(reportInfo);
            return true;
        }
    }
}
