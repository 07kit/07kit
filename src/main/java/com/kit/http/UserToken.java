package com.kit.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * User token model
 *
 */
public final class UserToken {

    @JsonProperty("id")
    private long id;
    @JsonProperty("account")
    private UserAccount account;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("created")
    private Timestamp created;
    @JsonProperty("updated")
    private Timestamp updated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "id=" + id +
                ", account=" + account +
                ", uuid='" + uuid + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
