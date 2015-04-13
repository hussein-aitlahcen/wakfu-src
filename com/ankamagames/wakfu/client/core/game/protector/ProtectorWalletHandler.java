package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.client.core.protector.wallet.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.item.*;

public class ProtectorWalletHandler extends com.ankamagames.wakfu.common.game.protector.ProtectorWalletHandler<Protector>
{
    private WalletHandlerView m_view;
    
    public ProtectorWalletHandler(final Protector protectorBase, final SimpleObjectFactory merchantFactory) {
        super(protectorBase, merchantFactory, null);
        this.setListener(this.getView(protectorBase));
    }
    
    public WalletHandlerView getView(final Protector protector) {
        if (this.m_view == null) {
            this.m_view = new WalletHandlerView(protector, this);
        }
        return this.m_view;
    }
}
