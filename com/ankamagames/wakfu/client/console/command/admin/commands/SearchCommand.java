package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import java.util.regex.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "search", commandParameters = "&lt;si | sm | search quest | smi | ss&gt; &lt;valeur&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "/!\\ Don't type search but this commands : si \"itemName\" search itemId, sm monsterId search monsters with this Id, smi \"name\" search monsterId, search quest \"questName\" search questId, ss \"monsterName\" search a monster with this name.", commandObsolete = false)
public final class SearchCommand extends ModerationCommand
{
    public static final byte SEARCH_RIGHT = 1;
    public static final byte SEARCH_MONSTER = 2;
    public static final byte SEARCH_RESOURCE = 3;
    public static final byte SEARCH_ITEM = 4;
    public static final byte SEARCH_MOB_ID = 5;
    public static final byte SEARCH_IE = 6;
    public static final byte SEARCH_STATE = 7;
    public static final byte SEARCH_SET = 8;
    public static final byte SEARCH_QUEST = 9;
    private Object[] m_params;
    private byte m_type;
    
    public SearchCommand(final byte type, final Object... params) {
        super();
        this.m_type = type;
        this.m_params = params;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_type) {
            case 1:
            case 2:
            case 3:
            case 6: {
                if (this.m_params != null && this.m_params.length > 0 && this.m_params[0] instanceof Integer) {
                    return true;
                }
            }
            case 4:
            case 5:
            case 7:
            case 8:
            case 9: {
                if (this.m_params != null && this.m_params.length == 1 && this.m_params[0] instanceof String) {
                    return true;
                }
                break;
            }
        }
        ConsoleManager.getInstance().err("Type de recherche inconnue " + this.m_type);
        return false;
    }
    
    @Override
    public void execute() {
        switch (this.m_type) {
            case 1: {
                this.sendMessage((byte)2);
                break;
            }
            case 2:
            case 3:
            case 6: {
                this.sendMessage((byte)3);
                break;
            }
            case 7: {
                final Pattern pattern = Pattern.compile(((String)this.m_params[0]).toLowerCase());
                final StringBuilder builder = new StringBuilder("Result :\r\n========\r\n");
                final TIntObjectHashMap<State> items = StateLoaderClient.getFullList();
                final int[] counter = { 0 };
                items.forEachEntry(new TIntObjectProcedure<State>() {
                    @Override
                    public boolean execute(final int a, final State b) {
                        try {
                            final StateClient state = (StateClient)b;
                            final String name = state.getUnformatedName().toLowerCase();
                            System.out.println(name);
                            if (pattern.matcher(name).find()) {
                                builder.append(a).append(" : ").append(b.getName()).append("\r\n");
                                final int[] val$counter = counter;
                                final int n = 0;
                                ++val$counter[n];
                            }
                        }
                        catch (Exception e) {
                            ConsoleManager.getInstance().customTrace("Probl\u00e8me de data pour l'\u00e9tat " + a, 16711680);
                        }
                        return true;
                    }
                });
                builder.append("========\r\n").append(counter[0]).append(" \u00e9tats trouv\u00e9s");
                ConsoleManager.getInstance().customTrace(builder.toString(), 16777215);
                break;
            }
            case 4: {
                final Pattern pattern = Pattern.compile(((String)this.m_params[0]).toLowerCase());
                final StringBuilder builder = new StringBuilder("Result :\r\n========\r\n");
                final TIntObjectHashMap<AbstractReferenceItem> items2 = ((ItemManagerImpl)ReferenceItemManager.getInstance()).getFullList();
                final int[] counter = { 0 };
                items2.forEachEntry((TIntObjectProcedure<AbstractReferenceItem>)new TIntObjectProcedure<AbstractReferenceItem>() {
                    @Override
                    public boolean execute(final int a, final AbstractReferenceItem b) {
                        try {
                            if (pattern.matcher(b.getName().toLowerCase()).find()) {
                                builder.append(a).append(" : ").append(b.getName()).append("\r\n");
                                final int[] val$counter = counter;
                                final int n = 0;
                                ++val$counter[n];
                            }
                        }
                        catch (Exception e) {
                            ConsoleManager.getInstance().customTrace("Probl\u00e8me de data pour l'item " + a, 16711680);
                        }
                        return true;
                    }
                });
                builder.append("========\r\n").append(counter[0]).append(" items found");
                ConsoleManager.getInstance().customTrace(builder.toString(), 16777215);
                break;
            }
            case 8: {
                final Pattern pattern = Pattern.compile(((String)this.m_params[0]).toLowerCase());
                final StringBuilder builder = new StringBuilder("Result :\r\n========\r\n");
                final TIntObjectHashMap<ItemSet> sets = ItemSetManager.getInstance().getFullList();
                final int[] counter = { 0 };
                sets.forEachEntry(new TIntObjectProcedure<ItemSet>() {
                    @Override
                    public boolean execute(final int a, final ItemSet b) {
                        try {
                            if (pattern.matcher(b.getName().toLowerCase()).find()) {
                                builder.append(a).append(" : ").append(b.getName()).append("\r\n");
                                final int[] val$counter = counter;
                                final int n = 0;
                                ++val$counter[n];
                            }
                        }
                        catch (Exception e) {
                            ConsoleManager.getInstance().customTrace("Probl\u00e8me de data pour le set " + a, 16711680);
                        }
                        return true;
                    }
                });
                builder.append("========\r\n").append(counter[0]).append(" sets found");
                ConsoleManager.getInstance().customTrace(builder.toString(), 16777215);
                break;
            }
            case 5: {
                final Pattern pattern = Pattern.compile(((String)this.m_params[0]).toLowerCase());
                final StringBuilder builder = new StringBuilder("Result :\r\n========\r\n");
                final TIntObjectHashMap<MonsterBreed> items3 = MonsterBreedManager.getInstance().getFullList();
                final int[] counter = { 0 };
                items3.forEachEntry(new TIntObjectProcedure<MonsterBreed>() {
                    @Override
                    public boolean execute(final int a, final MonsterBreed b) {
                        if (pattern.matcher(b.getName().toLowerCase()).find()) {
                            builder.append(a).append(" : ").append(b.getName()).append("\r\n");
                            final int[] val$counter = counter;
                            final int n = 0;
                            ++val$counter[n];
                        }
                        return true;
                    }
                });
                builder.append("========\r\n").append(counter[0]).append(" items found");
                ConsoleManager.getInstance().customTrace(builder.toString(), 16776960);
                break;
            }
            case 9: {
                final Pattern pattern = Pattern.compile(((String)this.m_params[0]).toLowerCase());
                final StringBuilder builder = new StringBuilder("Result :\r\n========\r\n");
                final int[] counter2 = { 0 };
                AchievementsModel.INSTANCE.forEachAchievementModel(new TIntObjectProcedure<Achievement>() {
                    @Override
                    public boolean execute(final int a, final Achievement b) {
                        final String name = WakfuTranslator.getInstance().getString(62, b.getId(), new Object[0]);
                        if (name == null) {
                            return true;
                        }
                        if (pattern.matcher(name.toLowerCase()).find()) {
                            builder.append(a).append(" : ").append(name).append("\r\n");
                            final int[] val$counter = counter2;
                            final int n = 0;
                            ++val$counter[n];
                        }
                        return true;
                    }
                });
                builder.append("========\r\n").append(counter2[0]).append(" items found");
                ConsoleManager.getInstance().customTrace(builder.toString(), 16776960);
                break;
            }
        }
    }
    
    private void sendMessage(final byte serverId) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.addByteParameter(this.m_type);
        netMessage.setCommand((short)56);
        netMessage.setServerId(serverId);
        netMessage.addIntParameter((int)this.m_params[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
