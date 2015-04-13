package com.ankamagames.wakfu.client.core.game.events.events;

import gnu.trove.*;
import com.ankamagames.framework.external.*;

public class ClientEventErrorMessageReceived implements ClientGameEvent
{
    private static TIntHashSet ALLOWED_RESULTS;
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_errorTypeId;
    
    public ClientEventErrorMessageReceived(final int errorTypeId) {
        super();
        this.m_errorTypeId = errorTypeId;
    }
    
    public static boolean allow(final int errorType) {
        return ClientEventErrorMessageReceived.ALLOWED_RESULTS.contains(errorType);
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_errorTypeId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventErrorMessageReceived.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.ERROR_MESSAGE_RECEIVED.getId();
    }
    
    static {
        ClientEventErrorMessageReceived.ALLOWED_RESULTS = new TIntHashSet(new int[] { 10, 35, 36 });
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id du type de message d'erreur") }) });
    }
}
