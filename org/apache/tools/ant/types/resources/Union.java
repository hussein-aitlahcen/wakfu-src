package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.util.*;

public class Union extends BaseResourceCollectionContainer
{
    public static Union getInstance(final ResourceCollection rc) {
        return (Union)((rc instanceof Union) ? rc : new Union(rc));
    }
    
    public Union() {
        super();
    }
    
    public Union(final Project project) {
        super(project);
    }
    
    public Union(final ResourceCollection rc) {
        this(Project.getProject(rc), rc);
    }
    
    public Union(final Project project, final ResourceCollection rc) {
        super(project);
        this.add(rc);
    }
    
    public String[] list() {
        if (this.isReference()) {
            return this.getCheckedRef(Union.class, this.getDataTypeName()).list();
        }
        final Collection<String> result = this.getAllToStrings();
        return result.toArray(new String[result.size()]);
    }
    
    public Resource[] listResources() {
        if (this.isReference()) {
            return this.getCheckedRef(Union.class, this.getDataTypeName()).listResources();
        }
        final Collection<Resource> result = this.getAllResources();
        return result.toArray(new Resource[result.size()]);
    }
    
    protected Collection<Resource> getCollection() {
        return this.getAllResources();
    }
    
    @Deprecated
    protected <T> Collection<T> getCollection(final boolean asString) {
        return (Collection<T>)(asString ? this.getAllToStrings() : this.getAllResources());
    }
    
    protected Collection<String> getAllToStrings() {
        final Set<Resource> allResources = this.getAllResources();
        final ArrayList<String> result = new ArrayList<String>(allResources.size());
        for (final Resource r : allResources) {
            result.add(r.toString());
        }
        return result;
    }
    
    protected Set<Resource> getAllResources() {
        final List<ResourceCollection> resourceCollections = this.getResourceCollections();
        if (resourceCollections.isEmpty()) {
            return Collections.emptySet();
        }
        final LinkedHashSet<Resource> result = new LinkedHashSet<Resource>(resourceCollections.size() * 2);
        for (final ResourceCollection resourceCollection : resourceCollections) {
            for (final Resource r : resourceCollection) {
                result.add(r);
            }
        }
        return result;
    }
}
