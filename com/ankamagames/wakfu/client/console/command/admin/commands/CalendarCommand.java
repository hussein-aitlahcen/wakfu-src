package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar.*;

@Documentation(commandName = "", commandParameters = "", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "", commandObsolete = true)
public class CalendarCommand extends ModerationCommand
{
    public static final int REGISTER = 1;
    public static final int CONSULT = 2;
    public static final int REMOVE_EVENT = 3;
    public static final int ADD_EVENT = 4;
    public static final int UNREGISTER = 5;
    public static final int VALIDATE_PARTICIPATION = 6;
    public static final int UNVALIDATE_PARTICIPATION = 7;
    public static final int SET_MAX = 8;
    public static final int INVIT = 9;
    public static final int UPDATE = 10;
    public static final int SET_END = 11;
    public static final int SET_DESC = 12;
    public static final int SET_TITLE = 13;
    public static final int SET_START = 14;
    public static final int CREATE_A_LOT_OF_EVENTS = 15;
    private int m_commandId;
    private String[] m_args;
    
    public CalendarCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 1: {
                return this.m_args.length == 1;
            }
            case 2: {
                return this.m_args.length == 0;
            }
            case 3: {
                return this.m_args.length == 1;
            }
            case 4: {
                return this.m_args.length == 7 || this.m_args.length == 9;
            }
            case 5: {
                return this.m_args.length == 1;
            }
            case 6: {
                return this.m_args.length == 2;
            }
            case 7: {
                return this.m_args.length == 2;
            }
            case 8: {
                return this.m_args.length == 3;
            }
            case 9: {
                return this.m_args.length == 2;
            }
            case 10: {
                return this.m_args.length == 1;
            }
            case 11: {
                return this.m_args.length == 4 || this.m_args.length == 6 || this.m_args.length == 2;
            }
            case 12: {
                return this.m_args.length == 2;
            }
            case 13: {
                return this.m_args.length == 2;
            }
            case 14: {
                return this.m_args.length == 4 || this.m_args.length == 6;
            }
            case 15: {
                return this.m_args.length == 1;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 1: {
                    this.register(networkEntity);
                    break;
                }
                case 5: {
                    this.unregister(networkEntity);
                    break;
                }
                case 2: {
                    this.consult();
                    break;
                }
                case 3: {
                    this.removeEvent(networkEntity);
                    break;
                }
                case 4: {
                    this.addEvent(networkEntity);
                    break;
                }
                case 6: {
                    this.validateParticipation(networkEntity);
                    break;
                }
                case 7: {
                    this.unvalidateParticipation(networkEntity);
                    break;
                }
                case 8: {
                    this.setMax(networkEntity);
                    break;
                }
                case 9: {
                    this.invit(networkEntity);
                    break;
                }
                case 10: {
                    this.update(networkEntity);
                    break;
                }
                case 11: {
                    this.setEnd(networkEntity);
                    break;
                }
                case 12: {
                    this.setDesc(networkEntity);
                    break;
                }
                case 13: {
                    this.setTitle(networkEntity);
                    break;
                }
                case 14: {
                    this.setDate(networkEntity);
                    break;
                }
                case 15: {
                    this.createALotOfEvents(networkEntity);
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de calendrier " + e);
        }
    }
    
