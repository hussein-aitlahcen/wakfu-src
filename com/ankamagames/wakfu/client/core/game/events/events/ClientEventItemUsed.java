package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.external.*;

public class ClientEventItemUsed implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final AbstractReferenceItem m_itemReference;
    
    public ClientEventItemUsed(final AbstractReferenceItem content) {
        super();
        this.m_itemReference = content;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_itemReference.getId()), Integer.toString(this.m_itemReference.getItemType().getId()) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventItemUsed.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.ITEM_USED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id de l'item"), new Parameter("Id de cat\u00e9gorie") }) });
    }
}
