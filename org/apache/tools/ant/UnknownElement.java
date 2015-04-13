package org.apache.tools.ant;

import java.io.*;
import org.apache.tools.ant.taskdefs.*;
import java.util.*;

public class UnknownElement extends Task
{
    private final String elementName;
    private String namespace;
    private String qname;
    private Object realThing;
    private List<UnknownElement> children;
    private boolean presetDefed;
    
    public UnknownElement(final String elementName) {
        super();
        this.namespace = "";
        this.children = null;
        this.presetDefed = false;
        this.elementName = elementName;
    }
    
    public List<UnknownElement> getChildren() {
        return this.children;
    }
    
    public String getTag() {
        return this.elementName;
    }
    
    public String getNamespace() {
        return this.namespace;
    }
    
    public void setNamespace(String namespace) {
        if (namespace.equals("ant:current")) {
            final ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
            namespace = helper.getCurrentAntlibUri();
        }
        this.namespace = ((namespace == null) ? "" : namespace);
    }
    
    public String getQName() {
        return this.qname;
    }
    
    public void setQName(final String qname) {
        this.qname = qname;
    }
    
    public RuntimeConfigurable getWrapper() {
        return super.getWrapper();
    }
    
    public void maybeConfigure() throws BuildException {
        if (this.realThing != null) {
            return;
        }
        this.configure(this.makeObject(this, this.getWrapper()));
    }
    
    public void configure(final Object realObject) {
        if (realObject == null) {
            return;
        }
        this.realThing = realObject;
        this.getWrapper().setProxy(this.realThing);
        Task task = null;
        if (this.realThing instanceof Task) {
            task = (Task)this.realThing;
            task.setRuntimeConfigurableWrapper(this.getWrapper());
            if (this.getWrapper().getId() != null) {
                this.getOwningTarget().replaceChild(this, (Task)this.realThing);
            }
        }
        if (task != null) {
            task.maybeConfigure();
        }
        else {
            this.getWrapper().maybeConfigure(this.getProject());
        }
        this.handleChildren(this.realThing, this.getWrapper());
    }
    
    protected void handleOutput(final String output) {
        if (this.realThing instanceof Task) {
            ((Task)this.realThing).handleOutput(output);
        }
        else {
            super.handleOutput(output);
        }
    }
    
    protected int handleInput(final byte[] buffer, final int offset, final int length) throws IOException {
        if (this.realThing instanceof Task) {
            return ((Task)this.realThing).handleInput(buffer, offset, length);
        }
        return super.handleInput(buffer, offset, length);
    }
    
    protected void handleFlush(final String output) {
        if (this.realThing instanceof Task) {
            ((Task)this.realThing).handleFlush(output);
        }
        else {
            super.handleFlush(output);
        }
    }
    
    protected void handleErrorOutput(final String output) {
        if (this.realThing instanceof Task) {
            ((Task)this.realThing).handleErrorOutput(output);
        }
        else {
            super.handleErrorOutput(output);
        }
    }
    
    protected void handleErrorFlush(final String output) {
        if (this.realThing instanceof Task) {
            ((Task)this.realThing).handleErrorFlush(output);
        }
        else {
            super.handleErrorFlush(output);
        }
    }
    
    public void execute() {
        if (this.realThing == null) {
            return;
        }
        try {
            if (this.realThing instanceof Task) {
                ((Task)this.realThing).execute();
            }
        }
        finally {
            if (this.getWrapper().getId() == null) {
                this.realThing = null;
                this.getWrapper().setProxy(null);
            }
        }
    }
    
    public void addChild(final UnknownElement child) {
        if (this.children == null) {
            this.children = new ArrayList<UnknownElement>();
        }
        this.children.add(child);
    }
    
