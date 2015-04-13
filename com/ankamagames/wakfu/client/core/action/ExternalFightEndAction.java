package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;

public class ExternalFightEndAction extends AbstractFightAction
{
    private final List<Long> m_loosers;
    private final List<Long> m_winners;
    private final List<Long> m_escapees;
    
    public ExternalFightEndAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_loosers = new ArrayList<Long>();
        this.m_winners = new ArrayList<Long>();
        this.m_escapees = new ArrayList<Long>();
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    @Override
    protected void runCore() {
        final FightInfo fight = this.getFight();
        if (fight == null) {
            return;
        }
        for (final CharacterInfo fighter : fight.getFighters()) {
            fighter.forceUncarry();
            fighter.resetFighterAfterFight();
            if ((this.m_loosers.contains(fighter.getId()) || fighter.isDead()) && !fighter.isActiveProperty(FightPropertyType.NO_DEATH) && !this.m_winners.contains(fighter.getId()) && fighter instanceof NonPlayerCharacter) {
                ((NonPlayerCharacter)fighter).onNPCDeath();
            }
        }
        fight.endFight();
        this.transfertItems();
        if (fight instanceof ExternalFightInfo) {
            LocalPartitionManager.getInstance().removeExternalFight((ExternalFightInfo)fight);
        }
        FightManager.getInstance().destroyFight(fight);
        FightActionGroupManager.getInstance().removePendingGroups(fight.getId());
    }
    
    private void transfertItems() {
        final FightInfo fight = this.getFight();
        FloorItemManager.getInstance().foreachFloorItem(new TObjectProcedure<FloorItem>() {
            @Override
            public boolean execute(final FloorItem floorItem) {
                if (floorItem.getCurrentFightId() == fight.getId()) {
                    floorItem.setRemainingTicksInPhase(-1L);
                    floorItem.setCurrentFightId(0);
                    FloorItemManager.getInstance().resetLockAndPhase(floorItem);
                    floorItem.getFloorItemInteractiveElement().notifyViews();
                }
                return true;
            }
        });
    }
    
    public void addLoosers(final Collection<Long> looserTeamMates) {
        if (looserTeamMates != null) {
            this.m_loosers.addAll(looserTeamMates);
        }
    }
    
    public void addWinners(final Collection<Long> winnerTeamMates) {
        if (winnerTeamMates != null) {
            this.m_winners.addAll(winnerTeamMates);
        }
    }
    
    public void addEscapees(final Collection<Long> escapees) {
        if (escapees != null) {
            this.m_escapees.addAll(escapees);
        }
    }
}
