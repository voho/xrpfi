package fi.xrp.fletcher.utility;

import com.google.common.base.Strings;
import lombok.NonNull;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class TextUtility {
    public static @NonNull Set<String> getKeywords(final @NonNull String title) {
        try {
            return new TreeSet<>(tokenize(title));
        } catch (final IOException e) {
            return Collections.emptySet();
        }
    }

    public static @NonNull List<String> tokenize(final @NonNull String text) throws IOException {
        return Arrays
                .stream(text.split("\\s+"))
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public static String htmlUnescape(@Nullable final String html) {
        if (Strings.isNullOrEmpty(html)) {
            return null;
        }
        return StringEscapeUtils.unescapeHtml4(html);
    }

    public static String htmlCleanupLineBreaksOnly(@Nullable final String html) {
        if (Strings.isNullOrEmpty(html)) {
            return null;
        }
        final Document doc = Jsoup.parse(html);
        final Document docClean = new Cleaner(Whitelist.none().addTags("br")).clean(doc);
        docClean.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return docClean.body().html().replace("<br>", "\n");
    }

    @Nullable
    public static String htmlCleanupBasic(@Nullable final String html) {
        if (Strings.isNullOrEmpty(html)) {
            return null;
        }
        return Jsoup.clean(html, Whitelist.simpleText()).trim();
    }

    @Nullable
    public static String htmlCleanupRelaxed(@Nullable final String html) {
        if (Strings.isNullOrEmpty(html)) {
            return null;
        }
        return Jsoup.clean(html, Whitelist.relaxed()).trim();
    }

    /**
     * SOURCE: http://blog.mark-mclaren.info/2007/02/invalid-xml-characters-when-valid-utf8_5873.html
     * This method ensures that the output String has only
     * valid XML unicode characters as specified by the
     * XML 1.0 standard. For reference, please see
     * <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
     * standard</a>. This method will return an empty
     * String if the input is null or empty.
     *
     * @param in The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    @Nullable
    public static String xmlCleanup(@Nullable final String in) {
        if (Strings.isNullOrEmpty(in)) {
            return null;
        }

        final StringBuilder out = new StringBuilder(in.length());

        for (int i = 0; i < in.length(); i++) {
            final char current = in.charAt(i);
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }

        return out.toString().trim();
    }
}
