package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

public interface EffectContainer<FX extends Effect> extends Iterable<FX>
{
    int getContainerType();
    
    long getEffectContainerId();
}
