package com.kaka.webgm.game.dao;

import java.util.Map;

public interface GameInterfaceDao {

    /**
     * @param userid 账号名称 比如:1000
     * @return code : 0成功，1超时, 其他 信息错误
     */
    CallGameInterfaceResult<Map<String, Object>> getCharListByAccount(String userid);

    /**
     * @return 0成功，1超时, 其他 信息错误
     */
    int addForbidTalk(Long playerid, int forbidTime, String desc, boolean notifytouser);

    /**
     * @param playerid
     * @return
     */
    int deleteForbidTalk(Long playerid);

    /**
     * @param playerid
     * @return
     */
    CallGameInterfaceResult<Map<String, Object>> queryForbidTalk(Long playerid);

    /**
     * playerid		:	玩家ID
     * forbidtime	:	0封禁时间为0直接踢人
     * desc			:	原因
     * notifytouser	:	提醒User信息
     */
    int addForbidLogin(Long playerid, int forbidTime, String desc, boolean notifytouser);

    /**
     * @param playerid
     * @return
     */
    int deleteForbidLogin(Long playerid);

    /**
     * @param playerid
     * @return
     */
    CallGameInterfaceResult<Map<String, Object>> queryForbidLogin(Long playerid);

    /**
     * playerid	:	玩家ID
     * stritem	:	Item ID
     * itemnum	:	数量
     * ordered	:	数字, 订单号
     * head	:	邮件标题 英文
     * context	:	邮件内容 英文
     */
    int mailItem(Long playerid, String stritem, String itemnum, String ordered, String head, String context);

    /**
     * @param playerid 角色id
     * @return 角色信息
     */
    CallGameInterfaceResult<Map<String, Object>> getCharDetailInfoById(Long playerid);

    /**
     * @param playername 角色名称
     * @return 角色信息
     */
    CallGameInterfaceResult<Map<String, Object>> getCharDetailInfoByName(String playername);


    /**
     * @param desc content
     * @return succeed or failed.
     */
    int doBull(String desc);
}
