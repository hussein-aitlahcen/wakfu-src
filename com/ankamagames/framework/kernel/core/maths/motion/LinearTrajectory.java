package com.ankamagames.framework.kernel.core.maths.motion;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class LinearTrajectory implements Trajectory
{
    private Vector3 m_initialPosition;
    private Vector3 m_initialVelocity;
    private Vector3 m_finalPosition;
    private Vector3 m_finalVelocity;
    private long m_initialTime;
    private long m_finalTime;
    
    public LinearTrajectory() {
        super();
        this.m_initialPosition = new Vector3();
        this.m_initialVelocity = new Vector3();
        this.m_finalPosition = new Vector3();
        this.m_finalVelocity = new Vector3();
        final long n = 0L;
        this.m_finalTime = n;
        this.m_initialTime = n;
    }
    
    public LinearTrajectory(final LinearTrajectory trajectory) {
        super();
        this.m_initialPosition = new Vector3();
        this.m_initialVelocity = new Vector3();
        this.m_finalPosition = new Vector3();
        this.m_finalVelocity = new Vector3();
        this.m_initialPosition.set(trajectory.m_initialPosition.getX(), trajectory.m_initialPosition.getY(), trajectory.m_initialPosition.getZ());
        this.m_initialVelocity.set(trajectory.m_initialVelocity.getX(), trajectory.m_initialVelocity.getY(), trajectory.m_initialVelocity.getZ());
        this.m_finalPosition.set(trajectory.m_finalPosition.getX(), trajectory.m_finalPosition.getY(), trajectory.m_finalPosition.getZ());
        this.m_finalVelocity.set(trajectory.m_finalVelocity.getX(), trajectory.m_finalVelocity.getY(), trajectory.m_finalVelocity.getZ());
        this.m_initialTime = trajectory.getInitialTime();
        this.m_finalTime = trajectory.getFinalTime();
    }
    
    @Override
    public void setInitialTime(final long time) {
        this.m_initialTime = time;
    }
    
    @Override
    public long getInitialTime() {
        return this.m_initialTime;
    }
    
    @Override
    public void setFinalTime(final long time) {
        this.m_finalTime = time;
    }
    
    @Override
    public long getFinalTime() {
        return this.m_finalTime;
    }
    
    @Override
    public Vector3 getInitialPosition() {
        return this.m_initialPosition;
    }
    
    @Override
    public Vector3 getInitialVelocity() {
        return this.m_initialVelocity;
    }
    
    @Override
    public Vector3 getFinalPosition() {
        return this.m_finalPosition;
    }
    
    @Override
    public Vector3 getFinalVelocity() {
        return this.m_finalVelocity;
    }
    
    @Override
    public void setInitialPosition(final Vector3 position) {
        this.m_initialPosition.setCurrent(position);
    }
    
    @Override
    public void setInitialVelocity(final Vector3 velocity) {
        this.m_initialVelocity.setCurrent(velocity);
    }
    
    @Override
    public void setFinalPosition(final Vector3 position) {
        this.m_finalPosition.setCurrent(position);
    }
    
    @Override
    public void setFinalVelocity(final Vector3 velocity) {
        this.m_finalVelocity.setCurrent(velocity);
    }
    
    @Override
    public Vector3 getPosition(long time) {
        if (time < this.m_initialTime) {
            time = this.m_initialTime;
        }
        if (this.m_finalTime == this.m_initialTime || (this.m_finalVelocity.length() == 0.0f && time >= this.m_finalTime)) {
            return new Vector3(this.m_finalPosition);
        }
        final float t = (time - this.m_initialTime) / (this.m_finalTime - this.m_initialTime);
        return new Vector3(this.m_initialPosition.getX() + this.m_initialVelocity.getX() * t, this.m_initialPosition.getY() + this.m_initialVelocity.getY() * t, this.m_initialPosition.getZ() + this.m_initialVelocity.getZ() * t);
    }
    
    public double getTimeRatio(final long time) {
        return (time - this.m_initialTime) / (this.m_finalTime - this.m_initialTime);
    }
    
    @Override
    public long getDuration() {
        return this.m_finalTime - this.m_initialTime;
    }
    
    @Override
    public List<int[]> integerSteps(final int step) {
        int lastX = 0;
        int lastY = 0;
        final ArrayList<int[]> cells = new ArrayList<int[]>();
        int[] currentCell = null;
        for (long t = this.m_initialTime; t < this.m_finalTime; t += step) {
            final Vector3 position = this.getPosition(t);
            final int x = (int)position.getX();
            final int y = (int)position.getY();
            final int z = (int)position.getZ();
            if (t <= this.m_initialTime || x != lastX || y != lastY) {
                currentCell = new int[] { x, y, z };
                cells.add(currentCell);
                lastX = x;
                lastY = y;
            }
        }
        if (!cells.isEmpty() && currentCell != null && (currentCell[0] != this.m_finalPosition.getX() || currentCell[1] != this.m_finalPosition.getY() || currentCell[2] != this.m_finalPosition.getZ())) {
            currentCell = new int[] { (int)this.m_finalPosition.getX(), (int)this.m_finalPosition.getY(), (int)this.m_finalPosition.getZ() };
            cells.add(currentCell);
        }
        return cells;
    }
    
    public void delay(final long time) {
        this.m_initialTime += time;
        this.m_finalTime += time;
    }
    
    public List<LinearTrajectory> split(final double maxTrajectoryLength) {
        final double trajectoryLength = this.m_finalPosition.sub(this.m_initialPosition).length2D();
        final int numberOfSubTrajectories = (int)Math.ceil(trajectoryLength / maxTrajectoryLength);
        final List<LinearTrajectory> subTrajectories = new ArrayList<LinearTrajectory>(numberOfSubTrajectories);
        if (numberOfSubTrajectories == 0) {
            subTrajectories.add(this);
            return subTrajectories;
        }
        final double subTrajectoryLength = trajectoryLength / numberOfSubTrajectories;
        final long subTrajectoryDuration = this.getDuration() / numberOfSubTrajectories;
        final Vector3 directionalVector = this.m_finalPosition.sub(this.m_initialPosition);
        directionalVector.normalizeCurrent();
        Vector3 previousFinalPosition = this.m_initialPosition;
        long previousFinalTime = this.m_initialTime;
        for (int i = 0; i < numberOfSubTrajectories; ++i) {
            final LinearTrajectory subTrajectory = new LinearTrajectory();
            subTrajectory.setInitialPosition(previousFinalPosition);
            subTrajectory.setInitialTime(previousFinalTime);
            if (i != numberOfSubTrajectories - 1) {
                subTrajectory.setFinalVelocity(this.m_initialVelocity);
                final Vector3 finalPosition = previousFinalPosition.add(directionalVector.mul((float)subTrajectoryLength));
                subTrajectory.setFinalPosition(finalPosition);
                subTrajectory.setFinalTime(previousFinalTime + subTrajectoryDuration);
            }
            else {
                subTrajectory.setFinalTime(this.m_finalTime);
                subTrajectory.setFinalVelocity(this.m_finalVelocity);
                subTrajectory.setFinalPosition(this.m_finalPosition);
            }
            subTrajectory.setInitialVelocity(subTrajectory.getFinalPosition().sub(subTrajectory.getInitialPosition()));
            previousFinalTime += subTrajectoryDuration;
            previousFinalPosition = subTrajectory.getFinalPosition();
            subTrajectories.add(subTrajectory);
        }
        return subTrajectories;
    }
    
    public double length() {
        return this.m_finalPosition.sub(this.m_initialPosition).length2D();
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
        buffer.append(" (duration:").append(this.m_finalTime - this.m_initialTime).append(") > from=");
        buffer.append(this.m_initialPosition);
        buffer.append(", to=");
        buffer.append(this.m_finalPosition);
        buffer.append(", initVel=");
        buffer.append(this.m_initialVelocity);
        buffer.append(", finalVel=");
        buffer.append(this.m_finalVelocity);
        buffer.append(", initialTime=");
        buffer.append(this.m_initialTime);
        buffer.append(", finalTime=");
        buffer.append(this.m_finalTime);
        return buffer.toString();
    }
}
