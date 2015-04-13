package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ElementReference implements FieldProvider
{
    private static final Logger m_logger;
    private static final EnumMap<Elements, ElementReference> m_basicElements;
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ELEMENT_ICON_URL_FIELD = "elementIconUrl";
    public static final String DECORATOR_STYLE_FIELD = "decoratorStyle";
    public static final String BUTTON_STYLE_FIELD = "buttonStyle";
    public static final String SPELL_DESCRIPTION_FIELD = "spellDescription";
    public static final String STRING_MASTER_FIELD = "stringMaster";
    public static final String ICON_STYLE_MASTER_FIELD = "iconStyleMaster";
    public static final String COLOR_FIELD = "color";
    private final byte m_id;
    protected final Elements m_element;
    public static final String[] FIELDS;
    private static final String STYLE_FIRE = "IconFireElement";
    private static final String STYLE_EARTH = "IconEarthElement";
    private static final String STYLE_AIR = "IconAirElement";
    private static final String STYLE_STASIS = "IconStasisElement";
    private static final String STYLE_WATER = "IconWaterElement";
    private static final String STYLE_SUPPORT = "IconSupportElement";
    private static final Color EARTH_COLOR;
    private static final Color WATER_COLOR;
    private static final Color AIR_COLOR;
    private static final Color FIRE_COLOR;
    private static final Color STASIS_COLOR;
    private static final Color SUPPORT_COLOR;
    
    ElementReference(final Elements element) {
        super();
        this.m_element = element;
        this.m_id = element.getId();
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static ElementReference[] getElementReferencesFromElements(final AvatarBreed breed) {
        final ArrayList<ElementReference> elementReferences = new ArrayList<ElementReference>();
        breed.foreachElement(new TObjectProcedure<Elements>() {
            @Override
            public boolean execute(final Elements element) {
                elementReferences.add(ElementReference.getElementReferenceFromElements(element));
                return true;
            }
        });
        return elementReferences.toArray(new ElementReference[elementReferences.size()]);
    }
    
    public static ElementReference getElementReferenceFromElements(final Elements element) {
        return ElementReference.m_basicElements.get(element);
    }
    
    public static ElementReference getElementReferenceFromId(final byte id) {
        return ElementReference.m_basicElements.get(Elements.getElementFromId(id));
    }
    
    public Elements getElement() {
        return this.m_element;
    }
    
    public String getElementName() {
        return WakfuTranslator.getInstance().getString(this.m_element.name());
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(String.format("%s%s", "characterCreation.elementDescription.", this.m_element.name()));
    }
    
    public String getSpellDescription() {
        return WakfuTranslator.getInstance().getString(String.format("%s%s", "desc.showSpells.", this.m_element.name()));
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getElementName();
        }
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("elementIconUrl")) {
            String url = null;
            try {
                url = String.format(WakfuConfiguration.getInstance().getString("elementsIconsPath"), this.m_element.name());
            }
            catch (PropertyException ex) {}
            return url;
        }
        if (fieldName.equals("decoratorStyle")) {
            return "elementDecorator" + this.getId();
        }
        if (fieldName.equals("buttonStyle")) {
            return this.m_element.name();
        }
        if (fieldName.equals("spellDescription")) {
            return this.getSpellDescription();
        }
        if (fieldName.equalsIgnoreCase("stringMaster")) {
            return this.getElementMasterTranslatorString();
        }
        if (fieldName.equalsIgnoreCase("iconStyleMaster")) {
            return this.getElementMasterStyle();
        }
        if (fieldName.equalsIgnoreCase("color")) {
            return this.getColor();
        }
        return null;
    }
    
    private String getElementMasterTranslatorString() {
        switch (this.m_element.getId()) {
            case 3: {
                return WakfuTranslator.getInstance().getString("desc.earthMaster");
            }
            case 2: {
                return WakfuTranslator.getInstance().getString("desc.waterMaster");
            }
            case 4: {
                return WakfuTranslator.getInstance().getString("desc.airMaster");
            }
            case 1: {
                return WakfuTranslator.getInstance().getString("desc.fireMaster");
            }
            case 0: {
                return WakfuTranslator.getInstance().getString("desc.physicalMaster");
            }
            case 5: {
                return WakfuTranslator.getInstance().getString("desc.stasisMaster");
            }
            case 9: {
                return WakfuTranslator.getInstance().getString("desc.supportMaster");
            }
            default: {
                return null;
            }
        }
    }
    
    private String getElementMasterStyle() {
        try {
            switch (this.m_element.getId()) {
                case 3: {
                    return "IconEarthElement";
                }
                case 2: {
                    return "IconWaterElement";
                }
                case 4: {
                    return "IconAirElement";
                }
                case 1: {
                    return "IconFireElement";
                }
                case 5: {
                    return "IconStasisElement";
                }
                case 0: {
                    return null;
                }
                case 9: {
                    return "IconSupportElement";
                }
                default: {
                    return null;
                }
            }
        }
        catch (Exception exc) {
            ElementReference.m_logger.error((Object)"Unknow Element");
            return null;
        }
    }
    
    private Color getColor() {
        try {
            switch (this.m_element.getId()) {
                case 3: {
                    return ElementReference.EARTH_COLOR;
                }
                case 2: {
                    return ElementReference.WATER_COLOR;
                }
                case 4: {
                    return ElementReference.AIR_COLOR;
                }
                case 1: {
                    return ElementReference.FIRE_COLOR;
                }
                case 5: {
                    return ElementReference.STASIS_COLOR;
                }
                case 0: {
                    return null;
                }
                case 9: {
                    return ElementReference.SUPPORT_COLOR;
                }
                default: {
                    return null;
                }
            }
        }
        catch (Exception exc) {
            ElementReference.m_logger.error((Object)"Unknow Element");
            return null;
        }
    }
    
    @Override
    public String[] getFields() {
        return ElementReference.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)ElementReference.class);
        m_basicElements = new EnumMap<Elements, ElementReference>(Elements.class);
        for (final Elements element : Elements.values()) {
            ElementReference.m_basicElements.put(element, new ElementReference(element));
        }
        FIELDS = new String[] { "name", "description", "elementIconUrl", "decoratorStyle", "spellDescription", "buttonStyle", "stringMaster", "iconStyleMaster", "color" };
        EARTH_COLOR = new Color(0.75f, 1.0f, 0.19f, 1.0f);
        WATER_COLOR = new Color(0.19f, 0.69f, 1.0f, 1.0f);
        AIR_COLOR = new Color(0.57f, 0.29f, 1.0f, 1.0f);
        FIRE_COLOR = new Color(1.0f, 0.67f, 0.19f, 1.0f);
        STASIS_COLOR = new Color(1.0f, 0.35f, 1.0f, 1.0f);
        SUPPORT_COLOR = new Color(0.76f, 0.62f, 0.0f, 1.0f);
    }
}
