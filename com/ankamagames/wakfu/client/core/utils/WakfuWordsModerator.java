package com.ankamagames.wakfu.client.core.utils;

import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import org.apache.tools.ant.filters.*;
import java.io.*;
import java.nio.charset.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;

public class WakfuWordsModerator extends WordsModerator
{
    public static String makeValidSentence(final String sentence) {
        try {
            return moderateString(sentence);
        }
        catch (Exception e) {
            return WordsModerator.getInstance().makeValidSentence(sentence, censorActivated());
        }
    }
    
    private static boolean censorActivated() {
        return WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.CENSOR_ACTIVATED) || !SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.CLIENT_CAN_DISABLE_PROFANITY_FILTER);
    }
    
    public static void controlChatMessage(final ChatMessage message) {
        final String text = message.getMessage();
        try {
            message.setMessage(moderateString(text));
        }
        catch (Exception e) {
            message.setMessage(WordsModerator.getInstance().makeValidSentence(message.getMessage(), censorActivated()));
        }
    }
    
    private static String moderateString(final String text) throws Exception {
        final XMLDocumentContainer c = new XMLDocumentContainer();
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final StringInputStream is = new StringInputStream("<text>" + text + "</text>", "utf-8");
        accessor.open(is);
        accessor.read(c, new DocumentEntryParser[0]);
        accessor.close();
        moderateNode(c.getRootNode());
        final XMLDocumentAccessor accessor2 = new XMLDocumentAccessor();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        accessor2.create(os);
        accessor2.write(c, true, false, false);
        final String s = new String(os.toByteArray(), Charset.forName("utf-8"));
        return s.substring(6, s.length() - 7);
    }
    
    private static void moderateNode(final XMLDocumentNode node) {
        if ("#text".equals(node.getName())) {
            node.setStringValue(WordsModerator.getInstance().makeValidSentence(node.getStringValue(), censorActivated()));
        }
        else {
            for (final DocumentEntry child : node.getChildren()) {
                final XMLDocumentNode c = (XMLDocumentNode)child;
                moderateNode(c);
            }
        }
    }
}
