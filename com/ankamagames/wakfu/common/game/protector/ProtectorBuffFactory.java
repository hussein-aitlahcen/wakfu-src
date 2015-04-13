package com.ankamagames.wakfu.common.game.protector;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;

public interface ProtectorBuffFactory<P extends ProtectorBuff>
{
    P createBuff(int p0, SimpleCriterion p1, byte p2, ArrayList<WakfuStandardEffect> p3);
}
