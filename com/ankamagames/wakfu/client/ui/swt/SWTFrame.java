package com.ankamagames.wakfu.client.ui.swt;

import org.eclipse.swt.widgets.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.eclipse.swt.graphics.*;

public class SWTFrame extends Composite
{
    private SWTApplicationSkin m_skin;
    private SWTSkinnedComponent m_topLeft;
    private SWTSkinnedComponent m_top;
    private SWTSkinnedComponent m_topRight;
    private SWTSkinnedComponent m_left;
    private SWTSkinnedComponent m_right;
    private SWTSkinnedComponent m_bottomLeft;
    private SWTSkinnedComponent m_bottom;
    private SWTSkinnedComponent m_bottomRight;
    private SWTButton m_swtButton;
    private Control m_content;
    private boolean m_showDecorations;
    
    public SWTFrame(final Composite parent, final int style, final SWTApplicationSkin skin) {
        super(parent, style);
        this.m_skin = skin;
        (this.m_swtButton = new SWTButton(this, 0)).setNormalIcon(skin.getCloseButtonImage());
        this.m_swtButton.setOverIcon(skin.getCloseButtonOverImage());
        this.m_swtButton.setPressedIcon(skin.getCloseButtonOverImage());
        this.m_topLeft = new SWTSkinnedComponent(this, 0, skin.getTopLeftImage());
        this.m_top = new SWTSkinnedComponent(this, 0, skin.getTopImage());
        this.m_topRight = new SWTSkinnedComponent(this, 0, skin.getTopRightImage());
        this.m_left = new SWTSkinnedComponent(this, 0, skin.getLeftImage());
        this.m_right = new SWTSkinnedComponent(this, 0, skin.getRightImage());
        this.m_bottomLeft = new SWTSkinnedComponent(this, 0, skin.getBottomLeftImage());
        this.m_bottom = new SWTSkinnedComponent(this, 0, skin.getBottomImage());
        this.m_bottomRight = new SWTSkinnedComponent(this, 0, skin.getBottomRightImage());
        this.setLayout((Layout)new FrameLayout());
        this.m_showDecorations = true;
    }
    
    public void setContent(final Control content) {
        this.m_content = content;
    }
    
    public void showDecorations(final boolean show) {
        if (this.m_showDecorations == show) {
            return;
        }
        this.m_showDecorations = show;
        this.m_topLeft.setVisible(show);
        this.m_top.setVisible(show);
        this.m_topRight.setVisible(show);
        this.m_left.setVisible(show);
        this.m_right.setVisible(show);
        this.m_bottomLeft.setVisible(show);
        this.m_bottom.setVisible(show);
        this.m_bottomRight.setVisible(show);
        this.m_swtButton.setVisible(show);
    }
    
    public SWTButton getCloseButton() {
        return this.m_swtButton;
    }
    
    public class FrameLayout extends Layout
    {
        private Point topLeft;
        private Point top;
        private Point topRight;
        private Point left;
        private Point right;
        private Point bottomLeft;
        private Point bottom;
        private Point bottomRight;
        private Point buttonSize;
        private Point contentSize;
        
        protected Point computeSize(final Composite parent, final int widthHint, final int heightHint, final boolean flushCache) {
            if (flushCache || this.topLeft == null) {
                this.topLeft = SWTFrame.this.m_topLeft.computeSize(widthHint, heightHint, flushCache);
                this.top = SWTFrame.this.m_top.computeSize(widthHint, heightHint, flushCache);
                this.topRight = SWTFrame.this.m_topRight.computeSize(widthHint, heightHint, flushCache);
                this.left = SWTFrame.this.m_left.computeSize(widthHint, heightHint, flushCache);
                this.right = SWTFrame.this.m_right.computeSize(widthHint, heightHint, flushCache);
                this.bottomLeft = SWTFrame.this.m_bottomLeft.computeSize(widthHint, heightHint, flushCache);
                this.bottom = SWTFrame.this.m_bottom.computeSize(widthHint, heightHint, flushCache);
                this.bottomRight = SWTFrame.this.m_bottomRight.computeSize(widthHint, heightHint, flushCache);
                this.buttonSize = SWTFrame.this.m_swtButton.computeSize(widthHint, heightHint, flushCache);
                this.contentSize = SWTFrame.this.m_content.computeSize(widthHint, heightHint, flushCache);
            }
            int width = 0;
            width = MathHelper.max(width, this.topLeft.x + this.top.x + this.topRight.x, new int[0]);
            width = MathHelper.max(width, this.left.x + this.contentSize.x + this.right.x, new int[0]);
            width = MathHelper.max(width, this.bottomLeft.x + this.bottom.x + this.bottomRight.x, new int[0]);
            int height = 0;
            height = MathHelper.max(height, this.topLeft.y + this.left.y + this.bottomLeft.y, new int[0]);
            height = MathHelper.max(height, this.top.y + this.contentSize.y + this.bottom.y, new int[0]);
            height = MathHelper.max(height, this.topRight.y + this.right.y + this.bottomRight.y, new int[0]);
            return new Point(width, height);
        }
        
        protected void layout(final Composite composite, final boolean flushCache) {
            final Rectangle contentBounds = composite.getBounds();
            final int contentWidth = contentBounds.width;
            final int contentHeight = contentBounds.height;
            SWTFrame.this.m_topLeft.setBounds(0, 0, this.topLeft.x, this.topLeft.y);
            SWTFrame.this.m_top.setBounds(this.topLeft.x, 0, contentWidth - this.topLeft.x - this.topRight.x, this.top.y);
            SWTFrame.this.m_topRight.setBounds(contentWidth - this.topRight.x, 0, this.topRight.x, this.topRight.y);
            SWTFrame.this.m_left.setBounds(0, this.topLeft.y, this.left.x, contentHeight - this.topLeft.y - this.bottomLeft.y);
            SWTFrame.this.m_right.setBounds(contentWidth - this.right.x, this.topRight.y, this.right.x, contentHeight - this.topRight.y - this.bottomRight.y);
            SWTFrame.this.m_bottomLeft.setBounds(0, contentHeight - this.bottomLeft.y, this.bottomLeft.x, this.bottomLeft.y);
            SWTFrame.this.m_bottom.setBounds(this.bottomLeft.x, contentHeight - this.bottom.y, contentWidth - this.bottomLeft.x - this.bottomRight.x, this.bottom.y);
            SWTFrame.this.m_bottomRight.setBounds(contentWidth - this.bottomRight.x, contentHeight - this.bottomRight.y, this.bottomRight.x, this.bottomRight.y);
            SWTFrame.this.m_swtButton.setBounds(contentWidth - this.buttonSize.x - 3, 3, this.buttonSize.x, this.buttonSize.y);
            if (SWTFrame.this.m_showDecorations) {
                SWTFrame.this.m_content.setBounds(this.left.x, this.top.y, contentWidth - this.left.x - this.right.x, contentHeight - this.top.y - this.bottom.y);
            }
            else {
                SWTFrame.this.m_content.setBounds(0, 0, contentWidth, contentHeight);
            }
        }
        
        protected boolean flushCache(final Control control) {
            final boolean success = super.flushCache(control);
            return success;
        }
    }
}
