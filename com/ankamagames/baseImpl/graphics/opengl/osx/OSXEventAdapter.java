package com.ankamagames.baseImpl.graphics.opengl.osx;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.lang.reflect.*;

public class OSXEventAdapter
{
    private static final Logger m_logger;
    private static final Object APPLICATION;
    
    private static Object createForegroundListener(final AppEventAdapter adapter) {
        return createListener("com.apple.eawt.AppForegroundListener", adapter);
    }
    
    private static Object createHiddenListener(final AppEventAdapter adapter) {
        return createListener("com.apple.eawt.AppHiddenListener", adapter);
    }
    
    private static Object createReopenedListener(final AppEventAdapter adapter) {
        return createListener("com.apple.eawt.AppHiddenListener", adapter);
    }
    
    private static Object createScreenSleepListener(final AppEventAdapter adapter) {
        return createListener("com.apple.eawt.ScreenSleepListener", adapter);
    }
    
    private static Object createSystemSleepListener(final AppEventAdapter adapter) {
        return createListener("com.apple.eawt.SystemSleepListener", adapter);
    }
    
    private static Object createUserSessionListener(final AppEventAdapter adapter) {
        return createListener("com.apple.eawt.UserSessionListener", adapter);
    }
    
    @Nullable
    private static Object createListener(final String className, final AppEventAdapter adapter) {
        try {
            final Class applicationListenerClass = Class.forName(className);
            return Proxy.newProxyInstance(OSXEventAdapter.class.getClassLoader(), new Class[] { applicationListenerClass }, adapter);
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    public static void registerAdapter(final AppEventAdapter adapter) {
        if (OSXEventAdapter.APPLICATION == null) {
            return;
        }
        try {
            final Class applicationClass = Class.forName("com.apple.eawt.Application");
            final Class applicationListenerClass = Class.forName("com.apple.eawt.AppEventListener");
            final Method addListenerMethod = applicationClass.getDeclaredMethod("addAppEventListener", applicationListenerClass);
            final Object foregroundListener = createForegroundListener(adapter);
            final Object hiddenListener = createHiddenListener(adapter);
            final Object reopenedListener = createReopenedListener(adapter);
            final Object screenSleepListener = createScreenSleepListener(adapter);
            final Object systemSleepListener = createSystemSleepListener(adapter);
            final Object userSessionListener = createUserSessionListener(adapter);
            if (foregroundListener != null) {
                addListenerMethod.invoke(OSXEventAdapter.APPLICATION, foregroundListener);
            }
            if (hiddenListener != null) {
                addListenerMethod.invoke(OSXEventAdapter.APPLICATION, hiddenListener);
            }
            if (reopenedListener != null) {
                addListenerMethod.invoke(OSXEventAdapter.APPLICATION, reopenedListener);
            }
            if (screenSleepListener != null) {
                addListenerMethod.invoke(OSXEventAdapter.APPLICATION, screenSleepListener);
            }
            if (systemSleepListener != null) {
                addListenerMethod.invoke(OSXEventAdapter.APPLICATION, systemSleepListener);
            }
            if (userSessionListener != null) {
                addListenerMethod.invoke(OSXEventAdapter.APPLICATION, userSessionListener);
            }
        }
        catch (ClassNotFoundException cnfe) {
            OSXEventAdapter.m_logger.warn((Object)("This version of Mac OS X does not support the Apple EAWT.  ApplicationEvent handling has been disabled (" + cnfe + ")"), (Throwable)cnfe);
        }
        catch (Exception ex) {
            OSXEventAdapter.m_logger.warn((Object)"Mac OS X Adapter could not talk to EAWT:", (Throwable)ex);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)OSXEventAdapter.class);
        Object application = null;
        try {
            final Class applicationClass = Class.forName("com.apple.eawt.Application");
            application = applicationClass.getConstructor((Class<?>[])null).newInstance((Object[])null);
        }
        catch (ClassNotFoundException cnfe) {
            OSXEventAdapter.m_logger.warn((Object)("This version of Mac OS X does not support the Apple EAWT.  ApplicationEvent handling has been disabled (" + cnfe + ")"), (Throwable)cnfe);
        }
        catch (Exception ex) {
            OSXEventAdapter.m_logger.warn((Object)"Mac OS X Adapter could not talk to EAWT:", (Throwable)ex);
        }
        APPLICATION = application;
    }
}
