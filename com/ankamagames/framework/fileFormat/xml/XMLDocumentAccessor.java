package com.ankamagames.framework.fileFormat.xml;

import org.apache.log4j.*;
import javax.xml.parsers.*;
import com.ankamagames.framework.fileFormat.io.*;
import org.xml.sax.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import java.util.*;
import org.w3c.dom.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.document.*;

public class XMLDocumentAccessor implements DocumentAccessor<XMLDocumentContainer>
{
    protected static final Logger m_logger;
    private DocumentBuilder m_documentBuilder;
    private Document m_document;
    private OutputStream m_lastCreatedOutputStream;
    private static final XMLDocumentAccessor m_instance;
    
    public static XMLDocumentAccessor getInstance() {
        return XMLDocumentAccessor.m_instance;
    }
    
    public XMLDocumentAccessor() {
        super();
        this.m_lastCreatedOutputStream = null;
        try {
            this.m_documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            XMLDocumentAccessor.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    @Override
    public void open(final String fileName) throws Exception {
        try {
            final InputStream stream = ContentFileHelper.openFile(fileName);
            this.open(stream);
            stream.close();
        }
        catch (Exception e2) {
            final File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                this.m_document = null;
                throw new FileNotFoundException("Fichier non trouv\u00e9 : " + fileName);
            }
            try {
                this.m_document = this.m_documentBuilder.parse(xmlFile);
            }
            catch (SAXException e1) {
                this.m_documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                throw e1;
            }
            if (this.m_document == null) {
                throw new Exception("Impossible de parser le document " + fileName);
            }
        }
    }
    
    public void open(final InputStream stream) throws Exception {
        this.m_document = this.m_documentBuilder.parse(stream);
    }
    
    @Override
    public boolean create(final String fileName) throws Exception {
        final File xmlfile = new File(fileName);
        final File parentFile = xmlfile.getParentFile();
        final boolean ok = parentFile.mkdirs() && xmlfile.createNewFile();
        this.m_lastCreatedOutputStream = new FileOutputStream(fileName);
        return ok;
    }
    
    public void create(final OutputStream stream) {
        this.m_lastCreatedOutputStream = stream;
    }
    
    @Override
    public void close() throws Exception {
        this.m_document = null;
    }
    
    @Override
    public void read(final XMLDocumentContainer container, final DocumentEntryParser... parsers) {
        try {
            if (this.m_document == null) {
                XMLDocumentAccessor.m_logger.error((Object)"read() invoqu\u00e9 sur un document non ouvert ( voir : open() )");
                return;
            }
            container.notifyOnLoadBegin();
            Node node;
            for (node = this.m_document.getFirstChild(); node != null && node.getNodeType() != 1; node = node.getNextSibling()) {}
            final XMLDocumentNode rootNode = this.readNode(node, parsers);
            if (rootNode != null) {
                container.setRootNode(rootNode);
            }
            container.notifyOnLoadComplete();
        }
        catch (Exception e) {
            container.notifyOnLoadError("Exception : " + e.getMessage());
            XMLDocumentAccessor.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    private static String parseEntry(final String value, final DocumentEntryParser[] parsers) {
        if (parsers != null && parsers.length > 0) {
            for (final DocumentEntryParser parser : parsers) {
                final String s = parser.parse(value);
                if (s != null) {
                    return s;
                }
            }
        }
        return value;
    }
    
    private XMLDocumentNode readNode(final Node node, final DocumentEntryParser[] parsers) {
        if (node == null) {
            return null;
        }
        String name = parseEntry(node.getNodeName(), parsers);
        String value = parseEntry(node.getNodeValue(), parsers);
        final XMLDocumentNode docNode = new XMLDocumentNode(name, value);
        final NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); ++i) {
                final Node a = attributes.item(i);
                name = parseEntry(a.getNodeName(), parsers);
                value = parseEntry(a.getNodeValue(), parsers);
                docNode.addParameter(new XMLNodeAttribute(name, value));
            }
        }
        for (Node nde = node.getFirstChild(); nde != null; nde = nde.getNextSibling()) {
            docNode.addChild(this.readNode(nde, parsers));
        }
        return docNode;
    }
    
    @Override
    public void write(final XMLDocumentContainer xmlContainer) {
        this.write(xmlContainer, true);
    }
    
