package com.ankamagames.wakfu.common.account.admin;

import com.google.gson.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;

public class Right
{
    @SerializedName("server")
    private final int m_serverId;
    @SerializedName("right")
    private final AdminRightsGroup m_right;
    
    public Right(final int serverId, final AdminRightsGroup right) {
        super();
        this.m_serverId = serverId;
        this.m_right = right;
    }
    
    public int getServerId() {
        return this.m_serverId;
    }
    
    public AdminRightsGroup getRight() {
        return this.m_right;
    }
    
    @Override
    public String toString() {
        return "Rights{m_serverId=" + this.m_serverId + ", m_right=" + this.m_right + '}';
    }
}
