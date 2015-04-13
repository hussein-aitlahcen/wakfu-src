package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.property.*;
import java.util.*;

public class MacroInstance extends Task implements DynamicAttribute, TaskContainer
{
    private MacroDef macroDef;
    private Map<String, String> map;
    private Map<String, MacroDef.TemplateElement> nsElements;
    private Map<String, UnknownElement> presentElements;
    private Hashtable<String, String> localAttributes;
    private String text;
    private String implicitTag;
    private List<Task> unknownElements;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_EXPECT_BRACKET = 1;
    private static final int STATE_EXPECT_NAME = 2;
    
    public MacroInstance() {
        super();
        this.map = new HashMap<String, String>();
        this.nsElements = null;
        this.text = null;
        this.implicitTag = null;
        this.unknownElements = new ArrayList<Task>();
    }
    
    public void setMacroDef(final MacroDef macroDef) {
        this.macroDef = macroDef;
    }
    
    public MacroDef getMacroDef() {
        return this.macroDef;
    }
    
    public void setDynamicAttribute(final String name, final String value) {
        this.map.put(name, value);
    }
    
    public Object createDynamicElement(final String name) throws BuildException {
        throw new BuildException("Not implemented any more");
    }
    
    private Map<String, MacroDef.TemplateElement> getNsElements() {
        if (this.nsElements == null) {
            this.nsElements = new HashMap<String, MacroDef.TemplateElement>();
            for (final Map.Entry<String, MacroDef.TemplateElement> entry : this.macroDef.getElements().entrySet()) {
                this.nsElements.put(entry.getKey(), entry.getValue());
                final MacroDef.TemplateElement te = entry.getValue();
                if (te.isImplicit()) {
                    this.implicitTag = te.getName();
                }
            }
        }
        return this.nsElements;
    }
    
    public void addTask(final Task nestedTask) {
        this.unknownElements.add(nestedTask);
    }
    
    private void processTasks() {
        if (this.implicitTag != null) {
            return;
        }
        for (final UnknownElement ue : this.unknownElements) {
            final String name = ProjectHelper.extractNameFromComponentName(ue.getTag()).toLowerCase(Locale.ENGLISH);
            if (this.getNsElements().get(name) == null) {
                throw new BuildException("unsupported element " + name);
            }
            if (this.presentElements.get(name) != null) {
                throw new BuildException("Element " + name + " already present");
            }
            this.presentElements.put(name, ue);
        }
    }
    
    private String macroSubs(final String s, final Map<String, String> macroMapping) {
        if (s == null) {
            return null;
        }
        final StringBuffer ret = new StringBuffer();
        StringBuffer macroName = null;
        int state = 0;
        for (int i = 0; i < s.length(); ++i) {
            final char ch = s.charAt(i);
            switch (state) {
                case 0: {
                    if (ch == '@') {
                        state = 1;
                        break;
                    }
                    ret.append(ch);
                    break;
                }
                case 1: {
                    if (ch == '{') {
                        state = 2;
                        macroName = new StringBuffer();
                        break;
                    }
                    if (ch == '@') {
                        state = 0;
                        ret.append('@');
                        break;
                    }
                    state = 0;
                    ret.append('@');
                    ret.append(ch);
                    break;
                }
                case 2: {
                    if (ch == '}') {
                        state = 0;
                        final String name = macroName.toString().toLowerCase(Locale.ENGLISH);
                        final String value = macroMapping.get(name);
                        if (value == null) {
                            ret.append("@{");
                            ret.append(name);
                            ret.append("}");
                        }
                        else {
                            ret.append(value);
                        }
                        macroName = null;
                        break;
                    }
                    macroName.append(ch);
                    break;
                }
            }
        }
        switch (state) {
            case 1: {
                ret.append('@');
                break;
            }
            case 2: {
                ret.append("@{");
                ret.append(macroName.toString());
                break;
            }
        }
        return ret.toString();
    }
    
    public void addText(final String text) {
        this.text = text;
    }
    
