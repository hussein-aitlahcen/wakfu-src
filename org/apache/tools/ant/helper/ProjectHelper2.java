package org.apache.tools.ant.helper;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.*;
import java.net.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.launch.*;
import org.apache.tools.zip.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import org.apache.tools.ant.*;

public class ProjectHelper2 extends ProjectHelper
{
    public static final String REFID_TARGETS = "ant.targets";
    private static AntHandler elementHandler;
    private static AntHandler targetHandler;
    private static AntHandler mainHandler;
    private static AntHandler projectHandler;
    private static final String REFID_CONTEXT = "ant.parsing.context";
    private static final FileUtils FILE_UTILS;
    
    public boolean canParseAntlibDescriptor(final Resource resource) {
        return true;
    }
    
    public UnknownElement parseAntlibDescriptor(final Project containingProject, final Resource resource) {
        final URLProvider up = resource.as(URLProvider.class);
        if (up == null) {
            throw new BuildException("Unsupported resource type: " + resource);
        }
        return this.parseUnknownElement(containingProject, up.getURL());
    }
    
    public UnknownElement parseUnknownElement(final Project project, final URL source) throws BuildException {
        final Target dummyTarget = new Target();
        dummyTarget.setProject(project);
        final AntXMLContext context = new AntXMLContext(project);
        context.addTarget(dummyTarget);
        context.setImplicitTarget(dummyTarget);
        this.parse(context.getProject(), source, new RootHandler(context, ProjectHelper2.elementHandler));
        final Task[] tasks = dummyTarget.getTasks();
        if (tasks.length != 1) {
            throw new BuildException("No tasks defined");
        }
        return (UnknownElement)tasks[0];
    }
    
    public void parse(final Project project, final Object source) throws BuildException {
        this.getImportStack().addElement(source);
        AntXMLContext context = null;
        context = project.getReference("ant.parsing.context");
        if (context == null) {
            context = new AntXMLContext(project);
            project.addReference("ant.parsing.context", context);
            project.addReference("ant.targets", context.getTargets());
        }
        if (this.getImportStack().size() > 1) {
            context.setIgnoreProjectTag(true);
            final Target currentTarget = context.getCurrentTarget();
            final Target currentImplicit = context.getImplicitTarget();
            final Map<String, Target> currentTargets = context.getCurrentTargets();
            try {
                final Target newCurrent = new Target();
                newCurrent.setProject(project);
                newCurrent.setName("");
                context.setCurrentTarget(newCurrent);
                context.setCurrentTargets(new HashMap<String, Target>());
                context.setImplicitTarget(newCurrent);
                this.parse(project, source, new RootHandler(context, ProjectHelper2.mainHandler));
                newCurrent.execute();
            }
            finally {
                context.setCurrentTarget(currentTarget);
                context.setImplicitTarget(currentImplicit);
                context.setCurrentTargets(currentTargets);
            }
        }
        else {
            context.setCurrentTargets(new HashMap<String, Target>());
            this.parse(project, source, new RootHandler(context, ProjectHelper2.mainHandler));
            context.getImplicitTarget().execute();
            this.resolveExtensionOfAttributes(project);
        }
    }
    
