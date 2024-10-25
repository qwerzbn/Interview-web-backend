package com.zbn.Interview.service;

import com.zbn.Interview.model.entity.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbn.Interview.model.entity.User;

/**
 * 帖子点赞服务
 *
 * @author zbn
 * @date 2024/10/23
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
