package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import java.util.*;
import gnu.trove.*;

public class WakfuCalendarEventFieldProvider implements FieldProvider, Comparable<WakfuCalendarEventFieldProvider>
{
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String TYPE_FIELD = "type";
    public static final String TYPES_FIELD = "types";
    public static final String SELECTED_TYPE_FIELD = "selectedType";
    public static final String TYPE_ICON_URL_FIELD = "iconUrl";
    public static final String TYPE_DESCRIPTION_FIELD = "typeDescription";
    public static final String ARE_PARTICIPANTS_LIMITED_FIELD = "areParticipantsLimited";
    public static final String MAX_PARTICIPANTS_FIELD = "maxParticipants";
    public static final String NUM_PARTICIPANTS_FIELD = "numParticipants";
    public static final String NUM_PARTICIPANTS_DESC_FIELD = "numParticipantsDesc";
    public static final String MAX_REGISTRATIONS_FIELD = "maxRegistrations";
    public static final String NUM_REGISTRATIONS_FIELD = "numRegistrations";
    public static final String NUM_REGISTRATIONS_DESC_FIELD = "numRegistrationsDesc";
    public static final String START_DATE_DESC_FIELD = "startDateDesc";
    public static final String END_DATE_DESC_FIELD = "endDateDesc";
    public static final String IS_REGISTERED_FIELD = "isRegistered";
    public static final String REGISTERED_LIST_FIELD = "registeredList";
    public static final String IS_VALIDATED_FIELD = "isValidated";
    public static final String VALIDATED_LIST_FIELD = "validatedList";
    public static final String CAN_EDIT_PARTICIPANTS_LIMIT_FIELD = "canEditParticipantsLimit";
    private WakfuCalendarEvent m_event;
    private WakfuCalendarEvent m_source;
    private boolean m_titleChanged;
    private boolean m_descriptionChanged;
    private boolean m_startChanged;
    private boolean m_endChanged;
    private WakfuCalendarEventTypeFieldProvider m_selectedType;
    
    public WakfuCalendarEventFieldProvider() {
        super();
        this.m_titleChanged = false;
        this.m_descriptionChanged = false;
        this.m_startChanged = false;
        this.m_endChanged = false;
    }
    
    @Override
    public int compareTo(final WakfuCalendarEventFieldProvider o) {
        if (this.m_event != null) {
            return this.m_event.getStartDate().compareTo(o.m_event.getStartDate());
        }
        return 0;
    }
    
