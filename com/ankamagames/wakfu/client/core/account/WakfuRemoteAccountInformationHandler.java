package com.ankamagames.wakfu.client.core.account;

import com.ankamagames.wakfu.common.account.*;

public class WakfuRemoteAccountInformationHandler extends WakfuAccountInformationHandler
{
    @Override
    public void setAdminRights(final int[] rights) {
        throw new UnsupportedOperationException("Il est interdit d'acc\u00e9der aux droits admin des characters remote");
    }
    
    @Override
    public boolean hasAdminRight(final short rightId) {
        throw new UnsupportedOperationException("Il est interdit d'acc\u00e9der aux droits admin des characters remote");
    }
    
    @Override
    public int[] getAdminRights() {
        throw new UnsupportedOperationException("Il est interdit d'acc\u00e9der aux droits admin des characters remote");
    }
}
