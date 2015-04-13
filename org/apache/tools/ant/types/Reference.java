package org.apache.tools.ant.types;

import org.apache.tools.ant.*;

public class Reference
{
    private String refid;
    private Project project;
    
    public Reference() {
        super();
    }
    
    public Reference(final String id) {
        super();
        this.setRefId(id);
    }
    
    public Reference(final Project p, final String id) {
        super();
        this.setRefId(id);
        this.setProject(p);
    }
    
    public void setRefId(final String id) {
        this.refid = id;
    }
    
    public String getRefId() {
        return this.refid;
    }
    
    public void setProject(final Project p) {
        this.project = p;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public Object getReferencedObject(final Project fallback) throws BuildException {
        if (this.refid == null) {
            throw new BuildException("No reference specified");
        }
        final Object o = (this.project == null) ? fallback.getReference(this.refid) : this.project.getReference(this.refid);
        if (o == null) {
            throw new BuildException("Reference " + this.refid + " not found.");
        }
        return o;
    }
    
    public Object getReferencedObject() throws BuildException {
        if (this.project == null) {
            throw new BuildException("No project set on reference to " + this.refid);
        }
        return this.getReferencedObject(this.project);
    }
}
