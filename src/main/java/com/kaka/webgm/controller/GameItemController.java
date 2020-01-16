package com.kaka.webgm.controller;

import com.kaka.webgm.domain.GameItem;
import com.kaka.webgm.service.GameItemServiceImpl;
import com.kaka.webgm.util.PinyinUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/admin/item")
public class GameItemController {

    @Autowired
    private GameItemServiceImpl gameItemService;

    @RequestMapping(path = "/index")
    public String toListView(Model model) {
        List<GameItem> gameItemList = this.gameItemService.getAllList();
        model.addAttribute("game_item_list", gameItemList);
        return "admin/item/game-item-list";
    }

    @RequestMapping(path = "/items")
    @ResponseBody
    public List<GameItem> getItems() {
        return this.gameItemService.getAllList();
    }

    @RequestMapping(path = "/search")
    @ResponseBody
    public List<GameItem> searchItem(@RequestParam(required = false) String itemKeyword) {
        if (StringUtils.isNotEmpty(itemKeyword) && StringUtils.isNumeric(itemKeyword)) {
            return this.gameItemService.getItemByItemIdString(itemKeyword);
        } else {
            return new ArrayList<>(0);
        }
    }

    @RequestMapping(path = "/edit")
    public String toEditView(@RequestParam(required = false) Long id, Model model) {
        GameItem gameItem = null;
        if (null != id) {
            gameItem = this.gameItemService.get(id);
        }

        if (null == gameItem) {
            gameItem = new GameItem();
        }

        model.addAttribute("item", gameItem);

        return "admin/item/game-item-edit";
    }

    @RequestMapping(path = "/save")
    public String save(@ModelAttribute GameItem gameItem, Model model) {
        this.gameItemService.save(gameItem);
        return "redirect:/admin/item/index";
    }

    @RequestMapping(path = "/delete")
    public String delete(@RequestParam Long id, Model model) {
        this.gameItemService.delete(id);
        return "redirect:/admin/item/index";
    }
}
