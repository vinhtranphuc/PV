package com.so.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * User for mapping field with sql select
 *
 * @author Vinh
 */
public class FieldMap extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = -5018625254427195587L;

    ArrayList<Object> allIndex = new ArrayList<Object>();

    @Override
    public Object put(String key, Object value) {
        if (!super.containsKey(key))
            allIndex.add(key);
        return super.put(StringUtils.lowerCase(key), value);
    }

    public Object getValueAtIndex(int i) {
        return super.get((i <= (allIndex.size() - 1) && allIndex.get(i) != null) ? allIndex.get(i) : "");
    }

    public Object getKeyAtIndex(int i) {
        try {
            allIndex.get(i);
        } catch (Exception e) {
            return null;
        }
        return allIndex.get(i);
    }

    public int getIndexOf(String key) {
        return allIndex.indexOf(key);
    }
}
