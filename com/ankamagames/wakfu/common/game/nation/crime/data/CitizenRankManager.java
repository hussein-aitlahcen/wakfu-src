package com.ankamagames.wakfu.common.game.nation.crime.data;

import org.jetbrains.annotations.*;
import java.util.*;

public final class CitizenRankManager
{
    private static CitizenRankManager INSTANCE;
    public static final CitizenRank NULL_RANK;
    private SortedSet<CitizenRank> m_ranks;
    private CitizenRank[] m_sortedCitizenRanks;
    private int m_minBound;
    private int m_maxBound;
    
    public static CitizenRankManager getInstance() {
        return CitizenRankManager.INSTANCE;
    }
    
    private CitizenRankManager() {
        super();
        this.m_ranks = new TreeSet<CitizenRank>(new Comparator<CitizenRank>() {
            @Override
            public int compare(final CitizenRank o1, final CitizenRank o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1.getCap() == o2.getCap()) {
                    return 0;
                }
                if (o1.getCap() > o2.getCap()) {
                    return 1;
                }
                return -1;
            }
        });
    }
    
    @NotNull
    public CitizenRank getRankFromCitizenScore(final int score) {
        if (this.m_sortedCitizenRanks == null || this.m_sortedCitizenRanks.length == 0) {
            return CitizenRankManager.NULL_RANK;
        }
        for (int i = 0; i < this.m_sortedCitizenRanks.length; ++i) {
            final CitizenRank citizenRank = this.m_sortedCitizenRanks[i];
            if (score <= citizenRank.getCap()) {
                return citizenRank;
            }
        }
        return this.m_sortedCitizenRanks[this.m_sortedCitizenRanks.length - 1];
    }
    
    public CitizenRank getRankBefore(final CitizenRank citizenRank) {
        if (this.m_sortedCitizenRanks == null || this.m_sortedCitizenRanks.length == 0) {
            return CitizenRankManager.NULL_RANK;
        }
        for (int i = this.m_sortedCitizenRanks.length - 1; i >= 0; --i) {
            final CitizenRank citizenRank2 = this.m_sortedCitizenRanks[i];
            if (citizenRank2.getCap() < citizenRank.getCap()) {
                return citizenRank2;
            }
        }
        return null;
    }
    
    public CitizenRank getRankAfter(final CitizenRank citizenRank) {
        if (this.m_sortedCitizenRanks == null || this.m_sortedCitizenRanks.length == 0) {
            return CitizenRankManager.NULL_RANK;
        }
        for (int i = 0; i < this.m_sortedCitizenRanks.length; ++i) {
            final CitizenRank citizenRank2 = this.m_sortedCitizenRanks[i];
            if (citizenRank.getCap() < citizenRank2.getCap()) {
                return citizenRank2;
            }
        }
        return null;
    }
    
    public int getMinBound() {
        return this.m_minBound;
    }
    
    public int getMaxBound() {
        return this.m_maxBound;
    }
    
    public void addRank(final int id, final int cap, final int pdcLossFactor, final String translationKey, final String color, final CitizenRankRule[] rules) {
        final CitizenRank rank = new CitizenRankImpl(id, cap, pdcLossFactor, translationKey, color, rules);
        this.m_ranks.add(rank);
    }
    
    public void onDataLoaded() {
        this.m_sortedCitizenRanks = new CitizenRank[this.m_ranks.size()];
        this.m_ranks.toArray(this.m_sortedCitizenRanks);
        this.m_minBound = this.m_ranks.first().getCap();
        this.m_maxBound = this.m_ranks.last().getCap();
        this.m_ranks = null;
    }
    
    void setRanks(final SortedSet<CitizenRank> ranks) {
        if (this.m_ranks == null) {
            this.m_ranks = ranks;
        }
        else {
            this.m_ranks.clear();
            this.m_ranks.addAll((Collection<?>)ranks);
        }
    }
    
    public CitizenRank getFromId(final int id) {
        for (int i = 0; i < this.m_sortedCitizenRanks.length; ++i) {
            final CitizenRank rank = this.m_sortedCitizenRanks[i];
            if (rank.getId() == id) {
                return rank;
            }
        }
        return null;
    }
    
    static {
        CitizenRankManager.INSTANCE = new CitizenRankManager();
        NULL_RANK = new NullCitizenRank();
    }
}
