package com.ankamagames.wakfu.common.game.eventsCalendar;

public class WakfuCalendarErrorCode
{
    public static final byte NO_ERROR = 0;
    public static final byte IMPOSSIBLE_TO_ADD = 1;
    public static final byte IMPOSSIBLE_TO_REGISTER = 2;
    public static final byte IMPOSSIBLE_TO_VALIDATE_REGISTRATION = 3;
    public static final byte IMPOSSIBLE_TO_UNVALIDATE_REGISTRATION = 4;
    public static final byte INVITED_NOT_CONNECTED = 5;
    public static final byte INVITED_PENDING_INVITATION = 6;
    public static final byte EVENT_INVITATION_REFUSED = 7;
    public static final byte EVENT_INVITATION_ACCEPTED = 8;
    public static final byte IMPOSSIBLE_TO_REMOVE = 9;
    public static final byte IMPOSSIBLE_TO_SET_END_DATE = 10;
    public static final byte EVENT_DOESNT_EXIST = 11;
    public static final byte IMPOSSIBLE_TO_SET_END_DATE_BEFORE_START = 12;
    public static final byte EVENT_NOT_OWNER = 13;
    public static final byte CHARACTER_DATA_DOESNT_EXIST = 14;
    public static final byte IMPOSSIBLE_TO_SET_UNDEFINED_START_DATE = 15;
    public static final byte INVITED_ALREADY_IN_EVENT = 16;
    public static final byte TOO_MUCH_PARTICIPANTS = 17;
    public static final byte TOO_MUCH_REGISTRATIONS = 18;
    public static final byte EVENT_ALREADY_EXISTS = 19;
    public static final byte OWNER_DOESNT_HAVE_GUILD = 20;
    public static final byte OWNER_DOESNT_HAVE_NATION = 21;
    public static final byte TOO_MANY_EVENTS_FOR_OWNER = 22;
    public static final byte OWNER_DOESNT_HAVE_ADMIN_RIGHTS = 23;
    public static final byte UNKNOWN_EVENT_TYPE = 24;
    public static final byte EXCEPTION_RAISED = 25;
    public static final byte CANT_MODIFY_EVENT = 26;
    public static final byte START_DATE_BEFORE_TODAY = 27;
    public static final byte CHARACTER_ALREADY_REGISTERED = 28;
    public static final byte EVENT_EXPIRED = 29;
    public static final byte CHARACTER_NOT_REGISTERED = 30;
    public static final byte CHARACTER_NAME_NULL = 31;
    public static final byte CHARACTER_NOT_PARTICIPANT = 32;
    public static final byte TOO_MUCH_EVENTS_IN_CALENDAR = 33;
    
    public static String log(final String action, final byte error) {
        final StringBuilder res = new StringBuilder();
        res.append("[EVENTS_CALENDAR] ");
        res.append(action).append(" : ");
        switch (error) {
            case 0: {
                res.append("Aucune erreur");
                break;
            }
            case 1: {
                res.append("Impossible a ajouter");
                break;
            }
            case 2: {
                res.append("Impossible de s'inscrire");
                break;
            }
            case 9: {
                res.append("Impossible de retirer l'evenement");
                break;
            }
            case 10: {
                res.append("Impossible de modifier la date de fin");
                break;
            }
            case 12: {
                res.append("Impossible de donner une date de fun precedant la date de debut");
                break;
            }
            case 15: {
                res.append("Date de debut obligatoire");
                break;
            }
            case 4: {
                res.append("Impossible d'annuler l'inscription");
                break;
            }
            case 3: {
                res.append("Impossible de valider l'inscription");
                break;
            }
            case 16: {
                res.append("Invite deha present pour l'evenement");
                break;
            }
            case 5: {
                res.append("Invit\u00e9 non connect\u00e9");
                break;
            }
            case 6: {
                res.append("L'invit\u00e9 a deja une demande en cours");
                break;
            }
            case 11: {
                res.append("L'evenement n'existe pas");
                break;
            }
            case 8: {
                res.append("Invitation acceptee");
                break;
            }
            case 7: {
                res.append("Incitation refusee");
                break;
            }
            case 13: {
                res.append("Pas le propri\u00e9taire de l'evenement");
                break;
            }
            case 14: {
                res.append("Les donnees du joueur n'existent pas sur le serveur");
                break;
            }
            case 17: {
                res.append("Trop de participants par rapport au max");
                break;
            }
            case 18: {
                res.append("Trop d'inscrits par rapport au max");
                break;
            }
            case 19: {
                res.append("Un evenement similaire existe deja");
                break;
            }
            case 25: {
                res.append("Exception levee");
                break;
            }
            case 24: {
                res.append("Type d'evenement inconnnu");
                break;
            }
            case 22: {
                res.append("Trop d'evenements pour le proprietaire");
                break;
            }
            case 23: {
                res.append("Le createur ne dispose pas des droits d'admin");
                break;
            }
            case 20: {
                res.append("Le createur n'a pas de guilde");
                break;
            }
            case 21: {
                res.append("Le createur n'a pas de nation");
                break;
            }
            case 26: {
                res.append("On n'a pas les droits pour modifier l'evenement");
                break;
            }
            case 27: {
                res.append("On n'a pas le droit d'ajouter un evenement a une date pass\u00e9e");
                break;
            }
            case 28: {
                res.append("Personnage deja inscrit a l'evenement");
                break;
            }
            case 29: {
                res.append("Evenement termin\u00e9");
                break;
            }
            case 30: {
                res.append("Personnage non inscrit");
                break;
            }
            case 31: {
                res.append("Nom du personnage null");
                break;
            }
            case 32: {
                res.append("Le personnage ne fait pas partie des participants");
                break;
            }
            case 33: {
                res.append("Trop d'evenements dans le calendrier concern\u00e9");
                break;
            }
            default: {
                res.append("Raison Inconnue ").append(error);
                break;
            }
        }
        return res.toString();
    }
}