    public void parse(final Project project, final Object source, final RootHandler handler) throws BuildException {
        final AntXMLContext context = handler.context;
        File buildFile = null;
        URL url = null;
        String buildFileName = null;
        if (source instanceof File) {
            buildFile = (File)source;
        }
        else if (source instanceof URL) {
            url = (URL)source;
        }
        else if (source instanceof Resource) {
            final FileProvider fp = ((Resource)source).as(FileProvider.class);
            if (fp != null) {
                buildFile = fp.getFile();
            }
            else {
                final URLProvider up = ((Resource)source).as(URLProvider.class);
                if (up != null) {
                    url = up.getURL();
                }
            }
        }
        if (buildFile != null) {
            buildFile = ProjectHelper2.FILE_UTILS.normalize(buildFile.getAbsolutePath());
            context.setBuildFile(buildFile);
            buildFileName = buildFile.toString();
        }
        else {
            if (url == null) {
                throw new BuildException("Source " + source.getClass().getName() + " not supported by this plugin");
            }
            try {
                context.setBuildFile((File)null);
                context.setBuildFile(url);
            }
            catch (MalformedURLException ex) {
                throw new BuildException(ex);
            }
            buildFileName = url.toString();
        }
        InputStream inputStream = null;
        InputSource inputSource = null;
        ZipFile zf = null;
        try {
            final XMLReader parser = JAXPUtils.getNamespaceXMLReader();
            String uri = null;
            if (buildFile != null) {
                uri = ProjectHelper2.FILE_UTILS.toURI(buildFile.getAbsolutePath());
                inputStream = new FileInputStream(buildFile);
            }
            else {
                uri = url.toString();
                int pling = -1;
                if (uri.startsWith("jar:file") && (pling = uri.indexOf("!/")) > -1) {
                    zf = new ZipFile(Locator.fromJarURI(uri), "UTF-8");
                    inputStream = zf.getInputStream(zf.getEntry(uri.substring(pling + 1)));
                }
                else {
                    inputStream = url.openStream();
                }
            }
            inputSource = new InputSource(inputStream);
            if (uri != null) {
                inputSource.setSystemId(uri);
            }
            project.log("parsing buildfile " + buildFileName + " with URI = " + uri + ((zf != null) ? " from a zip file" : ""), 3);
            parser.setContentHandler(handler);
            parser.setEntityResolver(handler);
            parser.setErrorHandler(handler);
            parser.setDTDHandler(handler);
            parser.parse(inputSource);
        }
        catch (SAXParseException exc) {
            final Location location = new Location(exc.getSystemId(), exc.getLineNumber(), exc.getColumnNumber());
            final Throwable t = exc.getException();
            if (t instanceof BuildException) {
                final BuildException be = (BuildException)t;
                if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                    be.setLocation(location);
                }
                throw be;
            }
            throw new BuildException(exc.getMessage(), (t == null) ? exc : t, location);
        }
        catch (SAXException exc2) {
            final Throwable t2 = exc2.getException();
            if (t2 instanceof BuildException) {
                throw (BuildException)t2;
            }
            throw new BuildException(exc2.getMessage(), (t2 == null) ? exc2 : t2);
        }
        catch (FileNotFoundException exc3) {
            throw new BuildException(exc3);
        }
        catch (UnsupportedEncodingException exc4) {
            throw new BuildException("Encoding of project file " + buildFileName + " is invalid.", exc4);
        }
        catch (IOException exc5) {
            throw new BuildException("Error reading project file " + buildFileName + ": " + exc5.getMessage(), exc5);
        }
        finally {
            FileUtils.close(inputStream);
            ZipFile.closeQuietly(zf);
        }
    }
    
    protected static AntHandler getMainHandler() {
        return ProjectHelper2.mainHandler;
    }
    
    protected static void setMainHandler(final AntHandler handler) {
        ProjectHelper2.mainHandler = handler;
    }
    
    protected static AntHandler getProjectHandler() {
        return ProjectHelper2.projectHandler;
    }
    
    protected static void setProjectHandler(final AntHandler handler) {
        ProjectHelper2.projectHandler = handler;
    }
    
    protected static AntHandler getTargetHandler() {
        return ProjectHelper2.targetHandler;
    }
    
    protected static void setTargetHandler(final AntHandler handler) {
        ProjectHelper2.targetHandler = handler;
    }
    
    protected static AntHandler getElementHandler() {
        return ProjectHelper2.elementHandler;
    }
    
    protected static void setElementHandler(final AntHandler handler) {
        ProjectHelper2.elementHandler = handler;
    }
    
    static {
        ProjectHelper2.elementHandler = new ElementHandler();
        ProjectHelper2.targetHandler = new TargetHandler();
        ProjectHelper2.mainHandler = new MainHandler();
        ProjectHelper2.projectHandler = new ProjectHandler();
        FILE_UTILS = FileUtils.getFileUtils();
    }
    
    public static class AntHandler
    {
        public void onStartElement(final String uri, final String tag, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
        }
        
        public AntHandler onStartChild(final String uri, final String tag, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            throw new SAXParseException("Unexpected element \"" + qname + " \"", context.getLocator());
        }
        
        public void onEndChild(final String uri, final String tag, final String qname, final AntXMLContext context) throws SAXParseException {
        }
        
        public void onEndElement(final String uri, final String tag, final AntXMLContext context) {
        }
        
        public void characters(final char[] buf, final int start, final int count, final AntXMLContext context) throws SAXParseException {
            final String s = new String(buf, start, count).trim();
            if (s.length() > 0) {
                throw new SAXParseException("Unexpected text \"" + s + "\"", context.getLocator());
            }
        }
        
        protected void checkNamespace(final String uri) {
        }
    }
    
    public static class RootHandler extends DefaultHandler
    {
        private Stack<AntHandler> antHandlers;
        private AntHandler currentHandler;
        private AntXMLContext context;
        
        public RootHandler(final AntXMLContext context, final AntHandler rootHandler) {
            super();
            this.antHandlers = new Stack<AntHandler>();
            this.currentHandler = null;
            this.currentHandler = rootHandler;
            this.antHandlers.push(this.currentHandler);
            this.context = context;
        }
        
        public AntHandler getCurrentAntHandler() {
            return this.currentHandler;
        }
        
        public InputSource resolveEntity(final String publicId, final String systemId) {
            this.context.getProject().log("resolving systemId: " + systemId, 3);
            if (systemId.startsWith("file:")) {
                final String path = ProjectHelper2.FILE_UTILS.fromURI(systemId);
                File file = new File(path);
                if (!file.isAbsolute()) {
                    file = ProjectHelper2.FILE_UTILS.resolveFile(this.context.getBuildFileParent(), path);
                    this.context.getProject().log("Warning: '" + systemId + "' in " + this.context.getBuildFile() + " should be expressed simply as '" + path.replace('\\', '/') + "' for compliance with other XML tools", 1);
                }
                this.context.getProject().log("file=" + file, 4);
                try {
                    final InputSource inputSource = new InputSource(new FileInputStream(file));
                    inputSource.setSystemId(ProjectHelper2.FILE_UTILS.toURI(file.getAbsolutePath()));
                    return inputSource;
                }
                catch (FileNotFoundException fne) {
                    this.context.getProject().log(file.getAbsolutePath() + " could not be found", 1);
                }
            }
            this.context.getProject().log("could not resolve systemId", 4);
            return null;
        }
        
        public void startElement(final String uri, final String tag, final String qname, final Attributes attrs) throws SAXParseException {
            final AntHandler next = this.currentHandler.onStartChild(uri, tag, qname, attrs, this.context);
            this.antHandlers.push(this.currentHandler);
            (this.currentHandler = next).onStartElement(uri, tag, qname, attrs, this.context);
        }
        
        public void setDocumentLocator(final org.xml.sax.Locator locator) {
            this.context.setLocator(locator);
        }
        
        public void endElement(final String uri, final String name, final String qName) throws SAXException {
            this.currentHandler.onEndElement(uri, name, this.context);
            final AntHandler prev = this.antHandlers.pop();
            this.currentHandler = prev;
            if (this.currentHandler != null) {
                this.currentHandler.onEndChild(uri, name, qName, this.context);
            }
        }
        
        public void characters(final char[] buf, final int start, final int count) throws SAXParseException {
            this.currentHandler.characters(buf, start, count, this.context);
        }
        
        public void startPrefixMapping(final String prefix, final String uri) {
            this.context.startPrefixMapping(prefix, uri);
        }
        
        public void endPrefixMapping(final String prefix) {
            this.context.endPrefixMapping(prefix);
        }
    }
    
    public static class MainHandler extends AntHandler
    {
        public AntHandler onStartChild(final String uri, final String name, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            if (name.equals("project") && (uri.equals("") || uri.equals("antlib:org.apache.tools.ant"))) {
                return ProjectHelper2.projectHandler;
            }
            if (name.equals(qname)) {
                throw new SAXParseException("Unexpected element \"{" + uri + "}" + name + "\" {" + "antlib:org.apache.tools.ant" + "}" + name, context.getLocator());
            }
            throw new SAXParseException("Unexpected element \"" + qname + "\" " + name, context.getLocator());
        }
    }
    
    public static class ProjectHandler extends AntHandler
    {
        public void onStartElement(final String uri, final String tag, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            String baseDir = null;
            boolean nameAttributeSet = false;
            final Project project = context.getProject();
            context.getImplicitTarget().setLocation(new Location(context.getLocator()));
            for (int i = 0; i < attrs.getLength(); ++i) {
                final String attrUri = attrs.getURI(i);
                if (attrUri == null || attrUri.equals("") || attrUri.equals(uri)) {
                    final String key = attrs.getLocalName(i);
                    final String value = attrs.getValue(i);
                    if (key.equals("default")) {
                        if (value != null && !value.equals("") && !context.isIgnoringProjectTag()) {
                            project.setDefault(value);
                        }
                    }
                    else if (key.equals("name")) {
                        if (value != null) {
                            context.setCurrentProjectName(value);
                            nameAttributeSet = true;
                            if (!context.isIgnoringProjectTag()) {
                                project.setName(value);
                                project.addReference(value, project);
                            }
                            else if (ProjectHelper.isInIncludeMode() && !"".equals(value) && ProjectHelper.getCurrentTargetPrefix() != null && ProjectHelper.getCurrentTargetPrefix().endsWith("USE_PROJECT_NAME_AS_TARGET_PREFIX")) {
                                final String newTargetPrefix = ProjectHelper.getCurrentTargetPrefix().replace("USE_PROJECT_NAME_AS_TARGET_PREFIX", value);
                                ProjectHelper.setCurrentTargetPrefix(newTargetPrefix);
                            }
                        }
                    }
                    else if (key.equals("id")) {
                        if (value != null && !context.isIgnoringProjectTag()) {
                            project.addReference(value, project);
                        }
                    }
                    else {
                        if (!key.equals("basedir")) {
                            throw new SAXParseException("Unexpected attribute \"" + attrs.getQName(i) + "\"", context.getLocator());
                        }
                        if (!context.isIgnoringProjectTag()) {
                            baseDir = value;
                        }
                    }
                }
            }
            final String antFileProp = "ant.file." + context.getCurrentProjectName();
            final String dup = project.getProperty(antFileProp);
            final String typeProp = "ant.file.type." + context.getCurrentProjectName();
            final String dupType = project.getProperty(typeProp);
            if (dup != null && nameAttributeSet) {
                Object dupFile = null;
                Object contextFile = null;
                if ("url".equals(dupType)) {
                    try {
                        dupFile = new URL(dup);
                    }
                    catch (MalformedURLException mue) {
                        throw new BuildException("failed to parse " + dup + " as URL while looking" + " at a duplicate project" + " name.", mue);
                    }
                    contextFile = context.getBuildFileURL();
                }
                else {
                    dupFile = new File(dup);
                    contextFile = context.getBuildFile();
                }
                if (context.isIgnoringProjectTag() && !dupFile.equals(contextFile)) {
                    project.log("Duplicated project name in import. Project " + context.getCurrentProjectName() + " defined first in " + dup + " and again in " + contextFile, 1);
                }
            }
            if (nameAttributeSet) {
                if (context.getBuildFile() != null) {
                    project.setUserProperty(antFileProp, context.getBuildFile().toString());
                    project.setUserProperty(typeProp, "file");
                }
                else if (context.getBuildFileURL() != null) {
                    project.setUserProperty(antFileProp, context.getBuildFileURL().toString());
                    project.setUserProperty(typeProp, "url");
                }
            }
            if (context.isIgnoringProjectTag()) {
                return;
            }
            if (project.getProperty("basedir") != null) {
                project.setBasedir(project.getProperty("basedir"));
            }
            else if (baseDir == null) {
                project.setBasedir(context.getBuildFileParent().getAbsolutePath());
            }
            else if (new File(baseDir).isAbsolute()) {
                project.setBasedir(baseDir);
            }
            else {
                project.setBaseDir(ProjectHelper2.FILE_UTILS.resolveFile(context.getBuildFileParent(), baseDir));
            }
            project.addTarget("", context.getImplicitTarget());
            context.setCurrentTarget(context.getImplicitTarget());
        }
        
        public AntHandler onStartChild(final String uri, final String name, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            return ((name.equals("target") || name.equals("extension-point")) && (uri.equals("") || uri.equals("antlib:org.apache.tools.ant"))) ? ProjectHelper2.targetHandler : ProjectHelper2.elementHandler;
        }
    }
    
    public static class TargetHandler extends AntHandler
    {
        public void onStartElement(final String uri, final String tag, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            String name = null;
            String depends = "";
            String extensionPoint = null;
            OnMissingExtensionPoint extensionPointMissing = null;
            final Project project = context.getProject();
            final Target target = "target".equals(tag) ? new Target() : new ExtensionPoint();
            target.setProject(project);
            target.setLocation(new Location(context.getLocator()));
            context.addTarget(target);
            for (int i = 0; i < attrs.getLength(); ++i) {
                final String attrUri = attrs.getURI(i);
                if (attrUri == null || attrUri.equals("") || attrUri.equals(uri)) {
                    final String key = attrs.getLocalName(i);
                    final String value = attrs.getValue(i);
                    if (key.equals("name")) {
                        name = value;
                        if ("".equals(name)) {
                            throw new BuildException("name attribute must not be empty");
                        }
                    }
                    else if (key.equals("depends")) {
                        depends = value;
                    }
                    else if (key.equals("if")) {
                        target.setIf(value);
                    }
                    else if (key.equals("unless")) {
                        target.setUnless(value);
                    }
                    else if (key.equals("id")) {
                        if (value != null && !value.equals("")) {
                            context.getProject().addReference(value, target);
                        }
                    }
                    else if (key.equals("description")) {
                        target.setDescription(value);
                    }
                    else {
                        if (!key.equals("extensionOf")) {
                            if (key.equals("onMissingExtensionPoint")) {
                                try {
                                    extensionPointMissing = OnMissingExtensionPoint.valueOf(value);
                                    continue;
                                }
                                catch (IllegalArgumentException e) {
                                    throw new BuildException("Invalid onMissingExtensionPoint " + value);
                                }
                            }
                            throw new SAXParseException("Unexpected attribute \"" + key + "\"", context.getLocator());
                        }
                        extensionPoint = value;
                    }
                }
            }
            if (name == null) {
                throw new SAXParseException("target element appears without a name attribute", context.getLocator());
            }
            String prefix = null;
            final boolean isInIncludeMode = context.isIgnoringProjectTag() && ProjectHelper.isInIncludeMode();
            final String sep = ProjectHelper.getCurrentPrefixSeparator();
            if (isInIncludeMode) {
                prefix = this.getTargetPrefix(context);
                if (prefix == null) {
                    throw new BuildException("can't include build file " + context.getBuildFileURL() + ", no as attribute has been given" + " and the project tag doesn't" + " specify a name attribute");
                }
                name = prefix + sep + name;
            }
            if (context.getCurrentTargets().get(name) != null) {
                throw new BuildException("Duplicate target '" + name + "'", target.getLocation());
            }
            final Hashtable<String, Target> projectTargets = project.getTargets();
            boolean usedTarget = false;
            if (projectTargets.containsKey(name)) {
                project.log("Already defined in main or a previous import, ignore " + name, 3);
            }
            else {
                target.setName(name);
                context.getCurrentTargets().put(name, target);
                project.addOrReplaceTarget(name, target);
                usedTarget = true;
            }
            if (depends.length() > 0) {
                if (!isInIncludeMode) {
                    target.setDepends(depends);
                }
                else {
                    for (final String string : Target.parseDepends(depends, name, "depends")) {
                        target.addDependency(prefix + sep + string);
                    }
                }
            }
            if (!isInIncludeMode && context.isIgnoringProjectTag() && (prefix = this.getTargetPrefix(context)) != null) {
                final String newName = prefix + sep + name;
                Target newTarget = target;
                if (usedTarget) {
                    newTarget = ("target".equals(tag) ? new Target(target) : new ExtensionPoint(target));
                }
                newTarget.setName(newName);
                context.getCurrentTargets().put(newName, newTarget);
                project.addOrReplaceTarget(newName, newTarget);
            }
            if (extensionPointMissing != null && extensionPoint == null) {
                throw new BuildException("onMissingExtensionPoint attribute cannot be specified unless extensionOf is specified", target.getLocation());
            }
            if (extensionPoint != null) {
                final ProjectHelper helper = context.getProject().getReference("ant.projectHelper");
                for (final String extPointName : Target.parseDepends(extensionPoint, name, "extensionOf")) {
                    if (extensionPointMissing == null) {
                        extensionPointMissing = OnMissingExtensionPoint.FAIL;
                    }
                    if (ProjectHelper.isInIncludeMode()) {
                        helper.getExtensionStack().add(new String[] { extPointName, target.getName(), extensionPointMissing.name(), prefix + sep });
                    }
                    else {
                        helper.getExtensionStack().add(new String[] { extPointName, target.getName(), extensionPointMissing.name() });
                    }
                }
            }
        }
        
        private String getTargetPrefix(final AntXMLContext context) {
            String configuredValue = ProjectHelper.getCurrentTargetPrefix();
            if (configuredValue != null && configuredValue.length() == 0) {
                configuredValue = null;
            }
            if (configuredValue != null) {
                return configuredValue;
            }
            String projectName = context.getCurrentProjectName();
            if ("".equals(projectName)) {
                projectName = null;
            }
            return projectName;
        }
        
        public AntHandler onStartChild(final String uri, final String name, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            return ProjectHelper2.elementHandler;
        }
        
        public void onEndElement(final String uri, final String tag, final AntXMLContext context) {
            context.setCurrentTarget(context.getImplicitTarget());
        }
    }
    
    public static class ElementHandler extends AntHandler
    {
        public void onStartElement(final String uri, final String tag, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            final RuntimeConfigurable parentWrapper = context.currentWrapper();
            Object parent = null;
            if (parentWrapper != null) {
                parent = parentWrapper.getProxy();
            }
            final UnknownElement task = new UnknownElement(tag);
            task.setProject(context.getProject());
            task.setNamespace(uri);
            task.setQName(qname);
            task.setTaskType(ProjectHelper.genComponentName(task.getNamespace(), tag));
            task.setTaskName(qname);
            final Location location = new Location(context.getLocator().getSystemId(), context.getLocator().getLineNumber(), context.getLocator().getColumnNumber());
            task.setLocation(location);
            task.setOwningTarget(context.getCurrentTarget());
            if (parent != null) {
                ((UnknownElement)parent).addChild(task);
            }
            else {
                context.getCurrentTarget().addTask(task);
            }
            context.configureId(task, attrs);
            final RuntimeConfigurable wrapper = new RuntimeConfigurable(task, task.getTaskName());
            for (int i = 0; i < attrs.getLength(); ++i) {
                String name = attrs.getLocalName(i);
                final String attrUri = attrs.getURI(i);
                if (attrUri != null && !attrUri.equals("") && !attrUri.equals(uri)) {
                    name = attrUri + ":" + attrs.getQName(i);
                }
                String value = attrs.getValue(i);
                if ("ant-type".equals(name) || ("antlib:org.apache.tools.ant".equals(attrUri) && "ant-type".equals(attrs.getLocalName(i)))) {
                    name = "ant-type";
                    final int index = value.indexOf(":");
                    if (index >= 0) {
                        final String prefix = value.substring(0, index);
                        final String mappedUri = context.getPrefixMapping(prefix);
                        if (mappedUri == null) {
                            throw new BuildException("Unable to find XML NS prefix \"" + prefix + "\"");
                        }
                        value = ProjectHelper.genComponentName(mappedUri, value.substring(index + 1));
                    }
                }
                wrapper.setAttribute(name, value);
            }
            if (parentWrapper != null) {
                parentWrapper.addChild(wrapper);
            }
            context.pushWrapper(wrapper);
        }
        
        public void characters(final char[] buf, final int start, final int count, final AntXMLContext context) throws SAXParseException {
            final RuntimeConfigurable wrapper = context.currentWrapper();
            wrapper.addText(buf, start, count);
        }
        
        public AntHandler onStartChild(final String uri, final String tag, final String qname, final Attributes attrs, final AntXMLContext context) throws SAXParseException {
            return ProjectHelper2.elementHandler;
        }
        
        public void onEndElement(final String uri, final String tag, final AntXMLContext context) {
            context.popWrapper();
        }
    }
}
