package cc.ryanc.halo.utils;

import cc.ryanc.halo.model.domain.Comment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * 拼装评论
 * </pre>
 *
 * @author : HJY
 * @date : 2018/7/12
 */
public class CommentUtil {

    /**
     * 获取组装好的评论
     *
     * @param commentsRoot commentsRoot
     * @return List
     */
    public static List<Comment> getComments(List<Comment> commentsRoot) {
        if (CollectionUtils.isEmpty(commentsRoot)) {
            return Collections.emptyList();
        }

        List<Comment> commentsResult = new ArrayList<>();

        for (Comment comment : commentsRoot) {
            if (comment.getCommentParent() == 0) {
                commentsResult.add(comment);
            }
        }

        for (Comment comment : commentsResult) {
            comment.setChildComments(getChild(comment.getCommentId(), commentsRoot));
        }
        // 集合倒序，最新的评论在最前面
        Collections.reverse(commentsResult);
        return commentsResult;
    }

    /**
     * 获取评论的子评论
     *
     * @param id           评论编号
     * @param commentsRoot commentsRoot
     * @return List
     */
    private static List<Comment> getChild(Long id, List<Comment> commentsRoot) {
        Assert.notNull(id, "comment id must not be null");

        if (CollectionUtils.isEmpty(commentsRoot)) {
            return null;
        }

        List<Comment> commentsChild = new ArrayList<>();
        for (Comment comment : commentsRoot) {
            if (comment.getCommentParent() != 0) {
                if (comment.getCommentParent().equals(id)) {
                    commentsChild.add(comment);
                }
            }
        }
        for (Comment comment : commentsChild) {
            if (comment.getCommentParent() != 0) {
                comment.setChildComments(getChild(comment.getCommentId(), commentsRoot));
            }
        }
        if (commentsChild.size() == 0) {
            return null;
        }
        return commentsChild;
    }
}
