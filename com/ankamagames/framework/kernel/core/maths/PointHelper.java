package com.ankamagames.framework.kernel.core.maths;

import java.util.*;
import org.jetbrains.annotations.*;

public class PointHelper
{
    @Nullable
    public static Point3 getClosestPointFrom(final Point3 source, final Collection<Point3> list) {
        if (list.isEmpty()) {
            return null;
        }
        int candidateDistance = Integer.MAX_VALUE;
        Point3 candidatePoint = null;
        for (final Point3 point : list) {
            final int distance = point.getDistance(source);
            if (distance < candidateDistance) {
                candidatePoint = point;
                candidateDistance = distance;
            }
        }
        return candidatePoint;
    }
}
