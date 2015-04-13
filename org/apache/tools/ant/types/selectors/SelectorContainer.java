package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.*;
import java.util.*;
import org.apache.tools.ant.types.selectors.modifiedselector.*;

public interface SelectorContainer
{
    boolean hasSelectors();
    
    int selectorCount();
    
    FileSelector[] getSelectors(Project p0);
    
    Enumeration<FileSelector> selectorElements();
    
    void appendSelector(FileSelector p0);
    
    void addSelector(SelectSelector p0);
    
    void addAnd(AndSelector p0);
    
    void addOr(OrSelector p0);
    
    void addNot(NotSelector p0);
    
    void addNone(NoneSelector p0);
    
    void addMajority(MajoritySelector p0);
    
    void addDate(DateSelector p0);
    
    void addSize(SizeSelector p0);
    
    void addFilename(FilenameSelector p0);
    
    void addCustom(ExtendSelector p0);
    
    void addContains(ContainsSelector p0);
    
    void addPresent(PresentSelector p0);
    
    void addDepth(DepthSelector p0);
    
    void addDepend(DependSelector p0);
    
    void addContainsRegexp(ContainsRegexpSelector p0);
    
    void addType(TypeSelector p0);
    
    void addDifferent(DifferentSelector p0);
    
    void addModified(ModifiedSelector p0);
    
    void add(FileSelector p0);
}
