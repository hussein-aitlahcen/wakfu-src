package com.ankamagames.wakfu.client.core.account;

import java.util.concurrent.atomic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.account.admin.*;
import com.google.common.base.*;

public class DispatchAccountInformation
{
    private static final AtomicReference<DispatchAccountInformation> INFO;
    private final Community m_community;
    private final Optional<Admin> m_admin;
    
    private DispatchAccountInformation(final Community community, final Optional<Admin> admin) {
        super();
        this.m_community = community;
        this.m_admin = admin;
    }
    
    public static void set(final Community community, final Optional<Admin> admin) {
        DispatchAccountInformation.INFO.set(new DispatchAccountInformation(community, admin));
    }
    
    public static Community getCommunity() {
        Preconditions.checkNotNull((Object)DispatchAccountInformation.INFO.get());
        return DispatchAccountInformation.INFO.get().m_community;
    }
    
    public static Optional<Admin> getAdmin() {
        Preconditions.checkNotNull((Object)DispatchAccountInformation.INFO.get());
        return DispatchAccountInformation.INFO.get().m_admin;
    }
    
    @Override
    public String toString() {
        return "DispatchAccountInformation{m_community=" + this.m_community + ", m_admin=" + this.m_admin + '}';
    }
    
    static {
        INFO = new AtomicReference<DispatchAccountInformation>();
    }
}
