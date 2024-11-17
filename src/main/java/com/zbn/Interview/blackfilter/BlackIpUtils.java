package com.zbn.Interview.blackfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

@Slf4j
public class BlackIpUtils {
    public static BitMapBloomFilter bloomFilter;

    // 判断IP是否在黑名单中
    public static boolean isBlackIp(String ip) {
        return bloomFilter.contains(ip);
    }

    // 重建IP黑名单（布隆过滤器不支持删除元素，配置更新就要重建）
    public static void rebuildBlackIp(String configInfo) {
        if (StrUtil.isBlank(configInfo)) {
            configInfo = "{}";
        }
        // 解析Yaml文件
        Yaml yaml = new Yaml();
        Map<String, List<String>> map = yaml.loadAs(configInfo, Map.class);
        List<String> blackIpList = map.get("blackIpList");
        // 加锁防止并发
        synchronized (BlackIpUtils.class) {
            if (CollectionUtils.isNotEmpty(blackIpList)) {
                BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(958506);
                for (String blackIp : blackIpList) {
                    if (StringUtils.isNotBlank(blackIp)) {
                        bitMapBloomFilter.add(blackIp);
                    }
                }
                bloomFilter = bitMapBloomFilter;
            } else {
                bloomFilter = new BitMapBloomFilter(100);
            }
        }
    }
}
