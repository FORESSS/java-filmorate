package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInvalidRequestException(InvalidRequestException e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }

   /* @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }*/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse("Data not validated", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IdNotFoundException.class)
    public ErrorResponse handleIdNotFoundExceptions(IdNotFoundException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(ex.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ErrorResponse handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        log.info("{}: {}", ex.getMessage(), ex.getObject());
        return new ErrorResponse(ex.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    public ErrorResponse handleInternalServerException(InternalServerException ex) {
        log.info("{}", ex.getMessage());
        return new ErrorResponse(ex.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("{}", ex.getMessage());
        return new ErrorResponse(ex.getMessage(), null);
    }
}