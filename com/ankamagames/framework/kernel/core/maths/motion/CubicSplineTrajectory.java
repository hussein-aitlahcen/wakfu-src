package com.ankamagames.framework.kernel.core.maths.motion;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class CubicSplineTrajectory implements Trajectory, Spline
{
    private Vector3 m_initialPosition;
    private Vector3 m_initialVelocity;
    private Vector3 m_finalPosition;
    private Vector3 m_finalVelocity;
    private float m_a;
    private float m_b;
    private float m_c;
    private float m_d;
    private float m_e;
    private float m_f;
    private float m_g;
    private float m_h;
    private float m_i;
    private float m_j;
    private float m_k;
    private float m_l;
    private boolean m_vectorsChanged;
    private long m_initialTime;
    private long m_finalTime;
    
    public CubicSplineTrajectory() {
        super();
        this.m_initialPosition = new Vector3();
        this.m_initialVelocity = new Vector3();
        this.m_finalPosition = new Vector3();
        this.m_finalVelocity = new Vector3();
        this.m_vectorsChanged = true;
    }
    
    public CubicSplineTrajectory(final CubicSplineTrajectory trajectory) {
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
        this.m_vectorsChanged = true;
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
    public Vector3 getNextPositionInDifferentCell(final long startingTime) {
        return LinearSpline.getNextPositionInDifferentCell(this, startingTime);
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
    public void setInitialPosition(final Vector3 position) {
        this.m_initialPosition = position;
        this.m_vectorsChanged = true;
    }
    
    @Override
    public Vector3 getInitialVelocity() {
        return this.m_initialVelocity;
    }
    
    @Override
    public void setInitialVelocity(final Vector3 velocity) {
        this.m_initialVelocity = velocity;
        this.m_vectorsChanged = true;
    }
    
    @Override
    public Vector3 getFinalPosition() {
        return this.m_finalPosition;
    }
    
    @Override
    public void setFinalPosition(final Vector3 position) {
        this.m_finalPosition = position;
        this.m_vectorsChanged = true;
    }
    
    @Override
    public Vector3 getFinalVelocity() {
        return this.m_finalVelocity;
    }
    
    @Override
    public void setFinalVelocity(final Vector3 velocity) {
        this.m_finalVelocity = velocity;
        this.m_vectorsChanged = true;
    }
    
    private void computeFactors() {
        final float time = 1.0f;
        final float x0 = this.m_initialPosition.getX();
        final float y0 = this.m_initialPosition.getY();
        final float z0 = this.m_initialPosition.getZ();
        final float x = x0 + this.m_initialVelocity.getX() * 1.0f;
        final float y = y0 + this.m_initialVelocity.getY() * 1.0f;
        final float z = z0 + this.m_initialVelocity.getZ() * 1.0f;
        final float x2 = this.m_finalPosition.getX();
        final float y2 = this.m_finalPosition.getY();
        final float z2 = this.m_finalPosition.getZ();
        final float x3 = x2 - this.m_finalVelocity.getX() * 1.0f;
        final float y3 = y2 - this.m_finalVelocity.getY() * 1.0f;
        final float z3 = z2 - this.m_finalVelocity.getZ() * 1.0f;
        this.m_a = x2 - 3.0f * x3 + 3.0f * x - x0;
        this.m_b = 3.0f * x3 - 6.0f * x + 3.0f * x0;
        this.m_c = 3.0f * x - 3.0f * x0;
        this.m_d = x0;
        this.m_e = y2 - 3.0f * y3 + 3.0f * y - y0;
        this.m_f = 3.0f * y3 - 6.0f * y + 3.0f * y0;
        this.m_g = 3.0f * y - 3.0f * y0;
        this.m_h = y0;
        this.m_i = z2 - 3.0f * z3 + 3.0f * z - z0;
        this.m_j = 3.0f * z3 - 6.0f * z + 3.0f * z0;
        this.m_k = 3.0f * z - 3.0f * z0;
        this.m_l = z0;
        this.m_vectorsChanged = false;
    }
    
    @Override
    public Vector3 getPosition(long time) {
        if (this.m_vectorsChanged) {
            this.computeFactors();
        }
        if (time < this.m_initialTime) {
            time = this.m_initialTime;
        }
        else if (time > this.m_finalTime) {
            time = this.m_finalTime;
        }
        final float t = (time - this.m_initialTime) / (this.m_finalTime - this.m_initialTime);
        final float t2 = t * t;
        final float t3 = t2 * t;
        return new Vector3(this.m_a * t3 + this.m_b * t2 + this.m_c * t + this.m_d, this.m_e * t3 + this.m_f * t2 + this.m_g * t + this.m_h, this.m_i * t3 + this.m_j * t2 + this.m_k * t + this.m_l);
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
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
        buffer.append(" (duration:").append(this.m_finalTime - this.m_initialTime).append(") > from=").append(this.m_initialPosition).append(", to=").append(this.m_finalPosition).append(", initVel=").append(this.m_initialVelocity).append(", finalVel=").append(this.m_finalVelocity).append(".");
        return buffer.toString();
    }
}
