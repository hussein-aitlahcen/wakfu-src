package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

public interface DataCopyFilter<T extends List.AdditionnalData>
{
    boolean hasToBeCopied(T p0);
}
