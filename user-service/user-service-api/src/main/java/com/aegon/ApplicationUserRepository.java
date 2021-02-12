package com.aegon;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUsername;
import com.aegon.util.lang.DomainReactiveRepository;
import reactor.core.publisher.Mono;

public interface ApplicationUserRepository extends DomainReactiveRepository<ApplicationUserId, ApplicationUser> {

	Mono<ApplicationUser> findByUsername(ApplicationUsername username);

}
