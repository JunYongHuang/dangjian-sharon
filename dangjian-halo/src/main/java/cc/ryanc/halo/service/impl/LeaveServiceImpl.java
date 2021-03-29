package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Leave;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.repository.LeaveRepository;
import cc.ryanc.halo.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     请假单业务逻辑实现类
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Service
public class LeaveServiceImpl implements LeaveService {

    private static final String WHITES_CACHE_NAME = "leaves";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private LeaveRepository leaveRepository;


    /**
     * 保存请假单
     *
     * @param leave Leave
     * @return Leave
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Leave save(Leave leave) {
        return leaveRepository.save(leave);
    }

    /**
     * 根据编号移除请假单
     *
     * @param leaveId leaveId
     * @return Leave
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Leave remove(Long leaveId) {
        Optional<Leave> leave = this.findByLeaveId(leaveId);
        leaveRepository.delete(leave.get());
        return leave.get();
    }



    /**
     * 获取请假单列表 不分页
     *
     * @param postId leave or page
     * @return List
     */
    @Override
    @Cacheable(value = WHITES_CACHE_NAME, key = "'leaves_type_'+#postId")
    public List<Leave> findAll(String postId) {
        return leaveRepository.findLeavesByPost(postId);
    }

    /**
     * 模糊查询请假单
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    @Override
    public List<Leave> searchLeaves(String keyWord, Pageable pageable) {
        return leaveRepository.findByLeaveContentLike(keyWord, pageable);
    }

    /**
     * 根据请假单状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<Leave> findLeave(Pageable pageable) {
        return leaveRepository.findAll(pageable);
    }


    /**
     * 根据编号查询请假单
     *
     * @param leaveId leaveId
     * @return Optional
     */
    @Override
    public Optional<Leave> findByLeaveId(Long leaveId) {
        return leaveRepository.findById(leaveId);
    }

    @Override
    public Leave findByWhiteAndPost(White white, Post post) {
        return leaveRepository.findByWhiteAndPost(white, post);
    }


}