    protected void handleChildren(Object parent, final RuntimeConfigurable parentWrapper) throws BuildException {
        if (parent instanceof TypeAdapter) {
            parent = ((TypeAdapter)parent).getProxy();
        }
        final String parentUri = this.getNamespace();
        final Class<?> parentClass = parent.getClass();
        final IntrospectionHelper ih = IntrospectionHelper.getHelper(this.getProject(), parentClass);
        if (this.children != null) {
            final Iterator<UnknownElement> it = this.children.iterator();
            int i = 0;
            while (it.hasNext()) {
                final RuntimeConfigurable childWrapper = parentWrapper.getChild(i);
                final UnknownElement child = it.next();
                try {
                    if (childWrapper.isEnabled(child) || !ih.supportsNestedElement(parentUri, ProjectHelper.genComponentName(child.getNamespace(), child.getTag()))) {
                        if (!this.handleChild(parentUri, ih, parent, child, childWrapper)) {
                            if (!(parent instanceof TaskContainer)) {
                                ih.throwNotSupported(this.getProject(), parent, child.getTag());
                            }
                            else {
                                final TaskContainer container = (TaskContainer)parent;
                                container.addTask(child);
                            }
                        }
                    }
                }
                catch (UnsupportedElementException ex) {
                    throw new BuildException(parentWrapper.getElementTag() + " doesn't support the nested \"" + ex.getElement() + "\" element.", ex);
                }
                ++i;
            }
        }
    }
    
    protected String getComponentName() {
        return ProjectHelper.genComponentName(this.getNamespace(), this.getTag());
    }
    
    public void applyPreSet(final UnknownElement u) {
        if (this.presetDefed) {
            return;
        }
        this.getWrapper().applyPreSet(u.getWrapper());
        if (u.children != null) {
            final List<UnknownElement> newChildren = new ArrayList<UnknownElement>();
            newChildren.addAll(u.children);
            if (this.children != null) {
                newChildren.addAll(this.children);
            }
            this.children = newChildren;
        }
        this.presetDefed = true;
    }
    
