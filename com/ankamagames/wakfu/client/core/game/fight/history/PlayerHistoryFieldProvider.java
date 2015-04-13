package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.fight.fightHistory.*;
import com.ankamagames.wakfu.common.game.pvp.fight.*;
import com.ankamagames.wakfu.common.game.companion.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import gnu.trove.*;

public class PlayerHistoryFieldProvider extends ImmutableFieldProvider
{
    public static final String VICTORY_FIELD = "victory";
    public static final String NAME_FIELD = "name";
    public static final String XP_VALUE_FIELD = "xpValue";
    public static final String XP_GAIN_FIELD = "xpGain";
    public static final String LEVEL_GAIN_FIELD = "levelGain";
    public static final String SPELLS_FIELD = "spells";
    public static final String LOOTS_FIELD = "loots";
    public static final String COLLECTED_LOOTS_FIELD = "collectedLoots";
    public static final String CANCELLED_LOOTS = "cancelledLoots";
    public static final String PREMIUM_XP = "premiumXp";
    public static final String IS_PREMIUM = "isPremium";
    public static final String KAMAS_FIELD = "kamas";
    public static final String COLLECTED_KAMAS_FIELD = "collectedKamas";
    public static final String TAXES_FIELD = "taxes";
    public static final String IS_LOCAL_PLAYER = "isLocalPlayer";
    public static final String IS_LOCAL_COMPANION = "isLocalCompanion";
    public static final String PVP_REPORT = "pvpReport";
    public static final String[] FIELDS;
    private final PlayerFightHistory m_playerHistory;
    PlayerXpModification m_xpModification;
    private final XpValueFieldProvider m_xpValueField;
    private final List<ReferenceItemFieldProvider> m_lootItems;
    private final List<ReferenceItemFieldProvider> m_collectedLootItems;
    private final List<ReferenceItemFieldProvider> m_cancelledLootItems;
    private final PvpValueFieldProvider m_pvpValueField;
    
    public void setPvpReportInfo(final PlayerReportInfo pvpReportInfo) {
        this.m_pvpValueField.setReportInfo(pvpReportInfo);
    }
    
    @Override
    public String[] getFields() {
        return PlayerHistoryFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("name".equals(fieldName)) {
            return this.getName();
        }
        if ("victory".equals(fieldName)) {
            return this.victoryField();
        }
        if ("xpValue".equals(fieldName)) {
            return this.m_xpValueField;
        }
        if ("pvpReport".equals(fieldName)) {
            return this.m_pvpValueField;
        }
        if ("xpGain".equals(fieldName)) {
            return this.xpGainField();
        }
        if ("levelGain".equals(fieldName)) {
            return this.levelGainField();
        }
        if ("loots".equals(fieldName)) {
            return this.m_lootItems.isEmpty() ? null : Collections.unmodifiableCollection((Collection<?>)this.m_lootItems);
        }
        if ("cancelledLoots".equals(fieldName)) {
            return this.m_cancelledLootItems.isEmpty() ? null : Collections.unmodifiableCollection((Collection<?>)this.m_cancelledLootItems);
        }
        if ("premiumXp".equals(fieldName)) {
            return this.premiumXpField();
        }
        if ("isPremium".equals(fieldName)) {
            if (this.xpGainField() == null) {
                return this.m_playerHistory.getPremiumXp() > 0L;
            }
            return this.m_playerHistory.getPremiumXp() == this.xpGainField();
        }
        else {
            if ("collectedLoots".equals(fieldName)) {
                return this.m_collectedLootItems.isEmpty() ? null : Collections.unmodifiableCollection((Collection<?>)this.m_collectedLootItems);
            }
            if ("kamas".equals(fieldName)) {
                return this.kamasField();
            }
            if ("taxes".equals(fieldName)) {
                return this.taxesField();
            }
            if ("collectedKamas".equals(fieldName)) {
                return this.kamasCollectedField();
            }
            if ("isLocalPlayer".equals(fieldName)) {
                return this.m_playerHistory.getCharacterId() == WakfuGameEntity.getInstance().getLocalPlayer().getId();
            }
            if ("isLocalCompanion".equals(fieldName)) {
                final long companionId = this.m_playerHistory.getCompanionId();
                final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
                return CompanionManager.INSTANCE.getCompanion(accountId, companionId) != null;
            }
            return null;
        }
    }
    
    PlayerHistoryFieldProvider(final PlayerFightHistory playerHistory, final PlayerXpModification xpModification) {
        super();
        this.m_lootItems = new ArrayList<ReferenceItemFieldProvider>();
        this.m_collectedLootItems = new ArrayList<ReferenceItemFieldProvider>();
        this.m_cancelledLootItems = new ArrayList<ReferenceItemFieldProvider>();
        this.m_playerHistory = playerHistory;
        this.m_xpModification = xpModification;
        this.m_xpValueField = this.xpValueField();
        this.m_pvpValueField = new PvpValueFieldProvider();
        this.fillLootItems();
    }
    
