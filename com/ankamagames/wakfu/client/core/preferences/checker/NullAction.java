package com.ankamagames.wakfu.client.core.preferences.checker;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.baseImpl.graphics.core.*;

public class NullAction implements CheckerAction
{
    @Override
    public void load(final DocumentEntry node) {
    }
    
    @Override
    public CheckerAction newInstance() {
        return this;
    }
    
    @Override
    public void execute(final GamePreferences preferences) {
    }
}
