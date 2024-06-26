package com.turkcell.crm.basket_service.data_access.abstracts;

import com.turkcell.crm.basket_service.entities.concretes.Basket;

import java.util.Map;
import java.util.Optional;

public interface BasketRepository {
    Map<String, Basket> getAll();

    Optional<Basket> getById(String id);

    void addOrUpdate(Basket basket);

    void delete(String id);
}
