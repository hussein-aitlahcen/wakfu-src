package org.apache.tools.ant.types.selectors;

import java.io.*;
import java.util.*;

public class NoneSelector extends BaseSelectorContainer
{
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        if (this.hasSelectors()) {
            buf.append("{noneselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        final Enumeration<FileSelector> e = this.selectorElements();
        while (e.hasMoreElements()) {
            if (e.nextElement().isSelected(basedir, filename, file)) {
                return false;
            }
        }
        return true;
    }
}
