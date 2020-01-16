package com.kaka.webgm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "gm_opertion_logs")
public class GmOperationLog implements Serializable {
    private static final long serialVersionUID = -8744401668759490744L;

    private Long id;

    private String opUser;
    private String opIp;
    private String reqPath;
    private String reqArg;
    private String resData;
    private Date createDate = new Date();

    public GmOperationLog() {
    }

    public GmOperationLog(String opUser, String opIp, String reqPath, String reqArg, String resData) {
        this.opUser = opUser;
        this.opIp = opIp;
        this.reqPath = reqPath;
        this.reqArg = reqArg;
        this.resData = resData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "op_user", length = 100, nullable = false)
    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    @Column(name = "op_ip", length = 100)
    public String getOpIp() {
        return opIp;
    }

    public void setOpIp(String opIp) {
        this.opIp = opIp;
    }

    @Column(name = "req_path", length = 200, nullable = false)
    public String getReqPath() {
        return reqPath;
    }

    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }

    @Column(name = "req_arg", nullable = false)
    @Lob
    public String getReqArg() {
        return reqArg;
    }

    public void setReqArg(String reqArg) {
        this.reqArg = reqArg;
    }

    @Column(name = "res_data", nullable = false)
    @Lob
    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    @Column(name = "create_data", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
