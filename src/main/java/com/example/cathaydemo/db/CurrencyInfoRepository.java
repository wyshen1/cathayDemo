package com.example.cathaydemo.db;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CurrencyInfoRepository extends JpaRepository<CurrencyInfoEntity, Long> {

    Optional<CurrencyInfoEntity> findByCode(String code);
    @Transactional
    Integer deleteByCode(String code);
}
