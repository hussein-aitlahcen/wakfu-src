package com.ankamagames.wakfu.common.game.protector;

import gnu.trove.*;

public enum ProtectorEcosystemAction
{
    PROTECT_MONSTER_FAMILY((byte)1), 
    PROTECT_RESOURCE_FAMILY((byte)2), 
    UNPROTECT_MONSTER_FAMILY((byte)3), 
    UNPROTECT_RESOURCE_FAMILY((byte)4), 
    REINTRODUCE_MONSTER_FAMILY((byte)5), 
    REINTRODUCE_RESOURCE_FAMILY((byte)6);
    
    private static final TByteObjectHashMap<ProtectorEcosystemAction> m_actionsById;
    private final byte m_id;
    
    private ProtectorEcosystemAction(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static ProtectorEcosystemAction getActionById(final byte actionId) {
        return ProtectorEcosystemAction.m_actionsById.get(actionId);
    }
    
    static {
        m_actionsById = new TByteObjectHashMap<ProtectorEcosystemAction>();
        for (final ProtectorEcosystemAction action : values()) {
            ProtectorEcosystemAction.m_actionsById.put(action.getId(), action);
        }
    }
}
