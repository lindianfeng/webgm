package com.kaka.webgm.game.dao;

import com.kaka.webgm.domain.GameServer;
import com.kaka.webgm.service.GameServerServiceImpl;
import com.kaka.webgm.service.GmOpLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameInterfaceDaoFactory {
    private final Map<Integer, GameInterfaceDao> gameInterfaceDaoMap = new ConcurrentHashMap<>(100);

    @Autowired
    private GameServerServiceImpl gameServerService;

    @Autowired
    private GmOpLogServiceImpl opLogService;

    public synchronized GameInterfaceDao getGameInterfaceDao(int id) {

        if (!this.gameServerService.getMapAll().containsKey(id)) {
            return null;
        }

        if (gameInterfaceDaoMap.containsKey(id)) {
            return gameInterfaceDaoMap.get(id);
        }
        GameServer server = this.gameServerService.getMapAll().get(id);

        GameInterfaceDao gameInterfaceDao = new GameInterfaceHttpDaoImpl(server.getServerHost(), server.getServerPort(), this.opLogService);

        gameInterfaceDaoMap.put(id, gameInterfaceDao);

        return gameInterfaceDaoMap.get(id);
    }
}
