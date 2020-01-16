package com.kaka.webgm.controller;

import com.kaka.webgm.game.dao.CallGameInterfaceResult;
import com.kaka.webgm.game.dao.GameInterfaceDao;
import com.kaka.webgm.game.dao.GameInterfaceDaoFactory;
import com.kaka.webgm.service.GameItemServiceImpl;
import com.kaka.webgm.service.GameServerServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/gm")
public class GameGmController {
    @Autowired
    private GameItemServiceImpl gameItemService;

    @Autowired
    private GameServerServiceImpl gameServerService;

    @Autowired
    private GameInterfaceDaoFactory gameInterfaceDaoFactory;

    @RequestMapping(path = "/account/index")
    public String toAccountIndexView(Model model) {
        model.addAttribute("all_servers", this.gameServerService.getListAll());
        return "gm/gm-account-query";
    }

    @RequestMapping(path = "/account/query")
    public String queryAccount(Model model, @RequestParam Integer serverId, @RequestParam String account) {

        model.addAttribute("all_servers", this.gameServerService.getListAll());

        if (null == serverId || null == account) {
            return "gm/gm-account-query";
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            model.addAttribute("error", "server id:" + serverId);
            return "gm/gm-account-query";
        }

        model.addAttribute("serverId", serverId);
        model.addAttribute("server", this.gameServerService.getMapAll().get(serverId));
        model.addAttribute("account", account);
        try {
            CallGameInterfaceResult<Map<String, Object>> r = gameInterfaceDao.getCharListByAccount(account);
            model.addAttribute("code", r.code);
            model.addAttribute("data", r.data);

            if (r.code != 0 || null == r.data || r.data.size() == 0) {
                model.addAttribute("error", "没有找到数据或者发生错误,游戏服务器返回码为:" + r.code);
                return "gm/gm-account-query";
            }
            return "gm/gm-account-info";
        } catch (Exception e) {
            model.addAttribute("error", "server id:" + serverId + ",error:" + e.getMessage());
            return "gm/gm-account-query";
        }
    }

    @RequestMapping(path = "/char/index")
    public String toCharIndexView(@RequestParam(required = false) String account, Model model) {
        if (null != account) {
            model.addAttribute("account", account);
        }
        model.addAttribute("all_servers", this.gameServerService.getListAll());
        return "gm/gm-char-query";
    }

    @RequestMapping(path = "/char/query")
    public String queryChar(Model model, @RequestParam(required = false) String account,
                            @RequestParam Integer serverId,
                            @RequestParam(required = false) Long roleId,
                            @RequestParam(required = false) String roleName
    ) {
        if (null != account) {
            model.addAttribute("account", account);
        }

        if (null == serverId) {
            return "gm/gm-char-query";
        }

        model.addAttribute("all_servers", this.gameServerService.getListAll());
        model.addAttribute("serverId", serverId);

        if (null == roleId && StringUtils.isEmpty(roleName)) {
            model.addAttribute("error", "角色ID和名称不能同时为空！");
            return "gm/gm-char-query";
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            model.addAttribute("error", "server id:" + serverId);
            return "gm/gm-char-query";
        }

        model.addAttribute("server", this.gameServerService.getMapAll().get(serverId));
        try {
            CallGameInterfaceResult<Map<String, Object>> r = null;

            if (null != roleId) {
                r = gameInterfaceDao.getCharDetailInfoById(roleId);
            } else if (StringUtils.isNotEmpty(roleName)) {
                r = gameInterfaceDao.getCharDetailInfoByName(roleName);
            }

            if (r.code != 0 || null == r.data || r.data.size() == 0) {
                model.addAttribute("error", "没有找到数据或者发生错误,游戏服务器返回码为:" + r.code);
                return "gm/gm-char-query";
            }

            final String lastLoginTime = (String) r.data.get("lastlogintime");
            final String createTime = (String) r.data.get("createtime");
            final String deleteTime = (String) r.data.get("deletetime");

            if (StringUtils.isNumeric(lastLoginTime) && lastLoginTime.length() > 4) {
                r.data.put("lastlogintime", DateFormatUtils.format(new Date(Long.parseLong(lastLoginTime) * 1000L), "yyyy-MM-dd HH:mm:ss"));
            } else {
                r.data.put("lastlogintime", "");
            }

            if (StringUtils.isNumeric(createTime) && createTime.length() > 4) {
                r.data.put("createtime", DateFormatUtils.format(new Date(Long.parseLong(lastLoginTime) * 1000L), "yyyy-MM-dd HH:mm:ss"));
            } else {
                r.data.put("createtime", "");
            }

            if (StringUtils.isNumeric(deleteTime) && deleteTime.length() > 4) {
                r.data.put("deletetime", DateFormatUtils.format(new Date(Long.parseLong(lastLoginTime) * 1000L), "yyyy-MM-dd HH:mm:ss"));
            } else {
                r.data.put("deletetime", "");
            }

            model.addAttribute("code", r.code);
            model.addAttribute("data", r.data);

        } catch (Exception e) {
            model.addAttribute("error", "server id:" + serverId + ",error:" + e.getMessage());
            return "gm/gm-char-query";
        }
        return "gm/gm-char-info";
    }