    private UnknownElement copy(final UnknownElement ue, final boolean nested) {
        final UnknownElement ret = new UnknownElement(ue.getTag());
        ret.setNamespace(ue.getNamespace());
        ret.setProject(this.getProject());
        ret.setQName(ue.getQName());
        ret.setTaskType(ue.getTaskType());
        ret.setTaskName(ue.getTaskName());
        ret.setLocation(this.macroDef.getBackTrace() ? ue.getLocation() : this.getLocation());
        if (this.getOwningTarget() == null) {
            final Target t = new Target();
            t.setProject(this.getProject());
            ret.setOwningTarget(t);
        }
        else {
            ret.setOwningTarget(this.getOwningTarget());
        }
        final RuntimeConfigurable rc = new RuntimeConfigurable(ret, ue.getTaskName());
        rc.setPolyType(ue.getWrapper().getPolyType());
        final Map<String, Object> m = ue.getWrapper().getAttributeMap();
        for (final Map.Entry<String, Object> entry : m.entrySet()) {
            rc.setAttribute(entry.getKey(), this.macroSubs(entry.getValue(), this.localAttributes));
        }
        rc.addText(this.macroSubs(ue.getWrapper().getText().toString(), this.localAttributes));
        final Enumeration<RuntimeConfigurable> e = ue.getWrapper().getChildren();
        while (e.hasMoreElements()) {
            final RuntimeConfigurable r = e.nextElement();
            final UnknownElement unknownElement = (UnknownElement)r.getProxy();
            String tag = unknownElement.getTaskType();
            if (tag != null) {
                tag = tag.toLowerCase(Locale.ENGLISH);
            }
            final MacroDef.TemplateElement templateElement = this.getNsElements().get(tag);
            if (templateElement == null || nested) {
                final UnknownElement child = this.copy(unknownElement, nested);
                rc.addChild(child.getWrapper());
                ret.addChild(child);
            }
            else if (templateElement.isImplicit()) {
                if (this.unknownElements.size() == 0 && !templateElement.isOptional()) {
                    throw new BuildException("Missing nested elements for implicit element " + templateElement.getName());
                }
                final Iterator<Task> i = this.unknownElements.iterator();
                while (i.hasNext()) {
                    final UnknownElement child2 = this.copy(i.next(), true);
                    rc.addChild(child2.getWrapper());
                    ret.addChild(child2);
                }
            }
            else {
                final UnknownElement presentElement = this.presentElements.get(tag);
                if (presentElement == null) {
                    if (!templateElement.isOptional()) {
                        throw new BuildException("Required nested element " + templateElement.getName() + " missing");
                    }
                    continue;
                }
                else {
                    final String presentText = presentElement.getWrapper().getText().toString();
                    if (!"".equals(presentText)) {
                        rc.addText(this.macroSubs(presentText, this.localAttributes));
                    }
                    final List<UnknownElement> list = presentElement.getChildren();
                    if (list == null) {
                        continue;
                    }
                    final Iterator<UnknownElement> j = list.iterator();
                    while (j.hasNext()) {
                        final UnknownElement child3 = this.copy(j.next(), true);
                        rc.addChild(child3.getWrapper());
                        ret.addChild(child3);
                    }
                }
            }
        }
        return ret;
    }
    
    public void execute() {
        this.presentElements = new HashMap<String, UnknownElement>();
        this.getNsElements();
        this.processTasks();
        this.localAttributes = new Hashtable<String, String>();
        final Set<String> copyKeys = new HashSet<String>(this.map.keySet());
        for (final MacroDef.Attribute attribute : this.macroDef.getAttributes()) {
            String value = this.map.get(attribute.getName());
            if (value == null && "description".equals(attribute.getName())) {
                value = this.getDescription();
            }
            if (value == null) {
                value = attribute.getDefault();
                value = this.macroSubs(value, this.localAttributes);
            }
            if (value == null) {
                throw new BuildException("required attribute " + attribute.getName() + " not set");
            }
            this.localAttributes.put(attribute.getName(), value);
            copyKeys.remove(attribute.getName());
        }
        if (copyKeys.contains("id")) {
            copyKeys.remove("id");
        }
        if (this.macroDef.getText() != null) {
            if (this.text == null) {
                final String defaultText = this.macroDef.getText().getDefault();
                if (!this.macroDef.getText().getOptional() && defaultText == null) {
                    throw new BuildException("required text missing");
                }
                this.text = ((defaultText == null) ? "" : defaultText);
            }
            if (this.macroDef.getText().getTrim()) {
                this.text = this.text.trim();
            }
            this.localAttributes.put(this.macroDef.getText().getName(), this.text);
        }
        else if (this.text != null && !this.text.trim().equals("")) {
            throw new BuildException("The \"" + this.getTaskName() + "\" macro does not support" + " nested text data.");
        }
        if (copyKeys.size() != 0) {
            throw new BuildException("Unknown attribute" + ((copyKeys.size() > 1) ? "s " : " ") + copyKeys);
        }
        final UnknownElement c = this.copy(this.macroDef.getNestedTask(), false);
        c.init();
        final LocalProperties localProperties = LocalProperties.get(this.getProject());
        localProperties.enterScope();
        try {
            c.perform();
        }
        catch (BuildException ex) {
            if (this.macroDef.getBackTrace()) {
                throw ProjectHelper.addLocationToBuildException(ex, this.getLocation());
            }
            ex.setLocation(this.getLocation());
            throw ex;
        }
        finally {
            this.presentElements = null;
            this.localAttributes = null;
            localProperties.exitScope();
        }
    }
    
    public static class Element implements TaskContainer
    {
        private List<Task> unknownElements;
        
        public Element() {
            super();
            this.unknownElements = new ArrayList<Task>();
        }
        
        public void addTask(final Task nestedTask) {
            this.unknownElements.add(nestedTask);
        }
        
        public List<Task> getUnknownElements() {
            return this.unknownElements;
        }
    }
}
