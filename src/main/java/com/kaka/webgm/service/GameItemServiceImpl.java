package com.kaka.webgm.service;

import com.kaka.webgm.domain.GameItem;
import com.kaka.webgm.repository.GameItemRepository;
import com.kaka.webgm.util.PinyinUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameItemServiceImpl {
    private static final String GAME_ITEM_CACHE_NAME = "game_item_cache";
    private static Logger logger = LoggerFactory.getLogger(GameItemServiceImpl.class);
    @Autowired
    private GameItemRepository itemRepository;

    @Cacheable(value = GAME_ITEM_CACHE_NAME, key = "'all_items_list'")
    public List<GameItem> getAllList() {
        return this.itemRepository.findAllByOrderByItemNameAsc();
    }

    @Cacheable(value = GAME_ITEM_CACHE_NAME, key = "'all_items_map'")
    public Map<Long, GameItem> getAllMap() {
        List<GameItem> gameItemList = this.getAllList();
        if (null == gameItemList || gameItemList.isEmpty()) {
            return new HashMap<>(0);
        }

        Map<Long, GameItem> itemMap = new HashMap<>(gameItemList.size());

        for (GameItem item : gameItemList) {
            itemMap.put(item.getId(), item);
        }

        return itemMap;
    }

    @Cacheable(value = GAME_ITEM_CACHE_NAME, key = "'items_list_by_first_spell_'+#firstSpell")
    public List<GameItem> getItemByFirstSpell(String firstSpell) {
        Map<String, List<GameItem>> map = this.getAllItemAlphaMap();
        if (map.containsKey(firstSpell)) {
            return map.get(firstSpell);
        }

        return new ArrayList<>(0);
    }

    @Cacheable(value = GAME_ITEM_CACHE_NAME, key = "'items_list_by_item_id_'+#itemIdPart")
    public List<GameItem> getItemByItemIdString(String itemIdPart) {
        Map<String, List<GameItem>> map = this.getAllItemSplitByIdMap();
        if (map.containsKey(itemIdPart)) {
            return map.get(itemIdPart);
        }

        return new ArrayList<>(0);
    }

    @Cacheable(value = GAME_ITEM_CACHE_NAME, key = "'all_items_map_by_first_spell'")
    public Map<String, List<GameItem>> getAllItemAlphaMap() {
        Map<String, List<GameItem>> itemMap = new HashMap<>();
        List<GameItem> gameItemList = this.getAllList();

        if (null == gameItemList || gameItemList.isEmpty()) {
            return new HashMap<>(0);
        }

        for (GameItem item : gameItemList) {
            String pinyin = PinyinUtils.getAlpha(item.getItemName());
            if (StringUtils.isEmpty(pinyin)) {
                continue;
            }

            String key = "";
            for (char c : pinyin.toUpperCase().toCharArray()) {
                key += c;
                if (itemMap.containsKey(key)) {
                    List<GameItem> list = itemMap.get(key);
                    list.add(item);
                } else {
                    List<GameItem> list = new ArrayList<>();
                    itemMap.put(key, list);
                    list.add(item);
                }
            }
        }

        return itemMap;
    }

    @Cacheable(value = GAME_ITEM_CACHE_NAME, key = "'all_items_map_by_item_id'")
    public Map<String, List<GameItem>> getAllItemSplitByIdMap() {
        Map<String, List<GameItem>> itemMap = new HashMap<>();
        List<GameItem> gameItemList = this.getAllList();

        if (null == gameItemList || gameItemList.isEmpty()) {
            return new HashMap<>(0);
        }
        for (GameItem item : gameItemList) {
            if (null == item.getItemId()) {
                continue;
            }

            String itemIdString = Long.toString(item.getItemId());
            String key = "";
            for (char c : itemIdString.toCharArray()) {
                key += c;
                if (itemMap.containsKey(key)) {
                    List<GameItem> list = itemMap.get(key);
                    list.add(item);
                } else {
                    List<GameItem> list = new ArrayList<>();
                    itemMap.put(key, list);
                    list.add(item);
                }
            }
        }
        return itemMap;
    }

    @CacheEvict(value = GAME_ITEM_CACHE_NAME, allEntries = true)
    public void save(GameItem gameItem) {
        itemRepository.save(gameItem);
    }

    public GameItem get(Long id) {
        return itemRepository.getOne(id);
    }

    @CacheEvict(value = GAME_ITEM_CACHE_NAME, allEntries = true)
    public void delete(Long id) {
        itemRepository.delete(id);
    }
}
