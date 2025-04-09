package com.example.chatteryusermodule.repositories;

import com.example.chatteryusermodule.models.Translation;
import com.example.chatteryusermodule.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findTranslationsByUser(User user);
}
