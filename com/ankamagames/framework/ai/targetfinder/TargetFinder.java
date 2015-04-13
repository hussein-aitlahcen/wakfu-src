package com.ankamagames.framework.ai.targetfinder;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class TargetFinder
{
    public static final TargetFinder m_instance;
    protected static final Logger m_logger;
    
    public static TargetFinder getInstance() {
        return TargetFinder.m_instance;
    }
    
    public <T extends Target> Iterable<T> getTargets(final Target applicant, final TargetInformationProvider<T> provider, final AreaOfEffect area, final int cellSearchX, final int cellSearchY, final short cellSearchAltitude) {
        if (area == null || provider == null) {
            return new EmptyIterable<T>();
        }
        int applicantX = 0;
        int applicantY = 0;
        short applicantZ = 0;
        if (applicant != null) {
            applicantX = applicant.getWorldCellX();
            applicantY = applicant.getWorldCellY();
            applicantZ = applicant.getWorldCellAltitude();
        }
        final Direction8 direction = (applicant == null) ? Direction8.NONE : applicant.getDirection();
        return area.getTargets(applicantX, applicantY, applicantZ, cellSearchX, cellSearchY, cellSearchAltitude, direction, provider.getAllPossibleTargets());
    }
    
    public <T extends Target, TV extends TargetValidator<T>> Iterable<T> getTargets(final Target applicant, final TargetInformationProvider<T> provider, final AreaOfEffect area, final int cellSearchX, final int cellSearchY, final short cellSearchAltitude, final TV filter) {
        if (filter == null) {
            return this.getTargets(applicant, provider, area, cellSearchX, cellSearchY, cellSearchAltitude);
        }
        if (area == null || provider == null) {
            return new EmptyIterable<T>();
        }
        final List<T> targets = new LinkedList<T>();
        final Iterator<T> it = provider.getAllPossibleTargets();
        while (it.hasNext()) {
            final T target = it.next();
            final ObjectPair<TargetValidity, ArrayList<T>> result = filter.getTargetValidity(target, applicant);
            switch (result.getFirst()) {
                case VALID: {
                    targets.add(target);
                    continue;
                }
                case VALID_IF_IN_AOE: {
                    int applicantX = 0;
                    int applicantY = 0;
                    short applicantZ = 0;
                    Direction8 dir = Direction8.NORTH_EAST;
                    if (applicant != null) {
                        applicantX = applicant.getWorldCellX();
                        applicantY = applicant.getWorldCellY();
                        applicantZ = applicant.getWorldCellAltitude();
                        dir = applicant.getDirection();
                    }
                    if (area.intersects(cellSearchX, cellSearchY, cellSearchAltitude, applicantX, applicantY, applicantZ, dir, target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude(), target.getPhysicalRadius())) {
                        targets.add(target);
                        continue;
                    }
                    continue;
                }
                case SUBTARGET_VALID_ONLY: {
                    for (final T subtarget : result.getSecond()) {
                        targets.add(subtarget);
                    }
                    continue;
                }
            }
        }
        return targets;
    }
    
    static {
        m_instance = new TargetFinder();
        m_logger = Logger.getLogger((Class)TargetFinder.class);
    }
}
