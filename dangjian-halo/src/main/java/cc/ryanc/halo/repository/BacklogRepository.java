package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Backlog;
import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <pre>
 *     文章持久层
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

    /**
     * 查询前五条文章
     *
     * @return List
     */
    @Query(value = "SELECT * FROM halo_backlog where backlog_type='backlog' ORDER BY backlog_date DESC LIMIT 5", nativeQuery = true)
    List<Backlog> findTopFive();

    /**
     * 查询所有文章 根据文章类型
     *
     * @param postId backlog or page
     * @return List
     */
    List<Backlog> findBacklogsByPost(String postId);

    /**
     * 分页查询文章
     *
     * @param postId backlog or page
     * @param pageable 分页信息
     * @return Page
     */
    Page<Backlog> findBacklogsByPost(String postId, Pageable pageable);

    /**
     * 模糊查询
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<Backlog> findByBacklogNameLike(String keyWord, Pageable pageable);

    /**
     * 根据文章的状态查询 分页
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<Backlog> findAll(Pageable pageable);

    Backlog findBacklogByPostAndWhite(Post post, White white);

    Page<Backlog> findByWhite(White white, Pageable pageable);

    @Query("select b from Backlog b, in(b.post.categories) cc where b.white.whiteId = :whiteId and  cc.roleId = :roleId and b.backlogStatus < :backlogStatus and b.post.postStatus = 0")
    Page<Backlog> findByWhiteAndBacklogStatusLessThan(Long whiteId, int backlogStatus, int roleId, Pageable pageable);

    @Query("select b from Backlog b, in(b.post.categories) cc where  cc.roleId = b.white.roleId  and b.post.postStatus = 0")
    Page<Backlog> findBacklogByRight(Pageable pageable);
}