    private void createALotOfEvents(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)87);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void setDate(final NetworkEntity networkEntity) {
        final long eventUid = Long.parseLong(this.m_args[0]);
        GameDate date = null;
        if (this.m_args.length == 6) {
            date = new GameDate(0, Integer.parseInt(this.m_args[5]), Integer.parseInt(this.m_args[4]), Integer.parseInt(this.m_args[1]), Integer.parseInt(this.m_args[2]), Integer.parseInt(this.m_args[3]));
        }
        else if (this.m_args.length == 4) {
            date = new GameDate(0, 0, 0, Integer.parseInt(this.m_args[1]), Integer.parseInt(this.m_args[2]), Integer.parseInt(this.m_args[3]));
        }
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(eventUid);
        if (event != null) {
            CharacterEventsCalendar.getInstance().changeEventStartDate(eventUid, date);
            final SetEventStartDateMessage request = new SetEventStartDateMessage();
            request.setDate(date);
            request.setEventUid(eventUid);
            networkEntity.sendMessage(request);
        }
        else {
            ConsoleManager.getInstance().err("Evenement " + eventUid + " inconnu");
        }
    }
    
    private void setDesc(final NetworkEntity networkEntity) {
        final long eventUid = Long.parseLong(this.m_args[0]);
        final String desc = this.m_args[1];
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(eventUid);
        if (event != null) {
            event.setDesc(desc);
            final SetEventDescMessage msg = new SetEventDescMessage();
            msg.setDesc(desc);
            msg.setEventUid(eventUid);
            networkEntity.sendMessage(msg);
        }
        else {
            ConsoleManager.getInstance().err("Evenement " + eventUid + " inconnu");
        }
    }
    
    private void setTitle(final NetworkEntity networkEntity) {
        final long eventUid = Long.parseLong(this.m_args[0]);
        final String title = this.m_args[1];
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(eventUid);
        if (event != null) {
            event.setTitle(title);
            final SetEventTitleMessage msg = new SetEventTitleMessage();
            msg.setTitle(title);
            msg.setEventUid(eventUid);
            networkEntity.sendMessage(msg);
        }
        else {
            ConsoleManager.getInstance().err("Evenement " + eventUid + " inconnu");
        }
    }
    
    private void setEnd(final NetworkEntity networkEntity) {
        final long eventUid = Long.parseLong(this.m_args[0]);
        GameDate date = null;
        if (this.m_args.length == 6) {
            date = new GameDate(0, Integer.parseInt(this.m_args[5]), Integer.parseInt(this.m_args[4]), Integer.parseInt(this.m_args[1]), Integer.parseInt(this.m_args[2]), Integer.parseInt(this.m_args[3]));
        }
        else if (this.m_args.length == 4) {
            date = new GameDate(0, 0, 0, Integer.parseInt(this.m_args[1]), Integer.parseInt(this.m_args[2]), Integer.parseInt(this.m_args[3]));
        }
        else if (this.m_args.length == 2 && this.m_args[1].equals("-1")) {
            date = null;
        }
        final WakfuCalendarEvent event = CharacterEventsCalendar.getInstance().getEventById(eventUid);
        if (event != null) {
            event.setEndDate(date);
            final SetEventEndDateMessage request = new SetEventEndDateMessage();
            request.setDate(date);
            request.setEventUid(eventUid);
            networkEntity.sendMessage(request);
        }
        else {
            ConsoleManager.getInstance().err("Evenement " + eventUid + " inconnu");
        }
    }
    
    private void update(final NetworkEntity networkEntity) {
        final byte type = Byte.parseByte(this.m_args[0]);
        switch (type) {
            case 0: {
                CharacterEventsCalendar.getInstance().clear();
                break;
            }
            default: {
                CharacterEventsCalendar.getInstance().removeCalendarEventType(type);
                break;
            }
        }
        final EventsCalendarInfosRequestMessage request = new EventsCalendarInfosRequestMessage();
        request.setCalendarType(type);
        networkEntity.sendMessage(request);
    }
    
    private void invit(final NetworkEntity networkEntity) {
        final EventParticipationCreatorRequestMessage msg = new EventParticipationCreatorRequestMessage();
        msg.setEventUid(Long.parseLong(this.m_args[0]));
        msg.setCharacterToInviteId(this.m_args[1]);
        networkEntity.sendMessage(msg);
    }
    
    private void setMax(final NetworkEntity networkEntity) {
        final SetEventMaxParticipantsOrRegistrationsMessage msg = new SetEventMaxParticipantsOrRegistrationsMessage();
        msg.setEventUid(Long.parseLong(this.m_args[0]));
        msg.setNewMax(Byte.parseByte(this.m_args[1]));
        msg.setType(Byte.parseByte(this.m_args[2]));
        networkEntity.sendMessage(msg);
    }
    
    private void unvalidateParticipation(final NetworkEntity networkEntity) {
        final UnvalidateParticipationMessage msg = new UnvalidateParticipationMessage();
        msg.setCharacterId(Long.parseLong(this.m_args[0]));
        msg.setEventUid(Long.parseLong(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private void validateParticipation(final NetworkEntity networkEntity) {
        final ValidateParticipationMessage msg = new ValidateParticipationMessage();
        msg.setCharacterId(Long.parseLong(this.m_args[0]));
        msg.setEventUid(Long.parseLong(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private void unregister(final NetworkEntity networkEntity) {
        final UnregisterToEventRequestMessage msg = new UnregisterToEventRequestMessage();
        msg.setEvent(Long.parseLong(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void addEvent(final NetworkEntity networkEntity) {
        final AddCalendarEventRequestMessage request = new AddCalendarEventRequestMessage();
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        WakfuCalendarEvent event;
        if (this.m_args.length == 7) {
            event = new WakfuCalendarEvent(this.m_args[0], this.m_args[1], character.getId(), Byte.parseByte(this.m_args[2]), Byte.parseByte(this.m_args[3]), Byte.parseByte(this.m_args[4]), Byte.parseByte(this.m_args[5]), Short.parseShort(this.m_args[6]));
        }
        else {
            event = new WakfuCalendarEvent(this.m_args[0], this.m_args[1], character.getId(), Byte.parseByte(this.m_args[2]), Byte.parseByte(this.m_args[3]), Byte.parseByte(this.m_args[4]), Byte.parseByte(this.m_args[5]), Short.parseShort(this.m_args[6]), Byte.parseByte(this.m_args[7]), Byte.parseByte(this.m_args[8]));
        }
        request.setEvent(event);
        event.addParticipant(character.getId(), character.getName());
        CharacterEventsCalendar.getInstance().addCalendarEvent(event);
        networkEntity.sendMessage(request);
    }
    
    private void removeEvent(final NetworkEntity networkEntity) {
        final RemoveCalendarEventRequestMessage request = new RemoveCalendarEventRequestMessage();
        request.setEventUid(Long.parseLong(this.m_args[0]));
        networkEntity.sendMessage(request);
    }
    
    private void consult() {
        CharacterEventsCalendar.getInstance().display();
    }
    
    private void register(final NetworkEntity networkEntity) {
        final RegisterToEventRequestMessage msg = new RegisterToEventRequestMessage();
        msg.setEvent(Long.parseLong(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
}
