package com.ankamagames.wakfu.common.game.fight.fightHistory;

import gnu.trove.*;
import com.ankamagames.framework.kernel.utils.*;

public class PlayerFightHistory
{
    private final long m_characterId;
    private final short m_breedId;
    private final String m_name;
    private final long m_companionId;
    private short m_endLevel;
    private long m_endXp;
    private boolean m_hasWon;
    private boolean m_hasFled;
    private long m_kamas;
    private long m_taxes;
    private long m_collectedKamas;
    private long m_premiumXp;
    private final TIntShortHashMap m_collectedLoots;
    private final TIntShortHashMap m_lootsDuringFight;
    private final TIntShortHashMap m_lootsAtEndFight;
    private final TIntShortHashMap m_canceledLootsAtEndFight;
    private final TIntShortHashMap m_almostCanceledLoots;
    private final TLongObjectHashMap<SummonFightHistory> m_summonsHistory;
    
    public PlayerFightHistory(final long characterId, final String name, final short breedId, final long companionId) {
        super();
        this.m_kamas = 0L;
        this.m_taxes = 0L;
        this.m_collectedKamas = 0L;
        this.m_premiumXp = 0L;
        this.m_collectedLoots = new TIntShortHashMap();
        this.m_lootsDuringFight = new TIntShortHashMap();
        this.m_lootsAtEndFight = new TIntShortHashMap();
        this.m_canceledLootsAtEndFight = new TIntShortHashMap();
        this.m_almostCanceledLoots = new TIntShortHashMap();
        this.m_summonsHistory = new TLongObjectHashMap<SummonFightHistory>();
        this.m_characterId = characterId;
        this.m_name = name;
        this.m_breedId = breedId;
        this.m_companionId = companionId;
    }
    
    public PlayerFightHistory(final long characterId, final String name, final short breedId) {
        this(characterId, name, breedId, -1L);
    }
    
    public PlayerFightHistory(final long characterId, final byte[] name, final short breedId, final long companionId) {
        this(characterId, StringUtils.fromUTF8(name), breedId, companionId);
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public short getEndLevel() {
        return this.m_endLevel;
    }
    
    public long getEndXp() {
        return this.m_endXp;
    }
    
    public void setEndXp(final long endXp, final short endLevel) {
        this.m_endXp = endXp;
        this.m_endLevel = endLevel;
    }
    
    public boolean hasWon() {
        return this.m_hasWon;
    }
    
    public TIntShortHashMap getLootsDuringFight() {
        return this.m_lootsDuringFight;
    }
    
    public TIntShortHashMap getLootsAtEndFight() {
        return this.m_lootsAtEndFight;
    }
    
    public TIntShortHashMap getCollectedLoots() {
        return this.m_collectedLoots;
    }
    
    public TIntShortHashMap getCanceledLootsAtEndFight() {
        return this.m_canceledLootsAtEndFight;
    }
    
    public TIntShortHashMap getAlmostCanceledLoots() {
        return this.m_almostCanceledLoots;
    }
    
    public long getKamas() {
        return this.m_kamas;
    }
    
    public void setKamas(final long kamas) {
        this.m_kamas = kamas;
    }
    
    public long getTaxes() {
        return this.m_taxes;
    }
    
    public void setTaxes(final long taxes) {
        this.m_taxes = taxes;
    }
    
    public void setHasWon(final boolean winner) {
        this.m_hasWon = winner;
    }
    
    public long getCollectedKamas() {
        return this.m_collectedKamas;
    }
    
    public void setCollectedKamas(final long collectedKamas) {
        this.m_collectedKamas = collectedKamas;
    }
    
    public void addCollectedLoot(final int refId, final short quantity) {
        this.m_collectedLoots.adjustOrPutValue(refId, quantity, quantity);
    }
    
    public void addLootDuringFight(final int refId, final short quantity) {
        this.m_lootsDuringFight.adjustOrPutValue(refId, quantity, quantity);
    }
    
    public void addLootAtEndFight(final int refId, final short quantity) {
        this.m_lootsAtEndFight.adjustOrPutValue(refId, quantity, quantity);
    }
    
    public void addCancelledLootAtEndFight(final int refId, final short quantity) {
        this.m_canceledLootsAtEndFight.adjustOrPutValue(refId, quantity, quantity);
    }
    
    public void addAlmostCancelledLoot(final int refId, final short quantity) {
        this.m_almostCanceledLoots.adjustOrPutValue(refId, quantity, quantity);
    }
    
    public void setSummonXp(final byte index, final long xp, final short levelEarned) {
        this.m_summonsHistory.put(index, new SummonFightHistory(index, xp, levelEarned));
    }
    
    public TLongObjectHashMap<SummonFightHistory> getSummonsHistory() {
        return this.m_summonsHistory;
    }
    
    public void addKamas(final int kamas) {
        this.m_kamas += kamas;
    }
    
    public void addTaxes(final int taxe) {
        this.m_taxes += taxe;
    }
    
    public boolean hasFled() {
        return this.m_hasFled;
    }
    
    public void setHasFled(final boolean hasFled) {
        this.m_hasFled = hasFled;
    }
    
    public long getPremiumXp() {
        return this.m_premiumXp;
    }
    
    public void setPremiumXp(final long premiumXp) {
        this.m_premiumXp = premiumXp;
    }
    
    @Override
    public String toString() {
        return "PlayerFightHistory{m_characterId=" + this.m_characterId + ", m_breedId=" + this.m_breedId + ", m_name='" + this.m_name + '\'' + ", m_companionId=" + this.m_companionId + ", m_endLevel=" + this.m_endLevel + ", m_endXp=" + this.m_endXp + ", m_hasWon=" + this.m_hasWon + ", m_hasFled=" + this.m_hasFled + ", m_kamas=" + this.m_kamas + ", m_taxes=" + this.m_taxes + ", m_collectedKamas=" + this.m_collectedKamas + ", m_premiumXp=" + this.m_premiumXp + ", m_collectedLoots=" + this.m_collectedLoots + ", m_lootsDuringFight=" + this.m_lootsDuringFight + ", m_lootsAtEndFight=" + this.m_lootsAtEndFight + ", m_canceledLootsAtEndFight=" + this.m_canceledLootsAtEndFight + ", m_almostCanceledLoots=" + this.m_almostCanceledLoots + '}';
    }
}
