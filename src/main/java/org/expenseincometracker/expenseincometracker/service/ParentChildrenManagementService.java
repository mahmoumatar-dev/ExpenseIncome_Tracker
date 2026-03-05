package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateChildRequest;
import org.springframework.security.core.Authentication;

public interface ParentChildrenManagementService {
    void createChild(CreateChildRequest request, Authentication authentication);
}
