package com.ankamagames.wakfu.common.dispatch;

import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.google.common.collect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import org.jetbrains.annotations.*;
import com.google.common.base.*;
import java.util.*;
import gnu.trove.*;

public class WorldInfoHelper
{
    public static boolean isPartnerValid(final WorldInfo info, final Partner partner) {
        final SystemConfiguration config = info.getConfig();
        final ArrayList<String> authorizedPartners = config.getStringArrayList(SystemConfigurationType.AUTHORIZED_PARTNERS);
        for (final String partnerName : authorizedPartners) {
            if (partnerName.equals(partner.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public static ImmutableList<Community> getCommunities(final WorldInfo info) {
        return getCommunities(info, (Predicate<Community>)Predicates.alwaysTrue());
    }
    
    public static ImmutableList<Community> getCommunities(final WorldInfo info, @NotNull final Predicate<Community> validator) {
        Preconditions.checkNotNull((Object)validator);
        final SystemConfiguration config = info.getConfig();
        final boolean withoutCheck = !config.getBooleanValue(SystemConfigurationType.COMMUNITY_CHECK_ENABLE);
        final Collection<Community> communities = new ArrayList<Community>();
        if (withoutCheck) {
            for (final Community com : Community.values()) {
                if (validator.apply((Object)com)) {
                    communities.add(com);
                }
            }
            return (ImmutableList<Community>)ImmutableList.copyOf((Collection)communities);
        }
        final TIntHashSet required = config.getIntHashSet(SystemConfigurationType.COMMUNITY_REQUIRED);
        if (!required.isEmpty()) {
            final TIntIterator it = required.iterator();
            while (it.hasNext()) {
                final Community com2 = Community.getFromId(it.next());
                if (validator.apply((Object)com2)) {
                    communities.add(com2);
                }
            }
            return (ImmutableList<Community>)ImmutableList.copyOf((Collection)communities);
        }
        final TIntHashSet forbidden = config.getIntHashSet(SystemConfigurationType.COMMUNITY_FORBIDDEN);
        for (final Community com3 : Community.values()) {
            if (!forbidden.contains(com3.getId()) && validator.apply((Object)com3)) {
                communities.add(com3);
            }
        }
        return (ImmutableList<Community>)ImmutableList.copyOf((Collection)communities);
    }
}
