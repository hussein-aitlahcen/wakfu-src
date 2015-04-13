package org.apache.tools.ant.helper;

import org.apache.tools.ant.*;

public class SingleCheckExecutor implements Executor
{
    public void executeTargets(final Project project, final String[] targetNames) throws BuildException {
        project.executeSortedTargets(project.topoSort(targetNames, project.getTargets(), false));
    }
    
    public Executor getSubProjectExecutor() {
        return this;
    }
}
