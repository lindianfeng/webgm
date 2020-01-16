package com.kaka.webgm.repository;

import com.kaka.webgm.domain.GmOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GmOperationLogRepository extends PagingAndSortingRepository<GmOperationLog, Long> {
    Page<GmOperationLog> findAllByOpUser(String opUser, Pageable pageable);

    Page<GmOperationLog> findAllByReqPath(String reqPath, Pageable pageable);

    Page<GmOperationLog> findAllByOpIp(String opIp, Pageable pageable);
}
