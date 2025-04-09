package com.example.chatteryusermodule.services.impl;

import com.example.chatteryusermodule.api.response.ConnectResponse;
import com.example.chatteryusermodule.dto.TranslationDto;
import com.example.chatteryusermodule.dto.creation.CreateTranslationDto;
import com.example.chatteryusermodule.models.Translation;

import java.util.List;

public interface TranslationServiceImpl {
    Translation add(CreateTranslationDto entity);
    TranslationDto getById(String id);
    void edit(TranslationDto entity);
    List<TranslationDto> getTranslationsByUser(String userId);
    void delete(TranslationDto entity);
    Translation getTranslationFromDto(TranslationDto translationDto);
    ConnectResponse connect(String translationId);
    ConnectResponse disconnect(String translationId);
    void save(Translation entity);
}
