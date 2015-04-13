package com.ankamagames.wakfu.common.game.fight.microbotCombination;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;

public class MicrobotCombination implements Iterable<Point3>
{
    public static final Logger m_logger;
    private Segment m_segment;
    
    public MicrobotCombination(final AbstractFakeFighterEffectArea bot1, final AbstractFakeFighterEffectArea bot2) {
        super();
        this.m_segment = new Segment(bot1.getPosition(), bot2.getPosition());
    }
    
    public MicrobotCombination(final Point3 p1, final Point3 p2) {
        super();
        this.m_segment = new Segment(p1, p2);
    }
    
    public Point3 getFirstEnding() {
        return this.m_segment.getStart();
    }
    
    public Point3 getSecondEnding() {
        return this.m_segment.getEnd();
    }
    
    public boolean isOnXAxis() {
        return this.m_segment.isOnXAxis();
    }
    
    public boolean isOnYAxis() {
        return this.m_segment.isOnYAxis();
    }
    
    boolean expandWith(final AbstractFakeFighterEffectArea microbot, final int maxSpacebetweenBots) {
        return this.canCombineWith(microbot.getPosition(), maxSpacebetweenBots) && this.m_segment.expandTo(microbot.getPosition());
    }
    
    @Override
    public Iterator<Point3> iterator() {
        return this.m_segment.iterator();
    }
    
    public boolean canCombineWith(final Point3 microbotPos, final int maxSpaceBetweenBots) {
        switch (this.m_segment.getRelativePosition(microbotPos)) {
            case AFTER: {
                return this.m_segment.getEnd().getSpaceBetween(microbotPos) <= maxSpaceBetweenBots;
            }
            case BEFORE: {
                return this.m_segment.getStart().getSpaceBetween(microbotPos) <= maxSpaceBetweenBots;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean canPointsCombine(final Point3 microbot1Position, final Point3 microbot2Position, final int maxSpaceBetweenBots) {
        return microbot1Position.isAlignedWith(microbot2Position) && microbot1Position.getSpaceBetween(microbot2Position) <= maxSpaceBetweenBots;
    }
    
    @Override
    public String toString() {
        return "{MicrobotCombination from segment " + this.m_segment + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MicrobotCombination that = (MicrobotCombination)o;
        return this.m_segment.equals(that.m_segment);
    }
    
    @Override
    public int hashCode() {
        return (this.m_segment != null) ? this.m_segment.hashCode() : 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MicrobotCombination.class);
    }
}
