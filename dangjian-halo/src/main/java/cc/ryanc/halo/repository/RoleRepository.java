package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Role;
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
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param roleUrl roleUrl 文章url
     * @return Role
     */
    Role findRoleByRoleUrl(String roleUrl);

    /**
     * 根据分类名称查询
     *
     * @param roleName 分类名称
     * @return Role
     */
    Role findRoleByRoleName(String roleName);

    /**
     * 根据分类查询文章总数
     *
     * @return Long
     */
    @Query(value = "select count(1) from halo_posts_rolegories where role_id=:roleId", nativeQuery = true)
    Long getRolePostCount(@Param("roleId") Long roleId);
}
