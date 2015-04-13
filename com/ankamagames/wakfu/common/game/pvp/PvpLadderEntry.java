package com.ankamagames.wakfu.common.game.pvp;

import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.ratings.*;

public final class PvpLadderEntry extends AbstractCharacterData implements Ratable<NationPvpRanks>
{
    private int m_nationId;
    private int m_strength;
    private int m_ranking;
    private NationPvpRanks m_rank;
    private int m_cachedStrength;
    private int m_cachedRanking;
    private int m_cachedBreedRanking;
    private int m_cachedGuildRanking;
    private NationPvpRanks m_cachedRank;
    private GameDate m_lastModification;
    private short m_level;
    private boolean m_canGainPvpPoints;
    private boolean m_canLosePvpPoints;
    private short m_currentStreak;
    private short m_maxStreak;
    private final TByteIntHashMap m_defeats;
    private final TByteIntHashMap m_victories;
    private boolean m_fake;
    private final LRUCache<Long, Integer> m_fightOccurences;
    
    public PvpLadderEntry(final long id, final int nationId) {
        super();
        this.m_ranking = -1;
        this.m_rank = NationPvpRanks.RANK_1;
        this.m_cachedRanking = -1;
        this.m_cachedBreedRanking = -1;
        this.m_cachedGuildRanking = -1;
        this.m_cachedRank = NationPvpRanks.RANK_1;
        this.m_lastModification = GameDate.getNullDate();
        this.m_defeats = new TByteIntHashMap();
        this.m_victories = new TByteIntHashMap();
        this.m_fightOccurences = new LRUCache<Long, Integer>(30);
        this.m_id = id;
        this.m_nationId = nationId;
    }
    
    public PvpLadderEntry(final long id, final int nationId, final GameDateConst lastModification) {
        super();
        this.m_ranking = -1;
        this.m_rank = NationPvpRanks.RANK_1;
        this.m_cachedRanking = -1;
        this.m_cachedBreedRanking = -1;
        this.m_cachedGuildRanking = -1;
        this.m_cachedRank = NationPvpRanks.RANK_1;
        this.m_lastModification = GameDate.getNullDate();
        this.m_defeats = new TByteIntHashMap();
        this.m_victories = new TByteIntHashMap();
        this.m_fightOccurences = new LRUCache<Long, Integer>(30);
        this.m_id = id;
        this.m_nationId = nationId;
        this.m_lastModification.set(lastModification);
    }
    
