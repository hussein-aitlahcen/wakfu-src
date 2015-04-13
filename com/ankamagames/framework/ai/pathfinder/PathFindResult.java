package com.ankamagames.framework.ai.pathfinder;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import com.ankamagames.framework.kernel.core.maths.motion.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public final class PathFindResult implements Iterable<int[]>
{
    public static final PathFindResult EMPTY;
    public static final int STEP_X = 0;
    public static final int STEP_Y = 1;
    public static final int STEP_Z = 2;
    private static final Logger m_logger;
    private final boolean m_pathFound;
    private final int[][] m_data;
    private static final Vector3 PREVIOUS_POINT;
    private static final Vector3 PREVIOUS_VECTOR;
    private static final Vector3 CURRENT_POINT;
    private static final Vector3 CURRENT_VECTOR;
    private static final Vector3 INITIAL_VELOCITY;
    
    private PathFindResult() {
        super();
        this.m_pathFound = false;
        this.m_data = null;
    }
    
    public PathFindResult(final int stepsCount) {
        super();
        this.m_data = new int[stepsCount][3];
        this.m_pathFound = true;
    }
    
    public PathFindResult(final int[][] path) {
        super();
        this.m_data = path;
        this.m_pathFound = true;
    }
    
    public PathFindResult(final int[] initialPosition, final List<int[]> remainingPath) {
        super();
        this.m_data = new int[1 + remainingPath.size()][3];
        this.m_data[0][0] = initialPosition[0];
        this.m_data[0][1] = initialPosition[1];
        this.m_data[0][2] = initialPosition[2];
        for (int i = 0, size = remainingPath.size(); i < size; ++i) {
            final int[] cell = remainingPath.get(i);
            assert cell.length == 3 : "Cellule de longueur invalide : " + cell.length;
            this.m_data[i + 1][0] = cell[0];
            this.m_data[i + 1][1] = cell[1];
            this.m_data[i + 1][2] = cell[2];
        }
        this.m_pathFound = true;
    }
    
    public PathFindResult(final List<int[]> path) {
        super();
        this.m_data = new int[path.size()][3];
        for (int i = 0, size = path.size(); i < size; ++i) {
            final int[] cell = path.get(i);
            assert cell.length == 3 : "Cellule de longueur invalide : " + cell.length;
            this.m_data[i][0] = cell[0];
            this.m_data[i][1] = cell[1];
            this.m_data[i][2] = cell[2];
        }
        this.m_pathFound = true;
    }
    
    public PathFindResult(final byte[] serialized) {
        super();
        final ByteBuffer buffer = ByteBuffer.wrap(serialized);
        if (buffer.remaining() > 0 && buffer.remaining() < 65536) {
            this.m_data = new int[buffer.remaining() / 10][3];
            int i = 0;
            while (buffer.remaining() >= 10) {
                this.m_data[i][0] = buffer.getInt();
                this.m_data[i][1] = buffer.getInt();
                this.m_data[i][2] = buffer.getShort();
                ++i;
            }
            this.m_pathFound = true;
        }
        else {
            this.m_data = null;
            this.m_pathFound = false;
            PathFindResult.m_logger.error((Object)("PathFindResult s\u00e9rialis\u00e9 de longueur louche : " + serialized.length + " @ " + ExceptionFormatter.currentStackTrace()));
        }
    }
    
    public void setStep(final int stepIndex, final int x, final int y, final short z) {
        this.m_data[stepIndex][0] = x;
        this.m_data[stepIndex][1] = y;
        this.m_data[stepIndex][2] = z;
    }
    
    public void setStep(final int stepIndex, final int[] step) {
        this.m_data[stepIndex] = step;
    }
    
    public int getPathLength() {
        if (!this.m_pathFound || this.m_data == null) {
            return 0;
        }
        return this.m_data.length;
    }
    
    public boolean isPathFound() {
        return this.m_pathFound;
    }
    
    public int[] getPathStep(final int stepIndex) {
        assert stepIndex >= 0 && stepIndex < this.m_data.length : "Trying to get a path step not within the bounds length = " + this.m_data.length + " stepIndex = " + stepIndex;
        if (stepIndex < this.m_data.length) {
            return this.m_data[stepIndex];
        }
        return null;
    }
    
    public int[] getFirstStep() {
        if (this.m_data != null && this.m_data.length != 0) {
            return this.m_data[0];
        }
        return null;
    }
    
    public int[] getLastStep() {
        if (this.m_data != null && this.m_data.length != 0) {
            return this.m_data[this.m_data.length - 1];
        }
        return null;
    }
    
    public Direction8 getDirectionToStep(final int i) {
        if (!this.m_pathFound || i < 0 || i >= this.getPathLength()) {
            return null;
        }
        final int dx = this.m_data[i][0] - this.m_data[i - 1][0];
        final int dy = this.m_data[i][1] - this.m_data[i - 1][1];
        return Direction8.getDirectionFromVector(dx, dy);
    }
    
    @Override
    public Iterator<int[]> iterator() {
        if (this.m_data == null) {
            return new EmptyIterator<int[]>();
        }
        return new ArrayIterator<int[]>(this.m_data, true);
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("{ ");
        if (this.m_pathFound) {
            for (final int[] pos : this.m_data) {
                buffer.append('[').append(pos[0]).append(';').append(pos[1]).append(';').append(pos[2]).append("] ");
            }
        }
        else {
            buffer.append("not found");
        }
        return buffer.append('}').toString();
    }
    
    public ArrayList<LinearTrajectory> toTrajectories(final long timeBetweenTwoCells, final boolean considerAltitudeChanges) {
        final ArrayList<LinearTrajectory> trajectories = new ArrayList<LinearTrajectory>();
        if (this.m_data.length < 2) {
            return null;
        }
        PathFindResult.PREVIOUS_POINT.set(this.m_data[0]);
        PathFindResult.PREVIOUS_VECTOR.set(this.m_data[1]);
        PathFindResult.PREVIOUS_VECTOR.subCurrent(PathFindResult.PREVIOUS_POINT);
        long currentTime = 0L;
        int i = 1;
        LinearTrajectory currentTrajectory = new LinearTrajectory();
        LinearTrajectory previousTrajectory = null;
        currentTrajectory.setInitialPosition(PathFindResult.PREVIOUS_POINT);
        currentTrajectory.setInitialTime(currentTime);
        do {
            PathFindResult.CURRENT_POINT.set(this.m_data[i]);
            PathFindResult.CURRENT_VECTOR.setCurrent(PathFindResult.CURRENT_POINT);
            PathFindResult.CURRENT_VECTOR.subCurrent(PathFindResult.PREVIOUS_POINT);
            if (PathFindResult.CURRENT_VECTOR.getX() != PathFindResult.PREVIOUS_VECTOR.getX() || PathFindResult.CURRENT_VECTOR.getY() != PathFindResult.PREVIOUS_VECTOR.getY() || (considerAltitudeChanges && PathFindResult.CURRENT_VECTOR.getZ() != PathFindResult.PREVIOUS_VECTOR.getZ())) {
                currentTrajectory.setFinalPosition(PathFindResult.PREVIOUS_POINT);
                currentTime += (long)currentTrajectory.getFinalPosition().sub(currentTrajectory.getInitialPosition()).length2D() * timeBetweenTwoCells;
                currentTrajectory.setFinalTime(currentTime);
                PathFindResult.INITIAL_VELOCITY.setCurrent(currentTrajectory.getFinalPosition());
                PathFindResult.INITIAL_VELOCITY.subCurrent(currentTrajectory.getInitialPosition());
                currentTrajectory.setInitialVelocity(PathFindResult.INITIAL_VELOCITY);
                if (previousTrajectory != null) {
                    previousTrajectory.setFinalVelocity(PathFindResult.PREVIOUS_VECTOR);
                }
                trajectories.add(currentTrajectory);
                PathFindResult.PREVIOUS_VECTOR.setCurrent(PathFindResult.CURRENT_VECTOR);
                previousTrajectory = currentTrajectory;
                currentTrajectory = new LinearTrajectory();
                currentTrajectory.setInitialPosition(PathFindResult.PREVIOUS_POINT);
                currentTrajectory.setInitialTime(currentTime);
            }
            PathFindResult.PREVIOUS_POINT.setCurrent(PathFindResult.CURRENT_POINT);
        } while (++i < this.m_data.length);
        currentTrajectory.setFinalPosition(PathFindResult.PREVIOUS_POINT);
        currentTime += (long)currentTrajectory.getFinalPosition().sub(currentTrajectory.getInitialPosition()).length2D() * timeBetweenTwoCells;
        currentTrajectory.setFinalTime(currentTime);
        currentTrajectory.setInitialVelocity(currentTrajectory.getFinalPosition().sub(currentTrajectory.getInitialPosition()));
        trajectories.add(currentTrajectory);
        return trajectories;
    }
    
    public boolean contains(final int[] position) {
        if (!this.isPathFound()) {
            return false;
        }
        for (final int[] cell : this) {
            if (cell[0] == position[0] && cell[1] == position[1] && cell[2] == position[2]) {
                return true;
            }
        }
        return false;
    }
    
    public PathFindResult subPath(final int startIndex, final int endIndex) {
        if (startIndex < 0 || startIndex >= endIndex || endIndex > this.getPathLength()) {
            throw new IllegalArgumentException("0 <= startIndex < endIndex <= getPathLength() non-respect\u00e9");
        }
        final PathFindResult subPath = new PathFindResult(endIndex - startIndex);
        for (int i = startIndex; i < endIndex; ++i) {
            subPath.setStep(i - startIndex, this.getPathStep(i));
        }
        return subPath;
    }
    
    public static ObjectPair<PathFindResult, Integer> fusionPaths(final int[] startingPosition, @NotNull final PathFindResult firstPath, @NotNull final PathFindResult secondPath) {
        final int[] junction = secondPath.getFirstStep();
        if (junction == null) {
            PathFindResult.m_logger.error((Object)("Le point de d\u00e9part du 2e chemin est null lors d'une fusion de chemin: firstPath=" + firstPath + ", secondPath=" + secondPath));
            return null;
        }
        int startIndex = -1;
        int junctionIndex = -1;
        for (int i = 0; i < firstPath.getPathLength(); ++i) {
            final int[] cell = firstPath.getPathStep(i);
            if (cell[0] == startingPosition[0] && cell[1] == startingPosition[1] && cell[2] == startingPosition[2] && startIndex == -1) {
                startIndex = i;
                if (junctionIndex != -1) {
                    break;
                }
            }
            if (cell[0] == junction[0] && cell[1] == junction[1] && cell[2] == junction[2] && junctionIndex == -1) {
                junctionIndex = i;
                if (startIndex != -1) {
                    break;
                }
            }
        }
        if (startIndex == -1) {
            PathFindResult.m_logger.error((Object)("La position de d\u00e9part " + startingPosition[0] + ':' + startingPosition[1] + ':' + startingPosition[2] + " n'a pas \u00e9t\u00e9 trouv\u00e9e sur le premier chemin. (path: " + firstPath + ')'));
            return null;
        }
        if (junctionIndex == -1) {
            PathFindResult.m_logger.error((Object)("Le point de jonction " + junction[0] + ':' + junction[1] + ':' + junction[2] + " n'a pas \u00e9t\u00e9 trouv\u00e9 sur le premier chemin. (path: " + firstPath + ')'));
            return null;
        }
        int j = 0;
        int backwardCells = 0;
        PathFindResult resultPath;
        if (startIndex <= junctionIndex) {
            resultPath = new PathFindResult(junctionIndex - startIndex + secondPath.getPathLength());
            for (int k = startIndex; k < junctionIndex; ++k) {
                resultPath.setStep(j++, firstPath.getPathStep(k));
            }
        }
        else {
            resultPath = new PathFindResult(startIndex - junctionIndex + secondPath.getPathLength());
            for (int k = startIndex; k > junctionIndex; --k) {
                resultPath.setStep(j++, firstPath.getPathStep(k));
            }
            backwardCells = startIndex - junctionIndex;
        }
        for (int k = 0; k < secondPath.getPathLength(); ++k) {
            resultPath.setStep(j++, secondPath.getPathStep(k));
        }
        return new ObjectPair<PathFindResult, Integer>(resultPath, backwardCells);
    }
    
    public byte[] serialize() {
        final int length = this.getPathLength();
        final ByteBuffer bf = ByteBuffer.allocate(length * 10);
        for (int i = 0; i < length; ++i) {
            final int[] step = this.getPathStep(i);
            bf.putInt(step[0]);
            bf.putInt(step[1]);
            bf.putShort((short)step[2]);
        }
        return bf.array();
    }
    
    public List<int[]> toCellsList() {
        return Arrays.asList(this.m_data);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof PathFindResult)) {
            return false;
        }
        final PathFindResult o = (PathFindResult)obj;
        if (o.m_pathFound != this.m_pathFound || o.m_data.length != this.m_data.length) {
            return false;
        }
        for (int i = 0, size = this.m_data.length; i < size; ++i) {
            final int[] cell = this.m_data[i];
            final int[] oCell = o.m_data[i];
            if (cell.length != oCell.length || cell[0] != oCell[0] || cell[1] != oCell[1] || cell[2] != oCell[2]) {
                return false;
            }
        }
        return true;
    }
    
    static {
        EMPTY = new PathFindResult();
        m_logger = Logger.getLogger((Class)PathFindResult.class);
        PREVIOUS_POINT = new Vector3();
        PREVIOUS_VECTOR = new Vector3();
        CURRENT_POINT = new Vector3();
        CURRENT_VECTOR = new Vector3();
        INITIAL_VELOCITY = new Vector3();
    }
}
