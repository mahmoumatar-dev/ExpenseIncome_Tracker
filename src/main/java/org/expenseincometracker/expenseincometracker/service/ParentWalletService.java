package org.expenseincometracker.expenseincometracker.service;
import org.expenseincometracker.expenseincometracker.dto.request.CreateWalletRequest;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateWalletRequest;
import org.expenseincometracker.expenseincometracker.dto.response.WalletResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ParentWalletService {

    WalletResponse createWallet(CreateWalletRequest request, Authentication authentication);

    WalletResponse updateWallet(Long walletId, UpdateWalletRequest request, Authentication authentication);

    List<WalletResponse> getParentWallets(Authentication authentication);

    void deleteWallet(Long walletId, Authentication authentication);
}
