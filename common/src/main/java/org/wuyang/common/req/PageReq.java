package org.wuyang.common.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class PageReq {
    
    @NotNull(message = "【页码】不能为空")
    private Integer pageNum;

    @NotNull(message = "【每页条数】不能为空")
    @Max(value = 100, message = "【每页条数】不能超过100")
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("pageNum=").append(pageNum);
        sb.append(", pageSize=").append(pageSize);
        sb.append('}');
        return sb.toString();
    }
}
