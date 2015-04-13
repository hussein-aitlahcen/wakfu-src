package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.eventsCalendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;
import com.ankamagames.wakfu.client.ui.protocol.message.group.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.*;
import gnu.trove.*;

public class UIEventsCalendarFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final UIEventsCalendarFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private boolean m_isEditingStartTime;
    private WakfuCalendarEventFieldProvider m_editableCalendarEvent;
    private long m_lastUpdateDate;
    
    public static UIEventsCalendarFrame getInstance() {
        return UIEventsCalendarFrame.m_instance;
    }
    
    private UIEventsCalendarFrame() {
        super();
        this.m_isEditingStartTime = true;
        this.m_lastUpdateDate = -1L;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19200: {
                final Widget w = (Widget)Xulor.getInstance().load("eventsCalendarCreateDialog", Dialogs.getDialogPath("eventsCalendarCreateDialog"), 256L, (short)10000);
                (this.m_editableCalendarEvent = new WakfuCalendarEventFieldProvider()).initWithDefaultValues(ClientEventsCalendarView.getInstance().getCurrentDay(), ClientEventsCalendarView.getInstance().getCurrentMonth() + 1, ClientEventsCalendarView.getInstance().getCurrentYear());
                PropertiesProvider.getInstance().setPropertyValue("calendar.event.editable", this.m_editableCalendarEvent);
                WakfuSoundManager.getInstance().windowFadeIn();
                return false;
            }
            case 19202: {
                final UIMessage msg = (UIMessage)message;
                if (msg.getBooleanValue()) {
                    final UICalendarEventCreationMessage calmsg = (UICalendarEventCreationMessage)message;
                    this.m_editableCalendarEvent.setTitle(calmsg.getTitle());
                    this.m_editableCalendarEvent.setDescription(calmsg.getDescription());
                    if (this.m_editableCalendarEvent.areParticipantsLimited()) {
                        this.m_editableCalendarEvent.setMaxParticipants(calmsg.getMaxParticipants());
                        this.m_editableCalendarEvent.setMaxRegistrations(calmsg.getMaxRegistrations());
                    }
                    final AddCalendarEventRequestMessage request = new AddCalendarEventRequestMessage();
                    final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
                    final WakfuCalendarEvent event = this.m_editableCalendarEvent.getEvent();
                    request.setEvent(event);
                    event.addParticipant(character.getId(), character.getName());
                    CharacterEventsCalendar.getInstance().addCalendarEvent(event);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
                    ClientEventsCalendarView.getInstance().fireEventsListChanged();
                }
                Xulor.getInstance().unload("eventsCalendarCreateDialog");
                this.m_editableCalendarEvent = null;
                return false;
            }
            case 19201: {
                final UIEventsCalendarMessage msg2 = (UIEventsCalendarMessage)message;
                if (msg2.getEvent() == null) {
                    return false;
                }
                (this.m_editableCalendarEvent = new WakfuCalendarEventFieldProvider()).setEventAsCopy(msg2.getEvent().getEvent());
                Xulor.getInstance().load("eventsCalendarEditDialog", Dialogs.getDialogPath("eventsCalendarEditDialog"), 256L, (short)10000);
                WakfuSoundManager.getInstance().windowFadeIn();
                PropertiesProvider.getInstance().setPropertyValue("calendar.event.editable", this.m_editableCalendarEvent);
                final boolean canModify = this.m_editableCalendarEvent.getEvent().getOwnerId() == WakfuGameEntity.getInstance().getLocalPlayer().getId() || (this.m_editableCalendarEvent.getEvent().getType() == 3 && AdminRightHelper.checkRight(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightsEnum.MODIFY_CALENDAR));
                PropertiesProvider.getInstance().setPropertyValue("calendar.event.editable.isOwner", canModify);
                return false;
            }
            case 19203: {
                final UIMessage msg = (UIMessage)message;
                if (msg.getBooleanValue()) {
                    final UICalendarEventCreationMessage calmsg = (UICalendarEventCreationMessage)message;
                    this.m_editableCalendarEvent.setTitle(calmsg.getTitle());
                    this.m_editableCalendarEvent.setDescription(calmsg.getDescription());
                    if (this.m_editableCalendarEvent.areParticipantsLimited()) {
                        this.m_editableCalendarEvent.setMaxParticipants(calmsg.getMaxParticipants());
                        this.m_editableCalendarEvent.setMaxRegistrations(calmsg.getMaxRegistrations());
                    }
                    this.m_editableCalendarEvent.applyAndSendChanges();
                    ClientEventsCalendarView.getInstance().fireEventsListChanged();
                }
                Xulor.getInstance().unload("eventsCalendarEditDialog");
                this.m_editableCalendarEvent = null;
                return false;
            }
            case 19205: {
                if (Xulor.getInstance().isLoaded("calendarDialog")) {
                    Xulor.getInstance().unload("calendarDialog");
                }
                else {
                    final UIMessage msg = (UIMessage)message;
                    this.m_isEditingStartTime = msg.getBooleanValue();
                    final CalendarFieldProvider cal = new CalendarFieldProvider();
                    GameDateConst date;
                    if (this.m_isEditingStartTime) {
                        date = this.m_editableCalendarEvent.getEvent().getStartDate();
                    }
                    else {
                        date = this.m_editableCalendarEvent.getEvent().getEndDate();
                    }
                    cal.setDate(date);
                    PropertiesProvider.getInstance().setPropertyValue("calendar.editable", cal);
                    Xulor.getInstance().load("calendarDialog", Dialogs.getDialogPath("calendarDialog"), 256L, (short)10000);
                    WakfuSoundManager.getInstance().windowFadeIn();
                }
                return false;
            }
            case 19204: {
                final UIMessage msg = (UIMessage)message;
                if (this.m_isEditingStartTime) {
                    this.m_editableCalendarEvent.setStartDate((int)msg.getFloatValue(), (int)msg.getLongValue(), msg.getByteValue(), msg.getShortValue(), msg.getIntValue());
                }
                else {
                    this.m_editableCalendarEvent.setEndDate((int)msg.getFloatValue(), (int)msg.getLongValue(), msg.getByteValue(), msg.getShortValue(), msg.getIntValue());
                }
                Xulor.getInstance().unload("calendarDialog");
                return false;
            }
            case 19206: {
                final RegisterToEventRequestMessage msg3 = new RegisterToEventRequestMessage();
                msg3.setEvent(this.m_editableCalendarEvent.getEvent().getUid());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg3);
                return false;
            }
            case 19207: {
                final UnregisterToEventRequestMessage msg4 = new UnregisterToEventRequestMessage();
                msg4.setEvent(this.m_editableCalendarEvent.getEvent().getUid());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg4);
                this.m_editableCalendarEvent.unregisterFromEvent();
                return false;
            }
            case 19208: {
                Xulor.getInstance().unload("eventsCalendarEditDialog");
                CharacterEventsCalendar.getInstance().removeCalendarEvent(this.m_editableCalendarEvent.getSource());
                ClientEventsCalendarView.getInstance().fireEventsListChanged();
                final RemoveCalendarEventRequestMessage request2 = new RemoveCalendarEventRequestMessage();
                request2.setEventUid(this.m_editableCalendarEvent.getEvent().getUid());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request2);
                this.m_editableCalendarEvent = null;
                return false;
            }
            case 19209: {
                final UIMessage msg = (UIMessage)message;
                final ValidateParticipationMessage netmsg = new ValidateParticipationMessage();
                netmsg.setCharacterId(msg.getLongValue());
                netmsg.setEventUid(this.m_editableCalendarEvent.getEvent().getUid());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg);
                return false;
            }
            case 19210: {
                final UIMessage msg = (UIMessage)message;
                final UnvalidateParticipationMessage netmsg2 = new UnvalidateParticipationMessage();
                netmsg2.setCharacterId(msg.getLongValue());
                netmsg2.setEventUid(this.m_editableCalendarEvent.getEvent().getUid());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg2);
                return false;
            }
            case 19211: {
                final TLongObjectIterator<String> it = this.m_editableCalendarEvent.getEvent().getParticipants().iterator();
                while (it.hasNext()) {
                    it.advance();
                    if (!it.value().equals(WakfuGameEntity.getInstance().getLocalPlayer().getName())) {
                        final UIGroupSendInvitationMessage msg5 = new UIGroupSendInvitationMessage();
                        msg5.setGroupType(GroupType.PARTY);
                        msg5.setName(it.value());
                        Worker.getInstance().pushMessage(msg5);
                    }
                }
                return false;
            }
            case 19212: {
                final UIMessage msg = (UIMessage)message;
                final EventParticipationCreatorRequestMessage netmsg3 = new EventParticipationCreatorRequestMessage();
                netmsg3.setEventUid(this.m_editableCalendarEvent.getEvent().getUid());
                netmsg3.setCharacterToInviteId(msg.getStringValue());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg3);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void refreshRegisteredList() {
        if (this.m_editableCalendarEvent != null) {
            this.m_editableCalendarEvent.refreshRegisteredListFromSource();
        }
    }
    
    public void refreshParticipantsList() {
        if (this.m_editableCalendarEvent != null) {
            this.m_editableCalendarEvent.refreshParticipantsListFromSource();
        }
    }
    
    public void reset() {
        this.m_lastUpdateDate = -1L;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            return;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("eventsCalendarDialog");
            Xulor.getInstance().removeActionClass("wakfu.eventsCalendar");
            PropertiesProvider.getInstance().removeProperty("eventsCalendar");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIEventsCalendarFrame.class);
        m_instance = new UIEventsCalendarFrame();
    }
}
