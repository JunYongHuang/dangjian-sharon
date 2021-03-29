package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.Backlog;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     文章/页面业务逻辑接口
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
public interface BacklogService {

    /**
     * 新增文章
     *
     * @param backlog Backlog
     * @return Backlog
     */
    Backlog save(Backlog backlog);

    /**
     * 根据编号删除文章
     *
     * @param backlogId backlogId
     * @return Backlog
     */
    Backlog remove(Long backlogId);



    /**
     * 获取文章列表 不分页
     *
     * @param roleId backlog or page
     * @return List
     */
    List<Backlog> findAll(String roleId);

    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<Backlog> searchBacklogs(String keyWord, Pageable pageable);

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<Backlog> findBacklog(Pageable pageable);




    /**
     * 根据编号查询文章
     *
     * @param backlogId backlogId
     * @return Backlog
     */
    Optional<Backlog> findByBacklogId(Long backlogId);


    void sendBacklog(Post post, List<String> cateList);

    Page<Backlog> findByWhite(White white, Pageable pageable);

    Backlog findByWhiteAndPost(White white, Post post);

    Page<Backlog> findByWhiteAndLessThanBacklogStatus(White white, Pageable pageable, int backlogStatus);

    Page<Backlog> findBacklogByRight(Pageable pageable);
}
