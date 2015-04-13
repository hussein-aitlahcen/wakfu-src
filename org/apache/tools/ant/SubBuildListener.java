package org.apache.tools.ant;

public interface SubBuildListener extends BuildListener
{
    void subBuildStarted(BuildEvent p0);
    
    void subBuildFinished(BuildEvent p0);
}
