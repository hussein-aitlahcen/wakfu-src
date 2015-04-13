package org.apache.tools.ant.types;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.util.*;

public class Mapper extends DataType implements Cloneable
{
    protected MapperType type;
    protected String classname;
    protected Path classpath;
    protected String from;
    protected String to;
    private ContainerMapper container;
    
    public Mapper(final Project p) {
        super();
        this.type = null;
        this.classname = null;
        this.classpath = null;
        this.from = null;
        this.to = null;
        this.container = null;
        this.setProject(p);
    }
    
    public void setType(final MapperType type) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.type = type;
    }
    
    public void addConfigured(final FileNameMapper fileNameMapper) {
        this.add(fileNameMapper);
    }
    
    public void add(final FileNameMapper fileNameMapper) {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (this.container == null) {
            if (this.type == null && this.classname == null) {
                this.container = new CompositeMapper();
            }
            else {
                final FileNameMapper m = this.getImplementation();
                if (!(m instanceof ContainerMapper)) {
                    throw new BuildException(String.valueOf(m) + " mapper implementation does not support nested mappers!");
                }
                this.container = (ContainerMapper)m;
            }
        }
        this.container.add(fileNameMapper);
        this.setChecked(false);
    }
    
    public void addConfiguredMapper(final Mapper mapper) {
        this.add(mapper.getImplementation());
    }
    
    public void setClassname(final String classname) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.classname = classname;
    }
    
    public void setClasspath(final Path classpath) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        }
        else {
            this.classpath.append(classpath);
        }
    }
    
    public Path createClasspath() {
        if (this.isReference()) {
            throw this.noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
        }
        this.setChecked(false);
        return this.classpath.createPath();
    }
    
    public void setClasspathRef(final Reference ref) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.createClasspath().setRefid(ref);
    }
    
    public void setFrom(final String from) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.from = from;
    }
    
    public void setTo(final String to) {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.to = to;
    }
    
    public void setRefid(final Reference r) throws BuildException {
        if (this.type != null || this.from != null || this.to != null) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    public FileNameMapper getImplementation() throws BuildException {
        if (this.isReference()) {
            this.dieOnCircularReference();
            final Reference r = this.getRefid();
            final Object o = r.getReferencedObject(this.getProject());
            if (o instanceof FileNameMapper) {
                return (FileNameMapper)o;
            }
            if (o instanceof Mapper) {
                return ((Mapper)o).getImplementation();
            }
            final String od = (o == null) ? "null" : o.getClass().getName();
            throw new BuildException(od + " at reference '" + r.getRefId() + "' is not a valid mapper reference.");
        }
        else {
            if (this.type == null && this.classname == null && this.container == null) {
                throw new BuildException("nested mapper or one of the attributes type or classname is required");
            }
            if (this.container != null) {
                return this.container;
            }
            if (this.type != null && this.classname != null) {
                throw new BuildException("must not specify both type and classname attribute");
            }
            try {
                final FileNameMapper m = (FileNameMapper)this.getImplementationClass().newInstance();
                final Project p = this.getProject();
                if (p != null) {
                    p.setProjectReference(m);
                }
                m.setFrom(this.from);
                m.setTo(this.to);
                return m;
            }
            catch (BuildException be) {
                throw be;
            }
            catch (Throwable t) {
                throw new BuildException(t);
            }
        }
    }
    
    protected Class<? extends FileNameMapper> getImplementationClass() throws ClassNotFoundException {
        String cName = this.classname;
        if (this.type != null) {
            cName = this.type.getImplementation();
        }
        final ClassLoader loader = (this.classpath == null) ? this.getClass().getClassLoader() : this.getProject().createClassLoader(this.classpath);
        return Class.forName(cName, true, loader).asSubclass(FileNameMapper.class);
    }
    
    protected Mapper getRef() {
        return this.getCheckedRef(Mapper.class, this.getDataTypeName());
    }
    
    public static class MapperType extends EnumeratedAttribute
    {
        private Properties implementations;
        
        public MapperType() {
            super();
            ((Hashtable<String, String>)(this.implementations = new Properties())).put("identity", "org.apache.tools.ant.util.IdentityMapper");
            ((Hashtable<String, String>)this.implementations).put("flatten", "org.apache.tools.ant.util.FlatFileNameMapper");
            ((Hashtable<String, String>)this.implementations).put("glob", "org.apache.tools.ant.util.GlobPatternMapper");
            ((Hashtable<String, String>)this.implementations).put("merge", "org.apache.tools.ant.util.MergingMapper");
            ((Hashtable<String, String>)this.implementations).put("regexp", "org.apache.tools.ant.util.RegexpPatternMapper");
            ((Hashtable<String, String>)this.implementations).put("package", "org.apache.tools.ant.util.PackageNameMapper");
            ((Hashtable<String, String>)this.implementations).put("unpackage", "org.apache.tools.ant.util.UnPackageNameMapper");
        }
        
        public String[] getValues() {
            return new String[] { "identity", "flatten", "glob", "merge", "regexp", "package", "unpackage" };
        }
        
        public String getImplementation() {
            return this.implementations.getProperty(this.getValue());
        }
    }
}
