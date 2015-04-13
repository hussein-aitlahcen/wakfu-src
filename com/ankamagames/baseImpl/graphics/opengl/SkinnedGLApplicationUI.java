package com.ankamagames.baseImpl.graphics.opengl;

import com.ankamagames.framework.kernel.core.controllers.*;
import javax.swing.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.graphics.debug.*;
import java.awt.event.*;
import java.awt.*;

public abstract class SkinnedGLApplicationUI extends GLApplicationUI implements FocusController, SkinnedTitleBar.TitleBarListener, WindowStateListener, WindowListener
{
    private ApplicationSkin m_skinDefault;
    private ApplicationSkin m_skinOff;
    protected JFrame m_frame;
    protected JComponent m_parentPanel;
    protected JPanel m_glComponentPanel;
    private SkinnedComponent m_bottomBorder;
    private SkinnedComponent m_bottomRightCorner;
    private SkinnedComponent m_rightBorder;
    private SkinnedComponent m_bottomLeftBorder;
    private SkinnedComponent m_leftBorder;
    private SkinnedTitleBar m_titleBar;
    private FrameResizer m_frameResizer;
    private boolean m_init;
    
    protected SkinnedGLApplicationUI() {
        super();
        this.m_init = false;
    }
    
    public void setSkins(final ApplicationSkin skin, final ApplicationSkin skinOff) {
        this.m_skinDefault = skin;
        this.m_skinOff = skinOff;
    }
    
    @Override
    protected JFrame createApplicationFrame() {
        JFrame.setDefaultLookAndFeelDecorated(false);
        (this.m_frame = new JFrame()).setUndecorated(true);
        this.m_frame.setFocusable(false);
        final Container contentPane = this.m_frame.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.insets = new Insets(0, 0, 0, 0);
        final Insets borderInsets = this.m_skinDefault.getBorderInsets();
        final Dimension titleBarSize = new Dimension(-1, Math.max(1, borderInsets.top));
        final GridBagConstraints gridBagConstraints = c;
        final GridBagConstraints gridBagConstraints2 = c;
        final double n = 0.0;
        gridBagConstraints2.weighty = n;
        gridBagConstraints.weightx = n;
        c.gridy = 0;
        c.gridwidth = 3;
        (this.m_titleBar = new SkinnedTitleBar(this)).applySkin(this.m_skinDefault);
        setAllSizes(this.m_titleBar, titleBarSize);
        contentPane.add(this.m_titleBar, c);
        c.gridwidth = 1;
        this.m_leftBorder = new SkinnedComponent();
        this.m_bottomLeftBorder = new SkinnedComponent();
        this.m_rightBorder = new SkinnedComponent();
        this.m_bottomBorder = new SkinnedComponent();
        this.m_bottomRightCorner = new SkinnedComponent();
        c.gridy = 1;
        c.gridx = 0;
        setAllSizes(this.m_leftBorder, new Dimension(borderInsets.left, -1));
        contentPane.add(this.m_leftBorder, c);
        c.gridx = 2;
        setAllSizes(this.m_rightBorder, new Dimension(borderInsets.right, -1));
        contentPane.add(this.m_rightBorder, c);
        c.gridy = 2;
        c.gridx = 0;
        setAllSizes(this.m_bottomLeftBorder, new Dimension(borderInsets.left, borderInsets.bottom));
        contentPane.add(this.m_bottomLeftBorder, c);
        c.gridx = 1;
        setAllSizes(this.m_bottomBorder, new Dimension(-1, borderInsets.bottom));
        contentPane.add(this.m_bottomBorder, c);
        c.gridx = 2;
        setAllSizes(this.m_bottomRightCorner, new Dimension(borderInsets.right, borderInsets.bottom));
        contentPane.add(this.m_bottomRightCorner, c);
        c.fill = 1;
        c.gridx = 1;
        c.gridy = 1;
        final GridBagConstraints gridBagConstraints3 = c;
        final GridBagConstraints gridBagConstraints4 = c;
        final double n2 = 1.0;
        gridBagConstraints4.weighty = n2;
        gridBagConstraints3.weightx = n2;
        contentPane.add(this.m_parentPanel = new JPanel(new BorderLayout(), true), c);
        this.m_glComponentPanel = new JPanel(new StaticLayout(), true);
        final StaticLayoutData glCompLayoutData = new StaticLayoutData(1.0f, 1.0f, (byte)1);
        glCompLayoutData.setEnsurePairDimension(true);
        this.m_glComponentPanel.add((Component)this.getGLComponent(), glCompLayoutData);
        this.m_parentPanel.add(this.m_glComponentPanel, "Center");
        final FrameMover cm = new FrameMover(this.m_frame, new Component[] { this.m_titleBar });
        (this.m_frameResizer = new FrameResizer(this.m_frame, new Component[] { this.m_bottomBorder, this.m_bottomRightCorner, this.m_rightBorder })).setMinimumSize(this.getMinimumSize());
        this.m_frameResizer.addComponentsToRepain(this.m_titleBar, this.m_bottomRightCorner, this.m_bottomBorder, this.m_rightBorder, this.m_leftBorder, this.m_bottomLeftBorder);
        this.m_frame.addWindowStateListener(this);
        this.m_frame.addWindowListener(this);
        this.getRenderer().pushFocusController(this, false);
        this.enableResize(true);
        this.m_init = true;
        return this.m_frame;
    }
    
