package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.domain.Tag;
import cc.ryanc.halo.model.dto.Archive;

import cc.ryanc.halo.repository.WhiteRepository;
import cc.ryanc.halo.service.CategoryService;
import cc.ryanc.halo.service.WhiteService;
import cc.ryanc.halo.service.TagService;
import cc.ryanc.halo.utils.HaloUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class WhiteServiceImpl implements WhiteService {

    private static final String WHITES_CACHE_NAME = "whites";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private WhiteRepository whiteRepository;


    /**
     * 保存文章
     *
     * @param white White
     * @return White
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public White save(White white) {
        return whiteRepository.save(white);
    }

    /**
     * 根据编号移除文章
     *
     * @param whiteId whiteId
     * @return White
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public White remove(Long whiteId) {
        Optional<White> white = this.findByWhiteId(whiteId);
        whiteRepository.delete(white.get());
        return white.get();
    }



    /**
     * 获取文章列表 不分页
     *
     * @param roleId white or page
     * @return List
     */
    @Override
    @Cacheable(value = WHITES_CACHE_NAME, key = "'whites_type_'+#roleId")
    public List<White> findAll(String roleId) {
        return whiteRepository.findWhitesByRoleId(Integer.parseInt(roleId));
    }

    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    @Override
    public List<White> searchWhites(String keyWord, Pageable pageable) {
        return whiteRepository.findByWhiteNameLike(keyWord, pageable);
    }

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<White> findWhite(Pageable pageable) {
        return whiteRepository.findAll(pageable);
    }


    /**
     * 根据编号查询文章
     *
     * @param whiteId whiteId
     * @return Optional
     */
    @Override
    public Optional<White> findByWhiteId(Long whiteId) {
        return whiteRepository.findById(whiteId);
    }

    @Override
    public White findByPhoneNo(String phonenum) {
        return whiteRepository.findByPhoneNo(phonenum);
    }


}
