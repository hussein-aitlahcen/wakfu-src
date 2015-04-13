package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.framework.external.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.parameter.*;

public abstract class ClientEventAction implements Parameterized
{
    protected static Logger m_logger;
    private final int m_id;
    
    protected ClientEventAction(final int id) {
        super();
        this.m_id = id;
    }
    
    public abstract void execute();
    
    protected abstract void parseParameters(final ArrayList<ParserObject> p0);
    
    public boolean setParams(final ArrayList<ParserObject> params) {
        if (ParametersChecker.checkType(this, params)) {
            this.parseParameters(params);
            return true;
        }
        ClientEventAction.m_logger.error((Object)("L'action client (" + this.getClass() + ") n'a pas les param\u00e8tres du bon type id=" + this.m_id));
        return false;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    static {
        ClientEventAction.m_logger = Logger.getLogger((Class)ClientEventAction.class);
    }
}
