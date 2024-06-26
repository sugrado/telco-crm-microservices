package com.turkcell.crm.account_service.entities.concretes;

import com.turkcell.crm.account_service.core.entities.BaseEntity;
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
@Table(name = "account_types")
@SQLRestriction(value = "deleted_date is null")
public class AccountType extends BaseEntity<Integer> {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<Account> accounts;
}
