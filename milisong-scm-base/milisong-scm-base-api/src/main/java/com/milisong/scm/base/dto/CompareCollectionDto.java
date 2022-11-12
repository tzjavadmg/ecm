package com.milisong.scm.base.dto;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 集合类型的属性的比较包装类
 * @author yangzhilong
 *
 * @param <T>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompareCollectionDto<T> implements Serializable {
	private static final long serialVersionUID = -4977550220859897197L;
	private String filedName;
	private Collection<T> oldCollection;
	private Collection<T> newCollection;
}
