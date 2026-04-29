package com.supermarket.pos.service;

/**
 * BaseService abstract class for business logic operations.
 * Provides common service methods for all service classes.
 * All services inherit validation and error handling patterns from this base class.
 */
public abstract class BaseService {

    /**
     * Validate that a string is not null or empty
     * @param value String to validate
     * @param fieldName Name of field for error message
     * @throws IllegalArgumentException if validation fails
     */
    protected static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }

    /**
     * Validate that a number is positive
     * @param value Number to validate
     * @param fieldName Name of field for error message
     * @throws IllegalArgumentException if validation fails
     */
    protected static void validatePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0");
        }
    }

    /**
     * Validate that an integer is positive
     * @param value Integer to validate
     * @param fieldName Name of field for error message
     * @throws IllegalArgumentException if validation fails
     */
    protected static void validatePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0");
        }
    }

    /**
     * Validate that an object is not null
     * @param object Object to validate
     * @param fieldName Name of field for error message
     * @throws IllegalArgumentException if validation fails
     */
    protected static void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}
