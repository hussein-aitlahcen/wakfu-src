package com.ankamagames.wakfu.common.game.protector;

import gnu.trove.*;

public class ProtectorEcosystemProtectionManager
{
    public static final ProtectorEcosystemProtectionManager INSTANCE;
    private final TIntObjectHashMap<ProtectorEcosystemProtectionDefinition> m_definitions;
    
    public ProtectorEcosystemProtectionManager() {
        super();
        this.m_definitions = new TIntObjectHashMap<ProtectorEcosystemProtectionDefinition>();
    }
    
    public void add(final int protectorId, final ProtectorEcosystemProtectionDefinition definition) {
        this.m_definitions.put(protectorId, definition);
    }
    
    public ProtectorEcosystemProtectionDefinition get(final int protectorId) {
        ProtectorEcosystemProtectionDefinition definition = this.m_definitions.get(protectorId);
        if (definition == null) {
            definition = new ProtectorEcosystemProtectionDefinition();
            this.add(protectorId, definition);
        }
        return definition;
    }
    
    static {
        INSTANCE = new ProtectorEcosystemProtectionManager();
    }
}
