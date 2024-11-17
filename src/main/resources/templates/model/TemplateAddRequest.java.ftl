package ${packageName}.model.dto.${dataKey};

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
* 创建${dataName}请求
*
* @author zbn
* @date 2024/10/24
*/
@Data
public class ${upperDataKey}AddRequest implements Serializable {

/**
* 标题
*/
private String title;

/**
* 内容
*/
private String content;

/**
* 标签列表
*/
private List
<String> tags;

    @Serial
    private static final long serialVersionUID = 1L;
    }