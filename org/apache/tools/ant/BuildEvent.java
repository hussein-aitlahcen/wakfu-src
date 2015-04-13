package org.apache.tools.ant;

import java.util.*;

public class BuildEvent extends EventObject
{
    private static final long serialVersionUID = 4538050075952288486L;
    private final Project project;
    private final Target target;
    private final Task task;
    private String message;
    private int priority;
    private Throwable exception;
    
    public BuildEvent(final Project project) {
        super(project);
        this.priority = 3;
        this.project = project;
        this.target = null;
        this.task = null;
    }
    
    public BuildEvent(final Target target) {
        super(target);
        this.priority = 3;
        this.project = target.getProject();
        this.target = target;
        this.task = null;
    }
    
    public BuildEvent(final Task task) {
        super(task);
        this.priority = 3;
        this.project = task.getProject();
        this.target = task.getOwningTarget();
        this.task = task;
    }
    
    public void setMessage(final String message, final int priority) {
        this.message = message;
        this.priority = priority;
    }
    
    public void setException(final Throwable exception) {
        this.exception = exception;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public Target getTarget() {
        return this.target;
    }
    
    public Task getTask() {
        return this.task;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public Throwable getException() {
        return this.exception;
    }
}
