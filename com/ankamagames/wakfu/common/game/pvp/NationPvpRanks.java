package com.ankamagames.wakfu.common.game.pvp;

import com.ankamagames.baseImpl.common.clientAndServer.game.ratings.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public enum NationPvpRanks implements Rank
{
    RANK_1(0, 0, -1), 
    RANK_2(1, 2000, -1), 
    RANK_3(2, 4000, -1), 
    RANK_4(3, 6000, -1), 
    RANK_5(4, 8000, -1), 
    RANK_6(5, 10000, -1), 
    RANK_7(6, 12000, -1), 
    RANK_8(7, 14000, 42), 
    RANK_9(8, 16000, 7), 
    RANK_10(9, 18000, 1);
    
    private final byte m_id;
    private final int m_pointsNeeded;
    private final int m_maxPlayerInRank;
    private int m_totalMaxPlayersInRank;
    private static final NationPvpRanks[] VALUES;
    
    private NationPvpRanks(final int id, final int pointsNeeded, final int maxPlayerInRank) {
        this.m_id = MathHelper.ensureByte(id);
        this.m_pointsNeeded = pointsNeeded;
        this.m_maxPlayerInRank = maxPlayerInRank;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @NotNull
    public static NationPvpRanks getRank(final int score, final int ranking) {
        NationPvpRanks rank = getByScore(score);
        if (rank.m_maxPlayerInRank <= 0) {
            return rank;
        }
        while (rank != null && rank.m_totalMaxPlayersInRank > 0 && rank.m_totalMaxPlayersInRank < ranking + 1) {
            rank = rank.getPrevious();
        }
        return (rank != null) ? rank : NationPvpRanks.RANK_1;
    }
    
    @NotNull
    private static NationPvpRanks getByScore(final int score) {
        int low = 0;
        int high = NationPvpRanks.VALUES.length - 1;
        while (low <= high) {
            final int mid = low + high >>> 1;
            final NationPvpRanks midVal = getValueByIndex(mid);
            final NationPvpRanks nextVal = getValueByIndex(mid + 1);
            if (nextVal != null && nextVal.m_pointsNeeded <= score) {
                low = mid + 1;
            }
            else {
                if (midVal == null || midVal.m_pointsNeeded <= score) {
                    return (midVal != null) ? midVal : NationPvpRanks.RANK_1;
                }
                high = mid - 1;
            }
        }
        return NationPvpRanks.RANK_1;
    }
    
    @Nullable
    private static NationPvpRanks getValueByIndex(final int index) {
        if (index < 0 || index >= NationPvpRanks.VALUES.length) {
            return null;
        }
        return NationPvpRanks.VALUES[index];
    }
    
    @Nullable
    public static NationPvpRanks getById(final byte rankId) {
        for (final NationPvpRanks ranks : values()) {
            if (rankId == ranks.m_id) {
                return ranks;
            }
        }
        return null;
    }
    
    @Override
    public int getScore() {
        return this.m_pointsNeeded;
    }
    
    @Override
    public int getMaxUsers() {
        return this.m_maxPlayerInRank;
    }
    
    @Nullable
    private NationPvpRanks getPrevious() {
        return getValueByIndex(this.ordinal() - 1);
    }
    
    public boolean isLessThan(final NationPvpRanks rank) {
        return this.m_pointsNeeded < rank.m_pointsNeeded;
    }
    
    public boolean isMoreThan(final NationPvpRanks rank) {
        return this.m_pointsNeeded > rank.m_pointsNeeded;
    }
    
    public static void main(final String... args) {
        displayRankTest(-1, -1);
        displayRankTest(-1, 15);
        displayRankTest(15, -1);
        displayRankTest(5999, 454);
        displayRankTest(6000, 54542);
        displayRankTest(6001, 54543);
        displayRankTest(15500, 43);
        displayRankTest(1550, 43);
        displayRankTest(15500, 1);
        displayRankTest(22554, 48);
    }
    
    private static void displayRankTest(final int score, final int ranking) {
        System.out.println("Score:" + score + ", Ranking:" + ranking + " => " + getRank(score, ranking));
    }
    
    static {
        VALUES = values();
        int currentMaxUsers = 0;
        for (int i = NationPvpRanks.VALUES.length - 1; i >= 0; --i) {
            final NationPvpRanks rank = NationPvpRanks.VALUES[i];
            if (rank.m_maxPlayerInRank >= 0) {
                rank.m_totalMaxPlayersInRank = rank.m_maxPlayerInRank + currentMaxUsers;
                currentMaxUsers += rank.m_maxPlayerInRank;
            }
        }
    }
}
