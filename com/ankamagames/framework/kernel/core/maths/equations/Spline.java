package com.ankamagames.framework.kernel.core.maths.equations;

import java.awt.geom.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class Spline implements Equation
{
    public static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    private static final AffineTransform transform;
    private static final Comparator<ControlPoint> COMPARATOR;
    private final ArrayList<ControlPoint> m_points;
    private final CubicCurve2D m_displayer;
    private final double[] coords;
    private String m_name;
    private double m_boundMinX;
    private double m_boundMinY;
    private double m_boundMaxX;
    private double m_boundMaxY;
    private double m_displayMinY;
    private double m_displayMaxY;
    
    public Spline(final String name, final double minX, final double maxX, final double minY, final double maxY) {
        super();
        this.m_points = new ArrayList<ControlPoint>();
        this.m_displayer = new CubicCurve2D.Double();
        this.coords = new double[6];
        this.m_name = name;
        this.setValueBounds(minX, maxX, minY, maxY);
        this.reset();
    }
    
    public Spline(final String name) {
        this(name, 0.0, 1.0, 0.0, 1.0);
    }
    
    public double getDisplayMinY() {
        return this.m_displayMinY;
    }
    
    public double getDisplayMaxY() {
        return this.m_displayMaxY;
    }
    
    public void setDisplayBound(final double minY, final double maxY) {
        this.m_displayMinY = minY;
        this.m_displayMaxY = maxY;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void addPoint(final ControlPoint pt) {
        this.m_points.add(pt);
    }
    
    public void setValueBounds(final double minX, final double maxX, final double minY, final double maxY) {
        this.m_boundMinX = minX;
        this.m_boundMaxX = maxX;
        this.m_boundMinY = minY;
        this.m_boundMaxY = maxY;
    }
    
    public double getBoundMinX() {
        return this.m_boundMinX;
    }
    
    public double getBoundMinY() {
        return this.m_boundMinY;
    }
    
    public double getBoundMaxX() {
        return this.m_boundMaxX;
    }
    
    public double getBoundMaxY() {
        return this.m_boundMaxY;
    }
    
    public boolean isInBounds(final double x, final double y) {
        return this.m_boundMinX <= x && x <= this.m_boundMaxX && this.m_boundMinY <= y && y <= this.m_boundMaxY;
    }
    
    void reset() {
        this.m_points.clear();
        ControlPoint p = new ControlPoint(this.m_boundMinX, this.m_boundMaxY);
        this.m_points.add(p);
        p = new ControlPoint(this.m_boundMaxX, this.m_boundMaxY);
        this.m_points.add(p);
        this.sort();
    }
    
    @Override
    public float compute(final float variable) {
        return (float)this.getValueClamped(variable);
    }
    
    public double getValue(final double variable) {
        if (variable < this.m_points.get(0).getX()) {
            return this.m_points.get(0).getY();
        }
        for (int i = 1; i < this.m_points.size(); ++i) {
            if (variable < this.m_points.get(i).getX()) {
                return this.getValue(this.m_points.get(i - 1), this.m_points.get(i), variable);
            }
        }
        return this.m_points.get(this.m_points.size() - 1).getY();
    }
    
    public double getValueClamped(final double variable) {
        final double value = this.getValue(variable);
        if (value < this.m_boundMinY) {
            return this.m_boundMinY;
        }
        if (value > this.m_boundMaxY) {
            return this.m_boundMaxY;
        }
        return value;
    }
    
    public PathIterator getPathIterator(final ControlPoint pt1, final ControlPoint pt2) {
        this.m_displayer.setCurve(pt1.getX(), pt1.getY(), pt1.getOutX(), pt1.getOutY(), pt2.getInX(), pt2.getInY(), pt2.getX(), pt2.getY());
        return this.m_displayer.getPathIterator(Spline.transform, 0.01);
    }
    
    private double getValue(final ControlPoint pt1, final ControlPoint pt2, final double variable) {
        final PathIterator pathIterator = this.getPathIterator(pt1, pt2);
        double x = pt1.getX();
        double y = pt1.getY();
        while (!pathIterator.isDone()) {
            pathIterator.currentSegment(this.coords);
            pathIterator.next();
            if (variable < this.coords[0]) {
                final double amount = (variable - x) / (this.coords[0] - x);
                return MathHelper.lerp(y, this.coords[1], amount);
            }
            x = this.coords[0];
            y = this.coords[1];
        }
        final double amount = (variable - x) / (pt2.getX() - x);
        return MathHelper.lerp(y, pt2.getY(), amount);
    }
    
    public boolean removePoint(final ControlPoint point) {
        if (this.m_points.size() == 2) {
            return false;
        }
        for (int i = this.m_points.size() - 1; i >= 0; --i) {
            if (this.m_points.get(i) == point) {
                if (i == this.m_points.size() - 1) {
                    this.m_points.get(i - 1).setX(1.0);
                }
                if (i == 0) {
                    this.m_points.get(1).setX(0.0);
                }
                this.m_points.remove(i);
                break;
            }
        }
        return true;
    }
    
    public int getIndex(final ControlPoint pt) {
        for (int i = 0; i < this.m_points.size(); ++i) {
            if (this.m_points.get(i) == pt) {
                return i;
            }
        }
        return -1;
    }
    
    public void sort() {
        Collections.sort(this.m_points, Spline.COMPARATOR);
    }
    
    public ControlPoint get(final int i) {
        return this.m_points.get(i);
    }
    
    public int size() {
        return this.m_points.size();
    }
    
    public void clear() {
        this.m_points.clear();
    }
    
    public Spline clone() {
        final Spline spline = new Spline("");
        spline.setFrom(this);
        return spline;
    }
    
    public void setFrom(final Spline spline) {
        this.m_name = spline.m_name;
        this.clear();
        this.m_boundMinX = spline.m_boundMinX;
        this.m_boundMaxX = spline.m_boundMaxX;
        this.m_boundMinY = spline.m_boundMinY;
        this.m_boundMaxY = spline.m_boundMaxY;
        for (int i = 0; i < spline.size(); ++i) {
            this.addPoint(spline.get(i).clone());
        }
    }
    
    public void scale(final float minX, final float maxX) {
        if (minX == this.m_boundMinX && maxX == this.m_boundMaxX) {
            return;
        }
        final double a = (maxX - minX) / (this.m_boundMaxX - this.m_boundMinX);
        final double b = minX - this.m_boundMinX * a;
        for (int i = 0; i < this.size(); ++i) {
            final ControlPoint pt = this.get(i);
            pt.setLocation(pt.getX() * a + b, pt.getY());
            if (pt.hasInTangent()) {
                pt.setInTangent(pt.getInX() * a + b, pt.getInY());
            }
            if (pt.hasOutTangent()) {
                pt.setOutTangent(pt.getOutX() * a + b, pt.getOutY());
            }
        }
        this.m_boundMinX = minX;
        this.m_boundMaxX = maxX;
    }
    
    public void copyScaled(final Spline spline) {
        if (spline == this) {
            return;
        }
        this.clear();
        final double maxY = (this.m_boundMaxY == Double.POSITIVE_INFINITY) ? 1.0 : this.m_boundMaxY;
        final double minY = (this.m_boundMinY == Double.POSITIVE_INFINITY) ? 1.0 : this.m_boundMinY;
        final double dX = spline.m_boundMaxX - spline.m_boundMinX;
        final double scaleX = (this.m_boundMaxX - this.m_boundMinX) / dX;
        final double dY = spline.m_boundMaxY - spline.m_boundMinY;
        final double scaleY = (maxY - this.m_boundMinY) / dY;
        final double offsetX = this.m_boundMinX - spline.m_boundMinX * scaleX;
        final double offsetY = this.m_boundMinY - spline.m_boundMinY * scaleY;
        for (int i = 0; i < spline.size(); ++i) {
            final ControlPoint pt = spline.get(i).clone();
            pt.setLocation(pt.getX() * scaleX + offsetX, pt.getY() * scaleY + offsetY);
            if (pt.hasInTangent()) {
                pt.setInTangent(pt.getInX() * scaleX + offsetX, pt.getInY() * scaleY + offsetY);
            }
            if (pt.hasOutTangent()) {
                pt.setOutTangent(pt.getOutX() * scaleX + offsetX, pt.getOutY() * scaleY + offsetY);
            }
            this.addPoint(pt);
        }
    }
    
    public boolean equals(final Spline s) {
        if (s.size() != this.size()) {
            return false;
        }
        if (this.m_boundMinX != s.m_boundMinX || this.m_boundMaxX != s.m_boundMaxX) {
            return false;
        }
        if (this.m_boundMinY != s.m_boundMinY || this.m_boundMaxY != s.m_boundMaxY) {
            return false;
        }
        for (int i = 0; i < this.size(); ++i) {
            if (!this.get(i).equals(s.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isConstant() {
        for (int i = 0; i < this.m_points.size() - 1; ++i) {
            final ControlPoint pt = this.m_points.get(i);
            final double y = pt.getY();
            if (y != this.m_points.get(i + 1).getY()) {
                return false;
            }
            if (pt.hasInTangent() && pt.getInY() != y) {
                return false;
            }
            if (pt.hasOutTangent() && pt.getOutY() != y) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isIdentity() {
        for (int i = 0; i < this.m_points.size(); ++i) {
            final ControlPoint pt = this.m_points.get(i);
            if (pt.getX() != pt.getY()) {
                return false;
            }
            if (pt.hasInTangent()) {
                return false;
            }
            if (pt.hasOutTangent()) {
                return false;
            }
        }
        return true;
    }
    
    public double[] getMinMaxY() {
        final double[] result = { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY };
        for (int i = 0; i < this.m_points.size() - 1; ++i) {
            final ControlPoint pt1 = this.m_points.get(i);
            final ControlPoint pt2 = this.m_points.get(i + 1);
            final PathIterator pathIterator = this.getPathIterator(pt1, pt2);
            while (!pathIterator.isDone()) {
                pathIterator.currentSegment(this.coords);
                pathIterator.next();
                if (this.coords[1] < result[0]) {
                    result[0] = this.coords[1];
                }
                if (this.coords[1] > result[1]) {
                    result[1] = this.coords[1];
                }
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        return SplineParser.toString(this);
    }
    
    static {
        transform = new AffineTransform();
        COMPARATOR = new Comparator<ControlPoint>() {
            @Override
            public int compare(final ControlPoint o1, final ControlPoint o2) {
                if (o1.getX() < o2.getX()) {
                    return -1;
                }
                return (o1.getX() != o2.getX()) ? 1 : 0;
            }
        };
    }
}
