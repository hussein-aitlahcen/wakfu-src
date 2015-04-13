package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import java.util.regex.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import java.io.*;
import com.ankamagames.wakfu.client.core.*;
import org.reflections.scanners.*;
import org.reflections.*;
import java.util.*;

@Documentation(commandName = "?", commandParameters = "(optionnal) &lt;\"commandName\"&gt; (optionnal) &lt;-rights | -r&gt;", commandDescription = "Show command list. Param commandName is facultative but permit to show only commands with this name pattern. Param -r permit to show associate rights.", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandObsolete = false)
public class HelpCommand extends ModerationCommand
{
    private static final Pattern PATTERN;
    private static List<Command> COMMANDS;
    private static final int RED = 16711680;
    protected static final String[] GROUPS_NAMES;
    public static final Color CUSTOM_GREEN;
    private final boolean m_withRights;
    private final String m_command;
    
    public HelpCommand() {
        super();
        this.m_command = "";
        this.m_withRights = false;
    }
    
    public HelpCommand(final boolean withRights) {
        super();
        this.m_command = "";
        this.m_withRights = withRights;
    }
    
    public HelpCommand(final String command) {
        super();
        this.m_command = command;
        this.m_withRights = false;
    }
    
    public HelpCommand(final String command, final boolean withRights) {
        super();
        this.m_command = command;
        this.m_withRights = withRights;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        if (HelpCommand.COMMANDS == null) {
            this.createCommandsList();
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        for (final Command c : HelpCommand.COMMANDS) {
            if ((!this.m_command.isEmpty() && c.m_name.contains(HelpCommand.PATTERN.matcher(this.m_command).replaceAll(""))) || this.m_command.isEmpty()) {
                c.createDescription(sb, this.m_withRights);
            }
        }
        final String result = sb.finishAndToString();
        if (!result.isEmpty()) {
            WakfuConsoleView.getInstance().customStyle(result);
        }
        else {
            ConsoleManager.getInstance().customTrace("La commande n'existe pas.", 16711680);
        }
    }
    
    private void createCommandsList() {
        final List<Command> helpResult = new ArrayList<Command>();
        ArrayList<Class> classes;
        try {
            classes = this.getClassList();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
        final AdminRightsGroup rights = AdminRightsGroup.fromRights(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights());
        for (final Class elem : classes) {
            final Documentation documentation = elem.getAnnotation(Documentation.class);
            if (elem.getSuperclass().equals(ModerationCommand.class) && !documentation.commandObsolete()) {
                if (rights == AdminRightsGroup.SUPER_ADMINISTRATOR) {
                    addCommand(helpResult, documentation);
                }
                else {
                    final AdminRightsGroup[] arr$;
                    final AdminRightsGroup[] neededRights = arr$ = documentation.commandRights();
                    for (final AdminRightsGroup right : arr$) {
                        if (right == rights) {
                            addCommand(helpResult, documentation);
                            break;
                        }
                    }
                }
            }
        }
        Collections.sort(helpResult, new CommandComparator());
        HelpCommand.COMMANDS = helpResult;
    }
    
    private static void addCommand(final List<Command> helpResult, final Documentation documentation) {
        final Command command = new Command(documentation.commandName(), documentation.commandParameters(), documentation.commandRights(), documentation.commandDescription());
        helpResult.add(command);
    }
    
    ArrayList<Class> getClassList() throws IOException {
        final Reflections reflections = new Reflections(this.getClass().getPackage().getName(), new Scanner[0]);
        final Set<Class<? extends ModerationCommand>> classes = (Set<Class<? extends ModerationCommand>>)reflections.getSubTypesOf((Class)ModerationCommand.class);
        return new ArrayList<Class>(classes);
    }
    
    @Override
    public String toString() {
        return "HelpCommand{m_command='" + this.m_command + '\'' + '}';
    }
    
    static {
        PATTERN = Pattern.compile("\"");
        GROUPS_NAMES = new String[] { "None", "Root", "Admin", "Staff", "Moderator" };
        CUSTOM_GREEN = new Color(0.0f, 0.7f, 0.0f, 1.0f);
    }
    
    private static class Command
    {
        protected String m_name;
        protected String m_parameters;
        private final AdminRightsGroup[] m_commandRights;
        protected String m_description;
        
        Command(final String commandName, final String commandParameters, final AdminRightsGroup[] commandRights, final String commandDescription) {
            super();
            this.m_name = commandName;
            this.m_parameters = commandParameters;
            this.m_commandRights = commandRights;
            this.m_description = commandDescription;
        }
        
        protected void createDescription(final TextWidgetFormater sb, final boolean withRights) {
            sb.newLine();
            sb.openText().addColor(HelpCommand.CUSTOM_GREEN).append(this.m_name).append(this.m_parameters.isEmpty() ? "" : (' ' + this.m_parameters)).closeText();
            sb.openText().addColor(Color.LIGHT_GRAY).newLine().append("\t - " + this.m_description).closeText();
            if (withRights) {
                sb.openText().addColor(Color.GRAY).newLine().append("\t - rights : ");
                for (final AdminRightsGroup group : this.m_commandRights) {
                    sb.append(HelpCommand.GROUPS_NAMES[group.getId()] + ' ');
                }
                sb.closeText();
            }
        }
        
        @Override
        public String toString() {
            return "Command{m_name='" + this.m_name + '\'' + ", m_parameters='" + this.m_parameters + '\'' + ", m_description='" + this.m_description + '\'' + '}';
        }
    }
    
    private static class CommandComparator implements Comparator<Command>
    {
        @Override
        public int compare(final Command o1, final Command o2) {
            return o1.m_name.compareTo(o2.m_name);
        }
    }
}
