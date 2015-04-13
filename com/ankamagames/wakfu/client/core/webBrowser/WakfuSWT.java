package com.ankamagames.wakfu.client.core.webBrowser;

import org.apache.log4j.*;
import org.eclipse.swt.widgets.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class WakfuSWT
{
    static final String XULRUNNER_RELATIVE_PATH;
    private static final Logger m_logger;
    private static Display m_display;
    private static boolean m_init;
    
    public static boolean isInit() {
        return WakfuSWT.m_init;
    }
    
    public static void initDisplay() {
        assert WakfuSWT.m_display == null;
        if (OS.isMacOs()) {
            try {
                runWithMacExecutor(new Runnable() {
                    @Override
                    public void run() {
                        WakfuSWT.m_display = new Display();
                    }
                });
                WakfuSWT.m_init = true;
            }
            catch (IllegalStateException e) {
                WakfuSWT.m_logger.warn((Object)"Impossible de cr\u00e9er le Display : ", (Throwable)e);
                WakfuSWT.m_init = false;
            }
        }
        else {
            try {
                WakfuSWT.m_display = new Display();
                WakfuSWT.m_init = true;
            }
            catch (Throwable t) {
                WakfuSWT.m_logger.warn((Object)"Impossible de cr\u00e9er le Display : ", t);
                WakfuSWT.m_init = false;
            }
        }
    }
    
    public static void runAsync(final Runnable runnable) {
        if (WakfuSWT.m_init && WakfuSWT.m_display != null) {
            WakfuSWT.m_display.asyncExec(runnable);
        }
    }
    
    public static void runEventPump() {
        if (!WakfuSWT.m_init) {
            return;
        }
        if (OS.isMacOs()) {
            runWithMacExecutor(new Runnable() {
                @Override
                public void run() {
                    _runEventPump();
                }
            });
        }
        else {
            _runEventPump();
        }
    }
    
    private static void _runEventPump() {
        while (!WakfuSWT.m_display.isDisposed()) {
            try {
                if (WakfuSWT.m_display.readAndDispatch()) {
                    continue;
                }
                WakfuSWT.m_display.sleep();
            }
            catch (Exception e) {
                WakfuSWT.m_logger.error((Object)"kaboom", (Throwable)e);
            }
        }
    }
    
    public static String getXulRunnerPath() {
        final String paths = System.getProperty("java.library.path");
        final String[] arr$;
        final String[] pathArray = arr$ = paths.split(File.pathSeparator);
        for (final String path : arr$) {
            final File xulPath = new File(path + WakfuSWT.XULRUNNER_RELATIVE_PATH);
            if (xulPath.exists()) {
                try {
                    return xulPath.getCanonicalPath();
                }
                catch (IOException e) {
                    throw new IllegalArgumentException("Invalide XulRunnerPath : " + xulPath);
                }
            }
        }
        return null;
    }
    
    public static void setXulRunnerPath() {
        final String xulRunnerPath = getXulRunnerPath();
        if (xulRunnerPath == null) {
            throw new IllegalArgumentException("Impossible de trouver le path xulRunner");
        }
        System.setProperty("sun.awt.xembedserver", "true");
        System.setProperty("org.eclipse.swt.browser.XULRunnerPath", xulRunnerPath);
        WakfuSWT.m_logger.info((Object)("xul runner path : " + xulRunnerPath));
        System.setProperty("MOZ_PLUGIN_PATH", xulRunnerPath + File.separator + "plugins");
    }
    
    public static boolean isXulRunnerPathSet() {
        final String property = System.getProperty("org.eclipse.swt.browser.XULRunnerPath");
        return property != null && property.length() != 0;
    }
    
    private static void runWithMacExecutor(final Runnable runnable) {
        Executor mainQueueExecutor;
        try {
            final Object dispatch = Class.forName("com.apple.concurrent.Dispatch").getMethod("getInstance", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            mainQueueExecutor = (Executor)dispatch.getClass().getMethod("getNonBlockingMainQueueExecutor", (Class<?>[])new Class[0]).invoke(dispatch, new Object[0]);
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to use the Mac Dispatch executor. This may happen if the version of Java that is used is too old.", e);
        }
        final AtomicBoolean isExecutorCallComplete = new AtomicBoolean(false);
        final AtomicReference<Throwable> exceptionReference = new AtomicReference<Throwable>();
        synchronized (isExecutorCallComplete) {
            mainQueueExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    }
                    catch (Throwable t) {
                        exceptionReference.set(t);
                        synchronized (isExecutorCallComplete) {
                            isExecutorCallComplete.set(true);
                            isExecutorCallComplete.notify();
                        }
                    }
                    finally {
                        synchronized (isExecutorCallComplete) {
                            isExecutorCallComplete.set(true);
                            isExecutorCallComplete.notify();
                        }
                    }
                }
            });
            while (!isExecutorCallComplete.get()) {
                try {
                    isExecutorCallComplete.wait();
                }
                catch (InterruptedException e2) {}
            }
        }
        final Throwable throwable = exceptionReference.get();
        if (throwable != null) {
            WakfuSWT.m_logger.error((Object)"Erreur dans le thread SWT : ", throwable);
        }
    }
    
    static {
        XULRUNNER_RELATIVE_PATH = File.separator + "xulrunner-" + OS.getCurrentOS().getName();
        m_logger = Logger.getLogger((Class)WakfuSWT.class);
        WakfuSWT.m_display = null;
        WakfuSWT.m_init = false;
    }
}
