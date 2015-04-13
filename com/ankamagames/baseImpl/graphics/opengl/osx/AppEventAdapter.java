package com.ankamagames.baseImpl.graphics.opengl.osx;

import java.lang.reflect.*;

public class AppEventAdapter implements InvocationHandler
{
    public void appMovedToBackground() {
    }
    
    public void appRaisedToForeground() {
    }
    
    public void appHidden() {
    }
    
    public void appUnHidden() {
    }
    
    public void appReOpened() {
    }
    
    public void screenAboutToSleep() {
    }
    
    public void screenAwoke() {
    }
    
    public void systemAboutToSleep() {
    }
    
    public void systemAwoke() {
    }
    
    public void userSessionActived() {
    }
    
    public void userSessionDeactivated() {
    }
    
    private void callTarget(final String functionName) {
        if (functionName == null) {
            return;
        }
        if ("appMovedToBackground".equals(functionName)) {
            this.appMovedToBackground();
        }
        else if ("appRaisedToForeground".equals(functionName)) {
            this.appRaisedToForeground();
        }
        else if ("appHidden".equals(functionName)) {
            this.appHidden();
        }
        else if ("appUnhidden".equals(functionName)) {
            this.appUnHidden();
        }
        else if ("appReOpened".equals(functionName)) {
            this.appReOpened();
        }
        else if ("screenAboutToSleep".equals(functionName)) {
            this.screenAboutToSleep();
        }
        else if ("screenAwoke".equals(functionName)) {
            this.screenAwoke();
        }
        else if ("systemAboutToSleep".equals(functionName)) {
            this.systemAboutToSleep();
        }
        else if ("systemAwoke".equals(functionName)) {
            this.systemAwoke();
        }
        else if ("userSessionActivated".equals(functionName)) {
            this.userSessionActived();
        }
        else if ("userSessionDeactivated".equals(functionName)) {
            this.userSessionDeactivated();
        }
    }
    
    @Override
    public final Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (this.isCorrectMethod(method, args)) {
            this.callTarget(method.getName());
        }
        return null;
    }
    
    private boolean isCorrectMethod(final Method method, final Object[] args) {
        return true;
    }
}
