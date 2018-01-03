package com.bugsnag.util;


import java.util.*;

/**
 * Decorates a map, and alters the return value of {@link #get(Object)} if the key matches a filter.
 */
public class FilteredMap implements Map<String, Object> {

    private static final String FILTERED_PLACEHOLDER = "[FILTERED]";

    private final Map<String, Object> map;
    private final Collection<String> keyFilters = new ArrayList<String>();

    public FilteredMap(Map<String, Object> map, Collection<String> keyFilters) {
        this.map = map;
        this.keyFilters.addAll(keyFilters);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public Object get(Object key) {
        Object o = map.get(key);
        return o != null ? transformEntry(key, o) : null;
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    private Object transformEntry(Object key, Object value) {
        if (value instanceof Map) {
            //noinspection unchecked
            return new FilteredMap((Map<String, Object>) value, keyFilters);
        }
        return shouldFilterKey((String) key) ? FILTERED_PLACEHOLDER : value;
    }

    private boolean shouldFilterKey(String key) {
        if (keyFilters == null || key == null) {
            return false;
        }

        for (String filter : keyFilters) {
            if (key.contains(filter)) {
                return true;
            }
        }
        return false;
    }

}
