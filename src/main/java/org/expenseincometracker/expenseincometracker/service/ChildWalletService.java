package org.expenseincometracker.expenseincometracker.service;

import org.expenseincometracker.expenseincometracker.dto.response.ChildWalletResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChildWalletService {
    List<ChildWalletResponse> getChildWallets(Authentication authentication);
}
