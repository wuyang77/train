package org.wuyang.${module}.req;

import req.org.wuyang.train.common.PageReq;

public class ${Domain}QueryReq extends PageReq {

    <#list fieldList as field>
        <#if field.name?ends_with('_id')>
    private ${field.javaType} ${field.nameHump};

    public ${field.javaType} get${field.nameBigHump}() {
        return ${field.nameHump};
    }

    public void set${field.nameBigHump}(${field.javaType} ${field.nameHump}) {
        this.${field.nameHump} = ${field.nameHump};
    }
        </#if>
    </#list>

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("${Domain}QueryReq{");
        <#list fieldList as field>
            <#if field.name?ends_with('_id')>
        sb.append("${field.nameHump}=").append(${field.nameHump});
            </#if>
        </#list>
        sb.append('}');
        return sb.toString();
    }
}
