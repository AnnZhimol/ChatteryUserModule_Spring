package com.example.chatteryusermodule.services.base;

import com.example.chatteryusermodule.dto.StreamSettingDto;
import com.example.chatteryusermodule.dto.creation.CreateStreamSettingDto;
import com.example.chatteryusermodule.models.StreamSetting;
import com.example.chatteryusermodule.repositories.StreamSettingRepository;
import com.example.chatteryusermodule.services.impl.StreamSettingServiceImpl;
import com.example.chatteryusermodule.services.impl.TranslationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.chatteryusermodule.utils.CipherUtil.decryptId;
import static com.example.chatteryusermodule.utils.CipherUtil.encryptId;

@Service
public class StreamSettingService implements StreamSettingServiceImpl {
    private final StreamSettingRepository streamSettingRepository;
    private final TranslationServiceImpl translationServiceImpl;
    private final Logger log = LoggerFactory.getLogger(StreamSettingService.class);

    public StreamSettingService(StreamSettingRepository streamSettingRepository, TranslationServiceImpl translationServiceImpl) {
        this.streamSettingRepository = streamSettingRepository;
        this.translationServiceImpl = translationServiceImpl;
    }

    public StreamSetting getStreamSettingFromDto(StreamSettingDto streamSettingDto) {
        return StreamSetting.builder()
                .id(decryptId(streamSettingDto.getId()))
                .backgroundColor(streamSettingDto.getBackgroundColor())
                .textColor(streamSettingDto.getTextColor())
                .textSize(streamSettingDto.getTextSize())
                .font(streamSettingDto.getFont())
                .aligmentType(streamSettingDto.getAligmentType())
                .delay(streamSettingDto.getDelay())
                .moodType(streamSettingDto.getMoodType())
                .sentenceType(streamSettingDto.getSentenceType())
                .translation(translationServiceImpl.getTranslationFromDto(
                        translationServiceImpl.getById(streamSettingDto.getTranslationId())
                ))
                .build();
    }

    private StreamSettingDto getStreamSettingDtoFromOrigin(StreamSetting streamSetting) {
        return StreamSettingDto.builder()
                .id(encryptId(streamSetting.getId()))
                .backgroundColor(streamSetting.getBackgroundColor())
                .textColor(streamSetting.getTextColor())
                .textSize(streamSetting.getTextSize())
                .font(streamSetting.getFont())
                .aligmentType(streamSetting.getAligmentType())
                .delay(streamSetting.getDelay())
                .moodType(streamSetting.getMoodType())
                .sentenceType(streamSetting.getSentenceType())
                .translationId(encryptId(streamSetting.getTranslation().getId()))
                .build();
    }

    @Deprecated
    private StreamSetting getStreamSettingFromCreateDto(CreateStreamSettingDto createStreamSettingDto) {
        return StreamSetting.builder()
                .backgroundColor(createStreamSettingDto.getBackgroundColor())
                .textColor(createStreamSettingDto.getTextColor())
                .textSize(createStreamSettingDto.getTextSize())
                .font(createStreamSettingDto.getFont())
                .aligmentType(createStreamSettingDto.getAligmentType())
                .delay(createStreamSettingDto.getDelay())
                .moodType(createStreamSettingDto.getMoodType())
                .sentenceType(createStreamSettingDto.getSentenceType())
                .translation(translationServiceImpl.getTranslationFromDto(
                        translationServiceImpl.getById(
                                createStreamSettingDto.getTranslationId()
                        )
                ))
                .build();
    }

    @Override
    public void add(StreamSetting entity) {
        if (entity == null) {
            log.error("Error adding StreamSetting. StreamSetting = null");
            throw new NullPointerException();
        }

        StreamSetting newStreamSetting = streamSettingRepository.save(entity);
        log.info("StreamSetting with id = {} successfully added", newStreamSetting.getId());
    }

    @Override
    public StreamSettingDto getById(String id) {
        Long longId = decryptId(id);
        StreamSetting foundStreamSetting = streamSettingRepository.findById(longId).orElse(null);

        if (foundStreamSetting != null) {
            log.info("StreamSetting with id = {} successfully find", longId);
            return getStreamSettingDtoFromOrigin(foundStreamSetting);
        }

        log.error("Search StreamSetting with id = {} failed", longId);
        return null;
    }

    @Override
    public StreamSettingDto getByTranslationId(String translationId) {
        Long longId = decryptId(translationId);
        StreamSetting foundStreamSetting = streamSettingRepository.findStreamSettingByTranslation_Id(longId);

        if (foundStreamSetting != null) {
            log.info("StreamSetting with id = {} successfully find", foundStreamSetting.getId());
            return getStreamSettingDtoFromOrigin(foundStreamSetting);
        }

        log.error("Search StreamSetting with id = {} failed", foundStreamSetting.getId());
        return null;
    }

    @Override
    public void edit(StreamSettingDto entity) {
        StreamSettingDto currentStreamSetting = getById(entity.getId());
        Long longId = decryptId(entity.getId());

        if (currentStreamSetting == null) {
            log.error("StreamSetting with id = {} not found", longId);
            throw new EntityNotFoundException();
        }

        currentStreamSetting.setBackgroundColor(entity.getBackgroundColor() == null ? currentStreamSetting.getBackgroundColor() : entity.getBackgroundColor());
        currentStreamSetting.setTextColor(entity.getTextColor() == null ? currentStreamSetting.getTextColor() : entity.getTextColor());
        currentStreamSetting.setTextSize(entity.getTextSize() == null ? currentStreamSetting.getTextSize() : entity.getTextSize());
        currentStreamSetting.setFont(entity.getFont() == null ? currentStreamSetting.getFont() : entity.getFont());
        currentStreamSetting.setAligmentType(entity.getAligmentType() == null ? currentStreamSetting.getAligmentType() : entity.getAligmentType());
        currentStreamSetting.setDelay(entity.getDelay() == null ? currentStreamSetting.getDelay() : entity.getDelay());
        currentStreamSetting.setMoodType(entity.getMoodType() == null ? currentStreamSetting.getMoodType() : entity.getMoodType());
        currentStreamSetting.setSentenceType(entity.getSentenceType() == null ? currentStreamSetting.getSentenceType() : entity.getSentenceType());
        currentStreamSetting.setTranslationId(entity.getTranslationId() == null ? currentStreamSetting.getTranslationId() : entity.getTranslationId());

        streamSettingRepository.save(getStreamSettingFromDto(currentStreamSetting));
        log.info("StreamSetting with id = {} successfully edit", longId);
    }

    @Override
    public void save(StreamSetting entity) {
        streamSettingRepository.save(entity);
        log.info("StreamSetting with id = {} successfully saved", entity.getId());
    }
}
