package org.apache.tools.ant.util;

import org.apache.tools.ant.*;
import org.xml.sax.*;
import java.io.*;
import javax.xml.parsers.*;

public class JAXPUtils
{
    private static final FileUtils FILE_UTILS;
    private static SAXParserFactory parserFactory;
    private static SAXParserFactory nsParserFactory;
    private static DocumentBuilderFactory builderFactory;
    
    public static synchronized SAXParserFactory getParserFactory() throws BuildException {
        if (JAXPUtils.parserFactory == null) {
            JAXPUtils.parserFactory = newParserFactory();
        }
        return JAXPUtils.parserFactory;
    }
    
    public static synchronized SAXParserFactory getNSParserFactory() throws BuildException {
        if (JAXPUtils.nsParserFactory == null) {
            (JAXPUtils.nsParserFactory = newParserFactory()).setNamespaceAware(true);
        }
        return JAXPUtils.nsParserFactory;
    }
    
    public static SAXParserFactory newParserFactory() throws BuildException {
        try {
            return SAXParserFactory.newInstance();
        }
        catch (FactoryConfigurationError e) {
            throw new BuildException("XML parser factory has not been configured correctly: " + e.getMessage(), e);
        }
    }
    
    public static Parser getParser() throws BuildException {
        try {
            return newSAXParser(getParserFactory()).getParser();
        }
        catch (SAXException e) {
            throw convertToBuildException(e);
        }
    }
    
    public static XMLReader getXMLReader() throws BuildException {
        try {
            return newSAXParser(getParserFactory()).getXMLReader();
        }
        catch (SAXException e) {
            throw convertToBuildException(e);
        }
    }
    
    public static XMLReader getNamespaceXMLReader() throws BuildException {
        try {
            return newSAXParser(getNSParserFactory()).getXMLReader();
        }
        catch (SAXException e) {
            throw convertToBuildException(e);
        }
    }
    
    public static String getSystemId(final File file) {
        return JAXPUtils.FILE_UTILS.toURI(file.getAbsolutePath());
    }
    
    public static DocumentBuilder getDocumentBuilder() throws BuildException {
        try {
            return getDocumentBuilderFactory().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new BuildException(e);
        }
    }
    
    private static SAXParser newSAXParser(final SAXParserFactory factory) throws BuildException {
        try {
            return factory.newSAXParser();
        }
        catch (ParserConfigurationException e) {
            throw new BuildException("Cannot create parser for the given configuration: " + e.getMessage(), e);
        }
        catch (SAXException e2) {
            throw convertToBuildException(e2);
        }
    }
    
    private static BuildException convertToBuildException(final SAXException e) {
        final Exception nested = e.getException();
        if (nested != null) {
            return new BuildException(nested);
        }
        return new BuildException(e);
    }
    
    private static synchronized DocumentBuilderFactory getDocumentBuilderFactory() throws BuildException {
        if (JAXPUtils.builderFactory == null) {
            try {
                JAXPUtils.builderFactory = DocumentBuilderFactory.newInstance();
            }
            catch (FactoryConfigurationError e) {
                throw new BuildException("Document builder factory has not been configured correctly: " + e.getMessage(), e);
            }
        }
        return JAXPUtils.builderFactory;
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
        JAXPUtils.parserFactory = null;
        JAXPUtils.nsParserFactory = null;
        JAXPUtils.builderFactory = null;
    }
}
