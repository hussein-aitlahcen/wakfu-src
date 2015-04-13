package org.apache.tools.ant.types;

import org.apache.tools.ant.filters.*;
import org.apache.tools.ant.*;
import java.util.*;

public class FilterChain extends DataType implements Cloneable
{
    private Vector<Object> filterReaders;
    
    public FilterChain() {
        super();
        this.filterReaders = new Vector<Object>();
    }
    
    public void addFilterReader(final AntFilterReader filterReader) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filterReader);
    }
    
    public Vector<Object> getFilterReaders() {
        if (this.isReference()) {
            return ((FilterChain)this.getCheckedRef()).getFilterReaders();
        }
        this.dieOnCircularReference();
        return this.filterReaders;
    }
    
    public void addClassConstants(final ClassConstants classConstants) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(classConstants);
    }
    
    public void addExpandProperties(final ExpandProperties expandProperties) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(expandProperties);
    }
    
    public void addHeadFilter(final HeadFilter headFilter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(headFilter);
    }
    
    public void addLineContains(final LineContains lineContains) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(lineContains);
    }
    
    public void addLineContainsRegExp(final LineContainsRegExp lineContainsRegExp) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(lineContainsRegExp);
    }
    
    public void addPrefixLines(final PrefixLines prefixLines) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(prefixLines);
    }
    
    public void addSuffixLines(final SuffixLines suffixLines) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(suffixLines);
    }
    
    public void addReplaceTokens(final ReplaceTokens replaceTokens) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(replaceTokens);
    }
    
    public void addStripJavaComments(final StripJavaComments stripJavaComments) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(stripJavaComments);
    }
    
    public void addStripLineBreaks(final StripLineBreaks stripLineBreaks) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(stripLineBreaks);
    }
    
    public void addStripLineComments(final StripLineComments stripLineComments) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(stripLineComments);
    }
    
    public void addTabsToSpaces(final TabsToSpaces tabsToSpaces) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(tabsToSpaces);
    }
    
    public void addTailFilter(final TailFilter tailFilter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(tailFilter);
    }
    
    public void addEscapeUnicode(final EscapeUnicode escapeUnicode) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(escapeUnicode);
    }
    
    public void addTokenFilter(final TokenFilter tokenFilter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(tokenFilter);
    }
    
    public void addDeleteCharacters(final TokenFilter.DeleteCharacters filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    public void addContainsRegex(final TokenFilter.ContainsRegex filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    public void addReplaceRegex(final TokenFilter.ReplaceRegex filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    public void addTrim(final TokenFilter.Trim filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    public void addReplaceString(final TokenFilter.ReplaceString filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    public void addIgnoreBlank(final TokenFilter.IgnoreBlank filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    public void setRefid(final Reference r) throws BuildException {
        if (!this.filterReaders.isEmpty()) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public void add(final ChainableReader filter) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        this.setChecked(false);
        this.filterReaders.addElement(filter);
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            for (final Object o : this.filterReaders) {
                if (o instanceof DataType) {
                    DataType.pushAndInvokeCircularReferenceCheck((DataType)o, stk, p);
                }
            }
            this.setChecked(true);
        }
    }
}
