package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.WxUser;
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
public interface WxUserRepository extends JpaRepository<WxUser, Long> {




    /**
     * 模糊查询
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<WxUser> findByNickNameLike(String keyWord, Pageable pageable);

    /**
     * 根据文章的状态查询 分页
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<WxUser> findAll(Pageable pageable);


    WxUser findByOpenId(String openId);
}
