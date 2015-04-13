package org.jdom.output;

import java.util.*;
import java.io.*;
import org.jdom.*;

public class XMLOutputter implements Cloneable
{
    private static final String CVS_ID = "@(#) $RCSfile: XMLOutputter.java,v $ $Revision: 1.112 $ $Date: 2004/09/01 06:08:18 $ $Name: jdom_1_0 $";
    private Format userFormat;
    protected static final Format preserveFormat;
    protected Format currentFormat;
    private boolean escapeOutput;
    
    static {
        preserveFormat = Format.getRawFormat();
    }
    
    public XMLOutputter() {
        super();
        this.userFormat = Format.getRawFormat();
        this.currentFormat = this.userFormat;
        this.escapeOutput = true;
    }
    
    public XMLOutputter(final Format format) {
        super();
        this.userFormat = Format.getRawFormat();
        this.currentFormat = this.userFormat;
        this.escapeOutput = true;
        this.userFormat = (Format)format.clone();
        this.currentFormat = this.userFormat;
    }
    
    public XMLOutputter(final XMLOutputter that) {
        super();
        this.userFormat = Format.getRawFormat();
        this.currentFormat = this.userFormat;
        this.escapeOutput = true;
        this.userFormat = (Format)that.userFormat.clone();
        this.currentFormat = this.userFormat;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    private NamespaceStack createNamespaceStack() {
        return new NamespaceStack();
    }
    
    private boolean endsWithWhite(final String str) {
        return str != null && str.length() > 0 && isWhitespace(str.charAt(str.length() - 1));
    }
    
    public String escapeAttributeEntities(final String str) {
        final EscapeStrategy strategy = this.currentFormat.escapeStrategy;
        StringBuffer buffer = null;
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            String entity = null;
            switch (ch) {
                case '<': {
                    entity = "&lt;";
                    break;
                }
                case '>': {
                    entity = "&gt;";
                    break;
                }
                case '\"': {
                    entity = "&quot;";
                    break;
                }
                case '&': {
                    entity = "&amp;";
                    break;
                }
                case '\r': {
                    entity = "&#xD;";
                    break;
                }
                case '\t': {
                    entity = "&#x9;";
                    break;
                }
                case '\n': {
                    entity = "&#xA;";
                    break;
                }
                default: {
                    if (strategy.shouldEscape(ch)) {
                        entity = "&#x" + Integer.toHexString(ch) + ";";
                        break;
                    }
                    entity = null;
                    break;
                }
            }
            if (buffer == null) {
                if (entity != null) {
                    buffer = new StringBuffer(str.length() + 20);
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            }
            else if (entity == null) {
                buffer.append(ch);
            }
            else {
                buffer.append(entity);
            }
        }
        return (buffer == null) ? str : buffer.toString();
    }
    
    public String escapeElementEntities(final String str) {
        if (!this.escapeOutput) {
            return str;
        }
        final EscapeStrategy strategy = this.currentFormat.escapeStrategy;
        StringBuffer buffer = null;
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            String entity = null;
            switch (ch) {
                case '<': {
                    entity = "&lt;";
                    break;
                }
                case '>': {
                    entity = "&gt;";
                    break;
                }
                case '&': {
                    entity = "&amp;";
                    break;
                }
                case '\r': {
                    entity = "&#xD;";
                    break;
                }
                case '\n': {
                    entity = this.currentFormat.lineSeparator;
                    break;
                }
                default: {
                    if (strategy.shouldEscape(ch)) {
                        entity = "&#x" + Integer.toHexString(ch) + ";";
                        break;
                    }
                    entity = null;
                    break;
                }
            }
            if (buffer == null) {
                if (entity != null) {
                    buffer = new StringBuffer(str.length() + 20);
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            }
            else if (entity == null) {
                buffer.append(ch);
            }
            else {
                buffer.append(entity);
            }
        }
        return (buffer == null) ? str : buffer.toString();
    }
    
    public Format getFormat() {
        return (Format)this.userFormat.clone();
    }
    
    private void indent(final Writer out, final int level) throws IOException {
        if (this.currentFormat.indent == null || this.currentFormat.indent.equals("")) {
            return;
        }
        for (int i = 0; i < level; ++i) {
            out.write(this.currentFormat.indent);
        }
    }
    
    private boolean isAllWhitespace(final Object obj) {
        String str = null;
        if (obj instanceof String) {
            str = (String)obj;
        }
        else {
            if (!(obj instanceof Text)) {
                return obj instanceof EntityRef && false;
            }
            str = ((Text)obj).getText();
        }
        for (int i = 0; i < str.length(); ++i) {
            if (!isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isWhitespace(final char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }
    
    private Writer makeWriter(final OutputStream out) throws UnsupportedEncodingException {
        return makeWriter(out, this.userFormat.encoding);
    }
    
    private static Writer makeWriter(final OutputStream out, String enc) throws UnsupportedEncodingException {
        if ("UTF-8".equals(enc)) {
            enc = "UTF8";
        }
        final Writer writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(out), enc));
        return writer;
    }
    
    private void newline(final Writer out) throws IOException {
        if (this.currentFormat.indent != null) {
            out.write(this.currentFormat.lineSeparator);
        }
    }
    
    private static int nextNonText(final List content, int start) {
        if (start < 0) {
            start = 0;
        }
        int index;
        int size;
        for (index = start, size = content.size(); index < size; ++index) {
            final Object node = content.get(index);
            if (!(node instanceof Text) && !(node instanceof EntityRef)) {
                return index;
            }
        }
        return size;
    }
    
    public void output(final List list, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(list, writer);
    }
    
    public void output(final List list, final Writer out) throws IOException {
        this.printContentRange(out, list, 0, list.size(), 0, this.createNamespaceStack());
        out.flush();
    }
    
    public void output(final CDATA cdata, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(cdata, writer);
    }
    
    public void output(final CDATA cdata, final Writer out) throws IOException {
        this.printCDATA(out, cdata);
        out.flush();
    }
    
    public void output(final Comment comment, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(comment, writer);
    }
    
    public void output(final Comment comment, final Writer out) throws IOException {
        this.printComment(out, comment);
        out.flush();
    }
    
    public void output(final DocType doctype, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(doctype, writer);
    }
    
    public void output(final DocType doctype, final Writer out) throws IOException {
        this.printDocType(out, doctype);
        out.flush();
    }
    
    public void output(final Document doc, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(doc, writer);
    }
    
    public void output(final Document doc, final Writer out) throws IOException {
        this.printDeclaration(out, doc, this.userFormat.encoding);
        final List content = doc.getContent();
        for (int size = content.size(), i = 0; i < size; ++i) {
            final Object obj = content.get(i);
            if (obj instanceof Element) {
                this.printElement(out, doc.getRootElement(), 0, this.createNamespaceStack());
            }
            else if (obj instanceof Comment) {
                this.printComment(out, (Comment)obj);
            }
            else if (obj instanceof ProcessingInstruction) {
                this.printProcessingInstruction(out, (ProcessingInstruction)obj);
            }
            else if (obj instanceof DocType) {
                this.printDocType(out, doc.getDocType());
                out.write(this.currentFormat.lineSeparator);
            }
            this.newline(out);
            this.indent(out, 0);
        }
        out.write(this.currentFormat.lineSeparator);
        out.flush();
    }
    
    public void output(final Element element, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(element, writer);
    }
    
    public void output(final Element element, final Writer out) throws IOException {
        this.printElement(out, element, 0, this.createNamespaceStack());
        out.flush();
    }
    
    public void output(final EntityRef entity, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(entity, writer);
    }
    
    public void output(final EntityRef entity, final Writer out) throws IOException {
        this.printEntityRef(out, entity);
        out.flush();
    }
    
    public void output(final ProcessingInstruction pi, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(pi, writer);
    }
    
    public void output(final ProcessingInstruction pi, final Writer out) throws IOException {
        final boolean currentEscapingPolicy = this.currentFormat.ignoreTrAXEscapingPIs;
        this.currentFormat.setIgnoreTrAXEscapingPIs(true);
        this.printProcessingInstruction(out, pi);
        this.currentFormat.setIgnoreTrAXEscapingPIs(currentEscapingPolicy);
        out.flush();
    }
    
    public void output(final Text text, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.output(text, writer);
    }
    
    public void output(final Text text, final Writer out) throws IOException {
        this.printText(out, text);
        out.flush();
    }
    
    public void outputElementContent(final Element element, final OutputStream out) throws IOException {
        final Writer writer = this.makeWriter(out);
        this.outputElementContent(element, writer);
    }
    
    public void outputElementContent(final Element element, final Writer out) throws IOException {
        final List content = element.getContent();
        this.printContentRange(out, content, 0, content.size(), 0, this.createNamespaceStack());
        out.flush();
    }
    
    public String outputString(final List list) {
        final StringWriter out = new StringWriter();
        try {
            this.output(list, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final CDATA cdata) {
        final StringWriter out = new StringWriter();
        try {
            this.output(cdata, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final Comment comment) {
        final StringWriter out = new StringWriter();
        try {
            this.output(comment, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final DocType doctype) {
        final StringWriter out = new StringWriter();
        try {
            this.output(doctype, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final Document doc) {
        final StringWriter out = new StringWriter();
        try {
            this.output(doc, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final Element element) {
        final StringWriter out = new StringWriter();
        try {
            this.output(element, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final EntityRef entity) {
        final StringWriter out = new StringWriter();
        try {
            this.output(entity, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final ProcessingInstruction pi) {
        final StringWriter out = new StringWriter();
        try {
            this.output(pi, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public String outputString(final Text text) {
        final StringWriter out = new StringWriter();
        try {
            this.output(text, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    private void printAdditionalNamespaces(final Writer out, final Element element, final NamespaceStack namespaces) throws IOException {
        final List list = element.getAdditionalNamespaces();
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                final Namespace additional = list.get(i);
                this.printNamespace(out, additional, namespaces);
            }
        }
    }
    
    protected void printAttributes(final Writer out, final List attributes, final Element parent, final NamespaceStack namespaces) throws IOException {
        for (int i = 0; i < attributes.size(); ++i) {
            final Attribute attribute = attributes.get(i);
            final Namespace ns = attribute.getNamespace();
            if (ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
                this.printNamespace(out, ns, namespaces);
            }
            out.write(" ");
            this.printQualifiedName(out, attribute);
            out.write("=");
            out.write("\"");
            out.write(this.escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
    }
    
    protected void printCDATA(final Writer out, final CDATA cdata) throws IOException {
        final String str = (this.currentFormat.mode == Format.TextMode.NORMALIZE) ? cdata.getTextNormalize() : ((this.currentFormat.mode == Format.TextMode.TRIM) ? cdata.getText().trim() : cdata.getText());
        out.write("<![CDATA[");
        out.write(str);
        out.write("]]>");
    }
    
    protected void printComment(final Writer out, final Comment comment) throws IOException {
        out.write("<!--");
        out.write(comment.getText());
        out.write("-->");
    }
    
    private void printContentRange(final Writer out, final List content, final int start, final int end, final int level, final NamespaceStack namespaces) throws IOException {
        int index = start;
        while (index < end) {
            final boolean firstNode = index == start;
            final Object next = content.get(index);
            if (next instanceof Text || next instanceof EntityRef) {
                final int first = this.skipLeadingWhite(content, index);
                index = nextNonText(content, first);
                if (first >= index) {
                    continue;
                }
                if (!firstNode) {
                    this.newline(out);
                }
                this.indent(out, level);
                this.printTextRange(out, content, first, index);
            }
            else {
                if (!firstNode) {
                    this.newline(out);
                }
                this.indent(out, level);
                if (next instanceof Comment) {
                    this.printComment(out, (Comment)next);
                }
                else if (next instanceof Element) {
                    this.printElement(out, (Element)next, level, namespaces);
                }
                else if (next instanceof ProcessingInstruction) {
                    this.printProcessingInstruction(out, (ProcessingInstruction)next);
                }
                ++index;
            }
        }
    }
    
    protected void printDeclaration(final Writer out, final Document doc, final String encoding) throws IOException {
        if (!this.userFormat.omitDeclaration) {
            out.write("<?xml version=\"1.0\"");
            if (!this.userFormat.omitEncoding) {
                out.write(" encoding=\"" + encoding + "\"");
            }
            out.write("?>");
            out.write(this.currentFormat.lineSeparator);
        }
    }
    
    protected void printDocType(final Writer out, final DocType docType) throws IOException {
        final String publicID = docType.getPublicID();
        final String systemID = docType.getSystemID();
        final String internalSubset = docType.getInternalSubset();
        boolean hasPublic = false;
        out.write("<!DOCTYPE ");
        out.write(docType.getElementName());
        if (publicID != null) {
            out.write(" PUBLIC \"");
            out.write(publicID);
            out.write("\"");
            hasPublic = true;
        }
        if (systemID != null) {
            if (!hasPublic) {
                out.write(" SYSTEM");
            }
            out.write(" \"");
            out.write(systemID);
            out.write("\"");
        }
        if (internalSubset != null && !internalSubset.equals("")) {
            out.write(" [");
            out.write(this.currentFormat.lineSeparator);
            out.write(docType.getInternalSubset());
            out.write("]");
        }
        out.write(">");
    }
    
    protected void printElement(final Writer out, final Element element, final int level, final NamespaceStack namespaces) throws IOException {
        final List attributes = element.getAttributes();
        final List content = element.getContent();
        String space = null;
        if (attributes != null) {
            space = element.getAttributeValue("space", Namespace.XML_NAMESPACE);
        }
        final Format previousFormat = this.currentFormat;
        if ("default".equals(space)) {
            this.currentFormat = this.userFormat;
        }
        else if ("preserve".equals(space)) {
            this.currentFormat = XMLOutputter.preserveFormat;
        }
        out.write("<");
        this.printQualifiedName(out, element);
        final int previouslyDeclaredNamespaces = namespaces.size();
        this.printElementNamespace(out, element, namespaces);
        this.printAdditionalNamespaces(out, element, namespaces);
        if (attributes != null) {
            this.printAttributes(out, attributes, element, namespaces);
        }
        final int start = this.skipLeadingWhite(content, 0);
        final int size = content.size();
        if (start >= size) {
            if (this.currentFormat.expandEmptyElements) {
                out.write("></");
                this.printQualifiedName(out, element);
                out.write(">");
            }
            else {
                out.write(" />");
            }
        }
        else {
            out.write(">");
            if (nextNonText(content, start) < size) {
                this.newline(out);
                this.printContentRange(out, content, start, size, level + 1, namespaces);
                this.newline(out);
                this.indent(out, level);
            }
            else {
                this.printTextRange(out, content, start, size);
            }
            out.write("</");
            this.printQualifiedName(out, element);
            out.write(">");
        }
        while (namespaces.size() > previouslyDeclaredNamespaces) {
            namespaces.pop();
        }
        this.currentFormat = previousFormat;
    }
    
    private void printElementNamespace(final Writer out, final Element element, final NamespaceStack namespaces) throws IOException {
        final Namespace ns = element.getNamespace();
        if (ns == Namespace.XML_NAMESPACE) {
            return;
        }
        if (ns != Namespace.NO_NAMESPACE || namespaces.getURI("") != null) {
            this.printNamespace(out, ns, namespaces);
        }
    }
    
    protected void printEntityRef(final Writer out, final EntityRef entity) throws IOException {
        out.write("&");
        out.write(entity.getName());
        out.write(";");
    }
    
    private void printNamespace(final Writer out, final Namespace ns, final NamespaceStack namespaces) throws IOException {
        final String prefix = ns.getPrefix();
        final String uri = ns.getURI();
        if (uri.equals(namespaces.getURI(prefix))) {
            return;
        }
        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(uri);
        out.write("\"");
        namespaces.push(ns);
    }
    
    protected void printProcessingInstruction(final Writer out, final ProcessingInstruction pi) throws IOException {
        final String target = pi.getTarget();
        boolean piProcessed = false;
        if (!this.currentFormat.ignoreTrAXEscapingPIs) {
            if (target.equals("javax.xml.transform.disable-output-escaping")) {
                this.escapeOutput = false;
                piProcessed = true;
            }
            else if (target.equals("javax.xml.transform.enable-output-escaping")) {
                this.escapeOutput = true;
                piProcessed = true;
            }
        }
        if (!piProcessed) {
            final String rawData = pi.getData();
            if (!"".equals(rawData)) {
                out.write("<?");
                out.write(target);
                out.write(" ");
                out.write(rawData);
                out.write("?>");
            }
            else {
                out.write("<?");
                out.write(target);
                out.write("?>");
            }
        }
    }
    
    private void printQualifiedName(final Writer out, final Attribute a) throws IOException {
        final String prefix = a.getNamespace().getPrefix();
        if (prefix != null && !prefix.equals("")) {
            out.write(prefix);
            out.write(58);
            out.write(a.getName());
        }
        else {
            out.write(a.getName());
        }
    }
    
    private void printQualifiedName(final Writer out, final Element e) throws IOException {
        if (e.getNamespace().getPrefix().length() == 0) {
            out.write(e.getName());
        }
        else {
            out.write(e.getNamespace().getPrefix());
            out.write(58);
            out.write(e.getName());
        }
    }
    
    private void printString(final Writer out, String str) throws IOException {
        if (this.currentFormat.mode == Format.TextMode.NORMALIZE) {
            str = Text.normalizeString(str);
        }
        else if (this.currentFormat.mode == Format.TextMode.TRIM) {
            str = str.trim();
        }
        out.write(this.escapeElementEntities(str));
    }
    
    protected void printText(final Writer out, final Text text) throws IOException {
        final String str = (this.currentFormat.mode == Format.TextMode.NORMALIZE) ? text.getTextNormalize() : ((this.currentFormat.mode == Format.TextMode.TRIM) ? text.getText().trim() : text.getText());
        out.write(this.escapeElementEntities(str));
    }
    
    private void printTextRange(final Writer out, final List content, int start, int end) throws IOException {
        String previous = null;
        start = this.skipLeadingWhite(content, start);
        final int size = content.size();
        if (start < size) {
            end = this.skipTrailingWhite(content, end);
            for (int i = start; i < end; ++i) {
                final Object node = content.get(i);
                String next;
                if (node instanceof Text) {
                    next = ((Text)node).getText();
                }
                else {
                    if (!(node instanceof EntityRef)) {
                        throw new IllegalStateException("Should see only CDATA, Text, or EntityRef");
                    }
                    next = "&" + ((EntityRef)node).getValue() + ";";
                }
                if (next != null) {
                    if (!"".equals(next)) {
                        if (previous != null && (this.currentFormat.mode == Format.TextMode.NORMALIZE || this.currentFormat.mode == Format.TextMode.TRIM) && (this.endsWithWhite(previous) || this.startsWithWhite(next))) {
                            out.write(" ");
                        }
                        if (node instanceof CDATA) {
                            this.printCDATA(out, (CDATA)node);
                        }
                        else if (node instanceof EntityRef) {
                            this.printEntityRef(out, (EntityRef)node);
                        }
                        else {
                            this.printString(out, next);
                        }
                        previous = next;
                    }
                }
            }
        }
    }
    
    public void setFormat(final Format newFormat) {
        this.userFormat = (Format)newFormat.clone();
        this.currentFormat = this.userFormat;
    }
    
    private int skipLeadingWhite(final List content, int start) {
        if (start < 0) {
            start = 0;
        }
        int index = start;
        final int size = content.size();
        if (this.currentFormat.mode != Format.TextMode.TRIM_FULL_WHITE && this.currentFormat.mode != Format.TextMode.NORMALIZE) {
            if (this.currentFormat.mode != Format.TextMode.TRIM) {
                return index;
            }
        }
        while (index < size) {
            if (!this.isAllWhitespace(content.get(index))) {
                return index;
            }
            ++index;
        }
        return index;
    }
    
    private int skipTrailingWhite(final List content, int start) {
        final int size = content.size();
        if (start > size) {
            start = size;
        }
        int index = start;
        if (this.currentFormat.mode != Format.TextMode.TRIM_FULL_WHITE && this.currentFormat.mode != Format.TextMode.NORMALIZE) {
            if (this.currentFormat.mode != Format.TextMode.TRIM) {
                return index;
            }
        }
        while (index >= 0 && this.isAllWhitespace(content.get(index - 1))) {
            --index;
        }
        return index;
    }
    
    private boolean startsWithWhite(final String str) {
        return str != null && str.length() > 0 && isWhitespace(str.charAt(0));
    }
    
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < this.userFormat.lineSeparator.length(); ++i) {
            final char ch = this.userFormat.lineSeparator.charAt(i);
            switch (ch) {
                case '\r': {
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    buffer.append("\\t");
                    break;
                }
                default: {
                    buffer.append("[" + (int)ch + "]");
                    break;
                }
            }
        }
        return "XMLOutputter[omitDeclaration = " + this.userFormat.omitDeclaration + ", " + "encoding = " + this.userFormat.encoding + ", " + "omitEncoding = " + this.userFormat.omitEncoding + ", " + "indent = '" + this.userFormat.indent + "'" + ", " + "expandEmptyElements = " + this.userFormat.expandEmptyElements + ", " + "lineSeparator = '" + buffer.toString() + "', " + "textMode = " + this.userFormat.mode + "]";
    }
    
    protected class NamespaceStack extends org.jdom.output.NamespaceStack
    {
    }
}
