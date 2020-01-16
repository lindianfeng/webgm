package com.kaka.webgm.controller;

import com.kaka.webgm.domain.GmOperationLog;
import com.kaka.webgm.service.GmOpLogServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GmOpLogController {
    @Autowired
    private GmOpLogServiceImpl opLogService;

    @RequestMapping(path = "/admin/log/index")
    public String toLogListView() {
        return "admin/log/gm-op-log-list";
    }

    @RequestMapping(path = "/admin/log/list")
    @ResponseBody
    public Map<String, Object> getGmOpLogs(HttpServletRequest request) {

        int pageSize = StringUtils.isEmpty(request.getParameter("length")) ? 10 : Integer.valueOf(request.getParameter("length"));
        int startRows = StringUtils.isEmpty(request.getParameter("start")) ? 0 : Integer.valueOf(request.getParameter("start"));
        int pageNo = startRows / pageSize;

        String extraSearch = request.getParameter("extra_search");
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable pageable = new PageRequest(pageNo, pageSize, sort);

        Page<GmOperationLog> pageList;

        if (StringUtils.isEmpty(extraSearch) || !extraSearch.contains(":")) {
            pageList = this.opLogService.getGmOpLogs(pageable);
        } else {
            String[] temp = extraSearch.split(":");
            if (temp.length != 2 || !StringUtils.isNumeric(temp[0]) || StringUtils.isEmpty(temp[1])) {
                pageList = this.opLogService.getGmOpLogs(pageable);
            } else {
                Integer queryType = Integer.valueOf(temp[0]);
                String keyWord = temp[1];
                pageList = this.opLogService.findAll(queryType, keyWord, pageable);
            }
        }

        final Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("recordsTotal", pageList.getTotalElements());
        resultMap.put("recordsFiltered", pageList.getTotalElements());
        resultMap.put("data", pageList.getContent());

        return resultMap;
    }
}
