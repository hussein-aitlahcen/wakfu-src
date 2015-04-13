package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.framework.external.*;

public enum ProtectorEvents implements ExportableEnum
{
    PROTECTOR_ATTACKED((ProtectorEventFactory)new ProtectorEventFactory(1) {
        public ProtectorEvent createProtectorEvent() {
            return new ProtectorAttacked();
        }
    }, "Protecteur attaqu\u00e9", "Le protecteur est attaqu\u00e9 par une nation, il propose aux joueurs de le d\u00e9fendre"), 
    PROTECTOR_DEFENDED((ProtectorEventFactory)new ProtectorEventFactory(2) {
        public ProtectorEvent createProtectorEvent() {
            return new ProtectorDefended();
        }
    }, "Protecteur a d\u00e9fendu", "Le protecteur a vaincu ses assaillants (avec ou sans aide)"), 
    PROTECTOR_DEFEATED((ProtectorEventFactory)new ProtectorEventFactory(3) {
        public ProtectorEvent createProtectorEvent() {
            return new ProtectorDefeated();
        }
    }, "Protecteur vaincu", "Le protecteur a \u00e9t\u00e9 vaincu et a chang\u00e9 de nation"), 
    PROTECTOR_WELCOME((ProtectorEventFactory)new ProtectorEventFactory(4) {
        @Override
        protected ProtectorEvent createProtectorEvent() {
            return new ProtectorWelcome();
        }
    }, "Message de bienvenue", "Le joueur rencontre un protecteur"), 
    CHALLENGE_PROPOSAL((ProtectorEventFactory)new ProtectorEventFactory(5) {
        @Override
        protected ProtectorEvent createProtectorEvent() {
            return new ProtectorChallengeProposalEvent();
        }
    }, "Challenge propos\u00e9", "Le protecteur propose un challenge"), 
    DAY_WEATHER_UPDATE((ProtectorEventFactory)new ProtectorEventFactory(6) {
        @Override
        protected ProtectorEvent createProtectorEvent() {
            return new ProtectorDayWeatherUpdateEvent();
        }
    }, "Changement de pr\u00e9vision meteo", "Les pr\u00e9visions m\u00e9t\u00e9o de la zone ont chang\u00e9"), 
    PROTECTOR_SATISFACTION_CHANGED((ProtectorEventFactory)new ProtectorEventFactory(7) {
        @Override
        protected ProtectorEvent createProtectorEvent() {
            return new ProtectorSatisfactionChangedEvent();
        }
    }, "Changement de satisfaction", "La satisfaction du protecteur a chang\u00e9"), 
    CHAOS_STARTED((ProtectorEventFactory)new ProtectorEventFactory(8) {
        @Override
        protected ProtectorEvent createProtectorEvent() {
            return new ProtectorChaosStartedEvent();
        }
    }, "D\u00e9but de chaos", "Un chaos a \u00e9t\u00e9 d\u00e9clench\u00e9 dans un territoire"), 
    CHAOS_ENDED((ProtectorEventFactory)new ProtectorEventFactory(9) {
        @Override
        protected ProtectorEvent createProtectorEvent() {
            return new ProtectorChaosEndedEvent();
        }
    }, "Fin de chaos", "Un chaos a \u00e9t\u00e9 stopp\u00e9 dans un territoire");
    
    private final ProtectorEventFactory m_factory;
    private final String m_label;
    private final String m_comment;
    
    private ProtectorEvents(final ProtectorEventFactory factory, final String label, final String comment) {
        this.m_factory = factory;
        this.m_label = label;
        this.m_comment = comment;
    }
    
    public static ProtectorEvents getFromId(final int id) {
        for (final ProtectorEvents value : values()) {
            if (id == value.getId()) {
                return value;
            }
        }
        return null;
    }
    
    public int getId() {
        return this.m_factory.getEventId();
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_factory.getEventId());
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return this.m_comment;
    }
    
    public ProtectorEvent create() {
        return this.m_factory.getProtectorEvent();
    }
}
