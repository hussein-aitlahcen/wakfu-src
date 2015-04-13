package com.ankamagames.wakfu.client.network;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gift.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.container.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.system.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.resource.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.xp.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.monster.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.worldaction.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.room.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.flea.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.dimensionalBag.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.skill.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gameAction.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.group.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.ecosystem.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.chaos.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.zone.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.weather.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.errorMessage.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.recustom.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.monster.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.playerTitle.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.convention.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dialog.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.craft.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.emote.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.map.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.collector.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.storageBox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.vault.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.nation.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.market.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pet.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.xp.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.bind.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.inventory.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gems.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildStorage.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildLadder.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.travel.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.restat.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pvp.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.pvp.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.auth.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.krosmoz.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.lock.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fightChallenge.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.seedSpreader.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import com.ankamagames.wakfu.client.network.protocol.message.craft.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch.*;

public class WakfuMessageDecoder extends AbstractClientMessageDecoder
{
    protected static final Logger m_logger;
    
    @Override
    protected Message createMessageFromType(final int msgType) {
        Message msg = null;
        switch (msgType) {
            case 1025: {
                msg = new ClientAuthenticationResultsMessage();
                break;
            }
            case 1202: {
                msg = new WorldSelectionResultMessage();
                break;
            }
            case 1208: {
                msg = new ConnectionQueuedMessage();
                break;
            }
            case 1042: {
                msg = new SteamLoginResultMessage();
                break;
            }
            case 1034: {
                msg = new ClientPublicKeyMessage();
                break;
            }
            case 1028: {
                msg = new ClientAccountInformationUpdateMessage();
                break;
            }
            case 1027: {
                msg = new ClientDispatchAuthenticationResultMessage();
                break;
            }
            case 1210: {
                msg = new OAuthAuthenticationTokenResultMessage();
                break;
            }
            case 1212: {
                msg = new WakfuAuthenticationTokenResultMessage();
                break;
            }
            case 1036: {
                msg = new ClientProxiesResultMessage();
                break;
            }
            case 1037: {
                msg = new ClientSecurityCardRequestMessage();
                break;
            }
            case 2048: {
                msg = new CharactersListMessage();
                break;
            }
            case 2050: {
                msg = new CharacterSelectionResultMessage();
                break;
            }
            case 2056: {
                msg = new CharacterChoiceLeaveResultMessage();
                break;
            }
            case 2054: {
                msg = new CharacterCreationResultMessage();
                break;
            }
            case 2052: {
                msg = new CharacterDeletionResultMessage();
                break;
            }
            case 2080: {
                msg = new DeleteForbiddenCharacterModerationMessage();
                break;
            }
            case 2058: {
                msg = new StatisticsUpdateResultMessage();
                break;
            }
            case 2077: {
                msg = new CompanionListMessage();
                break;
            }
            case 2081: {
                msg = new ClientSystemConfigurationUpdateValueMessage();
                break;
            }
            case 2078: {
                msg = new FreeCompanionBreedIdMessage();
                break;
            }
            case 2063: {
                msg = new ClientCalendarSynchronizationMessage();
                break;
            }
            case 2067: {
                msg = new ClientSystemConfigurationMessage();
                break;
            }
            case 2069: {
                msg = new ClientAdditionalCharacterSlotsUpdateMessage();
                break;
            }
            case 2061: {
                msg = new WhoisResponseMessage();
                break;
            }
            case 2070: {
                msg = new CharacterRenameOrderMessage();
                break;
            }
            case 2072: {
                msg = new CharacterRenameResultMessage();
                break;
            }
            case 2074: {
                msg = new SecretQuestionMessage();
                break;
            }
            case 2076: {
                msg = new SecretAnswerResultMessage();
                break;
            }
            case 1039: {
                msg = new ClientNickNameRequestMessage();
                break;
            }
            case 13000: {
                msg = new GiftInventoryResultMessage();
                break;
            }
            case 13004: {
                msg = new GiftConsumeResultMessage();
                break;
            }
            case 10056: {
                msg = new ContainerBrowseResultMessage();
                break;
            }
            case 10052: {
                msg = new ContainerDepositResultMessage();
                break;
            }
            case 10058: {
                msg = new ContainerSetLockResultMessage();
                break;
            }
            case 10054: {
                msg = new ContainerWithdrawResultMessage();
                break;
            }
            case 10060: {
                msg = new ContainerSpawnMessage();
                break;
            }
            case 10062: {
                msg = new ContainerDespawnMessage();
                break;
            }
            case 10064: {
                msg = new ContainerMoveMessage();
                break;
            }
            case 10030: {
                msg = new PSRoomPutContainerResultMessage();
                break;
            }
            case 10032: {
                msg = new PSRoomRemoveContainerResultMessage();
                break;
            }
            case 10034: {
                msg = new PSRoomMoveContainerResultMessage();
                break;
            }
            case 10036: {
                msg = new InventoryToRoomGemExchangeResult();
                break;
            }
            case 10038: {
                msg = new RoomToInventoryGemExchangeResult();
                break;
            }
            case 10040: {
                msg = new RoomsGemsExchangeResult();
                break;
            }
            case 10048: {
                msg = new CloseUIHavenBagKicked();
                break;
            }
            case 11002: {
                msg = new GoToWorldSelectionResultMessage();
                break;
            }
            case 4200: {
                msg = new ResourceSpawnMessage();
                break;
            }
            case 4202: {
                msg = new PartitionResourceInfoMessage();
                break;
            }
            case 4204: {
                msg = new ResourceSynchroMessage();
                break;
            }
            case 4201: {
                msg = new ResourceModificationMessage();
                break;
            }
            case 9506: {
                msg = new ResourceResultMessage();
                break;
            }
            case 9508: {
                msg = new SearchTreasureResultMessage();
                break;
            }
            case 9504: {
                msg = new WeatherZoneInformationMessage();
                break;
            }
            case 4098: {
                msg = new CharacterInformationMessage();
                break;
            }
            case 15650: {
                msg = new CharacterEnterHavenWorldMessage();
                break;
            }
            case 15652: {
                msg = new HavenWorldManageActionResult();
                break;
            }
            case 15655: {
                msg = new HavenWorldBuildingFactorUpdate();
                break;
            }
            case 4100: {
                msg = new CharacterEnterWorldMessage();
                break;
            }
            case 4104: {
                msg = new ActorDespawnMessage();
                break;
            }
            case 4106: {
                msg = new PlayerUnstuckMessage();
                break;
            }
            case 4116: {
                msg = new ActorSetActionMessage();
                break;
            }
            case 4126: {
                msg = new ActorTeleportMessage();
                break;
            }
            case 4128: {
                msg = new ActorLeaveInstanceMessage();
                break;
            }
            case 4115: {
                msg = new ActorStopMovementMessage();
                break;
            }
            case 4114: {
                msg = new ActorPathUpdateMessage();
                break;
            }
            case 4127: {
                msg = new ActorMoveToMessage();
                break;
            }
            case 4118: {
                msg = new ActorChangeDirectionMessage();
                break;
            }
            case 5206: {
                msg = new ActorEquipmentUpdateMessage();
                break;
            }
            case 8036: {
                msg = new AggroActorIsWatchingLPCMessage();
                break;
            }
            case 8402: {
                msg = new ActorSpellLearnMessage();
                break;
            }
            case 4216: {
                msg = new ActorUpdatePropertiesMessage();
                break;
            }
            case 4210: {
                msg = new ActorNonCombatSkillXpGainedMessage();
                break;
            }
            case 4214: {
                msg = new PlayerXpModificationMessage();
                break;
            }
            case 4220: {
                msg = new OtherPlayerLevelUpMessage();
                break;
            }
            case 8408: {
                msg = new ActorLevelUpAptitudeResultMessage();
                break;
            }
            case 8417: {
                msg = new LevelUpNewAptitudeResultMessage();
                break;
            }
            case 4000: {
                msg = new ErrorResultMessage();
                break;
            }
            case 4206: {
                msg = new StartCollectOnInteractiveElementMessage();
                break;
            }
            case 4124: {
                msg = new CharacterHealthUpdateMessage();
                break;
            }
            case 5402: {
                msg = new SymbiotHealthUpdateMessage();
                break;
            }
            case 4130: {
                msg = new CharacterUpdateMessage();
                break;
            }
            case 15132: {
                msg = new CrimePurgationStartedMessage();
                break;
            }
            case 15133: {
                msg = new CrimePurgationCancelMessage();
                break;
            }
            case 4170: {
                msg = new SimpleOccupationModificationMessage();
                break;
            }
            case 4181: {
                msg = new SimpleOccupationModificationActionMessage();
                break;
            }
            case 4172: {
                msg = new PlantOccupationStartMessage();
                break;
            }
            case 4218: {
                msg = new SearchTreasureOccupationStartMessage();
                break;
            }
            case 4174: {
                msg = new CollectOccupationModifMessage();
                break;
            }
            case 5200: {
                msg = new ItemInventoryResetMessage();
                break;
            }
            case 5300: {
                msg = new ItemIdCacheUpdateMessage();
                break;
            }
            case 5208: {
                msg = new ItemPickedUpMessage();
                break;
            }
            case 5212: {
                msg = new ItemQuantityUpdateMessage();
                break;
            }
            case 5251: {
                msg = new ItemRentInfoUpdateMessage();
                break;
            }
            case 5210: {
                msg = new ItemSpawnInInventoryMessage();
                break;
            }
            case 5214: {
                msg = new TemporaryInventoryModificationMessage();
                break;
            }
            case 5216: {
                msg = new TemporaryInventoryItemMovedMessage();
                break;
            }
            case 4102: {
                msg = new ActorSpawnMessage();
                break;
            }
            case 4108: {
                msg = new MonsterBehaviourMessage();
                break;
            }
            case 4530: {
                msg = new MonsterActionMessage();
                break;
            }
            case 4110: {
                msg = new MonsterEvolutionMessage();
                break;
            }
            case 4526: {
                msg = new MonsterCollectNotificationMessage();
                break;
            }
            case 4112: {
                msg = new MonsterStateMessage();
                break;
            }
            case 4134: {
                msg = new MonsterGroupUpdateMessage();
                break;
            }
            case 7902: {
                msg = new FightInvitationMessage();
                break;
            }
            case 7904: {
                msg = new FightInvitationCancelledMessage();
                break;
            }
            case 8000: {
                msg = new FightCreationMessage();
                break;
            }
            case 8042: {
                msg = new ReconnectionInFightMessage();
                break;
            }
            case 8043: {
                msg = new CharacterEffectManagerForReconnectionMessage();
                break;
            }
            case 8044: {
                msg = new CharacterDataForReconnectionMessage();
                break;
            }
            case 8046: {
                msg = new CharacterPublicCharacteristicsDataMessage();
                break;
            }
            case 8045: {
                msg = new SpellCastHistoryMessage();
                break;
            }
            case 8415: {
                msg = new PlayerChangeConnectionStateMessage();
                break;
            }
            case 8038: {
                msg = new FightCreationForSpectatorMessage();
                break;
            }
            case 7998: {
                msg = new FightCreationErrorMessage();
                break;
            }
            case 8010: {
                msg = new FightTimelineRecoveryMessage();
                break;
            }
            case 7906: {
                msg = new FightersPlacementPositionMessage();
                break;
            }
            case 8012: {
                msg = new FightChangeParametersMessage();
                break;
            }
            case 8014: {
                msg = new FightChangeTimePointGapMessage();
                break;
            }
            case 8016: {
                msg = new AwaitingFightersNotificationMessage();
                break;
            }
            case 8006: {
                msg = new ExternalFightCreationMessage();
                break;
            }
            case 8002: {
                msg = new FightJoinMessage();
                break;
            }
            case 8004: {
                msg = new FightJoinFailureMessage();
                break;
            }
            case 8005: {
                msg = new FighterUnsummonedMessage();
                break;
            }
            case 8026: {
                msg = new FightPlacementStartMessage();
                break;
            }
            case 8028: {
                msg = new FightPlacementEndMessage();
                break;
            }
            case 8150: {
                msg = new FighterReadyMessage();
                break;
            }
            case 8154: {
                msg = new CallHelpMessage();
                break;
            }
            case 8158: {
                msg = new LockFightMessage();
                break;
            }
            case 8308: {
                msg = new PointEffectSelectedNotificationMessage();
                break;
            }
            case 8310: {
                msg = new PointEffectSelectionActivationMessage();
                break;
            }
            case 8030: {
                msg = new StartActionMessage();
                break;
            }
            case 8114: {
                msg = new FighterEquipmentChangedMessage();
                break;
            }
            case 8300: {
                msg = new EndFightMessage();
                break;
            }
            case 8304: {
                msg = new FightReportMessage();
                break;
            }
            case 8410: {
                msg = new FighterSpeakMessage();
                break;
            }
            case 4300: {
                msg = new InteractiveElementUpdateActionMessage();
                break;
            }
            case 6200: {
                msg = new EffectAreaActionMessage();
                break;
            }
            case 6204: {
                msg = new EffectAreaDespawnMessage();
                break;
            }
            case 8100: {
                msg = new TableTurnBeginMessage();
                break;
            }
            case 8104: {
                msg = new FighterTurnBeginMessage();
                break;
            }
            case 8106: {
                msg = new FighterTurnEndMessage();
                break;
            }
            case 8034: {
                msg = new ItemPickedUpInFightMessage();
                break;
            }
            case 8033: {
                msg = new ItemPickedUpByOtherMessage();
                break;
            }
            case 8200: {
                msg = new FightActionSequenceExecuteMessage();
                break;
            }
            case 8202: {
                msg = new EndFightCreationMessage();
                break;
            }
            case 8120: {
                msg = new RunningEffectActionMessage();
                break;
            }
            case 8124: {
                msg = new RunningEffectApplicationMessage();
                break;
            }
            case 8122: {
                msg = new RunningEffectUnapplicationMessage();
                break;
            }
            case 6320: {
                msg = new RunningEffectWorldActionMessage();
                break;
            }
            case 6322: {
                msg = new RunningEffectWorldApplicationMessage();
                break;
            }
            case 6324: {
                msg = new RunningEffectWorldUnapplicationMessage();
                break;
            }
            case 6300: {
                msg = new WorldActionSequenceExecute();
                break;
            }
            case 4122: {
                msg = new ApplyStateMessage();
                break;
            }
            case 4123: {
                msg = new UnapplyStateMessage();
                break;
            }
            case 8040: {
                msg = new DebugFightAccessSquareMessage();
                break;
            }
            case 8110: {
                msg = new SpellCastExecutionMessage();
                break;
            }
            case 8116: {
                msg = new SpellCastNotificationMessage();
                break;
            }
            case 8108: {
                msg = new FighterItemUseMessage();
                break;
            }
            case 4506: {
                msg = new FighterTackledMessage();
                break;
            }
            case 4508: {
                msg = new FighterInvisibleDetectedMessage();
                break;
            }
            case 4524: {
                msg = new FighterMoveMessage();
                break;
            }
            case 4528: {
                msg = new FighterPositionMessage();
                break;
            }
            case 4520: {
                msg = new FighterActivityChangeMessage();
                break;
            }
            case 4522: {
                msg = new FighterChangeDirectionMessage();
                break;
            }
            case 8412: {
                msg = new MoverHitInvisibleFighterMessage();
                break;
            }
            case 8302: {
                msg = new FighterFledFromFightMessage();
                break;
            }
            case 8156: {
                msg = new CellReportMessage();
                break;
            }
            case 6050: {
                msg = new ExchangeEndMessage();
                break;
            }
            case 6004: {
                msg = new ExchangeInvitationConfirmationMessage();
                break;
            }
            case 6002: {
                msg = new ExchangeInvitationMessage();
                break;
            }
            case 6010: {
                msg = new ExchangeItemAddedMessage();
                break;
            }
            case 6012: {
                msg = new ExchangeItemRemovedMessage();
                break;
            }
            case 6014: {
                msg = new ExchangeSetCashMessage();
                break;
            }
            case 6022: {
                msg = new ExchangeUserReadyMessage();
                break;
            }
            case 10008: {
                msg = new PSAddUserResultMessage();
                break;
            }
            case 10014: {
                msg = new PSChangeGroupPermsResultMessage();
                break;
            }
            case 10012: {
                msg = new PSChangeUserPermsResultMessage();
                break;
            }
            case 10004: {
                msg = new PSEnterResultMessage();
                break;
            }
            case 10016: {
                msg = new PSKickUserResultMessage();
                break;
            }
            case 10006: {
                msg = new PSLeaveResultMessage();
                break;
            }
            case 10010: {
                msg = new PSRemoveUserResultMessage();
                break;
            }
            case 10018: {
                msg = new PSUpdateMessage();
                break;
            }
            case 10106: {
                msg = new FleaContentMessage();
                break;
            }
            case 10114: {
                msg = new DimensionalBagAllFleasContentMessage();
                break;
            }
            case 10118: {
                msg = new MerchantInventoryAddedMessage();
                break;
            }
            case 10120: {
                msg = new MerchantInventoryRemovedMessage();
                break;
            }
            case 10116: {
                msg = new DimensionalBagFleaContentMessage();
                break;
            }
            case 10108: {
                msg = new FleaLockMessage();
                break;
            }
            case 5232: {
                msg = new MerchantItemAddMessage();
                break;
            }
            case 5236: {
                msg = new MerchantItemRemoveMessage();
                break;
            }
            case 5238: {
                msg = new MerchantItemUpdateMessage();
                break;
            }
            case 10000: {
                msg = new DimensionalBagSpawnMessage();
                break;
            }
            case 5234: {
                msg = new FleaBuyResultMessage();
                break;
            }
            case 5244: {
                msg = new ItemSoldNotificationMessage();
                break;
            }
            case 5240: {
                msg = new WalletUpdateMessage();
                break;
            }
            case 5242: {
                msg = new OfflineFleaTransactionSummaryMessage();
                break;
            }
            case 10002: {
                msg = new DimensionalBagDespawnMessage();
                break;
            }
            case 10022: {
                msg = new PSRoomAddUserResultMessage();
                break;
            }
            case 10024: {
                msg = new PSRoomRemoveUserResultMessage();
                break;
            }
            case 10042: {
                msg = new FetchTransactionLogResultMessage();
                break;
            }
            case 10044: {
                msg = new DimensionalBagPermissionsUpdateMessage();
                break;
            }
            case 4125: {
                msg = new CharacterEnterPartitionMessage();
                break;
            }
            case 4141: {
                msg = new ActorPlantStartMessage();
                break;
            }
            case 4142: {
                msg = new ActorPlantResultMessage();
                break;
            }
            case 4180: {
                msg = new ActorSearchTreasureResultMessage();
                break;
            }
            case 4168: {
                msg = new CollectMonsterOccupationModifMessage();
                break;
            }
            case 11100: {
                msg = new RunClientEventMessage();
                break;
            }
            case 15800: {
                msg = new GameActionPlayScriptMessage();
                break;
            }
            case 11200: {
                msg = new TerminateScenarioMessage();
                break;
            }
            case 11202: {
                msg = new ChallengeActionLoadedMessage();
                break;
            }
            case 11208: {
                msg = new RewardMessage();
                break;
            }
            case 11102: {
                msg = new AddItemToInventoryMessage();
                break;
            }
            case 11110: {
                msg = new RemoveItemFromInventoryMessage();
                break;
            }
            case 11108: {
                msg = new UnEquipEventMessage();
                break;
            }
            case 11112: {
                msg = new RemoveItemFromInventoryWithReferenceIdMessage();
                break;
            }
            case 11106: {
                msg = new ActorEquipmentPositionCleared();
                break;
            }
            case 11204: {
                msg = new ChallengeVarUpdatedMessage();
                break;
            }
            case 11210: {
                msg = new ChallengeProposalMessage();
                break;
            }
            case 11228: {
                msg = new ChallengeRankingMessage();
                break;
            }
            case 11206: {
                msg = new ChallengeActionCompletedMessage();
                break;
            }
            case 12404: {
                msg = new GroupInvitAnswerDispatchMessage();
                break;
            }
            case 11214: {
                msg = new ChallengeInformationMessage();
                break;
            }
            case 12600: {
                msg = new WakfuZoneInformationMessage();
                break;
            }
            case 12602: {
                msg = new PlayerWakfuGaugeModificationMessage();
                break;
            }
            case 9200: {
                msg = new ActorPlayAnimationMessage();
                break;
            }
            case 9202: {
                msg = new ActorChangeStaticAnimationKeyMessage();
                break;
            }
            case 9201: {
                msg = new ActorPlayApsMessage();
                break;
            }
            case 11216: {
                msg = new CreateGraphicalInputRequestMessage();
                break;
            }
            case 5504: {
                msg = new PlayerTitleChangedMessage();
                break;
            }
            case 5506: {
                msg = new PlayerTitleListMessage();
                break;
            }
            case 11218: {
                msg = new SetChallengeTarget();
                break;
            }
            case 11220: {
                msg = new ChallengeStatusMessage();
                break;
            }
            case 11222: {
                msg = new FailChallengeMessage();
                break;
            }
            case 5302: {
                msg = new ItemActionPlayScriptMessage();
                break;
            }
            case 15001: {
                msg = new TimeResultMessage();
                break;
            }
            case 9301: {
                msg = new TerritoryChaosMessage();
                break;
            }
            case 9303: {
                msg = new ProtectorChaosMessage();
                break;
            }
            case 9305: {
                msg = new InteractiveElementChaosMessage();
                break;
            }
            case 15206: {
                msg = new BoatEventMessage();
                break;
            }
            case 15120: {
                msg = new NationCandidateRegistrationFailedMessage();
                break;
            }
            case 15122: {
                msg = new ClientNationCandidateRegistrationResultMessage();
                break;
            }
            case 4184: {
                msg = new ActorRecycleResultMessage();
                break;
            }
            case 15200: {
                msg = new AddZoneBuffMessage();
                break;
            }
            case 15202: {
                msg = new RemoveZoneBuffMessage();
                break;
            }
            case 15204: {
                msg = new SetZoneBuffsMessage();
                break;
            }
            case 11230: {
                msg = new SetChallengeCountdownMessage();
                break;
            }
            case 11232: {
                msg = new StopChallengeCountdownMessage();
                break;
            }
            case 9602: {
                msg = new WeatherHistoryAnswerMessage();
                break;
            }
            case 3140: {
                msg = new ChannelContentMessage();
                break;
            }
            case 3144: {
                msg = new FriendListMessage();
                break;
            }
            case 3146: {
                msg = new IgnoreListMessage();
                break;
            }
            case 3148: {
                msg = new NotificationFriendOnlineMessage();
                break;
            }
            case 3150: {
                msg = new NotificationFriendOfflineMessage();
                break;
            }
            case 3164: {
                msg = new NotificationIgnoreOnlineMessage();
                break;
            }
            case 3166: {
                msg = new NotificationIgnoreOfflineMessage();
                break;
            }
            case 3152: {
                msg = new VicinityContentMessage();
                break;
            }
            case 3168: {
                msg = new TradeContentMessage();
                break;
            }
            case 3176: {
                msg = new RecruteContentMessage();
                break;
            }
            case 3174: {
                msg = new VicinityPoliticContentMessage();
                break;
            }
            case 3170: {
                msg = new TeamContentMessage();
                break;
            }
            case 3154: {
                msg = new PrivateContentMessage();
                break;
            }
            case 3178: {
                msg = new ModeratorChatCreationMessage();
                break;
            }
            case 3180: {
                msg = new ModeratorNumRequestUpdateMessage();
                break;
            }
            case 3182: {
                msg = new ModeratorRequestClosedMessage();
                break;
            }
            case 3222: {
                msg = new HasModerationRequestMessage();
                break;
            }
            case 3220: {
                msg = new ModeratorRequestErrorMessage();
                break;
            }
            case 3156: {
                msg = new FriendAddedMessage();
                break;
            }
            case 3160: {
                msg = new FriendRemovedMessage();
                break;
            }
            case 3158: {
                msg = new IgnoreAddedMessage();
                break;
            }
            case 3162: {
                msg = new IgnoreRemovedMessage();
                break;
            }
            case 3300: {
                msg = new RedModerationMessage();
                break;
            }
            case 3202: {
                msg = new ChannelNotFoundMessage();
                break;
            }
            case 3204: {
                msg = new UserNotFoundMessage();
                break;
            }
            case 3214: {
                msg = new TargetIsYourselfMessage();
                break;
            }
            case 3216: {
                msg = new OperationNotPermitedMessage();
                break;
            }
            case 3218: {
                msg = new UserIgnoreYouMessage();
                break;
            }
            case 3221: {
                msg = new WorldPropertyTypeErrorMessage();
                break;
            }
            case 500: {
                msg = new GroupGlobalDataUpdateMessage();
                break;
            }
            case 502: {
                msg = new GroupInvitationRequestMessage();
                break;
            }
            case 504: {
                msg = new GroupResultMessage();
                break;
            }
            case 506: {
                msg = new GroupRemovedCharacterMessage();
                break;
            }
            case 516: {
                msg = new GroupDestroyedMessage();
                break;
            }
            case 508: {
                msg = new GroupPrivateContentDispatchMessage();
                break;
            }
            case 528: {
                msg = new GuildPrivateContentDispatchMessage();
                break;
            }
            case 20054: {
                msg = new CreateGuildAnswerMessage();
                break;
            }
            case 526: {
                msg = new GuildInfoAnswerMessage();
                break;
            }
            case 562: {
                msg = new EventsCalendarInfosAnswerMessage();
                break;
            }
            case 564: {
                msg = new AddCalendarEventAnswerMessage();
                break;
            }
            case 566: {
                msg = new RemoveCalendarEventAnswerMessage();
                break;
            }
            case 568: {
                msg = new CalendarEventNotificationMessage();
                break;
            }
            case 570: {
                msg = new RegisterToEventAnswerMessage();
                break;
            }
            case 574: {
                msg = new ValidateParticipationResultMessage();
                break;
            }
            case 576: {
                msg = new UnvalidateParticipationResultMessage();
                break;
            }
            case 578: {
                msg = new CalendarEventsUpdateMessage();
                break;
            }
            case 582: {
                msg = new EventParticipationServerAnswerMessage();
                break;
            }
            case 580: {
                msg = new EventParticipationServerRequestMessage();
                break;
            }
            case 584: {
                msg = new CalendarEventsInformationMessage();
                break;
            }
            case 586: {
                msg = new SetEventEndDateResultMessage();
                break;
            }
            case 594: {
                msg = new SetEventStartDateResultMessage();
                break;
            }
            case 588: {
                msg = new SetEventDescResultMessage();
                break;
            }
            case 592: {
                msg = new EventActionNotificationMessage();
                break;
            }
            case 590: {
                msg = new SetEventTitleResultMessage();
                break;
            }
            case 596: {
                msg = new ModifyCalendarEventResultMessage();
                break;
            }
            case 20000: {
                msg = new ClientNationSynchronizationMessage();
                break;
            }
            case 20004: {
                msg = new NationVoteStartEventMessage();
                break;
            }
            case 20006: {
                msg = new NationVoteEndEventMessage();
                break;
            }
            case 20010: {
                msg = new NationVoteEndWhileDeconnectedEventMessage();
                break;
            }
            case 20008: {
                msg = new NationCandidateDesistResultMessage();
                break;
            }
            case 20002: {
                msg = new ClientCharacterUpdateMessage();
                break;
            }
            case 15300: {
                msg = new ProtectorAttackedMessage();
                break;
            }
            case 15302: {
                msg = new ProtectorDefendedMessage();
                break;
            }
            case 15304: {
                msg = new ProtectorDefeatedMessage();
                break;
            }
            case 15306: {
                msg = new ProtectorAcquiredMessage();
                break;
            }
            case 2065: {
                msg = new RecustomMessage();
                break;
            }
            case 15776: {
                msg = new RecustomPrepareMessage();
                break;
            }
            case 15308: {
                msg = new ProtectorNationChangedMessage();
                break;
            }
            case 15317: {
                msg = new GetProtectorFightStakeAnswerMessage();
                break;
            }
            case 15320: {
                msg = new ProtectorManagementAnswerMessage();
                break;
            }
            case 15334: {
                msg = new ProtectorTaxUpdateAnswerMessage();
                break;
            }
            case 15326: {
                msg = new InstanceProtectorsUpdateMessage();
                break;
            }
            case 15402: {
                msg = new ActorPlayEmoteAnswerMessage();
                break;
            }
            case 11224: {
                msg = new ChallengeSuccessMessage();
                break;
            }
            case 11226: {
                msg = new ChallengeEntitiesMessage();
                break;
            }
            case 15405: {
                msg = new MonsterSpeakMessage();
                break;
            }
            case 15328: {
                msg = new ProtectorSatisfactionChangedMessage();
                break;
            }
            case 15602: {
                msg = new AchievementCompleteMessage();
                break;
            }
            case 15600: {
                msg = new AchievementObjectiveCompleteMessage();
                break;
            }
            case 15604: {
                msg = new AchievementVariableUpdateMessage();
                break;
            }
            case 15606: {
                msg = new AchievementActivatedMessage();
                break;
            }
            case 15608: {
                msg = new AchievementFollowedMessage();
                break;
            }
            case 15612: {
                msg = new AchievementActivationRequestMessage();
                break;
            }
            case 15614: {
                msg = new AchievementFailedMessage();
                break;
            }
            case 15616: {
                msg = new AchievementSharedAnswerMessage();
                break;
            }
            case 15618: {
                msg = new AchievementSharedFeedbackMessage();
                break;
            }
            case 15610: {
                msg = new AchievementResetMessage();
                break;
            }
            case 5502: {
                msg = new PlayerTitleUnlockedMessage();
                break;
            }
            case 15700: {
                msg = new ConventionGiftMessage();
                break;
            }
            case 15704: {
                msg = new DisplayDialogRequestMessage();
                break;
            }
            case 15702: {
                msg = new ValidateDialogResultMessage();
                break;
            }
            case 15710: {
                msg = new CraftLearnedMessage();
                break;
            }
            case 15712: {
                msg = new CraftLearnedRecipeMessage();
                break;
            }
            case 15714: {
                msg = new CraftXpGainedMessage();
                break;
            }
            case 15716: {
                msg = new CraftOccupationStartedMessage();
                break;
            }
            case 15720: {
                msg = new VisualAnimationMessage();
                break;
            }
            case 11118: {
                msg = new BagOperationsMessage();
                break;
            }
            case 11120: {
                msg = new InventoryToEquipmentResultMessage();
                break;
            }
            case 11122: {
                msg = new EquipmentToInventoryResultMessage();
                break;
            }
            case 11130: {
                msg = new EquipCostumeResultMessage();
                break;
            }
            case 5222: {
                msg = new AddBagResultMessage();
                break;
            }
            case 5224: {
                msg = new RemoveBagResultMessage();
                break;
            }
            case 13010: {
                msg = new RollRandomElementsResultMessage();
                break;
            }
            case 5228: {
                msg = new ChangeBagPositionResultMessage();
                break;
            }
            case 15718: {
                msg = new CraftOccupationResultMessage();
                break;
            }
            case 15722: {
                msg = new EmoteLearnedMessage();
                break;
            }
            case 15726: {
                msg = new DimensionalBagViewLearnedMessage();
                break;
            }
            case 15962: {
                msg = new LandMarkLearnedMessage();
                break;
            }
            case 15724: {
                msg = new SitOccupationMessage();
                break;
            }
            case 15730: {
                msg = new CollectorContentMessage();
                break;
            }
            case 15972: {
                msg = new StorageBoxCompartmentContentMessage();
                break;
            }
            case 15671: {
                msg = new VaultConsultResultMessage();
                break;
            }
            case 15674: {
                msg = new VaultReloadedMessage();
                break;
            }
            case 20012: {
                msg = new NationVoteInformationResultMessage();
                break;
            }
            case 20020: {
                msg = new NationGovernmentInformationResultMessage();
                break;
            }
            case 20028: {
                msg = new NationGovernmentDetailResultMessage();
                break;
            }
            case 15124: {
                msg = new NationGovernmentNominationConfirmationRequestMessage();
                break;
            }
            case 20024: {
                msg = new NationGovernmentNominationResultMessage();
                break;
            }
            case 20026: {
                msg = new NationGovernmentRevokeResultMessage();
                break;
            }
            case 15330: {
                msg = new ProtectorEcosystemActionAnswerMessage();
                break;
            }
            case 15134: {
                msg = new NationLawTriggeredMessage();
                break;
            }
            case 20034: {
                msg = new ClientNationDiplomacyInformationResult();
                break;
            }
            case 15802: {
                msg = new TravelLoadingMessage();
                break;
            }
            case 15804: {
                msg = new TeleporterLoadingMessage();
                break;
            }
            case 20036: {
                msg = new ClientNationDiplomacyChangeResultMessage();
                break;
            }
            case 3172: {
                msg = new PoliticContentMessage();
                break;
            }
            case 15950: {
                msg = new DungeonLadderInformationMessage();
                break;
            }
            case 15952: {
                msg = new DungeonRoundMessage();
                break;
            }
            case 15954: {
                msg = new DungeonWaveMessage();
                break;
            }
            case 15956: {
                msg = new DungeonUserMessage();
                break;
            }
            case 15958: {
                msg = new DungeonEventWonMessage();
                break;
            }
            case 15960: {
                msg = new DungeonChallengeMessage();
                break;
            }
            case 20100: {
                msg = new MarketConsultResultMessage();
                break;
            }
            case 20102: {
                msg = new MarketConsultSellerResultMessage();
                break;
            }
            case 15982: {
                msg = new PetChangeMessage();
                break;
            }
            case 15990: {
                msg = new ItemXpChangeMessage();
                break;
            }
            case 15992: {
                msg = new ItemBindMessage();
                break;
            }
            case 5226: {
                msg = new EquipmentPositionLockedMessage();
                break;
            }
            case 15998: {
                msg = new QuestInventoryChangeMessage();
                break;
            }
            case 15997: {
                msg = new CosmeticsInventoryChangeMessage();
                break;
            }
            case 15999: {
                msg = new TemporaryInventoryChangeMessage();
                break;
            }
            case 20050: {
                msg = new GuildChangeMessage();
                break;
            }
            case 20056: {
                msg = new GuildInvitationMessage();
                break;
            }
            case 20072: {
                msg = new GuildBonusLearnedMessage();
                break;
            }
            case 13102: {
                msg = new GemResultMessage();
                break;
            }
            case 13103: {
                msg = new GemRemovedMessage();
                break;
            }
            case 13106: {
                msg = new GemMergedResultMessage();
                break;
            }
            case 13108: {
                msg = new GemImproveResultMessage();
                break;
            }
            case 20059: {
                msg = new GuildErrorMessage();
                break;
            }
            case 20076: {
                msg = new GuildStorageConsultResultMessage();
                break;
            }
            case 20078: {
                msg = new GuildStorageCompartmentConsultResultMessage();
                break;
            }
            case 20080: {
                msg = new GuildStorageMoneyResultMessage();
                break;
            }
            case 20086: {
                msg = new GuildLadderConsultResultMessage();
                break;
            }
            case 8414: {
                msg = new ActorAptitudePointGainMessage();
                break;
            }
            case 15727: {
                msg = new TravelDiscoveredMessage();
                break;
            }
            case 4186: {
                msg = new DisassembleResultMessage();
                break;
            }
            case 4188: {
                msg = new RandomItemResultMessage();
                break;
            }
            case 13200: {
                msg = new SpellsRestatNeededMessage();
                break;
            }
            case 13202: {
                msg = new SpellsRestatResultMessage();
                break;
            }
            case 20300: {
                msg = new NationSelectionInfoResult();
                break;
            }
            case 20400: {
                msg = new PvpRankUpdateMessage();
                break;
            }
            case 20402: {
                msg = new PvpFightReportMessage();
                break;
            }
            case 20404: {
                msg = new NationPvpLadderPageResponse();
                break;
            }
            case 20406: {
                msg = new NationPvpLadderEntryResponse();
                break;
            }
            case 20408: {
                msg = new NationPvpLadderEntryByNameResponse();
                break;
            }
            case 20090: {
                msg = new HavenWorldInfoResult();
                break;
            }
            case 20097: {
                msg = new HavenWorldGuildNotifyBuildingAddedMessage();
                break;
            }
            case 20098: {
                msg = new HavenWorldGuildNotifyBuildingRemovedMessage();
                break;
            }
            case 20073: {
                msg = new HavenWorldGuildNotifyBuildingEvolvedMessage();
                break;
            }
            case 20099: {
                msg = new GuildObtainHavenWorldMessage();
                break;
            }
            case 20094: {
                msg = new HavenWorldAuctionInfoResultMessage();
                break;
            }
            case 20095: {
                msg = new HavenWorldBidResultMessage();
                break;
            }
            case 20096: {
                msg = new HavenWorldOpenResourcesCollectorResultMessage();
                break;
            }
            case 5522: {
                msg = new HavenWorldUpdateMessage();
                break;
            }
            case 5510: {
                msg = new HavenWorldTopologyUpdateMessage();
                break;
            }
            case 5512: {
                msg = new HavenWorldBuildingCreationMessage();
                break;
            }
            case 5516: {
                msg = new HavenWorldEditorMessage();
                break;
            }
            case 5520: {
                msg = new HavenWorldBuildingInfoMessage();
                break;
            }
            case 5514: {
                msg = new HavenWorldBuildingRemovedMessage();
                break;
            }
            case 5524: {
                msg = new HavenWorldBuildingEquippedMessage();
                break;
            }
            case 5526: {
                msg = new HavenWorldAuctionAnswerMessage();
                break;
            }
            case 5518: {
                msg = new HavenWorldGuildMessage();
                break;
            }
            case 2079: {
                msg = new AuthentificationTokenResultMessage();
                break;
            }
            case 14011: {
                msg = new KrosmozFigureListResultMessage();
                break;
            }
            case 14013: {
                msg = new KrosmozFigureAddResultMessage();
                break;
            }
            case 14015: {
                msg = new KrosmozFigureDeleteResultMessage();
                break;
            }
            case 15500: {
                msg = new LockActivatedMessage();
                break;
            }
            case 15502: {
                msg = new LockIncrementedMessage();
                break;
            }
            case 15510: {
                msg = new FightChallengesActivatedMessage();
                break;
            }
            case 15512: {
                msg = new FightChallengeEndedMessage();
                break;
            }
            case 15942: {
                msg = new OpenSeedSpreaderResultMessage();
                break;
            }
            case 5550: {
                msg = new CompanionAddedMessage();
                break;
            }
            case 5551: {
                msg = new CompanionListUpdateMessage();
                break;
            }
            case 5552: {
                msg = new CompanionUpdateNameMessage();
                break;
            }
            case 5559: {
                msg = new CompanionUpdateXpMessage();
                break;
            }
            case 5562: {
                msg = new CompanionUpdateUnlockedMessage();
                break;
            }
            case 5556: {
                msg = new AddItemToCompanionEquipmentResultMessage();
                break;
            }
            case 5558: {
                msg = new RemoveItemFromCompanionEquipmentResultMessage();
                break;
            }
            case 5563: {
                msg = new AddItemToCompanionEquipmentErrorMessage();
                break;
            }
            case 5565: {
                msg = new HeroAddedMessage();
                break;
            }
            case 32006: {
                msg = new CraftInventoryMessage();
                break;
            }
            case 32008: {
                msg = new CraftTasksSlotsMessage();
                break;
            }
            case 32012: {
                msg = new CraftsMessage();
                break;
            }
            case 32014: {
                msg = new CraftUserRatesMessage();
                break;
            }
            case 15706: {
                msg = new PopupModerationMessage();
                break;
            }
            case 20415: {
                msg = new PartySearchPlayerSearchResultMessage();
                break;
            }
            case 20417: {
                msg = new PartySearchPlayerInviteTransferMessage();
                break;
            }
            case 20418: {
                msg = new PartySearchPlayerDefinitionMessage();
                break;
            }
            case 20419: {
                msg = new PartySearchPlayerFeedbackMessage();
                break;
            }
            case 20420: {
                msg = new PartyRequesterUpdateMessage();
                break;
            }
            default: {
                WakfuMessageDecoder.m_logger.warn((Object)("Type de message inconnu du d\u00ef¿½codeur : " + msgType));
                break;
            }
        }
        return msg;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuMessageDecoder.class);
    }
}
