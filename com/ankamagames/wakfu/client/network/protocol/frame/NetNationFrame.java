package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.impl.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import java.nio.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.nation.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.chaos.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import java.util.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.data.*;
import com.ankamagames.wakfu.common.constants.*;
import gnu.trove.*;

public class NetNationFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final MessageFrame INSTANCE;
    private static final TObjectProcedure<CharacterInfo> CLEAR_CANDIDATES_PROC;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        PropertiesProvider.getInstance().setPropertyValue("nations", NationsView.INSTANCE);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        final TIntObjectIterator<Nation> it = NationManager.INSTANCE.realNationIterator(new Integer[0]);
        while (it.hasNext()) {
            it.advance();
            it.remove();
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 20000: {
                final ClientNationSynchronizationMessage msg = (ClientNationSynchronizationMessage)message;
                final TIntObjectIterator<byte[]> nations = msg.nationsIterator();
                while (nations.hasNext()) {
                    nations.advance();
                    final int nationId = nations.key();
                    final byte[] bytes = nations.value();
                    boolean createdNation = false;
                    Nation nation = NationManager.INSTANCE.getNationById(nationId);
                    if (nation == null) {
                        NetNationFrame.m_logger.info((Object)("[NATION] Reception d'un message de synchro de nation demandant l'ajout d'une nouvelle nation : " + nationId));
                        nation = Nation.createNation(nationId);
                        NationManager.INSTANCE.registerNation(nation);
                        createdNation = true;
                    }
                    NetNationFrame.m_logger.info((Object)("[NATION] Synchronisation de la nation " + nation + " (" + bytes.length + ") bytes"));
                    nation.unserialize(bytes, Version.SERIALIZATION_VERSION);
                    if (createdNation) {
                        nation.finishInitialization();
                    }
                }
                NationDisplayer.getInstance().updateUI();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer != null) {
                    if (localPlayer.getCitizenComportment().getNation() != Nation.VOID_NATION) {
                        final ClientCitizenComportment citizen = (ClientCitizenComportment)localPlayer.getCitizenComportment();
                        citizen.updateCandidateInfo();
                    }
                    localPlayer.updateAdditionalAppearance();
                }
                return false;
            }
            case 20004: {
                final NationVoteStartEventMessage msg2 = (NationVoteStartEventMessage)message;
                NetNationFrame.m_logger.info((Object)("[NATION] Un vote a commenc\u00e9 pour ma nation, d'une dur\u00e9e de " + msg2.getVoteDuration()));
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer == null) {
                    return false;
                }
                if (NationDisplayer.getInstance().isPreventiveElection()) {
                    final String title = WakfuTranslator.getInstance().getString("notification.preventiveElectionTitle");
                    final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.preventiveElectionText"), NotificationMessageType.NATION, "0");
                    final Message uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                    Worker.getInstance().pushMessage(uiNotificationMessage);
                }
                else {
                    final String title = WakfuTranslator.getInstance().getString("notification.newElectionTitle");
                    final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.newElectionText"), NotificationMessageType.NATION, "0");
                    final Message uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                    Worker.getInstance().pushMessage(uiNotificationMessage);
                }
                final CitizenComportment comportment = localPlayer.getCitizenComportment();
                final Nation nation2 = comportment.getNation();
                nation2.registerVoteState(msg2.getVoteStart(), msg2.getVoteDuration(), true);
                String txtMessage;
                if (localPlayer.getLevel() >= 1) {
                    txtMessage = WakfuTranslator.getInstance().getString("nation.vote.start.can.be.candidate", 3000);
                }
                else if (localPlayer.getLevel() >= 1) {
                    txtMessage = WakfuTranslator.getInstance().getString("nation.vote.start.can.vote", (short)1);
                }
                else {
                    txtMessage = WakfuTranslator.getInstance().getString("nation.vote.start.cant.do.anything", (short)1);
                }
                ChatManager.getInstance().pushMessage(txtMessage, 8);
                if (Xulor.getInstance().isLoaded("nationDialog")) {
                    NationDisplayer.getInstance().fullUpdate();
                }
                if (WakfuGameEntity.getInstance().hasFrame(UIVoteInformationFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UIVoteInformationFrame.getInstance());
                }
                return false;
            }
            case 20006: {
                final NationVoteEndEventMessage msg3 = (NationVoteEndEventMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer == null) {
                    return false;
                }
                final ClientCitizenComportment comportment2 = (ClientCitizenComportment)localPlayer.getCitizenComportment();
                final Nation nation2 = comportment2.getNation();
                comportment2.setHasVoted(false);
                CharacterInfoManager.getInstance().forEachPlayerCharacter(NetNationFrame.CLEAR_CANDIDATES_PROC);
                final String oldGovernorName = msg3.getOldGovernorName();
                final boolean wasPlayerGovernorBefore = localPlayer.getName().equals(oldGovernorName);
                final String newGovernorName = msg3.getNewGovernorName();
                final boolean isGovernorPresent = newGovernorName != null;
                final boolean isGovernorElected = msg3.isGovernorElected();
                final boolean isPlayerGovernorNow = isGovernorPresent && localPlayer.getName().equals(newGovernorName);
                final boolean wasPlayerCandidate = comportment2.isCandidate();
                nation2.getLawManager().onGovernorElected();
                nation2.registerVoteState(msg3.getNextVoteStartDate(), msg3.getNextVoteDuration(), false);
                nation2.askForVoteEnd();
                comportment2.updateCandidateInfo();
                NetNationFrame.m_logger.info((Object)("[NATION] Un vote commenc\u00e9 le " + msg3.getVoteStartDate() + "vient de se terminer pour ma nation." + " Gouverneur : " + newGovernorName + " Gouverneur \u00e9lu d\u00e9mocratiquement : " + isGovernorElected));
                String specificMsg;
                String notifText;
                if (isGovernorPresent) {
                    if (isPlayerGovernorNow) {
                        if (wasPlayerGovernorBefore) {
                            if (isGovernorElected) {
                                specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.reelected");
                            }
                            else {
                                specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.reelectedByDefault");
                            }
                        }
                        else {
                            specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.elected");
                        }
                        notifText = WakfuTranslator.getInstance().getString("notification.governorEndElectionText");
                    }
                    else {
                        if (wasPlayerGovernorBefore) {
                            if (wasPlayerCandidate) {
                                specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.reelectionLost", newGovernorName);
                            }
                            else {
                                specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.noMoreGovernor", newGovernorName);
                            }
                        }
                        else if (wasPlayerCandidate) {
                            specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.electionLost", newGovernorName);
                        }
                        else if (isGovernorElected) {
                            specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.someoneElected", newGovernorName);
                        }
                        else {
                            specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.someoneReelected", newGovernorName);
                        }
                        notifText = WakfuTranslator.getInstance().getString("notification.endElectionText", newGovernorName);
                    }
                }
                else {
                    specificMsg = WakfuTranslator.getInstance().getString("nation.vote.end.result.noGovernor");
                    notifText = WakfuTranslator.getInstance().getString("notification.noGovernorEndElectionText");
                }
                final String title2 = WakfuTranslator.getInstance().getString("notification.endElectionTitle");
                final String text2 = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.NATION, "0");
                final Message uiNotificationMessage2 = new UINotificationMessage(title2, text2, NotificationMessageType.NATION);
                Worker.getInstance().pushMessage(uiNotificationMessage2);
                WakfuSoundManager.getInstance().playGuiSoundWithDelayAndFadeMusic(600133L, 0.0f, 500, 2000, 6400);
                final String fullTxtMsg = WakfuTranslator.getInstance().getString("nation.vote.end") + "\n" + specificMsg;
                ChatManager.getInstance().pushMessage(fullTxtMsg, 8);
                if (Xulor.getInstance().isLoaded("nationDialog")) {
                    final Message ngirqm = new NationGovernmentInformationRequestMessage();
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(ngirqm);
                }
                if (WakfuGameEntity.getInstance().hasFrame(UIVoteInformationFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UIVoteInformationFrame.getInstance());
                }
                return false;
            }
            case 20010: {
                final NationVoteEndWhileDeconnectedEventMessage msg4 = (NationVoteEndWhileDeconnectedEventMessage)message;
                final String newGovernorName2 = msg4.getGovernorName();
                String txtMsg = null;
                String notifText2 = null;
                switch (msg4.getResult()) {
                    case ELECTION_LOST: {
                        txtMsg = WakfuTranslator.getInstance().getString("nation.vote.end.resultDelayed.voteLost", newGovernorName2);
                        notifText2 = WakfuTranslator.getInstance().getString("notification.endElectionText", newGovernorName2);
                        break;
                    }
                    case ELECTION_LOST_WHILE_GOVERNOR: {
                        txtMsg = WakfuTranslator.getInstance().getString("nation.vote.end.resultDelayed.voteLostStatusLost", newGovernorName2);
                        notifText2 = WakfuTranslator.getInstance().getString("notification.endElectionText", newGovernorName2);
                        break;
                    }
                    case ELECTION_MISSED: {
                        txtMsg = WakfuTranslator.getInstance().getString("nation.vote.end.resultDelayed.voteMissed", newGovernorName2);
                        notifText2 = WakfuTranslator.getInstance().getString("notification.endElectionText", newGovernorName2);
                        break;
                    }
                    case ELECTION_WON: {
                        txtMsg = WakfuTranslator.getInstance().getString("nation.vote.end.resultDelayed.voteWon", newGovernorName2);
                        notifText2 = WakfuTranslator.getInstance().getString("notification.governorEndElectionText");
                        break;
                    }
                    default: {
                        return false;
                    }
                }
                ChatManager.getInstance().pushMessage(txtMsg, 8);
                if (notifText2 != null) {
                    final String title3 = WakfuTranslator.getInstance().getString("notification.endElectionTitle");
                    final String text3 = NotificationPanelDialogActions.createLink(notifText2, NotificationMessageType.NATION, "0");
                    final Message uiNotificationMessage3 = new UINotificationMessage(title3, text3, NotificationMessageType.NATION, 600132);
                    Worker.getInstance().pushMessage(uiNotificationMessage3);
                }
                return false;
            }
            case 15120: {
                final NationCandidateRegistrationFailedMessage msg5 = (NationCandidateRegistrationFailedMessage)message;
                final boolean success = msg5.isSuccess();
                String chatMsg;
                if (!success) {
                    chatMsg = WakfuTranslator.getInstance().getString("nation.vote.candidateRegistration.error");
                }
                else {
                    chatMsg = WakfuTranslator.getInstance().getString("nation.vote.candidateRegistration.success");
                }
                ChatManager.getInstance().pushMessage(chatMsg, 4);
                return false;
            }
            case 15122: {
                final ClientNationCandidateRegistrationResultMessage msg6 = (ClientNationCandidateRegistrationResultMessage)message;
                final CandidateInfo candidate = msg6.getCandidate();
                final Nation nation3 = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNation();
                nation3.registerCandidate(candidate);
                nation3.setCandidatesStats(msg6.getNbCandidates(), msg6.getNbBallots());
                return false;
            }
            case 20012: {
                final NationVoteInformationResultMessage msg7 = (NationVoteInformationResultMessage)message;
                final ArrayList<CandidateInfo> candidates = msg7.getCandidates();
                final Nation nation3 = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNation();
                if (nation3.isVoteRunning()) {
                    nation3.setCandidates(candidates);
                    nation3.setCandidatesStats(msg7.getNbCandidates(), msg7.getNbBallots());
                }
                else {
                    ((CNationElectionHistory)nation3.getElectionHistory()).setCandidates(candidates, msg7.getNbCandidates(), msg7.getNbBallots());
                }
                NationDisplayer.getInstance().updateUI();
                return false;
            }
            case 20008: {
                final NationCandidateDesistResultMessage msg8 = (NationCandidateDesistResultMessage)message;
                if (msg8.isSuccess()) {
                    final long localPlayerId = WakfuGameEntity.getInstance().getLocalPlayer().getId();
                    if (msg8.getCandidateFromId() == localPlayerId) {
                        final String txtMsg2 = WakfuTranslator.getInstance().getString("nation.desist.result.sender", msg8.getCandidateToName(), msg8.getCandidateBallotsCount());
                        ChatManager.getInstance().pushMessage(txtMsg2, 4);
                    }
                    else if (msg8.getCandidateToId() == localPlayerId) {
                        final String txtMsg2 = WakfuTranslator.getInstance().getString("nation.desist.result.receiver", msg8.getCandidateFromName(), msg8.getCandidateBallotsCount(), msg8.getBallotsGiven());
                        ChatManager.getInstance().pushMessage(txtMsg2, 4);
                    }
                    else {
                        NetNationFrame.m_logger.error((Object)("[NATION] On re\u00e7oit un r\u00e9sultat de d\u00e9sistement de " + msg8.getCandidateFromId() + " pour " + msg8.getCandidateToId() + " alors qu'on est d'id " + localPlayerId + ". Message pas arriv\u00e9 au bon destinataire ???"));
                    }
                }
                else {
                    NetNationFrame.m_logger.error((Object)"[NATION] R\u00e9sultat de la demande de d\u00e9sistement : erreur");
                }
                return false;
            }
            case 15124: {
                final NationGovernmentNominationConfirmationRequestMessage msg9 = (NationGovernmentNominationConfirmationRequestMessage)message;
                final NationRank rank = msg9.getRank();
                final String rankName = WakfuTranslator.getInstance().getString(57, (int)rank.getId(), new Object[0]);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("nation.governmentNominationQuestion", rankName), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                        final Nation nation = player.getCitizenComportment().getNation();
                        final byte result = (type == 8) ? NationGovernmentNominationConfirmationResult.ACCEPTED : NationGovernmentNominationConfirmationResult.REFUSED;
                        nation.requestGovernmentNominationResult(player.getId(), result, rank.getId());
                    }
                });
                return false;
            }
            case 20024: {
                final NationGovernmentNominationResultMessage msg10 = (NationGovernmentNominationResultMessage)message;
                final String memberName = msg10.getCharacterName();
                final NationRank rank2 = msg10.getRank();
                final int resultCode = msg10.getResultCode();
                final String rankName2 = WakfuTranslator.getInstance().getString(57, (int)rank2.getId(), new Object[0]);
                if (resultCode == 0) {
                    final String txtMessage2 = WakfuTranslator.getInstance().getString("nation.governmentNominationChatMessage", memberName, rankName2);
                    ChatManager.getInstance().pushMessage(txtMessage2, 8);
                    final String title4 = WakfuTranslator.getInstance().getString("notification.governmentNominationTitle");
                    final String text4 = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.governmentNominationText", memberName, rankName2), NotificationMessageType.NATION);
                    final Message uiNotificationMessage4 = new UINotificationMessage(title4, text4, NotificationMessageType.NATION, 600132);
                    Worker.getInstance().pushMessage(uiNotificationMessage4);
                    if (WakfuGameEntity.getInstance().hasFrame(UINationFrame.getInstance())) {
                        WakfuGameEntity.getInstance().removeFrame(UINationFrame.getInstance());
                    }
                }
                else {
                    ErrorsMessageTranslator.getInstance().pushMessage(resultCode, 3, memberName, rankName2);
                }
                return false;
            }
            case 20026: {
                final NationGovernmentRevokeResultMessage msg11 = (NationGovernmentRevokeResultMessage)message;
                final String memberName = msg11.getCharacterName();
                final NationRank rank2 = msg11.getRank();
                final int resultCode = msg11.getResultCode();
                final String rankName2 = WakfuTranslator.getInstance().getString(57, (int)rank2.getId(), new Object[0]);
                if (resultCode == 0) {
                    final String txtMessage2 = WakfuTranslator.getInstance().getString("nation.governmentRevokeChatMessage", memberName, rankName2);
                    ChatManager.getInstance().pushMessage(txtMessage2, 8);
                    final String title4 = WakfuTranslator.getInstance().getString("notification.governmentRevokeTitle");
                    final String text4 = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.governmentRevokeText", memberName, rankName2), NotificationMessageType.NATION);
                    final Message uiNotificationMessage4 = new UINotificationMessage(title4, text4, NotificationMessageType.NATION, 600132);
                    Worker.getInstance().pushMessage(uiNotificationMessage4);
                    if (WakfuGameEntity.getInstance().hasFrame(UINationFrame.getInstance())) {
                        WakfuGameEntity.getInstance().removeFrame(UINationFrame.getInstance());
                    }
                    final CharacterInfo byName = CharacterInfoManager.getInstance().getCharacterByName(memberName);
                    if (byName instanceof PlayerCharacter) {
                        byName.refreshDisplayEquipment();
                    }
                }
                else {
                    ErrorsMessageTranslator.getInstance().pushMessage(resultCode, 3, memberName, rankName2);
                }
                return false;
            }
            case 15134: {
                final NationLawTriggeredMessage msg12 = (NationLawTriggeredMessage)message;
                final int concernedNationId = msg12.getNationId();
                final long triggeredLawId = msg12.getLawId();
                final CitizenComportment citizenComportment = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
                final NationLawsManager manager = citizenComportment.getNation().getLawManager();
                final String lawName = new TextWidgetFormater().b().addColor("cc4444").append(WakfuTranslator.getInstance().getString(97, (int)triggeredLawId, new Object[0]))._b().finishAndToString();
                final NationAlignement currentNationAlignment = AbstractLawMRUAction.getCurrentNationAlignment();
                if (currentNationAlignment != null) {
                    int points = manager.getCitizenPointsCost(triggeredLawId);
                    final int percentCost = manager.getCitizenPointsPercentCost(triggeredLawId);
                    final int citizenScoreForNation = citizenComportment.getCitizenScoreForNation(concernedNationId);
                    if (citizenScoreForNation > 0) {
                        points += citizenScoreForNation * percentCost / 100;
                    }
                    final String citizenPointsCost = new TextWidgetFormater().b().addColor("cc4444").append(((points > 0) ? "+" : "") + points)._b().finishAndToString();
                    final String textMessage = WakfuTranslator.getInstance().getString("nation.chatLaw", citizenPointsCost, lawName);
                    ChatManager.getInstance().pushMessage(textMessage, 4);
                }
                return false;
            }
            case 20034: {
                final ClientNationDiplomacyInformationResult msg13 = (ClientNationDiplomacyInformationResult)message;
                final TIntObjectIterator<byte[]> it = msg13.diplomacyDataIterator();
                final TIntIntIterator pIt = msg13.numProtectorIterator();
                while (it.hasNext() && pIt.hasNext()) {
                    it.advance();
                    pIt.advance();
                    final int nationId2 = it.key();
                    final byte[] dData = it.value();
                    final int numProtector = pIt.value();
                    final Nation nation4 = NationManager.INSTANCE.getNationById(nationId2);
                    NationDisplayer.getInstance().putNationProtector(nationId2, numProtector);
                    final NationPart part = nation4.getPart(NationSerializationType.Part.DIPLOMACY);
                    part.unSerialize(ByteBuffer.wrap(dData), Version.SERIALIZATION_VERSION);
                    part.fireDataChanged();
                }
                return false;
            }
            case 20036: {
                final ClientNationDiplomacyChangeResultMessage msg14 = (ClientNationDiplomacyChangeResultMessage)message;
                final long remainingTime = msg14.getParam();
                final String timeText = ChallengeDataView.formatTime((short)(remainingTime / 1000L));
                final String errorMessage = ErrorsMessageTranslator.getInstance().getMessageByErrorId(msg14.getError(), timeText);
                ErrorsMessageTranslator.getInstance().pushMessage(msg14.getError(), 3, timeText);
                Xulor.getInstance().msgBox(errorMessage, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1026L, 102, 1);
                UINationFrame.getInstance().setNetEnable(true);
                return false;
            }
            case 9303: {
                final ProtectorChaosMessage msg15 = (ProtectorChaosMessage)message;
                final int protectorId = msg15.getProtectorId();
                final boolean inChaos = msg15.isInChaos();
                NationProtectorInfoManager.updateProtectorChaos(protectorId, inChaos);
                NationDisplayer.getInstance().updateProtectorList();
                final Territory territory = (Territory)ProtectorManager.INSTANCE.getStaticProtector(protectorId).getTerritory();
                String territoryName = null;
                if (WakfuTranslator.getInstance().containsContentKey(66, territory.getId())) {
                    territoryName = WakfuTranslator.getInstance().getString(66, territory.getId(), new Object[0]);
                }
                if (inChaos) {
                    final ProtectorChaosStartedEvent chaosStartedEvent = (ProtectorChaosStartedEvent)ProtectorEvents.CHAOS_STARTED.create();
                    chaosStartedEvent.setTerritory(territory);
                    ProtectorEventDispatcher.INSTANCE.dispatch(chaosStartedEvent);
                    if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentTerritory() == territory) {
                        WakfuSystemMessageManager.getInstance().showMessage(new BannerSystemMessageData(WakfuSystemMessageManager.SystemMessageType.CHALLENGE, WakfuTranslator.getInstance().getString("notification.chaosStartedTitle"), 500, 2500, "AnimChallenge1"));
                        WakfuSoundManager.getInstance().playGUISound(600144L);
                    }
                    final Message uiNotificationMessage3 = new UINotificationMessage(WakfuTranslator.getInstance().getString("notification.chaosStartedTitle"), WakfuTranslator.getInstance().getString("notification.chaosStartedText", territoryName), NotificationMessageType.PROTECTOR_CHALLENGE);
                    Worker.getInstance().pushMessage(uiNotificationMessage3);
                }
                else {
                    final ProtectorChaosEndedEvent chaosEndedEvent = (ProtectorChaosEndedEvent)ProtectorEvents.CHAOS_ENDED.create();
                    chaosEndedEvent.setTerritory(territory);
                    ProtectorEventDispatcher.INSTANCE.dispatch(chaosEndedEvent);
                    final Message uiNotificationMessage3 = new UINotificationMessage(WakfuTranslator.getInstance().getString("notification.chaosStoppedTitle"), WakfuTranslator.getInstance().getString("notification.chaosStoppedText", territoryName), NotificationMessageType.PROTECTOR_CHALLENGE);
                    Worker.getInstance().pushMessage(uiNotificationMessage3);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetNationFrame.class);
        INSTANCE = new NetNationFrame();
        CLEAR_CANDIDATES_PROC = new TObjectProcedure<CharacterInfo>() {
            @Override
            public boolean execute(final CharacterInfo character) {
                final ClientCitizenComportment comp = (ClientCitizenComportment)character.getCitizenComportment();
                if (WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNationId() == comp.getNationId()) {
                    comp.setCandidate(false);
                }
                return true;
            }
        };
    }
}
