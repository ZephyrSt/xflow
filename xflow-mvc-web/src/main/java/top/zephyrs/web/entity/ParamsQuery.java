package top.zephyrs.web.entity;

import com.github.pagehelper.PageParam;
import top.zephyrs.xflow.utils.JSONUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ParamsQuery implements top.zephyrs.xflow.entity.Query, Map<String, Object> {

    private PageParam page;
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    private final Map<String, Object> data = new HashMap<>();

    public ParamsQuery() {
    }

    public void setPage(PageParam page) {
        this.page = page;
    }

    public PageParam getPage() {
        if(this.page == null) {
            if(data.get("page") != null) {
                String json = JSONUtils.toJson(data.get("page"));
                this.page = JSONUtils.fromJson(json, PageParam.class);
            } else {
                this.page = new PageParam(1, DEFAULT_PAGE_SIZE);
            }
        }
        return page;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return data.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return data.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        data.putAll(m);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<Object> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return data.entrySet();
    }
}
