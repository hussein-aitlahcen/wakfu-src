package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

public class PreSetDef extends AntlibDefinition implements TaskContainer
{
    private UnknownElement nestedTask;
    private String name;
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void addTask(final Task nestedTask) {
        if (this.nestedTask != null) {
            throw new BuildException("Only one nested element allowed");
        }
        if (!(nestedTask instanceof UnknownElement)) {
            throw new BuildException("addTask called with a task that is not an unknown element");
        }
        this.nestedTask = (UnknownElement)nestedTask;
    }
    
    public void execute() {
        if (this.nestedTask == null) {
            throw new BuildException("Missing nested element");
        }
        if (this.name == null) {
            throw new BuildException("Name not specified");
        }
        this.name = ProjectHelper.genComponentName(this.getURI(), this.name);
        final ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
        final String componentName = ProjectHelper.genComponentName(this.nestedTask.getNamespace(), this.nestedTask.getTag());
        final AntTypeDefinition def = helper.getDefinition(componentName);
        if (def == null) {
            throw new BuildException("Unable to find typedef " + componentName);
        }
        final PreSetDefinition newDef = new PreSetDefinition(def, this.nestedTask);
        newDef.setName(this.name);
        helper.addDataTypeDefinition(newDef);
        this.log("defining preset " + this.name, 3);
    }
    
    public static class PreSetDefinition extends AntTypeDefinition
    {
        private AntTypeDefinition parent;
        private UnknownElement element;
        
        public PreSetDefinition(AntTypeDefinition parent, final UnknownElement el) {
            super();
            if (parent instanceof PreSetDefinition) {
                final PreSetDefinition p = (PreSetDefinition)parent;
                el.applyPreSet(p.element);
                parent = p.parent;
            }
            this.parent = parent;
            this.element = el;
        }
        
        public void setClass(final Class clazz) {
            throw new BuildException("Not supported");
        }
        
        public void setClassName(final String className) {
            throw new BuildException("Not supported");
        }
        
        public String getClassName() {
            return this.parent.getClassName();
        }
        
        public void setAdapterClass(final Class adapterClass) {
            throw new BuildException("Not supported");
        }
        
        public void setAdaptToClass(final Class adaptToClass) {
            throw new BuildException("Not supported");
        }
        
        public void setClassLoader(final ClassLoader classLoader) {
            throw new BuildException("Not supported");
        }
        
        public ClassLoader getClassLoader() {
            return this.parent.getClassLoader();
        }
        
        public Class getExposedClass(final Project project) {
            return this.parent.getExposedClass(project);
        }
        
        public Class getTypeClass(final Project project) {
            return this.parent.getTypeClass(project);
        }
        
        public void checkClass(final Project project) {
            this.parent.checkClass(project);
        }
        
        public Object createObject(final Project project) {
            return this.parent.create(project);
        }
        
        public UnknownElement getPreSets() {
            return this.element;
        }
        
        public Object create(final Project project) {
            return this;
        }
        
        public boolean sameDefinition(final AntTypeDefinition other, final Project project) {
            return other != null && other.getClass() == this.getClass() && this.parent != null && this.parent.sameDefinition(((PreSetDefinition)other).parent, project) && this.element.similar(((PreSetDefinition)other).element);
        }
        
        public boolean similarDefinition(final AntTypeDefinition other, final Project project) {
            return other != null && other.getClass().getName().equals(this.getClass().getName()) && this.parent != null && this.parent.similarDefinition(((PreSetDefinition)other).parent, project) && this.element.similar(((PreSetDefinition)other).element);
        }
    }
}
