package com.kaka.webgm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "game_servers")
public class GameServer implements Serializable {
    private static final long serialVersionUID = 4097991944368194612L;
    private Long id;
    private String serverName;
    private Integer serverId;
    private String serverHost;
    private Integer serverPort;
    private String serverDesc;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "server_name", length = 100, nullable = false)
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Column(name = "server_id", nullable = false)
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Column(name = "server_host", length = 200, nullable = false)
    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    @Column(name = "server_port", nullable = false)
    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Column(name = "server_desc")
    public String getServerDesc() {
        return serverDesc;
    }

    public void setServerDesc(String serverDesc) {
        this.serverDesc = serverDesc;
    }
}
