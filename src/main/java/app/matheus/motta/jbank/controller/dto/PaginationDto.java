package app.matheus.motta.jbank.controller.dto;

public record PaginationDto(Integer page,
                            Integer pageSize,
                            Long totalElements,
                            Integer totalPages) {
}
