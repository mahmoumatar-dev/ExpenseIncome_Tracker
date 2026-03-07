package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.request.CreateChildRequest;
import org.expenseincometracker.expenseincometracker.dto.response.ChildResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ParentChildrenManagementService {
    void createChild(CreateChildRequest request, Authentication authentication);
    List<ChildResponse> getChildren(Authentication authentication);
    void updateChildStatus(Long childId, Authentication authentication);
}
