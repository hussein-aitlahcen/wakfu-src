package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.jetbrains.annotations.*;

public interface InventoryContentProvider<ContentType extends InventoryContent, RawType>
{
    @Nullable
    ContentType unSerializeContent(RawType p0);
}
