package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FreeCompanionBreedIdMessageRunner implements MessageRunner<FreeCompanionBreedIdMessage>
{
    @Override
    public boolean run(final FreeCompanionBreedIdMessage msg) {
        FreeCompanionManager.INSTANCE.setFreeCompanionBreedId(msg.getFreeCompanionBreedId());
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 2078;
    }
}
