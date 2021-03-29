package cc.ryanc.halo.model.dto;

/**
 * <pre>
 *     日志常量
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/1/19
 */
public interface LogsRecord {

    String INSTALL = "初始化应用";

    String LOGIN = "登录后台";

    String LOGIN_SUCCESS = "登录成功";

    String LOGIN_ERROR = "登录失败";

    String LOGOUT = "退出登录";

    String PUSH_POST = "发布提示单";
    String REMOVE_POST = "删除提示单";
    String PUSH_SPECIAL = "发布专题";
    String REMOVE_SPECIAL = "删除专题";
    String PUSH_MEETING = "发布会议通知";
    String REMOVE_MEETING = "删除会议通知";
    String PUSH_WHITE = "新增白名单";
    String REMOVE_WHITE = "删除白名单";
    String PUSH_BACKLOG = "新增待办提醒";
    String REMOVE_BACKLOG = "删除待办提醒";

    String PUSH_PAGE = "发表页面";



    String CHANGE_THEME = "更换主题";

    String UPLOAD_THEME = "上传主题";

    String UPLOAD_FILE = "上传附件";

    String REMOVE_FILE = "移除附件";

    String PUSH_LEAVE = "会议请假";
    String REMOVE_LEAVE = "删除请假";
}
