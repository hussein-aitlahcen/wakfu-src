package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.io.*;

public class StringResource extends Resource
{
    private static final int STRING_MAGIC;
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;
    
    public StringResource() {
        super();
        this.encoding = "UTF-8";
    }
    
    public StringResource(final String value) {
        this(null, value);
    }
    
    public StringResource(final Project project, final String value) {
        super();
        this.encoding = "UTF-8";
        this.setProject(project);
        this.setValue((project == null) ? value : project.replaceProperties(value));
    }
    
    public synchronized void setName(final String s) {
        if (this.getName() != null) {
            throw new BuildException(new ImmutableResourceException());
        }
        super.setName(s);
    }
    
    public synchronized void setValue(final String s) {
        this.setName(s);
    }
    
    public synchronized String getName() {
        return super.getName();
    }
    
    public synchronized String getValue() {
        return this.getName();
    }
    
    public boolean isExists() {
        return this.getValue() != null;
    }
    
    public void addText(final String text) {
        this.checkChildrenAllowed();
        this.setValue(this.getProject().replaceProperties(text));
    }
    
    public synchronized void setEncoding(final String s) {
        this.checkAttributesAllowed();
        this.encoding = s;
    }
    
    public synchronized String getEncoding() {
        return this.encoding;
    }
    
    public synchronized long getSize() {
        return this.isReference() ? ((Resource)this.getCheckedRef()).getSize() : this.getContent().length();
    }
    
    public synchronized int hashCode() {
        if (this.isReference()) {
            return this.getCheckedRef().hashCode();
        }
        return super.hashCode() * StringResource.STRING_MAGIC;
    }
    
    public String toString() {
        return String.valueOf(this.getContent());
    }
    
    public synchronized InputStream getInputStream() throws IOException {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getInputStream();
        }
        final String content = this.getContent();
        if (content == null) {
            throw new IllegalStateException("unset string value");
        }
        return new ByteArrayInputStream((this.encoding == null) ? content.getBytes() : content.getBytes(this.encoding));
    }
    
    public synchronized OutputStream getOutputStream() throws IOException {
        if (this.isReference()) {
            return ((Resource)this.getCheckedRef()).getOutputStream();
        }
        if (this.getValue() != null) {
            throw new ImmutableResourceException();
        }
        return new StringResourceFilterOutputStream();
    }
    
    public void setRefid(final Reference r) {
        if (this.encoding != "UTF-8") {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    protected synchronized String getContent() {
        return this.getValue();
    }
    
    private void setValueFromOutputStream(final String output) {
        String value;
        if (this.getProject() != null) {
            value = this.getProject().replaceProperties(output);
        }
        else {
            value = output;
        }
        this.setValue(value);
    }
    
    static {
        STRING_MAGIC = Resource.getMagicNumber("StringResource".getBytes());
    }
    
    private class StringResourceFilterOutputStream extends FilterOutputStream
    {
        private final ByteArrayOutputStream baos;
        
        public StringResourceFilterOutputStream() {
            super(new ByteArrayOutputStream());
            this.baos = (ByteArrayOutputStream)this.out;
        }
        
        public void close() throws IOException {
            super.close();
            final String result = (StringResource.this.encoding == null) ? this.baos.toString() : this.baos.toString(StringResource.this.encoding);
            StringResource.this.setValueFromOutputStream(result);
        }
    }
}
