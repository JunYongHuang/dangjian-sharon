package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.WxUser;
import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.repository.WxUserRepository;
import cc.ryanc.halo.repository.WhiteRepository;
import cc.ryanc.halo.service.WxUserService;
import cc.ryanc.halo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     文章业务逻辑实现类
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Service
public class WxUserServiceImpl implements WxUserService {

    private static final String WHITES_CACHE_NAME = "wxUsers";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private WxUserRepository wxUserRepository;

    @Autowired
    private WhiteRepository whiteRepository;

    @Autowired
    private CategoryService categoryService;


    /**
     * 保存文章
     *
     * @param wxUser WxUser
     * @return WxUser
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public WxUser save(WxUser wxUser) {
        return wxUserRepository.save(wxUser);
    }

    /**
     * 根据编号移除文章
     *
     * @param wxUserId wxUserId
     * @return WxUser
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public WxUser remove(Long wxUserId) {
        Optional<WxUser> wxUser = this.findByWxUserId(wxUserId);
        wxUserRepository.delete(wxUser.get());
        return wxUser.get();
    }




    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    @Override
    public List<WxUser> searchWxUsers(String keyWord, Pageable pageable) {
        return wxUserRepository.findByNickNameLike(keyWord, pageable);
    }

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<WxUser> findWxUser(Pageable pageable) {
        return wxUserRepository.findAll(pageable);
    }


    /**
     * 根据编号查询文章
     *
     * @param wxUserId wxUserId
     * @return Optional
     */
    @Override
    public Optional<WxUser> findByWxUserId(Long wxUserId) {
        return wxUserRepository.findById(wxUserId);
    }

    @Override
    public WxUser findByOpenId(String openId) {
        return wxUserRepository.findByOpenId(openId);
    }


}
