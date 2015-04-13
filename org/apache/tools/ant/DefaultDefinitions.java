package org.apache.tools.ant;

public final class DefaultDefinitions
{
    private static final String IF_NAMESPACE = "ant:if";
    private static final String UNLESS_NAMESPACE = "ant:unless";
    private static final String OATA = "org.apache.tools.ant.";
    private final ComponentHelper componentHelper;
    
    public DefaultDefinitions(final ComponentHelper componentHelper) {
        super();
        this.componentHelper = componentHelper;
    }
    
    public void execute() {
        this.attributeNamespaceDef("ant:if");
        this.attributeNamespaceDef("ant:unless");
        this.ifUnlessDef("true", "IfTrueAttribute");
        this.ifUnlessDef("set", "IfSetAttribute");
        this.ifUnlessDef("blank", "IfBlankAttribute");
    }
    
    private void attributeNamespaceDef(final String ns) {
        final AntTypeDefinition def = new AntTypeDefinition();
        def.setName(ProjectHelper.nsToComponentName(ns));
        def.setClassName("org.apache.tools.ant.attribute.AttributeNamespace");
        def.setClassLoader(this.getClass().getClassLoader());
        def.setRestrict(true);
        this.componentHelper.addDataTypeDefinition(def);
    }
    
    private void ifUnlessDef(final String name, final String base) {
        final String classname = "org.apache.tools.ant.attribute." + base;
        this.componentDef("ant:if", name, classname);
        this.componentDef("ant:unless", name, classname + "$Unless");
    }
    
    private void componentDef(final String ns, final String name, final String classname) {
        final AntTypeDefinition def = new AntTypeDefinition();
        final String n = ProjectHelper.genComponentName(ns, name);
        def.setName(ProjectHelper.genComponentName(ns, name));
        def.setClassName(classname);
        def.setClassLoader(this.getClass().getClassLoader());
        def.setRestrict(true);
        this.componentHelper.addDataTypeDefinition(def);
    }
}
