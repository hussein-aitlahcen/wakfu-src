package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.concurrent.atomic.*;
import io.netty.buffer.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.google.common.base.*;
import com.ankamagames.wakfu.common.account.admin.*;

public class ClientDispatchAuthenticationResultMessage extends InputOnlyProxyMessage
{
    private byte m_resultCode;
    private boolean m_activateSteamLinkHint;
    private final AtomicReference<AccountInformation> m_information;
    
    public ClientDispatchAuthenticationResultMessage() {
        super();
        this.m_information = new AtomicReference<AccountInformation>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 1, false)) {
            return false;
        }
        final ByteBuf buf = Unpooled.wrappedBuffer(rawDatas);
        this.m_resultCode = buf.readByte();
        this.m_activateSteamLinkHint = buf.readBoolean();
        if (buf.readBoolean()) {
            this.m_information.set(AccountInformation.unSerialize(buf));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 1027;
    }
    
    public byte getResultCode() {
        return this.m_resultCode;
    }
    
    public boolean isActivateSteamLinkHint() {
        return this.m_activateSteamLinkHint;
    }
    
    public Community getCommunity() {
        return this.m_information.get().getCommunity();
    }
    
    public Optional<Admin> getAdmin() {
        return this.m_information.get().getAdminInformation();
    }
    
    private static class AccountInformation
    {
        private final Community m_community;
        private final Optional<Admin> m_adminInformation;
        
        AccountInformation(final Community community, final Optional<Admin> adminInformation) {
            super();
            this.m_community = community;
            this.m_adminInformation = adminInformation;
        }
        
        public Community getCommunity() {
            return this.m_community;
        }
        
        public Optional<Admin> getAdminInformation() {
            return this.m_adminInformation;
        }
        
        public static AccountInformation unSerialize(final ByteBuf buf) {
            final Community community = Community.getFromId(buf.readInt());
            Optional<Admin> admin;
            if (buf.readBoolean()) {
                final byte[] sAdmin = new byte[buf.readInt()];
                buf.readBytes(sAdmin);
                admin = (Optional<Admin>)Optional.of((Object)AdminUtils.unSerialize(sAdmin));
            }
            else {
                admin = (Optional<Admin>)Optional.absent();
            }
            return new AccountInformation(community, admin);
        }
        
        @Override
        public String toString() {
            return "AccountInformation{m_community=" + this.m_community + ", m_adminInformation=" + this.m_adminInformation + '}';
        }
    }
}
