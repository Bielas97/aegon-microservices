package com.aegon;

import com.aegon.domain.CustomerName;
import com.aegon.util.lang.Address;
import com.aegon.util.lang.Email;
import java.time.LocalDate;

public record CustomerUpdateRequest(CustomerName name, Email email, Address address, LocalDate birthDate) {

}
