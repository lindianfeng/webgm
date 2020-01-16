package com.kaka.webgm.security;

import com.kaka.webgm.util.SubnetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserConfig implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(UserConfig.class);
    private final Map<String, UserInfo> users = new ConcurrentHashMap<>();
    private long configFileLastModify = 0;

    @SuppressWarnings("unchecked")
    private static UserInfo mapToUserInfo(Map<String, Object> map) {
        final String passwrd = (String) map.get("password");
        final List<String> roles = (List<String>) map.get("roles");
        final List<String> ips = (List<String>) map.get("ip");

        UserInfo user = new UserInfo();

        user.password = passwrd;

        if (null != roles) {
            for (String role : roles) {
                if (StringUtils.isEmpty(role)) {
                    continue;
                }
                user.roleSet.add(role.trim());
            }
        }

        if (null != ips) {
            for (String ip : ips) {
                if (StringUtils.isEmpty(ip)) {
                    continue;
                }
                user.ipSet.add(ip.trim());
            }
        }

        return user;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reload();
    }

    @Scheduled(fixedDelay = 30000)
    public synchronized void reload() throws FileNotFoundException {
        File configFile = new File("config/users.yml");

        if (!configFile.exists()) {
            logger.error("the config file:'{}' is not exist", configFile.toString());
            return;
        }

        long lastModifyTime = configFile.lastModified();

        if (this.configFileLastModify == lastModifyTime) {
            return;
        }

        logger.info("the config file:{} is modified,will load,lastModified:{}", configFile.toString(), lastModifyTime);
        this.users.clear();

        this.configFileLastModify = lastModifyTime;

        Yaml yaml = new Yaml();
        Map<String, Map<String, Object>> map = yaml.load(new FileInputStream(configFile));
        logger.info(yaml.dump(map));

        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            if (StringUtils.isEmpty(entry.getKey())) {
                continue;
            }

            this.users.put(entry.getKey().trim().toLowerCase(), mapToUserInfo(entry.getValue()));
        }
    }

    public synchronized UserInfo getUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        return this.users.get(username.toLowerCase());
    }

    public static class UserInfo implements Serializable {
        private static final long serialVersionUID = 1365023489054619849L;
        public final HashSet<String> roleSet = new HashSet<>();
        public final HashSet<String> ipSet = new HashSet<>();
        public String password;

        public boolean isAllowedIp(String ip) {
            if (ipSet.contains(ip)) {
                return true;
            } else {
                for (String s : ipSet) {
                    if (!s.contains("/")) {
                        continue;
                    }
                    SubnetUtils subnetUtils = new SubnetUtils(s);
                    if (subnetUtils.getInfo().isInRange(ip)) {
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean hasRole(String rolename) {
            return roleSet.contains(rolename);
        }
    }
}

