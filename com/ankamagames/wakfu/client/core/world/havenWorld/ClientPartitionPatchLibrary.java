package com.ankamagames.wakfu.client.core.world.havenWorld;

import java.io.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;

public class ClientPartitionPatchLibrary extends PartitionPatchLibrary<ClientPartitionPatch>
{
    public static final ClientPartitionPatchLibrary INSTANCE;
    
    @Override
    protected ClientPartitionPatch createPatch(final int id) throws IOException {
        return new ClientPartitionPatch(id);
    }
    
    static {
        INSTANCE = new ClientPartitionPatchLibrary();
    }
}
