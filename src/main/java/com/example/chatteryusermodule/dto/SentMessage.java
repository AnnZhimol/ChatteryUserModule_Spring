package com.example.chatteryusermodule.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SentMessage implements Serializable {
    @SerializedName("user")
    private String user;
    @SerializedName("message")
    private String message;
    @SerializedName("sentence_type")
    private String sentenceType;
    @SerializedName("sentiment_type")
    private String sentimentType;
    @SerializedName("parent_user")
    private String parentUser;
    @SerializedName("parent_message")
    private String parentMessage;
    @SerializedName("channel")
    private String channel;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("trans_id")
    private String transId;
}