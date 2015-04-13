package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventSpellLearn implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int spellId;
    
    public ClientEventSpellLearn(final int craftId) {
        super();
        this.spellId = craftId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.spellId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventSpellLearn.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.SPELL_LEARN.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id du sort") }) });
    }
}