    protected Object makeObject(final UnknownElement ue, final RuntimeConfigurable w) {
        if (!w.isEnabled(ue)) {
            return null;
        }
        final ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
        final String name = ue.getComponentName();
        Object o = helper.createComponent(ue, ue.getNamespace(), name);
        if (o == null) {
            throw this.getNotFoundException("task or type", name);
        }
        if (o instanceof PreSetDef.PreSetDefinition) {
            final PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition)o;
            o = def.createObject(ue.getProject());
            if (o == null) {
                throw this.getNotFoundException("preset " + name, def.getPreSets().getComponentName());
            }
            ue.applyPreSet(def.getPreSets());
            if (o instanceof Task) {
                final Task task = (Task)o;
                task.setTaskType(ue.getTaskType());
                task.setTaskName(ue.getTaskName());
                task.init();
            }
        }
        if (o instanceof UnknownElement) {
            o = ((UnknownElement)o).makeObject((UnknownElement)o, w);
        }
        if (o instanceof Task) {
            ((Task)o).setOwningTarget(this.getOwningTarget());
        }
        if (o instanceof ProjectComponent) {
            ((ProjectComponent)o).setLocation(this.getLocation());
        }
        return o;
    }
    
    protected Task makeTask(final UnknownElement ue, final RuntimeConfigurable w) {
        final Task task = this.getProject().createTask(ue.getTag());
        if (task != null) {
            task.setLocation(this.getLocation());
            task.setOwningTarget(this.getOwningTarget());
            task.init();
        }
        return task;
    }
    
    protected BuildException getNotFoundException(final String what, final String name) {
        final ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
        final String msg = helper.diagnoseCreationFailure(name, what);
        return new BuildException(msg, this.getLocation());
    }
    
    public String getTaskName() {
        return (this.realThing == null || !(this.realThing instanceof Task)) ? super.getTaskName() : ((Task)this.realThing).getTaskName();
    }
    
    public Task getTask() {
        if (this.realThing instanceof Task) {
            return (Task)this.realThing;
        }
        return null;
    }
    
    public Object getRealThing() {
        return this.realThing;
    }
    
    public void setRealThing(final Object realThing) {
        this.realThing = realThing;
    }
    
    private boolean handleChild(final String parentUri, final IntrospectionHelper ih, final Object parent, final UnknownElement child, final RuntimeConfigurable childWrapper) {
        final String childName = ProjectHelper.genComponentName(child.getNamespace(), child.getTag());
        if (ih.supportsNestedElement(parentUri, childName, this.getProject(), parent)) {
            IntrospectionHelper.Creator creator = null;
            try {
                creator = ih.getElementCreator(this.getProject(), parentUri, parent, childName, child);
            }
            catch (UnsupportedElementException use) {
                if (!ih.isDynamic()) {
                    throw use;
                }
                return false;
            }
            creator.setPolyType(childWrapper.getPolyType());
            Object realChild = creator.create();
            if (realChild instanceof PreSetDef.PreSetDefinition) {
                final PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition)realChild;
                realChild = creator.getRealObject();
                child.applyPreSet(def.getPreSets());
            }
            childWrapper.setCreator(creator);
            childWrapper.setProxy(realChild);
            if (realChild instanceof Task) {
                final Task childTask = (Task)realChild;
                childTask.setRuntimeConfigurableWrapper(childWrapper);
                childTask.setTaskName(childName);
                childTask.setTaskType(childName);
            }
            if (realChild instanceof ProjectComponent) {
                ((ProjectComponent)realChild).setLocation(child.getLocation());
            }
            childWrapper.maybeConfigure(this.getProject());
            child.handleChildren(realChild, childWrapper);
            creator.store();
            return true;
        }
        return false;
    }
    
    public boolean similar(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!this.getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }
        final UnknownElement other = (UnknownElement)obj;
        if (!equalsString(this.elementName, other.elementName)) {
            return false;
        }
        if (!this.namespace.equals(other.namespace)) {
            return false;
        }
        if (!this.qname.equals(other.qname)) {
            return false;
        }
        if (!this.getWrapper().getAttributeMap().equals(other.getWrapper().getAttributeMap())) {
            return false;
        }
        if (!this.getWrapper().getText().toString().equals(other.getWrapper().getText().toString())) {
            return false;
        }
        final int childrenSize = (this.children == null) ? 0 : this.children.size();
        if (childrenSize == 0) {
            return other.children == null || other.children.size() == 0;
        }
        if (other.children == null) {
            return false;
        }
        if (childrenSize != other.children.size()) {
            return false;
        }
        for (int i = 0; i < childrenSize; ++i) {
            final UnknownElement child = this.children.get(i);
            if (!child.similar(other.children.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean equalsString(final String a, final String b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public UnknownElement copy(final Project newProject) {
        final UnknownElement ret = new UnknownElement(this.getTag());
        ret.setNamespace(this.getNamespace());
        ret.setProject(newProject);
        ret.setQName(this.getQName());
        ret.setTaskType(this.getTaskType());
        ret.setTaskName(this.getTaskName());
        ret.setLocation(this.getLocation());
        if (this.getOwningTarget() == null) {
            final Target t = new Target();
            t.setProject(this.getProject());
            ret.setOwningTarget(t);
        }
        else {
            ret.setOwningTarget(this.getOwningTarget());
        }
        final RuntimeConfigurable copyRC = new RuntimeConfigurable(ret, this.getTaskName());
        copyRC.setPolyType(this.getWrapper().getPolyType());
        final Map<String, Object> m = this.getWrapper().getAttributeMap();
        for (final Map.Entry<String, Object> entry : m.entrySet()) {
            copyRC.setAttribute(entry.getKey(), entry.getValue());
        }
        copyRC.addText(this.getWrapper().getText().toString());
        final Enumeration<RuntimeConfigurable> e = this.getWrapper().getChildren();
        while (e.hasMoreElements()) {
            final RuntimeConfigurable r = e.nextElement();
            final UnknownElement ueChild = (UnknownElement)r.getProxy();
            final UnknownElement copyChild = ueChild.copy(newProject);
            copyRC.addChild(copyChild.getWrapper());
            ret.addChild(copyChild);
        }
        return ret;
    }
}
