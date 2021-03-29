package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.Leave;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     请假单/页面业务逻辑接口
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
public interface LeaveService {

    /**
     * 新增请假单
     *
     * @param leave Leave
     * @return Leave
     */
    Leave save(Leave leave);

    /**
     * 根据编号删除请假单
     *
     * @param leaveId leaveId
     * @return Leave
     */
    Leave remove(Long leaveId);



    /**
     * 获取请假单列表 不分页
     *
     * @param roleId leave or page
     * @return List
     */
    List<Leave> findAll(String roleId);

    /**
     * 模糊查询请假单
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<Leave> searchLeaves(String keyWord, Pageable pageable);

    /**
     * 根据请假单状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<Leave> findLeave(Pageable pageable);




    /**
     * 根据编号查询请假单
     *
     * @param leaveId leaveId
     * @return Leave
     */
    Optional<Leave> findByLeaveId(Long leaveId);


    Leave findByWhiteAndPost(White white, Post post);
}
