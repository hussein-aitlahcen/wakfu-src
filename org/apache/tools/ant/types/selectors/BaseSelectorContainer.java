package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.*;
import java.io.*;
import org.apache.tools.ant.types.selectors.modifiedselector.*;
import org.apache.tools.ant.types.*;
import java.util.*;

public abstract class BaseSelectorContainer extends BaseSelector implements SelectorContainer
{
    private Vector<FileSelector> selectorsList;
    
    public BaseSelectorContainer() {
        super();
        this.selectorsList = new Vector<FileSelector>();
    }
    
    public boolean hasSelectors() {
        this.dieOnCircularReference();
        return !this.selectorsList.isEmpty();
    }
    
    public int selectorCount() {
        this.dieOnCircularReference();
        return this.selectorsList.size();
    }
    
    public FileSelector[] getSelectors(final Project p) {
        this.dieOnCircularReference();
        final FileSelector[] result = new FileSelector[this.selectorsList.size()];
        this.selectorsList.copyInto(result);
        return result;
    }
    
    public Enumeration<FileSelector> selectorElements() {
        this.dieOnCircularReference();
        return this.selectorsList.elements();
    }
    
    public String toString() {
        this.dieOnCircularReference();
        final StringBuilder buf = new StringBuilder();
        final Enumeration<FileSelector> e = this.selectorElements();
        while (e.hasMoreElements()) {
            buf.append(e.nextElement().toString());
            if (e.hasMoreElements()) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }
    
    public void appendSelector(final FileSelector selector) {
        this.selectorsList.addElement(selector);
        this.setChecked(false);
    }
    
    public void validate() {
        this.verifySettings();
        this.dieOnCircularReference();
        final String errmsg = this.getError();
        if (errmsg != null) {
            throw new BuildException(errmsg);
        }
        final Enumeration<FileSelector> e = this.selectorElements();
        while (e.hasMoreElements()) {
            final Object o = e.nextElement();
            if (o instanceof BaseSelector) {
                ((BaseSelector)o).validate();
            }
        }
    }
    
    public abstract boolean isSelected(final File p0, final String p1, final File p2);
    
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
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
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
}
