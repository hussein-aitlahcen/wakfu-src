package com.ankamagames.xulor2.component.text.document;

import org.jetbrains.annotations.*;
import java.util.*;
import java.util.regex.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.framework.kernel.utils.*;

public class MultiplePartTextDocument extends TextDocument
{
    public static final Pattern TEXT_DATA_PATTERN;
    
    @Override
    public String getRawText() {
        final StringBuilder builder = new StringBuilder();
        for (final AbstractDocumentPart documentPart : this) {
            builder.append(documentPart.serialize());
        }
        return builder.toString();
    }
    
    @Override
    public boolean rawTextEquals(@NotNull final String text) {
        final int length = text.length();
        if (length > 0 && this.isEmpty()) {
            return false;
        }
        final int position = 0;
        for (final AbstractDocumentPart documentPart : this) {
            final String part = documentPart.serialize();
            final int endPos = position + part.length();
            if (endPos > length) {
                return false;
            }
            if (!part.equals(text.substring(position, endPos))) {
                return false;
            }
        }
        return position == length;
    }
    
    @Override
    public void setRawText(final String rawText) {
        this.removeAllParts();
        this.clearSelectionIndices();
        this.appendRawText(rawText);
    }
    
    @Override
    public void appendRawText(final String rawText) {
        final Matcher textDataMatcher = MultiplePartTextDocument.TEXT_DATA_PATTERN.matcher(rawText);
        textDataMatcher.reset();
        final ArrayList<AbstractDocumentPart> toAddAfter = new ArrayList<AbstractDocumentPart>();
        while (textDataMatcher.find()) {
            final AbstractDocumentPart documentPart = this.createDocumentPart(textDataMatcher, null, true);
            documentPart.unserialize(textDataMatcher, toAddAfter);
        }
        for (int i = 0, size = toAddAfter.size(); i < size; ++i) {
            this.addPart(toAddAfter.get(i));
        }
    }
    
    public AbstractDocumentPart createDocumentPart(final Matcher textDataMatcher, final AbstractDocumentPart parent, final boolean forceCreation) {
        final DocumentPartType documentPartType = AbstractDocumentPart.extractTypeFromAttributes(textDataMatcher.group(2));
        if (documentPartType == DocumentPartType.IMAGE) {
            return new ImageDocumentPart(this, parent);
        }
        return new TextDocumentPart(this, parent, false);
    }
    
    public static String extractText(final String formattedText) {
        final Matcher textDataMatcher = MultiplePartTextDocument.TEXT_DATA_PATTERN.matcher(formattedText);
        textDataMatcher.reset();
        final StringBuilder result = new StringBuilder();
        while (textDataMatcher.find()) {
            final String toReplace = textDataMatcher.group(0);
            if (StringUtils.isEmptyOrNull(toReplace)) {
                continue;
            }
            final String content = textDataMatcher.group(5);
            if (StringUtils.isEmptyOrNull(content)) {
                result.append(toReplace);
            }
            else {
                result.append(content);
            }
        }
        return result.toString();
    }
    
    static {
        TEXT_DATA_PATTERN = Pattern.compile("(<(\\p{Alpha}+?)( ([^<>]*))*>(.*?)</(\\2)>)|([^<>]+)", 32);
    }
}
