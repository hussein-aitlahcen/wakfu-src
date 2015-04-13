package org.apache.tools.ant.taskdefs.condition;

import java.util.*;
import org.apache.tools.ant.*;

public class Or extends ConditionBase implements Condition
{
    public boolean eval() throws BuildException {
        final Enumeration e = this.getConditions();
        while (e.hasMoreElements()) {
            final Condition c = e.nextElement();
            if (c.eval()) {
                return true;
            }
        }
        return false;
    }
}