    public void write(final XMLDocumentContainer xmlContainer, final boolean humanReadable) {
        this.write(xmlContainer, humanReadable, true);
    }
    
    public void write(final XMLDocumentContainer xmlContainer, final boolean humanReadable, final boolean withHeader) {
        this.write(xmlContainer, humanReadable, withHeader, true);
    }
    
    public void write(final XMLDocumentContainer xmlContainer, final boolean humanReadable, final boolean withHeader, final boolean withAddedSpace) {
        if (xmlContainer == null) {
            return;
        }
        xmlContainer.notifyOnSaveBegin();
        try {
            if (humanReadable) {
                this.writeCustom(xmlContainer, withHeader, withAddedSpace);
            }
            else {
                this.writeWithDOM(xmlContainer);
            }
        }
        catch (TransformerException e) {
            XMLDocumentAccessor.m_logger.error((Object)"Probleme pendant la sauvegarde d'un fichier XML.", (Throwable)e);
        }
        xmlContainer.notifyOnSaveComplete();
    }
    
    private void writeWithDOM(final XMLDocumentContainer xmlContainer) throws TransformerException {
        final Document outDoc = this.m_documentBuilder.newDocument();
        final Node rootNode = createNodeForDocument(xmlContainer.getRootNode(), outDoc);
        outDoc.appendChild(rootNode);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final Source source = new DOMSource(outDoc);
        final Result result = new StreamResult(this.m_lastCreatedOutputStream);
        transformer.transform(source, result);
    }
    
    private static Node createNodeForDocument(final DocumentEntry node, final Document document) {
        if (node == null) {
            return null;
        }
        final Node element = createNode(node, document);
        for (final DocumentEntry child : node.getChildren()) {
            final Node childNode = createNodeForDocument(child, document);
            element.appendChild(childNode);
        }
        return element;
    }
    
    private static Node createNode(final DocumentEntry node, final Document document) {
        final String name = node.getName();
        final String value = node.getStringValue();
        if (name.equals("#text")) {
            return document.createTextNode(value);
        }
        if (name.equals("#comment")) {
            return document.createComment(value);
        }
        if (name.equals("#cdata-section")) {
            return document.createCDATASection(value);
        }
        final Element element = document.createElement(name);
        for (final DocumentEntry entry : node.getParameters()) {
            final Attr attribute = document.createAttribute(entry.getName());
            attribute.setValue(entry.getStringValue());
            element.setAttributeNode(attribute);
        }
        return element;
    }
    
