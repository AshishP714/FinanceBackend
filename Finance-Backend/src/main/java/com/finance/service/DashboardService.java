package com.finance.service;

import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository repo;

    public Map<String, Object> getSummary() {
        BigDecimal income = Optional.ofNullable(repo.sumByType("INCOME")).orElse(BigDecimal.ZERO);
        BigDecimal expense = Optional.ofNullable(repo.sumByType("EXPENSE")).orElse(BigDecimal.ZERO);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("totalIncome", income);
        map.put("totalExpenses", expense);
        map.put("netBalance", income.subtract(expense));
        return map;
    }

    public Map<String, BigDecimal> getCategoryBreakdown() {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] row : repo.categoryTotals()) {
            result.put((String) row[0], (BigDecimal) row[1]);
        }
        return result;
    }

    public List<Map<String, Object>> getMonthlyTrends() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : repo.monthlyTrends()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("month", row[0]);
            entry.put("year", row[1]);
            entry.put("type", row[2]);
            entry.put("total", row[3]);
            result.add(entry);
        }
        return result;
    }

    public List<?> getRecentActivity() {
        return repo.findTop10ByDeletedFalseOrderByDateDesc();
    }

    public Map<String, Object> getWeeklySummary() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        BigDecimal income = Optional.ofNullable(repo.sumByTypeAndDateAfter("INCOME", weekAgo)).orElse(BigDecimal.ZERO);
        BigDecimal expense = Optional.ofNullable(repo.sumByTypeAndDateAfter("EXPENSE", weekAgo)).orElse(BigDecimal.ZERO);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("weeklyIncome", income);
        map.put("weeklyExpenses", expense);
        map.put("period", "Last 7 days");
        return map;
    }
}