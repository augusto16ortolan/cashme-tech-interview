package com.cashme.tech_project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageFilter {

    private static final String SORT_SPLIT_REGEX = " ";

    private static final int PAGE_DEFAULT = 0;
    private static final int SIZE_DEFAULT = 10;

    private int page = PAGE_DEFAULT;
    private int size = SIZE_DEFAULT;
    private String sortBy;

    public Pageable toPageable() {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return PageRequest.of(page, size, Sort.unsorted());
        }

        final var sortParams = sortBy.split(SORT_SPLIT_REGEX);
        final var sortField = sortParams[0];
        final var sortDirection = Sort.Direction.fromString(sortParams[1]);

        return PageRequest.of(page, size, Sort.by(sortDirection, sortField));
    }
}
