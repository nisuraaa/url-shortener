package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsResponse {
    private int count;

    public StatsResponse(int count) {
        this.count = count;
    }
}
