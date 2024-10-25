package com.zbn.Interview.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}