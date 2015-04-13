package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.group.partySearch.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.client.ui.protocol.message.partySearch.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;

public class UIPartySearchFrame implements MessageFrame
{
    public static final UIPartySearchFrame INSTANCE;
    protected static final Logger m_logger;
    static PartyRequester m_currentRequester;
    private Container m_resultContainer;
    private Container m_selectedOccupationsContainer;
    private PartyOccupationRegistrationViews m_registrationViews;
    private DialogUnloadListener m_dialogUnloadListener;
    private PartyOccupationSearchViews m_searchViews;
    
    public static void addPartyPlayer(final PartyPlayerDefinition definition) {
        if (UIPartySearchFrame.m_currentRequester != null) {
            UIPartySearchFrame.m_currentRequester.merge(definition);
        }
        UIMessage.send((short)19454);
    }
    
    public static void removePartyPlayer(final long characterId) {
        if (UIPartySearchFrame.m_currentRequester != null) {
            UIPartySearchFrame.m_currentRequester.remove(characterId);
            UIMessage.send((short)19454);
        }
        else {
            UIMessage.send((short)19456);
        }
    }
    
    public static boolean isPartyRequesterExists() {
        return UIPartySearchFrame.m_currentRequester != null;
    }
    
    public static void updatePartyRequester(final PartyRequester requester) {
        UIPartySearchFrame.m_currentRequester = requester;
        if (UIPartySearchFrame.INSTANCE.m_registrationViews != null) {
            UIPartySearchFrame.INSTANCE.m_registrationViews.update(requester);
        }
    }
    
    public static void cleanPartyRequester() {
        UIPartySearchFrame.m_currentRequester = null;
        UIPartySearchFrame.INSTANCE.clearView();
    }
    
