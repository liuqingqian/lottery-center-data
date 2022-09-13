package com.hermes.lotdata.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/1.
 * <p>
 * 大小枚举 @1:小@2:大@:3豹
 */
public enum LotSizeEnum {
    SMALL(1, "小"),
    BIG(2, "大"),
    DUAL(3, "豹");

    private Integer code;

    private String desc;

    LotSizeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equals(Integer code) {
        return this.getCode().equals(code);
    }

    public boolean equalsDesc(String desc) {
        return this.getDesc().equals(desc);
    }

    public static LotSizeEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        LotSizeEnum[] values = values();
        for (LotSizeEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static LotSizeEnum fromDesc(String desc) {
        if (Objects.isNull(desc)) {
            return null;
        }
        LotSizeEnum[] values = values();
        for (LotSizeEnum item : values) {
            if (item.getDesc().equals(desc)) {
                return item;
            }
        }
        return null;
    }
}
