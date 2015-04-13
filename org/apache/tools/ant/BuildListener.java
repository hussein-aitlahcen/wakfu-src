package org.apache.tools.ant;

import java.util.*;

public interface BuildListener extends EventListener
{
    void buildStarted(BuildEvent p0);
    
    void buildFinished(BuildEvent p0);
    
    void targetStarted(BuildEvent p0);
    
    void targetFinished(BuildEvent p0);
    
    void taskStarted(BuildEvent p0);
    
    void taskFinished(BuildEvent p0);
    
    void messageLogged(BuildEvent p0);
}
