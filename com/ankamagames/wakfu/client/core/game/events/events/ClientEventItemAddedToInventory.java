package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.external.*;

public class ClientEventItemAddedToInventory implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_itemReferenceId;
    private short m_itemTypeId;
    
    public ClientEventItemAddedToInventory(final AbstractReferenceItem content) {
        super();
        this.m_itemReferenceId = content.getId();
        this.m_itemTypeId = content.getItemType().getId();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_itemReferenceId), Integer.toString(this.m_itemTypeId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventItemAddedToInventory.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.ITEM_ADDED_TO_INVENTORY.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9 sur l'Dd de l'item", new Parameter[] { new Parameter("Id de l'item"), new Parameter("Id de la famille directe") }) });
    }
}
