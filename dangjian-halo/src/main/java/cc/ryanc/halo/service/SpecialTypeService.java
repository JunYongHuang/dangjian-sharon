package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.SpecialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     分类业务逻辑接口
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/30
 */
public interface SpecialTypeService {

    /**
     * 新增/修改分类目录
     *
     * @param specialType 分类目录
     * @return 如果插入成功，返回分类目录对象
     */
    SpecialType save(SpecialType specialType);

    /**
     * 根据编号删除分类目录
     *
     * @param specialTypeId 分类目录编号
     * @return specialType
     */
    SpecialType remove(Long specialTypeId);

    /**
     * 获取所有分类目录
     *
     * @return 返回List集合
     */
    List<SpecialType> findAll();

    /**
     * 根据编号查询单个分类
     *
     * @param specialTypeId 分类编号
     * @return 返回specialType实体
     */
    Optional<SpecialType> findBySpecialTypeId(Long specialTypeId);

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param specialTypeUrl specialTypeUrl
     * @return specialType
     */
    SpecialType findBySpecialTypeUrl(String specialTypeUrl);

    /**
     * 根据分类名称查询
     *
     * @param specialTypeName 分类名称
     * @return SpecialType
     */
    SpecialType findBySpecialTypeName(String specialTypeName);

    /**
     * 将分类字符串集合转化为SpecialType泛型集合
     *
     * @param strings strings
     * @return List
     */
    List<SpecialType> strListToSpecialTypeList(List<String> strings);


    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param pageable 分页信息
     * @return Page
     */
    Page<SpecialType> findSpecialType(Pageable pageable);
}
