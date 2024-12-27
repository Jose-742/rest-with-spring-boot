package br.com.erudio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"fileName", "fileDownloadUri", "fileType", "Size"})
public class UploadFileResponseVO implements Serializable {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long Size;
}
