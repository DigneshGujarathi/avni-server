package org.openchs.web.request;

import java.util.Map;

public class RuleRequest {
    private String ruleDependencyUUID;
    private String formUUID;
    private String type;
    private Double executionOrder;

    private Map<String, String> data;

    private String name;

    private String uuid;
    private String fnName;

    public String getFormUUID() {
        return formUUID;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFnName() {
        return fnName;
    }

    public void setFnName(String fnName) {
        this.fnName = fnName;
    }

    public String getRuleDependencyUUID() {
        return ruleDependencyUUID;
    }

    public void setRuleDependencyUUID(String ruleDependencyUUID) {
        this.ruleDependencyUUID = ruleDependencyUUID;
    }

    public Double getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Double executionOrder) {
        this.executionOrder = executionOrder;
    }
}