package com.ankamagames.wakfu.client.core.game.characterInfo;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;

public class QuestActivationManager
{
    public static final QuestActivationManager INSTANCE;
    private int m_currentProposedQuest;
    private long m_currentInviterId;
    private TIntLongHashMap m_proposedQuestsHistory;
    private final ArrayList<QuestActivation> m_activations;
    private DialogUnloadListener m_dialogUnloadListener;
    private DialogCloseRequestListener m_dialogCloseListener;
    
    public QuestActivationManager() {
        super();
        this.m_proposedQuestsHistory = new TIntLongHashMap();
        this.m_activations = new ArrayList<QuestActivation>();
        this.m_dialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if (id.startsWith("questActivationDialog")) {
                    Xulor.getInstance().removeDialogUnloadListener(this);
                    Xulor.getInstance().removeActionClass("wakfu.quests");
                    DialogClosesManager.getInstance().removeDialogCloseRequestListener(QuestActivationManager.this.m_dialogCloseListener);
                }
            }
        };
        this.m_dialogCloseListener = new DialogCloseRequestListener() {
            @Override
            public int onDialogCloseRequest(final String id) {
                if (id.startsWith("questActivationDialog")) {
                    return 2;
                }
                return 0;
            }
        };
    }
    
    private void proposeNewQuestOrClose() {
        if (this.m_activations.isEmpty()) {
            Xulor.getInstance().unload("questActivationDialog");
        }
        else {
            final QuestActivation activation = this.m_activations.remove(this.m_activations.size() - 1);
            this.loadDialog(activation.m_achievementId, activation.m_inviterId);
        }
    }
    
    public void acceptCurrentQuest() {
        if (this.m_currentProposedQuest != -1) {
            final Message answer = new AchievementActivationAnswerMessage(this.m_currentProposedQuest, this.m_currentInviterId, true);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answer);
        }
        this.proposeNewQuestOrClose();
    }
    
    public void refuseCurrentQuest() {
        if (this.m_currentProposedQuest != -1) {
            final Message answer = new AchievementActivationAnswerMessage(this.m_currentProposedQuest, this.m_currentInviterId, false);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answer);
        }
        this.proposeNewQuestOrClose();
    }
    
    public void activateQuest(final int achievementId, final long inviterId) {
        final long lastProposed = this.m_proposedQuestsHistory.get(achievementId);
        final long now = System.currentTimeMillis();
        if (lastProposed != 0L && now - lastProposed < 30000L) {
            return;
        }
        this.m_proposedQuestsHistory.put(achievementId, now);
        if (Xulor.getInstance().isLoaded("questActivationDialog")) {
            this.m_activations.add(new QuestActivation(inviterId, achievementId));
        }
        else {
            this.loadDialog(achievementId, inviterId);
        }
    }
    
    private void loadDialog(final int achievementId, final long inviterId) {
        this.m_currentProposedQuest = achievementId;
        this.m_currentInviterId = inviterId;
        if (!Xulor.getInstance().isLoaded("questActivationDialog")) {
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this.m_dialogCloseListener);
            Xulor.getInstance().load("questActivationDialog", Dialogs.getDialogPath("questActivationDialog"), 8192L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.quests", QuestDialogActions.class);
        }
        final AchievementView quest = AchievementsViewManager.INSTANCE.getAchievement(WakfuGameEntity.getInstance().getLocalPlayer().getId(), achievementId);
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", quest, "questActivationDialog");
    }
    
    public void cleanUp() {
        this.m_activations.clear();
        this.m_currentProposedQuest = -1;
        this.m_currentInviterId = -1L;
        this.m_proposedQuestsHistory.clear();
        DialogClosesManager.getInstance().removeDialogCloseRequestListener(this.m_dialogCloseListener);
        Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
        Xulor.getInstance().removeActionClass("wakfu.quests");
    }
    
    static {
        INSTANCE = new QuestActivationManager();
    }
    
    private static class QuestActivation
    {
        private long m_inviterId;
        private int m_achievementId;
        
        private QuestActivation(final long inviterId, final int achievementId) {
            super();
            this.m_inviterId = inviterId;
            this.m_achievementId = achievementId;
        }
    }
}
