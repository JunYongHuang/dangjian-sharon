package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Role;
import cc.ryanc.halo.repository.RoleRepository;
import cc.ryanc.halo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
public class RoleServiceImpl implements RoleService {

    private static final String ROLES_CACHE_NAME = "roles";

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 保存/修改分类目录
     *
     * @param role 分类目录
     * @return Role
     */
    @Override
    @CacheEvict(value = ROLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    /**
     * 根据编号移除分类目录
     *
     * @param roleId 分类目录编号
     * @return Role
     */
    @Override
    @CacheEvict(value = ROLES_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Role remove(Long roleId) {
        Optional<Role> role = this.findByRoleId(roleId);
        roleRepository.delete(role.get());
        return role.get();
    }

    /**
     * 查询所有分类目录
     *
     * @return List
     */
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * 根据编号查询分类目录
     *
     * @param roleId 分类编号
     * @return Role
     */
    @Override
    public Optional<Role> findByRoleId(Long roleId) {
        return roleRepository.findById(roleId);
    }

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param roleUrl roleUrl
     * @return Role
     */
    @Override
    public Role findByRoleUrl(String roleUrl) {
        return roleRepository.findRoleByRoleUrl(roleUrl);
    }

    /**
     * 根据分类名称查询
     *
     * @param roleName 分类名称
     * @return Role
     */
    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }

    /**
     * 将分类字符串集合转化为Role泛型集合
     *
     * @param strings strings
     * @return List
     */
    @Override
    public List<Role> strListToRoleList(List<String> strings) {
        if (null == strings) {
            return null;
        }
        List<Role> rolegories = new ArrayList<>();
        Optional<Role> role = null;
        for (String str : strings) {
            role = findByRoleId(Long.parseLong(str));
            rolegories.add(role.get());
        }
        return rolegories;
    }
}
