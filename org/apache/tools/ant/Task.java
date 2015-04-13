package org.apache.tools.ant;

import java.io.*;
import org.apache.tools.ant.dispatch.*;
import java.util.*;

public abstract class Task extends ProjectComponent
{
    protected Target target;
    protected String taskName;
    protected String taskType;
    protected RuntimeConfigurable wrapper;
    private boolean invalid;
    private UnknownElement replacement;
    
    public void setOwningTarget(final Target target) {
        this.target = target;
    }
    
    public Target getOwningTarget() {
        return this.target;
    }
    
    public void setTaskName(final String name) {
        this.taskName = name;
    }
    
    public String getTaskName() {
        return this.taskName;
    }
    
    public void setTaskType(final String type) {
        this.taskType = type;
    }
    
    public void init() throws BuildException {
    }
    
    public void execute() throws BuildException {
    }
    
    public RuntimeConfigurable getRuntimeConfigurableWrapper() {
        if (this.wrapper == null) {
            this.wrapper = new RuntimeConfigurable(this, this.getTaskName());
        }
        return this.wrapper;
    }
    
    public void setRuntimeConfigurableWrapper(final RuntimeConfigurable wrapper) {
        this.wrapper = wrapper;
    }
    
    public void maybeConfigure() throws BuildException {
        if (!this.invalid) {
            if (this.wrapper != null) {
                this.wrapper.maybeConfigure(this.getProject());
            }
        }
        else {
            this.getReplacement();
        }
    }
    
    public void reconfigure() {
        if (this.wrapper != null) {
            this.wrapper.reconfigure(this.getProject());
        }
    }
    
    protected void handleOutput(final String output) {
        this.log(output, 2);
    }
    
    protected void handleFlush(final String output) {
        this.handleOutput(output);
    }
    
    protected int handleInput(final byte[] buffer, final int offset, final int length) throws IOException {
        return this.getProject().defaultInput(buffer, offset, length);
    }
    
    protected void handleErrorOutput(final String output) {
        this.log(output, 1);
    }
    
    protected void handleErrorFlush(final String output) {
        this.handleErrorOutput(output);
    }
    
    public void log(final String msg) {
        this.log(msg, 2);
    }
    
    public void log(final String msg, final int msgLevel) {
        if (this.getProject() != null) {
            this.getProject().log(this, msg, msgLevel);
        }
        else {
            super.log(msg, msgLevel);
        }
    }
    
    public void log(final Throwable t, final int msgLevel) {
        if (t != null) {
            this.log(t.getMessage(), t, msgLevel);
        }
    }
    
    public void log(final String msg, final Throwable t, final int msgLevel) {
        if (this.getProject() != null) {
            this.getProject().log(this, msg, t, msgLevel);
        }
        else {
            super.log(msg, msgLevel);
        }
    }
    
    public final void perform() {
        if (!this.invalid) {
            this.getProject().fireTaskStarted(this);
            Throwable reason = null;
            try {
                this.maybeConfigure();
                DispatchUtils.execute(this);
            }
            catch (BuildException ex) {
                if (ex.getLocation() == Location.UNKNOWN_LOCATION) {
                    ex.setLocation(this.getLocation());
                }
                reason = ex;
                throw ex;
            }
            catch (Exception ex2) {
                reason = ex2;
                final BuildException be = new BuildException(ex2);
                be.setLocation(this.getLocation());
                throw be;
            }
            catch (Error ex3) {
                reason = ex3;
                throw ex3;
            }
            finally {
                this.getProject().fireTaskFinished(this, reason);
            }
        }
        else {
            final UnknownElement ue = this.getReplacement();
            final Task task = ue.getTask();
            task.perform();
        }
    }
    
    final void markInvalid() {
        this.invalid = true;
    }
    
    protected final boolean isInvalid() {
        return this.invalid;
    }
    
    private UnknownElement getReplacement() {
        if (this.replacement == null) {
            (this.replacement = new UnknownElement(this.taskType)).setProject(this.getProject());
            this.replacement.setTaskType(this.taskType);
            this.replacement.setTaskName(this.taskName);
            this.replacement.setLocation(this.getLocation());
            this.replacement.setOwningTarget(this.target);
            this.replacement.setRuntimeConfigurableWrapper(this.wrapper);
            this.wrapper.setProxy(this.replacement);
            this.replaceChildren(this.wrapper, this.replacement);
            this.target.replaceChild(this, this.replacement);
            this.replacement.maybeConfigure();
        }
        return this.replacement;
    }
    
    private void replaceChildren(final RuntimeConfigurable wrapper, final UnknownElement parentElement) {
        final Enumeration<RuntimeConfigurable> e = wrapper.getChildren();
        while (e.hasMoreElements()) {
            final RuntimeConfigurable childWrapper = e.nextElement();
            final UnknownElement childElement = new UnknownElement(childWrapper.getElementTag());
            parentElement.addChild(childElement);
            childElement.setProject(this.getProject());
            childElement.setRuntimeConfigurableWrapper(childWrapper);
            childWrapper.setProxy(childElement);
            this.replaceChildren(childWrapper, childElement);
        }
    }
    
    public String getTaskType() {
        return this.taskType;
    }
    
    protected RuntimeConfigurable getWrapper() {
        return this.wrapper;
    }
    
    public final void bindToOwner(final Task owner) {
        this.setProject(owner.getProject());
        this.setOwningTarget(owner.getOwningTarget());
        this.setTaskName(owner.getTaskName());
        this.setDescription(owner.getDescription());
        this.setLocation(owner.getLocation());
        this.setTaskType(owner.getTaskType());
    }
}
