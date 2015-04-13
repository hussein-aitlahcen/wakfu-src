package com.ankamagames.wakfu.client.core.preferences.checker;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.baseImpl.graphics.core.*;

public interface CheckerAction
{
    void load(DocumentEntry p0);
    
    CheckerAction newInstance();
    
    void execute(GamePreferences p0);
}
