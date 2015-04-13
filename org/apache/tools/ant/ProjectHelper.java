package org.apache.tools.ant;

import java.io.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;
import org.xml.sax.*;
import java.util.*;

public class ProjectHelper
{
    public static final String ANT_CORE_URI = "antlib:org.apache.tools.ant";
    public static final String ANT_CURRENT_URI = "ant:current";
    public static final String ANT_ATTRIBUTE_URI = "ant:attribute";
    public static final String ANTLIB_URI = "antlib:";
    public static final String ANT_TYPE = "ant-type";
    public static final String HELPER_PROPERTY = "org.apache.tools.ant.ProjectHelper";
    public static final String SERVICE_ID = "META-INF/services/org.apache.tools.ant.ProjectHelper";
    public static final String PROJECTHELPER_REFERENCE = "ant.projectHelper";
    public static final String USE_PROJECT_NAME_AS_TARGET_PREFIX = "USE_PROJECT_NAME_AS_TARGET_PREFIX";
    private Vector<Object> importStack;
    private List<String[]> extensionStack;
    private static final ThreadLocal<String> targetPrefix;
    private static final ThreadLocal<String> prefixSeparator;
    private static final ThreadLocal<Boolean> inIncludeMode;
    
    public static void configureProject(final Project project, final File buildFile) throws BuildException {
        final FileResource resource = new FileResource(buildFile);
        final ProjectHelper helper = ProjectHelperRepository.getInstance().getProjectHelperForBuildFile(resource);
        project.addReference("ant.projectHelper", helper);
        helper.parse(project, buildFile);
    }
    
    public ProjectHelper() {
        super();
        this.importStack = new Vector<Object>();
        this.extensionStack = new LinkedList<String[]>();
    }
    
    public Vector<Object> getImportStack() {
        return this.importStack;
    }
    
    public List<String[]> getExtensionStack() {
        return this.extensionStack;
    }
    
    public static String getCurrentTargetPrefix() {
        return ProjectHelper.targetPrefix.get();
    }
    
    public static void setCurrentTargetPrefix(final String prefix) {
        ProjectHelper.targetPrefix.set(prefix);
    }
    
    public static String getCurrentPrefixSeparator() {
        return ProjectHelper.prefixSeparator.get();
    }
    
    public static void setCurrentPrefixSeparator(final String sep) {
        ProjectHelper.prefixSeparator.set(sep);
    }
    
    public static boolean isInIncludeMode() {
        return Boolean.TRUE.equals(ProjectHelper.inIncludeMode.get());
    }
    
    public static void setInIncludeMode(final boolean includeMode) {
        ProjectHelper.inIncludeMode.set(includeMode);
    }
    
    public void parse(final Project project, final Object source) throws BuildException {
        throw new BuildException("ProjectHelper.parse() must be implemented in a helper plugin " + this.getClass().getName());
    }
    
    public static ProjectHelper getProjectHelper() {
        return ProjectHelperRepository.getInstance().getHelpers().next();
    }
    
    public static ClassLoader getContextClassLoader() {
        return LoaderUtils.isContextLoaderAvailable() ? LoaderUtils.getContextClassLoader() : null;
    }
    
    public static void configure(Object target, final AttributeList attrs, final Project project) throws BuildException {
        if (target instanceof TypeAdapter) {
            target = ((TypeAdapter)target).getProxy();
        }
        final IntrospectionHelper ih = IntrospectionHelper.getHelper(project, target.getClass());
        for (int i = 0, length = attrs.getLength(); i < length; ++i) {
            final String value = replaceProperties(project, attrs.getValue(i), project.getProperties());
            try {
                ih.setAttribute(project, target, attrs.getName(i).toLowerCase(Locale.ENGLISH), value);
            }
            catch (BuildException be) {
                if (!attrs.getName(i).equals("id")) {
                    throw be;
                }
            }
        }
    }
    
    public static void addText(final Project project, final Object target, final char[] buf, final int start, final int count) throws BuildException {
        addText(project, target, new String(buf, start, count));
    }
    
    public static void addText(final Project project, Object target, final String text) throws BuildException {
        if (text == null) {
            return;
        }
        if (target instanceof TypeAdapter) {
            target = ((TypeAdapter)target).getProxy();
        }
        IntrospectionHelper.getHelper(project, target.getClass()).addText(project, target, text);
    }
    
    public static void storeChild(final Project project, final Object parent, final Object child, final String tag) {
        final IntrospectionHelper ih = IntrospectionHelper.getHelper(project, parent.getClass());
        ih.storeElement(project, parent, child, tag);
    }
    
    public static String replaceProperties(final Project project, final String value) throws BuildException {
        return project.replaceProperties(value);
    }
    
    public static String replaceProperties(final Project project, final String value, final Hashtable<String, Object> keys) throws BuildException {
        final PropertyHelper ph = PropertyHelper.getPropertyHelper(project);
        return ph.replaceProperties(null, value, keys);
    }
    
