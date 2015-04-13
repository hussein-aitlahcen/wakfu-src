package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.nation.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.nation.*;
import com.ankamagames.framework.reflect.*;

public class UIVoteInformationFrame implements MessageFrame, Runnable
{
    protected static final Logger m_logger;
    private static UIVoteInformationFrame m_instance;
    private MobileStartPathListener m_listener;
    private int m_currentCandidatesOffset;
    private DialogUnloadListener m_dialogUnloadListener;
    private final CitizenAuthorizationRules m_authorizationRules;
    private final TIntHashSet m_knownCandidatesOffsets;
    
    public UIVoteInformationFrame() {
        super();
        this.m_authorizationRules = CitizenAuthorizationRules.getInstance();
        this.m_knownCandidatesOffsets = new TIntHashSet();
    }
    
    public static UIVoteInformationFrame getInstance() {
        return UIVoteInformationFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18401: {
                final UICandidateInfoMessage msg = (UICandidateInfoMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final ClientCitizenComportment citizen = (ClientCitizenComportment)localPlayer.getCitizenComportment();
                if (localPlayer.getLevel() < 1) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("nation.vote.not.level.required", (short)1), 8);
                    return false;
                }
                if (!this.m_authorizationRules.hasRightOfVoteConcerningCitizenRank(citizen)) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("nation.rank.unauthorized.for.vote"), 8);
                    return false;
                }
                final Nation nation = citizen.getNation();
                nation.requestCitizenVote(citizen.getCitizenId(), msg.getCandidateInfo().getCandidateInfo().getId());
                final CandidateInfo candidateInfo = (CandidateInfo)msg.getCandidateInfo().getCandidateInfo();
                String txtMsg;
                if (localPlayer.getId() == candidateInfo.getId()) {
                    txtMsg = WakfuTranslator.getInstance().getString("nation.vote.voteFor.voteDoneForMe");
                }
                else {
                    txtMsg = WakfuTranslator.getInstance().getString("nation.vote.voteFor.voteDoneForSomeone", candidateInfo.getName());
                }
                ChatManager.getInstance().pushMessage(txtMsg, 8);
                citizen.setHasVoted(true);
                this.requestPageUpdate();
                return false;
            }
            case 16161: {
                final ClientCitizenComportment citizen2 = (ClientCitizenComportment)WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
                final Nation nation2 = citizen2.getNation();
                --this.m_currentCandidatesOffset;
                final int nbCandidates = nation2.isVoteRunning() ? nation2.getNbCandidates() : nation2.getElectionHistory().getNbCandidates();
                if (nbCandidates < 3) {
                    return false;
                }
                if (this.m_currentCandidatesOffset < 0) {
                    this.m_currentCandidatesOffset = (int)Math.floor((nbCandidates - 1.0f) / 3.0f);
                }
                if (!nation2.isVoteRunning() && this.m_knownCandidatesOffsets.contains(this.m_currentCandidatesOffset)) {
                    NationDisplayer.getInstance().updateUI();
                    return false;
                }
                this.m_knownCandidatesOffsets.add(this.m_currentCandidatesOffset);
                this.requestPageUpdate();
                return false;
            }
            case 16162: {
                final ClientCitizenComportment citizen2 = (ClientCitizenComportment)WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
                final Nation nation2 = citizen2.getNation();
                ++this.m_currentCandidatesOffset;
                final int nbCandidates = nation2.isVoteRunning() ? nation2.getNbCandidates() : nation2.getElectionHistory().getNbCandidates();
                if (nbCandidates < 3) {
                    return false;
                }
                if (nbCandidates <= this.getCurrentCandidatesOffset() * 3) {
                    this.m_currentCandidatesOffset = 0;
                }
                if (!nation2.isVoteRunning() && this.m_knownCandidatesOffsets.contains(this.m_currentCandidatesOffset)) {
                    NationDisplayer.getInstance().updateUI();
                    return false;
                }
                this.m_knownCandidatesOffsets.add(this.m_currentCandidatesOffset);
                this.requestPageUpdate();
                return false;
            }
            case 18400: {
                final UIMessage msg2 = (UIMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final ClientCitizenComportment citizen = (ClientCitizenComportment)localPlayer.getCitizenComportment();
                if (!this.m_authorizationRules.hasRightToStandForElectionConcerningCitizenRank(citizen)) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("nation.rank.unauthorized.for.registration"), 8);
                    return false;
                }
                final Nation nation = citizen.getNation();
                final String slogan = msg2.getStringValue().replace("\r\n", " ").replace("\n", " ");
                nation.requestRegisterCandidate(localPlayer.getId(), slogan);
                citizen.updateCandidateInfo();
                this.requestPageUpdate();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public int getCandidatesPerPage() {
        return 3 + ((this.m_currentCandidatesOffset == 0) ? 1 : 0);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_listener = new MobileStartPathListener() {
                @Override
                public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                    WakfuGameEntity.getInstance().removeFrame(UIVoteInformationFrame.getInstance());
                }
            };
            character.getActor().addStartPathListener(this.m_listener);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("voteInformationDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIVoteInformationFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("voteInformationDialog", Dialogs.getDialogPath("voteInformationDialog"), 32769L, (short)10000);
            final ClientCitizenComportment citizen = (ClientCitizenComportment)character.getCitizenComportment();
            final Nation nation = citizen.getNation();
            if (nation.isVoteRunning()) {
                ProcessScheduler.getInstance().schedule(this, 1000L);
                PropertiesProvider.getInstance().setPropertyValue("electionSloganDirty", false);
            }
            PropertiesProvider.getInstance().setPropertyValue("voteInscription.feeWarning", WakfuTranslator.getInstance().getString("nation.vote.eligible.warning", nation.getCandidateRegistrationFee()));
            this.m_currentCandidatesOffset = 0;
            this.m_knownCandidatesOffsets.add(this.m_currentCandidatesOffset);
            this.requestPageUpdate();
            Xulor.getInstance().putActionClass("wakfu.voteInformation", VoteInformationDialogActions.class);
        }
    }
    
    private void requestPageUpdate() {
        final NationVoteInformationRequestMessage msg = new NationVoteInformationRequestMessage();
        msg.setOffset(this.getCurrentCandidatesOffset() * 3);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            NationDisplayer.getInstance().clearElections();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            localPlayer.getActor().removeStartListener(this.m_listener);
            this.m_listener = null;
            this.m_knownCandidatesOffsets.clear();
            final Nation nation = localPlayer.getCitizenComportment().getNation();
            if (!nation.isVoteRunning()) {
                nation.getElectionHistory().clear();
            }
            else {
                PropertiesProvider.getInstance().removeProperty("voteInscription.feeWarning");
                PropertiesProvider.getInstance().removeProperty("electionSloganDirty");
            }
            ProcessScheduler.getInstance().remove(this);
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("voteInformationDialog");
            PropertiesProvider.getInstance().removeProperty("voteList");
            Xulor.getInstance().removeActionClass("wakfu.voteInformation");
        }
    }
    
    @Override
    public void run() {
        PropertiesProvider.getInstance().firePropertyValueChanged(NationDisplayer.getInstance(), "electionClosureDescription");
    }
    
    public int getCurrentCandidatesOffset() {
        return this.m_currentCandidatesOffset;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIVoteInformationFrame.class);
        UIVoteInformationFrame.m_instance = new UIVoteInformationFrame();
    }
}
