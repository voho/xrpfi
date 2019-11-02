package fi.xrp.fletcher.utility;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

public final class UrlUtility {
    public static Set<String> getYoutubeVideoIds(final String body) {
        return getExternalUrls(body, uri -> !Strings.isNullOrEmpty(getYoutubeVideoId(uri)));
    }

    public static String getYoutubeVideoId(final URI uri) {
        if (uri != null && !Strings.isNullOrEmpty(uri.getHost())) {
            if (uri.getHost().contains("youtube.com")) {
                // e.g. https://www.youtube.com/watch?v=bhNse0kPybc
                final String query = uri.getQuery();
                if (query != null && query.startsWith("v=")) {
                    return query.substring(2);
                }
            } else if (uri.getHost().contains("youtu.be")) {
                // e.g. https://youtu.be/jnLqSytkl4A
                final String path = uri.getPath();
                if (path != null && path.startsWith("/")) {
                    return path.substring(1);
                }
            }
        }
        return null;
    }

    public static Set<String> getExternalUrls(final String body, final Predicate<URI> uriFilter) {
        if (Strings.isNullOrEmpty(body)) {
            return Collections.emptySet();
        }
        final Set<String> result = Sets.newTreeSet();
        final Elements links = Jsoup.parse(body).select("a");
        for (final Element link : links) {
            final String href = link.attributes().get("href");

            if (!Strings.isNullOrEmpty(href)) {
                try {
                    final URI uri = URI.create(href);
                    if (uriFilter.test(uri)) {
                        result.add(uri.toString());
                    }
                } catch (RuntimeException e) {
                    // just ignore wrong URL
                }
            }
        }
        return result;
    }

    public static Set<String> extractImageUrls(final String body) {
        if (Strings.isNullOrEmpty(body)) {
            return Collections.emptySet();
        }
        final Set<String> result = new LinkedHashSet<>();
        final Elements images = Jsoup.parse(body).select("img");
        for (final Element image : images) {
            final String src = image.absUrl("src");
            if (!Strings.isNullOrEmpty(src)) {
                result.add(src);
            }
        }
        return result;
    }
}
