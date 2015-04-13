package org.apache.tools.ant.helper;

import java.io.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import java.net.*;
import org.xml.sax.*;
import java.util.*;

public class AntXMLContext
{
    private Project project;
    private File buildFile;
    private URL buildFileURL;
    private Vector<Target> targetVector;
    private File buildFileParent;
    private URL buildFileParentURL;
    private String currentProjectName;
    private Locator locator;
    private Target implicitTarget;
    private Target currentTarget;
    private Vector<RuntimeConfigurable> wStack;
    private boolean ignoreProjectTag;
    private Map<String, List<String>> prefixMapping;
    private Map<String, Target> currentTargets;
    
    public AntXMLContext(final Project project) {
        super();
        this.targetVector = new Vector<Target>();
        this.implicitTarget = new Target();
        this.currentTarget = null;
        this.wStack = new Vector<RuntimeConfigurable>();
        this.ignoreProjectTag = false;
        this.prefixMapping = new HashMap<String, List<String>>();
        this.currentTargets = null;
        this.project = project;
        this.implicitTarget.setProject(project);
        this.implicitTarget.setName("");
        this.targetVector.addElement(this.implicitTarget);
    }
    
    public void setBuildFile(final File buildFile) {
        this.buildFile = buildFile;
        if (buildFile != null) {
            this.buildFileParent = new File(buildFile.getParent());
            this.implicitTarget.setLocation(new Location(buildFile.getAbsolutePath()));
            try {
                this.setBuildFile(FileUtils.getFileUtils().getFileURL(buildFile));
                return;
            }
            catch (MalformedURLException ex) {
                throw new BuildException(ex);
            }
        }
        this.buildFileParent = null;
    }
    
    public void setBuildFile(final URL buildFile) throws MalformedURLException {
        this.buildFileURL = buildFile;
        this.buildFileParentURL = new URL(buildFile, ".");
        if (this.implicitTarget.getLocation() == null) {
            this.implicitTarget.setLocation(new Location(buildFile.toString()));
        }
    }
    
    public File getBuildFile() {
        return this.buildFile;
    }
    
    public File getBuildFileParent() {
        return this.buildFileParent;
    }
    
    public URL getBuildFileURL() {
        return this.buildFileURL;
    }
    
    public URL getBuildFileParentURL() {
        return this.buildFileParentURL;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public String getCurrentProjectName() {
        return this.currentProjectName;
    }
    
    public void setCurrentProjectName(final String name) {
        this.currentProjectName = name;
    }
    
    public RuntimeConfigurable currentWrapper() {
        if (this.wStack.size() < 1) {
            return null;
        }
        return this.wStack.elementAt(this.wStack.size() - 1);
    }
    
    public RuntimeConfigurable parentWrapper() {
        if (this.wStack.size() < 2) {
            return null;
        }
        return this.wStack.elementAt(this.wStack.size() - 2);
    }
    
    public void pushWrapper(final RuntimeConfigurable wrapper) {
        this.wStack.addElement(wrapper);
    }
    
    public void popWrapper() {
        if (this.wStack.size() > 0) {
            this.wStack.removeElementAt(this.wStack.size() - 1);
        }
    }
    
    public Vector<RuntimeConfigurable> getWrapperStack() {
        return this.wStack;
    }
    
    public void addTarget(final Target target) {
        this.targetVector.addElement(target);
        this.currentTarget = target;
    }
    
    public Target getCurrentTarget() {
        return this.currentTarget;
    }
    
    public Target getImplicitTarget() {
        return this.implicitTarget;
    }
    
    public void setCurrentTarget(final Target target) {
        this.currentTarget = target;
    }
    
    public void setImplicitTarget(final Target target) {
        this.implicitTarget = target;
    }
    
    public Vector<Target> getTargets() {
        return this.targetVector;
    }
    
    public void configureId(final Object element, final Attributes attr) {
        final String id = attr.getValue("id");
        if (id != null) {
            this.project.addIdReference(id, element);
        }
    }
    
    public Locator getLocator() {
        return this.locator;
    }
    
    public void setLocator(final Locator locator) {
        this.locator = locator;
    }
    
    public boolean isIgnoringProjectTag() {
        return this.ignoreProjectTag;
    }
    
    public void setIgnoreProjectTag(final boolean flag) {
        this.ignoreProjectTag = flag;
    }
    
    public void startPrefixMapping(final String prefix, final String uri) {
        List<String> list = this.prefixMapping.get(prefix);
        if (list == null) {
            list = new ArrayList<String>();
            this.prefixMapping.put(prefix, list);
        }
        list.add(uri);
    }
    
    public void endPrefixMapping(final String prefix) {
        final List<String> list = this.prefixMapping.get(prefix);
        if (list == null || list.size() == 0) {
            return;
        }
        list.remove(list.size() - 1);
    }
    
    public String getPrefixMapping(final String prefix) {
        final List<String> list = this.prefixMapping.get(prefix);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(list.size() - 1);
    }
    
    public Map<String, Target> getCurrentTargets() {
        return this.currentTargets;
    }
    
    public void setCurrentTargets(final Map<String, Target> currentTargets) {
        this.currentTargets = currentTargets;
    }
}
