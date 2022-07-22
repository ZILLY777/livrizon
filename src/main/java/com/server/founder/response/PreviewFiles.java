package com.server.founder.response;

import com.server.founder.model.FileView;
import lombok.Data;

import java.util.List;
@Data
public class PreviewFiles {
    int number;
    List<FileView> files;

    public PreviewFiles() {
    }

}
