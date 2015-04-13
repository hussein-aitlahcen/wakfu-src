package com.ankamagames.framework.graphics.image;

import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PooledRectangle implements Releasable
{
    protected static final Logger m_logger;
    private static final ObjectPool m_pool;
    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    
    public static PooledRectangle checkout() {
        return checkout(0, 0, 0, 0);
    }
    
    public static PooledRectangle checkout(final int x, final int y, final int w, final int h) {
        try {
            final PooledRectangle rectangle = (PooledRectangle)PooledRectangle.m_pool.borrowObject();
            rectangle.setBounds(x, y, w, h);
            return rectangle;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut sur un Item : ", e);
        }
    }
    
    private PooledRectangle(final int x, final int y, final int width, final int height) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
    }
    
    @Override
    public void release() {
        try {
            PooledRectangle.m_pool.returnObject(this);
        }
        catch (Exception e) {
            PooledRectangle.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + " (normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_x = 0;
        this.m_y = 0;
        this.m_width = 0;
        this.m_height = 0;
    }
    
    public PooledRectangle checkoutedUnion(final PooledRectangle r) {
        final int x1 = Math.min(this.m_x, r.m_x);
        final int y1 = Math.min(this.m_y, r.m_y);
        final int x2 = Math.max(this.m_x + this.m_width, r.m_x + r.m_width);
        final int y2 = Math.max(this.m_y + this.m_height, r.m_y + r.m_height);
        return checkout(x1, y1, x2 - x1, y2 - y1);
    }
    
    public void storeUnion(final PooledRectangle r) {
        final int x1 = Math.min(this.m_x, r.m_x);
        final int y1 = Math.min(this.m_y, r.m_y);
        final int x2 = Math.max(this.m_x + this.m_width, r.m_x + r.m_width);
        final int y2 = Math.max(this.m_y + this.m_height, r.m_y + r.m_height);
        this.setBounds(x1, y1, x2 - x1, y2 - y1);
    }
    
    public PooledRectangle checkoutedIntersection(final PooledRectangle r) {
        final PooledRectangle ir = checkout();
        ir.setBoundsFromIntersection(this, r);
        return ir;
    }
    
    public void setBoundsFromIntersection(final PooledRectangle a, final PooledRectangle b) {
        int tx1 = a.m_x;
        int ty1 = a.m_y;
        final int rx1 = b.m_x;
        final int ry1 = b.m_y;
        long tx2 = tx1;
        tx2 += a.m_width;
        long ty2 = ty1;
        ty2 += a.m_height;
        long rx2 = rx1;
        rx2 += b.m_width;
        long ry2 = ry1;
        ry2 += b.m_height;
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        tx2 -= tx1;
        ty2 -= ty1;
        if (tx2 < -2147483648L) {
            tx2 = -2147483648L;
        }
        if (ty2 < -2147483648L) {
            ty2 = -2147483648L;
        }
        this.m_x = tx1;
        this.m_y = ty1;
        this.m_width = (int)tx2;
        this.m_height = (int)ty2;
    }
    
    public void setBoundsFromIntersection(final int x0, final int y0, final int w0, final int h0, final int x1, final int y1, final int w1, final int h1) {
        int tx1 = x0;
        int ty1 = y0;
        long tx2 = tx1;
        tx2 += w0;
        long ty2 = ty1;
        ty2 += h0;
        long rx2 = x1;
        rx2 += w1;
        long ry2 = y1;
        ry2 += h1;
        if (tx1 < x1) {
            tx1 = x1;
        }
        if (ty1 < y1) {
            ty1 = y1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        tx2 -= tx1;
        ty2 -= ty1;
        if (tx2 < -2147483648L) {
            tx2 = -2147483648L;
        }
        if (ty2 < -2147483648L) {
            ty2 = -2147483648L;
        }
        this.m_x = tx1;
        this.m_y = ty1;
        this.m_width = (int)tx2;
        this.m_height = (int)ty2;
    }
    
    public boolean intersects(final PooledRectangle r) {
        int tw = this.m_width;
        int th = this.m_height;
        int rw = r.m_width;
        int rh = r.m_height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        final int tx = this.m_x;
        final int ty = this.m_y;
        final int rx = r.m_x;
        final int ry = r.m_y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        return (rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry);
    }
    
    public static boolean intersects(final int x0, final int y0, final int w0, final int h0, final int x1, final int y1, final int w1, final int h1) {
        if (w1 <= 0 || h1 <= 0 || w0 <= 0 || h0 <= 0) {
            return false;
        }
        final int rw = w1 + x1;
        final int rh = h1 + y1;
        final int tw = w0 + x0;
        final int th = h0 + y0;
        return (rw < x1 || rw > x0) && (rh < y1 || rh > y0) && (tw < x0 || tw > x1) && (th < y0 || th > y1);
    }
    
    public void setBounds(final PooledRectangle rect) {
        this.m_x = rect.m_x;
        this.m_y = rect.m_y;
        this.m_width = rect.m_width;
        this.m_height = rect.m_height;
    }
    
    public void setBounds(final int x, final int y, final int w, final int h) {
        this.m_x = x;
        this.m_y = y;
        this.m_width = w;
        this.m_height = h;
    }
    
    public void setLocation(final int x, final int y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public void setSize(final Dimension d) {
        this.setSize(d.width, d.height);
    }
    
    public void setSize(final int width, final int height) {
        this.m_width = width;
        this.m_height = height;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PooledRectangle.class);
        m_pool = new MonitoredPool(new ObjectFactory<PooledRectangle>() {
            @Override
            public PooledRectangle makeObject() {
                return new PooledRectangle(0, 0, 0, 0, null);
            }
        });
    }
}
