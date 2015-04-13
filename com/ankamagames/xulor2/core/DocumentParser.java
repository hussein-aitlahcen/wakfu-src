package com.ankamagames.xulor2.core;

import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import java.net.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.core.factory.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.xulor2.*;
import java.lang.reflect.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.regex.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import java.io.*;
import com.ankamagames.xulor2.util.xmlToJava.init.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.converter.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;

public class DocumentParser
{
    private static Logger m_logger;
    private static final boolean DEBUG = false;
    private boolean m_profileThemeUsage;
    private HashMap<String, Integer> m_stylesNumReferences;
    private static final Pattern REPLACEMENT_STRING;
    public static final String ATTR_ID = "id";
    public static final String ATTR_INCLUDE_ID = "includeId";
    public static final String ATTR_PATH = "path";
    public static final String ATTR_SIZE = "size";
    public static final String DELTAX = "deltaX";
    public static final String DELTAY = "deltaY";
    public static final String ELEM_FORM = "form";
    public static final String ELEM_INCLUDE = "include";
    public static final String ATTR_TEMPLATE_ID = "templateId";
    public static final String ATTR_TEMPLATE_REF = "templateRef";
    public static final String ELEM_TEMPLATE = "template";
    public static final String ELEM_TEMPLATE_ELEMENT = "templateElement";
    public static final String ATTR_TEMPLATE_ELEMENT_IGNORE = "templateElementIgnore";
    public static final String ATTR_ATLAS = "atlas";
    public static final String ATTR_REFERENCE = "ref";
    public static final String ELEM_INIT = "init";
    public static final String ELEM_THEME_ELEMENT = "themeElement";
    public static final String ELEM_TEXTURE = "texture";
    public static final String ELEM_PIXMAP = "pixmap";
    public static final String ATTR_TEXTURE_INIT_AT_ONCE = "initAtOnce";
    public static final String ATTR_TEXTURE = "texture";
    public static final String ELEM_COLOR = "color";
    public static final String ATTR_COLOR = "color";
    public static final String ELEM_CURSOR = "cursor";
    public static final String ELEM_ANIMATED_CURSOR = "animatedCursor";
    public static final String ELEM_CURSOR_FRAME = "cursorFrame";
    public static final String ELEM_TOOLTIP = "tooltip";
    public static final String ATTR_TEXT_COLOR = "textColor";
    public static final String ATTR_BORDER_COLOR = "borderColor";
    public static final String ATTR_BORDER_WIDTH = "borderWidth";
    public static final String ATTR_BACKGROUND_COLOR = "backgroundColor";
    public static final String ATTR_X = "x";
    public static final String ATTR_Y = "y";
    public static final String ATTR_WIDTH = "width";
    public static final String ATTR_HEIGHT = "height";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_DELAY = "delay";
    public static final String ELEM_FONT = "font";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_FONT = "font";
    public static final String ATTR_FONT_BORDERED = "bordered";
    public static final String ATTR_PERMANENT = "permanent";
    public static final String FONT_DEFINITION = "fontDefinition";
    public static final String DESC = "desc";
    public static final String LANG = "lang";
    public static final String DEFINITION = "definition";
    private ElementMap m_currentElementMap;
    private PrintWriter m_writer;
    private ClassDocument m_classDocument;
    private ThemeStyleProviderClassDocument m_themeStyleProvider;
    private ThemeLoader m_themeInitLoader;
    private boolean m_changingStyle;
    private HashMap<String, ArrayList<Rectangle>> m_registeredPixmapCalls;
    private HashMap<String, Color> m_colors;
    private StyleProvider m_styleProvider;
    private static final boolean PIXMAP_DEBUG = false;
    private URL m_currentIncludeURL;
    private URL m_themeFileURL;
    private String m_themeDirectory;
    private final ArrayList<DocumentEntry> m_textureEntries;
    private final HashMap<String, DocumentEntry> m_referencedInitEntries;
    private final HashMap<String, DocumentEntry> m_referencedDocumentEntries;
    private final HashMap<String, TextRenderer> m_fonts;
    private final HashMap<String, FontDefinition> m_fontDefinitions;
    private final HashMap<String, String> m_textures;
    private boolean m_needToLoadTextures;
    private Widget m_currentStyleChangingWidget;
    
    public DocumentParser() {
        super();
        this.m_profileThemeUsage = false;
        this.m_stylesNumReferences = null;
        this.m_currentElementMap = null;
        this.m_classDocument = null;
        this.m_themeStyleProvider = null;
        this.m_themeInitLoader = null;
        this.m_changingStyle = false;
        this.m_colors = new HashMap<String, Color>();
        this.m_styleProvider = null;
        this.m_currentIncludeURL = null;
        this.m_textureEntries = new ArrayList<DocumentEntry>();
        this.m_referencedInitEntries = new HashMap<String, DocumentEntry>();
        this.m_referencedDocumentEntries = new HashMap<String, DocumentEntry>();
        this.m_fonts = new HashMap<String, TextRenderer>();
        this.m_fontDefinitions = new HashMap<String, FontDefinition>();
        this.m_textures = new HashMap<String, String>();
        this.m_needToLoadTextures = false;
        this.m_currentStyleChangingWidget = null;
    }
    
    public StyleProvider getStyleProvider() {
        return this.m_styleProvider;
    }
    
    public void setStyleProvider(final StyleProvider styleProvider) {
        this.m_styleProvider = styleProvider;
    }
    
    public ThemeLoader getThemeInitLoader() {
        return this.m_themeInitLoader;
    }
    
    public void setThemeInitLoader(final ThemeLoader themeInitLoader) {
        this.m_themeInitLoader = themeInitLoader;
    }
    
    public void setProfileThemeUsage(final boolean profileThemeUsage) {
        this.m_profileThemeUsage = profileThemeUsage;
        if (this.m_profileThemeUsage) {
            this.m_stylesNumReferences = new HashMap<String, Integer>();
        }
        else {
            this.m_stylesNumReferences = null;
        }
    }
    
    public EventDispatcher parse(final XMLDocumentContainer doc, final URL currentDirectory, final Environment env, final ElementMap map, final boolean generateClass, final URL parentDirectory, final String className, final String packageName) {
        preProcessDocument(doc, currentDirectory);
        if (generateClass) {
            this.m_currentElementMap = null;
            URL url = null;
            try {
                url = URLUtils.urlCompound(parentDirectory, className + ".java");
            }
            catch (MalformedURLException e1) {
                DocumentParser.m_logger.error((Object)"Exception", (Throwable)e1);
            }
            if (url != null) {
                try {
                    this.m_writer = new PrintWriter(new FileOutputStream(new File(url.getFile())));
                }
                catch (FileNotFoundException e2) {
                    DocumentParser.m_logger.error((Object)"Exception", (Throwable)e2);
                }
                this.m_classDocument = new ClassDocument(this.m_writer, className, packageName, doc.getRootNode());
            }
        }
        final Stack<ElementMap> elementMaps = new Stack<ElementMap>();
        elementMaps.push(map);
        EventDispatcher ed;
        if (generateClass) {
            ed = (EventDispatcher)this.getElementPrecompiled(this.m_classDocument.getRootTagElement(), this.m_classDocument.getRootParent(), env, elementMaps);
            this.m_classDocument.generateClass();
            this.m_classDocument = null;
        }
        else {
            ed = (EventDispatcher)getBasicElement(doc.getRootNode(), null, env, elementMaps);
        }
        return ed;
    }
    
