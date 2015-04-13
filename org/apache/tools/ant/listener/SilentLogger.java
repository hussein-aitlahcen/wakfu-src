package org.apache.tools.ant.listener;

import org.apache.tools.ant.*;

public class SilentLogger extends DefaultLogger
{
    public void buildStarted(final BuildEvent event) {
    }
    
    public void buildFinished(final BuildEvent event) {
        if (event.getException() != null) {
            super.buildFinished(event);
        }
    }
    
    public void targetStarted(final BuildEvent event) {
    }
    
    public void targetFinished(final BuildEvent event) {
    }
    
    public void taskStarted(final BuildEvent event) {
    }
    
    public void taskFinished(final BuildEvent event) {
    }
}
