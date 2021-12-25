package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    private static final String NOT_FOUND = "해당 정보는 존재하지 않습니다.";

    public NotFoundException(final HttpStatus httpStatus) {
        super(httpStatus, NOT_FOUND);
    }
}