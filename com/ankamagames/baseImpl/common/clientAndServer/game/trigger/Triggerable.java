package com.ankamagames.baseImpl.common.clientAndServer.game.trigger;

import java.util.*;

public interface Triggerable<T>
{
    boolean trigger(BitSet p0, T p1, byte p2);
}
