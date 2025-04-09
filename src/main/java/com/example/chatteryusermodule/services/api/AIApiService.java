package com.example.chatteryusermodule.services.api;

import com.example.chatteryusermodule.api.AIApi;
import com.example.chatteryusermodule.api.response.ConnectResponse;
import com.example.chatteryusermodule.models.enums.PlatformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

import static com.example.chatteryusermodule.utils.RegexPlatformUtil.checkRegex;

@Service
public class AIApiService {
    private final AIApi aiApi;
    private final Logger log = LoggerFactory.getLogger(AIApiService.class);

    public AIApiService(AIApi aiApi) {
        this.aiApi = aiApi;
    }

    public ConnectResponse setConnect(String url, PlatformType platformType, String id) {
        if (!checkRegex(platformType, url)) {
            log.error("Error set connect to VK. Url or Platform not correct");
            throw new IllegalArgumentException();
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String[] segments = uri.getPath().split("/");
        String channel = segments[segments.length - 1];

        if (platformType == PlatformType.TWITCH){
            return aiApi.setConnectTwitch(channel, id);
        }
        if (platformType == PlatformType.VK_VIDEO_LIVE){
            return aiApi.setConnectVK(channel, id);
        } else {
            return ConnectResponse.builder()
                    .status("error")
                    .message("Wrong platform type.")
                    .url(null)
                    .build();
        }
    }

    public ConnectResponse removeConnect(String url, PlatformType platformType, String id) {
        if (!checkRegex(platformType, url)) {
            log.error("Error remove connect to VK. Url or Platform not correct");
            throw new IllegalArgumentException();
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String[] segments = uri.getPath().split("/");
        String channel = segments[segments.length - 1];

        if (platformType == PlatformType.TWITCH){
            return aiApi.removeConnectTwitch(channel, id);
        }
        if (platformType == PlatformType.VK_VIDEO_LIVE){
            return aiApi.removeConnectVK(channel, id);
        } else {
            return ConnectResponse.builder()
                    .status("error")
                    .message("Wrong platform type.")
                    .url(null)
                    .build();
        }
    }
}
