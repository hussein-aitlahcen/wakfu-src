package com.ankamagames.wakfu.client.ui.protocol.message.havenWorld;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;

public class UIHavenWorldError extends UIMessage
{
    private static final Logger m_logger;
    private final Modification m_modification;
    private final ArrayList<ConstructionError> m_errors;
    
    public UIHavenWorldError(final Modification modification, final ArrayList<ConstructionError> errors) {
        super();
        this.m_modification = modification;
        this.m_errors = errors;
    }
    
    public Modification getModification() {
        return this.m_modification;
    }
    
    public ArrayList<ConstructionError> getErrors() {
        return this.m_errors;
    }
    
    @Override
    public int getId() {
        return 17806;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIHavenWorldModification.class);
    }
}