    @RequestMapping(path = "/forbid_talk/edit")
    public String toAddForbidTalkView(@RequestParam(required = false) Integer serverId, @RequestParam(required = false) Long roleId, Model model) {
        if (null != serverId) {
            model.addAttribute("serverId", serverId);
        }

        if (null != roleId) {
            model.addAttribute("roleId", roleId);
        }

        model.addAttribute("all_servers", this.gameServerService.getListAll());
        return "gm/gm-char-forbid-talk";
    }

    @RequestMapping(path = "/forbid_talk/add")
    @ResponseBody
    public Map<String, Object> doAddForbidTalk(@RequestParam Integer serverId,
                                               @RequestParam Long roleId,
                                               @RequestParam Integer forbidTimes,
                                               @RequestParam String forbidDesc,
                                               @RequestParam(required = false) Boolean notifyToUser,
                                               Model model) {
        Map<String, Object> result = new HashMap<>(2);
        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }
        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        if (StringUtils.isEmpty(forbidDesc)) {
            forbidDesc = StringUtils.EMPTY;
        }

        if (null == notifyToUser) {
            notifyToUser = Boolean.FALSE;
        }

        try {
            int code = gameInterfaceDao.addForbidTalk(roleId, forbidTimes, forbidDesc, notifyToUser);
            result.put("code", code);

            if (0 == code) {
                result.put("error", "禁言 Succeed!");
            } else {
                result.put("error", "禁言 Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/forbid_talk/delete")
    @ResponseBody
    public Map<String, Object> doDeleteForbidTalk(@RequestParam Integer serverId, @RequestParam Long roleId) {

        Map<String, Object> result = new HashMap<>(2);

        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        try {
            int code = gameInterfaceDao.deleteForbidTalk(roleId);
            result.put("code", code);

            if (0 == code) {
                result.put("error", "解除禁言 Succeed!");
            } else {
                result.put("error", "解除禁言 Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/forbid_talk/query")
    @ResponseBody
    public Map<String, Object> doQueryForbidTalk(@RequestParam Integer serverId, @RequestParam Long roleId) {
        Map<String, Object> result = new HashMap<>(2);

        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        try {
            CallGameInterfaceResult<Map<String, Object>> r = gameInterfaceDao.queryForbidTalk(roleId);
            result.put("code", r.code);

            if (0 == r.code) {
                if (null != r.data && r.data.size() > 0) {
                    final String beginTime = (String) r.data.get("begintime");

                    if (StringUtils.isNumeric(beginTime) && beginTime.length() > 4) {
                        result.put("forbid_begintime", DateFormatUtils.format(new Date(Long.parseLong(beginTime) * 1000L), "yyyy-MM-dd HH:mm:ss"));
                    } else {
                        result.put("forbid_begintime", "");
                    }

                    result.put("forbid_lasttime", r.data.get("lasttime"));
                    result.put("forbid_desc", r.data.get("desc"));
                    result.put("error", "查询禁言 Succeed!");
                } else {
                    result.put("error", "查询禁言 Succeed,返回数据为空!");
                }
            } else if (-3 == r.code) {
                result.put("error", "查询禁言 没有查询到数据!");
            } else {
                result.put("error", "查询禁言 Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/forbid_login/edit")
    public String toAddForbidLoginView(@RequestParam(required = false) Integer serverId, @RequestParam(required = false) Long roleId, Model model) {
        if (null != serverId) {
            model.addAttribute("serverId", serverId);
        }

        if (null != roleId) {
            model.addAttribute("roleId", roleId);
        }

        model.addAttribute("all_servers", this.gameServerService.getListAll());
        return "gm/gm-char-forbid-login";
    }

    @RequestMapping(path = "/forbid_login/add")
    @ResponseBody
    public Map<String, Object> doAddForbidLogin(@RequestParam Integer serverId,
                                                @RequestParam Long roleId,
                                                @RequestParam Integer forbidTimes,
                                                @RequestParam String forbidDesc,
                                                @RequestParam(required = false) Boolean notifyToUser,
                                                Model model) {
        Map<String, Object> result = new HashMap<>(3);

        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        if (StringUtils.isEmpty(forbidDesc)) {
            forbidDesc = StringUtils.EMPTY;
        }
        if (null == notifyToUser) {
            notifyToUser = Boolean.FALSE;
        }
        try {
            int code = gameInterfaceDao.addForbidLogin(roleId, forbidTimes, forbidDesc, notifyToUser);
            result.put("code", code);

            if (0 == code) {
                result.put("error", "封禁 Succeed!");
            } else {
                result.put("error", "封禁 Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/forbid_login/delete")
    @ResponseBody
    public Map<String, Object> doDeleteForbidLogin(@RequestParam Integer serverId, @RequestParam Long roleId) {
        Map<String, Object> result = new HashMap<>(2);
        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        try {
            int code = gameInterfaceDao.deleteForbidLogin(roleId);
            result.put("code", code);

            if (0 == code) {
                result.put("error", "解除封禁 Succeed!");
            } else {
                result.put("error", "解除封禁Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/forbid_login/query")
    @ResponseBody
    public Map<String, Object> doQueryForbidLogin(@RequestParam Integer serverId, @RequestParam Long roleId) {
        Map<String, Object> result = new HashMap<>(2);

        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        try {
            CallGameInterfaceResult<Map<String, Object>> r = gameInterfaceDao.queryForbidLogin(roleId);
            result.put("code", r.code);

            if (0 == r.code) {
                if (null != r.data && r.data.size() > 0) {
                    final String beginTime = (String) r.data.get("begintime");

                    if (StringUtils.isNumeric(beginTime) && beginTime.length() > 4) {
                        result.put("forbid_begintime", DateFormatUtils.format(new Date(Long.parseLong(beginTime) * 1000L), "yyyy-MM-dd HH:mm:ss"));
                    } else {
                        result.put("forbid_begintime", "");
                    }

                    result.put("forbid_lasttime", r.data.get("lasttime"));
                    result.put("forbid_desc", r.data.get("desc"));
                    result.put("error", "查询封禁 Succeed!");
                } else {
                    result.put("error", "查询封禁 Succeed,返回数据为空!");
                }
            } else if (-3 == r.code) {
                result.put("error", "查询封禁 没有查询到数据!");
            } else {
                result.put("error", "查询封禁 Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/kick_out")
    @ResponseBody
    public Map<String, Object> kickOutPlayer(@RequestParam Integer serverId, @RequestParam Long roleId) {
        Map<String, Object> result = new HashMap<>(3);

        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        try {
            int code = gameInterfaceDao.addForbidLogin(roleId, 0, "kick out", false);
            result.put("code", code);

            if (0 == code) {
                result.put("error", "踢人 Succeed!");
            } else {
                result.put("error", "踢人Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }

    @RequestMapping(path = "/item_mail/edit")
    public String toMailItemView(@RequestParam(required = false) Integer serverId, @RequestParam(required = false) Long roleId, Model model) {
        if (null != serverId) {
            model.addAttribute("serverId", serverId);
        }

        if (null != roleId) {
            model.addAttribute("roleId", roleId);
        }

        model.addAttribute("all_servers", this.gameServerService.getListAll());
        model.addAttribute("all_items", this.gameItemService.getAllList());
        return "gm/gm-item-mail";
    }

    @RequestMapping(path = "/item_mail/send")
    @ResponseBody
    public Map<String, Object> doMailItem(@RequestParam Integer serverId,
                                          @RequestParam Long roleId,
                                          @RequestParam Integer itemId,
                                          @RequestParam Integer itemCount,
                                          @RequestParam String title,
                                          @RequestParam String content
    ) {
        Map<String, Object> result = new HashMap<>(3);

        if (null == serverId) {
            result.put("code", -200);
            result.put("error", "服务器必须选择！");
            return result;
        }

        if (null == roleId) {
            result.put("code", -300);
            result.put("error", "角色ID必须填写！");
            return result;
        }

        GameInterfaceDao gameInterfaceDao = gameInterfaceDaoFactory.getGameInterfaceDao(serverId);

        if (null == gameInterfaceDao) {
            result.put("code", -100);
            result.put("error", "服务器不存在！");
            return result;
        }

        try {
            int code = gameInterfaceDao.mailItem(roleId, Integer.toString(itemId), Integer.toString(itemCount), Long.toString(System.currentTimeMillis()), title, content);
            result.put("code", code);

            if (0 == code) {
                result.put("error", "发道具邮件 Succeed!");
            } else {
                result.put("error", "发道具邮件 Failed!");
            }
            return result;
        } catch (Exception e) {
            result.put("code", -500);
            result.put("error", "系统错误：" + e.getMessage() + "！");
            return result;
        }
    }
}
