package org.apache.tools.ant;

import java.lang.reflect.*;
import org.apache.tools.ant.helper.*;
import org.apache.tools.ant.util.*;
import java.net.*;
import java.io.*;
import org.apache.tools.ant.types.*;
import java.util.*;

public class ProjectHelperRepository
{
    private static final String DEBUG_PROJECT_HELPER_REPOSITORY = "ant.project-helper-repo.debug";
    private static final boolean DEBUG;
    private static ProjectHelperRepository instance;
    private List<Constructor<? extends ProjectHelper>> helpers;
    private static Constructor<ProjectHelper2> PROJECTHELPER2_CONSTRUCTOR;
    
    public static ProjectHelperRepository getInstance() {
        return ProjectHelperRepository.instance;
    }
    
    private ProjectHelperRepository() {
        super();
        this.helpers = new ArrayList<Constructor<? extends ProjectHelper>>();
        this.collectProjectHelpers();
    }
    
    private void collectProjectHelpers() {
        Constructor<? extends ProjectHelper> projectHelper = this.getProjectHelperBySystemProperty();
        this.registerProjectHelper(projectHelper);
        try {
            final ClassLoader classLoader = LoaderUtils.getContextClassLoader();
            if (classLoader != null) {
                final Enumeration<URL> resources = classLoader.getResources("META-INF/services/org.apache.tools.ant.ProjectHelper");
                while (resources.hasMoreElements()) {
                    final URL resource = resources.nextElement();
                    projectHelper = this.getProjectHelperByService(resource.openStream());
                    this.registerProjectHelper(projectHelper);
                }
            }
            final InputStream systemResource = ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.tools.ant.ProjectHelper");
            if (systemResource != null) {
                projectHelper = this.getProjectHelperByService(systemResource);
                this.registerProjectHelper(projectHelper);
            }
        }
        catch (Exception e) {
            System.err.println("Unable to load ProjectHelper from service META-INF/services/org.apache.tools.ant.ProjectHelper (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            if (ProjectHelperRepository.DEBUG) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    public void registerProjectHelper(final String helperClassName) throws BuildException {
        this.registerProjectHelper(this.getHelperConstructor(helperClassName));
    }
    
    public void registerProjectHelper(final Class<? extends ProjectHelper> helperClass) throws BuildException {
        try {
            this.registerProjectHelper(helperClass.getConstructor((Class<?>[])new Class[0]));
        }
        catch (NoSuchMethodException e) {
            throw new BuildException("Couldn't find no-arg constructor in " + helperClass.getName());
        }
    }
    
    private void registerProjectHelper(final Constructor<? extends ProjectHelper> helperConstructor) {
        if (helperConstructor == null) {
            return;
        }
        if (ProjectHelperRepository.DEBUG) {
            System.out.println("ProjectHelper " + helperConstructor.getClass().getName() + " registered.");
        }
        this.helpers.add(helperConstructor);
    }
    
    private Constructor<? extends ProjectHelper> getProjectHelperBySystemProperty() {
        final String helperClass = System.getProperty("org.apache.tools.ant.ProjectHelper");
        try {
            if (helperClass != null) {
                return this.getHelperConstructor(helperClass);
            }
        }
        catch (SecurityException e) {
            System.err.println("Unable to load ProjectHelper class \"" + helperClass + " specified in system property " + "org.apache.tools.ant.ProjectHelper" + " (" + e.getMessage() + ")");
            if (ProjectHelperRepository.DEBUG) {
                e.printStackTrace(System.err);
            }
        }
        return null;
    }
    
    private Constructor<? extends ProjectHelper> getProjectHelperByService(final InputStream is) {
        try {
            InputStreamReader isr;
            try {
                isr = new InputStreamReader(is, "UTF-8");
            }
            catch (UnsupportedEncodingException e2) {
                isr = new InputStreamReader(is);
            }
            final BufferedReader rd = new BufferedReader(isr);
            final String helperClassName = rd.readLine();
            rd.close();
            if (helperClassName != null && !"".equals(helperClassName)) {
                return this.getHelperConstructor(helperClassName);
            }
        }
        catch (Exception e) {
            System.out.println("Unable to load ProjectHelper from service META-INF/services/org.apache.tools.ant.ProjectHelper (" + e.getMessage() + ")");
            if (ProjectHelperRepository.DEBUG) {
                e.printStackTrace(System.err);
            }
        }
        return null;
    }
    
    private Constructor<? extends ProjectHelper> getHelperConstructor(final String helperClass) throws BuildException {
        final ClassLoader classLoader = LoaderUtils.getContextClassLoader();
        try {
            Class<?> clazz = null;
            if (classLoader != null) {
                try {
                    clazz = classLoader.loadClass(helperClass);
                }
                catch (ClassNotFoundException ex) {}
            }
            if (clazz == null) {
                clazz = Class.forName(helperClass);
            }
            return clazz.asSubclass(ProjectHelper.class).getConstructor((Class<?>[])new Class[0]);
        }
        catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    public ProjectHelper getProjectHelperForBuildFile(final Resource buildFile) throws BuildException {
        final Iterator<ProjectHelper> it = this.getHelpers();
        while (it.hasNext()) {
            final ProjectHelper helper = it.next();
            if (helper.canParseBuildFile(buildFile)) {
                if (ProjectHelperRepository.DEBUG) {
                    System.out.println("ProjectHelper " + helper.getClass().getName() + " selected for the build file " + buildFile);
                }
                return helper;
            }
        }
        throw new RuntimeException("BUG: at least the ProjectHelper2 should have supported the file " + buildFile);
    }
    
    public ProjectHelper getProjectHelperForAntlib(final Resource antlib) throws BuildException {
        final Iterator<ProjectHelper> it = this.getHelpers();
        while (it.hasNext()) {
            final ProjectHelper helper = it.next();
            if (helper.canParseAntlibDescriptor(antlib)) {
                if (ProjectHelperRepository.DEBUG) {
                    System.out.println("ProjectHelper " + helper.getClass().getName() + " selected for the antlib " + antlib);
                }
                return helper;
            }
        }
        throw new RuntimeException("BUG: at least the ProjectHelper2 should have supported the file " + antlib);
    }
    
    public Iterator<ProjectHelper> getHelpers() {
        return new ConstructingIterator(this.helpers.iterator());
    }
    
    static {
        DEBUG = "true".equals(System.getProperty("ant.project-helper-repo.debug"));
        ProjectHelperRepository.instance = new ProjectHelperRepository();
        try {
            ProjectHelperRepository.PROJECTHELPER2_CONSTRUCTOR = ProjectHelper2.class.getConstructor((Class<?>[])new Class[0]);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static class ConstructingIterator implements Iterator<ProjectHelper>
    {
        private final Iterator<Constructor<? extends ProjectHelper>> nested;
        private boolean empty;
        
        ConstructingIterator(final Iterator<Constructor<? extends ProjectHelper>> nested) {
            super();
            this.empty = false;
            this.nested = nested;
        }
        
        public boolean hasNext() {
            return this.nested.hasNext() || !this.empty;
        }
        
        public ProjectHelper next() {
            Constructor<? extends ProjectHelper> c;
            if (this.nested.hasNext()) {
                c = this.nested.next();
            }
            else {
                this.empty = true;
                c = ProjectHelperRepository.PROJECTHELPER2_CONSTRUCTOR;
            }
            try {
                return (ProjectHelper)c.newInstance(new Object[0]);
            }
            catch (Exception e) {
                throw new BuildException("Failed to invoke no-arg constructor on " + c.getName());
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }
}
