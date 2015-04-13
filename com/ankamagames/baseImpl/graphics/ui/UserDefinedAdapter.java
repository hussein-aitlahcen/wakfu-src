package com.ankamagames.baseImpl.graphics.ui;

import com.ankamagames.framework.preferences.*;
import org.jetbrains.annotations.*;

public interface UserDefinedAdapter
{
    @Nullable
    PreferenceStore getPreferenceStoreForDialog(String p0);
}
