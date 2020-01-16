package com.kaka.webgm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "game_items")
public class GameItem implements Serializable {
    private static final long serialVersionUID = 2782204944788501008L;

    private Long id;
    private Long itemId;
    private String itemName;
    private String itemDesc;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "item_id", nullable = false)
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Column(name = "item_name", length = 100, nullable = false)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Column(name = "item_desc", length = 200)
    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    @Override
    public String toString() {
        return "GameItem{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                '}';
    }
}
