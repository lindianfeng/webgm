package com.kaka.webgm.controller;

import com.kaka.webgm.domain.GameServer;
import com.kaka.webgm.service.GameServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path = "/admin/server")
public class GameServerController {

    @Autowired
    private GameServerServiceImpl gameServerService;

    @RequestMapping(path = "/index")
    public String toListView(Model model) {
        List<GameServer> gameServers = this.gameServerService.getListAll();
        model.addAttribute("game_server_list", gameServers);
        return "admin/server/game-server-list";
    }

    @RequestMapping(path = "/edit")
    public String toEditView(@RequestParam(required = false) Long id, Model model) {
        GameServer server = null;
        if (null != id) {
            server = this.gameServerService.get(id);
        }

        if (null == server) {
            server = new GameServer();
        }

        model.addAttribute("server", server);

        return "admin/server/game-server-edit";
    }

    @RequestMapping(path = "/save")
    public String save(@ModelAttribute GameServer gameserver, Model model) {
        this.gameServerService.save(gameserver);
        return "redirect:/admin/server/index";
    }

    @RequestMapping(path = "/delete")
    public String delete(@RequestParam Long id, Model model) {
        this.gameServerService.delete(id);
        return "redirect:/admin/server/index";
    }
}
