package com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors;

import org.apache.log4j.*;
import java.net.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import java.util.*;
import org.xml.sax.*;

public class CommandDescriptorSet extends CommandPattern
{
    protected static final Logger m_logger;
    private CommandDescriptorSet m_parent;
    private ArrayList<CommandPattern> m_children;
    
    public CommandDescriptorSet() {
        this("", "", false);
    }
    
    public CommandDescriptorSet(final String cmdRegex, final String argsRegex, final boolean allowNoArg) {
        super(cmdRegex, argsRegex, allowNoArg);
        this.m_children = new ArrayList<CommandPattern>();
    }
    
    public boolean addCommandListFromXmlFile(final URL url) {
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            final SAXParser saxParser = factory.newSAXParser();
            final DescriptorHandler handler = new DescriptorHandler(this);
            saxParser.parse(url.openStream(), handler);
            return true;
        }
        catch (Exception e) {
            CommandDescriptorSet.m_logger.error((Object)"SAX parser error :", (Throwable)e);
            return false;
        }
    }
    
    public void addCommandList(final ArrayList<CommandDescriptor> commandDescriptors) {
        for (final CommandDescriptor commandDescriptor : commandDescriptors) {
            this.getRoot().addChild(commandDescriptor);
        }
    }
    
    public boolean isAutoCompletionChild(final String commandLine, final byte userLevel) {
        for (final CommandPattern commandPattern : this.getMatchesCommandPatterns(commandLine, userLevel)) {
            if (commandPattern.isAutoCompletion()) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<CommandPattern> getChildren() {
        return this.m_children;
    }
    
    public void addChild(final CommandPattern commandPattern) {
        this.m_children.add(commandPattern);
    }
    
    private void setParent(final CommandDescriptorSet parent) {
        this.m_parent = parent;
    }
    
    public CommandDescriptorSet getParent() {
        return this.m_parent;
    }
    
    public boolean isRoot() {
        return this.m_parent == null;
    }
    
    public CommandDescriptorSet getRoot() {
        if (this.isRoot()) {
            return this;
        }
        return this.getParent().getRoot();
    }
    
    public String getPath() {
        final StringBuilder builder = new StringBuilder();
        if (this.m_parent != null) {
            builder.append(this.getParent().getPath());
        }
        return builder.append(this.getName()).append("/").toString();
    }
    
    public ArrayList<CommandPattern> getMatchesCommandPatterns(final String input, final byte level) {
        final ArrayList<CommandPattern> matchesDescriptors = new ArrayList<CommandPattern>();
        for (final CommandPattern descriptor : this.m_children) {
            if (descriptor.getLevel() <= level) {
                final Matcher matcher = descriptor.getCmdPattern().matcher(input);
                if (!matcher.matches()) {
                    continue;
                }
                matchesDescriptors.add(descriptor);
            }
        }
        return matchesDescriptors;
    }
    
    public ArrayList<String> getChildrenNamesList() {
        final ArrayList<String> names = new ArrayList<String>();
        for (final CommandPattern commandPattern : this.m_children) {
            names.add(commandPattern.getName());
        }
        return names;
    }
    
    @Override
    public Command createInstance() {
        return new NavigateToCommandSetCommand(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CommandDescriptorSet.class);
    }
    
    private static class DescriptorHandler extends DefaultHandler
    {
        private static final String COMMAND_LIST = "commandList";
        private static final String DESCRIPTOR_SET = "commandSet";
        private static final String DESCRIPTOR = "command";
        private static final String NAME_ATTRIBUTE = "name";
        private static final String CMD_PATTERN_ATTRIBUTE = "cmdPattern";
        private static final String ARGS_PATTERN_ATTRIBUTE = "argsPattern";
        private static final String ALLOW_NO_ARG_ATTRIBUTE = "allowNoArg";
        private static final String CLASS_ATTRIBUTE = "class";
        private static final String LEVEL_ATTRIBUTE = "level";
        private static final String AUTO_COMPLETION_ATTRIBUTE = "autoCompletion";
        private Stack<CommandDescriptorSet> m_commandDescriptorSets;
        
        public DescriptorHandler(final CommandDescriptorSet rootCommandDescriptorSet) {
            super();
            (this.m_commandDescriptorSets = new Stack<CommandDescriptorSet>()).add(rootCommandDescriptorSet);
        }
        
        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            if (!qName.equals("commandList")) {
                final String name = attributes.getValue("name");
                final String cmdRegex = attributes.getValue("cmdPattern");
                final String argsRegex = attributes.getValue("argsPattern");
                final boolean allowNoArg = attributes.getValue("allowNoArg") != null && Boolean.parseBoolean(attributes.getValue("allowNoArg"));
                final String level = attributes.getValue("level");
                final boolean autoCompletion = attributes.getValue("autoCompletion") != null && Boolean.parseBoolean(attributes.getValue("autoCompletion"));
                if (cmdRegex == null || cmdRegex.length() == 0) {
                    CommandDescriptorSet.m_logger.error((Object)("cmdPattern est invalide pour " + name + "!"));
                }
                if (qName.equals("commandSet")) {
                    final CommandDescriptorSet descriptorSet = new CommandDescriptorSet(cmdRegex, argsRegex, allowNoArg);
                    if (name != null) {
                        descriptorSet.setName(name);
                    }
                    if (level != null) {
                        descriptorSet.setLevel(Byte.valueOf(level));
                    }
                    if (!this.m_commandDescriptorSets.isEmpty()) {
                        final CommandDescriptorSet parent = this.m_commandDescriptorSets.lastElement();
                        descriptorSet.setParent(parent);
                        parent.addChild(descriptorSet);
                    }
                    this.m_commandDescriptorSets.add(descriptorSet);
                }
                else if (qName.equals("command")) {
                    final String classAttribute = attributes.getValue("class");
                    try {
                        Command command = null;
                        try {
                            command = (Command)this.getClass().getClassLoader().loadClass(classAttribute).newInstance();
                        }
                        catch (InstantiationException e) {
                            CommandDescriptorSet.m_logger.error((Object)e.getMessage());
                        }
                        catch (IllegalAccessException e2) {
                            CommandDescriptorSet.m_logger.error((Object)e2.getMessage());
                        }
                        catch (ClassCastException e3) {
                            CommandDescriptorSet.m_logger.error((Object)e3.getMessage());
                        }
                        if (!this.m_commandDescriptorSets.isEmpty()) {
                            final CommandDescriptor descriptor = new CommandDescriptor(cmdRegex, argsRegex, command, allowNoArg);
                            if (name != null) {
                                descriptor.setName(name);
                            }
                            if (level != null) {
                                descriptor.setLevel(Byte.valueOf(level));
                            }
                            descriptor.setAutoCompletion(autoCompletion);
                            this.m_commandDescriptorSets.lastElement().addChild(descriptor);
                        }
                    }
                    catch (ClassNotFoundException e4) {
                        CommandDescriptorSet.m_logger.error((Object)"ClassNotFound", (Throwable)e4);
                    }
                }
            }
        }
        
        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if (qName.equals("commandSet")) {
                this.m_commandDescriptorSets.pop();
            }
        }
        
        @Override
        public void endDocument() throws SAXException {
        }
    }
}
