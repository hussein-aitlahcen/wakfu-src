package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;

public final class ProtectorFactory implements com.ankamagames.wakfu.common.game.protector.ProtectorFactory<Protector>
{
    @Override
    public Protector createProtector(final int id) {
        final Protector protector = new Protector(id);
        protector.addProtectorListener(TerritoriesView.INSTANCE);
        protector.addProtectorListener(ProtectorView.getInstance());
        return protector;
    }
}
