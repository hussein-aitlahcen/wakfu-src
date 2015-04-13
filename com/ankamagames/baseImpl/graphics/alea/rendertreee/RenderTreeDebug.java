package com.ankamagames.baseImpl.graphics.alea.rendertreee;

import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import javax.swing.*;

public final class RenderTreeDebug
{
    public static final RenderTreeDebug INSTANCE;
    private boolean m_initialized;
    private JFrame m_frame;
    private RenderTreeDebugView m_view;
    private RenderTreeStencil m_renderTree;
    
    public void initialize() {
        if (this.m_initialized) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RenderTreeDebug.this.m_frame = new JFrame("RenderTree Debug");
                RenderTreeDebug.this.m_view = new RenderTreeDebugView();
                RenderTreeDebug.this.m_frame.setContentPane(RenderTreeDebug.this.m_view.getRootPanel());
                RenderTreeDebug.this.m_frame.setDefaultCloseOperation(3);
                RenderTreeDebug.this.m_frame.setSize(300, 600);
                RenderTreeDebug.this.m_frame.setVisible(true);
                ProcessScheduler.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (RenderTreeDebug.this.m_view != null && RenderTreeDebug.this.m_renderTree != null) {
                            RenderTreeDebug.this.m_view.updateData(RenderTreeDebug.this.m_renderTree);
                        }
                    }
                }, 2000L, -1);
            }
        });
        this.m_initialized = true;
    }
    
    public void setRenderTree(final RenderTreeStencil renderTree) {
        this.m_renderTree = renderTree;
    }
    
    static {
        INSTANCE = new RenderTreeDebug();
    }
}
