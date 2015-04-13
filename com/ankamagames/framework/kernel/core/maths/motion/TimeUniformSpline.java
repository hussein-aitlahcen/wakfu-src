package com.ankamagames.framework.kernel.core.maths.motion;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class TimeUniformSpline implements Spline
{
    protected static final float[][] HERMITE_MATRIX;
    protected final ArrayList<TimeUniformSplineNode> m_nodes;
    protected float m_totalPathLength;
    protected long m_totalPathDuration;
    protected long m_startingTime;
    private static final Vector3 m_startingVelocityHelper;
    private static final Vector3 m_endVelocityHelper;
    
    public TimeUniformSpline(final List<LinearTrajectory> trajectories, final long startingTime) {
        super();
        this.m_startingTime = startingTime;
        this.m_totalPathLength = 0.0f;
        this.m_totalPathDuration = 0L;
        this.m_nodes = new ArrayList<TimeUniformSplineNode>();
        if (trajectories.size() == 1) {
            final LinearTrajectory trajectory = trajectories.get(0);
            final List<LinearTrajectory> subTrajectories = trajectory.split(trajectory.length() / 2.0);
            trajectories.clear();
            trajectories.addAll(subTrajectories);
        }
        TimeUniformSplineNode previousNode = null;
        for (int i = 0; i < trajectories.size(); ++i) {
            final Trajectory trajectory2 = trajectories.get(i);
            final TimeUniformSplineNode node = new TimeUniformSplineNode();
            node.setPosition(trajectory2.getInitialPosition());
            if (i != 0) {
                previousNode.setLength(previousNode.getPosition().sub(node.getPosition()).length2D());
                this.m_totalPathLength += previousNode.getLength();
            }
            this.m_totalPathDuration += trajectory2.getDuration();
            this.m_nodes.add(node);
            previousNode = node;
            if (i == trajectories.size() - 1) {
                final TimeUniformSplineNode lastNode = new TimeUniformSplineNode();
                lastNode.setPosition(trajectory2.getFinalPosition());
                previousNode.setLength(previousNode.getPosition().sub(lastNode.getPosition()).length2D());
                this.m_totalPathLength += previousNode.getLength();
                this.m_nodes.add(lastNode);
            }
        }
        this.buildSpline();
    }
    
    protected void buildSpline() {
        for (int i = 1; i < this.m_nodes.size() - 1; ++i) {
            final TimeUniformSplineNode previousNode = this.m_nodes.get(i - 1);
            final TimeUniformSplineNode currentNode = this.m_nodes.get(i);
            final TimeUniformSplineNode nextNode = this.m_nodes.get(i + 1);
            final Vector3 departureVelocity = nextNode.getPosition().sub(currentNode.getPosition());
            departureVelocity.normalizeCurrent();
            final Vector3 arrivalVelocity = previousNode.getPosition().sub(currentNode.getPosition());
            arrivalVelocity.normalizeCurrent();
            final Vector3 velocity = departureVelocity.sub(arrivalVelocity);
            velocity.normalizeCurrent();
            currentNode.setVelocity(velocity);
        }
        final TimeUniformSplineNode firstNode = this.m_nodes.get(0);
        final TimeUniformSplineNode secondNode = this.m_nodes.get(1);
        final Vector3 tmp = secondNode.getPosition().sub(firstNode.getPosition());
        tmp.mulCurrent(1.0f / firstNode.getLength());
        tmp.mulCurrent(3.0);
        tmp.subCurrent(secondNode.getVelocity());
        tmp.mulCurrent(0.5);
        firstNode.setVelocity(tmp);
        final TimeUniformSplineNode lastNode = this.m_nodes.get(this.m_nodes.size() - 1);
        final TimeUniformSplineNode beforeLastNode = this.m_nodes.get(this.m_nodes.size() - 2);
        final Vector3 tmp2 = lastNode.getPosition().sub(beforeLastNode.getPosition());
        tmp2.mulCurrent(1.0f / beforeLastNode.getLength());
        tmp2.mulCurrent(3.0);
        tmp2.subCurrent(beforeLastNode.getVelocity());
        tmp2.mulCurrent(0.5);
        lastNode.setVelocity(tmp2);
    }
    
    @Override
    public Vector3 getPosition(final long time) {
        if (time >= this.m_startingTime + this.m_totalPathDuration) {
            return this.m_nodes.get(this.m_nodes.size() - 1).getPosition();
        }
        if (time < this.m_startingTime) {
            return this.m_nodes.get(0).getPosition();
        }
        final long deltaTime = time - this.m_startingTime;
        final float percentTime = deltaTime / this.m_totalPathDuration;
        float distance;
        float currentDistance;
        int i;
        for (distance = percentTime * this.m_totalPathLength, currentDistance = 0.0f, i = 0; i < this.m_nodes.size() - 1 && currentDistance + this.m_nodes.get(i).getLength() < distance; currentDistance += this.m_nodes.get(i).getLength(), ++i) {}
        float deltaDistance = distance - currentDistance;
        final TimeUniformSplineNode node = this.m_nodes.get(i);
        final TimeUniformSplineNode nextNode = this.m_nodes.get(i + 1);
        deltaDistance /= node.getLength();
        TimeUniformSpline.m_startingVelocityHelper.setCurrent(node.getVelocity());
        TimeUniformSpline.m_startingVelocityHelper.mulCurrent(node.getLength());
        TimeUniformSpline.m_endVelocityHelper.setCurrent(nextNode.getVelocity());
        TimeUniformSpline.m_endVelocityHelper.mulCurrent(node.getLength());
        return this.getPositionOnCubic(node.getPosition(), TimeUniformSpline.m_startingVelocityHelper, nextNode.getPosition(), TimeUniformSpline.m_endVelocityHelper, deltaDistance);
    }
    
    protected Vector3 getPositionOnCubic(final Vector3 startPosition, final Vector3 startVelocity, final Vector3 endPosition, final Vector3 endVelocity, final float time) {
        final float a = 2.0f * startPosition.getX() - 2.0f * endPosition.getX() + startVelocity.getX() + endVelocity.getX();
        final float b = 2.0f * startPosition.getY() - 2.0f * endPosition.getY() + startVelocity.getY() + endVelocity.getY();
        final float c = 2.0f * startPosition.getZ() - 2.0f * endPosition.getZ() + startVelocity.getZ() + endVelocity.getZ();
        final float d = -3.0f * startPosition.getX() + 3.0f * endPosition.getX() - 2.0f * startVelocity.getX() - endVelocity.getX();
        final float e = -3.0f * startPosition.getY() + 3.0f * endPosition.getY() - 2.0f * startVelocity.getY() - endVelocity.getY();
        final float f = -3.0f * startPosition.getZ() + 3.0f * endPosition.getZ() - 2.0f * startVelocity.getZ() - endVelocity.getZ();
        final float g = startVelocity.getX();
        final float h = startVelocity.getY();
        final float i = startVelocity.getZ();
        final float j = startPosition.getX();
        final float k = startPosition.getY();
        final float l = startPosition.getZ();
        float t2 = time * time;
        t2 *= time;
        return new Vector3(a * t2 + d * t2 + g * time + j, b * t2 + e * t2 + h * time + k, startPosition.getZ());
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TimeUniformSpline={");
        for (final TimeUniformSplineNode node : this.m_nodes) {
            sb.append("( position=").append(node.getPosition()).append(" velocity=").append(node.getVelocity()).append(", length=").append(node.getLength()).append(" ), ");
        }
        return sb.append("}").toString();
    }
    
    public ArrayList<TimeUniformSplineNode> getNodes() {
        return this.m_nodes;
    }
    
    public long getTotalPathDuration() {
        return this.m_totalPathDuration;
    }
    
    public double getTotalPathLength() {
        return this.m_totalPathLength;
    }
    
    @Override
    public Vector3 getNextPositionInDifferentCell(final long startingTime) {
        return LinearSpline.getNextPositionInDifferentCell(this, startingTime);
    }
    
    @Override
    public long getFinalTime() {
        return this.m_startingTime + this.m_totalPathDuration;
    }
    
    @Override
    public long getInitialTime() {
        return this.m_startingTime;
    }
    
    @Override
    public Vector3 getInitialPosition() {
        return this.m_nodes.get(0).getPosition();
    }
    
    @Override
    public Vector3 getFinalPosition() {
        return this.m_nodes.get(this.m_nodes.size() - 1).getPosition();
    }
    
    static {
        HERMITE_MATRIX = new float[][] { { 2.0f, -3.0f, 0.0f, 1.0f }, { 2.0f, 3.0f, 0.0f, 0.0f }, { 1.0f, -2.0f, 1.0f, 0.0f }, { 1.0f, -1.0f, 0.0f, 0.0f } };
        m_startingVelocityHelper = new Vector3();
        m_endVelocityHelper = new Vector3();
    }
    
    public static class TimeUniformSplineNode
    {
        protected Vector3 m_position;
        protected Vector3 m_velocity;
        protected float m_length;
        
        public TimeUniformSplineNode() {
            super();
            this.m_length = 0.0f;
        }
        
        public Vector3 getVelocity() {
            return this.m_velocity;
        }
        
        public void setVelocity(final Vector3 velocity) {
            this.m_velocity = velocity;
        }
        
        public Vector3 getPosition() {
            return this.m_position;
        }
        
        public void setPosition(final Vector3 position) {
            this.m_position = position;
        }
        
        public float getLength() {
            return this.m_length;
        }
        
        public void setLength(final float length) {
            this.m_length = length;
        }
        
        @Override
        public String toString() {
            return this.m_position.toString();
        }
    }
}
