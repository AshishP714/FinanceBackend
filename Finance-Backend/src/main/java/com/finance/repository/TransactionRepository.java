package com.finance.repository;

import com.finance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDeletedFalse();

    List<Transaction> findByDeletedFalseAndType(String type);

    List<Transaction> findByDeletedFalseAndCategory(String category);

    List<Transaction> findByDeletedFalseAndDateBetween(LocalDate start, LocalDate end);

    List<Transaction> findTop10ByDeletedFalseOrderByDateDesc();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.deleted = false")
    BigDecimal sumByType(@Param("type") String type);

    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE t.deleted = false GROUP BY t.category")
    List<Object[]> categoryTotals();

    @Query("SELECT MONTH(t.date), YEAR(t.date), t.type, SUM(t.amount) FROM Transaction t WHERE t.deleted = false GROUP BY YEAR(t.date), MONTH(t.date), t.type ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> monthlyTrends();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.deleted = false AND t.date >= :start")
    BigDecimal sumByTypeAndDateAfter(@Param("type") String type, @Param("start") LocalDate start);
}