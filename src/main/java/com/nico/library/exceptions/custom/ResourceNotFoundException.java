package com.nico.library.exceptions.custom;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException
{
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    private final String username;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue)
    {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.username = null;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue, String username)
    {
        super(String.format("%s with %s '%s' not found for user '%s'", resourceName, fieldName, fieldValue, username));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.username = username;
    }
}
