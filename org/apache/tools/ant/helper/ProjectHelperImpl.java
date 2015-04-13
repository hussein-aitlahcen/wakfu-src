package org.apache.tools.ant.helper;

import org.apache.tools.ant.util.*;
import org.xml.sax.helpers.*;
import java.io.*;
import org.xml.sax.*;
import org.apache.tools.ant.*;
import java.util.*;

public class ProjectHelperImpl extends ProjectHelper
{
    private static final FileUtils FILE_UTILS;
    private Parser parser;
    private Project project;
    private File buildFile;
    private File buildFileParent;
    private Locator locator;
    private Target implicitTarget;
    
    public ProjectHelperImpl() {
        super();
        (this.implicitTarget = new Target()).setName("");
    }
    
    public void parse(final Project project, final Object source) throws BuildException {
        if (!(source instanceof File)) {
            throw new BuildException("Only File source supported by default plugin");
        }
        final File bFile = (File)source;
        FileInputStream inputStream = null;
        InputSource inputSource = null;
        this.project = project;
        this.buildFile = new File(bFile.getAbsolutePath());
        this.buildFileParent = new File(this.buildFile.getParent());
        try {
            try {
                this.parser = JAXPUtils.getParser();
            }
            catch (BuildException e) {
                this.parser = new XMLReaderAdapter(JAXPUtils.getXMLReader());
            }
            final String uri = ProjectHelperImpl.FILE_UTILS.toURI(bFile.getAbsolutePath());
            inputStream = new FileInputStream(bFile);
            inputSource = new InputSource(inputStream);
            inputSource.setSystemId(uri);
            project.log("parsing buildfile " + bFile + " with URI = " + uri, 3);
            final HandlerBase hb = new RootHandler(this);
            this.parser.setDocumentHandler(hb);
            this.parser.setEntityResolver(hb);
            this.parser.setErrorHandler(hb);
            this.parser.setDTDHandler(hb);
            this.parser.parse(inputSource);
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
            throw new BuildException(exc.getMessage(), t, location);
        }
        catch (SAXException exc2) {
            final Throwable t2 = exc2.getException();
            if (t2 instanceof BuildException) {
                throw (BuildException)t2;
            }
            throw new BuildException(exc2.getMessage(), t2);
        }
        catch (FileNotFoundException exc3) {
            throw new BuildException(exc3);
        }
        catch (UnsupportedEncodingException exc4) {
            throw new BuildException("Encoding of project file is invalid.", exc4);
        }
        catch (IOException exc5) {
            throw new BuildException("Error reading project file: " + exc5.getMessage(), exc5);
        }
        finally {
            FileUtils.close(inputStream);
        }
    }
    
    private static void handleElement(final ProjectHelperImpl helperImpl, final DocumentHandler parent, final Target target, final String elementName, final AttributeList attrs) throws SAXParseException {
        if (elementName.equals("description")) {
            new DescriptionHandler(helperImpl, parent);
        }
        else if (helperImpl.project.getDataTypeDefinitions().get(elementName) != null) {
            new DataTypeHandler(helperImpl, parent, target).init(elementName, attrs);
        }
        else {
            new TaskHandler(helperImpl, parent, target, null, target).init(elementName, attrs);
        }
    }
    
    private void configureId(final Object target, final AttributeList attr) {
        final String id = attr.getValue("id");
        if (id != null) {
            this.project.addReference(id, target);
        }
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
    
    static class AbstractHandler extends HandlerBase
    {
        protected DocumentHandler parentHandler;
        ProjectHelperImpl helperImpl;
        
        public AbstractHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler) {
            super();
            this.parentHandler = parentHandler;
            this.helperImpl = helperImpl;
            helperImpl.parser.setDocumentHandler(this);
        }
        
        public void startElement(final String tag, final AttributeList attrs) throws SAXParseException {
            throw new SAXParseException("Unexpected element \"" + tag + "\"", this.helperImpl.locator);
        }
        
        public void characters(final char[] buf, final int start, final int count) throws SAXParseException {
            final String s = new String(buf, start, count).trim();
            if (s.length() > 0) {
                throw new SAXParseException("Unexpected text \"" + s + "\"", this.helperImpl.locator);
            }
        }
        
        public void endElement(final String name) throws SAXException {
            this.helperImpl.parser.setDocumentHandler(this.parentHandler);
        }
    }
    
