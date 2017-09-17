package org.openchs.healthmodule.adapter.contract;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.openchs.util.O;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RuleResponse {
    protected ScriptObjectMirror scriptObjectMirror;

    public RuleResponse(ScriptObjectMirror scriptObjectMirror) {
        this.scriptObjectMirror = scriptObjectMirror;
    }

    protected String getDateAsString(ScriptObjectMirror mirror, String name) {
        return O.getDateInDbFormat(this.getDate(mirror, name));
    }

    protected Date getDate(ScriptObjectMirror mirror, String name) {
        ScriptObjectMirror field = (ScriptObjectMirror) mirror.get(name);
        double timestampLocalTime = (Double) field.callMember("getTime");
        return new Date((long) timestampLocalTime);
    }

    protected Date getDate(String name) {
        return getDate(scriptObjectMirror, name);
    }

    protected String getDateAsString(String name) {
        return this.getDateAsString(scriptObjectMirror, name);
    }

    protected boolean isDate(ScriptObjectMirror mirror, String name) {
        ScriptObjectMirror field = (ScriptObjectMirror) mirror.get(name);
        return field.hasMember("getTime");
    }

    protected boolean isDate(String name) {
        return this.isDate(scriptObjectMirror, name);
    }

    protected Object getUnderlyingValue(ScriptObjectMirror mirror, String name, ObjectCreator objectCreator) {
        if (isDate(name)) return getDateAsString(name);
        if (isList(name)) {
            ArrayList list = new ArrayList();
            addToList((ScriptObjectMirror) mirror.get(name), list, objectCreator);
            return list;
        }
        if (mirror.get(name) instanceof ScriptObjectMirror)
            return new ArrayList();
        return mirror.get(name);
    }

    protected Object getUnderlyingValue(String name, ObjectCreator objectCreator) {
        return this.getUnderlyingValue(this.scriptObjectMirror, name, objectCreator);
    }

    protected void addToList(ScriptObjectMirror array, List list, ObjectCreator objectCreator) {
        int length = array.getOwnKeys(false).length;
        for (int i = 0; i < length; i++) {
            Object arrayElement = array.get(Integer.toString(i));
            list.add(objectCreator.create(arrayElement));
        }
    }

    protected boolean isList(ScriptObjectMirror mirror, String name) {
        ScriptObjectMirror field = (ScriptObjectMirror) mirror.get(name);
        return field.get("0") != null;
    }

    protected boolean isList(String name) {
        return this.isList(scriptObjectMirror, name);
    }

    interface ObjectCreator {
        Object create(Object object);
    }
}