    private void writeCustom(final XMLDocumentContainer xmlContainer, final boolean withHeader, final boolean withAddedSpace) {
        final XMLDocumentNode rootNode = xmlContainer.getRootNode();
        try {
            if (withHeader) {
                writeHeader(this.m_lastCreatedOutputStream, "UTF-8");
            }
            if (rootNode != null) {
                this.writeNode(rootNode, this.m_lastCreatedOutputStream, "UTF-8", 0, withAddedSpace);
            }
            this.m_lastCreatedOutputStream.close();
            this.m_lastCreatedOutputStream = null;
        }
        catch (Exception e) {
            xmlContainer.notifyOnLoadError("Exception : " + e.getMessage());
            XMLDocumentAccessor.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    private static void writeHeader(final OutputStream outStream, final String charSet) throws IOException {
        outStream.write(("<?xml version=\"1.0\" encoding=\"" + charSet + "\"?>\r\n").getBytes(charSet));
    }
    
    private void writeNode(final DocumentEntry node, final OutputStream outStream, final String charSet, final int tabs, final boolean modifySpaceCharacters) throws IOException {
        if (node == null) {
            return;
        }
        if (modifySpaceCharacters) {
            cleanNode(node);
        }
        final String name = node.getName();
        final String value = node.getStringValue();
        final StringBuilder buffer = new StringBuilder();
        if (modifySpaceCharacters) {
            for (int i = 0; i < tabs; ++i) {
                buffer.append('\t');
            }
        }
        final String prefix = buffer.toString();
        if (name.equals("#text")) {
            outStream.write(prefix.getBytes(charSet));
            outStream.write(value.getBytes(charSet));
            if (modifySpaceCharacters) {
                outStream.write("\r\n".getBytes(charSet));
            }
        }
        else if (name.equals("#comment")) {
            outStream.write(prefix.getBytes(charSet));
            outStream.write("<!--".getBytes(charSet));
            outStream.write(value.getBytes(charSet));
            outStream.write("-->".getBytes(charSet));
        }
        else if (name.equals("#cdata-section")) {
            outStream.write(prefix.getBytes(charSet));
            outStream.write("<![CDATA[".getBytes(charSet));
            outStream.write(value.getBytes(charSet));
            outStream.write("]]>".getBytes(charSet));
            if (modifySpaceCharacters) {
                outStream.write("\n".getBytes(charSet));
            }
        }
        else {
            outStream.write(prefix.getBytes(charSet));
            outStream.write("<".getBytes(charSet));
            outStream.write(name.getBytes(charSet));
            writeParameters(node, outStream, charSet);
            final boolean hasChild = !node.getChildren().isEmpty();
            final boolean hasValue = node.getStringValue() != null && !node.getStringValue().isEmpty();
            if (!hasChild && !hasValue) {
                outStream.write("/".getBytes(charSet));
            }
            if (!hasValue) {
                outStream.write(">".getBytes(charSet));
                if (modifySpaceCharacters) {
                    outStream.write("\r\n".getBytes(charSet));
                }
            }
            else {
                outStream.write(">".getBytes(charSet));
            }
            for (final DocumentEntry de : node.getChildren()) {
                this.writeNode(de, outStream, charSet, tabs + 1, modifySpaceCharacters);
            }
            if (node.getStringValue() != null) {
                outStream.write(node.getStringValue().trim().getBytes(charSet));
            }
            if (hasChild) {
                outStream.write(prefix.getBytes(charSet));
            }
            if (hasChild || hasValue) {
                outStream.write("</".getBytes(charSet));
                outStream.write(name.getBytes(charSet));
                outStream.write(">".getBytes(charSet));
                if (modifySpaceCharacters) {
                    outStream.write("\r\n".getBytes(charSet));
                }
            }
        }
    }
    
    private static void cleanNode(final DocumentEntry node) {
        final String name = node.getName();
        if (name.equals("#text")) {
            node.setStringValue(node.getStringValue().replaceAll("[\n\t]", ""));
        }
    }
    
    private static void writeParameters(final DocumentEntry node, final OutputStream outStream, final String charSet) throws IOException {
        if (node == null) {
            return;
        }
        for (final DocumentEntry de : node.getParameters()) {
            final String name = de.getName();
            final String value = de.getStringValue();
            outStream.write(" ".getBytes(charSet));
            outStream.write(name.getBytes(charSet));
            outStream.write("=\"".getBytes(charSet));
            outStream.write((value != null) ? value.getBytes(charSet) : "".getBytes());
            outStream.write("\"".getBytes(charSet));
        }
    }
    
    @Override
    public XMLDocumentContainer getNewDocumentContainer() {
        return new XMLDocumentContainer();
    }
    
    public void writeWithHeader(final XMLDocumentContainer xmlContainer, final String header) {
        this.writeWithHeader(xmlContainer, header, true);
    }
    
    public void writeWithHeader(final XMLDocumentContainer xmlContainer, final String header, final boolean withSpaceChars) {
        if (xmlContainer == null) {
            return;
        }
        final XMLDocumentNode rootNode = xmlContainer.getRootNode();
        xmlContainer.notifyOnSaveBegin();
        try {
            writeHeader(this.m_lastCreatedOutputStream, "UTF-8");
            this.m_lastCreatedOutputStream.write(header.getBytes("UTF-8"));
            if (rootNode != null) {
                this.writeNode(rootNode, this.m_lastCreatedOutputStream, "UTF-8", 0, withSpaceChars);
            }
            this.m_lastCreatedOutputStream.close();
            this.m_lastCreatedOutputStream = null;
        }
        catch (Exception e) {
            xmlContainer.notifyOnLoadError("Exception : " + e.getMessage());
            XMLDocumentAccessor.m_logger.error((Object)"Exception", (Throwable)e);
            return;
        }
        xmlContainer.notifyOnSaveComplete();
    }
    
    static {
        m_logger = Logger.getLogger((Class)XMLDocumentAccessor.class);
        m_instance = new XMLDocumentAccessor();
    }
}
