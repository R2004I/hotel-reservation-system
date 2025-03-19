package com.ritam.Hotel.Reservation.System.helper;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PaginationUtil<T> {

    public Page<T> paginate(List<T> list, Pageable pageable){
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(),list.size());

        List<T> sublist = new ArrayList<>();

        if (start < list.size()) {
            sublist = list.subList(start, end);
        }

        return new PageImpl<>(sublist, pageable, list.size());
    }
}
