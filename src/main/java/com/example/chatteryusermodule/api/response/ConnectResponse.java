package com.example.chatteryusermodule.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectResponse {
    private String status;
    private String message;
    private String url;
    private String id;
}
