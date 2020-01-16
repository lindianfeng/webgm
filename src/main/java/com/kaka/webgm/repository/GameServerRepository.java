package com.kaka.webgm.repository;

import com.kaka.webgm.domain.GameServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameServerRepository extends JpaRepository<GameServer, Long> {
    List<GameServer> findAllByOrderByServerNameAsc();
}
