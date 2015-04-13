package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;

@Documentation(commandName = "havenWorld | hw", commandParameters = "&lt;-help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "hw h to show full documentation.", commandObsolete = false)
public final class HavenWorldCommand extends ModerationCommand
{
    public static final int START = 0;
    public static final int STOP = 1;
    public static final int COMMIT = 2;
    public static final int LIST = 3;
    public static final int TOPOLOGY_CREATE = 4;
    public static final int TOPOLOGY_UPDATE = 5;
    public static final int BUILDING_CREATE = 6;
    public static final int BUILDING_DELETE = 7;
    public static final int BID = 8;
    public static final int START_AUCTION = 9;
    public static final int END_AUCTION = 10;
    public static final int RESET_GUILD_ID = 11;
    public static final int SET_GUILD_ID = 12;
    public static final int SET_AUCTION_END_DATE = 13;
    public static final int HELP = 14;
    public static final int ADD_RESOURCES = 15;
    public static final int SET_ADMIN_BUILDING_FACTOR = 16;
    public static final int CONSOLE_COLOR = 8421631;
    private final int m_commandId;
    private final String[] m_args;
    
    public HavenWorldCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 14: {
                return this.m_args.length == 0;
            }
            case 7:
            case 9:
            case 10:
            case 11: {
                return this.m_args.length == 1;
            }
            case 4:
            case 8:
            case 12:
            case 15: {
                return this.m_args.length == 2;
            }
            case 5:
            case 6: {
                return this.m_args.length == 3;
            }
            case 13: {
                return this.m_args.length == 7;
            }
            case 16: {
                return this.m_args.length == 2;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9!");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 0: {
                    startOccupation(networkEntity);
                    break;
                }
                case 1: {
                    stopOccupation(networkEntity);
                    break;
                }
                case 2: {
                    commitActions(networkEntity);
                    break;
                }
                case 3: {
                    list(networkEntity);
                    break;
                }
                case 4: {
                    this.createTopology(networkEntity);
                    break;
                }
                case 5: {
                    this.updateTopology(networkEntity);
                    break;
                }
                case 6: {
                    this.createBuilding(networkEntity);
                    break;
                }
                case 7: {
                    this.deleteBuilding(networkEntity);
                    break;
                }
                case 8: {
                    this.bid(networkEntity);
                    break;
                }
                case 9: {
                    this.startAuction(networkEntity);
                    break;
                }
                case 10: {
                    this.endAuction(networkEntity);
                    break;
                }
                case 11: {
                    this.resetGuildId(networkEntity);
                    break;
                }
                case 12: {
                    this.setGuildId(networkEntity);
                    break;
                }
                case 13: {
                    this.setAuctionEndDate(networkEntity);
                    break;
                }
                case 15: {
                    this.addResources(networkEntity);
                    break;
                }
                case 16: {
                    this.setAdminBuildDurationFactor(networkEntity);
                    break;
                }
                case 14: {
                    help();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me d'une commande" + e);
        }
    }
    
    private static void help() {
        ModerationCommand.log("hw (startAuction|sa) idhw : start bid");
        ModerationCommand.log("hw bid idhw bidValue : bit for this havenworld (bid must be started");
        ModerationCommand.log("hw (endAuction|ea) idhw : finish bid");
        ModerationCommand.log("hw (resetGuild|rg) idIhw : reset owner");
        ModerationCommand.log("hw (setGuild | sg) idIhw guildId: give to a guild");
        ModerationCommand.log("hw (setEndDate|sed) idhw s min h d m y : change end date of a bid");
        ModerationCommand.log("hw (addResources|ar) idIhw resourcesAmount : add resource points");
        ModerationCommand.log("hw (setAdminBuildingFactor|sabf) idIhw factor : change % of building speed (1 to 100)");
        ModerationCommand.log("hw (help|h) : show full documentation");
    }
    
    private void setAdminBuildDurationFactor(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)161);
        msg.addShortParameter(Short.parseShort(this.m_args[0]));
        msg.addIntParameter(Integer.parseInt(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private void addResources(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)158);
        msg.addShortParameter(Short.parseShort(this.m_args[0]));
        msg.addIntParameter(Integer.parseInt(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private void setAuctionEndDate(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)156);
        final int worldId = Integer.parseInt(this.m_args[0]);
        final int s = Integer.parseInt(this.m_args[1]);
        final int min = Integer.parseInt(this.m_args[2]);
        final int h = Integer.parseInt(this.m_args[3]);
        final int d = Integer.parseInt(this.m_args[4]);
        final int m = Integer.parseInt(this.m_args[5]);
        final int y = Integer.parseInt(this.m_args[6]);
        msg.addIntParameter(worldId);
        msg.addIntParameter(s);
        msg.addIntParameter(min);
        msg.addIntParameter(h);
        msg.addIntParameter(d);
        msg.addIntParameter(m);
        msg.addIntParameter(y);
        networkEntity.sendMessage(msg);
    }
    
    private void resetGuildId(final NetworkEntity networkEntity) {
        this.setGuildId(networkEntity, 0);
    }
    
    private void setGuildId(final NetworkEntity networkEntity) {
        this.setGuildId(networkEntity, Integer.parseInt(this.m_args[1]));
    }
    
    private void setGuildId(final NetworkEntity networkEntity, final int guildId) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)148);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        msg.addIntParameter(guildId);
        networkEntity.sendMessage(msg);
    }
    
    private void endAuction(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)147);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void startAuction(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)146);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void bid(final FrameworkEntity networkEntity) {
        final Message msg = new HavenWorldBidRequestMessage(Integer.valueOf(this.m_args[0]), Integer.valueOf(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private static void startOccupation(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)128);
        networkEntity.sendMessage(msg);
    }
    
    private static void stopOccupation(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)129);
        networkEntity.sendMessage(msg);
    }
    
    private static void commitActions(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        final HavenWorldManageActionRequest request = new HavenWorldManageActionRequest();
        ((ManageHavenWorldOccupation)occupation).getActionManager().forEachAction(new TObjectProcedure<HavenWorldAction>() {
            @Override
            public boolean execute(final HavenWorldAction object) {
                request.addAction(object);
                return true;
            }
        });
        networkEntity.sendMessage(request);
        ConsoleManager.getInstance().customTrace("Actions envoy\u00e9es au server", 8421631);
    }
    
    private static void list(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)130);
        networkEntity.sendMessage(msg);
    }
    
    private void createTopology(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        try {
            final short partitionX = Short.valueOf(this.m_args[0]);
            final short partitionY = Short.valueOf(this.m_args[1]);
            final TopologyCreate action = new TopologyCreate(partitionX, partitionY);
            ((ManageHavenWorldOccupation)occupation).getActionManager().addAction(action);
            ConsoleManager.getInstance().customTrace("Action enregistr\u00e9e : " + action, 8421631);
        }
        catch (NumberFormatException e) {
            ConsoleManager.getInstance().err("Arguments invalides : " + e.getMessage());
        }
    }
    
    private void updateTopology(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        try {
            final short patchX = Short.valueOf(this.m_args[0]);
            final short patchY = Short.valueOf(this.m_args[1]);
            final short patchId = Short.valueOf(this.m_args[2]);
            final TopologyUpdate action = new TopologyUpdate(patchX, patchY, patchId, (short)0);
            ((ManageHavenWorldOccupation)occupation).getActionManager().addAction(action);
            ConsoleManager.getInstance().customTrace("Action enregistr\u00e9e : " + action, 8421631);
        }
        catch (NumberFormatException e) {
            ConsoleManager.getInstance().err("Arguments invalides : " + e.getMessage());
        }
    }
    
    private void createBuilding(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        try {
            final short x = Short.valueOf(this.m_args[0]);
            final short y = Short.valueOf(this.m_args[1]);
            final short buildingRefId = Short.valueOf(this.m_args[2]);
            final BuildingCreate action = new BuildingCreate(buildingRefId, x, y);
            ((ManageHavenWorldOccupation)occupation).getActionManager().addAction(action);
            ConsoleManager.getInstance().customTrace("Action enregistr\u00e9e : " + action, 8421631);
        }
        catch (NumberFormatException e) {
            ConsoleManager.getInstance().err("Arguments invalides : " + e.getMessage());
        }
    }
    
    private void deleteBuilding(final FrameworkEntity networkEntity) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = player.getCurrentOccupation();
        if (occupation == null || occupation.getOccupationTypeId() != 25) {
            ConsoleManager.getInstance().err("L'occupation de gestion du Havre-monde doit \u00eatre lanc\u00e9e");
            return;
        }
        try {
            final long buildingUid = Long.valueOf(this.m_args[0]);
            final BuildingDelete action = new BuildingDelete(buildingUid);
            ((ManageHavenWorldOccupation)occupation).getActionManager().addAction(action);
            ConsoleManager.getInstance().customTrace("Action enregistr\u00e9e : " + action, 8421631);
        }
        catch (NumberFormatException e) {
            ConsoleManager.getInstance().err("Arguments invalides : " + e.getMessage());
        }
    }
}