    public void initWithDefaultValues(final int day, final int month, final int year) {
        this.m_event = new WakfuCalendarEvent();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            this.m_event.setOwnerId(localPlayer.getId());
        }
        this.m_event.setTextsUserDefined(true);
        this.m_event.setType((byte)1);
        this.m_event.setSubType((byte)0);
        this.m_selectedType = WakfuCalendarEventTypeFieldProvider.getByType((byte)1);
        this.m_event.setMaxParticipants((byte)(-1));
        final Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.add(10, 1);
        int hourAMPM = (c.get(9) == 0) ? 0 : 12;
        GameDate gd = new GameDate(0, 0, c.get(10) + hourAMPM, day, month, year);
        this.m_event.setStartDate(gd);
        c.add(10, 1);
        hourAMPM = ((c.get(9) == 0) ? 0 : 12);
        gd = new GameDate(0, 0, c.get(10) + hourAMPM, day, month, year);
        this.m_event.setEndDate(gd);
        this.m_descriptionChanged = false;
        this.m_titleChanged = false;
        this.m_startChanged = false;
        this.m_endChanged = false;
    }
    
    public void setStartDate(final int minutes, final int hour, final int day, final int month, final int year) {
        final GameDate startDate = this.m_event.getModifiableStartDate();
        final long startTimeInMillis = startDate.toLong();
        final GameDate endDate = this.m_event.getModifiableEndDate();
        final long endTimeInMillis = endDate.toLong();
        final int diffTimeInSeconds = (int)((endTimeInMillis - startTimeInMillis) / 1000.0f);
        startDate.set(0, minutes, hour, day, month, year);
        endDate.set(diffTimeInSeconds, minutes, hour, day, month, year);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "startDateDesc", "endDateDesc");
    }
    
    public void setEndDate(final int minutes, final int hour, final int day, final int month, final int year) {
        final GameDate date = this.m_event.getModifiableEndDate();
        date.set(0, minutes, hour, day, month, year);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "endDateDesc");
    }
    
    public void setAreParticipantsLimited(final boolean limited) {
        final byte maxParticipants = this.m_event.getMaxParticipants();
        if ((limited && maxParticipants >= 1) || (!limited && maxParticipants < 1)) {
            return;
        }
        if (limited) {
            this.m_event.setMaxParticipants((byte)2);
            this.m_event.setMaxRegistrations((byte)2);
        }
        else {
            this.m_event.setMaxParticipants((byte)(-1));
            this.m_event.setMaxRegistrations((byte)(-1));
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "areParticipantsLimited", "maxParticipants", "maxRegistrations");
    }
    
    public boolean areParticipantsLimited() {
        return this.m_event.getMaxParticipants() != -1;
    }
    
    public void setMaxParticipants(final byte max) {
        if (max < 1 || max > 20) {
            return;
        }
        this.m_event.setMaxParticipants(max);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "maxParticipants");
    }
    
    public void setMaxRegistrations(final byte max) {
        if (max < 1 || max > 20) {
            return;
        }
        this.m_event.setMaxRegistrations(max);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "maxRegistrations");
    }
    
    public void setType(final byte type) {
        if (this.m_event.getType() == type) {
            return;
        }
        this.m_event.setType(type);
        this.m_selectedType = WakfuCalendarEventTypeFieldProvider.getByType(type);
        switch (type) {
            case 3: {
                this.setAreParticipantsLimited(false);
                break;
            }
            case 2: {
                this.setAreParticipantsLimited(true);
                break;
            }
            case 4: {
                this.setAreParticipantsLimited(false);
                break;
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "type", "typeDescription", "iconUrl", "selectedType", "canEditParticipantsLimit");
    }
    
    public void setSubType(final byte subType) {
        if (this.m_event.getSubType() == subType) {
            return;
        }
        this.m_event.setSubType(subType);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "iconUrl", "selectedType");
    }
    
    public void setTitle(final String title) {
        final String oldTitle = this.m_event.getTitle();
        if (oldTitle == title || (oldTitle != null && oldTitle.equals(title)) || (title != null && title.equals(oldTitle))) {
            return;
        }
        this.m_event.setTitle(title);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "title");
    }
    
    public void setDescription(final String desc) {
        final String oldDesc = this.m_event.getDescription();
        if (oldDesc == desc || (oldDesc != null && oldDesc.equals(desc)) || (desc != null && desc.equals(oldDesc))) {
            return;
        }
        this.m_event.setDesc(desc);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "description");
    }
    
    public void setEvent(final WakfuCalendarEvent event) {
        this.m_event = event;
        this.m_selectedType = WakfuCalendarEventTypeFieldProvider.getByType(this.m_event.getType());
    }
    
    public void setEventAsCopy(final WakfuCalendarEvent event) {
        this.m_source = event;
        (this.m_event = new WakfuCalendarEvent()).copyFrom(this.m_source);
        this.m_selectedType = WakfuCalendarEventTypeFieldProvider.getByType(this.m_event.getType());
        this.m_descriptionChanged = false;
        this.m_titleChanged = false;
        this.m_startChanged = false;
        this.m_endChanged = false;
    }
    
    public boolean hasTitleChanged() {
        return this.m_source != null && !this.m_source.getTitle().equals(this.m_event.getTitle());
    }
    
    public boolean hasDescriptionChanged() {
        return this.m_source != null && !this.m_source.getDescription().equals(this.m_event.getDescription());
    }
    
    public boolean hasStartDateChanged() {
        return this.m_source != null && this.m_source.getStartDate().compareTo(this.m_event.getStartDate()) != 0;
    }
    
    public boolean hasEndDateChanged() {
        return this.m_source != null && this.m_source.getEndDate().compareTo(this.m_event.getEndDate()) != 0;
    }
    
    public boolean hasMaxParticipantsChanged() {
        return this.m_source != null && this.m_source.getMaxParticipants() != this.m_event.getMaxParticipants();
    }
    
    public boolean hasMaxRegistrationsChanged() {
        return this.m_source != null && this.m_source.getMaxRegistrations() != this.m_event.getMaxRegistrations();
    }
    
    public void refreshRegisteredListFromSource() {
        if (this.m_source == null) {
            return;
        }
        final TLongObjectHashMap<String> registered = this.m_event.getRegistered();
        registered.clear();
        final TLongObjectHashMap<String> source = this.m_source.getRegistered();
        if (source.size() > 0) {
            final TLongObjectIterator<String> it = source.iterator();
            while (it.hasNext()) {
                it.advance();
                registered.put(it.key(), it.value());
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isRegistered", "registeredList", "numRegistrationsDesc", "numRegistrations");
    }
    
    public void refreshParticipantsListFromSource() {
        if (this.m_source == null) {
            return;
        }
        final TLongObjectHashMap<String> participants = this.m_event.getParticipants();
        participants.clear();
        final TLongObjectHashMap<String> source = this.m_source.getParticipants();
        if (source.size() > 0) {
            final TLongObjectIterator<String> it = source.iterator();
            while (it.hasNext()) {
                it.advance();
                participants.put(it.key(), it.value());
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isValidated", "validatedList", "numParticipants", "numParticipantsDesc");
    }
    
    public void unregisterFromEvent() {
        if (this.m_source == null) {
            return;
        }
        final long id = WakfuGameEntity.getInstance().getLocalPlayer().getId();
        if (this.m_event.getRegistered().containsKey(id)) {
            this.m_source.removeRegistered(id);
            this.refreshRegisteredListFromSource();
        }
        else if (this.m_event.getParticipants().containsKey(id)) {
            this.m_source.removeParticipant(id);
            this.refreshParticipantsListFromSource();
        }
    }
    
    public WakfuCalendarEvent getEvent() {
        return this.m_event;
    }
    
    public WakfuCalendarEvent getSource() {
        return this.m_source;
    }
    
    public void applyAndSendChanges() {
        if (this.hasTitleChanged()) {
            this.m_source.setTitle(this.m_event.getTitle());
            final SetEventTitleMessage msg = new SetEventTitleMessage();
            msg.setTitle(this.m_source.getTitle());
            msg.setEventUid(this.m_source.getUid());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        }
        if (this.hasDescriptionChanged()) {
            this.m_source.setDesc(this.m_event.getDescription());
            final SetEventDescMessage msg2 = new SetEventDescMessage();
            msg2.setDesc(this.m_source.getDescription());
            msg2.setEventUid(this.m_source.getUid());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg2);
        }
        if (this.hasMaxParticipantsChanged()) {
            this.m_source.setMaxParticipants(this.m_event.getMaxParticipants());
            final SetEventMaxParticipantsOrRegistrationsMessage msg3 = new SetEventMaxParticipantsOrRegistrationsMessage();
            msg3.setEventUid(this.m_source.getUid());
            msg3.setNewMax(this.m_source.getMaxParticipants());
            msg3.setType((byte)2);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg3);
        }
        if (this.hasMaxRegistrationsChanged()) {
            this.m_source.setMaxRegistrations(this.m_event.getMaxRegistrations());
            final SetEventMaxParticipantsOrRegistrationsMessage msg3 = new SetEventMaxParticipantsOrRegistrationsMessage();
            msg3.setEventUid(this.m_source.getUid());
            msg3.setNewMax(this.m_source.getMaxRegistrations());
            msg3.setType((byte)1);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg3);
        }
        if (this.hasStartDateChanged()) {
            CharacterEventsCalendar.getInstance().changeEventStartDate(this.m_source.getUid(), this.m_event.getStartDate());
            final SetEventStartDateMessage request = new SetEventStartDateMessage();
            request.setDate(this.m_source.getStartDate());
            request.setEventUid(this.m_source.getUid());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
        }
        if (this.hasEndDateChanged()) {
            this.m_source.setEndDate(this.m_event.getEndDate());
            final SetEventEndDateMessage request2 = new SetEventEndDateMessage();
            request2.setDate(this.m_source.getEndDate());
            request2.setEventUid(this.m_source.getUid());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request2);
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return CalendarEventFormatter.formatTitle(this.m_event);
        }
        if (fieldName.equals("description")) {
            return CalendarEventFormatter.formatDescription(this.m_event);
        }
        if (fieldName.equals("areParticipantsLimited")) {
            return this.areParticipantsLimited();
        }
        if (fieldName.equals("maxParticipants")) {
            return this.m_event.getMaxParticipants();
        }
        if (fieldName.equals("maxRegistrations")) {
            return this.m_event.getMaxRegistrations();
        }
        if (fieldName.equals("numParticipants")) {
            return this.m_event.getParticipants().size();
        }
        if (fieldName.equals("numRegistrations")) {
            return this.m_event.getRegistered().size();
        }
        if (fieldName.equals("numParticipantsDesc")) {
            final StringBuilder sb = new StringBuilder().append(this.m_event.getParticipants().size());
            final byte maxPart = this.m_event.getMaxParticipants();
            if (maxPart >= 0) {
                sb.append("/").append(maxPart);
            }
            return sb.toString();
        }
        if (fieldName.equals("numRegistrationsDesc")) {
            final StringBuilder sb = new StringBuilder().append(this.m_event.getRegistered().size());
            final byte maxRegs = this.m_event.getMaxRegistrations();
            if (maxRegs >= 0) {
                sb.append("/").append(maxRegs);
            }
            return sb.toString();
        }
        if (fieldName.equals("startDateDesc")) {
            final GameDateConst date = this.m_event.getStartDate();
            if (date == null || date.isNull()) {
                return null;
            }
            return date.toDescriptionString();
        }
        else if (fieldName.equals("endDateDesc")) {
            final GameDateConst date = this.m_event.getEndDate();
            if (date == null || date.isNull()) {
                return null;
            }
            return date.toDescriptionString();
        }
        else {
            if (fieldName.equals("type")) {
                return this.m_event.getType();
            }
            if (fieldName.equals("iconUrl")) {
                return WakfuConfiguration.getInstance().getCalendarEventIcon(this.m_event.getType());
            }
            if (fieldName.equals("typeDescription")) {
                return WakfuTranslator.getInstance().getString("calendar.event.type." + this.m_event.getType());
            }
            if (fieldName.equals("selectedType")) {
                return this.m_selectedType;
            }
            if (fieldName.equals("types")) {
                return WakfuCalendarEventTypeFieldProvider.getTypes();
            }
            if (fieldName.equals("isRegistered")) {
                return this.m_event.getRegistered().contains(WakfuGameEntity.getInstance().getLocalPlayer().getId());
            }
            if (fieldName.equals("isValidated")) {
                return this.m_event.getParticipants().contains(WakfuGameEntity.getInstance().getLocalPlayer().getId());
            }
            if (fieldName.equals("registeredList")) {
                return this.convertToParticipantList(this.m_event.getRegistered());
            }
            if (fieldName.equals("validatedList")) {
                return this.convertToParticipantList(this.m_event.getParticipants());
            }
            if (fieldName.equals("canEditParticipantsLimit")) {
                return this.m_event.getOwnerId() == WakfuGameEntity.getInstance().getLocalPlayer().getInteractivityId() || (this.m_event.getType() == 3 && AdminRightHelper.checkRight(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightsEnum.MODIFY_CALENDAR));
            }
            return null;
        }
    }
    
    private ArrayList<CalendarEventParticipant> convertToParticipantList(final TLongObjectHashMap<String> map) {
        final ArrayList<CalendarEventParticipant> list = new ArrayList<CalendarEventParticipant>(map.size());
        final TLongObjectIterator<String> it = map.iterator();
        while (it.hasNext()) {
            it.advance();
            list.add(new CalendarEventParticipant(it.key(), it.value()));
        }
        return list;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
}
