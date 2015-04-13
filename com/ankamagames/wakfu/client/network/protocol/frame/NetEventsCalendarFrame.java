package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util.*;

public class NetEventsCalendarFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static NetEventsCalendarFrame m_instance;
    
    public static NetEventsCalendarFrame getInstance() {
        return NetEventsCalendarFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final int msgID = message.getId();
        switch (msgID) {
            case 562: {
                this.caseEventsCalendarInfosAnswerMessage((EventsCalendarInfosAnswerMessage)message);
                return false;
            }
            case 564: {
                this.caseAddCalendarEventAnswerMessage((AddCalendarEventAnswerMessage)message);
                return false;
            }
            case 566: {
                this.caseRemoveCalendarEventAnswerMessage((RemoveCalendarEventAnswerMessage)message);
                return false;
            }
            case 568: {
                this.caseCalendarEventNotificationMessage((CalendarEventNotificationMessage)message);
                return false;
            }
            case 570: {
                this.caseRegisterToEventAnswerMessage((RegisterToEventAnswerMessage)message);
                return false;
            }
            case 574: {
                this.caseValidateParticipationResultMessage((ValidateParticipationResultMessage)message);
                return false;
            }
            case 576: {
                this.caseUnvalidateRegistrationResultMessage((UnvalidateParticipationResultMessage)message);
                return false;
            }
            case 578: {
                this.caseCalendarEventsUpdateMessage((CalendarEventsUpdateMessage)message);
                return false;
            }
            case 580: {
                this.caseEventParticipationServerRequestMessage((EventParticipationServerRequestMessage)message);
                return false;
            }
            case 582: {
                this.caseEventParticipationServerAnswerMessage((EventParticipationServerAnswerMessage)message);
                return false;
            }
            case 584: {
                this.caseCalendarEventsInformationMessage((CalendarEventsInformationMessage)message);
                return false;
            }
            case 586: {
                this.caseSetEventEndDateResultMessage((SetEventEndDateResultMessage)message);
                return false;
            }
            case 594: {
                this.caseSetEventStartDateResultMessage((SetEventStartDateResultMessage)message);
                return false;
            }
            case 588: {
                this.caseSetEventDescResultMessage((SetEventDescResultMessage)message);
                return false;
            }
            case 592: {
                this.caseRegisteringNotificationMessage((EventActionNotificationMessage)message);
                return false;
            }
            case 590: {
                this.caseSetEventTitleResultMessage((SetEventTitleResultMessage)message);
                return false;
            }
            case 596: {
                this.caseModifyCalendarEventResultMessage((ModifyCalendarEventResultMessage)message);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void caseModifyCalendarEventResultMessage(final ModifyCalendarEventResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        NetEventsCalendarFrame.m_logger.warn((Object)WakfuCalendarErrorCode.log("Erreur lors de la modification d'un evenement", errorCode));
        final WakfuCalendarEvent oldEvent = msg.getEvent();
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.modification.error"), 3);
        this.chatLog(errorCode, oldEvent);
        CharacterEventsCalendar.getInstance().modifyEvent(oldEvent);
        ClientEventsCalendarView.getInstance().fireEventsListChanged();
    }
    
    private void caseSetEventStartDateResultMessage(final SetEventStartDateResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        NetEventsCalendarFrame.m_logger.warn((Object)WakfuCalendarErrorCode.log("Impossible de changer la date de debut de l'\u00e9v\u00e8nement", errorCode));
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.start.date.change.error"), 3);
        this.chatLog(errorCode, CharacterEventsCalendar.getInstance().getEventById(msg.getEventId()));
        CharacterEventsCalendar.getInstance().changeEventStartDate(msg.getEventId(), msg.getDate());
        ClientEventsCalendarView.getInstance().fireEventsListChanged();
    }
    
    private void caseSetEventTitleResultMessage(final SetEventTitleResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        NetEventsCalendarFrame.m_logger.warn((Object)WakfuCalendarErrorCode.log("Impossible de changer le titre de l'\u00e9v\u00e8nement", errorCode));
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Bizarre, on a re\u00e7u une reponse sur un changement de description d'un event mais nous ne connaissons pas cet event");
            return;
        }
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.title.change.error", CalendarEventFormatter.formatTitle(event)), 4);
        event.setDesc(msg.getOldDesc());
    }
    
    private void caseRegisteringNotificationMessage(final EventActionNotificationMessage msg) {
        final byte actionId = msg.getActionId();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.error((Object)"On est notifi\u00e9 d'une modification d'un \u00e9v\u00e8nement dont on n'a pas connaissance");
            return;
        }
        switch (actionId) {
            case 1: {
                event.addRegistered(msg.getPerformerId(), msg.getPerformerName());
                UIEventsCalendarFrame.getInstance().refreshRegisteredList();
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.inscription", msg.getPerformerName(), CalendarEventFormatter.formatTitle(event)), 4);
            }
            case 2: {
                event.removeParticipant(msg.getPerformerId());
                UIEventsCalendarFrame.getInstance().refreshRegisteredList();
                UIEventsCalendarFrame.getInstance().refreshParticipantsList();
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.other.leave.participants", msg.getPerformerName(), CalendarEventFormatter.formatTitle(event)), 4);
            }
            case 3: {
                event.removeRegistered(msg.getPerformerId());
                UIEventsCalendarFrame.getInstance().refreshRegisteredList();
            }
            default: {
                NetEventsCalendarFrame.m_logger.error((Object)("[CALENDAR] Action de type inconnue : " + actionId));
            }
        }
    }
    
    private void caseSetEventDescResultMessage(final SetEventDescResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        switch (errorCode) {
            case 0: {
                NetEventsCalendarFrame.m_logger.error((Object)"[CALENDAR] Le serveur ne devrait envoyer aucune reponse si le changement de desc s'est bien passe");
                return;
            }
            case 11: {
                NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Impossible de changer la desc de l'\u00e9v\u00e8nement : \u00e9v\u00e8nement inconnu");
                break;
            }
            case 14: {
                NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Impossible de changer la desc de l'\u00e9v\u00e8nement personnnage non connect\u00e9");
                break;
            }
            case 13: {
                NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Impossible de changer la desc de l'\u00e9v\u00e8nement personnage non propri\u00e9taire de l'evenement");
                break;
            }
            case 26: {
                NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Impossible de changer la desc de l'\u00e9v\u00e8nement car on ne dipose pas des droits n\u00e9cessaires");
                break;
            }
            default: {
                NetEventsCalendarFrame.m_logger.error((Object)"[CALENDAR] Le serveur envoie un code d'erreur non trait\u00e9 dans le cas d'un message de reponse au changement de desc");
                break;
            }
        }
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"[EVENTS_CALENDAR] Bizarre, on a re\u00e7u une reponse sur un changement de description d'un event mais nous ne connaissons pas cet event");
            return;
        }
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.desc.change.error", CalendarEventFormatter.formatTitle(event)), 4);
        event.setDesc(msg.getOldDesc());
    }
    
    private void caseSetEventEndDateResultMessage(final SetEventEndDateResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        Label_0140: {
            switch (errorCode) {
                case 0: {
                    NetEventsCalendarFrame.m_logger.error((Object)"[CALENDAR] Le serveur ne devrait envoyer aucune reponse si le changement de titre s'est bien passe");
                    return;
                }
                case 11: {
                    NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Impossible de changer le titre de l'\u00e9v\u00e8nement : \u00e9v\u00e8nement inconnu");
                    break Label_0140;
                }
                case 14: {
                    NetEventsCalendarFrame.m_logger.warn((Object)"[CALENDAR] Impossible de changer le titre de l'\u00e9v\u00e8nement personnnage non connect\u00e9");
                    break Label_0140;
                }
                case 13: {
                    NetEventsCalendarFrame.m_logger.warn((Object)"[CLAENDAR] Impossible de changer le titre de l'\u00e9v\u00e8nement personnage non propri\u00e9taire de l'evenement");
                    break Label_0140;
                }
                case 26: {
                    NetEventsCalendarFrame.m_logger.warn((Object)"[CLAENDAR] Impossible de changer le titre de l'\u00e9v\u00e8nement car on ne dispose pas des droits n\u00e9cessaires");
                    break Label_0140;
                }
                case 12: {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.end.date.preceding.start"), 4);
                    break;
                }
            }
            NetEventsCalendarFrame.m_logger.error((Object)"[CALENDAR] Le serveur envoie un code d'erreur non trait\u00e9 dans le cas d'un message de reponse au changement de desc");
        }
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"[EVENTS_CALENDAR] Bizarre, on a re\u00e7u une reponse sur un changement de date de fin d'un event mais nous ne connaissons pas cet event");
            return;
        }
        event.setEndDate(msg.getDate());
    }
    
    private void caseRemoveCalendarEventAnswerMessage(final RemoveCalendarEventAnswerMessage msg) {
        final byte errorCode = msg.getErrorCode();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventUid());
        if (errorCode == 0) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.removed", CalendarEventFormatter.formatTitle(event)), 4);
            CharacterEventsCalendar.getInstance().removeCalendarEvent(event);
            NetEventsCalendarFrame.m_logger.info((Object)CharacterEventsCalendar.getInstance().toString());
            return;
        }
        if (errorCode == 9) {
            NetEventsCalendarFrame.m_logger.info((Object)"[CALENDAR] Le serveur a rejet\u00e9 la demande pour le retrait d'un evenement au calendrier");
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.removed.error", CalendarEventFormatter.formatTitle(event)), 4);
            return;
        }
        NetEventsCalendarFrame.m_logger.error((Object)"[CALENDAR] Le serveur envoie un code d'erreur non trait\u00e9 dans le cas d'un message de reponse au retrait d'un evenement");
    }
    
    private void caseCalendarEventsInformationMessage(final CalendarEventsInformationMessage msg) {
        final Set<WakfuCalendarEvent> events = msg.getCalendarEvents();
        final Iterator<WakfuCalendarEvent> it = events.iterator();
        while (it.hasNext()) {
            CharacterEventsCalendar.getInstance().addCalendarEvent((WakfuCalendarEvent)it.next());
        }
    }
    
    private void caseEventParticipationServerAnswerMessage(final EventParticipationServerAnswerMessage msg) {
        final long eventId = msg.getEventId();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(eventId);
        final byte result = msg.getResult();
        switch (result) {
            case 8: {
                event.addParticipant(msg.getInvitedId(), msg.getInvitedName());
                UIEventsCalendarFrame.getInstance().refreshParticipantsList();
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.invitation.accepted", msg.getInvitedName(), CalendarEventFormatter.formatTitle(event)), 4);
                break;
            }
            case 7: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.invitation.refused", msg.getInvitedName(), CalendarEventFormatter.formatTitle(event)), 4);
                break;
            }
            case 5: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.invited.not.connected", msg.getInvitedName()), 4);
                break;
            }
            case 6: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.invited.has.pending.invitation", msg.getInvitedName()), 4);
                break;
            }
            default: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.invitation.error", msg.getInvitedName()), 4);
                this.chatLog(result, event);
                break;
            }
        }
    }
    
    private void caseEventParticipationServerRequestMessage(final EventParticipationServerRequestMessage msg) {
        final String invitMsg = WakfuTranslator.getInstance().getString("calendar.event.invitation.message", msg.getInviterName(), msg.getEventName());
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(invitMsg, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                final EventParticipationClientAnswerMessage answerMsg = new EventParticipationClientAnswerMessage();
                answerMsg.setAnswer((byte)((type == 8) ? 8 : 7));
                answerMsg.setEventUid(msg.getEventId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answerMsg);
            }
        });
    }
    
    private void caseCalendarEventsUpdateMessage(final CalendarEventsUpdateMessage msg) {
        final Set<WakfuCalendarEvent> events = msg.getCalendarEvents();
        for (final WakfuCalendarEvent event : events) {
            CharacterEventsCalendar.getInstance().addCalendarEvent(event);
        }
        ClientEventsCalendarView.getInstance().fireEventsListChanged();
    }
    
    private void caseUnvalidateRegistrationResultMessage(final UnvalidateParticipationResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"On recoit du serveur une reponse d'invalidation de participation sur un event que le client ne connait pas");
            return;
        }
        if (errorCode == 0) {
            final String name = event.removeParticipant(msg.getCharacterId());
            event.addRegistered(msg.getCharacterId(), name);
            UIEventsCalendarFrame.getInstance().refreshRegisteredList();
            UIEventsCalendarFrame.getInstance().refreshParticipantsList();
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.participation.invalidation", name, CalendarEventFormatter.formatTitle(event)), 4);
        }
        else {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.participation.unvalidation.error", CalendarEventFormatter.formatTitle(event)), 4);
            this.chatLog(errorCode, event);
        }
    }
    
    private void caseValidateParticipationResultMessage(final ValidateParticipationResultMessage msg) {
        final byte errorCode = msg.getErrorCode();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"On recoit du serveur une reponse de validation de participation sur un event que le client ne connait pas");
            return;
        }
        if (errorCode == 0) {
            final String name = event.removeRegistered(msg.getCharacterId());
            event.addParticipant(msg.getCharacterId(), name);
            UIEventsCalendarFrame.getInstance().refreshRegisteredList();
            UIEventsCalendarFrame.getInstance().refreshParticipantsList();
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.participation.validation.success", name, CalendarEventFormatter.formatTitle(event)), 4);
        }
        else {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.participation.validation.error", CalendarEventFormatter.formatTitle(event)), 4);
            this.chatLog(errorCode, event);
        }
    }
    
    private void caseRegisterToEventAnswerMessage(final RegisterToEventAnswerMessage msg) {
        final byte errorCode = msg.getErrorCode();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(msg.getEventId());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"On recoit du serveur une reponse d'inscription sur un event que le client ne connait pas");
            return;
        }
        if (errorCode == 0) {
            event.addRegistered(WakfuGameEntity.getInstance().getLocalPlayer().getId(), WakfuGameEntity.getInstance().getLocalPlayer().getName());
            UIEventsCalendarFrame.getInstance().refreshRegisteredList();
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.you.inscription.success", CalendarEventFormatter.formatTitle(event)), 4);
            return;
        }
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.you.inscription.error", event.getTitle()), 3);
        this.chatLog(errorCode, event);
        NetEventsCalendarFrame.m_logger.error((Object)"[CALENDAR] Le serveur envoie un code d'erreur non trait\u00e9 dans le cas d'un message de reponse a la demande d'inscription");
    }
    
    private void caseCalendarEventNotificationMessage(final CalendarEventNotificationMessage msg) {
        final Set<WakfuCalendarEvent> events = msg.getCalendarEvents();
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.todays.events"), 4);
        for (final WakfuCalendarEvent event : events) {
            if (event != null) {
                final String chatMsg = GameDateFormatter.format(WakfuTranslator.getInstance().getString("calendar.expectations", CalendarEventFormatter.formatTitle(event)), event.getStartDate());
                ChatManager.getInstance().pushMessage(chatMsg, 4);
            }
        }
    }
    
    private void caseAddCalendarEventAnswerMessage(final AddCalendarEventAnswerMessage msg) {
        final byte errorCode = msg.getErrorCode();
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventByAlternativeHashCode(msg.getAlternativeCode());
        if (event == null) {
            NetEventsCalendarFrame.m_logger.warn((Object)"Impossible de valider l'ajout de l'evenement chez le client, il ne connait pas l'evenement avec le hashcode specifie");
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.events.add.error", ""), 4);
            ClientEventsCalendarView.getInstance().fireEventsListChanged();
            return;
        }
        if (errorCode != 0) {
            this.chatLog(errorCode, event);
            NetEventsCalendarFrame.m_logger.info((Object)"[CALENDAR] Le serveur a rejet\u00e9 la demande pour l'ajout d'un evenement au calendrier, on retire le dernier evt ajoute par le client");
            CharacterEventsCalendar.getInstance().removeCalendarEvent(event);
            ClientEventsCalendarView.getInstance().fireEventsListChanged();
            return;
        }
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.events.add.success", CalendarEventFormatter.formatTitle(event)), 4);
        CharacterEventsCalendar.getInstance().validateEvent(event);
        CharacterEventsCalendar.getInstance().changeEventUid(event, msg.getNewUid());
        ClientEventsCalendarView.getInstance().fireEventsListChanged();
    }
    
    private void caseEventsCalendarInfosAnswerMessage(final EventsCalendarInfosAnswerMessage msg) {
        final Set<WakfuCalendarEvent> events = msg.getCalendar().getEvents();
        for (final WakfuCalendarEvent event : events) {
            CharacterEventsCalendar.getInstance().addCalendarEvent(event);
        }
        CharacterEventsCalendar.getInstance().clearLastAddedStack();
        ClientEventsCalendarView.getInstance().fireEventsListChanged();
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void chatLog(final byte errorCode, final WakfuCalendarEvent event) {
        switch (errorCode) {
            case 1: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.events.add.error", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 22: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.events.too.many.for.owner", (byte)20), 3);
                break;
            }
            case 19: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.already.exists", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 27: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.start.date.before.today", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 29: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.expired", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 28: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.already.registered", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 18: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.too.much.registrations", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 30: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.not.registered", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 17: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.too.much.participants", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 13: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.you.not.owner", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 32: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.not.participant", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            case 33: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.event.too.much.in.calendar", CalendarEventFormatter.formatTitle(event)), 3);
                break;
            }
            default: {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("calendar.events.unknown.error", errorCode), 3);
                break;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetEventsCalendarFrame.class);
        NetEventsCalendarFrame.m_instance = new NetEventsCalendarFrame();
    }
}
