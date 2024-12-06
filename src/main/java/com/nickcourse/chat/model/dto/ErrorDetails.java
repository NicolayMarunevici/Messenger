package com.nickcourse.chat.model.dto;

import java.util.Date;

public record ErrorDetails(
  Date timestamp,
  String message,
  String details
) {

}
