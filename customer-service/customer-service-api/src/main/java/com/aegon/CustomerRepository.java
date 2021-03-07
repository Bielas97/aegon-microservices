package com.aegon;

import com.aegon.domain.Customer;
import com.aegon.domain.CustomerId;
import com.aegon.util.lang.DomainReactiveRepository;

// TODO move to core/domain
public interface CustomerRepository extends DomainReactiveRepository<CustomerId, Customer> {

}
