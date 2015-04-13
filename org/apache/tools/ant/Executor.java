package org.apache.tools.ant;

public interface Executor
{
    void executeTargets(Project p0, String[] p1) throws BuildException;
    
    Executor getSubProjectExecutor();
}
