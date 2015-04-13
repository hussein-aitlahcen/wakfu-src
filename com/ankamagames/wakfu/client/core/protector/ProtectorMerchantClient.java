package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.core.common.*;

public class ProtectorMerchantClient extends AbstractProtectorMerchantClient<Protector>
{
    public static final SimpleObjectFactory<ProtectorMerchantClient> FACTORY;
    
    static {
        FACTORY = new SimpleObjectFactory<ProtectorMerchantClient>() {
            @Override
            public ProtectorMerchantClient createNew() {
                return new ProtectorMerchantClient();
            }
        };
    }
}
