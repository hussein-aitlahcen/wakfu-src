package com.ankamagames.wakfu.common.account;

import com.ankamagames.baseImpl.common.clientAndServer.account.*;

public interface WakfuAccountInformationHolder extends AccountInformationHolder
{
    WakfuAccountInformationHandler getAccountInformationHandler();
    
    short getInstanceId();
}
