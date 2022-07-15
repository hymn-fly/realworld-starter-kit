package org.realworld.demo.domain.article.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utility {

//    private static final Pattern NONTEXT = Pattern.compile("[가-힣-][^\\w-]"); // [^\w-]
    private static final Pattern NONTEXT = Pattern.compile("[^\\w-]"); // [^\w-]
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String title) {
        String noWhiteSpace = WHITESPACE.matcher(title).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONTEXT.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

}
