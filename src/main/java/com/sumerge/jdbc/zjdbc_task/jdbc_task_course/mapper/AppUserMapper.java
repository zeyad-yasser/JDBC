package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.AppUser;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.AppUserDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.AppUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO toDto(AppUser user);
    AppUser toEntity(AppUserDTO dto);
}
