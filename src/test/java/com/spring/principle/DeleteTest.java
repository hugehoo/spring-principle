package com.spring.principle;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DeleteTest {
    @Test
    void DELETE_NOT_EXISTING_FILE() {
        File file = new File("./test");
        boolean delete = file.delete();
        assertFalse(delete);
    }

    @Test
    void FILE_SAFE_DELETE() throws IOException {
        File file = new File("test");
        Path path = Path.of(file.getPath());
        Files.delete(path);
        assertFalse(file.exists());
    }

    @Test
    void DELETE_WHOLE_DIRECTORY() throws IOException {
        File file = new File("src/test/java/com/tripbtoz/folder/test");
        File parentFile = file.getParentFile();
        FileUtils.cleanDirectory(parentFile);
        Files.delete(Path.of(parentFile.getPath()));
    }


}
