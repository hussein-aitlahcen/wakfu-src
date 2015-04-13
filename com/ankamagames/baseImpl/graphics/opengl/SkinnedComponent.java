package com.ankamagames.baseImpl.graphics.opengl;

import javax.swing.*;
import org.apache.log4j.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.*;

public class SkinnedComponent extends JComponent
{
    public static final Logger m_logger;
    private Image m_image;
    
    public SkinnedComponent() {
        this(null);
    }
    
    public SkinnedComponent(final Image image) {
        super();
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        this.m_image = image;
    }
    
    public void setImage(final Image image) {
        this.m_image = image;
        this.repaint();
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        if (this.m_image == null) {
            return;
        }
        g.drawImage(this.m_image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
    
    @Override
    protected void paintBorder(final Graphics graphics) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)SkinnedComponent.class);
    }
}
