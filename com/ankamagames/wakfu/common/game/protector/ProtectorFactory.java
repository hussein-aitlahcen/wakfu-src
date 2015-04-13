package com.ankamagames.wakfu.common.game.protector;

public interface ProtectorFactory<P extends ProtectorBase>
{
    P createProtector(int p0);
}
