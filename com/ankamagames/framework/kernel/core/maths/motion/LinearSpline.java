package com.ankamagames.framework.kernel.core.maths.motion;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class LinearSpline implements Spline
{
    protected List<LinearTrajectory> m_trajectories;
    protected long m_startingTime;
    protected long m_totalDuration;
    
    public LinearSpline(final List<LinearTrajectory> trajectories, final long startingTime) {
        super();
        this.m_trajectories = trajectories;
        this.m_startingTime = startingTime;
        this.m_totalDuration = 0L;
        for (final LinearTrajectory t : this.m_trajectories) {
            this.m_totalDuration += t.getDuration();
            t.delay(startingTime);
        }
    }
    
    @Override
    public Vector3 getPosition(final long time) {
        if (time <= this.m_startingTime) {
            return this.getInitialPosition();
        }
        if (time >= this.m_startingTime + this.m_totalDuration) {
            return this.getFinalPosition();
        }
        for (int i = 0; i < this.m_trajectories.size(); ++i) {
            final LinearTrajectory t = this.m_trajectories.get(i);
            if (time >= t.getInitialTime() && time < t.getFinalTime()) {
                final Vector3 position = t.getPosition(time);
                position.setZ(t.getInitialPosition().getZ());
                return position;
            }
        }
        return this.getFinalPosition();
    }
    
    @Override
    public Vector3 getNextPositionInDifferentCell(final long startingTime) {
        return getNextPositionInDifferentCell(this, startingTime);
    }
    
    @Override
    public Vector3 getInitialPosition() {
        if (this.m_trajectories == null || this.m_trajectories.size() <= 0) {
            return null;
        }
        return this.m_trajectories.get(0).getInitialPosition();
    }
    
    @Override
    public Vector3 getFinalPosition() {
        if (this.m_trajectories == null || this.m_trajectories.size() <= 0) {
            return null;
        }
        return this.m_trajectories.get(this.m_trajectories.size() - 1).getFinalPosition();
    }
    
    @Override
    public long getFinalTime() {
        return this.m_startingTime + this.m_totalDuration;
    }
    
    @Override
    public long getInitialTime() {
        return this.m_startingTime;
    }
    
    public static Vector3 getNextPositionInDifferentCell(final Spline spline, long startingTime) {
        final long DELTA = 75L;
        final Vector3 startingPosition = spline.getPosition(startingTime);
        final Vector3 startingCell = new Vector3(MathHelper.fastFloor(startingPosition.getX()), MathHelper.fastFloor(startingPosition.getY()), MathHelper.fastFloor(startingPosition.getZ()));
        final Vector3 currentCell = new Vector3(startingCell);
        Vector3 currentPosition = startingCell;
        for (long endTime = spline.getFinalTime(); startingTime < endTime && currentCell.equals(startingCell); startingTime += 75L) {
            currentPosition = spline.getPosition(startingTime + 75L);
            currentCell.set(MathHelper.fastFloor(currentPosition.getX()), MathHelper.fastFloor(currentPosition.getY()), MathHelper.fastFloor(currentPosition.getZ()));
        }
        return currentPosition;
    }
}
