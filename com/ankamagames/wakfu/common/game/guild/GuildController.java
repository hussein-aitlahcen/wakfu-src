package com.ankamagames.wakfu.common.game.guild;

import com.ankamagames.wakfu.common.game.guild.exception.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class GuildController
{
    private final GuildModel m_guild;
    
    public GuildController(final Guild guild) {
        super();
        this.m_guild = (GuildModel)guild;
    }
    
    public void addMember(final GuildMember member) throws GuildException {
        if (this.m_guild.getMember(member.getId()) != null) {
            throw new GuildException("Membre d\u00e9j\u00e0 pr\u00e9sent dans la guilde");
        }
        this.m_guild.addMember(member);
    }
    
    public void removeMember(final long memberId) throws GuildException {
        final GuildMember member = this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        this.m_guild.removeMember(memberId);
    }
    
    public void addBonus(final GuildBonus bonus) throws GuildException {
        if (this.m_guild.getBonus(bonus.getBonusId()) != null) {
            throw new GuildException("Bonus d\u00e9j\u00e0 pr\u00e9sent dans la guilde");
        }
        this.m_guild.addBonus(bonus);
    }
    
    public void removeBonus(final int bonusId) throws GuildException {
        final GuildBonus bonus = this.m_guild.getBonus(bonusId);
        if (bonus == null) {
            throw new GuildException("Bonus non-pr\u00e9sent dans la guilde");
        }
        this.m_guild.removeBonus(bonusId);
    }
    
    public void addRank(final GuildRank rank) throws GuildException {
        if (this.m_guild.getRank(rank.getId()) != null) {
            throw new GuildException("Rank d\u00e9j\u00e0 pr\u00e9sent dans la guilde");
        }
        if (rank.getName().length() > 16) {
            throw new GuildException("Le nom de rang demand\u00e9 est trop long");
        }
        this.m_guild.addRank(rank);
    }
    
    public void moveRank(final long rankId, final short position) throws GuildException {
        final GuildRank rank = this.m_guild.getRank(rankId);
        if (rank == null) {
            throw new GuildException("Rang non-pr\u00e9sent dans la guilde");
        }
        this.m_guild.moveRank(rankId, position);
    }
    
    public void removeRank(final long rankId) throws GuildException {
        final GuildRank rank = this.m_guild.getRank(rankId);
        if (rank == null) {
            throw new GuildException("Rang non-pr\u00e9sent dans la guilde");
        }
        if (rankId == this.m_guild.getBestRank()) {
            throw new GuildException("Impossible de supprimer le plus haut rang de la guilde");
        }
        if (rankId == this.m_guild.getWorstRank()) {
            throw new GuildException("Impossible de supprimer rang le plus bas de la guilde");
        }
        if (!this.m_guild.forEachMember(new CheckMemberRank(rankId))) {
            throw new GuildException("Des membres de la guildes ont encore ce rang assign\u00e9");
        }
        this.m_guild.removeRank(rankId);
    }
    
    public void removeGuildPoints(final int points) throws GuildException {
        if (points < 0) {
            throw new GuildException("Impossible de retirer des points n\u00e9gatifs");
        }
        this.m_guild.setCurrentGuildPoints((int)Math.max(this.m_guild.getCurrentGuildPoints() - points, 0L));
    }
    
    public int addGuildPoints(final int points) throws GuildException {
        if (points < 0) {
            throw new GuildException("Impossible d'ajouter des points n\u00e9gatifs");
        }
        if (this.m_guild.getWeeklyPointsLimit() <= 0) {
            this.addPointsToGuild(points);
            return points;
        }
        this.handleNewWeek();
        final int remainingPointsForWeek = this.m_guild.getWeeklyPointsLimit() - this.m_guild.getEarnedPointsWeekly();
        if (remainingPointsForWeek <= 0) {
            return 0;
        }
        final int pointsToAdd = Math.min(points, remainingPointsForWeek);
        this.addPointsToGuild(pointsToAdd);
        return pointsToAdd;
    }
    
    private void handleNewWeek() {
        final int lastWeek = this.m_guild.getLastEarningPointWeek();
        final int currentWeek = WakfuGameCalendar.getInstance().get(3);
        if (lastWeek != currentWeek) {
            this.m_guild.setLastEarningPointWeek(currentWeek);
            this.m_guild.setEarnedPointsWeekly(0);
        }
    }
    
    void addPointsToGuild(final int pointsToAdd) {
        this.m_guild.setCurrentGuildPoints((int)Math.min(pointsToAdd + this.m_guild.getCurrentGuildPoints(), Long.MAX_VALUE));
        this.m_guild.setTotalGuildPoints((int)Math.min(pointsToAdd + this.m_guild.getTotalGuildPoints(), Long.MAX_VALUE));
        this.m_guild.setEarnedPointsWeekly(this.m_guild.getEarnedPointsWeekly() + pointsToAdd);
    }
    
    public void setWeeklyPointsLimit(final int dailyPointsLimit) {
        this.m_guild.setWeeklyPointsLimit(dailyPointsLimit);
    }
    
    public void setGuildLevel(final short level) throws GuildException {
        if (level < 0) {
            throw new GuildException("Impossible d'appliquer un niveau n\u00e9gatif");
        }
        if (this.getGuild().getLevel() >= level) {
            throw new GuildException("Mauvais level \u00e0 d\u00e9bloquer");
        }
        if (level > 10) {
            throw new GuildException("Impossible d'appliquer un niveau > 10");
        }
        this.m_guild.setLevel(level);
    }
    
    public void changeDescription(final String description) throws GuildException {
        this.m_guild.setDescription(WordsModerator.getInstance().makeValidSentence(description, true));
    }
    
    public void changeNation(final int nationId) throws GuildException {
        this.m_guild.setNationId(nationId);
    }
    
    public void changeMessage(final String message) throws GuildException {
        this.m_guild.setMessage(WordsModerator.getInstance().makeValidSentence(message, true));
    }
    
    public void changeRankName(final long rankId, final String name) throws GuildException {
        final GuildRankModel rank = (GuildRankModel)this.m_guild.getRank(rankId);
        if (rank == null) {
            throw new GuildException("Rang non-pr\u00e9sent dans la guilde");
        }
        if (name.length() > 16) {
            throw new GuildException("Le nom de rang demand\u00e9 est trop long");
        }
        rank.setName(name);
    }
    
    public void changeRankAuthorisations(final long rankId, final long authorisations) throws GuildException {
        final GuildRankModel rank = (GuildRankModel)this.m_guild.getRank(rankId);
        if (rank == null) {
            throw new GuildException("Rang non-pr\u00e9sent dans la guilde");
        }
        if (rankId == this.m_guild.getBestRank() && authorisations != GuildRankAuthorisation.ALL) {
            throw new GuildException("Impossible de modifier le plus haut rang de la guilde");
        }
        if (rankId == this.m_guild.getWorstRank() && authorisations != GuildRankAuthorisation.NONE) {
            throw new GuildException("Impossible de modifier rang le plus bas de la guilde");
        }
        rank.setAuthorisations(authorisations);
    }
    
    public void changeMemberSmiley(final long memberId, final byte smiley) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setSmiley(smiley);
    }
    
    public void changeMemberGuildPoints(final long memberId, final int points) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setGuildPoints(points);
    }
    
    public void changeMemberXp(final long memberId, final long xp) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setXp(xp);
    }
    
    public void changeMemberNation(final long memberId, final int nationId) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setNationId(nationId);
    }
    
    public void changeMemberName(final long memberId, final String memberName) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setName(memberName);
    }
    
    public void changeMemberBreed(final long memberId, final short breedId) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setBreedId(breedId);
    }
    
    public void changeMemberSex(final long memberId, final byte sex) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setSex(sex);
    }
    
    public void changeMemberRank(final long memberId, final long rankId) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        final GuildRank rank = this.m_guild.getRank(rankId);
        if (rank == null) {
            throw new GuildException("Le rank demand\u00e9 n'existe pas");
        }
        member.setRank(rankId);
    }
    
    public void changeMemberConnected(final long memberId, final boolean connected) throws GuildException {
        final GuildMemberModel member = (GuildMemberModel)this.m_guild.getMember(memberId);
        if (member == null) {
            throw new GuildException("Membre non-pr\u00e9sent dans la guilde");
        }
        member.setConnected(connected);
    }
    
    public void changeBonusActivationDate(final int bonusId, final GameDateConst activationDate) throws GuildException {
        final GuildBonusModel bonus = (GuildBonusModel)this.m_guild.getBonus(bonusId);
        if (bonus == null) {
            throw new GuildException("Bonus non-pr\u00e9sent dans la guilde");
        }
        bonus.setActivationDate(activationDate);
    }
    
    public Guild getGuild() {
        return this.m_guild;
    }
    
    @Override
    public String toString() {
        return "GuildController{m_guild=" + this.m_guild + '}';
    }
    
    private static class CheckMemberRank implements TObjectProcedure<GuildMember>
    {
        private final long m_rankId;
        
        CheckMemberRank(final long rankId) {
            super();
            this.m_rankId = rankId;
        }
        
        @Override
        public boolean execute(final GuildMember object) {
            return object.getRank() != this.m_rankId;
        }
        
        @Override
        public String toString() {
            return "CheckMemberRank{m_rankId=" + this.m_rankId + '}';
        }
    }
}
