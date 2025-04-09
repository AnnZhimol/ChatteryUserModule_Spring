package com.example.chatteryusermodule.services.base;

import com.example.chatteryusermodule.dto.BanWordDto;
import com.example.chatteryusermodule.dto.TranslationDto;
import com.example.chatteryusermodule.dto.creation.CreateBanWordDto;
import com.example.chatteryusermodule.models.BanWord;
import com.example.chatteryusermodule.repositories.BanWordRepository;
import com.example.chatteryusermodule.services.impl.BanWordServiceImpl;
import com.example.chatteryusermodule.services.impl.TranslationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.chatteryusermodule.utils.CipherUtil.decryptId;
import static com.example.chatteryusermodule.utils.CipherUtil.encryptId;

@Service
public class BanWordService implements BanWordServiceImpl {
    private final Logger log = LoggerFactory.getLogger(BanWordService.class);
    private final BanWordRepository banWordRepository;
    private final TranslationServiceImpl translationServiceImpl;

    public BanWordService(BanWordRepository banWordRepository, TranslationServiceImpl translationServiceImpl) {
        this.banWordRepository = banWordRepository;
        this.translationServiceImpl = translationServiceImpl;
    }

    private BanWord getBanWordFromDto(BanWordDto banWordDto) {
        return BanWord.builder()
                .id(decryptId(banWordDto.getId()))
                .word(banWordDto.getWord())
                .build();
    }

    private BanWordDto getBanWordDtoFromOrigin(BanWord banWord) {
        return BanWordDto.builder()
                .id(encryptId(banWord.getId()))
                .word(banWord.getWord())
                .build();
    }

    private BanWord getBanWordFromCreateDto(CreateBanWordDto banWordDto) {
        return BanWord.builder()
                .word(banWordDto.getWord())
                .translation(translationServiceImpl.getTranslationFromDto(
                        translationServiceImpl.getById(
                                banWordDto.getTranslationId()
                        )
                ))
                .build();
    }

    @Override
    public List<BanWordDto> getBanWordsByTranslation(String translationId) {
        TranslationDto translationDto = translationServiceImpl.getById(translationId);

        if (translationDto == null) {
            log.error("Search Translation with id = {} failed", decryptId(translationId));
            throw new NullPointerException();
        }

        return banWordRepository.findBanWordsByTranslation(
                translationServiceImpl.getTranslationFromDto(translationDto)
        ).stream().map(this::getBanWordDtoFromOrigin).toList();
    }

    @Override
    public void delete(BanWordDto entity) {
        if (entity == null) {
            log.error("Error deleting BanWord. BanWord = null");
            throw new NullPointerException();
        }

        banWordRepository.delete(getBanWordFromDto(entity));
        log.info("BanWord with id = {} successfully delete", decryptId(entity.getId()));
    }

    @Override
    public void add(CreateBanWordDto entity) {
        if (entity == null) {
            log.error("Error adding BanWord. BanWord = null");
            throw new NullPointerException();
        }

        BanWord newBannedWord = banWordRepository.save(getBanWordFromCreateDto(entity));
        log.info("BanWord with id = {} successfully added", newBannedWord.getId());
    }

    @Override
    public BanWordDto getById(String id) {
        Long longId = decryptId(id);
        BanWord foundBanWord = banWordRepository.findById(longId).orElse(null);

        if (foundBanWord != null) {
            log.info("BanWord with id = {} successfully find", longId);
            return getBanWordDtoFromOrigin(foundBanWord);
        }

        log.error("Search BanWord with id = {} failed", longId);
        return null;
    }

    @Override
    public void deleteBanWordsByTranslation(String translationId) {
        TranslationDto translationDto = translationServiceImpl.getById(translationId);

        if (translationDto == null) {
            log.error("Search Translation with id = {} failed", decryptId(translationId));
            throw new NullPointerException();
        }

        banWordRepository.deleteBanWordsByTranslation(
                translationServiceImpl.getTranslationFromDto(translationDto)
        );
    }
}
