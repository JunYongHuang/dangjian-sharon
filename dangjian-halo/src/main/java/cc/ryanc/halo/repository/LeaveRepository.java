package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Leave;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <pre>
 *     请假单持久层
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    /**
     * 查询前五条请假单
     *
     * @return List
     */
    @Query(value = "SELECT * FROM halo_leave where leave_type='leave' ORDER BY leave_date DESC LIMIT 5", nativeQuery = true)
    List<Leave> findTopFive();

    /**
     * 查询所有请假单 根据请假单类型
     *
     * @param postId leave or page
     * @return List
     */
    List<Leave> findLeavesByPost(String postId);

    /**
     * 分页查询请假单
     *
     * @param postId leave or page
     * @param pageable 分页信息
     * @return Page
     */
    Page<Leave> findLeavesByPost(String postId, Pageable pageable);

    /**
     * 模糊查询
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<Leave> findByLeaveContentLike(String keyWord, Pageable pageable);

    /**
     * 根据请假单的状态查询 分页
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<Leave> findAll(Pageable pageable);

    Leave findByWhiteAndPost(White white, Post post);
}
