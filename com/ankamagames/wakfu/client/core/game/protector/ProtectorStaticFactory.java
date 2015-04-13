package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;

public final class ProtectorStaticFactory implements ProtectorFactory<Protector>
{
    @Override
    public Protector createProtector(final int id) {
        final Protector protector = new Protector(id);
        return protector;
    }
}