    private static BasicElement getBasicElement(final DocumentEntry tagElement, final EventDispatcher parent, final Environment env, final Stack<ElementMap> elementMaps) {
        final Factory<EventDispatcher> factory = (Factory<EventDispatcher>)XulorTagLibrary.getInstance().getFactory(tagElement.getName());
        if (factory == null) {
            DocumentParser.m_logger.error((Object)("Tag Inconnu : " + tagElement.getName()));
            return null;
        }
        EventDispatcher newElement;
        try {
            newElement = factory.newInstance();
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)("Erreur lors de l'instanciation du tag " + tagElement.getName() + "."));
            return null;
        }
        newElement.preApplyAttributes(tagElement, parent, elementMaps, env);
        newElement.applyAttributes(tagElement);
        newElement.postApplyAttributes(tagElement, parent, elementMaps, env);
        newElement.computeDocumentEntry(tagElement, parent, elementMaps, env);
        return newElement;
    }
    
    private EventDispatcher getElement(DocumentEntry tagElement, EventDispatcher parent, final Environment env, final Stack<ElementMap> elementMaps) {
        boolean themeElementTag = false;
        DocumentEntry oldTagElement = tagElement;
        tagElement = this.getDocumentEntry(tagElement);
        if (tagElement == oldTagElement) {
            oldTagElement = null;
        }
        String tag = null;
        if (tagElement.getName().equalsIgnoreCase("themeElement")) {
            final DocumentEntry entry = tagElement.getParameterByName("name");
            if (entry != null) {
                tag = entry.getStringValue();
            }
            themeElementTag = true;
        }
        if (tag != null && parent != null) {
            Widget w;
            if (parent instanceof Widget) {
                w = (Widget)parent;
            }
            else {
                w = parent.getParentOfType(Widget.class);
            }
            if (w != null) {
                w = w.getWidgetByThemeElementName(tag, false);
                if (w != null) {
                    parent = w;
                }
                else if (!(parent instanceof Widget)) {
                    return null;
                }
            }
        }
        final ArrayList<? extends DocumentEntry> entries = tagElement.getChildren();
        if (themeElementTag) {
            final ArrayList<? extends DocumentEntry> tagChildren = entries;
            for (int i = 0; i < tagChildren.size(); ++i) {
                final DocumentEntry entry2 = (DocumentEntry)tagChildren.get(i);
                if (!entry2.getName().equals("#text") && !entry2.getName().equals("#comment")) {
                    this.getElement(entry2, parent, env, elementMaps);
                }
            }
            if (oldTagElement != null) {
                final ArrayList<? extends DocumentEntry> oldTagChildren = oldTagElement.getChildren();
                for (int j = 0; j < oldTagChildren.size(); ++j) {
                    final DocumentEntry entry3 = (DocumentEntry)oldTagChildren.get(j);
                    if (!entry3.getName().equals("#text") && !entry3.getName().equals("#comment")) {
                        this.getElement(entry3, parent, env, elementMaps);
                    }
                }
            }
            return null;
        }
        final String id = (tagElement.getParameterByName("id") != null && !this.m_changingStyle) ? tagElement.getParameterByName("id").getStringValue().trim() : null;
        ElementMap elementMap = (elementMaps != null) ? elementMaps.peek() : null;
        if (elementMap == null && parent != null) {
            elementMap = parent.getElementMap();
        }
        String elementMapId;
        if (elementMap == null) {
            elementMapId = "";
        }
        else {
            elementMapId = elementMap.getId();
        }
        final Factory<EventDispatcher> factory = (Factory<EventDispatcher>)XulorTagLibrary.getInstance().getFactory(tagElement.getName());
        if (factory == null) {
            DocumentParser.m_logger.error((Object)("Tag Inconnu : " + tagElement.getName()));
            return null;
        }
        EventDispatcher newElement;
        try {
            newElement = factory.newInstance();
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)("Erreur lors de l'instanciation du tag " + tagElement.getName() + "."));
            return null;
        }
        if (newElement instanceof FontElement) {
            final DocumentEntry refEntry = tagElement.getParameterByName("ref");
            if (refEntry != null) {
                ((FontElement)newElement).setRenderer(this.m_fonts.get(refEntry.getStringValue()));
            }
        }
        if (newElement instanceof DecoratorAppearance && parent != null) {
            Widget w2;
            if (parent instanceof Widget) {
                w2 = (Widget)parent;
            }
            else {
                w2 = parent.getParentOfType(Widget.class);
            }
            if (w2 != null && w2.getAppearance() != null) {
                newElement.destroySelfFromParent();
                newElement = w2.getAppearance();
            }
        }
        newElement.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, newElement);
        }
        if (tagElement.getName().equalsIgnoreCase("form")) {
            String formLocalId = null;
            final DocumentEntry entry4 = tagElement.getParameterByName("id");
            if (entry4 != null) {
                formLocalId = entry4.getStringValue();
            }
            else {
                DocumentParser.m_logger.warn((Object)"Attention : l'id du formulaire est nulle.");
            }
            env.openForm(elementMapId + '.' + formLocalId, (Form)newElement);
        }
        final String objName = null;
        this.applyAttributes(newElement, null, null, factory, tagElement.getParameters(), false, env, elementMap);
        if (parent != null) {
            if (parent.isValidAdd(newElement)) {
                parent.addFromXML(newElement);
            }
            else if (newElement.getParent() == null) {
                newElement.destroySelfFromParent();
                return null;
            }
        }
        newElement.onAttributesInitialized();
        for (int k = 0, size = entries.size(); k < size; ++k) {
            final DocumentEntry entry5 = (DocumentEntry)entries.get(k);
            if (!entry5.getName().equals("#text") && !entry5.getName().equals("#comment")) {
                if (entry5.getParameterByName("include") != null) {
                    final String childId = entry5.getParameterByName("includeId").getStringValue();
                    if (childId == null) {
                        DocumentParser.m_logger.error((Object)"Pas d'id pour le tag Include, impossible de l'ajouter");
                    }
                    else {
                        final ElementMap includeElementMap = env.createElementMap(elementMapId + '.' + childId);
                        includeElementMap.setParentElementMap(elementMap);
                        elementMaps.push(includeElementMap);
                        this.getElement(entry5, newElement, env, elementMaps);
                        elementMaps.pop();
                    }
                }
                else {
                    this.getElement(entry5, newElement, env, elementMaps);
                }
            }
        }
        newElement.onChildrenAdded();
        if (tagElement.getName().equals("form")) {
            env.closeForm(elementMapId + '.' + tagElement.getParameterByName("id").getStringValue());
        }
        return newElement;
    }
    
    private BasicElement getElementPrecompiled(final String tagElementName, String parentName, final Environment env, final Stack<ElementMap> elementMaps) {
        DocumentEntry tagElement = (DocumentEntry)this.m_classDocument.getVar(tagElementName);
        BasicElement parent = (BasicElement)this.m_classDocument.getVar(parentName);
        final String oldParentName = parentName;
        final boolean currentMethodFull = this.m_classDocument.isCurrentMethodFull();
        if (currentMethodFull) {
            this.m_classDocument.pushMethod(tagElement, parent, parentName);
        }
        this.m_classDocument.addImport(String.class);
        this.m_classDocument.addImport(Environment.class);
        this.m_classDocument.addImport(ElementMap.class);
        this.m_classDocument.addImport(XulorTagLibrary.class);
        this.m_classDocument.addImport(Widget.class);
        this.m_classDocument.addImport(Factory.class);
        this.m_classDocument.addImport(Xulor.class);
        this.m_classDocument.addImport(FontElement.class);
        boolean themeElementTag = false;
        DocumentEntry oldTagElement = tagElement;
        tagElement = this.getDocumentEntry(tagElement);
        if (tagElement == oldTagElement) {
            oldTagElement = null;
        }
        String tag = null;
        if (tagElement.getName().equalsIgnoreCase("themeElement")) {
            final DocumentEntry entry = tagElement.getParameterByName("name");
            if (entry != null) {
                tag = entry.getStringValue();
            }
            themeElementTag = true;
        }
        boolean testWidgetByThemeElement = false;
        if (tag != null && parent != null) {
            this.m_classDocument.mark();
            final String wName = this.m_classDocument.getUnusedVarName();
            Widget w;
            if (parent instanceof Widget) {
                w = (Widget)parent;
                this.m_classDocument.addGeneratedCommandLine(new ClassVariable(Widget.class, wName, parentName));
            }
            else {
                w = parent.getParentOfType(Widget.class);
                this.m_classDocument.addGeneratedCommandLine(new ClassVariable(Widget.class, wName, parentName + ".getParentOfType(Widget.class)"));
            }
            if (w != null) {
                w = w.getWidgetByThemeElementName(tag, true);
                if (w != null) {
                    testWidgetByThemeElement = true;
                    parent = w;
                    parentName = this.m_classDocument.getUnusedVarName();
                    this.m_classDocument.setVarValue(parentName, w);
                    this.m_classDocument.addGeneratedCommandLine(new ClassVariable(EventDispatcher.class, parentName, wName + ".getWidgetByThemeElementName(\"" + tag + "\", false)"));
                    this.m_classDocument.addGeneratedCommandLine(new RawCommand("if (" + parentName + " != null) {"));
                }
                else if (!(parent instanceof Widget)) {
                    this.m_classDocument.deleteCommandsFromMark();
                    this.m_classDocument.resetMark();
                    if (currentMethodFull) {
                        this.m_classDocument.popMethod();
                    }
                    return null;
                }
            }
            this.m_classDocument.resetMark();
        }
        if (themeElementTag) {
            for (final DocumentEntry entry2 : tagElement.getChildren()) {
                if (!entry2.getName().equals("#text") && !entry2.getName().equals("#comment")) {
                    final String entryName = this.m_classDocument.getUnusedVarName();
                    this.m_classDocument.setVarValue(entryName, entry2);
                    this.getElementPrecompiled(entryName, parentName, env, elementMaps);
                }
            }
            if (oldTagElement != null) {
                for (final DocumentEntry entry2 : oldTagElement.getChildren()) {
                    if (!entry2.getName().equals("#text") && !entry2.getName().equals("#comment")) {
                        final String entryName = this.m_classDocument.getUnusedVarName();
                        this.m_classDocument.setVarValue(entryName, entry2);
                        this.getElementPrecompiled(entryName, parentName, env, elementMaps);
                    }
                }
            }
            if (testWidgetByThemeElement) {
                this.m_classDocument.addGeneratedCommandLine(new RawCommand("}"));
            }
            if (currentMethodFull) {
                this.m_classDocument.popMethod();
            }
            return null;
        }
        final String id = (tagElement.getParameterByName("id") != null) ? tagElement.getParameterByName("id").getStringValue().trim() : null;
        String idName = null;
        ElementMap elementMap = (elementMaps != null) ? elementMaps.peek() : null;
        if (elementMap == null && parent != null) {
            elementMap = parent.getParentOfType(EventDispatcher.class).getElementMap();
        }
        String elementMapId;
        if (elementMap == null) {
            elementMapId = "";
        }
        else {
            elementMapId = elementMap.getId();
        }
        if (id != null) {
            idName = this.m_classDocument.getUnusedVarName();
            this.m_classDocument.addGeneratedCommandLine(new ClassVariable(String.class, idName, "\"" + id + "\""));
        }
        final Factory<BasicElement> factory = (Factory<BasicElement>)XulorTagLibrary.getInstance().getFactory(tagElement.getName());
        if (factory == null) {
            DocumentParser.m_logger.error((Object)("Tag Inconnu : " + tagElement.getName() + " " + this.m_classDocument.getClassName()));
            if (currentMethodFull) {
                this.m_classDocument.popMethod();
            }
            return null;
        }
        final String newElementName = this.m_classDocument.getUnusedVarName();
        BasicElement newElement;
        try {
            this.m_classDocument.mark();
            newElement = factory.newInstance(this.m_classDocument, newElementName);
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)("Erreur lors de l'instanciation du tag " + tagElement.getName() + "."), (Throwable)e);
            this.m_classDocument.resetMark();
            if (currentMethodFull) {
                this.m_classDocument.popMethod();
            }
            return null;
        }
        if (newElement instanceof DecoratorAppearance && parent != null) {
            final DecoratorAppearance decoApp = (DecoratorAppearance)newElement;
            final String wName2 = this.m_classDocument.getUnusedVarName();
            ClassCommand com = null;
            Widget w2;
            if (parent instanceof Widget) {
                w2 = (Widget)parent;
                com = new ClassVariable(Widget.class, wName2, parentName);
            }
            else {
                w2 = parent.getParentOfType(Widget.class);
                com = new ClassVariable(Widget.class, wName2, parentName + ".getParentOfType(Widget.class)");
            }
            if (w2 != null && w2.getAppearance() != null) {
                decoApp.destroySelfFromParent();
                newElement = w2.getAppearance();
                this.m_classDocument.deleteCommandsFromMark();
                this.m_classDocument.resetMark();
                if (com != null) {
                    this.m_classDocument.addGeneratedCommandLine(com);
                    this.m_classDocument.addGeneratedCommandLine(new ClassVariable(DecoratorAppearance.class, newElementName, wName2 + ".getAppearance()"));
                }
            }
        }
        this.m_classDocument.resetMark();
        if (newElement instanceof FontElement) {
            final DocumentEntry refEntry = tagElement.getParameterByName("ref");
            if (refEntry != null) {
                this.m_classDocument.addGeneratedCommandLine(new RawCommand("((FontElement)" + newElementName + ").setRenderer(Xulor.getInstance().getDocumentParser().getFont(\"" + refEntry.getStringValue() + "\"));"));
                ((FontElement)newElement).setRenderer(Xulor.getInstance().getDocumentParser().getFont(refEntry.getStringValue()));
            }
        }
        if (newElement.getElementType() == ElementType.EVENT_DISPATCHER) {
            final EventDispatcher ed = (EventDispatcher)newElement;
            this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "setElementMap", newElementName, new String[] { "elementMap" }));
            ed.setElementMap(elementMap);
            if (elementMap != null && id != null) {
                elementMap.add(id, ed);
            }
            if (id != null) {
                this.m_classDocument.addGeneratedCommandLine(new RawCommand("if (elementMap != null && " + idName + " != null)"));
                this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "add", "elementMap", new String[] { idName, newElementName }));
            }
        }
        if (tagElement.getName().equalsIgnoreCase("form")) {
            String formLocalId = null;
            final DocumentEntry entry3 = tagElement.getParameterByName("id");
            if (entry3 != null) {
                formLocalId = entry3.getStringValue();
            }
            else {
                DocumentParser.m_logger.warn((Object)"Attention : l'id du formulaire est nulle.");
            }
            final String formFullId = elementMapId + '.' + formLocalId;
            this.m_classDocument.addGeneratedCommandLine(new RawCommand("env.openForm((elementMap != null ? elementMap.getId() : \"\") + \"." + formLocalId + "\", (Form) " + newElementName + ");"));
            env.openForm(formFullId, (Form)newElement);
        }
        this.applyAttributes(newElement, newElementName, tagElement.getName(), factory, tagElement.getParameters(), true, env, elementMap);
        if (parent != null) {
            if (!(parent instanceof EventDispatcher) || ((EventDispatcher)parent).isValidAdd(newElement)) {
                this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "addBasicElement", parentName, new String[] { newElementName }));
                parent.addBasicElement(newElement);
            }
            else if (newElement.getBasicParent() == null) {
                this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "release", newElementName));
                newElement.release();
                if (currentMethodFull) {
                    this.m_classDocument.popMethod();
                }
                return null;
            }
        }
        newElement.onAttributesInitialized();
        this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "onAttributesInitialized", newElementName));
        this.m_classDocument.setVarValue(newElementName, newElement);
        final Iterator i$2 = tagElement.getChildren().iterator();
        while (i$2.hasNext()) {
            final DocumentEntry entry3 = i$2.next();
            if (!entry3.getName().equals("#text") && !entry3.getName().equals("#comment")) {
                final String entryName2 = this.m_classDocument.addVar(entry3);
                if (entry3.getParameterByName("include") != null) {
                    final String childId = entry3.getParameterByName("includeId").getStringValue();
                    if (childId == null) {
                        DocumentParser.m_logger.error((Object)"Pas d'id pour le tag Include, impossible de l'ajouter");
                    }
                    else {
                        final String previousElementMapName = this.m_classDocument.getUnusedVarName();
                        this.m_classDocument.addGeneratedCommandLine(new ClassVariable(ElementMap.class, previousElementMapName, "elementMap"));
                        final ElementMap includeElementMap = env.createElementMap(elementMapId + '.' + childId);
                        this.m_classDocument.addGeneratedCommandLine(new ClassVariable(ElementMap.class, "elementMap", "env.createElementMap((elementMap != null ? elementMap.getId() : \"\") + \"." + childId + "\")"));
                        includeElementMap.setParentElementMap(elementMap);
                        this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "setParentElementMap", "elementMap", new String[] { previousElementMapName }));
                        elementMaps.push(includeElementMap);
                        this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "push", "elementMaps", new String[] { "elementMap" }));
                        this.getElementPrecompiled(entryName2, newElementName, env, elementMaps);
                        elementMaps.pop();
                        this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "pop", "elementMaps"));
                        this.m_classDocument.addGeneratedCommandLine(new ClassVariable(ElementMap.class, "elementMap", "elementMaps.peek()"));
                    }
                }
                else {
                    this.getElementPrecompiled(entryName2, newElementName, env, elementMaps);
                }
            }
        }
        newElement.onChildrenAdded();
        this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(null, "onChildrenAdded", newElementName));
        if (tagElement.getName().equals("form")) {
            final String formFullId2 = elementMapId + '.' + tagElement.getParameterByName("id").getStringValue();
            this.m_classDocument.addGeneratedCommandLine(new RawCommand("env.closeForm((elementMap != null ? elementMap.getId() : \"\") + \"." + tagElement.getParameterByName("id").getStringValue() + "\");"));
            env.closeForm(formFullId2);
        }
        if (currentMethodFull) {
            this.m_classDocument.popMethod();
        }
        return newElement;
    }
    
    public static void applyAttributes(final BasicElement obj, final Factory<?> factory, final String attributeName, final String value) {
        final Method method = factory.guessSetter(attributeName);
        if (method != null) {
            final Class<?> paraType = method.getParameterTypes()[0];
            Object para = null;
            try {
                para = ConverterLibrary.getInstance().convert(paraType, value);
                method.invoke(obj, para);
            }
            catch (Exception e) {
                DocumentParser.m_logger.error((Object)("Probl\u00e8me \u00e0 l'invoke :" + method.getName() + ":" + para), (Throwable)e);
            }
        }
    }
    
    private void applyAttributes(final BasicElement obj, final String newElementName, final String tagElement, final Factory<?> factory, final List<? extends DocumentEntry> attributes, final boolean precompiledMode, final Environment env, final ElementMap elementMap) {
        if (precompiledMode) {
            this.m_classDocument.addImport(Class.class);
            this.m_classDocument.addImport(Method.class);
            this.m_classDocument.addImport(Converter.class);
            this.m_classDocument.addImport(ConverterLibrary.class);
        }
        final boolean isAPixmap = obj instanceof PixmapElement;
        String textureId = null;
        for (int size = attributes.size(), i = 0; i < size; ++i) {
            final DocumentEntry attr = (DocumentEntry)attributes.get(i);
            final String attrName = attr.getName();
            if (!"id".equals(attrName) && !"ref".equals(attrName) && !"templateId".equals(attrName)) {
                if (!"templateRef".equals(attrName)) {
                    if (isAPixmap && "texture".equalsIgnoreCase(attrName)) {
                        textureId = attr.getStringValue();
                    }
                    final boolean attributeSet = obj.setXMLAttribute(attrName, attr.getStringValue());
                    if (!attributeSet || precompiledMode) {
                        final Method method = factory.guessSetter(attrName, null, BasicElement.class);
                        String methodName = null;
                        if (method != null) {
                            final Class<?> paraType = method.getParameterTypes()[0];
                            String paraTypeName = null;
                            final Converter converter = ConverterLibrary.getInstance().getConverter(paraType);
                            String converterName = null;
                            if (converter != null) {
                                if (precompiledMode && !converter.canConvertFromScratch()) {
                                    final String factoryName = this.m_classDocument.getUnusedVarName();
                                    this.m_classDocument.addGeneratedCommandLine(new ClassVariable(Factory.class, factoryName, "XulorTagLibrary.getInstance().getFactory(\"" + tagElement + "\")"));
                                    methodName = this.m_classDocument.getUnusedVarName();
                                    this.m_classDocument.addGeneratedCommandLine(new ClassVariable(Method.class, methodName, factoryName + ".guessSetter(\"" + attrName + "\")"));
                                    converterName = this.m_classDocument.getUnusedVarName();
                                    paraTypeName = this.m_classDocument.getUnusedVarName();
                                    this.m_classDocument.addGeneratedCommandLine(new ClassVariable(Class.class, paraTypeName, methodName + ".getParameterTypes()[0]"));
                                    this.m_classDocument.addGeneratedCommandLine(new ClassVariable(Converter.class, converterName, "ConverterLibrary.getInstance().getConverter(" + paraTypeName + ")"));
                                }
                                Object para = null;
                                try {
                                    if (precompiledMode) {
                                        String varName = null;
                                        if (converter.canConvertFromScratch()) {
                                            varName = converter.toJavaCommandLine(this.m_classDocument, this, paraType, attr.getStringValue(), env);
                                        }
                                        else {
                                            para = converter.convert(paraType, attr.getStringValue());
                                            varName = this.m_classDocument.getUnusedVarName();
                                            this.m_classDocument.addGeneratedCommandLine(new ClassVariable(paraType, varName, converterName + ".convert(" + paraTypeName + ", \"" + attr.getStringValue() + "\")"));
                                        }
                                        this.m_classDocument.addGeneratedCommandLine(new ClassMethodCall(method.getDeclaringClass(), method.getName(), newElementName, new String[] { varName }));
                                    }
                                    para = converter.convert(paraType, attr.getStringValue());
                                    method.invoke(obj, para);
                                }
                                catch (Exception e) {
                                    DocumentParser.m_logger.error((Object)("Probl\u00e8me \u00e0 l'invoke :" + method.getName() + ":" + para), (Throwable)e);
                                }
                            }
                        }
                        else if (precompiledMode && !attrName.equals("atlas") && !attrName.equals("includeId") && !attrName.equals("include")) {
                            throw new IllegalArgumentException("Impossible de trouver l'attribut " + attrName);
                        }
                    }
                }
            }
        }
    }
    
    public static void preProcessDocument(final XMLDocumentContainer doc, final URL currentDirectory) {
        final DocumentEntry rootNode = doc.getRootNode();
        final Stack<URL> currentDirectories = new Stack<URL>();
        final HashMap<String, String> templateVars = new HashMap<String, String>();
        currentDirectories.push(currentDirectory);
        final DocumentEntry newEntry = preProcessEntry(rootNode, currentDirectories, templateVars);
        if (newEntry != null) {
            doc.setRootNode((XMLDocumentNode)newEntry);
        }
    }
    
    public static DocumentEntry preProcessEntry(final DocumentEntry entry, final Stack<URL> currentDirectories, final HashMap<String, String> templateVars) {
        if (entry == null) {
            return null;
        }
        DocumentEntry ret = entry;
        final int depth = currentDirectories.size();
        if ("template".equalsIgnoreCase(entry.getName())) {
            final DocumentEntry path = entry.getParameterByName("path");
            if (path != null) {
                for (final XMLNodeAttribute attr : entry.getParameters()) {
                    if (attr.getName().equals("path")) {
                        continue;
                    }
                    templateVars.put(attr.getName(), attr.getStringValue());
                }
                final ArrayList<DocumentEntry> templateElements = entry.getChildrenByName("templateElement");
                final DocumentEntry templateDocument = loadTemplateDocument(path.getStringValue(), currentDirectories, false);
                replaceTemplateElement(templateDocument, entry, templateElements, false, templateVars);
                ret = templateDocument;
            }
        }
        else if ("include".equalsIgnoreCase(entry.getName())) {
            final DocumentEntry path = entry.getParameterByName("path");
            if (path != null) {
                final DocumentEntry templateDocument2 = loadTemplateDocument(path.getStringValue(), currentDirectories, true);
                final DocumentEntry idEntry = entry.getParameterByName("id");
                ret = templateDocument2;
                ret.addParameter(new XMLNodeAttribute("include", ""));
                if (idEntry != null) {
                    ret.addParameter(new XMLNodeAttribute("includeId", idEntry.getStringValue()));
                }
            }
        }
        final ArrayList<DocumentEntry> listBackup = new ArrayList<DocumentEntry>();
        for (int i = ret.getChildren().size() - 1; i >= 0; --i) {
            final DocumentEntry child = (DocumentEntry)ret.getChildren().get(i);
            listBackup.add(child);
        }
        for (int i = listBackup.size() - 1; i >= 0; --i) {
            ret.removeChild(listBackup.get(i));
        }
        for (int i = listBackup.size() - 1; i >= 0; --i) {
            final DocumentEntry child = listBackup.get(i);
            if (!child.getName().equals("#text") && !child.getName().equals("#comment")) {
                final DocumentEntry newEntry = preProcessEntry(child, currentDirectories, templateVars);
                if (newEntry != null) {
                    ret.addChild(newEntry);
                }
                else {
                    ret.addChild(child);
                }
            }
        }
        while (currentDirectories.size() > depth) {
            currentDirectories.pop();
        }
        if (ret == entry) {
            return null;
        }
        return ret;
    }
    
    public static void replaceTemplateElement(final DocumentEntry template, final DocumentEntry parent, final ArrayList<DocumentEntry> toReplace, final boolean fromTemplate, final HashMap<String, String> templateVars) {
        if (template == null || toReplace == null || toReplace.isEmpty()) {
            return;
        }
        for (final XMLNodeAttribute attr : template.getParameters()) {
            final Matcher matcher = DocumentParser.REPLACEMENT_STRING.matcher(attr.getStringValue());
            while (matcher.find()) {
                final String value = templateVars.get(matcher.group(2));
                if (value != null) {
                    attr.setStringValue(StringUtils.replaceVar(attr.getStringValue(), matcher.group(1), value));
                }
            }
        }
        final DocumentEntry refAttribute = template.getParameterByName("templateId");
        if (refAttribute != null) {
            DocumentEntry replace = null;
            for (int i = 0, size = toReplace.size(); i < size; ++i) {
                final DocumentEntry candidateReplace = toReplace.get(i);
                final DocumentEntry idAttribute = candidateReplace.getParameterByName("templateRef");
                if (refAttribute.getStringValue().equalsIgnoreCase(idAttribute.getStringValue())) {
                    replace = candidateReplace;
                    break;
                }
            }
            if (replace != null) {
                final DocumentEntry ignore = replace.getParameterByName("templateElementIgnore");
                if (ignore != null && ignore.getBooleanValue()) {
                    parent.removeChild(template);
                    return;
                }
                final ArrayList<? extends DocumentEntry> children = replace.getChildren();
                for (int j = 0, size2 = children.size(); j < size2; ++j) {
                    template.addChild((DocumentEntry)children.get(j));
                }
                final ArrayList<? extends DocumentEntry> parameters = replace.getParameters();
                for (int k = 0, size3 = parameters.size(); k < size3; ++k) {
                    final DocumentEntry e = (DocumentEntry)parameters.get(k);
                    final String attrName = e.getName();
                    if (!attrName.equals("#text") && !attrName.equals("#comment")) {
                        if (!"templateRef".equalsIgnoreCase(attrName)) {
                            final DocumentEntry sameNameAttr = template.getParameterByName(attrName);
                            if (sameNameAttr != null) {
                                template.removeParameter(sameNameAttr);
                            }
                            template.addParameter(e);
                        }
                    }
                }
            }
            template.removeParameter(refAttribute);
        }
        final boolean isTemplateElement = template.getName().equalsIgnoreCase("templateElement");
        if (fromTemplate && isTemplateElement) {
            return;
        }
        final ArrayList<? extends DocumentEntry> list = template.getChildren();
        for (int l = list.size() - 1; l >= 0; --l) {
            final DocumentEntry entry = (DocumentEntry)list.get(l);
            if (!entry.getName().equals("#text")) {
                if (!entry.getName().equals("#comment")) {
                    final boolean isTemplate = entry.getName().equalsIgnoreCase("template");
                    replaceTemplateElement(entry, template, toReplace, isTemplate, templateVars);
                }
            }
        }
    }
    
    public static DocumentEntry loadTemplateDocument(final String templatePath, final Stack<URL> currentDirectories, final boolean changeCurrentDirectory) {
        URL url = null;
        XMLDocumentContainer doc = null;
        try {
            url = URLUtils.urlCompound(currentDirectories.peek(), templatePath);
            if (changeCurrentDirectory) {
                currentDirectories.push(url);
            }
            doc = Xulor.loadDoc(url);
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)("Impossible de charger le template d'url : " + currentDirectories.peek() + templatePath));
        }
        if (doc != null) {
            return doc.getRootNode();
        }
        return null;
    }
    
    public String getThemeDirectory() {
        return this.m_themeDirectory;
    }
    
    public DocumentEntry getDocumentEntry(final DocumentEntry entry) {
        if (entry == null) {
            return null;
        }
        final DocumentEntry refAttr = entry.getParameterByName("ref");
        DocumentEntry retValue = null;
        if (refAttr != null) {
            retValue = this.m_referencedInitEntries.get(refAttr.getStringValue().toUpperCase());
        }
        if (retValue == null) {
            return entry;
        }
        return retValue;
    }
    
    public DocumentEntry getChildThemeElement(DocumentEntry entry, final String name) {
        if (entry == null) {
            DocumentParser.m_logger.error((Object)"Probl\u00e8me lors de la recherche de ThemeElement : entry est null");
            return null;
        }
        if (name == null) {
            DocumentParser.m_logger.error((Object)"Probl\u00e8me lors de la recherche de ThemeElement : name est null");
            return null;
        }
        entry = this.getDocumentEntry(entry);
        final ArrayList<DocumentEntry> children = getChildrenByName(entry, "themeElement");
        if (children != null) {
            for (DocumentEntry child : children) {
                child = this.getDocumentEntry(child);
                final DocumentEntry attr = child.getParameterByName("name");
                if (attr != null && name.equalsIgnoreCase(attr.getStringValue())) {
                    return child;
                }
            }
        }
        return null;
    }
    
    private ArrayList<StyleParameters> getThemeElements(final DocumentEntry entry, final String name) {
        final ArrayList<StyleParameters> parameters = new ArrayList<StyleParameters>();
        this.getThemeElements(entry, parameters, name);
        return parameters;
    }
    
    private void getThemeElements(DocumentEntry entry, final ArrayList<StyleParameters> parameters, final String currentName) {
        if (entry == null) {
            DocumentParser.m_logger.error((Object)"Probl\u00e8me lors de la recherche de ThemeElement : entry est null");
            return;
        }
        entry = this.getDocumentEntry(entry);
        final DocumentEntry type = entry.getParameterByName("type");
        if (type == null) {
            DocumentParser.m_logger.warn((Object)("type inconnu pour " + currentName));
            return;
        }
        final Factory<?> factory = XulorTagLibrary.getInstance().getFactory(type.getStringValue());
        BasicElement elem = null;
        try {
            elem = (BasicElement)factory.newInstance();
        }
        catch (Exception e) {
            DocumentParser.m_logger.warn((Object)("Probl\u00e8me \u00e0 la g\u00e9n\u00e9ration de " + type.getStringValue()));
            return;
        }
        parameters.add(new StyleParameters(elem, currentName, entry));
        final ArrayList<DocumentEntry> children = getChildrenByName(entry, "themeElement");
        if (children != null) {
            for (final DocumentEntry child : children) {
                final DocumentEntry newChild = this.getDocumentEntry(child);
                if (newChild == entry) {
                    continue;
                }
                final DocumentEntry attr = newChild.getParameterByName("name");
                if (attr == null) {
                    continue;
                }
                final String nameAttr = attr.getStringValue();
                this.getThemeElements(newChild, parameters, currentName + "$" + nameAttr.substring(0, 1).toUpperCase() + nameAttr.substring(1, nameAttr.length()));
            }
        }
    }
    
    public static ArrayList<DocumentEntry> getChildrenByName(final DocumentEntry entry, final String name) {
        ArrayList<DocumentEntry> list = new ArrayList<DocumentEntry>();
        if (entry.getName().equalsIgnoreCase(name)) {
            list.add(entry);
        }
        for (final DocumentEntry child : entry.getChildren()) {
            final ArrayList<DocumentEntry> subList = child.getChildrenByName(name);
            if (subList != null) {
                list.addAll(subList);
            }
        }
        if (list.isEmpty()) {
            list = null;
        }
        return list;
    }
    
    public void reloadTheme() {
        this.resetTheme();
        this.loadTheme();
    }
    
    public void resetTheme() {
        this.m_textureEntries.clear();
        this.m_referencedInitEntries.clear();
        this.m_referencedDocumentEntries.clear();
        this.m_fonts.clear();
        this.m_textures.clear();
    }
    
    private void loadTheme() {
        if (this.m_themeInitLoader == null || this.m_styleProvider == null) {
            final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
            final XMLDocumentContainer doc = new XMLDocumentContainer();
            try {
                accessor.open(new BufferedInputStream(this.m_themeFileURL.openStream()));
                accessor.read(doc, new DocumentEntryParser[0]);
                accessor.close();
            }
            catch (Exception e) {
                DocumentParser.m_logger.error((Object)("Probl\u00e8me lors du chargement du theme " + e.getMessage()));
                throw new IllegalArgumentException("Probl\u00e8me lors du chargement du theme ", e);
            }
            this.loadInitFromXML(doc.getRootNode());
        }
        else {
            this.loadInitDirect();
        }
    }
    
    public void loadThemeFile(final StyleProvider sp, final ThemeLoader tl, final String directory) {
        this.m_styleProvider = sp;
        this.m_themeInitLoader = tl;
        this.m_themeDirectory = directory;
        this.m_textureEntries.clear();
        this.loadTheme();
    }
    
    public void generateThemeFile(final URL url, final String directory, final ThemeCompileData data) {
        this.m_themeFileURL = url;
        this.m_themeDirectory = directory;
        this.m_textureEntries.clear();
        XMLDocumentContainer doc = null;
        try {
            doc = Xulor.loadDoc(this.m_themeFileURL);
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)("Probl\u00e8me lors du chargement du theme " + e.getMessage()));
        }
        final DocumentEntry themeRoot = doc.getRootNode();
        this.loadInitFromXML(themeRoot);
        this.compileTheme(themeRoot, data);
    }
    
    public void loadThemeFile(final URL url, final String directory) {
        this.m_themeFileURL = url;
        this.m_themeDirectory = directory;
        this.m_textureEntries.clear();
        this.loadTheme();
    }
    
    public Widget getCurrentStyleChangingWidget() {
        return this.m_currentStyleChangingWidget;
    }
    
    public String getThemeProfiling() {
        if (this.m_profileThemeUsage) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Styles non utilis\u00e9s : \n");
            final ArrayList<String> unused = new ArrayList<String>();
            for (final Map.Entry<String, Integer> entry : this.m_stylesNumReferences.entrySet()) {
                if (entry.getValue() == 0) {
                    unused.add(entry.getKey());
                }
            }
            Collections.sort(unused);
            for (int i = 0, size = unused.size(); i < size; ++i) {
                sb.append(unused.get(i)).append("\n");
            }
            return sb.toString();
        }
        return null;
    }
    
    public void changeStyle(final Widget w, final String style) {
        if (this.m_styleProvider != null) {
            this.changeStyleDirect(w, style);
        }
        else {
            this.changeStyleXML(w, style);
        }
    }
    
    private void changeStyleXML(final Widget w, final String style) {
        final String[] stylePath = StringUtils.split(style, "\\$");
        if (stylePath.length == 1) {
            stylePath[0] = w.getTag() + stylePath[0];
        }
        DocumentEntry entry = this.m_referencedDocumentEntries.get(stylePath[0].toUpperCase());
        if (entry == null) {
            entry = this.m_referencedDocumentEntries.get(w.getTag().toUpperCase());
        }
        else if (this.m_profileThemeUsage) {
            final String styleName = stylePath[0].toUpperCase();
            final Integer numUsages = this.m_stylesNumReferences.get(styleName);
            this.m_stylesNumReferences.put(styleName, numUsages + 1);
        }
        if (this.m_classDocument != null || entry == null) {
            return;
        }
        DocumentEntry themeEntry = entry.getChildByName("themeElement");
        for (int i = 1; i < stylePath.length; ++i) {
            themeEntry = this.getChildThemeElement(themeEntry, stylePath[i]);
        }
        if (themeEntry != null) {
            final Stack<ElementMap> elementMaps = new Stack<ElementMap>();
            elementMaps.push(w.getElementMap());
            this.m_changingStyle = true;
            this.getElement(themeEntry, w, w.getElementMap().getEnvironment(), elementMaps);
            this.m_changingStyle = false;
        }
    }
    
    private void changeStyleDirect(final Widget w, String style) {
        assert this.m_styleProvider != null : "m_styleProvider est null !";
        style = (style.contains("$") ? style.toUpperCase() : (w.getTag().toUpperCase() + style.toUpperCase()));
        StyleSetter styleSetter = this.m_styleProvider.getStyleSetter(style);
        if (styleSetter == null) {
            style = w.getTag().toUpperCase();
            styleSetter = this.m_styleProvider.getStyleSetter(style);
            if (styleSetter == null) {
                return;
            }
        }
        styleSetter.applyStyle(w.getElementMap(), this, w);
    }
    
    public void loadInit(final DocumentEntry themeRoot) {
        if (this.m_themeInitLoader == null || this.m_styleProvider == null) {
            this.loadInitFromXML(themeRoot);
        }
        else {
            this.loadInitDirect();
        }
    }
    
    private void loadInitDirect() {
        assert this.m_themeInitLoader != null && this.m_styleProvider != null : "loadInitDirect : variable mal initialis\u00e9e !";
        this.m_themeInitLoader.initTheme(this);
    }
    
    private void loadInitFromXML(final DocumentEntry themeRoot) {
        for (final DocumentEntry entry : themeRoot.getChildren()) {
            final String entryName = entry.getName();
            if (!entryName.equals("#text")) {
                if (entryName.equals("#comment")) {
                    continue;
                }
                if (entryName.equalsIgnoreCase("init")) {
                    for (final DocumentEntry entry2 : entry.getChildren()) {
                        final String subEntryName = entry2.getName();
                        if (!subEntryName.equals("#text")) {
                            if (subEntryName.equals("#comment")) {
                                continue;
                            }
                            if (subEntryName.equalsIgnoreCase("fontDefinition")) {
                                this.loadFontDefinition(entry2);
                            }
                            else if (subEntryName.equalsIgnoreCase("font")) {
                                this.loadFont(entry2);
                            }
                            else if (subEntryName.equalsIgnoreCase("texture")) {
                                this.m_textureEntries.add(entry2);
                                this.loadTexture(entry2);
                            }
                            else if (subEntryName.equalsIgnoreCase("cursor")) {
                                this.loadCursor(entry2);
                            }
                            else if (subEntryName.equalsIgnoreCase("animatedCursor")) {
                                this.loadAnimatedCursor(entry2);
                            }
                            else if (subEntryName.equalsIgnoreCase("tooltip")) {
                                this.loadTooltip(entry2);
                            }
                            else {
                                final DocumentEntry entryId = entry2.getParameterByName("id");
                                final String id = (entryId != null) ? entryId.getStringValue() : null;
                                if (id == null) {
                                    continue;
                                }
                                this.m_referencedInitEntries.put(id.toUpperCase(), entry2);
                            }
                        }
                    }
                }
                else {
                    final String entryNameUpperCase = entryName.toUpperCase();
                    this.m_referencedDocumentEntries.put(entryNameUpperCase, entry);
                    if (!this.m_profileThemeUsage) {
                        continue;
                    }
                    this.m_stylesNumReferences.put(entryNameUpperCase, 0);
                }
            }
        }
    }
    
    private void compileTheme(final DocumentEntry themeRoot, final ThemeCompileData data) {
        final File file = new File(data.getOutputDirectory() + "\\" + data.getThemeInitLoaderName() + ".java");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                DocumentParser.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
        }
        catch (FileNotFoundException e3) {
            return;
        }
        final ThemeInitClassDocument doc = new ThemeInitClassDocument(writer, data.getThemeInitLoaderName(), data.getPackage(), this);
        final File file2 = new File(data.getOutputDirectory() + "\\" + data.getStyleProviderName() + ".java");
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            }
            catch (IOException e2) {
                DocumentParser.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
        try {
            writer = new PrintWriter(file2);
        }
        catch (FileNotFoundException e4) {
            return;
        }
        this.m_themeStyleProvider = new ThemeStyleProviderClassDocument(writer, data.getStyleProviderName(), data.getPackage());
        for (final DocumentEntry entry : themeRoot.getChildren()) {
            if (!entry.getName().equals("#text") && !entry.getName().equals("#comment")) {
                if (entry.getName().equalsIgnoreCase("init")) {
                    for (final DocumentEntry entry2 : entry.getChildren()) {
                        if (!entry2.getName().equals("#text") && !entry2.getName().equals("#comment")) {
                            if (entry2.getName().equalsIgnoreCase("fontDefinition")) {
                                new FontDefinitionInitLoader(entry2).addToDocument(doc);
                            }
                            else if (entry2.getName().equalsIgnoreCase("font")) {
                                new FontInitLoader(entry2).addToDocument(doc);
                            }
                            else if (entry2.getName().equalsIgnoreCase("texture")) {
                                new TextureInitLoader(entry2).addToDocument(doc);
                            }
                            else if (entry2.getName().equalsIgnoreCase("cursor")) {
                                new CursorInitLoader(entry2).addToDocument(doc);
                            }
                            else if (entry2.getName().equalsIgnoreCase("animatedCursor")) {
                                new AnimatedCursorInitLoader(entry2).addToDocument(doc);
                            }
                            else {
                                if (!entry2.getName().equalsIgnoreCase("tooltip")) {
                                    continue;
                                }
                                new TooltipInitLoader(entry2, this).addToDocument(doc);
                            }
                        }
                    }
                }
                else {
                    this.createPrecompiledStyle(entry, data);
                }
            }
        }
        doc.generateClass();
        this.m_themeStyleProvider.generateClass();
    }
    
    private void createPrecompiledStyle(final DocumentEntry entry, final ThemeCompileData data) {
        URL parentDirectory = null;
        try {
            parentDirectory = ContentFileHelper.getURL("file:" + data.getOutputDirectory());
        }
        catch (MalformedURLException ex) {}
        String className = entry.getName().substring(0, 1).toUpperCase() + entry.getName().substring(1);
        this.m_currentElementMap = null;
        URL url = null;
        final TextureConverter textureConverter = (TextureConverter)ConverterLibrary.getInstance().getConverter(Texture.class);
        textureConverter.setCanConvertFromScratch(true);
        final DocumentEntry themeEntry = entry.getChildByName("themeElement");
        final ArrayList<StyleParameters> parameters = this.getThemeElements(themeEntry, className);
        for (final StyleParameters parameter : parameters) {
            className = parameter.getThemeName();
            try {
                url = URLUtils.urlCompound(parentDirectory, className + ".java");
            }
            catch (MalformedURLException e1) {
                DocumentParser.m_logger.error((Object)"Exception", (Throwable)e1);
            }
            if (url != null) {
                try {
                    this.m_writer = new PrintWriter(new FileOutputStream(new File(url.getFile())));
                }
                catch (FileNotFoundException e2) {
                    DocumentParser.m_logger.error((Object)"Exception", (Throwable)e2);
                }
                this.m_classDocument = new ThemeStyleClassDocument(this.m_writer, className, data.getPackage(), parameter.getThemeElement(), parameter.getWidget(), this);
                final Stack<ElementMap> maps = new Stack<ElementMap>();
                final ElementMap map = new ElementMap("", new Environment());
                maps.push(map);
                this.getElementPrecompiled(this.m_classDocument.getRootTagElement(), this.m_classDocument.getRootParent(), map.getEnvironment(), maps);
                this.m_classDocument.generateClass();
                this.m_themeStyleProvider.addGeneratedCommandLine(new RawCommand("m_setters.put(\"" + className.toUpperCase() + "\", new " + className + "());"));
            }
        }
        textureConverter.setCanConvertFromScratch(false);
    }
    
    private static BasicElement getWidgetByStyleName(final String name) {
        for (int i = 0, size = name.length(); i < size; ++i) {
            final Factory<?> factory = XulorTagLibrary.getInstance().getFactory(name.substring(0, i + 1));
            if (factory != null) {
                try {
                    return (BasicElement)factory.newInstance();
                }
                catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
    
    public void loadTooltip(final float borderWidth, final Color bgColor, final Color textColor, final Color borderColor, final String font) {
        ToolTipElement.DEFAULT_BORDER_WIDTH = borderWidth;
        ToolTipElement.DEFAULT_BACKGROUND_COLOR = bgColor;
        ToolTipElement.DEFAULT_TEXT_COLOR = textColor;
        ToolTipElement.DEFAULT_BORDER_COLOR = borderColor;
        final TextRenderer tr = this.m_fonts.get(font);
        if (tr != null) {
            ToolTipElement.DEFAULT_FONT = tr.getFont();
        }
        if (ToolTipElement.DEFAULT_FONT == null) {
            ToolTipElement.DEFAULT_FONT = FontFactory.createFont(font);
        }
    }
    
    private void loadTooltip(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("tooltip")) {
            return;
        }
        float borderWidth = ToolTipElement.DEFAULT_BORDER_WIDTH;
        DocumentEntry param = entry.getParameterByName("borderWidth");
        if (param != null) {
            borderWidth = param.getFloatValue();
        }
        Color bgColor = ToolTipElement.DEFAULT_BACKGROUND_COLOR;
        final ColorConverter conv = (ColorConverter)ConverterLibrary.getInstance().getConverter(Color.class);
        param = entry.getParameterByName("backgroundColor");
        if (param != null) {
            final Color old = bgColor;
            bgColor = this.getColor(param.getStringValue());
            if (bgColor == null) {
                bgColor = conv.convert((Class<? extends Color>)Color.class, param.getStringValue());
            }
            if (bgColor == null) {
                bgColor = old;
            }
        }
        Color textColor = ToolTipElement.DEFAULT_TEXT_COLOR;
        param = entry.getParameterByName("textColor");
        if (param != null) {
            final Color old2 = textColor;
            textColor = this.getColor(param.getStringValue());
            if (textColor == null) {
                textColor = conv.convert((Class<? extends Color>)Color.class, param.getStringValue());
            }
            if (textColor == null) {
                textColor = old2;
            }
        }
        Color borderColor = ToolTipElement.DEFAULT_BORDER_COLOR;
        param = entry.getParameterByName("borderColor");
        if (param != null) {
            final Color old3 = borderColor;
            borderColor = this.getColor(param.getStringValue());
            if (borderColor == null) {
                borderColor = conv.convert((Class<? extends Color>)Color.class, param.getStringValue());
            }
            if (borderColor == null) {
                borderColor = old3;
            }
        }
        String font = null;
        param = entry.getParameterByName("font");
        if (param != null) {
            font = param.getStringValue();
        }
        this.loadTooltip(borderWidth, bgColor, textColor, borderColor, font);
    }
    
    public void loadFont(final String id, final String fontDefinitionName, final String fontDesc, final boolean bordered) {
        final FontDefinition definition = this.m_fontDefinitions.get(fontDefinitionName);
        String path = definition.getPath();
        String fontName = "default";
        path = path.toLowerCase();
        int nameStart = path.indexOf(47);
        if (nameStart >= 0) {
            ++nameStart;
        }
        fontName = FileHelper.getNameWithoutExt(path) + fontDesc;
        FontFactory.setFontPath(this.m_themeDirectory + path.substring(0, nameStart));
        final String fontType = FontFactory.getType(fontName);
        int fontStyle = FontFactory.getStyle(fontName);
        int fontSize = FontFactory.getSize(fontName);
        if (bordered) {
            fontStyle |= 0x4;
        }
        fontSize += definition.getFontSizeModificator();
        final Font font = FontFactory.createFont(fontType, fontStyle, fontSize, definition.getDeltaX(), definition.getDeltaY(), false);
        this.m_fonts.put(id, TexturedFontRendererFactory.createTextRenderer(font));
    }
    
    private void loadFontDefinition(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("fontDefinition") || entry.getParameterByName("name") == null) {
            return;
        }
        final DocumentEntry nameEntry = entry.getParameterByName("name");
        final String name = nameEntry.getStringValue();
        final ArrayList<DocumentEntry> descs = entry.getChildrenByName("desc");
        for (int i = 0, size = descs.size(); i < size; ++i) {
            final DocumentEntry desc = descs.get(i);
            final DocumentEntry pathAttr = desc.getParameterByName("path");
            final DocumentEntry langAttr = desc.getParameterByName("lang");
            final DocumentEntry sizeAttr = desc.getParameterByName("size");
            final DocumentEntry deltaXAttr = desc.getParameterByName("deltaX");
            final DocumentEntry deltaYAttr = desc.getParameterByName("deltaY");
            if (pathAttr != null) {
                if (langAttr != null) {
                    final String path = pathAttr.getStringValue();
                    final String lang = langAttr.getStringValue();
                    final int fontSize = (sizeAttr != null) ? PrimitiveConverter.getInteger(sizeAttr.getStringValue(), 0) : 0;
                    final int deltaX = (deltaXAttr != null) ? PrimitiveConverter.getInteger(deltaXAttr.getStringValue(), 0) : 0;
                    final int deltaY = (deltaYAttr != null) ? PrimitiveConverter.getInteger(deltaYAttr.getStringValue(), 0) : 0;
                    this.loadFontDefinition(name, path, lang, fontSize, deltaX, deltaY);
                }
            }
        }
    }
    
    public void loadFontDefinition(final String name, final String path, final String lang, final int fontSize, final int deltaX, final int deltaY) {
        FontDefinition def = this.m_fontDefinitions.get(name);
        if (def == null) {
            this.m_fontDefinitions.put(name, def = new FontDefinition(name));
        }
        def.addDefinition(lang, path, fontSize, deltaX, deltaY);
        if (!def.isEmpty()) {
            this.m_fontDefinitions.put(name, def);
        }
    }
    
    private void loadFont(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("font") || entry.getParameterByName("font") == null || entry.getParameterByName("id") == null) {
            return;
        }
        boolean bordered = false;
        if (entry.getParameterByName("bordered") != null) {
            bordered = entry.getParameterByName("bordered").getBooleanValue();
        }
        final DocumentEntry definitionEntry = entry.getParameterByName("definition");
        final String fontAttributes = entry.getParameterByName("font").getStringValue();
        final String id = entry.getParameterByName("id").getStringValue();
        this.loadFont(id, (definitionEntry != null) ? definitionEntry.getStringValue() : null, fontAttributes, bordered);
    }
    
    public void loadColor(final String id, final Color color) {
        this.m_colors.put(id, color);
    }
    
    public void loadColor(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("color") || entry.getParameterByName("color") == null || entry.getParameterByName("id") == null) {
            return;
        }
        final DocumentEntry colorEntry = entry.getParameterByName("color");
        this.loadColor(entry.getParameterByName("id").getStringValue(), ConverterLibrary.getInstance().convertToColor(colorEntry.getStringValue()));
    }
    
    public void loadCursor(final String path, final CursorFactory.CursorType type, final int x, final int y) {
        try {
            final BufferedImage image = ImageIO.read(ContentFileHelper.getURL(this.m_themeDirectory + path));
            CursorFactory.getInstance().addCursor(type, x, y, image);
        }
        catch (Exception e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Impossible de cr\u00e9er le curseur");
            if (path != null) {
                sb.append(" : ").append(path);
            }
            DocumentParser.m_logger.error((Object)sb.toString(), (Throwable)e);
        }
    }
    
    private void loadCursor(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("cursor") || entry.getParameterByName("path") == null || entry.getParameterByName("id") == null) {
            return;
        }
        final DocumentEntry pathEntry = entry.getParameterByName("path");
        if (pathEntry != null) {
            final DocumentEntry xEntry = entry.getParameterByName("x");
            final DocumentEntry yEntry = entry.getParameterByName("y");
            final DocumentEntry cursorTypeEntry = entry.getParameterByName("type");
            final int x = (xEntry == null) ? 0 : xEntry.getIntValue();
            final int y = (yEntry == null) ? 0 : yEntry.getIntValue();
            final CursorFactory.CursorType type = (cursorTypeEntry == null) ? CursorFactory.CursorType.DEFAULT : CursorFactory.CursorType.valueOf(cursorTypeEntry.getStringValue().toUpperCase());
            this.loadCursor(pathEntry.getStringValue(), type, x, y);
        }
    }
    
    public void loadAnimatedCursor(final CursorFactory.CursorType type, final int x, final int y, final int delay, final ArrayList<String> framesPath) {
        try {
            final ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(framesPath.size());
            for (int i = 0, size = framesPath.size(); i < size; ++i) {
                final String path = framesPath.get(i);
                images.add(ImageIO.read(ContentFileHelper.getURL(this.m_themeDirectory + path)));
            }
            CursorFactory.getInstance().addAnimatedCursor(type, x, y, delay, images);
        }
        catch (Exception e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Impossible de cr\u00e9er le curseur");
            DocumentParser.m_logger.error((Object)sb.toString(), (Throwable)e);
        }
    }
    
    private void loadAnimatedCursor(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("animatedCursor") || entry.getParameterByName("id") == null) {
            return;
        }
        try {
            final DocumentEntry xEntry = entry.getParameterByName("x");
            final DocumentEntry yEntry = entry.getParameterByName("y");
            final DocumentEntry delayEntry = entry.getParameterByName("delay");
            final DocumentEntry cursorTypeEntry = entry.getParameterByName("type");
            final int x = (xEntry == null) ? 0 : xEntry.getIntValue();
            final int y = (yEntry == null) ? 0 : yEntry.getIntValue();
            final int delay = (delayEntry == null) ? 500 : delayEntry.getIntValue();
            final CursorFactory.CursorType type = (cursorTypeEntry == null) ? CursorFactory.CursorType.DEFAULT : CursorFactory.CursorType.valueOf(cursorTypeEntry.getStringValue().toUpperCase());
            final ArrayList<DocumentEntry> cursorFrames = entry.getChildrenByName("cursorFrame");
            final ArrayList<String> pathes = new ArrayList<String>(cursorFrames.size());
            for (int i = 0, size = cursorFrames.size(); i < size; ++i) {
                final DocumentEntry cursorFrameEntry = cursorFrames.get(i);
                final DocumentEntry pathEntry = cursorFrameEntry.getParameterByName("path");
                pathes.add(pathEntry.getStringValue());
            }
            this.loadAnimatedCursor(type, x, y, delay, pathes);
        }
        catch (Exception e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Impossible de cr\u00e9er le curseur");
            DocumentParser.m_logger.error((Object)sb.toString(), (Throwable)e);
        }
    }
    
    public void loadTexture(@NotNull final String id, @NotNull final String path, final boolean permanent) {
        final TextureConverter textureConverter = (TextureConverter)ConverterLibrary.getInstance().getConverter(Texture.class);
        try {
            final String fullPath = this.m_themeDirectory + path;
            if (permanent) {
                final Texture texture = textureConverter.convert((Class<? extends Texture>)Texture.class, fullPath);
                texture.addReference();
            }
            this.m_textures.put(id, fullPath);
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)"Impossible de cr\u00e9er l'instance de texture", (Throwable)e);
        }
    }
    
    private void loadTexture(final DocumentEntry entry) {
        if (!entry.getName().equalsIgnoreCase("texture") || entry.getParameterByName("path") == null || entry.getParameterByName("id") == null) {
            return;
        }
        final TextureConverter textureConverter = (TextureConverter)ConverterLibrary.getInstance().getConverter(Texture.class);
        try {
            if (entry.getParameterByName("path") != null) {
                final String path = this.m_themeDirectory + entry.getParameterByName("path").getStringValue();
                final DocumentEntry permanentEntry = entry.getParameterByName("permanent");
                if (permanentEntry != null && permanentEntry.getBooleanValue()) {
                    final Texture texture = textureConverter.convert((Class<? extends Texture>)Texture.class, path);
                    texture.addReference();
                }
                this.m_textures.put(entry.getParameterByName("id").getStringValue(), path);
            }
        }
        catch (Exception e) {
            DocumentParser.m_logger.error((Object)"Impossible de cr\u00e9er l'instance de texture", (Throwable)e);
        }
    }
    
    public void loadTextures() {
        for (final DocumentEntry entry : this.m_textureEntries) {
            this.loadTexture(entry);
        }
        this.m_needToLoadTextures = false;
    }
    
    public boolean needsToLoadTextures() {
        return this.m_needToLoadTextures;
    }
    
    public void setNeedToLoadTextures(final boolean need) {
        this.loadTextures();
    }
    
    public Texture getTexture(final String id) {
        final String textureName = this.m_textures.get(id);
        if (textureName == null) {
            return null;
        }
        Texture texture = TextureManager.getInstance().getTexture(Engine.getTextureName(textureName));
        if (texture == null) {
            final TextureConverter textureConverter = (TextureConverter)ConverterLibrary.getInstance().getConverter(Texture.class);
            texture = textureConverter.convert((Class<? extends Texture>)Texture.class, textureName);
        }
        return texture;
    }
    
    public TextRenderer getFont(final String id) {
        return this.m_fonts.get(id);
    }
    
    public Color getColor(final String id) {
        DocumentEntry entry = this.m_referencedInitEntries.get(id.toUpperCase());
        if (entry == null || !entry.getName().equalsIgnoreCase("color")) {
            return null;
        }
        entry = entry.getParameterByName("color");
        if (entry == null) {
            return null;
        }
        final Converter conv = ConverterLibrary.getInstance().getConverter(Color.class);
        return conv.convert(Color.class, entry.getStringValue());
    }
    
    public void resetPixmapRegistration() {
    }
    
    public void textureLoadDiag() {
    }
    
    static {
        DocumentParser.m_logger = Logger.getLogger((Class)DocumentParser.class);
        REPLACEMENT_STRING = Pattern.compile("(\\$([A-Za-z0-9_\\-]+)\\$)");
    }
    
    private static class StyleParameters
    {
        private BasicElement m_widget;
        private String m_themeName;
        private DocumentEntry m_themeElement;
        
        private StyleParameters(final BasicElement widget, final String themeName, final DocumentEntry themeElement) {
            super();
            this.m_widget = widget;
            this.m_themeName = themeName;
            this.m_themeElement = themeElement;
        }
        
        public BasicElement getWidget() {
            return this.m_widget;
        }
        
        public String getThemeName() {
            return this.m_themeName;
        }
        
        public DocumentEntry getThemeElement() {
            return this.m_themeElement;
        }
    }
}
