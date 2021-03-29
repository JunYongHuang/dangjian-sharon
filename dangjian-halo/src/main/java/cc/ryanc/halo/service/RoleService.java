package cc.ryanc.halo.service;

import cc.ryanc.halo.model.domain.Role;

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
public interface RoleService {

    /**
     * 新增/修改分类目录
     *
     * @param role 分类目录
     * @return 如果插入成功，返回分类目录对象
     */
    Role save(Role role);

    /**
     * 根据编号删除分类目录
     *
     * @param roleId 分类目录编号
     * @return role
     */
    Role remove(Long roleId);

    /**
     * 获取所有分类目录
     *
     * @return 返回List集合
     */
    List<Role> findAll();

    /**
     * 根据编号查询单个分类
     *
     * @param roleId 分类编号
     * @return 返回role实体
     */
    Optional<Role> findByRoleId(Long roleId);

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param roleUrl roleUrl
     * @return role
     */
    Role findByRoleUrl(String roleUrl);

    /**
     * 根据分类名称查询
     *
     * @param roleName 分类名称
     * @return Role
     */
    Role findByRoleName(String roleName);

    /**
     * 将分类字符串集合转化为Role泛型集合
     *
     * @param strings strings
     * @return List
     */
    List<Role> strListToRoleList(List<String> strings);
}
