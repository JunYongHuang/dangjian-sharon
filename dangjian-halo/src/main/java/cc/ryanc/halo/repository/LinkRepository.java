package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 *     友情链接持久层
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
public interface LinkRepository extends JpaRepository<Link, Long> {
}
