package com.kaka.webgm.repository;

import com.kaka.webgm.domain.GameItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameItemRepository extends JpaRepository<GameItem, Long> {
    List<GameItem> findAllByOrderByItemNameAsc();
}
