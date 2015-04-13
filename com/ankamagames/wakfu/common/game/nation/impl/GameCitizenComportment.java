package com.ankamagames.wakfu.common.game.nation.impl;

import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.crime.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.diplomacy.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;

public abstract class GameCitizenComportment extends AbstractCitizenComportement
{
    private NationRank m_rank;
    protected final TIntIntHashMap m_citizenScores;
    protected int m_crimePurgationNationId;
    protected CrimePurgationCooldown m_purgationCooldown;
    protected final TIntHashSet m_offendedNations;
    protected boolean m_isPasseportActive;
    
    protected GameCitizenComportment(final Citizen citizen) {
        super(citizen);
        this.m_rank = null;
        this.m_citizenScores = new TIntIntHashMap();
        this.m_crimePurgationNationId = -1;
        this.m_offendedNations = new TIntHashSet();
    }
    
    @Override
    public int getCitizenScoreForNation(final int nationId) {
        return this.m_citizenScores.get(nationId);
    }
    
    public boolean containsCitizenPoints(final int nationId) {
        return this.m_citizenScores.containsKey(nationId);
    }
    
    @Override
    public NationRank getRank() {
        return this.m_rank;
    }
    
    @Override
    public void setRank(final NationRank rank) {
        this.m_rank = rank;
    }
    
    @Override
    public boolean hasRank(final NationRank rank) {
        return this.m_rank != null && this.m_rank == rank;
    }
    
    @Override
    public boolean isNationEnemy(final int nationId) {
        return this.isOutlaw(nationId) || this.isPvpEnemy(nationId);
    }
    
    @Override
    public boolean isPvpEnemy() {
        return this.isPvpEnemy(((BasicCharacterInfo)this.m_citizen).getTravellingNationId());
    }
    
    @Override
    public boolean isPvpEnemy(final int nationId) {
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            return false;
        }
        final NationDiplomacyManager citizenDiplomacy = this.m_citizen.getCitizenComportment().getNation().getDiplomacyManager();
        final NationAlignement alignment = citizenDiplomacy.getAlignment(nationId);
        return alignment == NationAlignement.ENEMY && NationPvpHelper.isPvpActive(this);
    }
    
    @Override
    public boolean isOutlaw() {
        return this.isOutlaw(((BasicCharacterInfo)this.m_citizen).getTravellingNationId());
    }
    
    @Override
    public boolean isOutlaw(final int nationId) {
        final Nation nation = NationManager.INSTANCE.getNationById(nationId);
        if (nation == null) {
            return false;
        }
        final NationDiplomacyManager citizenDiplomacy = this.m_citizen.getCitizenComportment().getNation().getDiplomacyManager();
        final NationAlignement alignment = citizenDiplomacy.getAlignment(nationId);
        switch (alignment) {
            case ENEMY: {
                return this.m_offendedNations.contains(nationId);
            }
            case ALLIED: {
                final int citizenScore = this.m_citizenScores.get(this.getNationId());
                return CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScore).hasRule(CitizenRankRule.IS_NATION_ENEMY);
            }
            default: {
                throw new UnsupportedOperationException("Type d'alignement non reconnu " + alignment);
            }
        }
    }
    
    @Override
    public boolean isNationEnemy() {
        return this.isNationEnemy(((BasicCharacterInfo)this.m_citizen).getTravellingNationId());
    }
    
    @Override
    public int getCrimePurgationScore() {
        return this.m_citizenScores.get(this.m_crimePurgationNationId);
    }
    
    @Override
    public int getCrimePurgationNationId() {
        return this.m_crimePurgationNationId;
    }
    
    @Override
    public int addPurgationCrimePoint(final int points) {
        final int newValue = this.m_citizenScores.adjustOrPutValue(this.m_crimePurgationNationId, points, points);
        if (newValue >= 0) {
            this.m_citizenScores.remove(this.m_crimePurgationNationId);
        }
        return newValue;
    }
    
    public int citizenScoresSize() {
        return this.m_citizenScores.size();
    }
    
    public int setCitizenScore(final int nationId, final int crimeValue) {
        return this.m_citizenScores.put(nationId, crimeValue);
    }
    
    public boolean hasCitizenScore() {
        return !this.m_citizenScores.isEmpty();
    }
    
    public TIntIntIterator citizenScoresIterator() {
        return this.m_citizenScores.iterator();
    }
    
    public void removePurgationCrimeScore() {
        this.m_citizenScores.remove(this.m_crimePurgationNationId);
    }
    
    public int[] getHasCitizenScoreNations() {
        return this.m_citizenScores.keys();
    }
    
    public boolean addOffendedNation(final Nation nation) {
        return nation != null && nation != Nation.VOID_NATION && this.m_offendedNations.add(nation.getNationId());
    }
    
    @Override
    public boolean removeOffendedNation(final Nation nation) {
        return nation != null && this.m_offendedNations.remove(nation.getNationId());
    }
    
    public TIntHashSet getOffendedNations() {
        return this.m_offendedNations;
    }
    
    @Override
    public abstract void startCrimePurgation(final int p0);
    
    @Override
    public void resetToNation(@NotNull final Nation nation) {
        super.resetToNation(nation);
        this.setPasseportActive(true);
    }
    
    @Override
    public void setPasseportActive(final boolean active) {
        this.m_isPasseportActive = active;
    }
    
    @Override
    public boolean isPasseportActive() {
        return this.m_isPasseportActive;
    }
}