    public static void parsePropertyString(final String value, final Vector<String> fragments, final Vector<String> propertyRefs) throws BuildException {
        PropertyHelper.parsePropertyStringDefault(value, fragments, propertyRefs);
    }
    
    public static String genComponentName(final String uri, final String name) {
        if (uri == null || uri.equals("") || uri.equals("antlib:org.apache.tools.ant")) {
            return name;
        }
        return uri + ":" + name;
    }
    
    public static String extractUriFromComponentName(final String componentName) {
        if (componentName == null) {
            return "";
        }
        final int index = componentName.lastIndexOf(58);
        if (index == -1) {
            return "";
        }
        return componentName.substring(0, index);
    }
    
    public static String extractNameFromComponentName(final String componentName) {
        final int index = componentName.lastIndexOf(58);
        if (index == -1) {
            return componentName;
        }
        return componentName.substring(index + 1);
    }
    
    public static String nsToComponentName(final String ns) {
        return "attribute namespace:" + ns;
    }
    
    public static BuildException addLocationToBuildException(final BuildException ex, final Location newLocation) {
        if (ex.getLocation() == null || ex.getMessage() == null) {
            return ex;
        }
        final String errorMessage = "The following error occurred while executing this line:" + System.getProperty("line.separator") + ex.getLocation().toString() + ex.getMessage();
        if (newLocation == null) {
            return new BuildException(errorMessage, ex);
        }
        return new BuildException(errorMessage, ex, newLocation);
    }
    
    public boolean canParseAntlibDescriptor(final Resource r) {
        return false;
    }
    
    public UnknownElement parseAntlibDescriptor(final Project containingProject, final Resource source) {
        throw new BuildException("can't parse antlib descriptors");
    }
    
    public boolean canParseBuildFile(final Resource buildFile) {
        return true;
    }
    
    public String getDefaultBuildFile() {
        return "build.xml";
    }
    
    public void resolveExtensionOfAttributes(final Project project) throws BuildException {
        for (final String[] extensionInfo : this.getExtensionStack()) {
            final String extPointName = extensionInfo[0];
            final String targetName = extensionInfo[1];
            final OnMissingExtensionPoint missingBehaviour = OnMissingExtensionPoint.valueOf(extensionInfo[2]);
            final String prefixAndSep = (extensionInfo.length > 3) ? extensionInfo[3] : null;
            final Hashtable<String, Target> projectTargets = project.getTargets();
            Target extPoint = null;
            if (prefixAndSep == null) {
                extPoint = projectTargets.get(extPointName);
            }
            else {
                extPoint = projectTargets.get(prefixAndSep + extPointName);
                if (extPoint == null) {
                    extPoint = projectTargets.get(extPointName);
                }
            }
            if (extPoint == null) {
                final String message = "can't add target " + targetName + " to extension-point " + extPointName + " because the extension-point is unknown.";
                if (missingBehaviour == OnMissingExtensionPoint.FAIL) {
                    throw new BuildException(message);
                }
                if (missingBehaviour != OnMissingExtensionPoint.WARN) {
                    continue;
                }
                final Target t = projectTargets.get(targetName);
                project.log(t, "Warning: " + message, 1);
            }
            else {
                if (!(extPoint instanceof ExtensionPoint)) {
                    throw new BuildException("referenced target " + extPointName + " is not an extension-point");
                }
                extPoint.addDependency(targetName);
            }
        }
    }
    
    static {
        targetPrefix = new ThreadLocal<String>();
        prefixSeparator = new ThreadLocal<String>() {
            protected String initialValue() {
                return ".";
            }
        };
        inIncludeMode = new ThreadLocal<Boolean>() {
            protected Boolean initialValue() {
                return Boolean.FALSE;
            }
        };
    }
    
    public static final class OnMissingExtensionPoint
    {
        public static final OnMissingExtensionPoint FAIL;
        public static final OnMissingExtensionPoint WARN;
        public static final OnMissingExtensionPoint IGNORE;
        private static final OnMissingExtensionPoint[] values;
        private final String name;
        
        private OnMissingExtensionPoint(final String name) {
            super();
            this.name = name;
        }
        
        public String name() {
            return this.name;
        }
        
        public String toString() {
            return this.name;
        }
        
        public static OnMissingExtensionPoint valueOf(final String name) {
            if (name == null) {
                throw new NullPointerException();
            }
            for (int i = 0; i < OnMissingExtensionPoint.values.length; ++i) {
                if (name.equals(OnMissingExtensionPoint.values[i].name())) {
                    return OnMissingExtensionPoint.values[i];
                }
            }
            throw new IllegalArgumentException("Unknown onMissingExtensionPoint " + name);
        }
        
        static {
            FAIL = new OnMissingExtensionPoint("fail");
            WARN = new OnMissingExtensionPoint("warn");
            IGNORE = new OnMissingExtensionPoint("ignore");
            values = new OnMissingExtensionPoint[] { OnMissingExtensionPoint.FAIL, OnMissingExtensionPoint.WARN, OnMissingExtensionPoint.IGNORE };
        }
    }
}
