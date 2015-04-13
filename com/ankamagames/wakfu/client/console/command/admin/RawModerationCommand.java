package com.ankamagames.wakfu.client.console.command.admin;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.console.command.admin.commands.antlr.*;
import org.antlr.runtime.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.console.command.admin.commands.*;

public class RawModerationCommand implements Command
{
    private static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args == null || args.size() < 1) {
            RawModerationCommand.m_logger.error((Object)"Commande vide.");
            return;
        }
        final String command = args.get(1);
        final String[] commandTokens = command.trim().split("[ ]+", 2);
        if (command.length() > 0) {
            commandTokens[0] = commandTokens[0].toLowerCase();
        }
        String xcommand = "";
        for (final String s : commandTokens) {
            xcommand = xcommand + s + " ";
        }
        RawModerationCommand.m_logger.info((Object)("Ex\u00e9cution de la commande brute : " + xcommand));
        final ModerationCommandLexer lex = new ModerationCommandLexer((CharStream)new ANTLRStringStream(xcommand + "\n"));
        final CommonTokenStream tokens = new CommonTokenStream((TokenSource)lex);
        final ModerationCommandParser parser = new ModerationCommandParser((TokenStream)tokens);
        ModerationCommand cmd;
        try {
            cmd = parser.cmd();
        }
        catch (RecognitionException e) {
            final String errorMsg = "Erreur de syntaxe dans la commande (ligne " + e.line + ", caract\u00e8re " + e.charPositionInLine + ", token " + e.token.toString() + ")";
            RawModerationCommand.m_logger.error((Object)errorMsg);
            ConsoleManager.getInstance().err(errorMsg);
            return;
        }
        catch (ParseException e2) {
            final String errorMsg = "Erreur de syntaxe :" + e2;
            RawModerationCommand.m_logger.error((Object)errorMsg);
            ConsoleManager.getInstance().err(errorMsg);
            return;
        }
        catch (Exception e3) {
            final String errorMsg = "Commande incorrecte : " + e3;
            RawModerationCommand.m_logger.error((Object)errorMsg);
            ConsoleManager.getInstance().err(errorMsg);
            return;
        }
        if (cmd != null && cmd.isValid()) {
            cmd.execute();
        }
        else {
            final String errorMsg2 = "Le parseur de commande n'a retourn\u00e9 aucun objet valide.";
            RawModerationCommand.m_logger.error((Object)"Le parseur de commande n'a retourn\u00e9 aucun objet valide.");
            ConsoleManager.getInstance().err("Le parseur de commande n'a retourn\u00e9 aucun objet valide.");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RawModerationCommand.class);
    }
}
