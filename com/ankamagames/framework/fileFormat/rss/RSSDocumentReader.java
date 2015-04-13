package com.ankamagames.framework.fileFormat.rss;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.io.*;

public class RSSDocumentReader
{
    private static final Logger m_logger;
    private static final String RSS = "rss";
    private static final String CHANNEL = "channel";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String URL = "url";
    private static final String ITEM = "item";
    
    public RSSChannel read(final XMLDocumentContainer doc, final boolean ignoreErrors) throws MalformedRSSException {
        final XMLDocumentNode rootNode = doc.getRootNode();
        if (!rootNode.getName().equals("rss")) {
            if (!ignoreErrors) {
                throw new MalformedRSSException("La racine n'est pas un tag rss");
            }
            return null;
        }
        else {
            final XMLDocumentNode channelNode = (XMLDocumentNode)rootNode.getChildByName("channel");
            if (channelNode != null) {
                String channelTitle = null;
                final XMLDocumentNode channelTitleNode = (XMLDocumentNode)channelNode.getChildByName("title");
                if (channelTitleNode == null) {
                    if (!ignoreErrors) {
                        throw new MalformedRSSException("tag title manquant dans channel");
                    }
                    RSSDocumentReader.m_logger.warn((Object)"tag title manquant dans channel");
                }
                else {
                    channelTitle = this.getTextValue(channelTitleNode);
                }
                String channelLink = null;
                final XMLDocumentNode channelLinkNode = (XMLDocumentNode)channelNode.getChildByName("link");
                if (channelLinkNode == null) {
                    if (!ignoreErrors) {
                        throw new MalformedRSSException("tag link manquant dans channel");
                    }
                    RSSDocumentReader.m_logger.warn((Object)"tag link manquant dans channel");
                }
                else {
                    channelLink = this.getTextValue(channelLinkNode).trim();
                }
                String channelDescription = null;
                final XMLDocumentNode channelDescNode = (XMLDocumentNode)channelNode.getChildByName("description");
                if (channelDescNode == null) {
                    if (!ignoreErrors) {
                        throw new MalformedRSSException("tag description manquant dans channel");
                    }
                    RSSDocumentReader.m_logger.warn((Object)"tag description manquant dans channel");
                }
                else {
                    channelDescription = this.getTextValue(channelDescNode);
                }
                final RSSChannel channel = new RSSChannel(channelTitle, channelLink, channelDescription);
                final XMLDocumentNode channelImageNode = (XMLDocumentNode)channelNode.getChildByName("image");
                if (channelImageNode != null) {
                    channel.setImage(this.readImageNode(channelImageNode, ignoreErrors));
                }
                final ArrayList<DocumentEntry> itemNodes = channelNode.getChildrenByName("item");
                if (itemNodes != null) {
                    for (int i = 0, size = itemNodes.size(); i < size; ++i) {
                        final RSSItem item = this.readItemNode(itemNodes.get(i), ignoreErrors);
                        if (item != null) {
                            channel.addItem(item);
                        }
                    }
                }
                return channel;
            }
            if (!ignoreErrors) {
                throw new MalformedRSSException("tag channel manquant dans rss");
            }
            return null;
        }
    }
    
    private RSSImage readImageNode(final XMLDocumentNode imageNode, final boolean ignoreErrors) throws MalformedRSSException {
        if (ignoreErrors) {
            final ArrayList<? extends DocumentEntry> children = imageNode.getChildren();
            if (children == null || children.size() == 0) {
                return null;
            }
        }
        final XMLDocumentNode urlNode = (XMLDocumentNode)imageNode.getChildByName("url");
        if (urlNode == null) {
            if (!ignoreErrors) {
                throw new MalformedRSSException("tag url manquant dans Image");
            }
            RSSDocumentReader.m_logger.warn((Object)"tag url manquant dans Image");
            return null;
        }
        else {
            final String url = this.getTextValue(urlNode).trim();
            final XMLDocumentNode titleNode = (XMLDocumentNode)imageNode.getChildByName("title");
            if (titleNode == null) {
                if (!ignoreErrors) {
                    throw new MalformedRSSException("tag title manquant dans Image");
                }
                RSSDocumentReader.m_logger.warn((Object)"tag title manquant dans Image");
                return null;
            }
            else {
                final String title = this.getTextValue(titleNode);
                final XMLDocumentNode descNode = (XMLDocumentNode)imageNode.getChildByName("link");
                if (descNode != null) {
                    final String desc = this.getTextValue(descNode).trim();
                    return new RSSImage(url, title, desc);
                }
                if (!ignoreErrors) {
                    throw new MalformedRSSException("tag link manquant dans Image");
                }
                RSSDocumentReader.m_logger.warn((Object)"tag link manquant dans Image");
                return null;
            }
        }
    }
    
    private RSSItem readItemNode(final XMLDocumentNode itemNode, final boolean ignoreErrors) throws MalformedRSSException {
        String title = null;
        String link = null;
        String description = null;
        final XMLDocumentNode titleNode = (XMLDocumentNode)itemNode.getChildByName("title");
        if (titleNode != null) {
            title = this.getTextValue(titleNode);
        }
        final XMLDocumentNode linkNode = (XMLDocumentNode)itemNode.getChildByName("link");
        if (linkNode != null) {
            link = this.getTextValue(linkNode).trim();
        }
        final XMLDocumentNode descNode = (XMLDocumentNode)itemNode.getChildByName("description");
        if (descNode != null) {
            description = this.getTextValue(descNode);
        }
        if (title != null || description != null) {
            final RSSItem rssItem = new RSSItem(title, link, description);
            final XMLDocumentNode imageNode = (XMLDocumentNode)itemNode.getChildByName("image");
            if (imageNode != null) {
                rssItem.setImage(this.readImageNode(imageNode, ignoreErrors));
            }
            return rssItem;
        }
        if (!ignoreErrors) {
            throw new MalformedRSSException("tag title et description manquants dans item");
        }
        RSSDocumentReader.m_logger.warn((Object)"tag title et description manquants dans item");
        return null;
    }
    
    public String getTextValue(final XMLDocumentNode node) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final ArrayList<? extends DocumentEntry> children = node.getChildren();
        for (int i = 0, size = children.size(); i < size; ++i) {
            final XMLDocumentNode childNode = (XMLDocumentNode)children.get(i);
            if (childNode.getName().equals("#text") || childNode.getName().equals("#cdata-section")) {
                sb.append(childNode.getStringValue());
            }
        }
        return sb.finishAndToString();
    }
    
    public static void main(final String[] args) {
        final XMLDocumentContainer container = new XMLDocumentContainer();
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        try {
            final InputStream stream = new URL("http://penny-arcade.com/feed").openStream();
            accessor.open(stream);
            accessor.read(container, new DocumentEntryParser[0]);
            accessor.close();
            stream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        final RSSDocumentReader rssDocumentReader = new RSSDocumentReader();
        try {
            rssDocumentReader.read(container, false);
        }
        catch (MalformedRSSException e2) {
            System.out.println(e2.getMessage());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RSSDocumentReader.class);
    }
}
