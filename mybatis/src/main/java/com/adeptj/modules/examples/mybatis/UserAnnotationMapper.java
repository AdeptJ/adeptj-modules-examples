package com.adeptj.modules.examples.mybatis;

import com.adeptj.modules.examples.mybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAnnotationMapper {

    @ResultMap("com.adeptj.modules.examples.mybatis.UserXmlMapper.userMap")
    @Select("select * from users where id = #{id}")
    User findById(Object id);
}
