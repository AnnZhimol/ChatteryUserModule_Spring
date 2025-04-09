package com.example.chatteryusermodule.repositories;

import com.example.chatteryusermodule.models.BanWord;
import com.example.chatteryusermodule.models.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BanWordRepository extends JpaRepository<BanWord, Long> {
    List<BanWord> findBanWordsByTranslation(Translation translation);
    @Transactional
    void deleteBanWordsByTranslation(Translation translation);
}
