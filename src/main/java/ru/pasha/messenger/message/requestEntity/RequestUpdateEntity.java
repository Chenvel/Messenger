package ru.pasha.messenger.message.requestEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestUpdateEntity {

    private String message;
    private String date;
}
