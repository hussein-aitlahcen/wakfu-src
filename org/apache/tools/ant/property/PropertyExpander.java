package org.apache.tools.ant.property;

import org.apache.tools.ant.*;
import java.text.*;

public interface PropertyExpander extends PropertyHelper.Delegate
{
    String parsePropertyName(String p0, ParsePosition p1, ParseNextProperty p2);
}
