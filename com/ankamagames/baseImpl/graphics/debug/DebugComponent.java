package com.ankamagames.baseImpl.graphics.debug;

import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;

public interface DebugComponent
{
    void registerComponent(AbstractGameClientInstance p0);
    
    void unregisterComponent(AbstractGameClientInstance p0);
    
    JComponent getAwtComponent();
    
    String getName();
}
