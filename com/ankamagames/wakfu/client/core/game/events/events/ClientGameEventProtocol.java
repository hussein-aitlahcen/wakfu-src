package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.*;

public enum ClientGameEventProtocol implements ExportableEnum, Parameterized
{
    PLAYER_DEATH(1, ClientEventPlayerDeath.PARAMETERS_LIST_SET), 
    ITEM_ADDED_TO_INVENTORY(2, ClientEventItemAddedToInventory.PARAMETERS_LIST_SET), 
    SEED_PLANTED(3, ClientEventSeedPlanted.PARAMETERS_LIST_SET), 
    WAKFU_GAUGE_UPDATED(4, ClientEventWakfuGaugeUpdate.PARAMETERS_LIST_SET), 
    UI_OPENED(5, ClientEventUIOpened.PARAMETERS_LIST_SET), 
    UI_CLOSED(6, ClientEventUIClosed.PARAMETERS_LIST_SET), 
    VOTE_START(7, ClientEventVoteStart.PARAMETERS_LIST_SET), 
    VOTE_END(8, ClientEventVoteEnd.PARAMETERS_LIST_SET), 
    CHALLENGE_PROPOSAL(9, ClientEventChallengeProposal.PARAMETERS_LIST_SET), 
    TERRITORY_ENTRANCE(10, ClientEventTerritoryEntrance.PARAMETERS_LIST_SET), 
    ACHIEVEMENT_UNLOCKED(11, ClientEventAchievementUnlocked.PARAMETERS_LIST_SET), 
    ENTER_INSTANCE(13, ClientEventEnterInstance.PARAMETERS_LIST_SET), 
    LEVEL_GAIN(14, ClientEventLevelGain.PARAMETERS_LIST_SET), 
    CITIZEN_RANK_CHANGED(15, ClientEventCitizenRankChanged.PARAMETERS_LIST_SET), 
    CRAFT_OPENED(16, ClientEventCraftOpened.PARAMETERS_LIST_SET), 
    ERROR_MESSAGE_RECEIVED(17, ClientEventErrorMessageReceived.PARAMETERS_LIST_SET), 
    PARTITION_CHANGED(18, ClientEventPartitionChanged.PARAMETERS_LIST_SET), 
    ITEM_USED(19, ClientEventItemUsed.PARAMETERS_LIST_SET), 
    TACKLED(20, ClientEventTackled.PARAMETERS_LIST_SET), 
    STATE_APPLIED(21, ClientEventStateApplied.PARAMETERS_LIST_SET), 
    CRAFT_LEARNED(22, ClientEventCraftLearned.PARAMETERS_LIST_SET), 
    SPELL_LEARN(23, ClientEventSpellLearn.PARAMETERS_LIST_SET), 
    INTERACTIVE_ELEMENT_ACTIVATION(24, ClientEventInteractiveElementActivation.PARAMETERS_LIST_SET), 
    ACHIEVEMENT_OBJECTIVE_UNLOCKED(25, ClientEventAchievementObjectiveUnlocked.PARAMETERS_LIST_SET), 
    TITLE_UNLOCKED(26, ClientEventTitleUnlocked.PARAMETERS_LIST_SET), 
    FIGHT_STARTED(27, ClientEventFightStarted.PARAMETERS_LIST_SET), 
    DIALOG_CHOICE_SELECTED(28, ClientEventDialogChoiceSelected.PARAMETERS_LIST_SET), 
    MOVE_IN_FIGHT(29, ClientEventMoveInFight.PARAMETERS_LIST_SET), 
    SPELL_CAST(30, ClientEventSpellCast.PARAMETERS_LIST_SET), 
    ASK_END_TURN(31, ClientEventAskEndTurn.PARAMETERS_LIST_SET), 
    CHARACTER_KO(32, ClientEventCharacterKo.PARAMETERS_LIST_SET), 
    WALKIN_ZONE_TRIGGER(33, ClientEventWalkinZoneTrigger.PARAMETERS_LIST_SET), 
    START_TURN(34, ClientEventStartTurn.PARAMETERS_LIST_SET), 
    JOIN_GROUP(35, ClientEventJointGroup.PARAMETERS_LIST_SET), 
    START_EXCHANGE(36, ClientEventStartExchange.PARAMETERS_LIST_SET), 
    LEAVE_INSTANCE(37, ClientEventLeaveInstance.PARAMETERS_LIST_SET), 
    NULL_EVENT(38, ClientEventNull.PARAMETERS_LIST_SET), 
    GROUP_KILLED_EVENT(39, ClientEventGroupKilled.PARAMETERS_LIST_SET), 
    TUTORIAL_MESSAGE_REMOVED(40, ClientEventTutorialMessageRemoved.PARAMETERS_LIST_SET), 
    NATION_CHANGED(41, ClientEventNationChanged.PARAMETERS_LIST_SET), 
    LOSE_FIGHT(42, ClientEventLoseFight.PARAMETERS_LIST_SET), 
    COMPANION_JOIN_GROUP(43, ClientEventCompanionJointGroup.PARAMETERS_LIST_SET), 
    COMPANION_ENTER_FIGHT(44, ClientEventCompanionEnterFight.PARAMETERS_LIST_SET), 
    COMPANION_END_FIGHT(45, ClientEventCompanionEndFight.PARAMETERS_LIST_SET), 
    COMPANION_OPEN_EQUIPMENT(46, ClientEventCompanionOpenEquipment.PARAMETERS_LIST_SET), 
    FIGHT_CHALLENGE_START(47, ClientEventFightChallengeStart.PARAMETERS_LIST_SET), 
    PVP_BUTTON_CLICKED(48, ClientEventPvpButtonClicked.PARAMETERS_LIST_SET), 
    PVP_ACTIVATION_FINISHED(49, ClientEventPvpActivationFinished.PARAMETERS_LIST_SET), 
    PVP_DISACTIVATION_UNLOCKED(50, ClientEventPvpDisactivationUnlocked.PARAMETERS_LIST_SET), 
    GUARD_JOB_LEARNED(51, ClientEventGuardJobLearned.PARAMETERS_LIST_SET), 
    GUILD_PANEL_OPENED(52, ClientEventGuildPanelOpened.PARAMETERS_LIST_SET);
    
    private int m_id;
    private ParameterListSet m_parameters;
    
    private ClientGameEventProtocol(final int id, final ParameterListSet parameters) {
        this.m_id = id;
        this.m_parameters = parameters;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return this.m_parameters;
    }
    
    public int getId() {
        return this.m_id;
    }
}
