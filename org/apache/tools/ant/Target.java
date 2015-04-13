package org.apache.tools.ant;

import java.util.*;
import org.apache.tools.ant.taskdefs.condition.*;
import org.apache.tools.ant.property.*;

public class Target implements TaskContainer
{
    private String name;
    private String ifString;
    private String unlessString;
    private Condition ifCondition;
    private Condition unlessCondition;
    private List<String> dependencies;
    private List<Object> children;
    private Location location;
    private Project project;
    private String description;
    
    public Target() {
        super();
        this.ifString = "";
        this.unlessString = "";
        this.dependencies = null;
        this.children = new ArrayList<Object>();
        this.location = Location.UNKNOWN_LOCATION;
        this.description = null;
    }
    
    public Target(final Target other) {
        super();
        this.ifString = "";
        this.unlessString = "";
        this.dependencies = null;
        this.children = new ArrayList<Object>();
        this.location = Location.UNKNOWN_LOCATION;
        this.description = null;
        this.name = other.name;
        this.ifString = other.ifString;
        this.unlessString = other.unlessString;
        this.ifCondition = other.ifCondition;
        this.unlessCondition = other.unlessCondition;
        this.dependencies = other.dependencies;
        this.location = other.location;
        this.project = other.project;
        this.description = other.description;
        this.children = other.children;
    }
    
    public void setProject(final Project project) {
        this.project = project;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public void setLocation(final Location location) {
        this.location = location;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setDepends(final String depS) {
        for (final String dep : parseDepends(depS, this.getName(), "depends")) {
            this.addDependency(dep);
        }
    }
    
    public static List<String> parseDepends(final String depends, final String targetName, final String attributeName) {
        final List<String> list = new ArrayList<String>();
        if (depends.length() > 0) {
            final StringTokenizer tok = new StringTokenizer(depends, ",", true);
            while (tok.hasMoreTokens()) {
                String token = tok.nextToken().trim();
                if ("".equals(token) || ",".equals(token)) {
                    throw new BuildException("Syntax Error: " + attributeName + " attribute of target \"" + targetName + "\" contains an empty string.");
                }
                list.add(token);
                if (!tok.hasMoreTokens()) {
                    continue;
                }
                token = tok.nextToken();
                if (!tok.hasMoreTokens() || !",".equals(token)) {
                    throw new BuildException("Syntax Error: " + attributeName + " attribute for target \"" + targetName + "\" ends with a \",\" " + "character");
                }
            }
        }
        return list;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addTask(final Task task) {
        this.children.add(task);
    }
    
    public void addDataType(final RuntimeConfigurable r) {
        this.children.add(r);
    }
    
    public Task[] getTasks() {
        final List<Task> tasks = new ArrayList<Task>(this.children.size());
        for (final Object o : this.children) {
            if (o instanceof Task) {
                tasks.add((Task)o);
            }
        }
        return tasks.toArray(new Task[tasks.size()]);
    }
    
    public void addDependency(final String dependency) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<String>(2);
        }
        this.dependencies.add(dependency);
    }
    
    public Enumeration<String> getDependencies() {
        return Collections.enumeration((this.dependencies == null) ? Collections.emptyList() : this.dependencies);
    }
    
    public boolean dependsOn(final String other) {
        final Project p = this.getProject();
        final Hashtable<String, Target> t = (p == null) ? null : p.getTargets();
        return p != null && p.topoSort(this.getName(), t, false).contains(t.get(other));
    }
    
    public void setIf(final String property) {
        this.ifString = ((property == null) ? "" : property);
        this.setIf(new IfStringCondition(this.ifString));
    }
    
    public String getIf() {
        return "".equals(this.ifString) ? null : this.ifString;
    }
    
    public void setIf(final Condition condition) {
        if (this.ifCondition == null) {
            this.ifCondition = condition;
        }
        else {
            final And andCondition = new And();
            andCondition.setProject(this.getProject());
            andCondition.setLocation(this.getLocation());
            andCondition.add(this.ifCondition);
            andCondition.add(condition);
            this.ifCondition = andCondition;
        }
    }
    
    public void setUnless(final String property) {
        this.unlessString = ((property == null) ? "" : property);
        this.setUnless(new UnlessStringCondition(this.unlessString));
    }
    
    public String getUnless() {
        return "".equals(this.unlessString) ? null : this.unlessString;
    }
    
    public void setUnless(final Condition condition) {
        if (this.unlessCondition == null) {
            this.unlessCondition = condition;
        }
        else {
            final Or orCondition = new Or();
            orCondition.setProject(this.getProject());
            orCondition.setLocation(this.getLocation());
            orCondition.add(this.unlessCondition);
            orCondition.add(condition);
            this.unlessCondition = orCondition;
        }
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String toString() {
        return this.name;
    }
    
    public void execute() throws BuildException {
        if (this.ifCondition != null && !this.ifCondition.eval()) {
            this.project.log(this, "Skipped because property '" + this.project.replaceProperties(this.ifString) + "' not set.", 3);
            return;
        }
        if (this.unlessCondition != null && this.unlessCondition.eval()) {
            this.project.log(this, "Skipped because property '" + this.project.replaceProperties(this.unlessString) + "' set.", 3);
            return;
        }
        final LocalProperties localProperties = LocalProperties.get(this.getProject());
        localProperties.enterScope();
        try {
            for (int i = 0; i < this.children.size(); ++i) {
                final Object o = this.children.get(i);
                if (o instanceof Task) {
                    final Task task = (Task)o;
                    task.perform();
                }
                else {
                    ((RuntimeConfigurable)o).maybeConfigure(this.project);
                }
            }
        }
        finally {
            localProperties.exitScope();
        }
    }
    
    public final void performTasks() {
        RuntimeException thrown = null;
        this.project.fireTargetStarted(this);
        try {
            this.execute();
        }
        catch (RuntimeException exc) {
            thrown = exc;
            throw exc;
        }
        finally {
            this.project.fireTargetFinished(this, thrown);
        }
    }
    
    void replaceChild(final Task el, final RuntimeConfigurable o) {
        int index;
        while ((index = this.children.indexOf(el)) >= 0) {
            this.children.set(index, o);
        }
    }
    
    void replaceChild(final Task el, final Task o) {
        int index;
        while ((index = this.children.indexOf(el)) >= 0) {
            this.children.set(index, o);
        }
    }
    
    private class IfStringCondition implements Condition
    {
        private String condition;
        
        public IfStringCondition(final String condition) {
            super();
            this.condition = condition;
        }
        
        public boolean eval() throws BuildException {
            final PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(Target.this.getProject());
            final Object o = propertyHelper.parseProperties(this.condition);
            return propertyHelper.testIfCondition(o);
        }
    }
    
    private class UnlessStringCondition implements Condition
    {
        private String condition;
        
        public UnlessStringCondition(final String condition) {
            super();
            this.condition = condition;
        }
        
        public boolean eval() throws BuildException {
            final PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(Target.this.getProject());
            final Object o = propertyHelper.parseProperties(this.condition);
            return !propertyHelper.testUnlessCondition(o);
        }
    }
}
