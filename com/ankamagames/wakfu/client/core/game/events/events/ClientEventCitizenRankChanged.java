package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.framework.external.*;

public class ClientEventCitizenRankChanged implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final CitizenRank m_newRank;
    private final int m_newPoints;
    
    public ClientEventCitizenRankChanged(final CitizenRank newRank, final int newPoints) {
        super();
        this.m_newRank = newRank;
        this.m_newPoints = newPoints;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { this.m_newRank.getTranslationKey(), String.valueOf(this.m_newPoints) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventCitizenRankChanged.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.CITIZEN_RANK_CHANGED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("nom du rang de criminalit\u00e9 (cl\u00e9 de trad)"), new Parameter("Points de criminalit\u00e9") }) });
    }
}
