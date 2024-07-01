package com.example.QuestionPortalBackend.util;

public interface MapperInterface<Entity, DTO> {
    Entity toEntity(DTO dto);

    DTO toDTO(Entity entity);

}
