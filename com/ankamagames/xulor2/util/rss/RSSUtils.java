package com.ankamagames.xulor2.util.rss;

import org.apache.log4j.*;
import com.ankamagames.framework.text.*;
import org.apache.commons.lang3.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;

public class RSSUtils
{
    private static final Logger m_logger;
    public static final String A_TAG = "a";
    public static final String B_TAG = "b";
    public static final String STRONG_TAG = "strong";
    public static final String P_TAG = "p";
    public static final String HREF_PARAM = "href";
    
    public static void main(final String[] args) {
        final String text = "\t\t\t\t\t\t\t\t\t\t\t\t\tTycho: \n\t\t\t\t\tWe are also doing some kinda PAX thing downtown, I guess?&nbsp; Should be pretty cool.\n(CW)TB \t\t\t\t\t\t\t\t";
        HTML2Text(text);
    }
    
    public static String HTML2Text(final String text) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final String escapedString = "<text>" + StringEscapeUtils.unescapeHtml4(text) + "</text>";
        byte[] escapedStringBytes;
        try {
            escapedStringBytes = escapedString.getBytes("utf-8");
        }
        catch (UnsupportedEncodingException e) {
            escapedStringBytes = escapedString.getBytes();
        }
        final InputStream stream = new ByteArrayInputStream(escapedStringBytes);
        final XMLDocumentContainer documentContainer = new XMLDocumentContainer();
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        try {
            accessor.open(stream);
            accessor.read(documentContainer, new DocumentEntryParser[0]);
            accessor.close();
        }
        catch (Exception e2) {
            RSSUtils.m_logger.warn((Object)"Probl\u00e8me au parse du document");
            return null;
        }
        HTML2Text(documentContainer.getRootNode(), sb);
        return sb.finishAndToString();
    }
    
    private static void HTML2Text(final XMLDocumentNode node, final TextWidgetFormater sb) {
        TagType type = TagType.TEXT;
        if (node.getName().equals("#text") || node.getName().equals("#cdata-section")) {
            sb.append(node.getStringValue());
            type = TagType.TEXT;
        }
        else if (node.getName().equalsIgnoreCase("a")) {
            final DocumentEntry hrefParam = node.getParameterByName("href");
            if (hrefParam != null) {
                type = TagType.A;
                sb.u().addId(hrefParam.getStringValue());
            }
        }
        else if (node.getName().equalsIgnoreCase("b") || node.getName().equalsIgnoreCase("strong")) {
            type = TagType.B;
            sb.b();
        }
        else if (node.getName().equalsIgnoreCase("p")) {
            type = TagType.P;
        }
        final ArrayList<XMLDocumentNode> children = (ArrayList<XMLDocumentNode>)node.getChildren();
        for (int i = 0, size = children.size(); i < size; ++i) {
            final XMLDocumentNode childNode = children.get(i);
            HTML2Text(childNode, sb);
        }
        switch (type) {
            case A: {
                sb._u();
                break;
            }
            case B: {
                sb._b();
                break;
            }
            case P: {
                sb.newLine();
                break;
            }
        }
    }
    
    public static String HTMLLinkToTextViewLink(final String link) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.u().addId(link).append(link)._u();
        return sb.finishAndToString();
    }
    
    public static String HTMLLinkToTextViewLink(final String link, final String text) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.u().addId(link).append(text)._u();
        return sb.finishAndToString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)RSSUtils.class);
    }
    
    private enum TagType
    {
        TEXT, 
        A, 
        B, 
        P;
    }
}
