package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.resources.selectors.*;
import java.io.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.*;

public class WritableSelector implements FileSelector, ResourceSelector
{
    public boolean isSelected(final File basedir, final String filename, final File file) {
        return file != null && file.canWrite();
    }
    
    public boolean isSelected(final Resource r) {
        final FileProvider fp = r.as(FileProvider.class);
        return fp != null && this.isSelected(null, null, fp.getFile());
    }
}
