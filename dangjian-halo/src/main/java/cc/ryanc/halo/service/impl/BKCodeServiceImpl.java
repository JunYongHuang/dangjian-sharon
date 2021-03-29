package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.BKCode;
import cc.ryanc.halo.repository.BKCodeRepository;
import cc.ryanc.halo.service.BKCodeService;
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
 *     文章业务逻辑实现类
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Service
public class BKCodeServiceImpl implements BKCodeService {

    private static final String WHITES_CACHE_NAME = "bkCodes";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private BKCodeRepository bkCodeRepository;


    /**
     * 保存文章
     *
     * @param bkCode BKCode
     * @return BKCode
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public BKCode save(BKCode bkCode) {
        return bkCodeRepository.save(bkCode);
    }

    /**
     * 根据编号移除文章
     *
     * @param bkCodeId bkCodeId
     * @return BKCode
     */
    @Override
    @CacheEvict(value = {WHITES_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public BKCode remove(Long bkCodeId) {
        Optional<BKCode> bkCode = this.findByBKCodeId(bkCodeId);
        bkCodeRepository.delete(bkCode.get());
        return bkCode.get();
    }




    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<BKCode> findBKCode(Pageable pageable) {
        return bkCodeRepository.findAll(pageable);
    }


    /**
     * 根据编号查询文章
     *
     * @param bkCodeId bkCodeId
     * @return Optional
     */
    @Override
    public Optional<BKCode> findByBKCodeId(Long bkCodeId) {
        return bkCodeRepository.findById(bkCodeId);
    }

    @Override
    public BKCode findByPhonenum(long phonenum) {
        return bkCodeRepository.findByPhonenum(phonenum);
    }

    @Override
    public BKCode findByPhonenumAndCode(long phonenum, String code) {
        return bkCodeRepository.findByPhonenumAndCode(phonenum, code);
    }


}
