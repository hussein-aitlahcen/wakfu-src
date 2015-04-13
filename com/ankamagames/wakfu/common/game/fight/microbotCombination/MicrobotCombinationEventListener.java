package com.ankamagames.wakfu.common.game.fight.microbotCombination;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface MicrobotCombinationEventListener
{
    void onCombinationsModification(MicrobotSet p0, List<MicrobotCombination> p1, List<MicrobotCombination> p2, List<Point3> p3, List<Point3> p4);
}
