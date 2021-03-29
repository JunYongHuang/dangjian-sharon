package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Backlog;
import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.repository.BacklogRepository;
import cc.ryanc.halo.repository.WhiteRepository;
import cc.ryanc.halo.service.BacklogService;
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
public class BacklogServiceImpl implements BacklogService {

    private static final String WHITES_CACHE_NAME = "backlogs";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private WhiteRepository whiteRepository;

    @Autowired
    private CategoryService categoryService;


    /**
     * 保存文章
     *
     * @param backlog Backlog
     * @return Backlog
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Backlog save(Backlog backlog) {
        return backlogRepository.save(backlog);
    }

    /**
     * 根据编号移除文章
     *
     * @param backlogId backlogId
     * @return Backlog
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Backlog remove(Long backlogId) {
        Optional<Backlog> backlog = this.findByBacklogId(backlogId);
        backlogRepository.delete(backlog.get());
        return backlog.get();
    }



    /**
     * 获取文章列表 不分页
     *
     * @param postId backlog or page
     * @return List
     */
    @Override
    @Cacheable(value = WHITES_CACHE_NAME, key = "'backlogs_type_'+#postId")
    public List<Backlog> findAll(String postId) {
        return backlogRepository.findBacklogsByPost(postId);
    }

    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    @Override
    public List<Backlog> searchBacklogs(String keyWord, Pageable pageable) {
        return backlogRepository.findByBacklogNameLike(keyWord, pageable);
    }

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<Backlog> findBacklog(Pageable pageable) {
        return backlogRepository.findAll(pageable);
    }


    /**
     * 根据编号查询文章
     *
     * @param backlogId backlogId
     * @return Optional
     */
    @Override
    public Optional<Backlog> findByBacklogId(Long backlogId) {
        return backlogRepository.findById(backlogId);
    }

    @Override
    public void sendBacklog(Post post, List<String> cateList) {
        List<Category> categories = categoryService.strListToCateList(cateList);
        for (Category category: categories) {
            Integer roleId = category.getRoleId();
            if(roleId > 0){
                List<White> whites = whiteRepository.findWhitesByRoleId(roleId);
                for (White white:whites) {
                    Backlog backlog = backlogRepository.findBacklogByPostAndWhite(post, white);
                    if(backlog == null){
                        backlog = new Backlog();
                        backlog.setBacklogName("");
                        backlog.setPost(post);
                        backlog.setWhite(white);
                        Date d = new Date();
                        backlog.setBacklogDate(d);
                        backlog.setBacklogUpdate(d);
                        backlogRepository.save(backlog);

                    }
                }

            }
        }
    }

    @Override
    public Page<Backlog> findByWhite(White white, Pageable pageable) {
        return backlogRepository.findByWhite(white, pageable);
    }

    @Override
    public Backlog findByWhiteAndPost(White white, Post post) {
        return backlogRepository.findBacklogByPostAndWhite(post, white);
    }

    @Override
    public Page<Backlog> findByWhiteAndLessThanBacklogStatus(White white, Pageable pageable, int backlogStatus) {
        return backlogRepository.findByWhiteAndBacklogStatusLessThan(white.getWhiteId(), white.getRoleId(),  backlogStatus, pageable);
    }

    @Override
    public Page<Backlog> findBacklogByRight(Pageable pageable) {
        return backlogRepository.findBacklogByRight(pageable);
    }


}
