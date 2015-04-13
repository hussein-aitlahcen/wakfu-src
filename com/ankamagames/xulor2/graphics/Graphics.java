package com.ankamagames.xulor2.graphics;

import javax.media.opengl.glu.*;
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.image.*;

public class Graphics
{
    private static Graphics m_graphics;
    private static boolean DEBUG;
    private GL m_gl;
    private GLU m_glu;
    private GLUquadric m_quadric;
    private int m_width;
    private int m_height;
    private Point m_centerOffset;
    private PooledRectangle m_scissor;
    private final ArrayList<PooledRectangle> m_scissorStack;
    private GLDrawable m_drawable;
    
    public static Graphics getInstance() {
        return Graphics.m_graphics;
    }
    
    private Graphics() {
        super();
        this.m_glu = new GLU();
        this.m_quadric = this.m_glu.gluNewQuadric();
        this.m_centerOffset = new Point();
        this.m_scissorStack = new ArrayList<PooledRectangle>();
    }
    
    public void initialize(final GL gl, final GLDrawable drawable) {
        this.m_gl = gl;
        this.m_drawable = drawable;
        this.m_width = this.m_drawable.getWidth();
        this.m_height = this.m_drawable.getHeight();
    }
    
    public final Point getCenterOffset() {
        return this.m_centerOffset;
    }
    
    public boolean isInScissor(final PooledRectangle r) {
        return r.intersects(this.m_scissor);
    }
    
    public PooledRectangle getScissor() {
        return this.m_scissor;
    }
    
    public void pushScissor(final PooledRectangle scissor) {
        if (!this.m_scissorStack.isEmpty()) {
            final PooledRectangle top = this.m_scissorStack.get(this.m_scissorStack.size() - 1);
            if (top.intersects(scissor)) {
                scissor.setBoundsFromIntersection(top, scissor);
            }
            else {
                scissor.setBounds(0, 0, 0, 0);
            }
        }
        this.m_scissorStack.add(scissor);
        this.m_scissor = scissor;
    }
    
    public void popScissor() {
        if (!this.m_scissorStack.isEmpty()) {
            this.m_scissorStack.remove(this.m_scissorStack.size() - 1).release();
            this.m_scissor = this.peekScissor();
        }
    }
    
    public PooledRectangle peekScissor() {
        if (!this.m_scissorStack.isEmpty()) {
            return this.m_scissorStack.get(this.m_scissorStack.size() - 1);
        }
        return null;
    }
    
    public void setDrawableSize(final int width, final int height) {
        this.m_width = width;
        this.m_height = height;
        this.m_scissor = PooledRectangle.checkout(0, 0, width, height);
        this.m_centerOffset.setLocation(-width / 2, -height / 2);
    }
    
    public Dimension getDrawableSize() {
        return new Dimension(this.m_width, this.m_height);
    }
    
    public void drawRect(final int x, final int y, final int width, final int height) {
        this.m_gl.glBegin(2);
        this.m_gl.glVertex2i(x, y);
        this.m_gl.glVertex2i(x + width, y);
        this.m_gl.glVertex2i(x + width, y + height);
        this.m_gl.glVertex2i(x, y + height);
        this.m_gl.glEnd();
    }
    
    public void drawFilledRect(final int x, final int y, final int width, final int height) {
        this.m_gl.glBegin(7);
        this.m_gl.glVertex2i(x, y);
        this.m_gl.glVertex2i(x + width, y);
        this.m_gl.glVertex2i(x + width, y + height);
        this.m_gl.glVertex2i(x, y + height);
        this.m_gl.glEnd();
    }
    
    public void drawCircle(final int x, final int y, final int radius) {
        this.m_glu.gluQuadricTexture(this.m_quadric, true);
        this.m_gl.glTranslatef((float)x, (float)y, 0.0f);
        this.m_glu.gluDisk(this.m_quadric, 0.0, (double)radius, (radius > 50) ? 32 : 16, 1);
        this.m_gl.glTranslatef((float)(-x), (float)(-y), 0.0f);
    }
    
    public void setColor(final Color color) {
        this.m_gl.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public void translate(final int x, final int y) {
        this.m_gl.glTranslatef((float)x, (float)y, 0.0f);
    }
    
    static {
        Graphics.m_graphics = new Graphics();
        Graphics.DEBUG = false;
    }
}
