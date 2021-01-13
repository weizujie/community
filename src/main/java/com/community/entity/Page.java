package com.community.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 封装分页相关的信息
 */
@AllArgsConstructor
@NoArgsConstructor
public class Page {

    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows;
    // 查询路径(用于复用分页链接)
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal() {
        //  rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     */
    public int getStart() {
        int from = current - 2;
        // from < 1 ? 1 : from
        return Math.max(from, 1);
    }

    /**
     * 获取结束页码
     */
    public int getEnd() {
        int end = current + 2;
        int total = getTotal();
        // end > total ? total : end
        return Math.min(end, total);
    }
}
