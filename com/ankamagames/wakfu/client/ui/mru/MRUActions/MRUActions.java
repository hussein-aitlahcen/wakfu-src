package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.world.action.*;

public enum MRUActions implements ExportableEnum
{
    OPEN_USER_MAIN_PAGE_ACTION(1, (AbstractMRUAction)new MRUOpenUserMainPageAction()), 
    CHARACTER_CAST_FIGHT_ACTION(2, (AbstractMRUAction)new MRUCastFightAction(FightModel.PVE)), 
    CHARACTER_JOIN_FIGHT_ACTION(3, (AbstractMRUAction)new MRUJoinFightAction()), 
    CHARACTER_EXCHANGE_ACTION(4, (AbstractMRUAction)new MRUExchangeAction()), 
    CHARACTER_KICK_ACTION(5, (AbstractMRUAction)new MRUKickAction()), 
    ITEM_PICK_UP_ACTION(7, (AbstractMRUAction)new MRUPickUpAction()), 
    CHARACTER_CAST_DUEL_FIGHT_ACTION(8, (AbstractMRUAction)new MRUCastFightAction(FightModel.DUEL)), 
    DIMENSIONAL_BAG_ENTER_ACTION(11, (AbstractMRUAction)new MRUDimensionalBagEnterAction()), 
    DIMENSIONAL_BAG_BROWSE_FLEA_ACTION(12, (AbstractMRUAction)new MRUDimensionalBagBrowseFleaAction()), 
    DIMENSIONAL_BAG_CHANGE_ROOM_ACTION(13, (AbstractMRUAction)new MRUDimensionalBagChangeRoomAction()), 
    DIMENSIONAL_BAG_UPDATE_ROOM_ACTION(14, (AbstractMRUAction)new MRUUpgradeDimmensionalBagRoomAction()), 
    COLLECT_ACTION(18, (AbstractMRUAction)new MRUCollectAction()), 
    MANAGE_FLEA_ACTION(28, (AbstractMRUAction)new MRUManageFleaAction()), 
    OPEN_CRAFT_ACTION(29, (AbstractMRUAction)new MRUCraftAction(false)), 
    CHARACTER_CAST_TRAINING_FIGHT_ACTION(31, (AbstractMRUAction)new MRUCastTrainingFightAction()), 
    CHARACTER_CAST_TRAINING_FIGHT_ACTION_2(32, (AbstractMRUAction)new MRUCastTrainingFightAction()), 
    BROWSE_FLEA_ACTION(33, (AbstractMRUAction)new MRUBrowseFleaAction()), 
    FOLLOW_CHARACTER_WITH_COMPASS(34, (AbstractMRUAction)new MRUFollowCharacterWithCompassAction()), 
    INVIT_TO_JOIN_PARTY(36, (AbstractMRUAction)new MRUInvitJoinPartyAction()), 
    INTERACTIF_ACTION(37, (AbstractMRUAction)new MRUInteractifMachine()), 
    GENERIC_INTERACTIVE_ACTION(41, (AbstractMRUAction)new MRUGenericInteractiveAction()), 
    FOLLOW_MONSTER_ACTION(42, (AbstractMRUAction)new MRUMonsterFollowAction()), 
    CREATE_PRIVATE_CHAT_ACTION(43, (AbstractMRUAction)new MRUCreatePrivateChatAction()), 
    REPACK_ITEMIZABLE_ELEMENT_ACTION(44, (AbstractMRUAction)new MRURepackAction()), 
    RECYCLE(45, (AbstractMRUAction)new MRURecycleAction()), 
    MOVE_ITEMIZABLE_ELEMENT_ACTION(46, (AbstractMRUAction)new MRUMoveAction()), 
    ROTATE_ITEMIZABLE_ELEMENT_ACTION(47, (AbstractMRUAction)new MRURotateAction()), 
    RESURRECT_MONSTER_ACTION(49, (AbstractMRUAction)new MRUResurrectMonsterAction(MonsterResurrectionType.NORMAL_RESURRECTION)), 
    RESURRECT_PEST_MONSTER_ACTION(50, (AbstractMRUAction)new MRUResurrectMonsterAction(MonsterResurrectionType.PEST_RESURRECTION)), 
    RESURRECT_PLAYER_ACTION(50, (AbstractMRUAction)new MRUResurrectPlayerAction()), 
    MANAGE_PROTECTOR_ACTION(51, (AbstractMRUAction)new MRUManageProtectorAction()), 
    VOTE_ACTION(53, (AbstractMRUAction)new MRUVoteAction()), 
    SCRIPT_ACTION(54, (AbstractMRUAction)new MRUScriptAction()), 
    ADD_TO_FRIEND_LIST(55, (AbstractMRUAction)new MRUAddToFriendListAction()), 
    ATTEND_FIGHT(57, (AbstractMRUAction)new MRUBecameFightSpectatorAction()), 
    MONSTER_ACTION(61, (AbstractMRUAction)new MRUMonsterAction()), 
    NO_ACTION(62, (AbstractMRUAction)new MRUNoAction()), 
    ZAAP_ACTION(63, (AbstractMRUAction)new MRUTravelAction()), 
    PREVIOUS_VOTE_LIST_ACTION(64, (AbstractMRUAction)new MRUPreviousVoteListAction()), 
    OPEN_CRAFT_FREE_ACTION(65, (AbstractMRUAction)new MRUCraftAction(true)), 
    RESPAWN_POINT_ACTION(66, (AbstractMRUAction)new MRURespawnPointAction()), 
    MARKET_ACTION(66, (AbstractMRUAction)new MRUMarketAction()), 
    BACKGROUND_DISPLAY_ACTION(67, (AbstractMRUAction)new MRUBackgroundDisplayAction()), 
    OPEN_MODERATION_PANEL_ACTION(68, (AbstractMRUAction)new MRUOpenModerationPanelAction()), 
    GUILD_CREATOR_ACTION(69, (AbstractMRUAction)new MRUGuildCreatorAction()), 
    TELEPORTER_ACTION(70, (AbstractMRUAction)new MRUTeleporterAction()), 
    GUILD_STORAGE_BOX_ACTION(71, (AbstractMRUAction)new MRUGuildStorageBoxAction()), 
    LOOT_CHEST_ACTION(72, (AbstractMRUAction)new MRULootChestAction()), 
    GENERIC_ACTIVABLE_ACTION(73, (AbstractMRUAction)new MRUGenericActivableAction()), 
    MANAGE_EQUIPABLE_DUMMY(74, (AbstractMRUAction)new MRUManageEquipableDummy()), 
    MANAGE_BOOKCASE(75, (AbstractMRUAction)new MRUManageBookcase()), 
    ZAAP_OUT_ONLY(76, (AbstractMRUAction)new MRUZaapOutTravelAction()), 
    BROWSE_BOOKCASE(78, (AbstractMRUAction)new MRUBrowseBookcase()), 
    MERGE_GEM(80, (AbstractMRUAction)new MRUMergeGemAction()), 
    FOLLOW_PLAYER(81, (AbstractMRUAction)new MRUFollowPlayerAction()), 
    STREET_LIGHT_ACTION(82, (AbstractMRUAction)new MRUActionWithCost()), 
    HAVEN_WORLD_BOARD_READ(83, (AbstractMRUAction)new MRUHavenWorldBoardReadAction()), 
    HAVEN_WORLD_BOARD_ENTER(84, (AbstractMRUAction)new MRUHavenWorldBoardEnterAction()), 
    CHARACTER_CAST_PVP_FIGHT_MODEL_ACTION(85, (AbstractMRUAction)new MRUCastFightAction(FightModel.PVP)), 
    CHARACTER_CAST_PVP_RANKED_FIGHT_MODEL_ACTION(86, (AbstractMRUAction)new MRUCastFightAction(FightModel.RANKED_NATION_PVP)), 
    CHARACTER_CAST_PROTECTOR_ASSAULT_FIGHT_MODEL_ACTION(87, (AbstractMRUAction)new MRUCastFightAction(FightModel.PROTECTOR_ASSAULT)), 
    CHARACTER_CAST_TRAINING_FIGHT_MODEL_ACTION(88, (AbstractMRUAction)new MRUCastFightAction(FightModel.TRAINING_FIGHT)), 
    CHARACTER_CAST_TRAINING_WITHOUT_REPORT_FIGHT_MODEL_ACTION(89, (AbstractMRUAction)new MRUCastFightAction(FightModel.TRAINING_FIGHT_WITH_XP_WITHOUT_REPORT));
    
    private final int m_actionId;
    private boolean m_usable;
    private final AbstractMRUAction m_model;
    
    public static void setAllUsable(final boolean usable) {
        final MRUActions[] actions = values();
        for (int i = 0, length = actions.length; i < length; ++i) {
            actions[i].m_usable = usable;
        }
    }
    
    private MRUActions(final int actionId, final AbstractMRUAction model) {
        this.m_usable = true;
        this.m_actionId = actionId;
        this.m_model = model;
    }
    
    @Override
    public String getEnumId() {
        return this.toString();
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    public <A extends AbstractMRUAction> A getMRUAction() {
        final AbstractMRUAction action = this.m_model.getCopy();
        action.setUsable(this.m_usable);
        return (A)action;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public AbstractMRUAction getModel() {
        return this.m_model;
    }
    
    public boolean isUsable() {
        return this.m_usable;
    }
    
    public void setUsable(final boolean usable) {
        this.m_usable = usable;
    }
    
    @Override
    public String getEnumComment() {
        return this.getEnumLabel();
    }
    
    public static MRUActions getById(final int id) {
        final MRUActions[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final MRUActions value = values[i];
            if (value.getActionId() == id) {
                return value;
            }
        }
        return null;
    }
}
