package com.example.chatteryusermodule.utils;

import com.example.chatteryusermodule.models.enums.PlatformType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPlatformUtil {
    private static boolean isValidUrl(String url, Pattern pattern) {
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean checkRegex(PlatformType pltType, String url) {
        String regexVK = "https://live\\.vkvideo\\.ru/[a-zA-Z0-9_]+";
        Pattern patternVK = Pattern.compile(regexVK);

        String regexTwitch = "https://www\\.twitch\\.tv/[a-zA-Z0-9_]+";
        Pattern patternTwitch = Pattern.compile(regexTwitch);

        if (!isValidUrl(url, patternVK) && !isValidUrl(url, patternTwitch)) {
            return false;
        }

        return (pltType != PlatformType.TWITCH || !isValidUrl(url, patternVK))
                && (pltType != PlatformType.VK_VIDEO_LIVE || !isValidUrl(url, patternTwitch));
    }
}
