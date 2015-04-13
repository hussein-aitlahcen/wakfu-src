package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gameAction.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.common.game.gameActions.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class NetScriptedEventsFrame implements MessageFrame
{
    public static final boolean CHALLENGE_DEBUG = false;
    private static final NetScriptedEventsFrame INSTANCE;
    protected static final Logger m_logger;
    
    public static NetScriptedEventsFrame getInstance() {
        return NetScriptedEventsFrame.INSTANCE;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 11100: {
                final RunClientEventMessage msg = (RunClientEventMessage)message;
                ScriptManager.getInstance().runEvent(msg.getScenarioId(), msg.getEventId(), msg.getParameters(), msg.isFromReward());
                return false;
            }
            case 11202: {
                final ChallengeActionLoadedMessage msg2 = (ChallengeActionLoadedMessage)message;
                ChallengeManager.getInstance().loadActions(msg2.getActions(), msg2.getScenarioId());
                final IntLongLightWeightMap vars = msg2.getVars();
                if (vars != null) {
                    for (int i = 0, size = vars.size(); i < size; ++i) {
                        ChallengeManager.getInstance().updateVars((byte)vars.getQuickKey(i), vars.getQuickValue(i), msg2.getScenarioId());
                    }
                }
                return false;
            }
            case 11210: {
                final ChallengeProposalMessage msg3 = (ChallengeProposalMessage)message;
                ChallengeManager.getInstance().proposeChallenge(msg3.getScenarioId());
                final ProtectorChallengeEvent protectorEvent = (ProtectorChallengeEvent)ProtectorEvents.CHALLENGE_PROPOSAL.create();
                protectorEvent.setChallengeId(msg3.getScenarioId());
                this.dispatchProtectorChallengeEvent(protectorEvent);
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventChallengeProposal());
                return false;
            }
            case 11206: {
                final ChallengeActionCompletedMessage msg4 = (ChallengeActionCompletedMessage)message;
                ChallengeManager.getInstance().completeAction(msg4.getScenarioId(), msg4.getActionId());
                return false;
            }
            case 11204: {
                final ChallengeVarUpdatedMessage msg5 = (ChallengeVarUpdatedMessage)message;
                ChallengeManager.getInstance().updateVars(msg5.getVarId(), msg5.getVarNewValue(), msg5.getScenarioId());
                return false;
            }
            case 11208: {
                final RewardMessage msg6 = (RewardMessage)message;
                NetScriptedEventsFrame.m_logger.info((Object)("reception d'un message de Reward pour le challenge " + msg6.getScenarioId()));
                this.onReward(msg6.getScenarioId(), msg6.getReward());
                return false;
            }
            case 11102: {
                final AddItemToInventoryMessage msg7 = (AddItemToInventoryMessage)message;
                final long uniqueId = msg7.getUid();
                final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(msg7.getReferenceId());
                if (refItem != null) {
                    final Item item = new Item(uniqueId);
                    item.initializeWithReferenceItem(refItem);
                    item.setQuantity(msg7.getQuantity());
                    if (localPlayer.getBags().addItemToBags(item) == null) {
                        NetScriptedEventsFrame.m_logger.error((Object)("[Add_item_inventory] : L'ajout d'un item en provenance d'un sc\u00e9nario a \u00e9chou\u00e9. Probl\u00e8me de synchro client/serveur ? Item : " + refItem.getId()));
                    }
                    else if (msg7.isCompleteFeedBack() && refItem.isTreasureItem()) {
                        if (!WakfuGameEntity.getInstance().hasFrame(UIChestLootFrame.getInstance())) {
                            WakfuGameEntity.getInstance().pushFrame(UIChestLootFrame.getInstance());
                        }
                        UIChestLootFrame.getInstance().addLootItem(refItem.getId(), msg7.getQuantity());
                    }
                    else {
                        ItemFeedbackHelper.sendChatItemAddedMessage(item);
                    }
                }
                else {
                    NetScriptedEventsFrame.m_logger.error((Object)("[ADD_ITEM_INVENTORY] : Impossible de trouver de r\u00e9f\u00e9renceItem d'id " + msg7.getReferenceId()));
                }
                return false;
            }
            case 11110: {
                final RemoveItemFromInventoryMessage msg8 = (RemoveItemFromInventoryMessage)message;
                final long uid = msg8.getUid();
                final LocalPlayerCharacter concerned = HeroUtils.getHeroWithItemUidInBags(localPlayer.getOwnerId(), uid);
                return concerned.getBags().removeItemFromBags(uid) == null && ((ArrayInventoryWithoutCheck<Item, R>)concerned.getEquipmentInventory()).removeWithUniqueId(uid) != null && false;
            }
            case 11112: {
                final RemoveItemFromInventoryWithReferenceIdMessage msg9 = (RemoveItemFromInventoryWithReferenceIdMessage)message;
                final int referenceId = msg9.getReferenceId();
                int count = msg9.getCount();
                int removed;
                if (count == -1) {
                    removed = localPlayer.getBags().destroyItemFromBags(referenceId);
                }
                else {
                    removed = localPlayer.getBags().destroyItemFromBags(referenceId, count);
                    count -= removed;
                }
                if (removed != 0) {
                    ItemFeedbackHelper.sendChatItemRemovedMessage(referenceId, (short)removed);
                }
                if (count == -1) {
                    localPlayer.getEquipmentInventory().listOfDestroyedWithReferenceId(referenceId);
                    localPlayer.getShortcutBarManager().removeShortcutItemWithId(referenceId, ShortCutType.ITEM, true);
                }
                if (count > 0) {
                    localPlayer.getEquipmentInventory().listOfDestroyedWithReferenceId(referenceId, count);
                    localPlayer.getShortcutBarManager().removeShortcutItemWithId(referenceId, ShortCutType.ITEM, true);
                }
                return false;
            }
            case 11108: {
                final UnEquipEventMessage msg10 = (UnEquipEventMessage)message;
                final long uid = msg10.getUid();
                final Item item2 = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getWithUniqueId(uid);
                try {
                    EquipmentToInventoryExchanger.getInstance().moveItem(localPlayer.getEquipmentInventory(), localPlayer.getBags().get(msg10.getContainerId()).getInventory(), msg10.getPos(), item2, localPlayer, localPlayer.getEffectContext());
                }
                catch (InventoryCapacityReachedException e) {
                    NetScriptedEventsFrame.m_logger.warn((Object)"Ne devrait pas arriver : Impossible de d\u00e9s\u00e9quiper cet objet car la capacit\u00e9 maximum du sac a \u00e9t\u00e9 atteinte");
                }
                catch (ContentAlreadyPresentException e2) {
                    NetScriptedEventsFrame.m_logger.warn((Object)"Ne devrait pas arriver : Impossible d'\u00e9quiper cet objet car l'objet est d\u00e9j\u00e0 pr\u00e9sent");
                }
                UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
                return false;
            }
            case 11214: {
                final ChallengeInformationMessage msg11 = (ChallengeInformationMessage)message;
                final int challengeId = msg11.getCurrentChallengeId();
                final int timeBeforeCurrentChallenge = msg11.getTimeBeforeCurrentChallenge();
                final int timeBeforeNextChallenge = msg11.getTimeBeforeNextChallenge();
                final GameDateConst startDate = msg11.getStartDate();
                final int protectorId = msg11.getProtectorId();
                final byte msgState = msg11.getState();
                final boolean registeredToChallenge = msg11.isRegisteredToChallenge();
                final TIntArrayList challengeActions = msg11.getCurrentChallengeActions();
                final int entityCount = msg11.getEntityCount();
                ChallengeStatus state;
                if (msgState != -1) {
                    state = ChallengeStatus.values()[msgState];
                }
                else {
                    state = null;
                }
                NetScriptedEventsFrame.m_logger.info((Object)("Challenge courant : " + challengeId + " (dans " + timeBeforeCurrentChallenge + "s)"));
                final AreaChallengeInformation challengeInformation = AreaChallengeInformation.getInstance();
                final ChallengeData data = challengeInformation.getChallengeInZone();
                boolean isChaos = false;
                if (data != null) {
                    isChaos = data.isChaos();
                }
                if (!isChaos && state == ChallengeStatus.PROPOSED && !registeredToChallenge && (challengeInformation.getChallengeInZone() == null || challengeInformation.getChallengeInZone().getId() != challengeId)) {
                    final UINotificationMessage uiNotificationMessage = new UINotificationMessage(WakfuTranslator.getInstance().getString("notification.challengeProposalTitle"), WakfuTranslator.getInstance().getString("notification.challengeProposalText"), NotificationMessageType.PROTECTOR_CHALLENGE);
                    Worker.getInstance().pushMessage(uiNotificationMessage);
                }
                challengeInformation.cleanChallengeInZone();
                final ChallengeData currentChallenge = challengeInformation.getChallengeInZone();
                if (!registeredToChallenge && currentChallenge != null) {
                    challengeInformation.cleanCurrentChallenge();
                }
                boolean statusHasChanged = false;
                if (challengeInformation.getCurrentZoneStatus() != state) {
                    challengeInformation.setCurrentZoneStatus(state, timeBeforeNextChallenge);
                    statusHasChanged = true;
                }
                challengeInformation.setCurrentProtectorId(protectorId);
                if (!registeredToChallenge || challengeId == -1) {
                    challengeInformation.setChallengeInZone(-1, timeBeforeNextChallenge, startDate, challengeActions, false, protectorId);
                    NetScriptedEventsFrame.m_logger.info((Object)"Pas de challenge dans cette zone, on s'arr\u00eate l\u00e0");
                    return false;
                }
                challengeInformation.setChallengeInZone(challengeId, timeBeforeNextChallenge, startDate, challengeActions, registeredToChallenge, protectorId);
                challengeInformation.getChallengeInZone().setEntityCount(entityCount);
                if (registeredToChallenge && statusHasChanged) {
                    challengeInformation.updateStatus();
                }
                final Protector protector = ProtectorManager.INSTANCE.getProtector(protectorId);
                if (protector != null) {
                    final ProtectorChallengeProposalEvent protectorEvent2 = (ProtectorChallengeProposalEvent)ProtectorEvents.CHALLENGE_PROPOSAL.create();
                    protectorEvent2.setProtector(protector);
                    protectorEvent2.setChallengeId(challengeId);
                    final ChallengeDataModel challengeDataModel = ChallengeManager.getInstance().getChallengeDataModel(challengeId);
                    if (challengeDataModel != null) {
                        protectorEvent2.setTitle(challengeDataModel.getChallengeTitle());
                        final ArrayList<ChallengeRewardModel> models = challengeDataModel.getRewards();
                        if (!models.isEmpty()) {
                            final Item item3 = ReferenceItemManager.getInstance().getDefaultItem(models.get(0).getItemId());
                            if (item3 != null) {
                                protectorEvent2.setReward(UIChatFrame.getItemFormatedForChatLinkString(item3));
                            }
                        }
                    }
                    this.dispatchProtectorChallengeEvent(protectorEvent2);
                }
                else {
                    NetScriptedEventsFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9venement de challenge pour un protecteur qu'on ne connait pas id=" + protectorId + " eventType=CHALLENGE_INFORMATION"));
                }
                return false;
            }
            case 11220: {
                final ChallengeStatusMessage msg12 = (ChallengeStatusMessage)message;
                final ChallengeStatus state2 = ChallengeStatus.values()[msg12.getStatus()];
                AreaChallengeInformation.getInstance().setCurrentZoneStatus(state2, msg12.getTimeBeforeNext());
                AreaChallengeInformation.getInstance().updateStatus();
                final ChallengeData currentChallenge2 = AreaChallengeInformation.getInstance().getChallengeInZone();
                if (state2 == ChallengeStatus.RUNNING && AreaChallengeInformation.getInstance().getChallengeInZone() == currentChallenge2 && !currentChallenge2.isChaos()) {
                    WakfuSystemMessageManager.getInstance().showMessage(new BannerSystemMessageData(WakfuSystemMessageManager.SystemMessageType.CHALLENGE, currentChallenge2.getTitle(), 500, 2500, "AnimChallenge1"));
                    WakfuSoundManager.getInstance().playGUISound(600144L);
                    final UINotificationMessage uiNotificationMessage2 = new UINotificationMessage(WakfuTranslator.getInstance().getString("notification.challengeStartTitle"), WakfuTranslator.getInstance().getString("notification.challengeStartText"), NotificationMessageType.PROTECTOR_CHALLENGE);
                    Worker.getInstance().pushMessage(uiNotificationMessage2);
                }
                return false;
            }
            case 4000: {
                final ErrorResultMessage msg13 = (ErrorResultMessage)message;
                switch (msg13.getResultId()) {
                    case 202: {
                        final ChallengeData areaChallenge = AreaChallengeInformation.getInstance().getChallengeInZone();
                        if (areaChallenge != null) {
                            areaChallenge.setLaunched(false);
                            break;
                        }
                        break;
                    }
                }
                final int pipe = (msg13.getResultId() == 0) ? 4 : 3;
                ErrorsMessageTranslator.getInstance().pushMessage(msg13.getResultId(), pipe, new Object[0]);
                return false;
            }
            case 11216: {
                final CreateGraphicalInputRequestMessage createGraphicalInputRequestMessage = (CreateGraphicalInputRequestMessage)message;
                Xulor.getInstance().load("challengeInputDialog", Dialogs.getDialogPath("challengeInputDialog"), 40961L, (short)10000);
                final int scenarioId = createGraphicalInputRequestMessage.getScenarioId();
                final byte varId = createGraphicalInputRequestMessage.getVarId();
                PropertiesProvider.getInstance().setPropertyValue("waitingForInputChallenge", new ScenarioInputData(scenarioId, varId));
                return false;
            }
            case 11218: {
                final SetChallengeTarget msg14 = (SetChallengeTarget)message;
                final ChallengeData data2 = ChallengeManager.getInstance().getChallengeData(msg14.getChallengeId());
                data2.setTarget(msg14.getPosition());
                return false;
            }
            case 11226: {
                final ChallengeEntitiesMessage challengeEntitiesMessage = (ChallengeEntitiesMessage)message;
                final ChallengeData challengeData = AreaChallengeInformation.getInstance().getChallengeInZone();
                if (challengeData != null) {
                    challengeData.setEntityCount(challengeEntitiesMessage.getEntityCount());
                }
                return false;
            }
            case 11224: {
                final ChallengeSuccessMessage msg15 = (ChallengeSuccessMessage)message;
                this.onReward(msg15.getScenarioId(), msg15.getRewardIndex());
                final boolean isPlayerConcerned = AreaChallengeInformation.getInstance().isPlayerConcernedByChallenge();
                final ChallengeData data3 = ChallengeManager.getInstance().getChallengeData(msg15.getScenarioId());
                final String winnerName = msg15.getWinnerName();
                if (isPlayerConcerned) {
                    data3.setRanking((short)(msg15.getRank() + 1));
                    data3.setFinalReward(msg15.getRewardIndex());
                    data3.setWinnerName(winnerName);
                    data3.setWinnerScore(msg15.getWinnerScore());
                    UIChallengeResultFrame.getInstance().setChallenge(data3);
                    if (!WakfuGameEntity.getInstance().hasFrame(UIChallengeResultFrame.getInstance())) {
                        WakfuGameEntity.getInstance().pushFrame(UIChallengeResultFrame.getInstance());
                    }
                    WakfuSoundManager.getInstance().playGUISound(600130L);
                }
                if (data3.getModel().isChaos()) {
                    final String stringMessage = WakfuTranslator.getInstance().getString("chat.challenge.chaosEnded");
                    final int chatPipe = 4;
                    ChatManager.getInstance().pushMessage(stringMessage, 4);
                }
                else if (isPlayerConcerned) {
                    final String localPlayerName = WakfuGameEntity.getInstance().getLocalPlayer().getName();
                    if (winnerName != null && winnerName.length() > 0) {
                        final int chatPipe2 = 4;
                        if (!localPlayerName.equals(winnerName)) {
                            final String stringMessage2 = WakfuTranslator.getInstance().getString("chat.challenge.won", winnerName, data3.getTitle());
                            ChatManager.getInstance().pushMessage(stringMessage2, 4);
                        }
                        else {
                            final String stringMessage2 = WakfuTranslator.getInstance().getString("chat.challenge.selfComplete", data3.getTitle());
                            ChatManager.getInstance().pushMessage(stringMessage2, 4);
                        }
                    }
                    else {
                        final String stringMessage2 = WakfuTranslator.getInstance().getString("chat.challenge.self.won", data3.getTitle());
                        final int chatPipe2 = 4;
                        ChatManager.getInstance().pushMessage(stringMessage2, 4);
                    }
                }
                AreaChallengeInformation.getInstance().setCurrentZoneStatus(ChallengeStatus.WAITING_NEXT_CHALLENGE);
                AreaChallengeInformation.getInstance().updateStatus();
                QuestConfigManager.INSTANCE.removeConfig(msg15.getScenarioId());
                return false;
            }
            case 11222: {
                final FailChallengeMessage msg16 = (FailChallengeMessage)message;
                final LuaScript script = ScriptManager.getInstance().getScript(msg16.getScenarioId(), false);
                AreaChallengeInformation.getInstance().failChallenge(msg16.getScenarioId());
                if (script != null) {
                    script.forceClose();
                }
                QuestConfigManager.INSTANCE.removeConfig(msg16.getScenarioId());
                return false;
            }
            case 11200: {
                final TerminateScenarioMessage msg17 = (TerminateScenarioMessage)message;
                final LuaScript script = ScriptManager.getInstance().getScript(msg17.getScenarioId(), false);
                final ChallengeData data3 = ChallengeManager.getInstance().getChallengeData(msg17.getScenarioId());
                final boolean concerned2 = AreaChallengeInformation.getInstance().isPlayerConcernedByChallenge();
                if (data3 != null) {
                    if (concerned2 && !data3.getModel().isChaos()) {
                        final String stringMessage = WakfuTranslator.getInstance().getString("chat.challenge.failed", data3.getTitle());
                        final int chatPipe = 4;
                        ChatManager.getInstance().pushMessage(stringMessage, 4);
                        data3.setRanking((short)(-2));
                        data3.setFinalReward(-1);
                        data3.setWinnerName(null);
                        data3.setWinnerScore(0);
                        data3.setEndReason(msg17.getReason());
                    }
                    QuestConfigManager.INSTANCE.removeConfig(data3.getId());
                }
                if (script != null) {
                    script.forceClose();
                }
                return false;
            }
            case 11228: {
                final ChallengeRankingMessage msg18 = (ChallengeRankingMessage)message;
                final ChallengeData currentChallenge3 = AreaChallengeInformation.getInstance().getChallengeInZone();
                if (currentChallenge3 != null) {
                    currentChallenge3.setRanking(msg18.getRanking());
                }
                return false;
            }
            case 15800: {
                final GameActionPlayScriptMessage msg19 = (GameActionPlayScriptMessage)message;
                runScript(msg19.getSource(), msg19.getActionId(), msg19.getVariables());
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onReward(final int scenarioId, final int rewardId) {
        ChallengeManager.getInstance().setValidRewards(scenarioId, new TIntArrayList(new int[] { rewardId }));
        AreaChallengeInformation.getInstance().lastGoalCompleted();
    }
    
    public void dispatchProtectorChallengeEvent(final ProtectorEvent protectorEvent) {
        final int currentProtectorId = AreaChallengeInformation.getInstance().getCurrentProtectorId();
        final Protector protector = ProtectorManager.INSTANCE.getProtector(currentProtectorId);
        if (protector != null) {
            protectorEvent.setProtector(protector);
            ProtectorEventDispatcher.INSTANCE.dispatch(protectorEvent);
        }
        else {
            NetScriptedEventsFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9venement de challenge pour un protecteur qu'on ne connait pas id=" + currentProtectorId + " eventType=" + protectorEvent.getClass().getSimpleName()));
        }
    }
    
    @Override
    public long getId() {
        return 6L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    public static LuaScript runScript(final PlayScriptSource source, final int actionId, final Map<String, Object> variables) {
        String path;
        if (source == PlayScriptSource.GENERIC_IE) {
            path = "genericIEActions/%d.lua";
        }
        else {
            path = "ie_actions/%d.lua";
        }
        final String scriptFile = String.format(path, actionId);
        return LuaManager.getInstance().runScript(scriptFile, null, variables, new LuaScriptEventListener() {
            @Override
            public void onLuaScriptError(final LuaScript script, final LuaScriptErrorType errorType, final String message) {
                NetScriptedEventsFrame.m_logger.error((Object)("[LD] Erreur dans le scrip " + scriptFile + " Erreur[" + errorType + " : " + message + "]"));
            }
            
            @Override
            public void onLuaScriptFinished(final LuaScript script) {
            }
            
            @Override
            public void onLuaScriptLoaded(final LuaScript script) {
            }
        }, false);
    }
    
    static {
        INSTANCE = new NetScriptedEventsFrame();
        m_logger = Logger.getLogger((Class)NetScriptedEventsFrame.class);
    }
}
