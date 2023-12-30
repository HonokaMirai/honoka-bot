package com.honoka.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honoka.dao.entity.UmaGachaPool;
import com.honoka.dao.mapper.UmaGachaPoolMapper;
import com.honoka.util.MybatisUtil;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
public class UmamusumeConfig {

    public static List<UmaGachaPool> gachaPoolList;

    public static Integer price = 300;

    public static class Command {
        /**
         * 刷新
         */
        public static final String REFRESH = "refresh";

        /**
         * 抽卡
         */
        public static final String GACHA = "gacha";

        /**
         * 训练
         */
        public static final String TRAIN = "train";

        /**
         * 比赛
         */
        public static final String MATCH = "match";

        /**
         * 对战
         */
        public static final String BATTLE = "battle";

    }

    public static class Type {

        /**
         * 添加
         */
        public static final String ADD = "add";

        /**
         * 减少
         */
        public static final String REDUCE = "reduce";

    }

    public static class Reply {

        /**
         * 未知指令
         */
        public static final String UNKNOWN = "未知指令~";

        /**
         * 未知指令
         */
        public static final String TIMES_ERROR = "抽卡次数错误，请选择1-10之间的数字~";

    }


    public static void initGachaPool() {
        // 从数据库中获取抽卡池
        List<UmaGachaPool> poolList = MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(UmaGachaPoolMapper.class).selectList(new QueryWrapper<UmaGachaPool>()
                        .eq(UmaGachaPool.STATUS, 1))
        );
        if (CollectionUtil.isNotEmpty(poolList)) {
            gachaPoolList = poolList;
        }

        BotConfig.logger.info("初始化抽卡池成功: " + JSONObject.toJSONString(gachaPoolList));
    }

    /**
     * 从gachaPoolList中随机取出一个
     */
    public static UmaGachaPool getRandomGacha() {
        if (CollectionUtil.isEmpty(gachaPoolList)) {
            // 从数据库中获取抽卡池
            initGachaPool();
        }
        // 获取gachaPool总权重
        Integer totalWeight = gachaPoolList.stream().mapToInt(UmaGachaPool::getUmaProbability).sum();
        // 生成一个随机数
        Integer random = new Random().nextInt(totalWeight) + 1;
        Integer weight = 0;
        for (int i = 0; i < gachaPoolList.size(); i++) {
            weight += gachaPoolList.get(i).getUmaProbability();
            if (random <= weight) {
                return gachaPoolList.get(i);
            }
        }
        return null;

    }

    /**
     * 从gachaPoolList中随机取出times个
     * @param times
     * @return
     */
    public static List<UmaGachaPool> getRandomGachaList(Integer times) {
        List<UmaGachaPool> gachaResultList = CollectionUtil.newArrayList();
        for (int i = 0; i < times; i++) {
            gachaResultList.add(getRandomGacha());
        }
        return gachaResultList;
    }

}
