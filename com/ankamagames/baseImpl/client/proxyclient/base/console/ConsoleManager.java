package com.ankamagames.baseImpl.client.proxyclient.base.console;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;

public final class ConsoleManager extends AbstractInputHistoryManager
{
    private static final Logger m_logger;
    private static final String COMMANDS_SEPARATOR = ";";
    private static final String PATH_SEPARATOR = "/";
    private static final String PATH_UNCHANGE_MARKER = "!";
    private static final String PROMPT_END = ">";
    private static final Pattern PATH_PATTERN;
    private static final CommandDescriptor GLOBAL_PARENT_COMMAND;
    private static final CommandDescriptor GLOBAL_HELP_COMMAND;
    private static final ConsoleManager m_instance;
    private CommandDescriptorSet m_nativeCommandDescriptorSet;
    private CommandDescriptorSet m_commandDescriptorSet;
    private Command m_garbageCommand;
    private List<ConsoleView> m_views;
    private boolean m_useMultiCommands;
    private boolean m_usePath;
    private byte m_userLevel;
    
    public ConsoleManager() {
        super();
        this.m_garbageCommand = null;
        this.m_useMultiCommands = true;
        this.m_usePath = true;
        this.m_userLevel = 127;
        (this.m_nativeCommandDescriptorSet = new CommandDescriptorSet()).addChild(ConsoleManager.GLOBAL_PARENT_COMMAND);
        this.m_nativeCommandDescriptorSet.addChild(ConsoleManager.GLOBAL_HELP_COMMAND);
        this.m_commandDescriptorSet = new CommandDescriptorSet();
        this.m_views = new ArrayList<ConsoleView>();
    }
    
    public static ConsoleManager getInstance() {
        return ConsoleManager.m_instance;
    }
    
    public void setGarbageCommand(final Command garbageCommand) {
        this.m_garbageCommand = garbageCommand;
    }
    
    public boolean isUseMultiCommands() {
        return this.m_useMultiCommands;
    }
    
    public void setUseMultiCommands(final boolean useMultiCommands) {
        this.m_useMultiCommands = useMultiCommands;
    }
    
    public boolean isUsePath() {
        return this.m_usePath;
    }
    
    public void setUsePath(final boolean usePath) {
        this.m_usePath = usePath;
    }
    
    public byte getUserLevel() {
        return this.m_userLevel;
    }
    
    public void setUserLevel(final byte userLevel) {
        this.m_userLevel = userLevel;
    }
    
    public void addView(final ConsoleView view) {
        this.m_views.add(view);
    }
    
    public boolean removeView(final ConsoleView view) {
        if (this.m_views.contains(view)) {
            this.m_views.remove(view);
            return true;
        }
        return false;
    }
    
    public CommandDescriptorSet getNativeCommandDescriptorSet() {
        return this.m_nativeCommandDescriptorSet;
    }
    
    public void navigateToParentCommandDescriptorSet() {
        if (this.m_commandDescriptorSet != null && this.m_commandDescriptorSet.getParent() != null) {
            this.setCommandDescriptorSet(this.m_commandDescriptorSet.getParent());
        }
    }
    
    public void setCommandDescriptorSet(final CommandDescriptorSet commandDescriptorSet) {
        if (commandDescriptorSet != null && commandDescriptorSet != this.m_commandDescriptorSet) {
            this.m_commandDescriptorSet = commandDescriptorSet;
            final String prompt = this.getPrompt();
            for (final ConsoleView view : this.m_views) {
                view.setPrompt(prompt);
            }
        }
    }
    
    public boolean addCommandListFromXmlFile(final URL url) {
        if (this.m_commandDescriptorSet != null) {
            final CommandDescriptorSet rootCommandDescriptorSet = this.m_commandDescriptorSet.getRoot();
            if (rootCommandDescriptorSet != null) {
                return rootCommandDescriptorSet.addCommandListFromXmlFile(url);
            }
        }
        return false;
    }
    
    public CommandDescriptorSet getCommandDescriptorSet() {
        return this.m_commandDescriptorSet;
    }
    
    public String getPrompt() {
        if (this.m_commandDescriptorSet != null) {
            return this.m_commandDescriptorSet.getPath() + ">";
        }
        return "";
    }
    
    public void parseInput(final String input) {
        this.parseInput(input, true);
    }
    
