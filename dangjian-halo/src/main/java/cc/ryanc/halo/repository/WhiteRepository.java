package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     文章持久层
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
public interface WhiteRepository extends JpaRepository<White, Long> {

    /**
     * 查询前五条文章
     *
     * @return List
     */
    @Query(value = "SELECT * FROM halo_white where white_type='white' ORDER BY white_date DESC LIMIT 5", nativeQuery = true)
    List<White> findTopFive();

    /**
     * 查询所有文章 根据文章类型
     *
     * @param roleId white or page
     * @return List
     */
    List<White> findWhitesByRoleId(Integer roleId);

    /**
     * 分页查询文章
     *
     * @param roleId white or page
     * @param pageable 分页信息
     * @return Page
     */
    Page<White> findWhitesByRoleId(String roleId, Pageable pageable);

    /**
     * 模糊查询
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<White> findByWhiteNameLike(String keyWord, Pageable pageable);

    /**
     * 根据文章的状态查询 分页
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<White> findAll(Pageable pageable);

    White findByPhoneNo(String phonenum);
}
