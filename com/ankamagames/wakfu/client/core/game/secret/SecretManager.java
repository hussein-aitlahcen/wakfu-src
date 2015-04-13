package com.ankamagames.wakfu.client.core.game.secret;

import gnu.trove.*;

public class SecretManager
{
    public static final SecretManager INSTANCE;
    private final TIntObjectHashMap<SecretData> m_secrets;
    
    private SecretManager() {
        super();
        this.m_secrets = new TIntObjectHashMap<SecretData>();
    }
    
    public void registerSecret(final SecretData secret) {
        this.m_secrets.put(secret.getId(), secret);
    }
    
    public SecretData getSecret(final int id) {
        return this.m_secrets.get(id);
    }
    
    public boolean forEachSecret(final TObjectProcedure<SecretData> procedure) {
        return this.m_secrets.forEachValue(procedure);
    }
    
    public int size() {
        return this.m_secrets.size();
    }
    
    @Override
    public String toString() {
        return "SecretManager{m_secrets=" + this.m_secrets + '}';
    }
    
    static {
        INSTANCE = new SecretManager();
    }
}
