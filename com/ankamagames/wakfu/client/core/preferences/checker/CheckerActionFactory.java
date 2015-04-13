package com.ankamagames.wakfu.client.core.preferences.checker;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;

public class CheckerActionFactory
{
    private static final Logger m_logger;
    static final CheckerActionFactory INSTANCE;
    private static final CheckerAction NULL_ACTION;
    private static final String TYPE_PARAM = "type";
    private static final String VERSION_PARAM = "version";
    static final String SET_PROPERTY = "setProperty";
    static final String RESET_CHAT = "resetChat";
    private final HashMap<String, CheckerAction> m_actions;
    
    private CheckerActionFactory() {
        super();
        (this.m_actions = new HashMap<String, CheckerAction>()).put("setProperty", new SetPropertyAction());
        this.m_actions.put("resetChat", new ChatResetAction());
    }
    
    CheckerAction loadAction(final DocumentEntry node, final int versionMin) {
        final DocumentEntry typeParam = node.getParameterByName("type");
        if (typeParam == null) {
            CheckerActionFactory.m_logger.warn((Object)"Action sans type");
            return CheckerActionFactory.NULL_ACTION;
        }
        final int nodeVersion = getVersion(node);
        if (nodeVersion <= versionMin) {
            return CheckerActionFactory.NULL_ACTION;
        }
        final String type = typeParam.getStringValue();
        final CheckerAction actionModel = this.m_actions.get(type);
        if (actionModel == null) {
            CheckerActionFactory.m_logger.warn((Object)("Type d'action inexistant : " + type));
            return CheckerActionFactory.NULL_ACTION;
        }
        final CheckerAction checkerAction = actionModel.newInstance();
        checkerAction.load(node);
        return checkerAction;
    }
    
    private static int getVersion(final DocumentEntry node) {
        final DocumentEntry versionParam = node.getParameterByName("version");
        if (versionParam == null) {
            CheckerActionFactory.m_logger.warn((Object)"Action sans num\u00e9ro de version");
            return -1;
        }
        return versionParam.getIntValue();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CheckerActionFactory{}");
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CheckerActionFactory.class);
        INSTANCE = new CheckerActionFactory();
        NULL_ACTION = new NullAction();
    }
}
