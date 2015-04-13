package com.ankamagames.wakfu.common.game.nation.data;

public enum NationSerializationType
{
    FOR_WORLD_TO_GLOBAL(new Part[] { Part.ID, Part.VOTE_DATE, Part.CANDIDATES_LIST_FULL, Part.LAWS, Part.DIPLOMACY, Part.ELECTION_HISTORY, Part.GOVERNMENT_FULL, Part.ECONOMY_BDD, Part.SURVEY, Part.FORBIDDEN_CANDIDATES }), 
    FOR_GLOBAL_TO_WORLD(new Part[] { Part.ID, Part.VOTE_DATE, Part.CANDIDATES_LIST_FULL, Part.LAWS, Part.DIPLOMACY, Part.ECONOMY_BDD, Part.SURVEY, Part.FORBIDDEN_CANDIDATES }), 
    ID_ONLY(new Part[] { Part.ID }), 
    SERVER_CANDIDATES(new Part[] { Part.ID, Part.CANDIDATES_LIST_FULL }), 
    LAWS(new Part[] { Part.ID, Part.LAWS }), 
    SURVEY(new Part[] { Part.ID, Part.SURVEY }), 
    DIPLOMACY(new Part[] { Part.ID, Part.DIPLOMACY }), 
    GOVERNMENT(new Part[] { Part.ID, Part.GOVERNMENT_FULL }), 
    ECONOMY(new Part[] { Part.ID, Part.ECONOMY }), 
    PROTECTORS(new Part[] { Part.ID, Part.PROTECTORS }), 
    TRAVELLING_NATION_CHANGE(new Part[] { Part.ID, Part.LAWS, Part.DIPLOMACY }), 
    FORBIDDEN_CANDIDATES(new Part[] { Part.FORBIDDEN_CANDIDATES }), 
    GLOBAL_TO_GAME_INITIALISATION(new Part[] { Part.ID, Part.VOTE_DATE, Part.CANDIDATES_LIST_FULL, Part.PROTECTOR_BUFFS, Part.JAIL, Part.LAWS, Part.DIPLOMACY, Part.GOVERNMENT_FULL, Part.PROTECTORS, Part.FORBIDDEN_CANDIDATES }), 
    GLOBAL_TO_GAME_VOTE_STATUS(new Part[] { Part.ID, Part.VOTE_DATE, Part.CANDIDATES_LIST_FULL, Part.FORBIDDEN_CANDIDATES }), 
    GLOBAL_TO_GAME_VOTE_UPDATE(new Part[] { Part.ID, Part.VOTE_UPDATE }), 
    GLOBAL_TO_GAME_PROTECTOR_BUFFS(new Part[] { Part.ID, Part.PROTECTOR_BUFFS, Part.PROTECTORS }), 
    GAME_TO_LOCAL_FOR_CHARACTER_NATION_INITIALIZATION(new Part[] { Part.ID, Part.PROTECTOR_BUFFS, Part.JAIL }), 
    GLOBAL_TO_LOCAL_FOR_CHARACTER_NATION_INITIALIZATION(new Part[] { Part.ID, Part.VOTE_DATE, Part.LAWS, Part.DIPLOMACY, Part.JAIL, Part.GOVERNOR_BOOK, Part.PROTECTORS });
    
    private final Part[] m_parts;
    
    private NationSerializationType(final Part[] parts) {
        this.m_parts = parts;
    }
    
    public Part[] getParts() {
        return this.m_parts;
    }
    
    public enum Part
    {
        ID, 
        VOTE_DATE, 
        CANDIDATES_LIST_FULL, 
        LAWS, 
        GOVERNOR_BOOK, 
        GOVERNMENT_FULL, 
        DIPLOMACY, 
        ELECTION_HISTORY, 
        ECONOMY, 
        ECONOMY_BDD, 
        PROTECTORS, 
        SURVEY, 
        PROTECTOR_BUFFS, 
        VOTE_UPDATE, 
        JAIL, 
        FORBIDDEN_CANDIDATES;
    }
}
