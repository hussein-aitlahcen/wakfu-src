package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.*;
import java.util.*;
import org.apache.tools.ant.*;

public class ResourceSelectorContainer extends DataType
{
    private final List<ResourceSelector> resourceSelectors;
    
    public ResourceSelectorContainer() {
        super();
        this.resourceSelectors = new ArrayList<ResourceSelector>();
    }
    
    public ResourceSelectorContainer(final ResourceSelector[] r) {
        super();
        this.resourceSelectors = new ArrayList<ResourceSelector>();
        for (int i = 0; i < r.length; ++i) {
            this.add(r[i]);
        }
    }
    
    public void add(final ResourceSelector s) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (s == null) {
            return;
        }
        this.resourceSelectors.add(s);
        this.setChecked(false);
    }
    
    public boolean hasSelectors() {
        if (this.isReference()) {
            return ((ResourceSelectorContainer)this.getCheckedRef()).hasSelectors();
        }
        this.dieOnCircularReference();
        return !this.resourceSelectors.isEmpty();
    }
    
    public int selectorCount() {
        if (this.isReference()) {
            return ((ResourceSelectorContainer)this.getCheckedRef()).selectorCount();
        }
        this.dieOnCircularReference();
        return this.resourceSelectors.size();
    }
    
    public Iterator<ResourceSelector> getSelectors() {
        if (this.isReference()) {
            return ((ResourceSelectorContainer)this.getCheckedRef()).getSelectors();
        }
        this.dieOnCircularReference();
        return Collections.unmodifiableList((List<? extends ResourceSelector>)this.resourceSelectors).iterator();
    }
    
    protected void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            for (final ResourceSelector resourceSelector : this.resourceSelectors) {
                if (resourceSelector instanceof DataType) {
                    DataType.pushAndInvokeCircularReferenceCheck((DataType)resourceSelector, stk, p);
                }
            }
            this.setChecked(true);
        }
    }
}