    static class RootHandler extends HandlerBase
    {
        ProjectHelperImpl helperImpl;
        
        public RootHandler(final ProjectHelperImpl helperImpl) {
            super();
            this.helperImpl = helperImpl;
        }
        
        public InputSource resolveEntity(final String publicId, final String systemId) {
            this.helperImpl.project.log("resolving systemId: " + systemId, 3);
            if (systemId.startsWith("file:")) {
                final String path = ProjectHelperImpl.FILE_UTILS.fromURI(systemId);
                File file = new File(path);
                if (!file.isAbsolute()) {
                    file = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, path);
                    this.helperImpl.project.log("Warning: '" + systemId + "' in " + this.helperImpl.buildFile + " should be expressed simply as '" + path.replace('\\', '/') + "' for compliance with other XML tools", 1);
                }
                try {
                    final InputSource inputSource = new InputSource(new FileInputStream(file));
                    inputSource.setSystemId(ProjectHelperImpl.FILE_UTILS.toURI(file.getAbsolutePath()));
                    return inputSource;
                }
                catch (FileNotFoundException fne) {
                    this.helperImpl.project.log(file.getAbsolutePath() + " could not be found", 1);
                }
            }
            return null;
        }
        
        public void startElement(final String tag, final AttributeList attrs) throws SAXParseException {
            if (tag.equals("project")) {
                new ProjectHandler(this.helperImpl, this).init(tag, attrs);
                return;
            }
            throw new SAXParseException("Config file is not of expected XML type", this.helperImpl.locator);
        }
        