    public PvpLadderEntry(final ByteBuffer bb) {
        super();
        this.m_ranking = -1;
        this.m_rank = NationPvpRanks.RANK_1;
        this.m_cachedRanking = -1;
        this.m_cachedBreedRanking = -1;
        this.m_cachedGuildRanking = -1;
        this.m_cachedRank = NationPvpRanks.RANK_1;
        this.m_lastModification = GameDate.getNullDate();
        this.m_defeats = new TByteIntHashMap();
        this.m_victories = new TByteIntHashMap();
        this.m_fightOccurences = new LRUCache<Long, Integer>(30);
        this.unserialize(bb);
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public String getGuildName() {
        return this.m_guildName;
    }
    
    @Override
    public int getStrength() {
        return this.m_strength;
    }
    
    @Override
    public void setStrength(final int strength) {
        this.m_strength = strength;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public void setLevel(final short level) {
        this.m_level = level;
    }
    
    @Override
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public void setBreedId(final short breedId) {
        this.m_breedId = breedId;
    }
    
    @Override
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public void setGuildId(final long guildId) {
        this.m_guildId = guildId;
    }
    
    public boolean canGainPvpPoints() {
        return this.m_canGainPvpPoints;
    }
    
    public void setCanGainPvpPoints(final boolean canGainPvpPoints) {
        this.m_canGainPvpPoints = canGainPvpPoints;
    }
    
    public boolean canLosePvpPoints() {
        return this.m_canLosePvpPoints;
    }
    
    public void setCanLosePvpPoints(final boolean canLosePvpPoints) {
        this.m_canLosePvpPoints = canLosePvpPoints;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    public void setNationId(final int nationId) {
        this.m_nationId = nationId;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    @Override
    public CharacterDataAppearance getAppearance() {
        return this.m_appearance;
    }
    
    public void setAppearance(final CharacterDataAppearance appearance) {
        this.m_appearance = appearance;
    }
    
    @Override
    public GameDate getLastModification() {
        return this.m_lastModification;
    }
    
    public void setLastModification(final GameDateConst date) {
        this.m_lastModification.set(date);
    }
    
    public void incNumberOfFightOccurenceWith(final long opponentId) {
        final Integer numFights = this.m_fightOccurences.remove(opponentId);
        if (numFights == null || numFights == 0) {
            this.m_fightOccurences.put(opponentId, 1);
        }
        else {
            this.m_fightOccurences.put(opponentId, numFights + 1);
        }
    }
    
    public int getNumberOfFightOccurenceWith(final long opponentId) {
        final Integer numFights = this.m_fightOccurences.get(opponentId);
        return (numFights != null) ? numFights : 0;
    }
    
    public void resetNumberOfFightOccurences() {
        this.m_fightOccurences.clear();
    }
    
    @Override
    public int getRanking() {
        return this.m_ranking;
    }
    
    @Override
    public void setRanking(final int ranking) {
        this.m_ranking = ranking;
    }
    
    @Override
    public NationPvpRanks getRank() {
        return this.m_rank;
    }
    
    @Override
    public void setRank(final NationPvpRanks rank) {
        this.m_rank = rank;
    }
    
    public int getCachedStrength() {
        return this.m_cachedStrength;
    }
    
    public void setCachedStrength(final int cachedStrength) {
        this.m_cachedStrength = cachedStrength;
    }
    
    public int getCachedRanking() {
        return this.m_cachedRanking;
    }
    
    public void setCachedRanking(final int cachedRanking) {
        this.m_cachedRanking = cachedRanking;
    }
    
    public int getCachedBreedRanking() {
        return this.m_cachedBreedRanking;
    }
    
    public void setCachedBreedRanking(final int cachedBreedRanking) {
        this.m_cachedBreedRanking = cachedBreedRanking;
    }
    
    public int getCachedGuildRanking() {
        return this.m_cachedGuildRanking;
    }
    
    public void setCachedGuildRanking(final int cachedGuildRanking) {
        this.m_cachedGuildRanking = cachedGuildRanking;
    }
    
    public NationPvpRanks getCachedRank() {
        return this.m_cachedRank;
    }
    
    public void setCachedRank(final NationPvpRanks cachedRank) {
        this.m_cachedRank = cachedRank;
    }
    
    public void mergeCachedData() {
        this.m_cachedRank = this.m_rank;
        this.m_cachedRanking = this.m_ranking;
        this.m_cachedStrength = this.m_strength;
    }
    
    @Override
    public byte getSex() {
        return this.m_sex;
    }
    
    public void setSex(final byte sex) {
        this.m_sex = sex;
    }
    
    public short getCurrentStreak() {
        return this.m_currentStreak;
    }
    
    public void setCurrentStreak(final short currentStreak) {
        this.m_currentStreak = currentStreak;
    }
    
    public short getMaxStreak() {
        return this.m_maxStreak;
    }
    
    public void setMaxStreak(final short maxStreak) {
        this.m_maxStreak = maxStreak;
    }
    
    public int getNumMatches(final MatchType type) {
        return this.m_victories.get(type.getId()) + this.m_defeats.get(type.getId());
    }
    
    public int getNumDefeats(final MatchType type) {
        return this.m_defeats.get(type.getId());
    }
    
    public int getNumVictories(final MatchType type) {
        return this.m_victories.get(type.getId());
    }
    
    public void setNumDefeats(final MatchType type, final int value) {
        this.m_defeats.put(type.getId(), MathHelper.max(0, value, new int[0]));
    }
    
    public void setNumVictories(final MatchType type, final int value) {
        this.m_victories.put(type.getId(), MathHelper.max(0, value, new int[0]));
    }
    
    public void incrementNumDefeats(final MatchType type) {
        this.m_defeats.adjustOrPutValue(type.getId(), 1, 1);
    }
    
    public void incrementNumVictories(final MatchType type) {
        this.m_victories.adjustOrPutValue(type.getId(), 1, 1);
    }
    
    public boolean isFake() {
        return this.m_fake;
    }
    
    public void setFake(final boolean fake) {
        this.m_fake = fake;
    }
    
    public void serialize(final ByteArray bb, final boolean withAppearance) {
        bb.putLong(this.m_id);
        bb.putInt(this.m_nationId);
        bb.putInt(this.m_strength);
        bb.putInt(this.m_cachedStrength);
        bb.putShort(this.m_level);
        bb.putShort(this.m_breedId);
        bb.putLong(this.m_guildId);
        final boolean appearance = withAppearance && this.m_appearance != null;
        bb.putBoolean(appearance);
        if (appearance) {
            this.m_appearance.serialize(bb);
        }
        bb.putInt(this.m_ranking);
        bb.putInt(this.m_cachedRanking);
        bb.put(this.m_rank.getId());
        bb.put(this.m_cachedRank.getId());
        bb.put(this.m_sex);
        bb.putShort(this.m_currentStreak);
        bb.putShort(this.m_maxStreak);
        for (final MatchType type : MatchType.values()) {
            bb.putInt(this.m_defeats.get(type.getId()));
        }
        for (final MatchType type : MatchType.values()) {
            bb.putInt(this.m_victories.get(type.getId()));
        }
        final byte[] bytes = StringUtils.toUTF8(this.m_name);
        bb.putInt(bytes.length);
        bb.put(bytes);
        bb.putLong(this.m_lastModification.toLong());
    }
    
    public void unserialize(final ByteBuffer bb) {
        this.m_id = bb.getLong();
        this.m_nationId = bb.getInt();
        this.m_strength = bb.getInt();
        this.m_cachedStrength = bb.getInt();
        this.m_level = bb.getShort();
        this.m_breedId = bb.getShort();
        this.m_guildId = bb.getLong();
        if (bb.get() != 0) {
            this.m_appearance = CharacterDataAppearance.fromBuild(bb);
        }
        this.m_ranking = bb.getInt();
        this.m_cachedRanking = bb.getInt();
        this.m_rank = NationPvpRanks.getById(bb.get());
        this.m_cachedRank = NationPvpRanks.getById(bb.get());
        this.m_sex = bb.get();
        this.m_currentStreak = bb.getShort();
        this.m_maxStreak = bb.getShort();
        for (final MatchType type : MatchType.values()) {
            this.m_defeats.put(type.getId(), bb.getInt());
        }
        for (final MatchType type : MatchType.values()) {
            this.m_victories.put(type.getId(), bb.getInt());
        }
        final byte[] nameArray = new byte[bb.getInt()];
        bb.get(nameArray);
        this.m_name = StringUtils.fromUTF8(nameArray);
        this.m_lastModification = GameDate.fromLong(bb.getLong());
    }
    
    @Override
    public int compareTo(@NotNull final Ratable<NationPvpRanks> o) {
        if (o.getStrength() > this.m_strength) {
            return 1;
        }
        if (o.getStrength() < this.m_strength) {
            return -1;
        }
        return this.m_lastModification.compareTo(o.getLastModification());
    }
    
    @Override
    public String toString() {
        return "PvpLadderEntry{m_id=" + this.m_id + ", m_nationId=" + this.m_nationId + ", m_strength=" + this.m_strength + ", m_level=" + this.m_level + ", m_breedId=" + this.m_breedId + ", m_canGainPvpPoints=" + this.m_canGainPvpPoints + ", m_name='" + this.m_name + '\'' + ", m_guildId=" + this.m_guildId + ", m_fightOccurences=" + this.m_fightOccurences + '}';
    }
}
