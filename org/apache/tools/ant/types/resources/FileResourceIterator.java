package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;

public class FileResourceIterator implements Iterator<Resource>
{
    private Project project;
    private File basedir;
    private String[] files;
    private int pos;
    
    public FileResourceIterator() {
        super();
        this.pos = 0;
    }
    
    public FileResourceIterator(final Project project) {
        super();
        this.pos = 0;
        this.project = project;
    }
    
    public FileResourceIterator(final File basedir) {
        this(null, basedir);
    }
    
    public FileResourceIterator(final Project project, final File basedir) {
        this(project);
        this.basedir = basedir;
    }
    
    public FileResourceIterator(final File basedir, final String[] filenames) {
        this(null, basedir, filenames);
    }
    
    public FileResourceIterator(final Project project, final File basedir, final String[] filenames) {
        this(project, basedir);
        this.addFiles(filenames);
    }
    
    public void addFiles(final String[] s) {
        final int start = (this.files == null) ? 0 : this.files.length;
        final String[] newfiles = new String[start + s.length];
        if (start > 0) {
            System.arraycopy(this.files, 0, newfiles, 0, start);
        }
        System.arraycopy(s, 0, this.files = newfiles, start, s.length);
    }
    
    public boolean hasNext() {
        return this.pos < this.files.length;
    }
    
    public Resource next() {
        return this.nextResource();
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    public FileResource nextResource() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        final FileResource result = new FileResource(this.basedir, this.files[this.pos++]);
        result.setProject(this.project);
        return result;
    }
}