    public static void doRegister(final PartyOccupationRegistrationViews view) {
        if (!view.canRegister()) {
            return;
        }
        UIPartySearchFrame.m_currentRequester = view.generatePartyRequester();
        final PartySearchFeedbackEnum feedback = UIPartySearchFrame.m_currentRequester.isValidOccupationsForRegister();
        if (feedback != PartySearchFeedbackEnum.NO_ERROR) {
            PartySearchFeedbackManagement.computeFeedback(feedback);
            return;
        }
        view.setRegistered(true);
        final Message msg = new PartySearchRegisterMessage(UIPartySearchFrame.m_currentRequester);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        UIPartySearchFrame.m_logger.info((Object)"[PartySearch] Envoi d'une requ\u00ef¿½te d'enregistrement \u00ef¿½ la recherche");
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19436: {
                final UIMessage msg = (UIMessage)message;
                final PartyOccupationView<PartyOccupation> view = msg.getObjectValue();
                this.m_registrationViews.selectOccupation(view);
                return false;
            }
            case 19437: {
                final UIMessage msg = (UIMessage)message;
                this.m_registrationViews.unselectOccupation(msg.getObjectValue());
                return false;
            }
            case 19438: {
                final UIMessage msg = (UIMessage)message;
                this.m_registrationViews.selectMood(msg.getObjectValue());
                return false;
            }
            case 19439: {
                final UIMessage msg = (UIMessage)message;
                this.m_registrationViews.selectRole(msg.getObjectValue(), msg.getLongValue());
                return false;
            }
            case 19446: {
                final UIMessage msg = (UIMessage)message;
                final PartySearchFilter filter = new PartySearchFilter(msg.getObjectValue());
                this.m_registrationViews.setSearchString(filter.getName());
                this.m_registrationViews.setOccupationMaxLevel(filter.getMaxLevel());
                this.m_registrationViews.setOccupationMinLevel(filter.getMinLevel());
                this.m_registrationViews.filterPvePartyOccupationGroup();
                return false;
            }
            case 19448: {
                this.m_registrationViews.clearView(false, true);
                return false;
            }
            case 19440: {
                final UIMessage msg = (UIMessage)message;
                this.m_registrationViews.setDescription(WordsModerator.getInstance().makeValidSentence(msg.getObjectValue(), true));
                doRegister(this.m_registrationViews);
                return false;
            }
            case 19455: {
                cleanPartyRequester();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new PartySearchUnregisterPlayerMessage());
                return false;
            }
            case 19454: {
                this.m_registrationViews.update(UIPartySearchFrame.m_currentRequester);
                return false;
            }
            case 19456: {
                if (UIPartySearchFrame.m_currentRequester == null) {
                    this.m_registrationViews.updateParty();
                }
                return false;
            }
            case 19457: {
                final AbstractUIMessage msg2 = (AbstractUIMessage)message;
                final boolean display = msg2.getBooleanValue();
                if (display) {
                    this.displayResultUI(true);
                    this.displaySelectedOccupationsUI(false);
                }
                else {
                    this.displayResultUI(false);
                    this.displaySelectedOccupationsUI(true);
                }
                return false;
            }
            case 19441: {
                final UIMessage msg = (UIMessage)message;
                final PartyOccupationView<PartyOccupation> occupation = msg.getObjectValue();
                this.m_searchViews.selectOccupation(occupation);
                return false;
            }
            case 19442: {
                final UIMessage msg = (UIMessage)message;
                final PartyOccupationView<PartyOccupation> occupation = msg.getObjectValue();
                this.m_searchViews.unselectOccupation(occupation);
                return false;
            }
            case 19443: {
                final UIMessage msg = (UIMessage)message;
                this.m_searchViews.selectMood(msg.getObjectValue());
                return false;
            }
            case 19444: {
                final UIMessage msg = (UIMessage)message;
                this.m_searchViews.selectRole(msg.getObjectValue());
                return false;
            }
            case 19451: {
                final UIMessage msg = (UIMessage)message;
                this.m_searchViews.setSelectedBreed(msg.getObjectValue());
                return false;
            }
            case 19447: {
                final UIMessage msg = (UIMessage)message;
                final PartySearchFilter filter = new PartySearchFilter(msg.getObjectValue());
                this.m_searchViews.setSearchString(filter.getName());
                this.m_searchViews.setOccupationMaxLevel(filter.getMaxLevel());
                this.m_searchViews.setOccupationMinLevel(filter.getMinLevel());
                this.m_searchViews.filterPvePartyOccupationGroup();
                return false;
            }
            case 19450: {
                final UIMessage msg = (UIMessage)message;
                final PartySearchFilter filter = new PartySearchFilter(msg.getObjectValue());
                this.m_searchViews.setRequesterMaxLevel(filter.getMaxLevel());
                this.m_searchViews.setRequesterMinLevel(this.m_searchViews.getSelectedView().getLevel());
                final SearchParameters searchParameters = this.m_searchViews.generateSearchParameters();
                if (searchParameters == null) {
                    return false;
                }
                final Message requestMsg = new PartySearchPlayerSearchMessage(searchParameters);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(requestMsg);
                UIPartySearchFrame.m_logger.info((Object)"[PartySearch] Requete de filtrage des demandes");
                return false;
            }
            case 19449: {
                final short level = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
                this.m_searchViews.setOccupationMaxLevel(level);
                this.m_searchViews.setOccupationMinLevel();
                this.m_searchViews.setSearchString("");
                this.m_searchViews.filterPvePartyOccupationGroup();
                return false;
            }
            case 19445: {
                final UIMessage msg = (UIMessage)message;
                this.m_searchViews.setPartyRequesters(msg.getObjectValue());
                return false;
            }
            case 19453: {
                final UIMessage mess = (UIMessage)message;
                final String leaderName = mess.getObjectValue();
                final AbstractUIMessage msg3 = new UIMessage((short)19061);
                msg3.setStringValue(leaderName);
                Worker.getInstance().pushMessage(msg3);
                return false;
            }
            case 19452: {
                final UIMessage mess = (UIMessage)message;
                final Long characterId = mess.getObjectValue();
                if (PartySearchFloodController.getInstance().isFlood(characterId)) {
                    PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.FLOOD);
                    return false;
                }
                final GroupClientInvitationRequestMessage msg4 = new GroupClientInvitationRequestMessage();
                msg4.setGroupType(GroupType.PARTY.getId());
                msg4.setFromPartySearch(true);
                msg4.setOccupationId(this.m_searchViews.getSelectedView().getOccupation().getId());
                msg4.setRequestedPlayerId(characterId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg4);
                UIPartySearchFrame.m_logger.info((Object)"[PartySearch] Invitation en groupe via la recherche");
                return false;
            }
            case 19458: {
                final UIPartyPlayerDefinitionMessage msg5 = (UIPartyPlayerDefinitionMessage)message;
                this.m_registrationViews.addPartyDefinition(msg5.getDefinition());
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void clearView() {
        if (this.m_registrationViews != null) {
            this.m_registrationViews.clearView(true, false);
            this.m_registrationViews.setRegistered(false);
            this.m_registrationViews.setCanRegister(false);
            this.displaySelectedOccupationsUI(this.m_registrationViews.hasSelections());
        }
        if (UIPartySearchFrame.m_currentRequester != null) {
            UIPartySearchFrame.m_currentRequester.isValidOccupationsForRegister();
        }
    }
    
    private void displayResultUI(final boolean display) {
        if (this.m_resultContainer == null) {
            return;
        }
        this.m_resultContainer.setUsePositionTween(true);
        this.m_resultContainer.setX(display ? 490 : 70);
    }
    
    private void displaySelectedOccupationsUI(final boolean display) {
        if (this.m_selectedOccupationsContainer == null) {
            return;
        }
        this.m_selectedOccupationsContainer.setUsePositionTween(true);
        this.m_selectedOccupationsContainer.setX(display ? 500 : 55);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("partySearchDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIPartySearchFrame.INSTANCE);
                    }
                }
            };
            (this.m_searchViews = new PartyOccupationSearchViews()).filterPvePartyOccupationGroup();
            if (UIPartySearchFrame.m_currentRequester == null) {
                this.m_registrationViews = new PartyOccupationRegistrationViews();
            }
            else {
                this.m_registrationViews = new PartyOccupationRegistrationViews(UIPartySearchFrame.m_currentRequester);
            }
            this.m_registrationViews.filterPvePartyOccupationGroup();
            PropertiesProvider.getInstance().setPropertyValue("partyOccupationsSearch", this.m_searchViews);
            PropertiesProvider.getInstance().setPropertyValue("partyOccupations", this.m_registrationViews);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("partySearchDialog", Dialogs.getDialogPath("partySearchDialog"), 32768L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("partySearchDialog");
            this.m_resultContainer = (Container)map.getElement("resultContainer");
            this.m_selectedOccupationsContainer = (Container)map.getElement("selectedOccupationsContainer");
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    UIPartySearchFrame.this.displaySelectedOccupationsUI(true);
                }
            }, 500L, 1);
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", 0, "partySearchDialog");
            Xulor.getInstance().putActionClass("wakfu.partySearch", PartySearchDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("partySearchDialog");
            Xulor.getInstance().removeActionClass("wakfu.partySearch");
            PropertiesProvider.getInstance().removeProperty("partyOccupationsSearch");
            PropertiesProvider.getInstance().removeProperty("partyOccupations");
            this.m_resultContainer = null;
            this.m_selectedOccupationsContainer = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String toString() {
        return "UIPartySearchRegistrationFrame{m_dialogUnloadListener=" + this.m_dialogUnloadListener + ", m_registrationViews=" + this.m_searchViews + '}';
    }
    
    static {
        INSTANCE = new UIPartySearchFrame();
        m_logger = Logger.getLogger((Class)UIPartySearchFrame.class);
        UIPartySearchFrame.m_currentRequester = null;
    }
}
