package org.apache.tools.ant.types.selectors;

import java.io.*;
import org.apache.tools.ant.*;

public interface FileSelector
{
    boolean isSelected(File p0, String p1, File p2) throws BuildException;
}