    private void applySkin(final ApplicationSkin skin) {
        this.m_bottomBorder.setImage(skin.getBottomImage());
        this.m_bottomLeftBorder.setImage(skin.getBottomLeftImage());
        this.m_bottomRightCorner.setImage(skin.getBottomRightImage());
        this.m_leftBorder.setImage(skin.getLeftImage());
        this.m_rightBorder.setImage(skin.getRightImage());
        this.m_titleBar.applySkin(skin);
        this.m_frame.repaint();
    }
    
    private static void setAllSizes(final Component c, final Dimension s) {
        c.setMinimumSize(s);
        c.setSize(s);
        c.setPreferredSize(s);
        c.setMaximumSize(s);
    }
    
    private void enableResize(final boolean enabled) {
        if (this.m_frameResizer == null) {
            return;
        }
        this.m_frameResizer.setEnabled(enabled);
        if (enabled) {
            this.m_bottomBorder.setCursor(Cursor.getPredefinedCursor(9));
            this.m_rightBorder.setCursor(Cursor.getPredefinedCursor(11));
            this.m_bottomRightCorner.setCursor(Cursor.getPredefinedCursor(5));
        }
        else {
            this.m_bottomBorder.setCursor(Cursor.getPredefinedCursor(0));
            this.m_rightBorder.setCursor(Cursor.getPredefinedCursor(0));
            this.m_bottomRightCorner.setCursor(Cursor.getPredefinedCursor(0));
        }
    }
    
    private void showDecoration(final boolean show) {
        for (final Component c : this.m_frame.getContentPane().getComponents()) {
            if (c != this.m_parentPanel) {
                c.setVisible(show);
            }
        }
    }
    
    @Override
    protected boolean applyFullScreenResolution(final int resWidth, final int resHeight, final int resBpp, final int frequency) {
        final boolean result = super.applyFullScreenResolution(resWidth, resHeight, resBpp, frequency);
        if (result) {
            this.showDecoration(false);
        }
        return result;
    }
    
    @Override
    protected void applyWindowedFullscreenResolution() {
        this.showDecoration(false);
        super.applyWindowedFullscreenResolution();
    }
    
    @Override
    protected void applyWindowedResolution(final int resWidth, final int resHeight) {
        this.showDecoration(true);
        super.applyWindowedResolution(resWidth, resHeight);
    }
    
    @Override
    public boolean focusGained(final FocusEvent e) {
        return false;
    }
    
    @Override
    public boolean focusLost(final FocusEvent e) {
        if (OS.isMacOs() && !e.isTemporary()) {
            final Component compFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (compFocusOwner != this.getGLComponent()) {
                this.getGLComponent().requestFocus();
            }
        }
        return false;
    }
    
    @Override
    public void onCloseEvent() {
        this.close();
    }
    
    @Override
    public void onEnlargeEvent() {
        this.m_frame.setExtendedState((this.m_frame.getExtendedState() == 6) ? 0 : 6);
    }
    
    @Override
    public void onReduceEvent() {
        final int currentMaximizationState = this.m_frame.getExtendedState() & 0x6;
        this.m_frame.setExtendedState(0x1 | currentMaximizationState);
    }
    
    @Override
    public void addDebugBar(final DebugBar bar) {
        final Rectangle bounds = new Rectangle(this.m_frame.getBounds());
        this.m_parentPanel.add(bar, "South");
        bar.setVisible(true);
        this.m_frame.pack();
        this.m_frame.setBounds(bounds);
    }
    
    @Override
    public void removeDebugBar() {
        final Rectangle bounds = new Rectangle(this.m_frame.getBounds());
        for (final Component c : this.m_parentPanel.getComponents()) {
            if (c instanceof DebugBar) {
                this.m_parentPanel.remove(c);
            }
        }
        this.m_frame.pack();
        this.m_frame.setBounds(bounds);
    }
    
    @Override
    public void windowStateChanged(final WindowEvent e) {
        final int newState = e.getNewState();
        final boolean maximized = (newState & 0x6) != 0x0;
        this.m_titleBar.setMaximized(maximized);
        this.enableResize(!maximized);
    }
    
    @Override
    public void windowActivated(final WindowEvent e) {
        if (this.m_skinDefault != null) {
            this.applySkin(this.m_skinDefault);
        }
    }
    
    @Override
    public void windowDeactivated(final WindowEvent e) {
        if (this.m_skinOff != null) {
            this.applySkin(this.m_skinOff);
        }
    }
    
    @Override
    public void windowOpened(final WindowEvent e) {
    }
    
    @Override
    public void windowClosing(final WindowEvent e) {
    }
    
    @Override
    public void windowClosed(final WindowEvent e) {
    }
    
    @Override
    public void windowIconified(final WindowEvent e) {
    }
    
    @Override
    public void windowDeiconified(final WindowEvent e) {
    }
    
    public JPanel getGlComponentPanel() {
        return this.m_glComponentPanel;
    }
    
    public boolean isInit() {
        return this.m_init;
    }
    
    @Override
    public void setGLComponentVisible(final boolean visible) {
        super.setGLComponentVisible(visible);
        if (!EventQueue.isDispatchThread()) {
            return;
        }
        this.m_glComponentPanel.validate();
    }
}
