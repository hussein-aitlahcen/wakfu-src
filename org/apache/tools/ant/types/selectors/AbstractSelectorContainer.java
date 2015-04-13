package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.selectors.modifiedselector.*;
import java.util.*;
import org.apache.tools.ant.*;

public abstract class AbstractSelectorContainer extends DataType implements Cloneable, SelectorContainer
{
    private Vector<FileSelector> selectorsList;
    
    public AbstractSelectorContainer() {
        super();
        this.selectorsList = new Vector<FileSelector>();
    }
    
    public boolean hasSelectors() {
        if (this.isReference()) {
            return ((AbstractSelectorContainer)this.getCheckedRef()).hasSelectors();
        }
        this.dieOnCircularReference();
        return !this.selectorsList.isEmpty();
    }
    
    public int selectorCount() {
        if (this.isReference()) {
            return ((AbstractSelectorContainer)this.getCheckedRef()).selectorCount();
        }
        this.dieOnCircularReference();
        return this.selectorsList.size();
    }
    
    public FileSelector[] getSelectors(final Project p) {
        if (this.isReference()) {
            return ((AbstractSelectorContainer)this.getCheckedRef(p)).getSelectors(p);
        }
        this.dieOnCircularReference(p);
        final FileSelector[] result = new FileSelector[this.selectorsList.size()];
        this.selectorsList.copyInto(result);
        return result;
    }
    
    public Enumeration<FileSelector> selectorElements() {
        if (this.isReference()) {
            return ((AbstractSelectorContainer)this.getCheckedRef()).selectorElements();
        }
        this.dieOnCircularReference();
        return this.selectorsList.elements();
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        final Enumeration<FileSelector> e = this.selectorElements();
        if (e.hasMoreElements()) {
            while (e.hasMoreElements()) {
                buf.append(e.nextElement().toString());
                if (e.hasMoreElements()) {
                    buf.append(", ");
                }
            }
        }
        return buf.toString();
    }
    
    public void appendSelector(final FileSelector selector) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.selectorsList.addElement(selector);
        this.setChecked(false);
    }
    
    public void validate() {
        if (this.isReference()) {
            ((AbstractSelectorContainer)this.getCheckedRef()).validate();
        }
        this.dieOnCircularReference();
        final Enumeration<FileSelector> e = this.selectorElements();
        while (e.hasMoreElements()) {
            final Object o = e.nextElement();
            if (o instanceof BaseSelector) {
                ((BaseSelector)o).validate();
            }
        }
    }
    
    public void addSelector(final SelectSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addAnd(final AndSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addOr(final OrSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addNot(final NotSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addNone(final NoneSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addMajority(final MajoritySelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDate(final DateSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addSize(final SizeSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addFilename(final FilenameSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addCustom(final ExtendSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addContains(final ContainsSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addPresent(final PresentSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDepth(final DepthSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDepend(final DependSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addDifferent(final DifferentSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addType(final TypeSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addContainsRegexp(final ContainsRegexpSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addModified(final ModifiedSelector selector) {
        this.appendSelector(selector);
    }
    
    public void addReadable(final ReadableSelector r) {
        this.appendSelector(r);
    }
    
    public void addWritable(final WritableSelector w) {
        this.appendSelector(w);
    }
    
    public void add(final FileSelector selector) {
        this.appendSelector(selector);
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            for (final FileSelector fileSelector : this.selectorsList) {
                if (fileSelector instanceof DataType) {
                    DataType.pushAndInvokeCircularReferenceCheck((DataType)fileSelector, stk, p);
                }
            }
            this.setChecked(true);
        }
    }
    
    public synchronized Object clone() {
        if (this.isReference()) {
            return ((AbstractSelectorContainer)this.getCheckedRef()).clone();
        }
        try {
            final AbstractSelectorContainer sc = (AbstractSelectorContainer)super.clone();
            sc.selectorsList = new Vector<FileSelector>(this.selectorsList);
            return sc;
        }
        catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }
}
