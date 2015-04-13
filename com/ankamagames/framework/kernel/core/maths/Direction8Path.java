package com.ankamagames.framework.kernel.core.maths;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.pathfinder.*;
import java.nio.*;
import java.util.*;

public final class Direction8Path
{
    private static final Logger m_logger;
    public static final int MIN_ENCODED_SIZE = 11;
    private final Point3 m_startingPosition;
    private Point3 m_endingPosition;
    private final List<Step> m_steps;
    
    public static Direction8Path fromPathFindResult(final PathFindResult pathFind) {
        if (!pathFind.isPathFound() || pathFind.getPathLength() <= 1) {
            Direction8Path.m_logger.error((Object)"Impossible de convertir un PathFindResult sans r\u00e9sultat en Direction8Path");
            return null;
        }
        final Direction8Path dirPath = new Direction8Path(pathFind.getPathLength() - 1);
        dirPath.setStartingPosition(new Point3(pathFind.getFirstStep()));
        for (int i = 1; i < pathFind.getPathLength(); ++i) {
            final Direction8 dir = pathFind.getDirectionToStep(i);
            final int heightDiff = pathFind.getPathStep(i)[2] - pathFind.getPathStep(i - 1)[2];
            if (dir == null) {
                Direction8Path.m_logger.error((Object)"Impossible de convertir le d\u00e9placement en Direction8 : le PathFindResult n'est pas continu ?");
                return null;
            }
            dirPath.addStep(dir, heightDiff);
        }
        dirPath.setEndingPosition(new Point3(pathFind.getLastStep()));
        return dirPath;
    }
    
    public Direction8Path(final Point3 startingPosition, final List<Step> steps) {
        super();
        this.m_startingPosition = new Point3();
        this.m_endingPosition = null;
        this.m_steps = steps;
        if (steps == null || steps.size() == 0) {
            Direction8Path.m_logger.error((Object)"Liste de cellules vide");
            return;
        }
        this.setStartingPosition(startingPosition);
    }
    
    public static Direction8Path decodeFromBuffer(final ByteBuffer buffer) {
        if (buffer.remaining() < 11) {
            Direction8Path.m_logger.error((Object)("Impossible de d\u00e9coder un Direction8Path dans un buffer de " + buffer.remaining() + " < " + 11));
            return null;
        }
        buffer.mark();
        final int startX = buffer.getInt();
        final int startY = buffer.getInt();
        final short startZ = buffer.getShort();
        final int length = buffer.get() & 0xFF;
        final Direction8Path dirPath = new Direction8Path(length);
        final Point3 pos = new Point3(startX, startY, startZ);
        dirPath.setStartingPosition(pos);
        if (buffer.remaining() < dirPath.contentSize()) {
            Direction8Path.m_logger.error((Object)("La taille du buffer ne correspond pas : attendu=" + dirPath.contentSize() + " > courant=" + buffer.remaining()));
            buffer.reset();
            return null;
        }
        for (int i = 0; i < length; ++i) {
            final int data = buffer.get();
            final Direction8 dir = Direction8.getDirectionFromIndex(data >> 5 & 0x7);
            int heightDiff = data & 0x1F;
            if ((heightDiff & 0x10) != 0x0) {
                heightDiff |= 0xFFFFFFE0;
            }
            dirPath.addStep(dir, heightDiff);
        }
        dirPath.setEndingPosition(pos);
        return dirPath;
    }
    
    public static Direction8Path fusionPaths(final Point3 startingPosition, final Direction8Path firstPath, final Direction8Path secondPath) {
        final Point3 junction = secondPath.getStartingPosition();
        final Point3 pos = firstPath.getStartingPosition();
        int startIndex = -1;
        int junctionIndex = -1;
        if (pos.equalsIgnoringAltitude(startingPosition)) {
            startIndex = 0;
        }
        if (pos.equalsIgnoringAltitude(junction)) {
            junctionIndex = 0;
        }
        for (int i = 0; i < firstPath.steps(); ++i) {
            final Step step = firstPath.getStep(i);
            pos.shift(step.direction);
            pos.add(0, 0, pos.getZ() + step.heightDiff);
            if (startIndex == -1 && pos.equalsIgnoringAltitude(startingPosition)) {
                startIndex = i + 1;
                if (junctionIndex != -1) {
                    break;
                }
            }
            if (junctionIndex == -1 && pos.equalsIgnoringAltitude(junction)) {
                junctionIndex = i + 1;
                if (startIndex != -1) {
                    break;
                }
            }
        }
        if (startIndex != -1 && junctionIndex != -1) {
            final Direction8Path result = new Direction8Path(secondPath.steps() + firstPath.steps());
            result.setStartingPosition(startingPosition);
            if (junctionIndex >= startIndex) {
                for (int j = startIndex; j <= junctionIndex - 1; ++j) {
                    result.addStep(firstPath.getStep(j));
                }
            }
            else {
                for (int j = startIndex - 1; j >= junctionIndex; --j) {
                    final Step step2 = firstPath.getStep(j);
                    result.addStep(step2.direction.opposite(), -step2.heightDiff);
                }
            }
            result.m_steps.addAll(secondPath.m_steps);
            result.setEndingPosition(new Point3(secondPath.getEndingPosition()));
            return result;
        }
        return null;
    }
    
    public Point3 getStartingPosition() {
        return new Point3(this.m_startingPosition);
    }
    
