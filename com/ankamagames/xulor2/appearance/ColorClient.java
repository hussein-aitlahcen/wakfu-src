package com.ankamagames.xulor2.appearance;

import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;

public interface ColorClient extends SwitchClient
{
    void setColor(Color p0, @Nullable String p1);
}
