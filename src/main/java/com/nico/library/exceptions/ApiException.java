package com.nico.library.exceptions;

import java.time.ZonedDateTime;

public record ApiException(ZonedDateTime timestamp,
                           String message,
                           String errorCode,
                           String path) {
}