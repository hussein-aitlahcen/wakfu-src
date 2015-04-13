package com.ankamagames.baseImpl.graphics.opengl;

import org.apache.log4j.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

class SkinnedTitleBar extends JPanel
{
    public static final Logger m_logger;
    private static final int BUTTON_WIDTH = 17;
    private static final int BUTTON_HEIGHT = 17;
    private final TitleBarListener m_listener;
    private JButton m_minimizeButton;
    private JButton m_maximizeRestoreButton;
    private JButton m_closeButton;
    private ApplicationSkin m_currentSkin;
    private boolean m_maximized;
    
    SkinnedTitleBar(final TitleBarListener listener) {
        super(new BorderLayout(1, 3));
        this.m_maximized = false;
        this.m_listener = listener;
        this.setFocusable(false);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        final JComponent leftPane = new JPanel(new FlowLayout(3, 1, 3));
        leftPane.setOpaque(false);
        this.add(leftPane, "Before");
        final int leftSpace = 20;
        final Component leftSpacer = new Container();
        leftSpacer.setSize(new Dimension(20, 1));
        leftSpacer.setMinimumSize(new Dimension(20, 1));
        leftSpacer.setMaximumSize(new Dimension(20, 1));
        leftSpacer.setPreferredSize(new Dimension(20, 1));
        leftPane.add(leftSpacer);
        final JComponent rightPane = new JPanel(new FlowLayout(4, 1, 3));
        rightPane.setOpaque(false);
        this.add(rightPane, "After");
        rightPane.add(this.m_minimizeButton = this.createButton());
        if (this.m_listener != null) {
            this.m_minimizeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent actionEvent) {
                    SkinnedTitleBar.this.m_listener.onReduceEvent();
                }
            });
        }
        rightPane.add(this.m_maximizeRestoreButton = this.createButton());
        if (this.m_listener != null) {
            this.m_maximizeRestoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent actionEvent) {
                    SkinnedTitleBar.this.m_listener.onEnlargeEvent();
                }
            });
        }
        rightPane.add(this.m_closeButton = this.createButton());
        this.m_closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                if (SkinnedTitleBar.this.m_listener != null) {
                    SkinnedTitleBar.this.m_listener.onCloseEvent();
                }
                else {
                    System.exit(0);
                }
            }
        });
        final Component rightSpacer = new Container();
        rightSpacer.setSize(new Dimension(2, 1));
        rightSpacer.setMinimumSize(new Dimension(2, 1));
        rightSpacer.setMaximumSize(new Dimension(2, 1));
        rightSpacer.setPreferredSize(new Dimension(2, 1));
        rightPane.add(rightSpacer);
        if (this.m_listener != null) {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        SkinnedTitleBar.this.m_listener.onEnlargeEvent();
                    }
                }
            });
        }
    }
    
    public void setMaximized(final boolean maximized) {
        this.m_maximized = maximized;
        this.applySkin(this.m_currentSkin);
    }
    
    private JButton createButton() {
        final Dimension size = new Dimension(17, 17);
        final JButton button = new JButton();
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setFocusPainted(false);
        button.setMinimumSize(size);
        button.setSize(size);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        return button;
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        if (this.m_currentSkin == null) {
            return;
        }
        final int topLetImageWidth = this.m_currentSkin.getTopLeftImage().getWidth(null);
        final int topRightImageWidth = this.m_currentSkin.getTopRightImage().getWidth(null);
        final int barHeight = this.getHeight();
        final int barWidth = this.getWidth();
        g.drawImage(this.m_currentSkin.getTopLeftImage(), 0, 0, topLetImageWidth, barHeight, null);
        g.drawImage(this.m_currentSkin.getTopRightImage(), barWidth - topRightImageWidth, 0, topRightImageWidth, barHeight, null);
        g.drawImage(this.m_currentSkin.getTopImage(), topLetImageWidth, 0, barWidth - topLetImageWidth - topRightImageWidth, barHeight, null);
    }
    
    void applySkin(final ApplicationSkin skin) {
        this.m_currentSkin = skin;
        if (!this.m_maximized) {
            this.m_maximizeRestoreButton.setIcon(skin.getMaximizeIcon());
            this.m_maximizeRestoreButton.setRolloverIcon(skin.getMaximizeOverIcon());
        }
        else {
            this.m_maximizeRestoreButton.setIcon(skin.getRestoreIcon());
            this.m_maximizeRestoreButton.setRolloverIcon(skin.getRestoreOverIcon());
        }
        this.m_minimizeButton.setIcon(skin.getMinimizeIcon());
        this.m_minimizeButton.setRolloverIcon(skin.getMinimizeOverIcon());
        this.m_closeButton.setIcon(skin.getCloseIcon());
        this.m_closeButton.setRolloverIcon(skin.getCloseOverIcon());
        this.repaint();
    }
    
    Component getMaximizeButton() {
        return this.m_maximizeRestoreButton;
    }
    
    Component getMinimizeButton() {
        return this.m_minimizeButton;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SkinnedTitleBar.class);
    }
    
    interface TitleBarListener
    {
        void onCloseEvent();
        
        void onEnlargeEvent();
        
        void onReduceEvent();
    }
}
