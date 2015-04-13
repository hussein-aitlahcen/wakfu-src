package com.ankamagames.framework.ai.targetfinder;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public interface TargetValidator<T extends Target>
{
    boolean isConditionActive(long p0);
    
    ObjectPair<TargetValidity, ArrayList<T>> getTargetValidity(Target p0, Target p1);
}
