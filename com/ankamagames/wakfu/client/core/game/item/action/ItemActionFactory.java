package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class ItemActionFactory
{
    private static final Logger m_logger;
    
    public static AbstractClientItemAction newAction(final int id, final ItemActionConstants actionType) {
        if (actionType == null) {
            ItemActionFactory.m_logger.error((Object)"Impossible de cr\u00e9er une action d'item \u00e0 partir d'une constante 'null'", (Throwable)new IllegalArgumentException());
            return null;
        }
        switch (actionType) {
            case DISASSEMBLE_ITEM: {
                return new DisassembleItemAction(id);
            }
            case SEED_ACTION: {
                return new SeedItemAction(id);
            }
            case PLAY_SCRIPT: {
                return new PlayScriptItemAction(id);
            }
            case DEPLOY_ELEMENT: {
                return new DeployElementItemAction(id);
            }
            case SPAWN_MONSTER: {
                return new SpawnMonsterItemAction(id);
            }
            case START_SCENARIO: {
                return new StartScenarioItemAction(id);
            }
            case GIVE_KAMAS: {
                return new AddKamasItemAction(id);
            }
            case TELEPORT: {
                return new TeleportItemAction(id);
            }
            case GIVE_TITLE: {
                return new AddTitleItemAction(id);
            }
            case OPEN_BACKGROUND_DISPLAY: {
                return new OpenBackgroundDisplayItemAction(id);
            }
            case OPEN_PASSPORT: {
                return new OpenPassportItemAction(id);
            }
            case LEARN_EMOTE: {
                return new LearnEmoteItemAction(id);
            }
            case ADD_WORLD_POSITION_MARKER: {
                return new AddWorldPositionMarkerItemAction(id);
            }
            case REDUCE_DEAD_STATE: {
                return new ReduceDeadStateItemAction(id);
            }
            case PLAY_EMOTE: {
                return new PlayEmoteItemAction(id);
            }
            case ACTIVATE_ACHIEVEMENT: {
                return new ActivateAchievementItemAction(id);
            }
            case FOLLOW_ACHIEVEMENT: {
                return new FollowAchievementItemAction(id);
            }
            case MERGE_ITEMS: {
                return new MergeItemsItemAction(id);
            }
            case LEARN_DIMENSIONAL_BAG_VIEW: {
                return new LearnDimensionalBagViewItemAction(id);
            }
            case GIVE_XP: {
                return new GiveXpItemAction(id);
            }
            case RESET_ACHIEVEMENT: {
                return new ResetAchievementItemAction(id);
            }
            case GIVE_LEVELS_TO_SPELL_BRANCH: {
                return new GiveLevelsToSpellBranchItemAction(id);
            }
            case GIVE_LEVELS_TO_ALL_SPELL_BRANCH: {
                return new GiveLevelsToAllSpellBranchItemAction(id);
            }
            case SPLIT_ITEM_SET: {
                return new SplitItemSetItemAction(id);
            }
            case GIVE_APTITUDE_LEVELS: {
                return new GiveAptitudeLevelsItemAction(id);
            }
            case GIVE_RANDOM_ITEM_IN_LIST: {
                return new GiveRandomItemInListItemAction(id);
            }
            case SPELL_RESTAT: {
                return new SpellRestatItemAction(id);
            }
            case APTITUDE_RESTAT: {
                return new AptitudeRestatItemAction(id);
            }
            case TP_TO_RESPAWN_POINT: {
                return new TpToRespawnPointItemAction(id);
            }
            case CONSUME_KROSMOZ_FIGURE: {
                return new AddKrosmozFigureItemAction(id);
            }
            case ACTIVATE_RESTAT: {
                return new ActivateRestatItemAction(id);
            }
            case SEARCH_TREASURE: {
                return new SearchTreasureItemAction(id);
            }
            case REMOVE_LAST_GEM: {
                return new RemoveLastGemItemAction(id);
            }
            case GIVE_ITEMS: {
                return new GiveItemsItemAction(id);
            }
            case CHANGE_NATION: {
                return new ChangeNationItemAction(id);
            }
            case KILL_MONSTERS_IN_RADIUS: {
                return new KillMonstersInRadiusItemAction(id);
            }
            case COMPANION_ACTIVATION: {
                return new CompanionActivationItemAction(id);
            }
            case CHANGE_GEM_TYPE: {
                return new ChangeGemItemAction(id);
            }
            case EXTENDS_RENT_DURATION: {
                return new ExtendsRentDurationItemAction(id);
            }
            case ALL_SPELL_RESTAT: {
                return new AllSpellRestatAction(id);
            }
            case COMMON_APTITUDE_RESTAT_ACTION: {
                return new CommonAptitudeRestatAction(id);
            }
            case RECUSTOM: {
                return new RecustomCharacterItemAction(id);
            }
            case INSTANCE_SPEAKER: {
                return new InstanceSpeakerItemAction(id);
            }
            case PET_XP: {
                return new PetXpItemAction(id);
            }
            case PET_HP: {
                return new PetHpItemAction(id);
            }
            case LEARN_COSTUME: {
                return new LearnCostumeItemAction(id);
            }
            case LEARN_PET_EQUIPMENT: {
                return new LearnPetEquipmentItemAction(id);
            }
            case REROLL_ELEMENTS: {
                return new RerollElementsItemAction(id);
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemActionFactory.class);
    }
}
