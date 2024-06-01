package com.turkcell.crm.search_service.business.concretes;

import com.turkcell.crm.search_service.business.abstracts.CustomerSearchService;
import com.turkcell.crm.search_service.business.dtos.responses.customers.SearchCustomersResponse;
import com.turkcell.crm.search_service.business.mappers.CustomerMapper;
import com.turkcell.crm.search_service.core.services.search.SearchService;
import com.turkcell.crm.search_service.core.services.search.models.DynamicQuery;
import com.turkcell.crm.search_service.data_access.abstracts.CustomerRepository;
import com.turkcell.crm.search_service.entities.concretes.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSearchManager implements CustomerSearchService {
    private final CustomerRepository customerRepository;
    private final SearchService searchService;
    private final CustomerMapper customerMapper;

    @Override
    public void add(Customer customer) {
        this.customerRepository.save(customer);
    }

    @Override
    public void update(Customer customer) {
        this.customerRepository.save(customer);
    }

    @Override
    public void delete(int customerId) {
        this.customerRepository.deleteById(customerId);
    }

    public List<SearchCustomersResponse> searchCustomers(DynamicQuery dynamicQuery) {
        List<Customer> customers = searchService.dynamicSearch(dynamicQuery, Customer.class);
        return this.customerMapper.toSearchCustomersResponses(customers);
    }
}
