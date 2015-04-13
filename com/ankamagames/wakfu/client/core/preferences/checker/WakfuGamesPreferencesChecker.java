package com.ankamagames.wakfu.client.core.preferences.checker;

import org.apache.log4j.*;
import java.net.*;
import com.ankamagames.baseImpl.graphics.core.*;
import java.io.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public class WakfuGamesPreferencesChecker
{
    private static final Logger m_logger;
    private static final String ACTION_TAG = "action";
    
    public static void check(final URL path, final GamePreferences preferences, final int version) {
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer container = new XMLDocumentContainer();
        try {
            final InputStream stream = path.openStream();
            accessor.open(stream);
            accessor.read(container, new DocumentEntryParser[0]);
            stream.close();
        }
        catch (Exception e) {
            WakfuGamesPreferencesChecker.m_logger.warn((Object)"Probl\u00e8me \u00e0 l'ouverture : ", (Throwable)e);
            return;
        }
        final List<CheckerAction> actions = parseCheckerFile(container, version);
        runActions(actions, preferences);
    }
    
    private static List<CheckerAction> parseCheckerFile(final XMLDocumentContainer doc, final int version) {
        final XMLDocumentNode rootNode = doc.getRootNode();
        final ArrayList<DocumentEntry> actionNodes = rootNode.getChildrenByName("action");
        final List<CheckerAction> actions = new ArrayList<CheckerAction>(actionNodes.size());
        for (int i = 0, size = actionNodes.size(); i < size; ++i) {
            final DocumentEntry node = actionNodes.get(i);
            actions.add(CheckerActionFactory.INSTANCE.loadAction(node, version));
        }
        return actions;
    }
    
    private static void runActions(final List<CheckerAction> actions, final GamePreferences preferences) {
        for (int i = 0, size = actions.size(); i < size; ++i) {
            actions.get(i).execute(preferences);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuGamesPreferencesChecker.class);
    }
}
