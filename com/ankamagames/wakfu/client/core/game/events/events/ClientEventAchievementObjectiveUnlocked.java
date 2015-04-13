package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventAchievementObjectiveUnlocked implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_objectiveId;
    private final int m_achievementId;
    
    public ClientEventAchievementObjectiveUnlocked(final int objectiveId, final int achievementId) {
        super();
        this.m_objectiveId = objectiveId;
        this.m_achievementId = achievementId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_objectiveId), Integer.toString(this.m_achievementId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventAchievementObjectiveUnlocked.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.ACHIEVEMENT_OBJECTIVE_UNLOCKED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id de l'objectif d\u00e9bloqu\u00e9") }), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id de l'objectif d\u00e9bloqu\u00e9"), new Parameter("id de l'achievement") }) });
    }
}
