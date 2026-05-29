package com.resumeforge.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Error details nested within API response.
 *
 * Provides detailed error information including error code and field-level validation errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {

    /**
     * Error code for categorization
     */
    private String code;

    /**
     * Detailed error description
     */
    private String details;

    /**
     * Field-level validation errors
     */
    private Map<String, String> fieldErrors;

    /**
     * Path that caused the error
     */
    private String path;
}
