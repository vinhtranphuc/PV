package com.so.common.pagination;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod.MethodSignature;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.so.exception.AppException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageBuilder<T> extends Page<T>{

	// output
	private Class<?> typeClass;
	private List<Map<String,Object>> mapList;

	// plugins
	private PageData pageData;

	@SafeVarargs
	public PageBuilder(Object mapper, String methodName, Object[] paramArr, T... typeInfo) {
		try {
			typeClass = typeInfo.getClass().getComponentType();
			// get method and validate
			Method method = getExecuteMethod(mapper.getClass(),methodName,paramArr);

			// get MapperProxy
			Proxy proxy = (Proxy) mapper;
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
			MapperProxy<?> mapperProxy = (MapperProxy<?>) invocationHandler;

			// get mapperInterface
			Field mapperInterfaceField = mapperProxy.getClass().getDeclaredField("mapperInterface");
			mapperInterfaceField.setAccessible(true);
			Class<?> mapperInterface = (Class<?>) mapperInterfaceField.get(mapperProxy);
			String mapperInterfaceName = mapperInterface.getName();

			// get SqlSession
			Field sqlSessionField = mapperProxy.getClass().getDeclaredField("sqlSession");
			sqlSessionField.setAccessible(true);
			SqlSession sqlSession = (SqlSession) sqlSessionField.get(mapperProxy);

			// get Configuration
			SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) sqlSession;
			SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
			Configuration configuration = sqlSessionFactory.getConfiguration();

			// create or get PageMapper
			MapperRegistry registry = sqlSessionFactory.getConfiguration().getMapperRegistry();
			if(!registry.hasMapper(PageMapper.class)) {
				configuration.addMapper(PageMapper.class);
			}
			PageMapper pageMapper = sqlSession.getMapper(PageMapper.class);

			// get MappedStatement
			MappedStatement mappedStatement = configuration.getMappedStatement(mapperInterfaceName+"."+methodName);

			// get parameter data
			MethodSignature methodSignature = new MethodSignature(configuration, mapperInterface, method);
			Object paramData = methodSignature.convertArgsToSqlCommandParam(paramArr);

			// create pageData
			pageData = new PageData(pageMapper, mappedStatement, paramData);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * get execute method from methods inner mapperInterface
	 * @param mapper
	 * @param methodName
	 * @param paramsData
	 * @return
	 */
	private static Method getExecuteMethod(Class<?> mapper, String methodName, Object... paramsData) {
		Class<?>[] inputParameterTypes = getParameterTypes(paramsData);
		Method[] methods = mapper.getDeclaredMethods();
		for (Method method : methods) {
			if(StringUtils.equals(methodName, method.getName())) {

				Class<?>[] targetParameterTypes = method.getParameterTypes();
				if ((Objects.isNull(inputParameterTypes) || (inputParameterTypes.length < 1))
						&& (Objects.isNull(targetParameterTypes) || (targetParameterTypes.length < 1)))
					return method;

				if ((Objects.isNull(inputParameterTypes) && (!Objects.isNull(targetParameterTypes)))
						|| (Objects.isNull(targetParameterTypes) && (!Objects.isNull(inputParameterTypes))))
					continue;

				if(inputParameterTypes.length == targetParameterTypes.length) {
					if(isNullPramTypes(inputParameterTypes))
						return method;
					boolean isParameterMapping = false;
					for(Class<?> targetType:targetParameterTypes) {
						int index = ArrayUtils.indexOf(targetParameterTypes, targetType);
						Class<?> inputType = inputParameterTypes[index];
						if(Objects.isNull(inputType) || !isParameterType(inputType,targetType)) {
							isParameterMapping = false;
							break;
						}
						isParameterMapping = true;
					}
					if(isParameterMapping)
						return method;
				}
			}
        }
		throw new AppException("com.so.common.pagination.PageBuilder : Not found mapping method at  ["
				+ mapper.getCanonicalName() + "." + methodName + "," + paramsData + "]");
	}

	/**
	 * get parameter types from parameter data
	 * @param params
	 * @return
	 */
	private static Class<?>[] getParameterTypes(final Object...params) {
		if(Objects.isNull(params) || params.length < 1)
			return null;
		Class<?>[] parameterTypes = new Class<?>[params.length];
		for(Object obj:params) {
			parameterTypes[ArrayUtils.indexOf(params, obj)] = Objects.nonNull(obj)?obj.getClass():null;
		}
		return parameterTypes;
	}

	/**
	 * check parameter type
	 * @param sourceType
	 * @param targetType
	 * @return
	 */
	public static boolean isParameterType(Class<?> sourceType, Class<?> targetType) {
		if(Objects.isNull(sourceType) || Objects.isNull(targetType))
			return false;
		return sourceType.equals(targetType) || targetType.isAssignableFrom(sourceType);
	}

	public static boolean isNullPramTypes(Class<?>[] parameterTypes) {
		for(Object obj:parameterTypes) {
			if(Objects.nonNull(obj))
				return false;
		}
		return true;
	}

	/**
	 * building with page
	 * @param page
	 * @return
	 */
	public PageBuilder<T> withPage(int page) {
		this.page = page;
		return this;
	}

	/**
	 * building with pageSize
	 * @param pageSize
	 * @return
	 */
	public PageBuilder<T> withPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * load page data
	 */
	private void loadPageData() {
		this.totalElements = pageData.getTotalElements();
		if(this.pageSize < 1 || this.page < 1) {
			this.isLast = true;
			this.mapList = new ArrayList<Map<String,Object>>();
		} else {
			int startList = (page-1)*this.pageSize;
			this.mapList = pageData.getList(startList,this.pageSize);
			this.totalPages = mapList.size()<1?1:(totalElements/pageSize + ((totalElements%pageSize)>0?1:0));
			this.isLast = (this.totalPages == page);
		}
	}

	/**
	 * build data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> build() {
		loadPageData();
		List<?> resultList = this.mapList.stream().map(t -> {
			return mapIgnoreNull(t, typeClass);
		}).collect(Collectors.toList());
		this.mapList = (List<Map<String, Object>>) resultList;
		return new Page<T>(this);
	}

	/**
	 * ignore null
	 * @param source
	 * @param clazz
	 * @return
	 */
	private Object mapIgnoreNull(Object source, Class<?> clazz) {
		ModelMapper modelMapper = new ModelMapper();
		Logger logger = LoggerFactory.getLogger(Page.class);
		try {
			return modelMapper.map(source, clazz);
		} catch (NullPointerException e) {
			logger.warn("Cannot execute model mapper because " + e + " occurred at source or destination!");
		} catch (IllegalArgumentException e) {
			logger.warn("Cannot execute model mapper : " + e.getMessage());
		}
		return null;
	}
}
