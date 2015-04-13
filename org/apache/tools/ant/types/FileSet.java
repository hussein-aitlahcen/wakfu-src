package org.apache.tools.ant.types;

import java.util.*;
import org.apache.tools.ant.types.resources.*;

public class FileSet extends AbstractFileSet implements ResourceCollection
{
    public FileSet() {
        super();
    }
    
    protected FileSet(final FileSet fileset) {
        super(fileset);
    }
    
    public Object clone() {
        if (this.isReference()) {
            return ((FileSet)this.getRef(this.getProject())).clone();
        }
        return super.clone();
    }
    
    public Iterator<Resource> iterator() {
        if (this.isReference()) {
            return ((FileSet)this.getRef(this.getProject())).iterator();
        }
        return new FileResourceIterator(this.getProject(), this.getDir(this.getProject()), this.getDirectoryScanner(this.getProject()).getIncludedFiles());
    }
    
    public int size() {
        if (this.isReference()) {
            return ((FileSet)this.getRef(this.getProject())).size();
        }
        return this.getDirectoryScanner(this.getProject()).getIncludedFilesCount();
    }
    
    public boolean isFilesystemOnly() {
        return true;
    }
}