    public void parseInput(final String input, final boolean addToHistory) {
        if (addToHistory) {
            this.pushToHistory(input);
        }
        String[] commandLines = null;
        if (this.isUseMultiCommands()) {
            commandLines = input.split(";");
        }
        else {
            commandLines = new String[] { input };
        }
        for (String commandLine : commandLines) {
            commandLine = commandLine.trim();
            CommandDescriptorSet savedCurrentCommandDescriptorSet = null;
            if (this.isUsePath() && commandLine.startsWith("!")) {
                commandLine = commandLine.substring(1);
                savedCurrentCommandDescriptorSet = this.m_commandDescriptorSet;
            }
            if (this.isUsePath()) {
                final Matcher matcher = ConsoleManager.PATH_PATTERN.matcher(commandLine);
                if (matcher.find()) {
                    final String pathGroup = matcher.group();
                    final boolean isAbsolutePath = pathGroup.startsWith("/");
                    String[] path;
                    CommandDescriptorSet targetCommandSet;
                    if (isAbsolutePath) {
                        path = pathGroup.substring(1).split("/");
                        targetCommandSet = this.m_commandDescriptorSet.getRoot();
                    }
                    else {
                        path = pathGroup.split("/");
                        targetCommandSet = this.m_commandDescriptorSet;
                    }
                    if (path.length == 1 && !pathGroup.endsWith("/")) {
                        commandLine = commandLine.substring(1);
                    }
                    else {
                        for (final String commandSet : path) {
                            final ArrayList<CommandPattern> pathMatches = targetCommandSet.getMatchesCommandPatterns(commandSet, this.m_userLevel);
                            if (pathMatches.isEmpty()) {
                                this.err("Chemin " + commandSet + " invalide");
                                break;
                            }
                            if (pathMatches.size() != 1) {
                                this.err("Trop de possibilit\u00e9s");
                                break;
                            }
                            final CommandPattern commandPattern = pathMatches.get(0);
                            if (!(commandPattern instanceof CommandDescriptorSet)) {
                                this.err("Chemin " + commandSet + " invalide");
                                break;
                            }
                            targetCommandSet = pathMatches.get(0);
                        }
                        commandLine = commandLine.substring(pathGroup.length());
                    }
                    this.setCommandDescriptorSet(targetCommandSet);
                }
            }
            final ArrayList<CommandPattern> matchesDescriptors = new ArrayList<CommandPattern>();
            matchesDescriptors.addAll(this.m_commandDescriptorSet.getMatchesCommandPatterns(commandLine, this.m_userLevel));
            matchesDescriptors.addAll(this.m_nativeCommandDescriptorSet.getMatchesCommandPatterns(commandLine, this.m_userLevel));
            if (matchesDescriptors.isEmpty()) {
                if (this.m_garbageCommand != null) {
                    final ArrayList<String> args = new ArrayList<String>();
                    args.add(commandLine);
                    this.m_garbageCommand.execute(this, null, args);
                }
                else {
                    this.err("Commande '" + commandLine + "' invalide");
                }
            }
            else {
                for (final CommandPattern commandPattern2 : matchesDescriptors) {
                    final Command command = commandPattern2.createInstance();
                    final Pattern argsPattern = commandPattern2.getArgsPattern();
                    final Matcher argsMatcher = argsPattern.matcher(commandLine);
                    if (argsMatcher.matches()) {
                        argsMatcher.reset();
                        final ArrayList<String> args2 = new ArrayList<String>();
                        while (argsMatcher.find()) {
                            for (int i = 0; i <= argsMatcher.groupCount(); ++i) {
                                args2.add(argsMatcher.group(i));
                            }
                        }
                        try {
                            command.execute(this, commandPattern2, args2);
                        }
                        catch (Exception e) {
                            this.err("Exception dans l'ex\u00e9cution de la commande \u00e0 la ligne : " + commandLine);
                            ConsoleManager.m_logger.error((Object)("Exception dans l'ex\u00e9cution de la commande \u00e0 la ligne : " + commandLine), (Throwable)e);
                        }
                    }
                    else if (argsPattern.pattern().length() != 0) {
                        this.err("Les param\u00e8tres de commande ne correspondent pas !");
                    }
                    if (!command.isPassThrough()) {
                        break;
                    }
                }
            }
            if (savedCurrentCommandDescriptorSet != null) {
                this.setCommandDescriptorSet(savedCurrentCommandDescriptorSet);
            }
        }
    }
    
    public void log(final String text) {
        for (final ConsoleView view : this.m_views) {
            view.log(text);
        }
    }
    
    public void trace(final String text) {
        for (final ConsoleView view : this.m_views) {
            view.trace(text);
        }
    }
    
    public void customStyle(final String text) {
        for (final ConsoleView view : this.m_views) {
            view.customStyle(text);
        }
    }
    
    public void customTrace(final String text, final int color) {
        for (final ConsoleView view : this.m_views) {
            view.customTrace(text, color);
        }
    }
    
    public void err(final String text) {
        for (final ConsoleView view : this.m_views) {
            view.err(text);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ConsoleManager.class);
        PATH_PATTERN = Pattern.compile("^((/(\\w+))+/){1}|^(((\\w+)/)+){1}|^(/\\w+){1}");
        GLOBAL_PARENT_COMMAND = new CommandDescriptor("[.]{2}", null, new NavigateToParentCommandSetCommand(), false);
        GLOBAL_HELP_COMMAND = new CommandDescriptor("/\\?", null, new HelpCommand(), false);
        m_instance = new ConsoleManager();
    }
}
