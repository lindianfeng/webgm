package com.kaka.webgm.security;

import com.kaka.webgm.util.GmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class IpUserDetailsChecker implements UserDetailsChecker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserConfig userConfig;

    @Override
    public void check(UserDetails toCheck) {
        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = GmUtils.getClientIP(curRequest);
        logger.info("user:{} ip is {}.", toCheck.getUsername(), ip);

        UserConfig.UserInfo userInfo = userConfig.getUser(toCheck.getUsername());

        if (null == userInfo) {
            throw new UsernameNotFoundException(toCheck.getUsername());
        }

        try {
            if (!userInfo.isAllowedIp(ip)) {
                logger.error("user:{} ip:{},not allowed to login.", toCheck.getUsername(), ip);
                throw new UsernameNotFoundException("username:" + toCheck.getUsername() + ",ip:" + ip);
            }
        } catch (IllegalArgumentException e) {
            logger.error("user:{} ip config invalid.", toCheck.getUsername());
            throw new UsernameNotFoundException("username:" + toCheck.getUsername());
        }
    }
}
