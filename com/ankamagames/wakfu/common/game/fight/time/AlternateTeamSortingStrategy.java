package com.ankamagames.wakfu.common.game.fight.time;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public final class AlternateTeamSortingStrategy extends FighterSortingStrategy
{
    private final BasicFightInfo<? extends BasicCharacterInfo> m_fight;
    private final InitProvider m_initProvider;
    private Comparator<BasicCharacterInfo> m_teamSortComparator;
    private List<BasicCharacterInfo> m_firstTeam;
    private List<BasicCharacterInfo> m_secondTeam;
    
    public AlternateTeamSortingStrategy(final BasicFightInfo<? extends BasicCharacterInfo> fight, final InitProvider initProvider) {
        super();
        this.m_fight = fight;
        this.m_initProvider = initProvider;
        this.m_teamSortComparator = new TeamSortComparator(this.m_fight.getId(), initProvider);
    }
    
    @Override
    protected void sortInitially(final DynamicLists dynamicLists) {
        this.clearTurnOrder(dynamicLists);
        this.initTeams();
        final TLongArrayList turnOrder = this.getTurnOrder();
        this.setDynamicAndTurnOrder(dynamicLists, turnOrder);
    }
    
    private void setDynamicAndTurnOrder(final DynamicLists dynamicLists, final TLongArrayList turnOrder) {
        dynamicLists.getDynamicOrder().add(turnOrder.toNativeArray());
        dynamicLists.getTurnOrder().add(turnOrder.toNativeArray());
    }
    
    private TLongArrayList getTurnOrder() {
        final TLongArrayList turnOrder = new TLongArrayList();
        if (this.m_firstTeam.isEmpty() && this.m_secondTeam.isEmpty()) {
            return turnOrder;
        }
        if (this.m_firstTeam.isEmpty()) {
            this.completeTurnOrderWith(turnOrder, this.m_secondTeam);
            return turnOrder;
        }
        if (this.m_secondTeam.isEmpty()) {
            this.completeTurnOrderWith(turnOrder, this.m_firstTeam);
            return turnOrder;
        }
        boolean firstTeamTurn = true;
        while (!this.m_firstTeam.isEmpty() && !this.m_secondTeam.isEmpty()) {
            if (firstTeamTurn) {
                turnOrder.add(this.m_firstTeam.remove(0).getId());
            }
            else {
                turnOrder.add(this.m_secondTeam.remove(0).getId());
            }
            firstTeamTurn = !firstTeamTurn;
            if (this.m_firstTeam.isEmpty()) {
                this.completeTurnOrderWith(turnOrder, this.m_secondTeam);
            }
            else {
                if (!this.m_secondTeam.isEmpty()) {
                    continue;
                }
                this.completeTurnOrderWith(turnOrder, this.m_firstTeam);
            }
        }
        return turnOrder;
    }
    
    private void completeTurnOrderWith(final TLongArrayList turnOrder, final List<? extends BasicCharacterInfo> team) {
        for (final BasicCharacterInfo basicCharacterInfo : team) {
            turnOrder.add(basicCharacterInfo.getId());
        }
    }
    
    private void clearTurnOrder(final DynamicLists dynamicLists) {
        dynamicLists.getTurnOrder().clear();
        dynamicLists.getDynamicOrder().clear();
    }
    
    private void initTeams() {
        final Collection<? extends BasicCharacterInfo> redTeam = this.m_fight.getFightersPresentInTimelineInPlayInTeam((byte)0);
        final Collection<? extends BasicCharacterInfo> blueTeam = this.m_fight.getFightersPresentInTimelineInPlayInTeam((byte)1);
        final int redTeamInitMean = this.getTeamInitMean(redTeam);
        final int blueTeamInitMean = this.getTeamInitMean(blueTeam);
        if (blueTeamInitMean > redTeamInitMean) {
            this.m_firstTeam = new ArrayList<BasicCharacterInfo>(blueTeam);
            this.m_secondTeam = new ArrayList<BasicCharacterInfo>(redTeam);
        }
        else if (blueTeamInitMean < redTeamInitMean) {
            this.m_firstTeam = new ArrayList<BasicCharacterInfo>(redTeam);
            this.m_secondTeam = new ArrayList<BasicCharacterInfo>(blueTeam);
        }
        else if (this.m_fight.getId() % 2 == 0) {
            this.m_firstTeam = new ArrayList<BasicCharacterInfo>(blueTeam);
            this.m_secondTeam = new ArrayList<BasicCharacterInfo>(redTeam);
        }
        else {
            this.m_firstTeam = new ArrayList<BasicCharacterInfo>(redTeam);
            this.m_secondTeam = new ArrayList<BasicCharacterInfo>(blueTeam);
        }
        Collections.sort(this.m_firstTeam, this.m_teamSortComparator);
        Collections.sort(this.m_secondTeam, this.m_teamSortComparator);
    }
    
    private int getTeamInitMean(final Collection<? extends BasicCharacterInfo> team) {
        if (team.isEmpty()) {
            return 0;
        }
        int teamInitMean = 0;
        for (final BasicCharacterInfo fighter : team) {
            teamInitMean += this.m_initProvider.getInit(fighter.getId());
        }
        teamInitMean /= team.size();
        return teamInitMean;
    }
    
    @Override
    protected void sortForOneTurn(final DynamicLists dynamicLists) {
    }
    
    @Override
    protected void sortDynamically(final DynamicLists dynamicLists) {
    }
    
    @Override
    protected void addToDynamicList(final DynamicLists dynamicLists, final long fighterId) {
        dynamicLists.getDynamicOrder().add(fighterId);
    }
    
    @Override
    protected void insertIntoTurnOrderList(final DynamicLists dynamicLists, final long fighterId, final int insertPos) {
        dynamicLists.getTurnOrder().insert(insertPos, fighterId);
        dynamicLists.getDynamicOrder().clear();
        dynamicLists.getDynamicOrder().add(dynamicLists.getTurnOrder().toNativeArray());
    }
    
    @Override
    protected void removeFromDynamicList(final DynamicLists dynamicLists, final long fighterId) {
        TroveUtils.removeFirstValue(dynamicLists.getDynamicOrder(), fighterId);
    }
}
