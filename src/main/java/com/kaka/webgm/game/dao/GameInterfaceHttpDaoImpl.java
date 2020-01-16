package com.kaka.webgm.game.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaka.webgm.service.GmOpLogServiceImpl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public class GameInterfaceHttpDaoImpl implements GameInterfaceDao {

    private static final Logger logger = LoggerFactory.getLogger(GameInterfaceHttpDaoImpl.class);

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String URI_GET_CHAR_LIST = "web/get_acccharlist";
    private static final String URI_ADD_FORBID_TALK = "web/add_forbidtalk";
    private static final String URI_DELETE_FORBID_TALK = "web/del_forbidtalk";
    private static final String URI_QUERY_FORBID_TALK = "web/find_forbidtalk";
    private static final String URI_ADD_FORBID_LOGIN = "web/add_forbidlogin";
    private static final String URI_DELETE_FORBID_LOGIN = "web/del_forbidlogin";
    private static final String URI_QUERY_FORBID_LOGIN = "web/find_forbidlogin";
    private static final String URI_MAIL_ITEM = "web/mail_item";
    private static final String URI_GET_CHAR_DETAIL_INFO_BY_ID = "web/get_char";
    private static final String URI_GET_CHAR_DETAIL_INFO_BY_NAME = "web/get_char";
    private static final String URI_DO_BULL = "web/do_bull";

    static private final Set<String> needWriteLogPathSet = new HashSet<>(10);

    static {
        needWriteLogPathSet.add(URI_ADD_FORBID_TALK);
        needWriteLogPathSet.add(URI_DELETE_FORBID_TALK);
        needWriteLogPathSet.add(URI_ADD_FORBID_LOGIN);
        needWriteLogPathSet.add(URI_DELETE_FORBID_LOGIN);
        needWriteLogPathSet.add(URI_MAIL_ITEM);
    }

    private String gameServerHost;
    private Integer port;

    private GmOpLogServiceImpl opLogService;

    public GameInterfaceHttpDaoImpl(String gameServerHost, Integer port, GmOpLogServiceImpl opLogService) {
        this.gameServerHost = gameServerHost;
        this.port = port;
        this.opLogService = opLogService;
    }

    /**
     * @param userid 账号名称 比如:1000
     * @return code : 0成功，1超时, 其他 信息错误
     */
    @Override
    public CallGameInterfaceResult<Map<String, Object>> getCharListByAccount(String userid) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("userid", userid);

        try {
            String response = this.doHttpGetRequest(URI_GET_CHAR_LIST, paramMap);
            response = response.replace("[", "_");
            response = response.replace("]", "_");

            CallGameInterfaceResult<Map> r = this.decodeHttpResponse(response);

            if (0 != r.code) {
                return new CallGameInterfaceResult<>(r.code, null);
            }

            return new CallGameInterfaceResult<Map<String, Object>>(r.code, r.data);
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param playerid
     * @param forbidTime
     * @param desc
     * @param notifytouser
     * @return 0成功，1超时, 其他 信息错误
     */
    @Override
    public int addForbidTalk(Long playerid, int forbidTime, String desc, boolean notifytouser) {
        Map<String, String> paramMap = new LinkedHashMap<>(4);
        paramMap.put("playerid", Long.toString(playerid));
        paramMap.put("forbidtime", Integer.toString(forbidTime));
        paramMap.put("desc", desc);
        paramMap.put("notifytouser", notifytouser ? "1" : "0");

        try {
            final String response = this.doHttpGetRequest(URI_ADD_FORBID_TALK, paramMap);
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
        return 0;
    }

    /**
     * @param playerid
     * @return
     */
    @Override
    public int deleteForbidTalk(Long playerid) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("playerid", Long.toString(playerid));

        try {
            final String response = this.doHttpGetRequest(URI_DELETE_FORBID_TALK, paramMap);
            CallGameInterfaceResult<Map> result = this.decodeHttpResponse(response);
            return result.code;
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param playerid
     * @return
     */
    @Override
    public CallGameInterfaceResult<Map<String, Object>> queryForbidTalk(Long playerid) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("playerid", Long.toString(playerid));

        try {
            final String response = this.doHttpGetRequest(URI_QUERY_FORBID_TALK, paramMap);
            CallGameInterfaceResult<Map> r = this.decodeHttpResponse(response);
            return new CallGameInterfaceResult<Map<String, Object>>(r.code, r.data);
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * playerid		:	玩家ID
     * forbidtime	:	0封禁时间为0直接踢人
     * desc			:	原因
     * notifytouser	:	提醒User信息
     *
     * @param playerid
     * @param forbidTime
     * @param desc
     * @param notifytouser
     */
    @Override
    public int addForbidLogin(Long playerid, int forbidTime, String desc, boolean notifytouser) {
        Map<String, String> paramMap = new LinkedHashMap<>(4);
        paramMap.put("playerid", Long.toString(playerid));
        paramMap.put("forbidtime", Integer.toString(forbidTime));
        paramMap.put("desc", desc);
        paramMap.put("notifytouser", notifytouser ? "1" : "0");

        try {
            final String response = this.doHttpGetRequest(URI_ADD_FORBID_LOGIN, paramMap);
            CallGameInterfaceResult<Map> result = this.decodeHttpResponse(response);
            return result.code;
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param playerid
     * @return
     */
    @Override
    public int deleteForbidLogin(Long playerid) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("playerid", Long.toString(playerid));

        try {
            final String response = this.doHttpGetRequest(URI_DELETE_FORBID_LOGIN, paramMap);
            CallGameInterfaceResult<Map> result = this.decodeHttpResponse(response);
            return result.code;
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param playerid
     * @return
     */
    @Override
    public CallGameInterfaceResult<Map<String, Object>> queryForbidLogin(Long playerid) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("playerid", Long.toString(playerid));
        try {
            final String response = this.doHttpGetRequest(URI_QUERY_FORBID_LOGIN, paramMap);
            CallGameInterfaceResult<Map> r = this.decodeHttpResponse(response);

            if (0 != r.code) {
                return new CallGameInterfaceResult<>(r.code, new HashMap<>(0));
            }

            return new CallGameInterfaceResult<Map<String, Object>>(r.code, r.data);
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }


    /**
     * playerid	:	玩家ID
     * stritem	:	Item ID
     * itemnum	:	数量
     * ordered	:	数字, 订单号
     * head	:	邮件标题 英文
     * context	:	邮件内容 英文
     *
     * @param playerid
     * @param stritem
     * @param itemnum
     * @param ordered
     * @param head
     * @param context
     */
    @Override
    public int mailItem(Long playerid, String stritem, String itemnum, String ordered, String head, String context) {
        Map<String, String> paramMap = new LinkedHashMap<>(6);
        paramMap.put("playerid", Long.toString(playerid));
        paramMap.put("stritem", stritem);
        paramMap.put("itemnum", itemnum);
        paramMap.put("ordered", ordered);
        paramMap.put("head", head);
        paramMap.put("context", context);

        try {
            final String response = this.doHttpGetRequest(URI_MAIL_ITEM, paramMap);
            CallGameInterfaceResult<Map> result = this.decodeHttpResponse(response);
            return result.code;
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param playerid 角色id
     * @return 角色信息
     */
    @Override
    public CallGameInterfaceResult<Map<String, Object>> getCharDetailInfoById(Long playerid) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("playerid", Long.toString(playerid));

        try {
            final String response = this.doHttpGetRequest(URI_GET_CHAR_DETAIL_INFO_BY_ID, paramMap);
            CallGameInterfaceResult<Map> r = this.decodeHttpResponse(response);

            if (0 != r.code) {
                return new CallGameInterfaceResult<>(r.code, null);
            }
            return new CallGameInterfaceResult<Map<String, Object>>(r.code, r.data);
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param playername 角色名称
     * @return 角色信息
     */
    @Override
    public CallGameInterfaceResult<Map<String, Object>> getCharDetailInfoByName(String playername) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("playername", playername);

        try {
            final String response = this.doHttpGetRequest(URI_GET_CHAR_DETAIL_INFO_BY_NAME, paramMap);
            return new CallGameInterfaceResult<>(0, null);
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    /**
     * @param desc content
     * @return succeed or failed.
     */
    @Override
    public int doBull(String desc) {
        Map<String, String> paramMap = new LinkedHashMap<>(1);
        paramMap.put("desc", desc);

        try {
            final String response = this.doHttpGetRequest(URI_DO_BULL, paramMap);
            CallGameInterfaceResult<Map> result = this.decodeHttpResponse(response);
            return result.code;
        } catch (IOException e) {
            throw new GameInterfaceDaoException(e);
        }
    }

    private String doHttpGetRequest(String pathSegment, Map<String, String> paramMap) throws IOException {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder();

        urlBuilder.scheme("http").host(this.gameServerHost).port(this.port).addPathSegments(pathSegment);

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        HttpUrl url = urlBuilder.build();

        logger.debug("the http request is:{}", url.toString());

        Request request = new Request.Builder().url(url).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            final String result = response.body().string();
            logger.debug("the http response is:{}", result);
            if (needWriteLogPathSet.contains(pathSegment)) {
                this.opLogService.writeLog(pathSegment, paramMap.toString(), result);
            }
            return result;
        }
    }

    private CallGameInterfaceResult<Map> decodeHttpResponse(String response) {
        if (StringUtils.isEmpty(response)) {
            return new CallGameInterfaceResult<>(-100, new HashMap(0));
        }
        try {
            Map map = mapper.readValue(response, Map.class);

            return new CallGameInterfaceResult<>(this.getResultCode(map), map);
        } catch (IOException e) {
            logger.error("decode the http response error.", e);
            throw new GameInterfaceDaoException(e);
        }
    }

    private int getResultCode(Map map) {
        if (null == map || null == map.get("code")) {
            return -1;
        }

        String code = (String) map.get("code");

        try {
            return Integer.valueOf(code);
        } catch (Exception e) {
            return -1000;
        }
    }
}
