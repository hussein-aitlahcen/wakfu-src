package org.apache.tools.ant.types.selectors;

import java.io.*;
import java.util.*;

public class MajoritySelector extends BaseSelectorContainer
{
    private boolean allowtie;
    
    public MajoritySelector() {
        super();
        this.allowtie = true;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        if (this.hasSelectors()) {
            buf.append("{majorityselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }
    
    public void setAllowtie(final boolean tiebreaker) {
        this.allowtie = tiebreaker;
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        int yesvotes = 0;
        int novotes = 0;
        final Enumeration<FileSelector> e = this.selectorElements();
        while (e.hasMoreElements()) {
            if (e.nextElement().isSelected(basedir, filename, file)) {
                ++yesvotes;
            }
            else {
                ++novotes;
            }
        }
        return yesvotes > novotes || (novotes <= yesvotes && this.allowtie);
    }
}