    private void fillLootItems() {
        final TIntShortHashMap completeLoots = new TIntShortHashMap();
        this.copyMap(this.m_playerHistory.getLootsDuringFight().iterator(), completeLoots);
        this.copyMap(this.m_playerHistory.getLootsAtEndFight().iterator(), completeLoots);
        completeLoots.forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int refId, final short qty) {
                PlayerHistoryFieldProvider.this.m_lootItems.add(new ReferenceItemFieldProvider(refId, qty));
                return true;
            }
        });
        Collections.sort(this.m_lootItems);
        this.m_playerHistory.getCollectedLoots().forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int refId, final short qty) {
                PlayerHistoryFieldProvider.this.m_collectedLootItems.add(new ReferenceItemFieldProvider(refId, qty));
                return true;
            }
        });
        Collections.sort(this.m_collectedLootItems);
        this.m_playerHistory.getCanceledLootsAtEndFight().forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int refId, final short qty) {
                PlayerHistoryFieldProvider.this.m_cancelledLootItems.add(new PremiumLoot(refId, qty, true));
                return true;
            }
        });
        this.m_playerHistory.getAlmostCanceledLoots().forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int refId, final short qty) {
                PlayerHistoryFieldProvider.this.m_cancelledLootItems.add(new PremiumLoot(refId, qty, false));
                return true;
            }
        });
        Collections.sort(this.m_cancelledLootItems);
    }
    
    private void copyMap(final TIntShortIterator inputIterator, final TIntShortHashMap output) {
        while (inputIterator.hasNext()) {
            inputIterator.advance();
            final int refId = inputIterator.key();
            final short qty = inputIterator.value();
            output.adjustOrPutValue(refId, qty, qty);
        }
    }
    
    private Object victoryField() {
        return this.m_playerHistory.hasWon();
    }
    
    private XpValueFieldProvider xpValueField() {
        return new XpValueFieldProvider(CharacterXpTable.getInstance(), this.m_playerHistory.getEndLevel(), this.m_playerHistory.getEndXp(), this.xpGainField(), this.m_playerHistory.getPremiumXp(), this.m_xpModification.getPlayerXpModification().getLevelDifference());
    }
    
    private Long xpGainField() {
        final XpModification xpModification = this.m_xpModification.getPlayerXpModification();
        if (!xpModification.affectsTarget()) {
            return null;
        }
        return xpModification.getXpDifference();
    }
    
    private Object premiumXpField() {
        return this.m_playerHistory.getPremiumXp();
    }
    
    private Object levelGainField() {
        final short levelDifference = this.m_xpModification.getPlayerXpModification().getLevelDifference();
        return (levelDifference > 0) ? WakfuTranslator.getInstance().getString("levelGain", levelDifference) : null;
    }
    
    private Object kamasField() {
        final long kamas = this.m_playerHistory.getKamas();
        if (kamas < 0L) {
            return null;
        }
        return WakfuTranslator.getInstance().getString("kama.shortGain", this.m_playerHistory.getKamas());
    }
    
    private Object kamasCollectedField() {
        final long kamas = this.m_playerHistory.getCollectedKamas();
        if (kamas < 0L) {
            return null;
        }
        return WakfuTranslator.getInstance().getString("kama.shortGain", this.m_playerHistory.getCollectedKamas());
    }
    
    private Object taxesField() {
        final long taxes = this.m_playerHistory.getTaxes();
        if (taxes == 0L) {
            return null;
        }
        return this.m_playerHistory.getTaxes();
    }
    
    public void setXpModification(final PlayerXpModification xpModification) {
        this.m_xpModification = xpModification;
    }
    
    public PlayerXpModification getXpModification() {
        return this.m_xpModification;
    }
    
    public String getName() {
        final String name = this.m_playerHistory.getName();
        if (name != null && !name.isEmpty()) {
            return name;
        }
        final short breedId = this.m_playerHistory.getBreedId();
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId(breedId);
        if (breed != null) {
            return breed.getName();
        }
        return name;
    }
    
    public short getLevel() {
        return this.m_xpValueField.getLevel();
    }
    
    public long getXP() {
        final XpModification xpModification = this.m_xpModification.getPlayerXpModification();
        if (!xpModification.affectsTarget()) {
            return 0L;
        }
        return xpModification.getXpDifference();
    }
    
    public long getKamas() {
        return this.m_playerHistory.getKamas();
    }
    
    public long getCollectedKams() {
        return this.m_playerHistory.getCollectedKamas();
    }
    
    static {
        FIELDS = new String[] { "name", "victory", "xpValue", "xpGain", "levelGain", "spells", "loots", "collectedLoots", "cancelledLoots", "premiumXp", "isPremium", "kamas", "collectedKamas", "isLocalPlayer", "isLocalCompanion" };
    }
    
    private class PremiumLoot extends ReferenceItemFieldProvider
    {
        private final boolean m_canceled;
        public static final String CANCELED_FIELD = "canceled";
        
        public PremiumLoot(final int refId, final short quantity, final boolean canceled) {
            super(refId, quantity);
            this.m_canceled = canceled;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if ("canceled".equals(fieldName)) {
                return this.isCanceled();
            }
            return super.getFieldValue(fieldName);
        }
        
        public boolean isCanceled() {
            return this.m_canceled;
        }
    }
}
