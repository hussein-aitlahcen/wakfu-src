package com.ankamagames.wakfu.client.core.world.havenWorld.buildings;

import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.building.*;

public class EditorGroupMapLibrary extends AbstractEditorGroupMapLibrary<EditorGroupMap>
{
    private static final Logger m_logger;
    public static final EditorGroupMapLibrary INSTANCE;
    
    @Override
    protected EditorGroupMap createBuilding(final int groupId) throws IOException {
        return new EditorGroupMap(groupId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)EditorGroupMapLibrary.class);
        INSTANCE = new EditorGroupMapLibrary();
    }
}
