package com.ankamagames.baseImpl.graphics.debug;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Separator extends JPanel
{
    private Graphics m_buffer;
    private Image m_bufferImage;
    
    public Separator(final int width, final int height) {
        super();
        final Dimension size = new Dimension(width, height);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.initImage();
    }
    
    private boolean initImage() {
        this.m_bufferImage = this.createImage(this.getWidth(), this.getHeight());
        if (this.m_bufferImage == null) {
            return false;
        }
        (this.m_buffer = this.m_bufferImage.getGraphics()).clearRect(0, 0, this.getWidth(), this.getHeight());
        this.m_buffer.setColor(Color.GRAY);
        this.m_buffer.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
        return true;
    }
    
    @Override
    public void paint(final Graphics graphics) {
        if (!this.initImage()) {
            super.paint(graphics);
        }
        graphics.drawImage(this.m_bufferImage, 0, 0, null);
    }
    
    @Override
    public void update(final Graphics graphics) {
        this.paint(graphics);
    }
}
