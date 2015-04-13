package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.*;

public interface Condition
{
    boolean eval() throws BuildException;
}
