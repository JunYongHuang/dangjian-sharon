package cc.ryanc.halo.model.enums;

/**
 * <pre>
 *     文章类型enum
 * </pre>
 *
 * @author : HJY
 * @date : 2018/7/1
 */
public enum PostTypeEnum {

    /**
     * 文章
     */
    POST_TYPE_POST("post"),

    /**
     * 页面
     */
    POST_TYPE_PAGE("page"),

    /**
     * special 专题
     */
    POST_TYPE_SPECIAL("special"),
    /**
     * meeting 会议
     */
    POST_TYPE_MEETING("meeting");

    private String desc;

    PostTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
