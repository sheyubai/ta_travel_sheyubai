package cn.wolfcode.wolf2w.business.service;

import cn.wolfcode.wolf2w.business.query.SearchQuery;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;

public interface ISearchService {
    /*
    * 参数1:mysql对应实体类得类型
    * 参数2:es中对应实体类得类型
    * 参数3:查询对象
    * 参数4:可变长参,表示查询那些列
    * 返回值:mysql对应实体类类型得分页对象
    * */
    <T> Page<T> searchHighLight(Class<T> clazz, Class<?> esClazz, SearchQuery qo, String... fields) throws InvocationTargetException, IllegalAccessException;
}
