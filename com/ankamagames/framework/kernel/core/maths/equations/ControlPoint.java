package com.ankamagames.framework.kernel.core.maths.equations;

public class ControlPoint
{
    public static double DELTA_X;
    public static double DELTA_Y;
    private final Point2D point;
    private Point2D inTangent;
    private Point2D outTangent;
    
    public ControlPoint() {
        super();
        this.point = new Point2D(0.0, 0.0);
    }
    
    public ControlPoint(final double x, final double y) {
        super();
        this.point = new Point2D(0.0, 0.0);
        this.point.x = x;
        this.point.y = y;
    }
    
    public void setX(final double x) {
        this.point.x = x;
    }
    
    public void setY(final double y) {
        this.point.y = y;
    }
    
    public ControlPoint setLocation(final double x, final double y) {
        this.point.x = x;
        this.point.y = y;
        return this;
    }
    
    public double getX() {
        return this.point.x;
    }
    
    public double getY() {
        return this.point.y;
    }
    
    public double getInX() {
        return (this.inTangent == null) ? this.point.x : this.inTangent.x;
    }
    
    public double getInY() {
        return (this.inTangent == null) ? this.point.y : this.inTangent.y;
    }
    
    public double getOutX() {
        return (this.outTangent == null) ? this.point.x : this.outTangent.x;
    }
    
    public double getOutY() {
        return (this.outTangent == null) ? this.point.y : this.outTangent.y;
    }
    
    public void removeInTangent() {
        this.inTangent = null;
    }
    
    public ControlPoint setInTangent(final double x, final double y) {
        if (Math.abs(this.getX() - x) < ControlPoint.DELTA_X && Math.abs(this.getY() - y) < ControlPoint.DELTA_Y) {
            this.inTangent = null;
        }
        else {
            this.inTangent = new Point2D(x, y);
        }
        return this;
    }
    
    public boolean hasInTangent() {
        return this.inTangent != null;
    }
    
    public boolean hasOutTangent() {
        return this.outTangent != null;
    }
    
    public void removeOutTangent() {
        this.outTangent = null;
    }
    
    public ControlPoint setOutTangent(final double x, final double y) {
        if (Math.abs(this.getX() - x) < ControlPoint.DELTA_X && Math.abs(this.getY() - y) < ControlPoint.DELTA_Y) {
            this.outTangent = null;
        }
        else {
            this.outTangent = new Point2D(x, y);
        }
        return this;
    }
    
    public void moveInTangent(final double dx, final double dy) {
        if (this.hasInTangent()) {
            final Point2D inTangent = this.inTangent;
            inTangent.x += dx;
            final Point2D inTangent2 = this.inTangent;
            inTangent2.y += dy;
        }
    }
    
    public void moveOutTangent(final double dx, final double dy) {
        if (this.hasOutTangent()) {
            final Point2D outTangent = this.outTangent;
            outTangent.x += dx;
            final Point2D outTangent2 = this.outTangent;
            outTangent2.y += dy;
        }
    }
    
    public ControlPoint clone() {
        final ControlPoint pt = new ControlPoint(this.getX(), this.getY());
        if (this.hasInTangent()) {
            pt.setInTangent(this.getInX(), this.getInY());
        }
        if (this.hasOutTangent()) {
            pt.setOutTangent(this.getOutX(), this.getOutY());
        }
        return pt;
    }
    
    public boolean equals(final ControlPoint pt) {
        return this.getX() == pt.getX() && this.getY() == pt.getY() && this.getInX() == pt.getInX() && this.getInY() == pt.getInY() && this.getOutX() == pt.getOutX() && this.getOutY() == pt.getOutY();
    }
    
    static {
        ControlPoint.DELTA_X = 4.0;
        ControlPoint.DELTA_Y = 4.0;
    }
    
    public static class Point2D
    {
        public double x;
        public double y;
        
        public Point2D(final double x, final double y) {
            super();
            this.x = x;
            this.y = y;
        }
    }
}
