package com.zbn.Interview.model.dto.file;

import java.io.Serializable;
import lombok.Data;

/**
 * 文件上传请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;

       @Serial
    private static final long serialVersionUID = 1L;
}