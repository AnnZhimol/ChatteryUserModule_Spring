package com.example.chatteryusermodule.api;

import com.example.chatteryusermodule.api.response.ConnectResponse;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class AIApi {
    private HttpHeaders headers = new HttpHeaders();
    private final RestTemplate restTemplate = new RestTemplate();

    public AIApi() {
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    private void setHeaders() {
        headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.setContentType(MediaType.valueOf("text/csv; charset=UTF-8"));
    }

    public ConnectResponse setConnectTwitch(@NotNull String channel, @NotNull String id) {
        String urlTemplate  = "http://127.0.0.1:8000/connect_twitch/{channel}/{id}";

        setHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlTemplate);

        String url = builder.buildAndExpand(channel, id).toUriString();

        return getConnectResponse(url);
    }

    public ConnectResponse setConnectVK(@NotNull String channel, @NotNull String id) {
        String urlTemplate  = "http://127.0.0.1:8000/connect_vk/{channel}/{id}";

        setHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlTemplate);

        String url = builder.buildAndExpand(channel, id).toUriString();
        System.out.println("Requesting: " + url);

        return getConnectResponse(url);
    }

    @Nullable
    private ConnectResponse getConnectResponse(String url) {
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new Gson().fromJson(response.getBody(), ConnectResponse.class);
        } else {
            return null;
        }
    }

    public ConnectResponse removeConnectTwitch(@NotNull String channel, @NotNull String id) {
        String urlTemplate  = "http://127.0.0.1:8000/disconnect_twitch/{channel}/{id}";

        setHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlTemplate);

        String url = builder.buildAndExpand(channel, id).toUriString();

        return getConnectResponse(url);
    }

    public ConnectResponse removeConnectVK(@NotNull String channel, @NotNull String id) {
        String urlTemplate  = "http://127.0.0.1:8000/disconnect_vk/{channel}/{id}";

        setHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlTemplate);

        String url = builder.buildAndExpand(channel, id).toUriString();

        return getConnectResponse(url);
    }
}
