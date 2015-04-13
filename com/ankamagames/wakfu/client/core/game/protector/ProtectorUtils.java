package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ProtectorUtils
{
    public static long m_lastTaxUpdateRequestDate;
    public static final int TAX_UPDATE_REQUEST_DELAY_IN_MS = 60000;
    
    public static void requestTaxUpdate() {
        final long currentTime = System.currentTimeMillis();
        if (ProtectorUtils.m_lastTaxUpdateRequestDate + 60000L < currentTime) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final boolean isInOwnDimensionalBag = localPlayer != null && localPlayer.getVisitingDimentionalBag() == localPlayer.getOwnedDimensionalBag();
            final Protector protector = isInOwnDimensionalBag ? ProtectorView.getInstance().getProtectorCacheForDimensionalBag() : ProtectorView.getInstance().getProtector();
            if (protector != null) {
                final ProtectorTaxUpdateRequestMessage msg = new ProtectorTaxUpdateRequestMessage();
                msg.setProtectorId(protector.getId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                ProtectorUtils.m_lastTaxUpdateRequestDate = currentTime;
            }
        }
    }
    
    static {
        ProtectorUtils.m_lastTaxUpdateRequestDate = 0L;
    }
}
