package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.BKCode;
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
public interface BKCodeService {

    /**
     * 新增文章
     *
     * @param bkCode BKCode
     * @return BKCode
     */
    BKCode save(BKCode bkCode);

    /**
     * 根据编号删除文章
     *
     * @param bkCodeId bkCodeId
     * @return BKCode
     */
    BKCode remove(Long bkCodeId);




    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<BKCode> findBKCode(Pageable pageable);




    /**
     * 根据编号查询文章
     *
     * @param bkCodeId bkCodeId
     * @return BKCode
     */
    Optional<BKCode> findByBKCodeId(Long bkCodeId);


    BKCode findByPhonenum(long phonenum);

    BKCode findByPhonenumAndCode(long phonenum, String code);
}
