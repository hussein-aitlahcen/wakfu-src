package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightInvitationMessageHandler extends UsingFightMessageHandler<FightInvitationMessage, ExternalFightInfo>
{
    private CharacterInfo m_characterInfo;
    
    private String getLawText() {
        final NationAlignement alignment = AbstractLawMRUAction.getCurrentNationAlignment();
        if (alignment == null) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        final boolean leadToEnemyAction = this.leadToEnemyStateAction();
        if (leadToEnemyAction) {
            sb.openText().addColor(Color.RED.getRGBtoHex());
            sb.append("\n").append(WakfuTranslator.getInstance().getString("desc.mru.illegalAction"));
            sb.closeText();
        }
        final List<NationLaw> goodLaws = this.getTriggeredGoodLaws();
        if (alignment == NationAlignement.ALLIED && !goodLaws.isEmpty()) {
            sb.newLine().openText().addColor(AbstractMRUAction.OK_TOOLTIP_COLOR);
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.good"))._b();
            sb.closeText();
            AbstractLawMRUAction.listLaws(sb, goodLaws, AbstractMRUAction.OK_TOOLTIP_COLOR, leadToEnemyAction);
        }
        final List<NationLaw> badLaws = this.getTriggeredBadLaws();
        if (!badLaws.isEmpty()) {
            sb.newLine().openText().addColor(AbstractMRUAction.NOK_TOOLTIP_COLOR);
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.bad"))._b();
            sb.closeText();
            AbstractLawMRUAction.listLaws(sb, badLaws, AbstractMRUAction.NOK_TOOLTIP_COLOR, leadToEnemyAction);
        }
        final List<NationLaw> probablyGoodLaws = this.getProbablyTriggeredGoodLaws();
        if (alignment == NationAlignement.ALLIED && !probablyGoodLaws.isEmpty()) {
            sb.newLine().openText().addColor("9ed34b");
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.probablyGood"))._b();
            sb.closeText();
            AbstractLawMRUAction.listLaws(sb, probablyGoodLaws, "9ed34b", leadToEnemyAction);
        }
        final List<NationLaw> probablyBadLaws = this.getProbablyTriggeredBadLaws();
        if (!probablyBadLaws.isEmpty()) {
            sb.newLine().openText().addColor("f48140");
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.probablyBad"))._b();
            sb.closeText();
            AbstractLawMRUAction.listLaws(sb, probablyBadLaws, "f48140", leadToEnemyAction);
        }
        return (sb.length() > 0) ? sb.finishAndToString() : null;
    }
    
    private boolean leadToEnemyStateAction() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ENEMY || NationPvpHelper.isPvpActive(this.m_characterInfo.getCitizenComportment())) {
            return false;
        }
        final List<NationLaw> badLaws = this.getTriggeredBadLaws();
        return !badLaws.isEmpty();
    }
    
    private List<NationLaw> getTriggeredGoodLaws() {
        return (List<NationLaw>)NationLaw.getGoodLaws((List<NationLaw<NationLawEvent>>)this.getTriggeredLaws());
    }
    
    private List<NationLaw> getProbablyTriggeredGoodLaws() {
        return (List<NationLaw>)NationLaw.getGoodLaws((List<NationLaw<NationLawEvent>>)this.getProbablyTriggeredLaws());
    }
    
    private List<NationLaw> getTriggeredBadLaws() {
        return (List<NationLaw>)NationLaw.getBadLaws((List<NationLaw<NationLawEvent>>)this.getTriggeredLaws());
    }
    
    private List<NationLaw> getProbablyTriggeredBadLaws() {
        return (List<NationLaw>)NationLaw.getBadLaws((List<NationLaw<NationLawEvent>>)this.getProbablyTriggeredLaws());
    }
    
    private List<NationLaw> getTriggeredLaws() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<BasicCharacterInfo> opponents = new ArrayList<BasicCharacterInfo>();
        final ArrayList<BasicCharacterInfo> teamMates = new ArrayList<BasicCharacterInfo>();
        final ArrayList<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        teamMates.add(localPlayer);
        opponents.add(this.m_characterInfo);
        final FightLawEvent fightLawEvent = new FightLawEvent(localPlayer, FightModel.DUEL, localPlayer, this.m_characterInfo, null, opponents);
        final JoinFightLawEvent joinFightLawEvent = new JoinFightLawEvent(localPlayer, FightModel.DUEL, this.m_characterInfo, localPlayer, teamMates);
        AnswerDuelLawEvent answerDuelLawEvent = new AnswerDuelLawEvent(localPlayer, true);
        triggeredLaws.addAll(answerDuelLawEvent.getTriggeringLaws());
        answerDuelLawEvent = new AnswerDuelLawEvent(localPlayer, false);
        triggeredLaws.addAll(answerDuelLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(fightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(joinFightLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    private List<NationLaw> getProbablyTriggeredLaws() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        final KillLawEvent killLawEvent = new KillLawEvent(localPlayer, this.m_characterInfo, (byte)0, (byte)1, FightModel.DUEL);
        triggeredLaws.addAll(killLawEvent.getTriggeringLaws());
        final LoseFightLawEvent loseFightLawEvent = new LoseFightLawEvent(localPlayer, FightModel.DUEL, localPlayer);
        final WonFightLawEvent wonFightLawEvent = new WonFightLawEvent(localPlayer, FightModel.DUEL);
        triggeredLaws.addAll(loseFightLawEvent.getTriggeringLaws());
        triggeredLaws.addAll(wonFightLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    @Override
    public boolean onMessage(final FightInvitationMessage msg) {
        final long id = msg.getFighterId();
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(id);
        if (info == null) {
            this.sendFightInvitationAnswerMessage(id, false);
            return false;
        }
        this.m_characterInfo = info;
        final String name = info.getName();
        if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserById(id) != null) {
            this.sendFightInvitationAnswerMessage(id, false);
            return false;
        }
        final String addedText = this.getLawText();
        String invitMessage = WakfuTranslator.getInstance().getString("fight.invitation.dialog", name);
        if (addedText != null) {
            invitMessage = invitMessage + "\n" + addedText;
        }
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(invitMessage, WakfuMessageBoxConstants.getMessageBoxIconUrl(6), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                FightInvitationMessageHandler.this.sendFightInvitationAnswerMessage(id, type == 8);
            }
        });
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("<text color=\"").append(ChatConstants.CHAT_FIGHT_INFORMATION_COLOR).append("\">");
        stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.dialog", name));
        stringBuffer.append("</text>");
        final ChatMessage chatMessage = new ChatMessage(stringBuffer.toString());
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        return false;
    }
    
    private void sendFightInvitationAnswerMessage(final long inviterId, final boolean answer) {
        final FightInvitAnswerRequestMessage answerMsg = new FightInvitAnswerRequestMessage();
        if (answer) {
            final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(inviterId);
            if (info == null) {
                answerMsg.setAnswer(false);
            }
            else {
                answerMsg.setAnswer(answer);
            }
        }
        else {
            answerMsg.setAnswer(false);
        }
        answerMsg.setLocked(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY));
        answerMsg.setPlayerId(inviterId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answerMsg);
    }
    
    @Override
    public String toString() {
        return "FightInvitationMessageHandler{m_characterInfo=" + this.m_characterInfo + '}';
    }
}
