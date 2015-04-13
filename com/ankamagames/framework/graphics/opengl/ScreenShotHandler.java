package com.ankamagames.framework.graphics.opengl;

import java.io.*;
import org.jetbrains.annotations.*;

public interface ScreenShotHandler
{
    @NotNull
    File getOutputFile();
    
    void onScreenShotTook();
    
    void onScreenShotError(Exception p0);
}
