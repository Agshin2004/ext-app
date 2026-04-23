package com.agshin.extapp.features.user.infrastructure;

import com.agshin.extapp.features.user.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    @Query("""
            SELECT t from PasswordResetToken t
                WHERE t.expiresAt > :now
                and t.used = false
            """)
    List<PasswordResetToken> findValidTokens(Instant now);
}
