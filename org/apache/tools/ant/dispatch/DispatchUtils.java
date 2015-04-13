package org.apache.tools.ant.dispatch;

import org.apache.tools.ant.*;
import java.lang.reflect.*;

public class DispatchUtils
{
    public static final void execute(final Object task) throws BuildException {
        String methodName = "execute";
        Dispatchable dispatchable = null;
        try {
            if (task instanceof Dispatchable) {
                dispatchable = (Dispatchable)task;
            }
            else if (task instanceof UnknownElement) {
                final UnknownElement ue = (UnknownElement)task;
                final Object realThing = ue.getRealThing();
                if (realThing != null && realThing instanceof Dispatchable && realThing instanceof Task) {
                    dispatchable = (Dispatchable)realThing;
                }
            }
            if (dispatchable != null) {
                String mName = null;
                try {
                    final String name = dispatchable.getActionParameterName();
                    if (name == null || name.trim().length() <= 0) {
                        throw new BuildException("Action Parameter Name must not be empty for Dispatchable Task.");
                    }
                    mName = "get" + name.trim().substring(0, 1).toUpperCase();
                    if (name.length() > 1) {
                        mName += name.substring(1);
                    }
                    final Class<? extends Dispatchable> c = dispatchable.getClass();
                    final Method actionM = c.getMethod(mName, (Class<?>[])new Class[0]);
                    if (actionM != null) {
                        final Object o = actionM.invoke(dispatchable, (Object[])null);
                        if (o == null) {
                            throw new BuildException("Dispatchable Task attribute '" + name.trim() + "' not set or value is empty.");
                        }
                        final String s = o.toString();
                        if (s == null || s.trim().length() <= 0) {
                            throw new BuildException("Dispatchable Task attribute '" + name.trim() + "' not set or value is empty.");
                        }
                        methodName = s.trim();
                        Method executeM = null;
                        executeM = dispatchable.getClass().getMethod(methodName, (Class<?>[])new Class[0]);
                        if (executeM == null) {
                            throw new BuildException("No public " + methodName + "() in " + dispatchable.getClass());
                        }
                        executeM.invoke(dispatchable, (Object[])null);
                        if (task instanceof UnknownElement) {
                            ((UnknownElement)task).setRealThing(null);
                        }
                    }
                }
                catch (NoSuchMethodException nsme) {
                    throw new BuildException("No public " + mName + "() in " + task.getClass());
                }
            }
            else {
                Method executeM2 = null;
                executeM2 = task.getClass().getMethod(methodName, (Class<?>[])new Class[0]);
                if (executeM2 == null) {
                    throw new BuildException("No public " + methodName + "() in " + task.getClass());
                }
                executeM2.invoke(task, (Object[])null);
                if (task instanceof UnknownElement) {
                    ((UnknownElement)task).setRealThing(null);
                }
            }
        }
        catch (InvocationTargetException ie) {
            final Throwable t = ie.getTargetException();
            if (t instanceof BuildException) {
                throw (BuildException)t;
            }
            throw new BuildException(t);
        }
        catch (NoSuchMethodException e) {
            throw new BuildException(e);
        }
        catch (IllegalAccessException e2) {
            throw new BuildException(e2);
        }
    }
}
