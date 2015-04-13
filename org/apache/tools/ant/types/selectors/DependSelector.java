package org.apache.tools.ant.types.selectors;

import java.io.*;

public class DependSelector extends MappingSelector
{
    public String toString() {
        final StringBuilder buf = new StringBuilder("{dependselector targetdir: ");
        if (this.targetdir == null) {
            buf.append("NOT YET SET");
        }
        else {
            buf.append(this.targetdir.getName());
        }
        buf.append(" granularity: ");
        buf.append(this.granularity);
        if (this.map != null) {
            buf.append(" mapper: ");
            buf.append(this.map.toString());
        }
        else if (this.mapperElement != null) {
            buf.append(" mapper: ");
            buf.append(this.mapperElement.toString());
        }
        buf.append("}");
        return buf.toString();
    }
    
    public boolean selectionTest(final File srcfile, final File destfile) {
        final boolean selected = SelectorUtils.isOutOfDate(srcfile, destfile, this.granularity);
        return selected;
    }
}
