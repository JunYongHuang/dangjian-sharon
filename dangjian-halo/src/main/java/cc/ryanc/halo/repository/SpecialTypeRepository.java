package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.SpecialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <pre>
 *     分类持久层
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/30
 */
public interface SpecialTypeRepository extends JpaRepository<SpecialType, Long> {

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param specialTypeUrl specialTypeUrl 专题url
     * @return SpecialType
     */
    SpecialType findSpecialTypeBySpecialTypeUrl(String specialTypeUrl);

    /**
     * 根据分类名称查询
     *
     * @param specialTypeName 分类名称
     * @return SpecialType
     */
    SpecialType findSpecialTypeBySpecialTypeName(String specialTypeName);

    /**
     * 根据分类查询专题总数
     *
     * @return Long
     */
    @Query(value = "select count(1) from halo_posts_specialTypegories where specialType_id=:specialTypeId", nativeQuery = true)
    Long getSpecialTypePostCount(@Param("specialTypeId") Long specialTypeId);

    Page<SpecialType> findAll(Pageable pageable);
}
