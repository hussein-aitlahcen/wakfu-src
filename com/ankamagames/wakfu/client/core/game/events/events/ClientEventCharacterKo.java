package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventCharacterKo implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final long m_fighterId;
    private final int m_figtherBreedId;
    private final long m_figtherGroupId;
    
    public ClientEventCharacterKo(final long fighterId, final int figtherBreedId, final long figtherGroupId) {
        super();
        this.m_fighterId = fighterId;
        this.m_figtherBreedId = figtherBreedId;
        this.m_figtherGroupId = figtherGroupId;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.CHARACTER_KO.getId();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_fighterId), String.valueOf(this.m_figtherBreedId), String.valueOf(this.m_figtherGroupId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventCharacterKo.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id du perso"), new Parameter("Id de breed"), new Parameter("Id du groupe") }) });
    }
}
