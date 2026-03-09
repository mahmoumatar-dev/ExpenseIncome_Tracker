package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.response.ChildWalletResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.entity.Wallet;
import org.expenseincometracker.expenseincometracker.exception.ResourceNotFoundException;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.repository.WalletRepository;
import org.expenseincometracker.expenseincometracker.service.ChildWalletService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChildWalletServiceImpl implements ChildWalletService {

    private final WalletRepository walletRepository;
    private final UserHelper userHelper;

    @Override
    public List<ChildWalletResponse> getChildWallets(Authentication authentication) {

        User child = userHelper.getAuthenticatedUser(authentication);

        List<Wallet> wallets = walletRepository.findByAssignedChildren_Id(child.getId());

        if (wallets.isEmpty()) {
            throw new ResourceNotFoundException("No wallet assigned to this child");
        }

        return wallets.stream().map(wallet -> new ChildWalletResponse(
                wallet.getId(),
                wallet.getName(),
                wallet.getBalance(),
                wallet.getCurrency()
        )).toList();
    }
}
