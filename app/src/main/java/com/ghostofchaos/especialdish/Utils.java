package com.ghostofchaos.especialdish;

import java.util.regex.Pattern;

/**
 * Created by Ghost on 18.03.2016.
 */
public class Utils {

    public static String specCharactersHtmlToString(String html) {
        String hex = null;
        if (html != null) {
            hex = html.replaceAll(Pattern.quote("&amp;ndash;"), "\u2013")
                    .replaceAll(Pattern.quote("&amp;quot;"), "\"")
                    .replaceAll(Pattern.quote("&amp;laquo;"), "«")
                    .replaceAll(Pattern.quote("&amp;raquo;"), "»")
                    .replaceAll(Pattern.quote("&amp;nbsp;"), " ")
                    .replaceAll(Pattern.quote("\\&quot;"), "\"")
                            //.replaceAll(Pattern.quote("&amp;"), "ñ")
                    .replaceAll(Pattern.quote("&amp;"), "&");
        }
        return hex;
    }
}
