package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ProtectorBuffFactory implements com.ankamagames.wakfu.common.game.protector.ProtectorBuffFactory<ProtectorBuff>
{
    @Override
    public ProtectorBuff createBuff(final int id, final SimpleCriterion criterion, final byte origin, final ArrayList<WakfuStandardEffect> effects) {
        return new ProtectorBuff(id, criterion, origin, effects);
    }
}
