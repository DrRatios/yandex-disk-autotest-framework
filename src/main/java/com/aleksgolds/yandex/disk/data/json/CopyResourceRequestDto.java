package com.aleksgolds.yandex.disk.data.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CopyResourceRequestDto {

    @JsonProperty(value = "from", required = true)
    private String from;

    @JsonProperty(value = "path", required = true)
    private String path;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("force_async")
    private Boolean forceAsync;

    @JsonProperty("overwrite")
    private Boolean overwrite;
}
