package com.ankamagames.baseImpl.common.clientAndServer.account;

public interface AccountPermissionContext<H extends AccountInformationHolder>
{
    boolean hasPermission(H... p0);
}
