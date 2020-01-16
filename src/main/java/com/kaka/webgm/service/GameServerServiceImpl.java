package com.kaka.webgm.service;

import com.kaka.webgm.domain.GameServer;
import com.kaka.webgm.repository.GameServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServerServiceImpl {
    public static final String ALL_SERVERS_CACHE_NAME = "game_server_cache";
    @Autowired
    private GameServerRepository gameServerRepository;

    @Cacheable(value = ALL_SERVERS_CACHE_NAME, key = "'list_servers'")
    public List<GameServer> getListAll() {
        return this.gameServerRepository.findAllByOrderByServerNameAsc();
    }

    @Cacheable(value = ALL_SERVERS_CACHE_NAME, key = "'map_servers'")
    public Map<Integer, GameServer> getMapAll() {
        List<GameServer> list = this.getListAll();
        Map<Integer, GameServer> map = new HashMap<>(list.size());
        for (GameServer server : list) {
            map.put(server.getServerId(), server);
        }

        return map;
    }

    @CacheEvict(value = ALL_SERVERS_CACHE_NAME, allEntries = true)
    public void save(GameServer server) {
        this.gameServerRepository.save(server);
    }

    public GameServer get(Long id) {
        return this.gameServerRepository.getOne(id);
    }

    @CacheEvict(value = ALL_SERVERS_CACHE_NAME, allEntries = true)
    public void delete(Long id) {
        this.gameServerRepository.delete(id);
    }
}
