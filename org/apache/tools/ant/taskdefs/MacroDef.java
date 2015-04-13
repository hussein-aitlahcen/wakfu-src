package org.apache.tools.ant.taskdefs;

import java.util.*;
import org.apache.tools.ant.*;

public class MacroDef extends AntlibDefinition
{
    private NestedSequential nestedSequential;
    private String name;
    private boolean backTrace;
    private List<Attribute> attributes;
    private Map<String, TemplateElement> elements;
    private String textName;
    private Text text;
    private boolean hasImplicitElement;
    
    public MacroDef() {
        super();
        this.backTrace = true;
        this.attributes = new ArrayList<Attribute>();
        this.elements = new HashMap<String, TemplateElement>();
        this.textName = null;
        this.text = null;
        this.hasImplicitElement = false;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void addConfiguredText(final Text text) {
        if (this.text != null) {
            throw new BuildException("Only one nested text element allowed");
        }
        if (text.getName() == null) {
            throw new BuildException("the text nested element needed a \"name\" attribute");
        }
        for (final Attribute attribute : this.attributes) {
            if (text.getName().equals(attribute.getName())) {
                throw new BuildException("the name \"" + text.getName() + "\" is already used as an attribute");
            }
        }
        this.text = text;
        this.textName = text.getName();
    }
    
    public Text getText() {
        return this.text;
    }
    
    public void setBackTrace(final boolean backTrace) {
        this.backTrace = backTrace;
    }
    
    public boolean getBackTrace() {
        return this.backTrace;
    }
    
    public NestedSequential createSequential() {
        if (this.nestedSequential != null) {
            throw new BuildException("Only one sequential allowed");
        }
        return this.nestedSequential = new NestedSequential();
    }
    
    public UnknownElement getNestedTask() {
        final UnknownElement ret = new UnknownElement("sequential");
        ret.setTaskName("sequential");
        ret.setNamespace("");
        ret.setQName("sequential");
        new RuntimeConfigurable(ret, "sequential");
        for (int size = this.nestedSequential.getNested().size(), i = 0; i < size; ++i) {
            final UnknownElement e = this.nestedSequential.getNested().get(i);
            ret.addChild(e);
            ret.getWrapper().addChild(e.getWrapper());
        }
        return ret;
    }
    
    public List<Attribute> getAttributes() {
        return this.attributes;
    }
    
    public Map<String, TemplateElement> getElements() {
        return this.elements;
    }
    
    public static boolean isValidNameCharacter(final char c) {
        return Character.isLetterOrDigit(c) || c == '.' || c == '-';
    }
    
    private static boolean isValidName(final String name) {
        if (name.length() == 0) {
            return false;
        }
        for (int i = 0; i < name.length(); ++i) {
            if (!isValidNameCharacter(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void addConfiguredAttribute(final Attribute attribute) {
        if (attribute.getName() == null) {
            throw new BuildException("the attribute nested element needed a \"name\" attribute");
        }
        if (attribute.getName().equals(this.textName)) {
            throw new BuildException("the name \"" + attribute.getName() + "\" has already been used by the text element");
        }
        for (int size = this.attributes.size(), i = 0; i < size; ++i) {
            final Attribute att = this.attributes.get(i);
            if (att.getName().equals(attribute.getName())) {
                throw new BuildException("the name \"" + attribute.getName() + "\" has already been used in " + "another attribute element");
            }
        }
        this.attributes.add(attribute);
    }
    
    public void addConfiguredElement(final TemplateElement element) {
        if (element.getName() == null) {
            throw new BuildException("the element nested element needed a \"name\" attribute");
        }
        if (this.elements.get(element.getName()) != null) {
            throw new BuildException("the element " + element.getName() + " has already been specified");
        }
        if (this.hasImplicitElement || (element.isImplicit() && this.elements.size() != 0)) {
            throw new BuildException("Only one element allowed when using implicit elements");
        }
        this.hasImplicitElement = element.isImplicit();
        this.elements.put(element.getName(), element);
    }
    
    public void execute() {
        if (this.nestedSequential == null) {
            throw new BuildException("Missing sequential element");
        }
        if (this.name == null) {
            throw new BuildException("Name not specified");
        }
        this.name = ProjectHelper.genComponentName(this.getURI(), this.name);
        final MyAntTypeDefinition def = new MyAntTypeDefinition(this);
        def.setName(this.name);
        def.setClass(MacroInstance.class);
        final ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
        helper.addDataTypeDefinition(def);
        this.log("creating macro  " + this.name, 3);
    }
    
    private static boolean safeCompare(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    private boolean sameOrSimilar(final Object obj, final boolean same) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        final MacroDef other = (MacroDef)obj;
        if (this.name == null) {
            return other.name == null;
        }
        if (!this.name.equals(other.name)) {
            return false;
        }
        if (other.getLocation() != null && other.getLocation().equals(this.getLocation()) && !same) {
            return true;
        }
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
        }
        else if (!this.text.equals(other.text)) {
            return false;
        }
        if (this.getURI() == null || this.getURI().equals("") || this.getURI().equals("antlib:org.apache.tools.ant")) {
            if (other.getURI() != null && !other.getURI().equals("") && !other.getURI().equals("antlib:org.apache.tools.ant")) {
                return false;
            }
        }
        else if (!this.getURI().equals(other.getURI())) {
            return false;
        }
        return this.nestedSequential.similar(other.nestedSequential) && this.attributes.equals(other.attributes) && this.elements.equals(other.elements);
    }
    
    public boolean similar(final Object obj) {
        return this.sameOrSimilar(obj, false);
    }
    
    public boolean sameDefinition(final Object obj) {
        return this.sameOrSimilar(obj, true);
    }
    
    private static int objectHashCode(final Object o) {
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }
    
    public static class NestedSequential implements TaskContainer
    {
        private List<Task> nested;
        
        public NestedSequential() {
            super();
            this.nested = new ArrayList<Task>();
        }
        
        public void addTask(final Task task) {
            this.nested.add(task);
        }
        
        public List<Task> getNested() {
            return this.nested;
        }
        
        public boolean similar(final NestedSequential other) {
            final int size = this.nested.size();
            if (size != other.nested.size()) {
                return false;
            }
            for (int i = 0; i < size; ++i) {
                final UnknownElement me = this.nested.get(i);
                final UnknownElement o = other.nested.get(i);
                if (!me.similar(o)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static class Attribute
    {
        private String name;
        private String defaultValue;
        private String description;
        private boolean doubleExpanding;
        
        public Attribute() {
            super();
            this.doubleExpanding = true;
        }
        
        public void setName(final String name) {
            if (!isValidName(name)) {
                throw new BuildException("Illegal name [" + name + "] for attribute");
            }
            this.name = name.toLowerCase(Locale.ENGLISH);
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setDefault(final String defaultValue) {
            this.defaultValue = defaultValue;
        }
        
        public String getDefault() {
            return this.defaultValue;
        }
        
        public void setDescription(final String desc) {
            this.description = desc;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public void setDoubleExpanding(final boolean doubleExpanding) {
            this.doubleExpanding = doubleExpanding;
        }
        
        public boolean isDoubleExpanding() {
            return this.doubleExpanding;
        }
        
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            final Attribute other = (Attribute)obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.defaultValue == null) {
                if (other.defaultValue != null) {
                    return false;
                }
            }
            else if (!this.defaultValue.equals(other.defaultValue)) {
                return false;
            }
            return true;
        }
        
        public int hashCode() {
            return objectHashCode(this.defaultValue) + objectHashCode(this.name);
        }
    }
    
    public static class Text
    {
        private String name;
        private boolean optional;
        private boolean trim;
        private String description;
        private String defaultString;
        
        public void setName(final String name) {
            if (!isValidName(name)) {
                throw new BuildException("Illegal name [" + name + "] for attribute");
            }
            this.name = name.toLowerCase(Locale.ENGLISH);
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setOptional(final boolean optional) {
            this.optional = optional;
        }
        
        public boolean getOptional() {
            return this.optional;
        }
        
        public void setTrim(final boolean trim) {
            this.trim = trim;
        }
        
        public boolean getTrim() {
            return this.trim;
        }
        
        public void setDescription(final String desc) {
            this.description = desc;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public void setDefault(final String defaultString) {
            this.defaultString = defaultString;
        }
        
        public String getDefault() {
            return this.defaultString;
        }
        
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            final Text other = (Text)obj;
            return safeCompare(this.name, other.name) && this.optional == other.optional && this.trim == other.trim && safeCompare(this.defaultString, other.defaultString);
        }
        
        public int hashCode() {
            return objectHashCode(this.name);
        }
    }
    
    public static class TemplateElement
    {
        private String name;
        private String description;
        private boolean optional;
        private boolean implicit;
        
        public TemplateElement() {
            super();
            this.optional = false;
            this.implicit = false;
        }
        
        public void setName(final String name) {
            if (!isValidName(name)) {
                throw new BuildException("Illegal name [" + name + "] for macro element");
            }
            this.name = name.toLowerCase(Locale.ENGLISH);
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setDescription(final String desc) {
            this.description = desc;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public void setOptional(final boolean optional) {
            this.optional = optional;
        }
        
        public boolean isOptional() {
            return this.optional;
        }
        
        public void setImplicit(final boolean implicit) {
            this.implicit = implicit;
        }
        
        public boolean isImplicit() {
            return this.implicit;
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(this.getClass())) {
                return false;
            }
            final TemplateElement t = (TemplateElement)obj;
            if (this.name == null) {
                if (t.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(t.name)) {
                return false;
            }
            if (this.optional == t.optional && this.implicit == t.implicit) {
                return true;
            }
            return false;
        }
        
        public int hashCode() {
            return objectHashCode(this.name) + (this.optional ? 1 : 0) + (this.implicit ? 1 : 0);
        }
    }
    
    private static class MyAntTypeDefinition extends AntTypeDefinition
    {
        private MacroDef macroDef;
        
        public MyAntTypeDefinition(final MacroDef macroDef) {
            super();
            this.macroDef = macroDef;
        }
        
        public Object create(final Project project) {
            final Object o = super.create(project);
            if (o == null) {
                return null;
            }
            ((MacroInstance)o).setMacroDef(this.macroDef);
            return o;
        }
        
        public boolean sameDefinition(final AntTypeDefinition other, final Project project) {
            if (!super.sameDefinition(other, project)) {
                return false;
            }
            final MyAntTypeDefinition otherDef = (MyAntTypeDefinition)other;
            return this.macroDef.sameDefinition(otherDef.macroDef);
        }
        
        public boolean similarDefinition(final AntTypeDefinition other, final Project project) {
            if (!super.similarDefinition(other, project)) {
                return false;
            }
            final MyAntTypeDefinition otherDef = (MyAntTypeDefinition)other;
            return this.macroDef.similar(otherDef.macroDef);
        }
    }
}
