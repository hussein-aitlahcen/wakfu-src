package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.*;
import java.util.*;
import org.apache.tools.ant.taskdefs.*;

public abstract class ConditionBase extends ProjectComponent
{
    private String taskName;
    private Vector conditions;
    
    protected ConditionBase() {
        super();
        this.taskName = "condition";
        this.conditions = new Vector();
        this.taskName = "component";
    }
    
    protected ConditionBase(final String taskName) {
        super();
        this.taskName = "condition";
        this.conditions = new Vector();
        this.taskName = taskName;
    }
    
    protected int countConditions() {
        return this.conditions.size();
    }
    
    protected final Enumeration getConditions() {
        return this.conditions.elements();
    }
    
    public void setTaskName(final String name) {
        this.taskName = name;
    }
    
    public String getTaskName() {
        return this.taskName;
    }
    
    public void addAvailable(final Available a) {
        this.conditions.addElement(a);
    }
    
    public void addChecksum(final Checksum c) {
        this.conditions.addElement(c);
    }
    
    public void addUptodate(final UpToDate u) {
        this.conditions.addElement(u);
    }
    
    public void addNot(final Not n) {
        this.conditions.addElement(n);
    }
    
    public void addAnd(final And a) {
        this.conditions.addElement(a);
    }
    
    public void addOr(final Or o) {
        this.conditions.addElement(o);
    }
    
    public void addEquals(final Equals e) {
        this.conditions.addElement(e);
    }
    
    public void addOs(final Os o) {
        this.conditions.addElement(o);
    }
    
    public void addIsSet(final IsSet i) {
        this.conditions.addElement(i);
    }
    
    public void addHttp(final Http h) {
        this.conditions.addElement(h);
    }
    
    public void addSocket(final Socket s) {
        this.conditions.addElement(s);
    }
    
    public void addFilesMatch(final FilesMatch test) {
        this.conditions.addElement(test);
    }
    
    public void addContains(final Contains test) {
        this.conditions.addElement(test);
    }
    
    public void addIsTrue(final IsTrue test) {
        this.conditions.addElement(test);
    }
    
    public void addIsFalse(final IsFalse test) {
        this.conditions.addElement(test);
    }
    
    public void addIsReference(final IsReference i) {
        this.conditions.addElement(i);
    }
    
    public void addIsFileSelected(final IsFileSelected test) {
        this.conditions.addElement(test);
    }
    
    public void add(final Condition c) {
        this.conditions.addElement(c);
    }
}
