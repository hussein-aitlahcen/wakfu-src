package com.ankamagames.wakfu.common.game.fight.fightHistory;

import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;
import gnu.trove.*;

public final class PlayerFightHistorySerializer
{
    private PlayerFightHistory m_fightHistory;
    
    public void setFightHistory(final PlayerFightHistory fightHistory) {
        this.m_fightHistory = fightHistory;
    }
    
    public PlayerFightHistory getFightHistory() {
        return this.m_fightHistory;
    }
    
    public int serializedSize() {
        return 10 + StringUtils.toUTF8(this.m_fightHistory.getName()).length + 2 + 1 + 1 + 2 + 8 + 8 + 8 + 8 + 2 + this.m_fightHistory.getLootsDuringFight().size() * 6 + 2 + this.m_fightHistory.getLootsAtEndFight().size() * 6 + 2 + this.m_fightHistory.getCanceledLootsAtEndFight().size() * 6 + 2 + this.m_fightHistory.getAlmostCanceledLoots().size() * 6 + 2 + this.summonFightHistorySerializedSize();
    }
    
    public void write(final ByteBuffer buffer) {
        buffer.putLong(this.m_fightHistory.getCharacterId());
        final byte[] name = StringUtils.toUTF8(this.m_fightHistory.getName());
        buffer.putShort((short)name.length);
        buffer.put(name);
        buffer.putShort(this.m_fightHistory.getBreedId());
        buffer.putLong(this.m_fightHistory.getCompanionId());
        buffer.put((byte)(this.m_fightHistory.hasWon() ? 1 : 0));
        buffer.put((byte)(this.m_fightHistory.hasFled() ? 1 : 0));
        buffer.putShort(this.m_fightHistory.getEndLevel());
        buffer.putLong(this.m_fightHistory.getEndXp());
        buffer.putLong(this.m_fightHistory.getKamas());
        buffer.putLong(this.m_fightHistory.getPremiumXp());
        this.writeTIntShortMap(buffer, this.m_fightHistory.getLootsDuringFight());
        this.writeTIntShortMap(buffer, this.m_fightHistory.getLootsAtEndFight());
        this.writeTIntShortMap(buffer, this.m_fightHistory.getCanceledLootsAtEndFight());
        this.writeTIntShortMap(buffer, this.m_fightHistory.getAlmostCanceledLoots());
        this.writeSummonFightHistory(buffer, this.m_fightHistory.getSummonsHistory());
    }
    
    public void deserialize(final ByteBuffer buffer) {
        final long characterId = buffer.getLong();
        final short nameLength = buffer.getShort();
        final byte[] nameBytes = new byte[nameLength];
        buffer.get(nameBytes);
        final short breedId = buffer.getShort();
        final long companionId = buffer.getLong();
        this.m_fightHistory = new PlayerFightHistory(characterId, nameBytes, breedId, companionId);
        this.read(buffer);
    }
    
    private void read(final ByteBuffer buffer) {
        this.m_fightHistory.setHasWon(buffer.get() == 1);
        this.m_fightHistory.setHasFled(buffer.get() == 1);
        final short endLevel = buffer.getShort();
        final long endXp = buffer.getLong();
        this.m_fightHistory.setEndXp(endXp, endLevel);
        this.m_fightHistory.setKamas(buffer.getLong());
        this.m_fightHistory.setPremiumXp(buffer.getLong());
        this.fillTIntShortMap(buffer, this.m_fightHistory.getLootsDuringFight());
        this.fillTIntShortMap(buffer, this.m_fightHistory.getLootsAtEndFight());
        this.fillTIntShortMap(buffer, this.m_fightHistory.getCanceledLootsAtEndFight());
        this.fillTIntShortMap(buffer, this.m_fightHistory.getAlmostCanceledLoots());
        this.fillSummonFightHistory(buffer, this.m_fightHistory.getSummonsHistory());
    }
    
    private void writeTIntShortMap(final ByteBuffer buffer, final TIntShortHashMap map) {
        buffer.putShort((short)map.size());
        map.forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int refId, final short qty) {
                buffer.putInt(refId);
                buffer.putShort(qty);
                return true;
            }
        });
    }
    
    private void fillTIntShortMap(final ByteBuffer buffer, final TIntShortHashMap map) {
        final short numLootsDuringFight = buffer.getShort();
        for (int i = 0; i < numLootsDuringFight; ++i) {
            final int refId = buffer.getInt();
            final short qty = buffer.getShort();
            map.put(refId, qty);
        }
    }
    
    private int summonFightHistorySerializedSize() {
        return this.m_fightHistory.getSummonsHistory().size() * 11;
    }
    
    private void writeSummonFightHistory(final ByteBuffer buffer, final TLongObjectHashMap<SummonFightHistory> summonsHistory) {
        buffer.putShort((short)summonsHistory.size());
        final TLongObjectIterator<SummonFightHistory> it = summonsHistory.iterator();
        while (it.hasNext()) {
            it.advance();
            final SummonFightHistory history = it.value();
            buffer.put(history.getIndex());
            buffer.putLong(history.getXpAtEndOfFight());
            buffer.putShort(history.getLevelEarned());
        }
    }
    
    private void fillSummonFightHistory(final ByteBuffer buffer, final TLongObjectHashMap<SummonFightHistory> summonsHistory) {
        final short nbSummonsInfos = buffer.getShort();
        for (int i = 0; i < nbSummonsInfos; ++i) {
            final byte index = buffer.get();
            final long xp = buffer.getLong();
            final short levelEarned = buffer.getShort();
            summonsHistory.put(index, new SummonFightHistory(index, xp, levelEarned));
        }
    }
}