        public void setDocumentLocator(final Locator locator) {
            this.helperImpl.locator = locator;
        }
    }
    
    static class ProjectHandler extends AbstractHandler
    {
        public ProjectHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler) {
            super(helperImpl, parentHandler);
        }
        
        public void init(final String tag, final AttributeList attrs) throws SAXParseException {
            String def = null;
            String name = null;
            String id = null;
            String baseDir = null;
            for (int i = 0; i < attrs.getLength(); ++i) {
                final String key = attrs.getName(i);
                final String value = attrs.getValue(i);
                if (key.equals("default")) {
                    def = value;
                }
                else if (key.equals("name")) {
                    name = value;
                }
                else if (key.equals("id")) {
                    id = value;
                }
                else {
                    if (!key.equals("basedir")) {
                        throw new SAXParseException("Unexpected attribute \"" + attrs.getName(i) + "\"", this.helperImpl.locator);
                    }
                    baseDir = value;
                }
            }
            if (def != null && !def.equals("")) {
                this.helperImpl.project.setDefault(def);
                if (name != null) {
                    this.helperImpl.project.setName(name);
                    this.helperImpl.project.addReference(name, this.helperImpl.project);
                }
                if (id != null) {
                    this.helperImpl.project.addReference(id, this.helperImpl.project);
                }
                if (this.helperImpl.project.getProperty("basedir") != null) {
                    this.helperImpl.project.setBasedir(this.helperImpl.project.getProperty("basedir"));
                }
                else if (baseDir == null) {
                    this.helperImpl.project.setBasedir(this.helperImpl.buildFileParent.getAbsolutePath());
                }
                else if (new File(baseDir).isAbsolute()) {
                    this.helperImpl.project.setBasedir(baseDir);
                }
                else {
                    final File resolvedBaseDir = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, baseDir);
                    this.helperImpl.project.setBaseDir(resolvedBaseDir);
                }
                this.helperImpl.project.addTarget("", this.helperImpl.implicitTarget);
                return;
            }
            throw new BuildException("The default attribute is required");
        }
        
        public void startElement(final String name, final AttributeList attrs) throws SAXParseException {
            if (name.equals("target")) {
                this.handleTarget(name, attrs);
            }
            else {
                handleElement(this.helperImpl, this, this.helperImpl.implicitTarget, name, attrs);
            }
        }
        
        private void handleTarget(final String tag, final AttributeList attrs) throws SAXParseException {
            new TargetHandler(this.helperImpl, this).init(tag, attrs);
        }
    }
    
    static class TargetHandler extends AbstractHandler
    {
        private Target target;
        
        public TargetHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler) {
            super(helperImpl, parentHandler);
        }
        
        public void init(final String tag, final AttributeList attrs) throws SAXParseException {
            String name = null;
            String depends = "";
            String ifCond = null;
            String unlessCond = null;
            String id = null;
            String description = null;
            for (int i = 0; i < attrs.getLength(); ++i) {
                final String key = attrs.getName(i);
                final String value = attrs.getValue(i);
                if (key.equals("name")) {
                    name = value;
                    if (name.equals("")) {
                        throw new BuildException("name attribute must not be empty", new Location(this.helperImpl.locator));
                    }
                }
                else if (key.equals("depends")) {
                    depends = value;
                }
                else if (key.equals("if")) {
                    ifCond = value;
                }
                else if (key.equals("unless")) {
                    unlessCond = value;
                }
                else if (key.equals("id")) {
                    id = value;
                }
                else {
                    if (!key.equals("description")) {
                        throw new SAXParseException("Unexpected attribute \"" + key + "\"", this.helperImpl.locator);
                    }
                    description = value;
                }
            }
            if (name == null) {
                throw new SAXParseException("target element appears without a name attribute", this.helperImpl.locator);
            }
            (this.target = new Target()).addDependency("");
            this.target.setName(name);
            this.target.setIf(ifCond);
            this.target.setUnless(unlessCond);
            this.target.setDescription(description);
            this.helperImpl.project.addTarget(name, this.target);
            if (id != null && !id.equals("")) {
                this.helperImpl.project.addReference(id, this.target);
            }
            if (depends.length() > 0) {
                this.target.setDepends(depends);
            }
        }
        
        public void startElement(final String name, final AttributeList attrs) throws SAXParseException {
            handleElement(this.helperImpl, this, this.target, name, attrs);
        }
    }
    
    static class DescriptionHandler extends AbstractHandler
    {
        public DescriptionHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler) {
            super(helperImpl, parentHandler);
        }
        
        public void characters(final char[] buf, final int start, final int count) {
            final String text = new String(buf, start, count);
            final String currentDescription = this.helperImpl.project.getDescription();
            if (currentDescription == null) {
                this.helperImpl.project.setDescription(text);
            }
            else {
                this.helperImpl.project.setDescription(currentDescription + text);
            }
        }
    }
    
    static class TaskHandler extends AbstractHandler
    {
        private Target target;
        private TaskContainer container;
        private Task task;
        private RuntimeConfigurable parentWrapper;
        private RuntimeConfigurable wrapper;
        
        public TaskHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler, final TaskContainer container, final RuntimeConfigurable parentWrapper, final Target target) {
            super(helperImpl, parentHandler);
            this.wrapper = null;
            this.container = container;
            this.parentWrapper = parentWrapper;
            this.target = target;
        }
        
        public void init(final String tag, final AttributeList attrs) throws SAXParseException {
            try {
                this.task = this.helperImpl.project.createTask(tag);
            }
            catch (BuildException ex) {}
            if (this.task == null) {
                (this.task = new UnknownElement(tag)).setProject(this.helperImpl.project);
                this.task.setTaskName(tag);
            }
            this.task.setLocation(new Location(this.helperImpl.locator));
            this.helperImpl.configureId(this.task, attrs);
            this.task.setOwningTarget(this.target);
            this.container.addTask(this.task);
            this.task.init();
            (this.wrapper = this.task.getRuntimeConfigurableWrapper()).setAttributes(attrs);
            if (this.parentWrapper != null) {
                this.parentWrapper.addChild(this.wrapper);
            }
        }
        
        public void characters(final char[] buf, final int start, final int count) {
            this.wrapper.addText(buf, start, count);
        }
        
        public void startElement(final String name, final AttributeList attrs) throws SAXParseException {
            if (this.task instanceof TaskContainer) {
                new TaskHandler(this.helperImpl, this, (TaskContainer)this.task, this.wrapper, this.target).init(name, attrs);
            }
            else {
                new NestedElementHandler(this.helperImpl, this, this.task, this.wrapper, this.target).init(name, attrs);
            }
        }
    }
    
    static class NestedElementHandler extends AbstractHandler
    {
        private Object parent;
        private Object child;
        private RuntimeConfigurable parentWrapper;
        private RuntimeConfigurable childWrapper;
        private Target target;
        
        public NestedElementHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler, final Object parent, final RuntimeConfigurable parentWrapper, final Target target) {
            super(helperImpl, parentHandler);
            this.childWrapper = null;
            if (parent instanceof TypeAdapter) {
                this.parent = ((TypeAdapter)parent).getProxy();
            }
            else {
                this.parent = parent;
            }
            this.parentWrapper = parentWrapper;
            this.target = target;
        }
        
        public void init(final String propType, final AttributeList attrs) throws SAXParseException {
            final Class<?> parentClass = this.parent.getClass();
            final IntrospectionHelper ih = IntrospectionHelper.getHelper(this.helperImpl.project, parentClass);
            try {
                final String elementName = propType.toLowerCase(Locale.ENGLISH);
                if (this.parent instanceof UnknownElement) {
                    final UnknownElement uc = new UnknownElement(elementName);
                    uc.setProject(this.helperImpl.project);
                    ((UnknownElement)this.parent).addChild(uc);
                    this.child = uc;
                }
                else {
                    this.child = ih.createElement(this.helperImpl.project, this.parent, elementName);
                }
                this.helperImpl.configureId(this.child, attrs);
                (this.childWrapper = new RuntimeConfigurable(this.child, propType)).setAttributes(attrs);
                this.parentWrapper.addChild(this.childWrapper);
            }
            catch (BuildException exc) {
                throw new SAXParseException(exc.getMessage(), this.helperImpl.locator, exc);
            }
        }
        
        public void characters(final char[] buf, final int start, final int count) {
            this.childWrapper.addText(buf, start, count);
        }
        
        public void startElement(final String name, final AttributeList attrs) throws SAXParseException {
            if (this.child instanceof TaskContainer) {
                new TaskHandler(this.helperImpl, this, (TaskContainer)this.child, this.childWrapper, this.target).init(name, attrs);
            }
            else {
                new NestedElementHandler(this.helperImpl, this, this.child, this.childWrapper, this.target).init(name, attrs);
            }
        }
    }
    
    static class DataTypeHandler extends AbstractHandler
    {
        private Target target;
        private Object element;
        private RuntimeConfigurable wrapper;
        
        public DataTypeHandler(final ProjectHelperImpl helperImpl, final DocumentHandler parentHandler, final Target target) {
            super(helperImpl, parentHandler);
            this.wrapper = null;
            this.target = target;
        }
        
        public void init(final String propType, final AttributeList attrs) throws SAXParseException {
            try {
                this.element = this.helperImpl.project.createDataType(propType);
                if (this.element == null) {
                    throw new BuildException("Unknown data type " + propType);
                }
                (this.wrapper = new RuntimeConfigurable(this.element, propType)).setAttributes(attrs);
                this.target.addDataType(this.wrapper);
            }
            catch (BuildException exc) {
                throw new SAXParseException(exc.getMessage(), this.helperImpl.locator, exc);
            }
        }
        
        public void characters(final char[] buf, final int start, final int count) {
            this.wrapper.addText(buf, start, count);
        }
        
        public void startElement(final String name, final AttributeList attrs) throws SAXParseException {
            new NestedElementHandler(this.helperImpl, this, this.element, this.wrapper, this.target).init(name, attrs);
        }
    }
}
