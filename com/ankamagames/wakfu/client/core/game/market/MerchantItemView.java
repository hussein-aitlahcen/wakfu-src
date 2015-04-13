package com.ankamagames.wakfu.client.core.game.market;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.item.*;

public interface MerchantItemView extends FieldProvider
{
    Item getItem();
    
    long getId();
}
