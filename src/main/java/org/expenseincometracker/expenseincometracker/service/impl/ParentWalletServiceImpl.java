package org.expenseincometracker.expenseincometracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.dto.request.CreateWalletRequest;
import org.expenseincometracker.expenseincometracker.dto.request.UpdateWalletRequest;
import org.expenseincometracker.expenseincometracker.dto.response.WalletResponse;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.expenseincometracker.expenseincometracker.entity.Wallet;
import org.expenseincometracker.expenseincometracker.enums.Role;
import org.expenseincometracker.expenseincometracker.exception.BusinessException;
import org.expenseincometracker.expenseincometracker.exception.NotFoundException;
import org.expenseincometracker.expenseincometracker.helper.UserHelper;
import org.expenseincometracker.expenseincometracker.helper.WalletHelper;
import org.expenseincometracker.expenseincometracker.repository.UserRepository;
import org.expenseincometracker.expenseincometracker.repository.WalletRepository;
import org.expenseincometracker.expenseincometracker.repository.TransactionRepository;
import org.expenseincometracker.expenseincometracker.service.ParentWalletService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentWalletServiceImpl implements ParentWalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserHelper userHelper;
    private final WalletHelper walletHelper;

    @Override
    @Transactional
    public WalletResponse createWallet(CreateWalletRequest request, Authentication authentication) {

        User parent = userHelper.getAuthenticatedUser(authentication);

        List<User> children = walletHelper.resolveAndValidateChildren(request.childrenIds(), parent);

        Wallet wallet = Wallet.builder()
                .name(request.name())
                .currency(request.currency())
                .balance(request.balance())
                .owner(parent)
                .assignedChildren(children)
                .build();

        Wallet saved = walletRepository.save(wallet);
        return toWalletResponse(saved);
    }

    @Override
    @Transactional
    public WalletResponse updateWallet(Long walletId, UpdateWalletRequest request, Authentication authentication) {

        User parent = userHelper.getAuthenticatedUser(authentication);

        Wallet wallet = walletRepository.findByIdAndOwnerId(walletId, parent.getId())
                .orElseThrow(() -> new NotFoundException("Wallet not found or does not belong to you"));

        if (request.name() != null) {
            if (request.name().isBlank()) {
                throw new BusinessException("Wallet name cannot be blank");
            }
            wallet.setName(request.name());
        }

        if (request.currency() != null) {
            wallet.setCurrency(request.currency());
        }

        if (request.balance() != null) {
            wallet.setBalance(request.balance());
        }

        if (request.childrenIds() != null) {
            List<User> children = walletHelper.resolveAndValidateChildren(request.childrenIds(), parent);
            wallet.setAssignedChildren(children);
        }

        Wallet updated = walletRepository.save(wallet);
        return toWalletResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WalletResponse> getParentWallets(Authentication authentication) {

        User parent = userHelper.getAuthenticatedUser(authentication);

        List<Wallet> wallets = walletRepository.findAllByOwnerId(parent.getId());

        return wallets.stream()
                .map(this::toWalletResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteWallet(Long walletId, Authentication authentication) {

        User parent = userHelper.getAuthenticatedUser(authentication);

        Wallet wallet = walletRepository.findByIdAndOwnerId(walletId, parent.getId())
                .orElseThrow(() -> new NotFoundException("Wallet not found or does not belong to you"));

        if (transactionRepository.existsByWalletId(walletId)) {
            throw new BusinessException("Cannot delete a wallet that has financial transaction history.");
        }

        walletRepository.delete(wallet);
    }

    private WalletResponse toWalletResponse(Wallet wallet) {
        List<WalletResponse.ChildSummary> childSummaries = wallet.getAssignedChildren()
                .stream()
                .map(child -> new WalletResponse.ChildSummary(child.getId(), child.getName()))
                .toList();

        return new WalletResponse(
                wallet.getId(),
                wallet.getName(),
                wallet.getCurrency(),
                wallet.getBalance(),
                childSummaries,
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}
