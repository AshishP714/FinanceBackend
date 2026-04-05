package com.finance.service;

import com.finance.dto.TransactionRequest;
import com.finance.entity.Transaction;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;

    public Transaction create(TransactionRequest req) {
        Transaction t = Transaction.builder()
                .amount(req.getAmount())
                .type(req.getType().toUpperCase())
                .category(req.getCategory())
                .date(req.getDate())
                .notes(req.getNotes())
                .deleted(false)
                .build();
        return repo.save(t);
    }

    public List<Transaction> getAll(String type, String category, LocalDate start, LocalDate end) {
        if (type != null) return repo.findByDeletedFalseAndType(type.toUpperCase());
        if (category != null) return repo.findByDeletedFalseAndCategory(category);
        if (start != null && end != null) return repo.findByDeletedFalseAndDateBetween(start, end);
        return repo.findByDeletedFalse();
    }

    public Transaction getById(Long id) {
        return repo.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Transaction update(Long id, TransactionRequest req) {
        Transaction t = getById(id);
        t.setAmount(req.getAmount());
        t.setType(req.getType().toUpperCase());
        t.setCategory(req.getCategory());
        t.setDate(req.getDate());
        t.setNotes(req.getNotes());
        return repo.save(t);
    }

    public void softDelete(Long id) {
        Transaction t = getById(id);
        t.setDeleted(true);
        repo.save(t);
    }
}