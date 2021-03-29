package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.BKCode;
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
public interface BKCodeRepository extends JpaRepository<BKCode, Long> {




    /**
     * 根据文章的状态查询 分页
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<BKCode> findAll(Pageable pageable);

    BKCode findByPhonenum(long phonenum);

    BKCode findByPhonenumAndCode(long phonenum, String code);
}
