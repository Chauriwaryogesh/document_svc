package com.SecureAccessPortal.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.PaymentSequence;

public interface PaymentSequenceRepo extends JpaRepository<PaymentSequence, String> {
	Optional<PaymentSequence> findByYearMonth(String yearMonth);
}
