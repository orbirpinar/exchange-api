package com.orbirpinar.exchange.repository;

import com.orbirpinar.exchange.entity.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion,String> {

    @Query(value = "SELECT e from Conversion e WHERE e.transactionDate BETWEEN :from AND :to")
    Page<Conversion> findAllByBetweenTransactionDates(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable);
}
