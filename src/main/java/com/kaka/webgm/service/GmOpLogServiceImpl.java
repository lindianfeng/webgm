package com.kaka.webgm.service;

import com.kaka.webgm.domain.GmOperationLog;
import com.kaka.webgm.repository.GmOperationLogRepository;
import com.kaka.webgm.util.GmUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class GmOpLogServiceImpl {
    @Autowired
    private GmOperationLogRepository logRepository;

    public void writeLog(String reqPath, String reqArg, String resData) {
        String userName = "";

        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String userIp = GmUtils.getClientIP(curRequest);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            userName = auth.getName();
        }

        GmOperationLog operationLog = new GmOperationLog(userName, userIp, reqPath, reqArg, resData);

        this.logRepository.save(operationLog);
    }

    public Page<GmOperationLog> getGmOpLogs(Pageable pageable) {
        return this.logRepository.findAll(pageable);
    }

    public Page<GmOperationLog> findAll(Integer queryType, String keyWord, Pageable pageable) {
        switch (queryType) {
            case 1: {
                return this.logRepository.findAllByOpUser(keyWord, pageable);
            }
            case 2: {
                return this.logRepository.findAllByReqPath(keyWord, pageable);
            }
            case 3: {
                return this.logRepository.findAllByOpIp(keyWord, pageable);
            }
            default: {
                return new PageImpl<>(new ArrayList<>(0));
            }
        }
    }
}
