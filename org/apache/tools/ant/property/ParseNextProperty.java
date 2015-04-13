package org.apache.tools.ant.property;

import org.apache.tools.ant.*;
import java.text.*;

public interface ParseNextProperty
{
    Project getProject();
    
    Object parseNextProperty(String p0, ParsePosition p1);
}
