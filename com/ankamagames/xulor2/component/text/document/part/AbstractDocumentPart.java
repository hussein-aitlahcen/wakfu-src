package com.ankamagames.xulor2.component.text.document.part;

import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.util.alignment.*;
import java.util.regex.*;
import java.util.*;
import com.ankamagames.xulor2.component.text.document.*;
import org.apache.commons.lang3.*;
import java.awt.*;
import org.jetbrains.annotations.*;

public abstract class AbstractDocumentPart
{
    private static final String TEXT_ELEMENT_NAME = "text";
    private static final String SELECTABLE_TEXT_ELEMENT_NAME = "selectableText";
    private static final String IMAGE_ELEMENT_NAME = "image";
    private static final String TYPE_ATTRIBUTE_NAME = "type";
    private static final String ALIGN_ATTRIBUTE_NAME = "align";
    private static final Pattern TYPE_PATTERN;
    private static final Pattern ALIGN_PATTERN;
    private DocumentPartType m_type;
    private final boolean m_serializeType;
    private final TextDocument m_document;
    private final AbstractDocumentPart m_parent;
    private Alignment5 m_alignment;
    private String m_data;
    
    public AbstractDocumentPart(final TextDocument document, final AbstractDocumentPart parent, final boolean serializeType) {
        super();
        this.m_type = DocumentPartType.NONE;
        this.m_alignment = null;
        this.m_data = null;
        this.m_document = document;
        this.m_serializeType = serializeType;
        this.m_parent = parent;
    }
    
    protected abstract String getTypeName();
    
    public DocumentPartType getType() {
        return this.m_type;
    }
    
    public TextDocument getDocument() {
        return this.m_document;
    }
    
    protected void setType(final DocumentPartType type) {
        this.m_type = type;
    }
    
    public String getData() {
        return this.m_data;
    }
    
    public void setData(final String data) {
        this.m_data = ((data != null) ? data.intern() : null);
    }
    
    public Alignment5 getAlignment() {
        return this.m_alignment;
    }
    
    public void setAlignment(final Alignment5 alignment) {
        if (alignment != null) {
            this.m_alignment = alignment;
        }
    }
    
    public abstract int getLength();
    
    public boolean unserialize(final Matcher textDataMatcher, final ArrayList<AbstractDocumentPart> toAddAfter) {
        final String data = textDataMatcher.group(5);
        if (data != null) {
            final String element = textDataMatcher.group(2);
            final String attributes = textDataMatcher.group(4);
            this.extractAttributes(element, attributes);
            final Matcher internMatcher = MultiplePartTextDocument.TEXT_DATA_PATTERN.matcher(data);
            boolean forcePartCreation = false;
            while (internMatcher.find()) {
                final AbstractDocumentPart part = ((MultiplePartTextDocument)this.m_document).createDocumentPart(internMatcher, this, forcePartCreation);
                if (part != this) {
                    forcePartCreation = true;
                }
                part.unserialize(internMatcher, toAddAfter);
            }
        }
        else {
            this.setData(this.getDecodedData(textDataMatcher.group(7)));
            this.extractAttributes(null, null);
        }
        return true;
    }
    
    public final String serialize() {
        final StringBuilder serializer = new StringBuilder(512);
        this.serializeAttributes(serializer);
        if (StringUtils.isEmpty(serializer)) {
            return this.getData();
        }
        final StringBuilder builder = new StringBuilder("<").append(this.getTypeName());
        builder.append(serializer.toString()).append('>');
        builder.append(this.getEncodedData());
        builder.append("</").append(this.getTypeName()).append('>');
        return builder.toString();
    }
    
    protected static void appendAttribute(final StringBuilder sb, final String name, final String value) {
        sb.append(' ').append(name);
        sb.append("=\"").append(value).append('\"');
    }
    
    protected static void appendAttribute(final StringBuilder sb, final String name, final int value) {
        sb.append(' ').append(name);
        sb.append("=\"").append(value).append('\"');
    }
    
    protected static void appendAttribute(final StringBuilder sb, final String name, final boolean value) {
        sb.append(' ').append(name);
        sb.append('=').append(value);
    }
    
    protected static void appendAttribute(final StringBuilder sb, final String name, final Color color) {
        sb.append(' ').append(name);
        sb.append("=\"");
        sb.append(Integer.toHexString(color.getRed() & 0xFF));
        sb.append(Integer.toHexString(color.getGreen() >> 8 & 0xFF));
        sb.append(Integer.toHexString(color.getBlue() >> 16 & 0xFF));
        sb.append('\"');
    }
    
    private String getEncodedData() {
        String encodedData = this.getData();
        encodedData = StringUtils.replace(encodedData, "<", "&lt;");
        encodedData = StringUtils.replace(encodedData, ">", "&gt;");
        return encodedData;
    }
    
    private String getDecodedData(final String encodedData) {
        String data = StringUtils.replace(encodedData, "&lt;", "<");
        data = StringUtils.replace(data, "&gt;", ">");
        return data;
    }
    
    protected void serializeAttributes(final StringBuilder sb) {
        if (this.m_alignment != null) {
            appendAttribute(sb, "align", this.m_alignment.toString().toLowerCase());
        }
    }
    
    protected void extractAttributes(final String element, final String attributes) {
        if (this.m_parent != null) {
            this.m_alignment = this.m_parent.m_alignment;
        }
        if (attributes == null) {
            return;
        }
        final Matcher alignMatcher = AbstractDocumentPart.ALIGN_PATTERN.matcher(attributes);
        if (alignMatcher.find()) {
            final Alignment5 alignment = Alignment5.valueOf(alignMatcher.group(1).toUpperCase());
            if (alignment != null) {
                this.setAlignment(alignment);
            }
        }
    }
    
    public static DocumentPartType extractTypeFromAttributes(final String element) {
        DocumentPartType documentPartType = DocumentPartType.TEXT;
        if (element != null && element.length() != 0) {
            final Matcher typeMatcher = AbstractDocumentPart.TYPE_PATTERN.matcher(element);
            if (typeMatcher.find()) {
                documentPartType = DocumentPartType.valueOf(typeMatcher.group(1).toUpperCase());
            }
        }
        return documentPartType;
    }
    
    public abstract boolean removeSubPartFromIndex(final int p0);
    
    public abstract boolean removeSubPartToIndex(final int p0);
    
    public abstract boolean removeSubPartFromToIndices(final int p0, final int p1);
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " data=" + this.getData();
    }
    
    public AbstractDocumentPart getParent() {
        return this.m_parent;
    }
    
    @Nullable
    public AbstractDocumentPart getParentOfType(final DocumentPartType type) {
        if (this.m_parent == null) {
            return null;
        }
        if (this.m_parent.getType() == type) {
            return this.m_parent;
        }
        return this.m_parent.getParentOfType(type);
    }
    
    public abstract void clean();
    
    static {
        TYPE_PATTERN = Pattern.compile("(text|image|selectableText)");
        ALIGN_PATTERN = Pattern.compile("align=\"(west|center|east)\"");
    }
}
