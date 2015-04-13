package org.apache.tools.ant.types;

import org.apache.tools.ant.util.*;
import java.util.*;
import org.apache.tools.ant.*;

public abstract class DataType extends ProjectComponent implements Cloneable
{
    protected Reference ref;
    protected boolean checked;
    
    public DataType() {
        super();
        this.checked = true;
    }
    
    public boolean isReference() {
        return this.ref != null;
    }
    
    public void setRefid(final Reference ref) {
        this.ref = ref;
        this.checked = false;
    }
    
    protected String getDataTypeName() {
        return ComponentHelper.getElementName(this.getProject(), this, true);
    }
    
    protected void dieOnCircularReference() {
        this.dieOnCircularReference(this.getProject());
    }
    
    protected void dieOnCircularReference(final Project p) {
        if (this.checked || !this.isReference()) {
            return;
        }
        this.dieOnCircularReference(new IdentityStack<Object>(this), p);
    }
    
    protected void dieOnCircularReference(final Stack<Object> stack, final Project project) throws BuildException {
        if (this.checked || !this.isReference()) {
            return;
        }
        final Object o = this.ref.getReferencedObject(project);
        if (o instanceof DataType) {
            final IdentityStack<Object> id = IdentityStack.getInstance(stack);
            if (id.contains(o)) {
                throw this.circularReference();
            }
            id.push(o);
            ((DataType)o).dieOnCircularReference(id, project);
            id.pop();
        }
        this.checked = true;
    }
    
    public static void invokeCircularReferenceCheck(final DataType dt, final Stack<Object> stk, final Project p) {
        dt.dieOnCircularReference(stk, p);
    }
    
    public static void pushAndInvokeCircularReferenceCheck(final DataType dt, final Stack<Object> stk, final Project p) {
        stk.push(dt);
        dt.dieOnCircularReference(stk, p);
        stk.pop();
    }
    
    protected Object getCheckedRef() {
        return this.getCheckedRef(this.getProject());
    }
    
    protected Object getCheckedRef(final Project p) {
        return this.getCheckedRef((Class<Object>)this.getClass(), this.getDataTypeName(), p);
    }
    
    protected <T> T getCheckedRef(final Class<T> requiredClass, final String dataTypeName) {
        return this.getCheckedRef(requiredClass, dataTypeName, this.getProject());
    }
    
    protected <T> T getCheckedRef(final Class<T> requiredClass, final String dataTypeName, final Project project) {
        if (project == null) {
            throw new BuildException("No Project specified");
        }
        this.dieOnCircularReference(project);
        final Object o = this.ref.getReferencedObject(project);
        if (!requiredClass.isAssignableFrom(o.getClass())) {
            this.log("Class " + o.getClass() + " is not a subclass of " + requiredClass, 3);
            final String msg = this.ref.getRefId() + " doesn't denote a " + dataTypeName;
            throw new BuildException(msg);
        }
        final T result = (T)o;
        return result;
    }
    
    protected BuildException tooManyAttributes() {
        return new BuildException("You must not specify more than one attribute when using refid");
    }
    
    protected BuildException noChildrenAllowed() {
        return new BuildException("You must not specify nested elements when using refid");
    }
    
    protected BuildException circularReference() {
        return new BuildException("This data type contains a circular reference.");
    }
    
    protected boolean isChecked() {
        return this.checked;
    }
    
    protected void setChecked(final boolean checked) {
        this.checked = checked;
    }
    
    public Reference getRefid() {
        return this.ref;
    }
    
    protected void checkAttributesAllowed() {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
    }
    
    protected void checkChildrenAllowed() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
    }
    
    public String toString() {
        final String d = this.getDescription();
        return (d == null) ? this.getDataTypeName() : (this.getDataTypeName() + " " + d);
    }
    
    public Object clone() throws CloneNotSupportedException {
        final DataType dt = (DataType)super.clone();
        dt.setDescription(this.getDescription());
        if (this.getRefid() != null) {
            dt.setRefid(this.getRefid());
        }
        dt.setChecked(this.isChecked());
        return dt;
    }
}
