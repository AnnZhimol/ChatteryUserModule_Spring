package com.example.chatteryusermodule.services.grpc;
import com.example.chatteryusermodule.controllers.ChatController;
import com.example.chatteryusermodule.dto.SentMessage;
import com.example.chatteryusermodule.dto.TranslationDto;
import com.example.chatteryusermodule.services.impl.TranslationServiceImpl;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import messages.MessageServiceGrpc;
import messages.Twitch;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final ChatController webSocketController;
    private final TranslationServiceImpl translationServiceImpl;

    public MessageServiceImpl(ChatController webSocketController, TranslationServiceImpl translationServiceImpl) {
        this.webSocketController = webSocketController;
        this.translationServiceImpl = translationServiceImpl;
    }

    @Override
    public void sendMessage(Twitch.TwitchMessage request, StreamObserver<Twitch.ResponseMessage> responseObserver) {
        TranslationDto translationDto = translationServiceImpl.getById(request.getTransId());

        if (translationDto == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Translation not found")
                    .asRuntimeException());
            return;
        }

        logger.info("Received gRPC message from {}: {}", request.getUser(), request.getMessage());
        SentMessage messageBody = SentMessage.builder()
                .user(request.getUser())
                .message(request.getMessage())
                .sentenceType(request.getSentenceType())
                .sentimentType(request.getSentimentType())
                .parentUser(request.getParentUser())
                .parentMessage(request.getParentMessage())
                .channel(request.getChannel())
                .timestamp(request.getTimestamp())
                .transId(request.getTransId())
                .build();

        webSocketController.broadcastMessage(messageBody);

        Twitch.ResponseMessage response = Twitch.ResponseMessage.newBuilder().setStatus("Received").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
