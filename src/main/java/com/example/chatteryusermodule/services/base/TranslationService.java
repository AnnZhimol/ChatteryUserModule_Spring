package com.example.chatteryusermodule.services.base;

import com.example.chatteryusermodule.api.response.ConnectResponse;
import com.example.chatteryusermodule.dto.TranslationDto;
import com.example.chatteryusermodule.dto.UserDto;
import com.example.chatteryusermodule.dto.creation.CreateTranslationDto;
import com.example.chatteryusermodule.models.Translation;
import com.example.chatteryusermodule.repositories.TranslationRepository;
import com.example.chatteryusermodule.services.api.AIApiService;
import com.example.chatteryusermodule.services.impl.TranslationServiceImpl;
import com.example.chatteryusermodule.services.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.chatteryusermodule.utils.CipherUtil.decryptId;
import static com.example.chatteryusermodule.utils.CipherUtil.encryptId;
import static com.example.chatteryusermodule.utils.RegexPlatformUtil.checkRegex;

@Service
public class TranslationService implements TranslationServiceImpl {
    private final TranslationRepository translationRepository;
    private final UserServiceImpl userServiceImpl;
    private final AIApiService aiApiService;
    private final Logger log = LoggerFactory.getLogger(TranslationService.class);

    public TranslationService(TranslationRepository translationRepository, UserServiceImpl userServiceImpl, AIApiService aiApiService) {
        this.translationRepository = translationRepository;
        this.userServiceImpl = userServiceImpl;
        this.aiApiService = aiApiService;
    }

    public Translation getTranslationFromDto(TranslationDto translationDto) {
        return Translation.builder()
                .id(decryptId(translationDto.getId()))
                .url(translationDto.getUrl())
                .name(translationDto.getName())
                .platformType(translationDto.getPlatformType())
                .build();
    }

    @Override
    public ConnectResponse connect(String translationId) {
        TranslationDto translationDto = getById(translationId);
        return aiApiService.setConnect(translationDto.getUrl(), translationDto.getPlatformType(), translationId);
    }

    @Override
    public ConnectResponse disconnect(String translationId) {
        TranslationDto translationDto = getById(translationId);
        return aiApiService.removeConnect(translationDto.getUrl(),translationDto.getPlatformType(), translationId);
    }

    private TranslationDto getTranslationDtoFromOrigin(Translation translation) {
        return TranslationDto.builder()
                .id(encryptId(translation.getId()))
                .url(translation.getUrl())
                .name(translation.getName())
                .platformType(translation.getPlatformType())
                .build();
    }

    private Translation getTranslationFromCreateDto(CreateTranslationDto createTranslationDto) {
        return Translation.builder()
                .url(createTranslationDto.getUrl())
                .name(createTranslationDto.getName())
                .platformType(createTranslationDto.getPlatformType())
                .user(userServiceImpl.getUserFromDto(
                        userServiceImpl.getById(createTranslationDto.getUserId())
                ))
                .build();
    }

    @Override
    public Translation add(CreateTranslationDto entity) {
        if (entity == null) {
            log.error("Error adding Translation. Translation = null");
            throw new NullPointerException();
        }

        if (!checkRegex(entity.getPlatformType(), entity.getUrl())) {
            log.error("Error adding Translation. Url or Platform not correct");
            throw new IllegalArgumentException();
        }

        Translation newTranslation = translationRepository.save(getTranslationFromCreateDto(entity));
        log.info("Translation with id = {} ({}) successfully added", newTranslation.getId(), encryptId(newTranslation.getId()));

        return newTranslation;
    }

    @Override
    public TranslationDto getById(String id) {
        Long longId = decryptId(id);
        Translation foundTranslation = translationRepository.findById(longId).orElse(null);

        if (foundTranslation != null) {
            log.info("Translation with id = {} ({}) successfully find", longId, encryptId(longId));
            return getTranslationDtoFromOrigin(foundTranslation);
        }

        log.error("Search Translation with id = {} ({}) failed", longId, encryptId(longId));
        return null;
    }

    @Override
    public void save(Translation entity) {
        translationRepository.save(entity);
        log.info("Translation with id = {} successfully saved", entity.getId());
    }

    @Override
    public void edit(TranslationDto entity) {
        TranslationDto currentTranslation = getById(entity.getId());
        long longId = decryptId(entity.getId());

        if (currentTranslation == null) {
            log.error("Translation with id = {} ({}) not found", longId, encryptId(longId));
            throw new EntityNotFoundException();
        }

        if (!checkRegex(entity.getPlatformType(), entity.getUrl())
                || !checkRegex(currentTranslation.getPlatformType(), entity.getUrl())
                || !checkRegex(entity.getPlatformType(), currentTranslation.getUrl())) {
            log.error("Error edit Translation. Url or Platform not correct");
            throw new IllegalArgumentException();
        }

        currentTranslation.setName(entity.getName() == null ? currentTranslation.getName() : entity.getName());
        currentTranslation.setUrl(entity.getUrl() == null ? currentTranslation.getUrl() : entity.getUrl());
        currentTranslation.setPlatformType(entity.getPlatformType() == null ? currentTranslation.getPlatformType() : entity.getPlatformType());

        translationRepository.save(getTranslationFromDto(currentTranslation));
        log.info("Translation with id = {} ({}) successfully edit", longId, encryptId(longId));
    }

    @Override
    public List<TranslationDto> getTranslationsByUser(String userId) {
        UserDto userDto = userServiceImpl.getById(userId);

        if (userDto == null) {
            log.error("Search User with id = {} ({}) failed", decryptId(userId), userId);
            throw new NullPointerException();
        }

        return translationRepository.findTranslationsByUser(
                userServiceImpl.getUserFromDto(userDto)
        ).stream().map(this::getTranslationDtoFromOrigin).toList();
    }

    @Override
    public void delete(TranslationDto entity) {
        if (entity == null) {
            log.error("Error deleting Translation. Translation = null");
            throw new NullPointerException();
        }

        Translation translation = translationRepository.findById(decryptId(entity.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Translation not found"));

        translationRepository.delete(translation);
        log.info("Translation with id = {} ({}) successfully deleted", decryptId(entity.getId()), entity.getId());
    }
}
