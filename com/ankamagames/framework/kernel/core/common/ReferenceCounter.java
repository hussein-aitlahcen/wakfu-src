package com.ankamagames.framework.kernel.core.common;

import org.apache.log4j.*;
import javax.swing.*;
import java.awt.*;

public abstract class ReferenceCounter
{
    private static final Logger m_logger;
    public static final String DELETED_ITEM_ACCESS_MESSAGE = "Using an item with a reference counter < 0 is forbidden";
    private static final short DEFAULT_LIFE = 10;
    private static final int MAX_REF_COUNT = 2147483646;
    private int m_numReferences;
    
    public ReferenceCounter() {
        super();
        this.m_numReferences = 0;
    }
    
    public final boolean exists() {
        return this.m_numReferences >= 0;
    }
    
    public void addReference() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        assert this.m_numReferences < Integer.MAX_VALUE : "Too many references added";
        if (this.m_numReferences >= 2147483646) {
            if (this.m_numReferences == 2147483646) {
                ReferenceCounter.m_logger.fatal((Object)("Too many references added " + this.getClass().getName()), (Throwable)new Exception());
                JOptionPane.showMessageDialog(null, "Fatal error: Too many references added " + this.getClass().getName());
                ++this.m_numReferences;
            }
        }
        else {
            ++this.m_numReferences;
        }
    }
    
    public void removeReference() {
        final int numReferences = this.m_numReferences - 1;
        this.m_numReferences = numReferences;
        if (numReferences == -1) {
            this.onNegativeNumReferences();
        }
        if (this.m_numReferences == -2) {
            ReferenceCounter.m_logger.warn((Object)("on enl\u00e8ve encore une reference " + this.getClass().getSimpleName()));
        }
    }
    
    public final int getNumReferences() {
        return this.m_numReferences;
    }
    
    protected void delete() {
    }
    
    protected void onNegativeNumReferences() {
        this.delete();
    }
    
    final void resetReferences() {
        this.m_numReferences = 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceCounter.class);
    }
}
