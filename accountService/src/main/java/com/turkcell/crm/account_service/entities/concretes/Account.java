package com.turkcell.crm.account_service.entities.concretes;

import com.turkcell.crm.account_service.core.entities.BaseEntity;
import com.turkcell.crm.account_service.entities.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
@SQLRestriction(value = "deleted_date is null")
public class Account extends BaseEntity<Integer> {
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private String number;

    @Column(name = "customer_id")
    private int customerId;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AccountType type;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<AccountAddress> addresses;

    public Account(int id) {
        setId(id);
    }
}
