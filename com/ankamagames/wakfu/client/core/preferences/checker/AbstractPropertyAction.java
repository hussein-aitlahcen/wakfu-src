package com.ankamagames.wakfu.client.core.preferences.checker;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.baseImpl.graphics.core.*;

public abstract class AbstractPropertyAction implements CheckerAction
{
    private static final Logger m_logger;
    private static final String NAME = "name";
    protected String m_name;
    
    @Override
    public void load(final DocumentEntry node) {
        final DocumentEntry nameParam = node.getParameterByName("name");
        if (nameParam != null) {
            this.m_name = nameParam.getStringValue();
        }
    }
    
    @Override
    public void execute(final GamePreferences preferences) {
        final KeyInterface keyInterface = preferences.getKeyInterface(this.m_name);
        if (keyInterface == null) {
            AbstractPropertyAction.m_logger.warn((Object)("Impossible de trouver la pr\u00e9f\u00e9rence " + this.m_name));
            return;
        }
        this.doExecute(keyInterface, preferences);
    }
    
    protected abstract void doExecute(final KeyInterface p0, final GamePreferences p1);
    
    static {
        m_logger = Logger.getLogger((Class)AbstractPropertyAction.class);
    }
}
