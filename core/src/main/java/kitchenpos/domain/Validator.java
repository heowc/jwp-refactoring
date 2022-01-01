package kitchenpos.domain;

@FunctionalInterface
public interface Validator<T> {

    void validate(T t);
}