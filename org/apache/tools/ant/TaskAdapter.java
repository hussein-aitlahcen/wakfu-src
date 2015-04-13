package org.apache.tools.ant;

import java.lang.reflect.*;
import org.apache.tools.ant.dispatch.*;

public class TaskAdapter extends Task implements TypeAdapter
{
    private Object proxy;
    
    public TaskAdapter() {
        super();
    }
    
    public TaskAdapter(final Object proxy) {
        this();
        this.setProxy(proxy);
    }
    
    public static void checkTaskClass(final Class<?> taskClass, final Project project) {
        if (!Dispatchable.class.isAssignableFrom(taskClass)) {
            try {
                final Method executeM = taskClass.getMethod("execute", (Class<?>[])null);
                if (!Void.TYPE.equals(executeM.getReturnType())) {
                    final String message = "return type of execute() should be void but was \"" + executeM.getReturnType() + "\" in " + taskClass;
                    project.log(message, 1);
                }
            }
            catch (NoSuchMethodException e2) {
                final String message = "No public execute() in " + taskClass;
                project.log(message, 0);
                throw new BuildException(message);
            }
            catch (LinkageError e) {
                final String message = "Could not load " + taskClass + ": " + e;
                project.log(message, 0);
                throw new BuildException(message, e);
            }
        }
    }
    
    public void checkProxyClass(final Class<?> proxyClass) {
        checkTaskClass(proxyClass, this.getProject());
    }
    
    public void execute() throws BuildException {
        try {
            final Method setLocationM = this.proxy.getClass().getMethod("setLocation", Location.class);
            if (setLocationM != null) {
                setLocationM.invoke(this.proxy, this.getLocation());
            }
        }
        catch (NoSuchMethodException e) {}
        catch (Exception ex) {
            this.log("Error setting location in " + this.proxy.getClass(), 0);
            throw new BuildException(ex);
        }
        try {
            final Method setProjectM = this.proxy.getClass().getMethod("setProject", Project.class);
            if (setProjectM != null) {
                setProjectM.invoke(this.proxy, this.getProject());
            }
        }
        catch (NoSuchMethodException e) {}
        catch (Exception ex) {
            this.log("Error setting project in " + this.proxy.getClass(), 0);
            throw new BuildException(ex);
        }
        try {
            DispatchUtils.execute(this.proxy);
        }
        catch (BuildException be) {
            throw be;
        }
        catch (Exception ex) {
            this.log("Error in " + this.proxy.getClass(), 3);
            throw new BuildException(ex);
        }
    }
    
    public void setProxy(final Object o) {
        this.proxy = o;
    }
    
    public Object getProxy() {
        return this.proxy;
    }
}
