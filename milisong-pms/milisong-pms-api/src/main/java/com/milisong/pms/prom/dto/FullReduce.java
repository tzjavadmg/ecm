package com.milisong.pms.prom.dto;

import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 满减档位
 *
 * @author sailor wang
 * @date 2018/11/8 12:42 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullReduce {

    private Byte isShare;// 是否同享

    // 开始时间
    private Date startDate;

    // 结束时间
    private Date endDate;

    private List<FullReducePair> fullReducePairList;

    public List<FullReducePair> getFullReducePairList() {
        if (CollectionUtils.isNotEmpty(fullReducePairList)) {
            fullReducePairList.sort(new Comparator<FullReducePair>() {
                @Override
                public int compare(FullReducePair o1, FullReducePair o2) {
                    return Ints.compare(o1.getLevel(), o2.getLevel());
                }
            });
        }
        return fullReducePairList;
    }

    public void setFullReducePairList(List<FullReducePair> fullReducePairList) {
        this.fullReducePairList = fullReducePairList;
    }

}