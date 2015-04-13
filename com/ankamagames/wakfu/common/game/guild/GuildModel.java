package com.ankamagames.wakfu.common.game.guild;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.util.*;
import gnu.trove.*;

final class GuildModel implements Guild, GuildRankListener, GuildMemberListener, GuildBonusListener
{
    private static final Logger m_logger;
    private final ArrayList<GuildListener> m_listeners;
    private long m_id;
    private String m_name;
    private long m_blazon;
    private String m_description;
    private String m_message;
    private short m_level;
    private int m_nationId;
    private final GuildPointsHandler m_pointsHandler;
    private final TLongObjectHashMap<GuildRank> m_ranks;
    private final TLongObjectHashMap<GuildMember> m_members;
    private final TIntObjectHashMap<GuildBonus> m_bonuses;
    private final GuildBonusConstantManager m_bonusConstants;
    
    GuildModel() {
        super();
        this.m_listeners = new ArrayList<GuildListener>();
        this.m_pointsHandler = new GuildPointsHandler();
        this.m_ranks = new TLongObjectHashMap<GuildRank>();
        this.m_members = new TLongObjectHashMap<GuildMember>();
        this.m_bonuses = new TIntObjectHashMap<GuildBonus>();
        this.m_bonusConstants = new GuildBonusConstantManager();
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public long getBestRank() {
        final long[] keys = this.m_ranks.keys();
        Arrays.sort(keys);
        return (keys.length > 0) ? keys[0] : 0L;
    }
    
    @Override
    public long getWorstRank() {
        final long[] keys = this.m_ranks.keys();
        Arrays.sort(keys);
        return (keys.length > 1) ? keys[1] : 0L;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public long getBlazon() {
        return this.m_blazon;
    }
    
    @Override
    public String getDescription() {
        return this.m_description;
    }
    
    @Override
    public String getMessage() {
        return this.m_message;
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public int getNationId() {
        return this.m_nationId;
    }
    
    @Override
    public int getCurrentGuildPoints() {
        return this.m_pointsHandler.getCurrentGuildPoints();
    }
    
    @Override
    public int getTotalGuildPoints() {
        return this.m_pointsHandler.getTotalGuildPoints();
    }
    
    @Override
    public GuildMember getMember(final long memberId) {
        return this.m_members.get(memberId);
    }
    
    @Override
    public GuildRank getRank(final long rankId) {
        return this.m_ranks.get(rankId);
    }
    
    @Override
    public GuildBonus getBonus(final int bonusId) {
        return this.m_bonuses.get(bonusId);
    }
    
    @Override
    public int memberSize() {
        return this.m_members.size();
    }
    
    @Override
    public boolean forEachMember(final TObjectProcedure<GuildMember> procedure) {
        return this.m_members.forEachValue(procedure);
    }
    
    @Override
    public int rankSize() {
        return this.m_ranks.size();
    }
    
    @Override
    public boolean forEachRank(final TObjectProcedure<GuildRank> procedure) {
        return this.m_ranks.forEachValue(procedure);
    }
    
    @Override
    public int bonusSize() {
        return this.m_bonuses.size();
    }
    
    @Override
    public boolean forEachBonus(final TObjectProcedure<GuildBonus> procedure) {
        return this.m_bonuses.forEachValue(procedure);
    }
    
    @Override
    public GuildBonusConstantManager getConstantManager() {
        return this.m_bonusConstants;
    }
    
    @Override
    public int getWeeklyPointsLimit() {
        return this.m_pointsHandler.getWeeklyPointsLimit();
    }
    
    @Override
    public int getEarnedPointsWeekly() {
        return this.m_pointsHandler.getEarnedPointsWeekly();
    }
    
    @Override
    public int getLastEarningPointWeek() {
        return this.m_pointsHandler.getLastEarningPointWeek();
    }
    
    void setId(final long id) {
        this.m_id = id;
    }
    
    void setName(final String name) {
        this.m_name = name;
        this.fireNameChanged();
    }
    
    void setBlazon(final long blazon) {
        this.m_blazon = blazon;
        this.fireBlazonChanged();
    }
    
    void setDescription(final String description) {
        this.m_description = description;
        this.fireDescriptionChanged();
    }
    
    void setMessage(final String message) {
        this.m_message = message;
        this.fireMessageChanged();
    }
    
    void setLevel(final short level) {
        this.fireLevelChanged(this.m_level = level);
    }
    
    void setNationId(final int nationId) {
        this.fireNationIdChanged(this.m_nationId = nationId);
    }
    
    void setTotalGuildPoints(final int totalGuildPoints) {
        final int previousTotalGuildPoints = this.m_pointsHandler.getTotalGuildPoints();
        this.m_pointsHandler.setTotalGuildPoints(totalGuildPoints);
        this.fireTotalGuildPointsChanged(this.m_pointsHandler.getTotalGuildPoints() - previousTotalGuildPoints);
    }
    
    void setCurrentGuildPoints(final int currentGuildPoints) {
        final int previousCurrentGuildPoints = this.m_pointsHandler.getCurrentGuildPoints();
        this.m_pointsHandler.setCurrentGuildPoints(currentGuildPoints);
        this.fireCurrentGuildPointsChanged(this.m_pointsHandler.getCurrentGuildPoints() - previousCurrentGuildPoints);
    }
    
    void setWeeklyPointsLimit(final int dailyPointsLimit) {
        this.m_pointsHandler.setWeeklyPointsLimit(dailyPointsLimit);
    }
    
    @Override
    public void setEarnedPointsWeekly(final int earnedPoints) {
        this.m_pointsHandler.setEarnedPointsWeekly(earnedPoints);
        this.fireEarnedPointsWeeklyChanged(earnedPoints);
    }
    
    @Override
    public void setLastEarningPointWeek(final int week) {
        this.m_pointsHandler.setLastEarningPointWeek(week);
        this.fireLastEarningPointWeekChanged(week);
    }
    
    void addRank(final GuildRank rank) {
        this.m_ranks.put(rank.getId(), rank);
        rank.addListener(this);
        this.fireRankAdded(rank);
    }
    
    void moveRank(final long rankId, final short position) {
        final GuildRank rank = this.m_ranks.get(rankId);
        rank.setPosition(position);
        this.fireRankMoved(rank);
    }
    
    void removeRank(final long rankId) {
        final GuildRank rank = this.m_ranks.remove(rankId);
        rank.removeListener(this);
        this.fireRankRemoved(rank);
    }
    
    void addMember(final GuildMember member) {
        this.m_members.put(member.getId(), member);
        member.addListener(this);
        this.fireMemberAdded(member);
    }
    
    void removeMember(final long memberId) {
        final GuildMember member = this.m_members.remove(memberId);
        member.removeListener(this);
        this.fireMemberRemoved(member);
    }
    
    void addBonus(final GuildBonus bonus) {
        this.m_bonuses.put(bonus.getBonusId(), bonus);
        bonus.addListener(this);
        this.fireBonusAdded(bonus);
    }
    
    void removeBonus(final int bonusId) {
        final GuildBonus bonus = this.m_bonuses.remove(bonusId);
        bonus.removeListener(this);
        this.fireBonusRemoved(bonus);
    }
    
    private void fireNameChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).nameChanged();
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de nom de la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireBlazonChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).blazonChanged();
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de blason pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireDescriptionChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).descriptionChanged();
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de description pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireLevelChanged(final short level) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).levelChanged(level);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de niveau pour la guilde " + this + " niveau = " + level), (Throwable)e);
            }
        }
    }
    
    private void fireNationIdChanged(final int nationId) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).nationIdChanged(nationId);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de nation pour la guilde " + this + " nationId= " + nationId), (Throwable)e);
            }
        }
    }
    
    private void fireTotalGuildPointsChanged(final int deltaPoints) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).totalGuildPointsChanged(deltaPoints);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de point de guilde, delta = " + deltaPoints + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireCurrentGuildPointsChanged(final int deltaPoints) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).currentGuildPointsChanged(deltaPoints);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de point de guilde, delta = " + deltaPoints + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireMessageChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).messageChanged();
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au changement de message de la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireRankAdded(final GuildRank rank) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).rankAdded(rank);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee \u00e0 l'ajout du rank " + rank + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireRankMoved(final GuildRank rank) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).rankMoved(rank);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee sur le changement de rang " + rank + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireRankRemoved(final GuildRank rank) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).rankRemoved(rank);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au retrait du rang " + rank + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireMemberAdded(final GuildMember member) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).memberAdded(member);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee \u00e0 l'ajout d'un membre " + member + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireMemberRemoved(final GuildMember member) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).memberRemoved(member);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au retrait d'un membre " + member + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireBonusAdded(final GuildBonus bonus) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).bonusAdded(bonus);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee pour le bonus " + bonus + " de la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireBonusRemoved(final GuildBonus bonus) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).bonusRemoved(bonus);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee au retrait d'un bonus " + bonus + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireEarnedPointsWeeklyChanged(final int earnedPoints) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).earnedPointsWeeklyChanged(earnedPoints);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee lors de la modification de points gagn\u00e9s " + earnedPoints + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    private void fireLastEarningPointWeekChanged(final int week) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).lastEarningPointWeekChanged(week);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee lors de la modification de la semaine " + week + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    @Override
    public void rankChanged(final GuildRank rank) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).rankChanged(rank);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee \u00e0 la modification d'un rang " + rank + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    @Override
    public void memberChanged(final GuildMember member) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).memberChanged(member);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee \u00e0 la modification d'un membre " + member + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    @Override
    public void bonusActivated(final GuildBonus bonus) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).bonusActivated(bonus);
            }
            catch (RuntimeException e) {
                GuildModel.m_logger.error((Object)("Exception levee \u00e0 l'activation d'un bonus " + bonus + " pour la guilde " + this), (Throwable)e);
            }
        }
    }
    
    @Override
    public boolean addListener(final GuildListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final GuildListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "GuildModel{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", m_members=" + this.m_members.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildModel.class);
    }
}
