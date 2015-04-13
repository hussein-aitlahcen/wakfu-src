package org.apache.tools.ant;

public abstract class ProjectComponent implements Cloneable
{
    protected Project project;
    protected Location location;
    protected String description;
    
    public ProjectComponent() {
        super();
        this.location = Location.UNKNOWN_LOCATION;
    }
    
    public void setProject(final Project project) {
        this.project = project;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(final Location location) {
        this.location = location;
    }
    
    public void setDescription(final String desc) {
        this.description = desc;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void log(final String msg) {
        this.log(msg, 2);
    }
    
    public void log(final String msg, final int msgLevel) {
        if (this.getProject() != null) {
            this.getProject().log(msg, msgLevel);
        }
        else if (msgLevel <= 2) {
            System.err.println(msg);
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final ProjectComponent pc = (ProjectComponent)super.clone();
        pc.setLocation(this.getLocation());
        pc.setProject(this.getProject());
        return pc;
    }
}
