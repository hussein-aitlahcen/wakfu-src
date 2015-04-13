package org.apache.tools.ant.taskdefs;

import java.net.*;
import java.io.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.util.*;

public class Antlib extends Task implements TaskContainer
{
    public static final String TAG = "antlib";
    private ClassLoader classLoader;
    private String uri;
    private List<Object> tasks;
    
    public Antlib() {
        super();
        this.uri = "";
        this.tasks = new ArrayList<Object>();
    }
    
    public static Antlib createAntlib(final Project project, final URL antlibUrl, final String uri) {
        try {
            antlibUrl.openConnection().connect();
        }
        catch (IOException ex) {
            throw new BuildException("Unable to find " + antlibUrl, ex);
        }
        final ComponentHelper helper = ComponentHelper.getComponentHelper(project);
        helper.enterAntLib(uri);
        final URLResource antlibResource = new URLResource(antlibUrl);
        try {
            ProjectHelper parser = null;
            final Object p = project.getReference("ant.projectHelper");
            if (p instanceof ProjectHelper) {
                parser = (ProjectHelper)p;
                if (!parser.canParseAntlibDescriptor(antlibResource)) {
                    parser = null;
                }
            }
            if (parser == null) {
                final ProjectHelperRepository helperRepository = ProjectHelperRepository.getInstance();
                parser = helperRepository.getProjectHelperForAntlib(antlibResource);
            }
            final UnknownElement ue = parser.parseAntlibDescriptor(project, antlibResource);
            if (!ue.getTag().equals("antlib")) {
                throw new BuildException("Unexpected tag " + ue.getTag() + " expecting " + "antlib", ue.getLocation());
            }
            final Antlib antlib = new Antlib();
            antlib.setProject(project);
            antlib.setLocation(ue.getLocation());
            antlib.setTaskName("antlib");
            antlib.init();
            ue.configure(antlib);
            return antlib;
        }
        finally {
            helper.exitAntLib();
        }
    }
    
    protected void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    protected void setURI(final String uri) {
        this.uri = uri;
    }
    
    private ClassLoader getClassLoader() {
        if (this.classLoader == null) {
            this.classLoader = Antlib.class.getClassLoader();
        }
        return this.classLoader;
    }
    
    public void addTask(final Task nestedTask) {
        this.tasks.add(nestedTask);
    }
    
    public void execute() {
        for (final UnknownElement ue : this.tasks) {
            this.setLocation(ue.getLocation());
            ue.maybeConfigure();
            final Object configuredObject = ue.getRealThing();
            if (configuredObject == null) {
                continue;
            }
            if (!(configuredObject instanceof AntlibDefinition)) {
                throw new BuildException("Invalid task in antlib " + ue.getTag() + " " + configuredObject.getClass() + " does not " + "extend org.apache.tools.ant.taskdefs.AntlibDefinition");
            }
            final AntlibDefinition def = (AntlibDefinition)configuredObject;
            def.setURI(this.uri);
            def.setAntlibClassLoader(this.getClassLoader());
            def.init();
            def.execute();
        }
    }
}
