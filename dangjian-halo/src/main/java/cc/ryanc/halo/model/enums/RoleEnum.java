package cc.ryanc.halo.model.enums;

/**
 * <pre>
 *     文章状态enum
 * </pre>
 *
 * @author : HJY
 * @date : 2018/7/1
 */
public enum RoleEnum {


    /**
     * 党委
     */
    COMMITTEE(1, "党委"),

    /**
     * 党支部
     */
    BRANCH(2, "党支部");

    private Integer code;
    private String desc;

    RoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
