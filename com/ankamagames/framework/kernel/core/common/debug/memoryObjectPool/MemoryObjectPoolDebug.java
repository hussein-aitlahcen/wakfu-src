package com.ankamagames.framework.kernel.core.common.debug.memoryObjectPool;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.debug.analyzer.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.debug.memoryObjectPool.data.*;
import java.awt.event.*;
import javax.swing.*;

public final class MemoryObjectPoolDebug
{
    public static final MemoryObjectPoolDebug INSTANCE;
    private static final Logger m_logger;
    private boolean m_initialized;
    private MonitoredPoolAnalyzer m_frame;
    private MemoryObjectPoolDebugView m_view;
    private final TIntObjectHashMap<PoolData> m_poolsData;
    
    private MemoryObjectPoolDebug() {
        super();
        this.m_poolsData = new TIntObjectHashMap<PoolData>();
    }
    
    public void initialize() {
        if (this.m_initialized) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MemoryObjectPoolDebug.this.m_frame = new MonitoredPoolAnalyzer();
                MemoryObjectPoolDebug.this.m_frame.setTitle("MonitoredPool Analyzer");
                MemoryObjectPoolDebug.this.m_frame.setDefaultCloseOperation(3);
                MemoryObjectPoolDebug.this.m_frame.setSize(800, 600);
                MemoryObjectPoolDebug.this.m_frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(final WindowEvent e) {
                        MemoryObjectPoolDebug.this.m_initialized = false;
                    }
                });
                MemoryObjectPoolDebug.this.m_frame.setDefaultCloseOperation(2);
                MemoryObjectPoolDebug.this.m_frame.setVisible(true);
            }
        });
        this.m_initialized = true;
    }
    
    static {
        INSTANCE = new MemoryObjectPoolDebug();
        m_logger = Logger.getLogger((Class)MemoryObjectPoolDebug.class);
    }
}