    public Point3 getEndingPosition() {
        if (this.m_endingPosition == null) {
            final Point3 pos = this.getStartingPosition();
            for (final Step step : this.m_steps) {
                pos.shift(step.direction);
                pos.add(0, 0, step.heightDiff);
            }
            this.m_endingPosition = pos;
        }
        return new Point3(this.m_endingPosition);
    }
    
    public byte[] encode() {
        assert this.m_steps.size() <= 255 : "Impossible d'encoder un Direction8Path de plus de 255 steps";
        final ByteBuffer buffer = ByteBuffer.allocate(this.encodedSize());
        buffer.putInt(this.m_startingPosition.getX());
        buffer.putInt(this.m_startingPosition.getY());
        buffer.putShort(this.m_startingPosition.getZ());
        buffer.put((byte)this.m_steps.size());
        for (final Step step : this.m_steps) {
            int data = (step.direction.m_index & 0x7) << 5;
            data |= (step.heightDiff & 0x1F);
            buffer.put((byte)data);
        }
        return buffer.array();
    }
    
    public int encodedSize() {
        return 11 + this.contentSize();
    }
    
    @Override
    public String toString() {
        String s = "{ [" + this.m_startingPosition.getX() + ":" + this.m_startingPosition.getY() + ":" + this.m_startingPosition.getZ() + "] ";
        final Point3 pos = this.getStartingPosition();
        for (final Step step : this.m_steps) {
            pos.shift(step.direction);
            pos.add(0, 0, step.heightDiff);
            s = s + "[" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + "] ";
        }
        s += "}";
        return s;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj instanceof Direction8Path) {
            final Direction8Path path2 = (Direction8Path)obj;
            return path2.m_startingPosition.equals(this.m_startingPosition) && path2.m_steps.equals(this.m_steps);
        }
        return false;
    }
    
    public int steps() {
        return this.m_steps.size();
    }
    
    public Step getStep(final int index) {
        return this.m_steps.get(index);
    }
    
    public Direction8Path subPath(final int startIndex, final int endIndex) {
        if (startIndex < 0 || startIndex >= endIndex || endIndex > this.steps()) {
            throw new IllegalArgumentException("0 <= startIndex < endIndex <= size() non-respect\u00e9");
        }
        final Direction8Path subPath = new Direction8Path(endIndex - startIndex);
        final Point3 newStartPos = this.getStartingPosition();
        for (int i = 0; i < startIndex; ++i) {
            final Step step = this.m_steps.get(i);
            newStartPos.shift(step.direction);
            newStartPos.add(0, 0, step.heightDiff);
        }
        subPath.setStartingPosition(newStartPos);
        for (int i = startIndex; i < endIndex; ++i) {
            subPath.addStep(this.m_steps.get(i));
        }
        return subPath;
    }
    
    public boolean contains(final Point3 position) {
        final Point3 pos = this.getStartingPosition();
        if (pos.equalsIgnoringAltitude(position)) {
            return true;
        }
        for (int i = 0; i < this.steps(); ++i) {
            final Step step = this.m_steps.get(i);
            pos.shift(step.direction);
            pos.add(0, 0, step.heightDiff);
            if (pos.equalsIgnoringAltitude(position)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(final Direction8Path path) {
        final Point3 pos = this.getStartingPosition();
        final Point3 pathPos = path.getStartingPosition();
        int startI = -1;
        if (pos.equalsIgnoringAltitude(pathPos)) {
            startI = 0;
        }
        else {
            for (int i = 0; i < this.steps(); ++i) {
                final Step step = this.m_steps.get(i);
                pos.shift(step.direction);
                pos.add(0, 0, step.heightDiff);
                if (pos.equalsIgnoringAltitude(pathPos)) {
                    startI = i + 1;
                    break;
                }
            }
        }
        if (startI == -1) {
            return false;
        }
        if (this.steps() - startI < path.steps()) {
            return false;
        }
        for (int i = startI, j = 0; i < this.steps() && j < path.steps(); ++i, ++j) {
            if (this.getStep(i) != path.getStep(j)) {
                return false;
            }
        }
        return true;
    }
    
    private Direction8Path(final int length) {
        super();
        this.m_startingPosition = new Point3();
        this.m_endingPosition = null;
        this.m_steps = new ArrayList<Step>(length);
    }
    
    private void addStep(final Direction8 dir, final int heightDiff) {
        if (heightDiff < -16 || heightDiff > 15 || dir.m_index < 0 || dir.m_index > 7 || this.m_steps.size() > 255) {
            throw new IllegalArgumentException("step ou direction invalides");
        }
        this.m_steps.add(new Step(dir, heightDiff));
    }
    
    private void addStep(final Step step) {
        if (step.heightDiff < -16 || step.heightDiff > 15 || step.direction.m_index < 0 || step.direction.m_index > 7) {
            throw new IllegalArgumentException("step ou direction invalides");
        }
        this.addStep(step.direction, step.heightDiff);
    }
    
    private void setStartingPosition(final Point3 startingPosition) {
        this.m_startingPosition.set(startingPosition);
    }
    
    private void setEndingPosition(final Point3 endingPosition) {
        this.m_endingPosition = endingPosition;
    }
    
    private int contentSize() {
        return this.m_steps.size();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Direction8Path.class);
    }
    
    public static class Step
    {
        public Direction8 direction;
        public int heightDiff;
        
        public Step(final Direction8 direction, final int heightDiff) {
            super();
            this.direction = direction;
            this.heightDiff = heightDiff;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj == null || !(obj instanceof Step)) {
                return false;
            }
            final Step step = (Step)obj;
            return step.direction == this.direction && step.heightDiff == this.heightDiff;
        }
    }
}
