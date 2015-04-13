package com.ankamagames.wakfu.common.game.protector;

public interface ProtectorSearchCriterion<P extends ProtectorBase>
{
    boolean match(P p0);
}
