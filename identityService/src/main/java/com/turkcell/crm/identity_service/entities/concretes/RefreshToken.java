package com.turkcell.crm.identity_service.entities.concretes;

import com.turkcell.crm.identity_service.core.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
@SQLRestriction(value = "deleted_date is null")
public class RefreshToken extends BaseEntity<Integer> {
    private String revokedByIp;
    private LocalDateTime revokedDate;
    private String revokeReason;
    private String token;
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
