package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.domain.Tag;
import cc.ryanc.halo.model.dto.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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
public interface WhiteService {

    /**
     * 新增文章
     *
     * @param white White
     * @return White
     */
    White save(White white);

    /**
     * 根据编号删除文章
     *
     * @param whiteId whiteId
     * @return White
     */
    White remove(Long whiteId);



    /**
     * 获取文章列表 不分页
     *
     * @param roleId white or page
     * @return List
     */
    List<White> findAll(String roleId);

    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    List<White> searchWhites(String keyWord, Pageable pageable);

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<White> findWhite(Pageable pageable);




    /**
     * 根据编号查询文章
     *
     * @param whiteId whiteId
     * @return White
     */
    Optional<White> findByWhiteId(Long whiteId);


    White findByPhoneNo(String phonenum);
}
