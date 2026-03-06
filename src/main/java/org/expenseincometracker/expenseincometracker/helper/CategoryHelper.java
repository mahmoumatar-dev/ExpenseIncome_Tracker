package org.expenseincometracker.expenseincometracker.helper;

import lombok.RequiredArgsConstructor;
import org.expenseincometracker.expenseincometracker.entity.Category;
import org.expenseincometracker.expenseincometracker.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHelper {

    public void validateCategoryAccess(Category category, User parent) {
        if (!category.getParent().getId().equals(parent.getId())) {
            throw new AccessDeniedException("Category does not belong to this parent");
        }
    }

}
