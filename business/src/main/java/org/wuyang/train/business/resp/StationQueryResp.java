package org.wuyang.train.business.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class StationQueryResp {

    /**
    * id
    */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
    * 站名
    */
    private String name;

    /**
    * 站名拼音
    */
    private String namePinyin;

    /**
    * 站名拼音首字母
    */
    private String namePy;

    /**
    * 新增时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
    * 修改时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getNamePy() {
        return namePy;
    }

    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StationQueryResp{");
        sb.append(", id =").append(id );
        sb.append(", name =").append(name ).append('\'');
        sb.append(", namePinyin =").append(namePinyin ).append('\'');
        sb.append(", namePy =").append(namePy ).append('\'');
        sb.append(", createTime =").append(createTime );
        sb.append(", updateTime =").append(updateTime );
        sb.append('}');
        return sb.toString();
    }
}
