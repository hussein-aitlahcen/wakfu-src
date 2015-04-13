package com.ankamagames.wakfu.client.ui.mru;

import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.xulor2.core.*;

public class MRUDirectRunnerManager
{
    public static final MRUDirectRunnerManager INSTANCE;
    private MRUActionDirectRunner m_currentRunner;
    
    private MRUDirectRunnerManager() {
        super();
        this.m_currentRunner = null;
    }
    
    public boolean isRunning() {
        return this.m_currentRunner != null;
    }
    
    public void onMouseMove(final WakfuWorldScene worldScene, final int x, final int y) {
        if (this.m_currentRunner != null) {
            this.m_currentRunner.onMouseMove(worldScene, x, y);
        }
    }
    
    public void onMouseClick(final WakfuWorldScene worldScene, final int x, final int y) {
        if (this.m_currentRunner != null) {
            this.m_currentRunner.onMouseClick(worldScene, x, y);
            this.m_currentRunner = null;
        }
    }
    
    public void start(final Class<? extends AbstractMRUAction> action, final CursorFactory.CursorType normalIcon, final CursorFactory.CursorType highlightIcon) {
        (this.m_currentRunner = new MRUActionDirectRunner(action, normalIcon, highlightIcon)).start();
    }
    
    public void stop() {
        if (this.m_currentRunner != null) {
            this.m_currentRunner.stop();
            this.m_currentRunner = null;
        }
    }
    
    static {
        INSTANCE = new MRUDirectRunnerManager();
    }
}
