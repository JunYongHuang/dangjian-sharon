package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.WxUser;
import cc.ryanc.halo.model.domain.Post;
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
public interface WxUserService {

    /**
     * 新增文章
     *
     * @param wxUser WxUser
     * @return WxUser
     */
    WxUser save(WxUser wxUser);

    /**
     * 根据编号删除文章
     *
     * @param wxUserId wxUserId
     * @return WxUser
     */
    WxUser remove(Long wxUserId);





    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<WxUser> searchWxUsers(String keyWord, Pageable pageable);

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<WxUser> findWxUser(Pageable pageable);




    /**
     * 根据编号查询文章
     *
     * @param wxUserId wxUserId
     * @return WxUser
     */
    Optional<WxUser> findByWxUserId(Long wxUserId);


    WxUser findByOpenId(String openId);
}
