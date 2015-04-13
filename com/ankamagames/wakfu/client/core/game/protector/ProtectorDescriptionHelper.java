package com.ankamagames.wakfu.client.core.game.protector;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.core.game.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ProtectorDescriptionHelper
{
    private static final Logger m_logger;
    
    private static boolean useFullDescription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer == null || !localPlayer.hasProperty(WorldPropertyType.DISPLAY_LIGHT_PROTECTOR_INFORMATION);
    }
    
    public static void describeProtectorAndTerritory(final TextWidgetFormater sb, final Protector protector) {
        final Territory territory = (Territory)protector.getTerritory();
        if (territory == null) {
            return;
        }
        final boolean useFullDescription = useFullDescription();
        if (WakfuTranslator.getInstance().containsContentKey(66, territory.getId())) {
            final NationProtectorInfo protectorInfo = protector.getCurrentNation().getProtectorInfoManager().getProtectorInfo(protector.getId());
            if (protectorInfo != null) {
                final boolean inChaos = protectorInfo.isInChaos();
                final String territoryName = WakfuTranslator.getInstance().getString(66, territory.getId(), new Object[0]);
                if (territoryName.length() > 0) {
                    if (useFullDescription && inChaos) {
                        sb.openText().addColor(TerritoryViewConstants.ENNEMY.getRGBtoHex());
                    }
                    sb.b().append(territoryName);
                    if (inChaos && useFullDescription) {
                        sb.append(' ' + WakfuTranslator.getInstance().getString("chaos.label"));
                    }
                    sb._b().newLine();
                    if (inChaos && useFullDescription) {
                        sb.closeText();
                    }
                    if (useFullDescription) {
                        final short minLevel = territory.getMinLevel();
                        final short maxLevel = territory.getMaxLevel();
                        if (minLevel != -1 && maxLevel != -1) {
                            sb.append(StringFormatter.format(WakfuTranslator.getInstance().getString("recommended.level", minLevel, maxLevel), new Object[0])).newLine();
                        }
                    }
                }
            }
        }
        describeProtector(sb, protector, useFullDescription);
    }
    
    public static void describeProtector(final TextWidgetFormater sb, final Protector protector) {
        describeProtector(sb, protector, useFullDescription());
    }
    
    private static void describeProtector(final TextWidgetFormater sb, final Protector protector, final boolean useFullDescription) {
        final WakfuTranslator translator = WakfuTranslator.getInstance();
        if (protector == null) {
            sb.append(translator.getString("map.territories.noProtector"));
        }
        else {
            sb.append(translator.getString("map.territories.protectorName", protector.getName()));
            if (useFullDescription) {
                if (protector.getCurrentNationId() != -1) {
                    sb.append(" - ").append(translator.getString(39, protector.getCurrentNationId(), new Object[0]));
                }
                else {
                    sb.append(" - ").append(translator.getString("map.territories.noNation"));
                }
            }
            writeBuffs(sb, protector, true);
            if (useFullDescription) {
                final EnumMap<TaxContext, Tax> taxes = protector.getTaxes();
                if (taxes != null && !taxes.isEmpty()) {
                    sb.newLine().newLine().b().append(translator.getString("protector.tax.desc"))._b();
                    final Tax fleaTax = taxes.get(TaxContext.FLEA_ADD_ITEM_CONTEXT);
                    sb.newLine().append(TaxView.getContextDesc(fleaTax));
                    sb.newLine().append(TaxView.getTaxValueDesc(fleaTax));
                }
                final int[] crafts = protector.getCraft();
                if (crafts != null && crafts.length != 0) {
                    sb.newLine().newLine().b().append(translator.getString("protector.craft"))._b();
                    for (int i = 0, size = crafts.length; i < size; ++i) {
                        sb.newLine().append(translator.getString(43, crafts[i], new Object[0]));
                    }
                }
            }
        }
    }
    
    public static void writeBuffs(final TextWidgetFormater sb, final Protector protector, final boolean titled) {
        final ProtectorBuff[] buffLists = protector.getNationBuffs();
        if (buffLists != null) {
            Arrays.sort(buffLists, ProtectorBuffIdComparator.INSTANCE);
            if (titled) {
                sb.append("\n\n").b().append(WakfuTranslator.getInstance().getString("protector.buff.desc"))._b();
            }
            if (protector.getKnownBuffsList().size() == 0) {
                sb.append("\n").openText().addColor(TerritoryViewConstants.ENNEMY.getRGBtoHex()).append(WakfuTranslator.getInstance().getString("protector.noWill")).closeText();
            }
            else if (buffLists.length == 0) {
                sb.append("\n").openText().addColor(TerritoryViewConstants.ENNEMY.getRGBtoHex()).append(WakfuTranslator.getInstance().getString("protector.noBuff")).closeText();
            }
            else {
                final ArrayList<WakfuEffect> effects = new ArrayList<WakfuEffect>();
                final ArrayList<TIntArrayList> paramsList = new ArrayList<TIntArrayList>();
                ProtectorBuff previousBuff = null;
                for (final ProtectorBuff buff : buffLists) {
                    if (buff == null) {
                        ProtectorDescriptionHelper.m_logger.warn((Object)"buff null dans la liste du protecteur ?");
                    }
                    else {
                        boolean first = false;
                        if (previousBuff == null || buff.getId() != previousBuff.getId()) {
                            writeBuff(sb, effects, paramsList, previousBuff);
                            first = true;
                            previousBuff = buff;
                        }
                        final Iterator<WakfuEffect> it = buff.iterator();
                        int j = 0;
                        while (it.hasNext()) {
                            final WakfuEffect effect = it.next();
                            final int count = effect.getParamsCount();
                            TIntArrayList params;
                            if (first) {
                                params = new TIntArrayList(count);
                                for (int i = 0; i < count; ++i) {
                                    params.add(0);
                                }
                                paramsList.add(params);
                                effects.add(effect);
                            }
                            else {
                                params = paramsList.get(j);
                            }
                            for (int i = 0; i < count; ++i) {
                                params.set(i, params.get(i) + effect.getParam(i, (short)0, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                            }
                            ++j;
                        }
                    }
                }
                if (previousBuff != null) {
                    writeBuff(sb, effects, paramsList, previousBuff);
                }
            }
        }
    }
    
    private static void writeBuff(final TextWidgetFormater sb, final ArrayList<WakfuEffect> effects, final ArrayList<TIntArrayList> paramsList, final ProtectorBuff previousBuff) {
        if (previousBuff != null && effects.size() != 0) {
            for (int i = 0, size = effects.size(); i < size; ++i) {
                final WakfuEffect effect = effects.get(i);
                final TIntArrayList paramsInt = paramsList.get(i);
                final Object[] params = new Object[paramsInt.size()];
                for (int j = 0, jSize = paramsInt.size(); j < jSize; ++j) {
                    params[j] = paramsInt.getQuick(j);
                }
                sb.append("\n");
                sb.addImage(WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", previousBuff.getGfxId()), 16, 16, null);
                sb.append(StringFormatter.format(WakfuTranslator.getInstance().getStringWithoutFormat(10, effect.getActionId()), params));
                final Elements element = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId()).getElement();
                if (element != null) {
                    try {
                        final String elementUrl = String.format(WakfuConfiguration.getInstance().getString("elementsSmallIconsPath"), element.name());
                        sb.append(" ").addImage(elementUrl, -1, -1, null);
                    }
                    catch (PropertyException e) {
                        ProtectorDescriptionHelper.m_logger.error((Object)e.toString());
                    }
                }
            }
            effects.clear();
            paramsList.clear();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorDescriptionHelper.class);
    }
    
    private static class ProtectorBuffIdComparator implements Comparator<ProtectorBuff>
    {
        private static ProtectorBuffIdComparator INSTANCE;
        
        @Override
        public int compare(final ProtectorBuff o1, final ProtectorBuff o2) {
            return o1.getId() - o2.getId();
        }
        
        static {
            ProtectorBuffIdComparator.INSTANCE = new ProtectorBuffIdComparator();
        }
    }
}
