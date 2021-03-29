package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <pre>
 *     附件持久层
 * </pre>
 *
 * @author : HJY
 * @date : 2018/1/10
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByWhiteIdAndPostId(Long whiteId, Long postId);

    @Query(value = "from Attachment where whiteId = :whiteId and ( postId=:postId or postId = 0)")
    List<Attachment> findByWhiteIdAndPostIdOrZero(Long whiteId, Long postId);

    @Query("select a from Attachment a where a.whiteId is null or a.whiteId = 0")
    Page<Attachment> findAllByAdmin(Pageable pageable);

    List<Attachment> findByPostId(Long postId);
}
