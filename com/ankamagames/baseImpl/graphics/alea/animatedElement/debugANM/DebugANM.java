package com.ankamagames.baseImpl.graphics.alea.animatedElement.debugANM;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.sun.opengl.util.texture.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;

public final class DebugANM
{
    public static final DebugANM INSTANCE;
    private boolean m_initialized;
    private JFrame m_frame;
    private float m_x;
    private float m_y;
    private Vector2 m_u;
    private Vector2 m_v;
    private Vector4 m_p;
    private Texture m_texture;
    private TextureCoords m_texCoords;
    private int m_hitX;
    private int m_hitY;
    private boolean m_hit;
    private BufferedImage m_image;
    private final ArrayList<Vector4[]> m_quads;
    private final ArrayList<Point> m_points;
    
    private DebugANM() {
        super();
        this.m_quads = new ArrayList<Vector4[]>();
        this.m_points = new ArrayList<Point>();
    }
    
    public void initialize() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DebugANM.this.m_frame = new JFrame("Debug ANM");
                DebugANM.this.m_frame.setContentPane(new JPanel());
                DebugANM.this.m_frame.setDefaultCloseOperation(3);
                DebugANM.this.m_frame.setSize(640, 480);
                DebugANM.this.m_frame.setVisible(true);
            }
        });
        this.m_initialized = true;
    }
    
    public void setVectors(final float x, final float y, final Vector2 u, final Vector2 v, final Vector4 p) {
        this.m_x = x;
        this.m_y = y;
        this.m_u = u;
        this.m_v = v;
        this.m_p = p;
    }
    
    private void drawQuad(final Graphics g, final Vector4[] q, final int cx, final int cy) {
        g.drawLine((int)(cx + q[0].getX()), (int)(cy - q[0].getY()), (int)(cx + q[1].getX()), (int)(cy - q[1].getY()));
        g.drawLine((int)(cx + q[1].getX()), (int)(cy - q[1].getY()), (int)(cx + q[2].getX()), (int)(cy - q[2].getY()));
        g.drawLine((int)(cx + q[2].getX()), (int)(cy - q[2].getY()), (int)(cx + q[3].getX()), (int)(cy - q[3].getY()));
        g.drawLine((int)(cx + q[3].getX()), (int)(cy - q[3].getY()), (int)(cx + q[0].getX()), (int)(cy - q[0].getY()));
    }
    
    public void update() {
        if (!this.m_initialized) {
            return;
        }
        if (this.m_frame == null) {
            return;
        }
        final Container panel = this.m_frame.getContentPane();
        final Graphics g = panel.getGraphics();
        final int w = panel.getWidth() / 2;
        final int h = panel.getHeight() / 2;
        g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
        g.setColor(Color.GRAY);
        for (final Vector4[] q : this.m_quads) {
            this.drawQuad(g, q, w, h);
        }
        final int x0 = (int)(w + this.m_x);
        final int y0 = (int)(h - this.m_y);
        for (final Point p : this.m_points) {
            p.draw(g, x0, y0);
        }
        if (this.m_image != null) {
            g.drawImage(this.m_image, 0, 0, null);
            if (this.m_hit) {
                g.setColor(Color.YELLOW);
            }
            else {
                g.setColor(new Color(64, 64, 64));
            }
            g.drawOval(this.m_hitX, this.m_hitY, 1, 1);
        }
        if (this.m_u != null) {
            g.setColor(Color.RED);
            g.drawLine(x0, y0, (int)(x0 + this.m_u.getX()), (int)(y0 - this.m_u.getY()));
        }
        if (this.m_v != null) {
            g.setColor(Color.PINK);
            g.drawLine(x0, y0, (int)(x0 + this.m_v.getX()), (int)(y0 - this.m_v.getY()));
        }
        g.setColor(Color.BLUE);
        if (this.m_p != null) {
            g.drawLine(x0, y0, (int)(x0 + this.m_p.getX()), (int)(y0 - this.m_p.getY()));
        }
    }
    
    public void addPoint(final Point point) {
        this.m_points.add(point);
    }
    
    public void addQuad(final Vector4[] quad) {
        this.m_quads.add(quad);
    }
    
    public void setTexture(final Texture texture, final TextureCoords coords) {
        if (this.m_texture == texture && equalsTexCoords(coords, this.m_texCoords)) {
            return;
        }
        this.m_texture = texture;
        this.m_texCoords = coords;
        final Layer layer = this.m_texture.getLayer(0);
        final int w = layer.getWidth();
        final int h = layer.getHeight();
        this.m_image = new BufferedImage(w, h, 2);
        final AlphaMask alphaMask = layer.getAlphaMask();
        final int color0 = new Color(128, 20, 128).getRGB();
        final int color = new Color(192, 192, 192).getRGB();
        try {
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    this.m_image.setRGB(x, y, alphaMask.getValue(x, y) ? color0 : color);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    private static boolean equalsTexCoords(final TextureCoords coords, final TextureCoords texCoords) {
        return coords == null || texCoords == null || (coords.left() == texCoords.left() && coords.bottom() == texCoords.bottom() && coords.right() == texCoords.right() && coords.top() == texCoords.left());
    }
    
    public void setTextureHit(final int x, final int y, final boolean hit) {
        this.m_hitX = x;
        this.m_hitY = y;
        this.m_hit = hit;
    }
    
    public void clear() {
        this.m_quads.clear();
        this.m_points.clear();
    }
    
    static {
        INSTANCE = new DebugANM();
        if (!DebugANM.INSTANCE.m_initialized) {
            DebugANM.INSTANCE.initialize();
        }
    }
    
    public static class Point
    {
        private final Vector2 m_pos;
        private final Color m_color;
        
        public Point(final Vector2 pos, final Color color) {
            super();
            this.m_pos = pos;
            this.m_color = color;
        }
        
        public Point(final float x, final float y, final Color color) {
            super();
            this.m_pos = new Vector2(x, y);
            this.m_color = color;
        }
        
        private void draw(final Graphics g, final int offsetX, final int offsetY) {
            g.setColor(this.m_color);
            g.drawOval(Math.round(this.m_pos.getX() + offsetX - 2.5f), Math.round(-this.m_pos.getY() + offsetY - 2.5f), 5, 5);
        }
    }
}
