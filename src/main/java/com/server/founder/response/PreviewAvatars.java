package com.server.founder.response;

import com.server.founder.model.File;
import com.server.founder.model.FileView;
import lombok.Data;

import java.util.List;
@Data
public class PreviewAvatars {
    int number;
    List<File> files;

    public PreviewAvatars() {
    }

}
