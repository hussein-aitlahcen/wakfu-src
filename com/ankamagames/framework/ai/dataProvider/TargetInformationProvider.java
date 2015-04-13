package com.ankamagames.framework.ai.dataProvider;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public interface TargetInformationProvider<T extends Target>
{
    Iterator<T> getAllPossibleTargets();
    
    List<T> getPossibleTargetsAtPosition(Point3 p0);
    
    List<T> getPossibleTargetsAtPosition(int p0, int p1, int p2);
}
