package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.api.response.ConnectResponse;
import com.example.chatteryusermodule.dto.TranslationDto;
import com.example.chatteryusermodule.dto.creation.CreateTranslationDto;
import com.example.chatteryusermodule.models.StreamSetting;
import com.example.chatteryusermodule.models.Translation;
import com.example.chatteryusermodule.services.impl.StreamSettingServiceImpl;
import com.example.chatteryusermodule.services.impl.TranslationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/translation")
public class TranslationController {
    private final TranslationServiceImpl translationServiceImpl;
    private final StreamSettingServiceImpl streamSettingServiceImpl;

    public TranslationController(TranslationServiceImpl translationServiceImpl, StreamSettingServiceImpl streamSettingServiceImpl) {
        this.translationServiceImpl = translationServiceImpl;
        this.streamSettingServiceImpl = streamSettingServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTranslation(@RequestBody CreateTranslationDto createTranslationDto) {
        try {
            Translation translation = translationServiceImpl.add(createTranslationDto);

            StreamSetting streamSetting = StreamSetting.builder()
                    .translation(translation)
                    .build();

            streamSettingServiceImpl.add(streamSetting);

            translation.setStreamSetting(streamSetting);
            streamSetting.setTranslation(translation);

            streamSettingServiceImpl.save(streamSetting);
            translationServiceImpl.save(translation);

            return new ResponseEntity<>("Translation successfully add.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editTranslation(@RequestBody TranslationDto translationDto) {
        try {
            translationServiceImpl.edit(translationDto);
            return new ResponseEntity<>("Translation successfully edit.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTranslation")
    public ResponseEntity<TranslationDto> getTranslation(@RequestParam String translationId) {
        TranslationDto translation = translationServiceImpl.getById(translationId);
        return translation == null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(translation, HttpStatus.OK);
    }

    @GetMapping("/getTranslations")
    public ResponseEntity<List<TranslationDto>> getTranslations(@RequestParam String userId) {
        List<TranslationDto> translations = translationServiceImpl.getTranslationsByUser(userId);
        return translations.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(translations, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTranslation(@RequestBody TranslationDto translationDto) {
        try{
            translationServiceImpl.delete(translationDto);
            return new ResponseEntity<>("Translation successfully delete.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/connect")
    public ResponseEntity<String> connectTranslation(@RequestParam String translationId) {
        ConnectResponse response = translationServiceImpl.connect(translationId);
        if (Objects.equals(response.getStatus(), "error")) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/disconnect")
    public ResponseEntity<String> disconnectTranslation(@RequestParam String translationId) {
        ConnectResponse response = translationServiceImpl.disconnect(translationId);
        if (Objects.equals(response.getStatus(), "error")) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
        }
    }
}
