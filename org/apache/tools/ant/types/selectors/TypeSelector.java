package org.apache.tools.ant.types.selectors;

import java.io.*;
import org.apache.tools.ant.types.*;

public class TypeSelector extends BaseExtendSelector
{
    private String type;
    public static final String TYPE_KEY = "type";
    
    public TypeSelector() {
        super();
        this.type = null;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{typeselector type: ");
        buf.append(this.type);
        buf.append("}");
        return buf.toString();
    }
    
    public void setType(final FileType fileTypes) {
        this.type = fileTypes.getValue();
    }
    
    public void setParameters(final Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                final String paramname = parameters[i].getName();
                if ("type".equalsIgnoreCase(paramname)) {
                    final FileType t = new FileType();
                    t.setValue(parameters[i].getValue());
                    this.setType(t);
                }
                else {
                    this.setError("Invalid parameter " + paramname);
                }
            }
        }
    }
    
    public void verifySettings() {
        if (this.type == null) {
            this.setError("The type attribute is required");
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        if (file.isDirectory()) {
            return this.type.equals("dir");
        }
        return this.type.equals("file");
    }
    
    public static class FileType extends EnumeratedAttribute
    {
        public static final String FILE = "file";
        public static final String DIR = "dir";
        
        public String[] getValues() {
            return new String[] { "file", "dir" };
        }
    }
}
