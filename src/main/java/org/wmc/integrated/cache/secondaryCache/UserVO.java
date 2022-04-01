package org.wmc.integrated.cache.secondaryCache;

import java.io.Serializable;
import java.util.Date;

public class UserVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -932137350038376517L;

    private long id;

    private String name;

    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}