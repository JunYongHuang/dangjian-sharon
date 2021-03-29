package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.SpecialType;
import cc.ryanc.halo.repository.SpecialTypeRepository;
import cc.ryanc.halo.service.SpecialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     分类业务逻辑实现类
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/30
 */
@Service
public class SpecialTypeServiceImpl implements SpecialTypeService {

    private static final String ROLES_CACHE_NAME = "specialTypes";

    @Autowired
    private SpecialTypeRepository specialTypeRepository;

    /**
     * 保存/修改分类目录
     *
     * @param specialType 分类目录
     * @return SpecialType
     */
    @Override
    @CacheEvict(value = ROLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public SpecialType save(SpecialType specialType) {
        return specialTypeRepository.save(specialType);
    }

    /**
     * 根据编号移除分类目录
     *
     * @param specialTypeId 分类目录编号
     * @return SpecialType
     */
    @Override
    @CacheEvict(value = ROLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public SpecialType remove(Long specialTypeId) {
        Optional<SpecialType> specialType = this.findBySpecialTypeId(specialTypeId);
        specialTypeRepository.delete(specialType.get());
        return specialType.get();
    }

    /**
     * 查询所有分类目录
     *
     * @return List
     */
    @Override
    public List<SpecialType> findAll() {
        return specialTypeRepository.findAll();
    }

    /**
     * 根据编号查询分类目录
     *
     * @param specialTypeId 分类编号
     * @return SpecialType
     */
    @Override
    public Optional<SpecialType> findBySpecialTypeId(Long specialTypeId) {
        return specialTypeRepository.findById(specialTypeId);
    }

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param specialTypeUrl specialTypeUrl
     * @return SpecialType
     */
    @Override
    public SpecialType findBySpecialTypeUrl(String specialTypeUrl) {
        return specialTypeRepository.findSpecialTypeBySpecialTypeUrl(specialTypeUrl);
    }

    /**
     * 根据分类名称查询
     *
     * @param specialTypeName 分类名称
     * @return SpecialType
     */
    @Override
    public SpecialType findBySpecialTypeName(String specialTypeName) {
        return specialTypeRepository.findSpecialTypeBySpecialTypeName(specialTypeName);
    }

    /**
     * 将分类字符串集合转化为SpecialType泛型集合
     *
     * @param strings strings
     * @return List
     */
    @Override
    public List<SpecialType> strListToSpecialTypeList(List<String> strings) {
        if (null == strings) {
            return null;
        }
        List<SpecialType> specialTypegories = new ArrayList<>();
        Optional<SpecialType> specialType = null;
        for (String str : strings) {
            specialType = findBySpecialTypeId(Long.parseLong(str));
            specialTypegories.add(specialType.get());
        }
        return specialTypegories;
    }

    @Override
    public Page<SpecialType> findSpecialType(Pageable pageable) {
        return specialTypeRepository.findAll(pageable);
    }
}
