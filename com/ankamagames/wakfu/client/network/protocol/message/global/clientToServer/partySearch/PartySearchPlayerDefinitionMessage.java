package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public class PartySearchPlayerDefinitionMessage extends InputOnlyProxyMessage
{
    private final List<PartyPlayerDefinition> m_definitions;
    
    public PartySearchPlayerDefinitionMessage() {
        super();
        this.m_definitions = new ArrayList<PartyPlayerDefinition>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        for (byte i = 0, size = bb.get(); i < size; ++i) {
            final PartyPlayerDefinition def = new PartyPlayerDefinition(bb);
            final byte[] container = new byte[bb.get()];
            bb.get(container);
            def.setName(StringUtils.fromUTF8(container));
            this.m_definitions.add(def);
        }
        return true;
    }
    
    public List<PartyPlayerDefinition> getDefinitions() {
        return Collections.unmodifiableList((List<? extends PartyPlayerDefinition>)this.m_definitions);
    }
    
    @Override
    public int getId() {
        return 20418;
    }
}
