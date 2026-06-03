package com.github.jroom36.api.controller;

import java.io.InputStream;
import java.util.UUID;

import com.github.jroom36.storage.files.FileInfo;
import com.github.jroom36.storage.files.FilesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilesController.class)
class FilesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FilesService filesService;

    @Test
    @DisplayName("POST /api/v1/files/upload uploads file and returns FileInfoView")
    void upload_whenValidFile_thenReturnsFileInfoView() throws Exception {
        var fileId = UUID.randomUUID();
        var mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());

        when(filesService.uploadFile(eq("test.txt"), any(InputStream.class), eq(null)))
                .thenReturn(new FileInfo(fileId, "test.txt", 5L, null));

        mockMvc.perform(multipart("/api/v1/files/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fileId.toString()))
                .andExpect(jsonPath("$.name").value("test.txt"))
                .andExpect(jsonPath("$.sizeBytes").value(5));

        verify(filesService).uploadFile(eq("test.txt"), any(InputStream.class), eq(null));
    }

    @Test
    @DisplayName("POST /api/v1/files/upload uploads file to specific folder")
    void upload_whenValidFileWithFolderId_thenReturnsFileInfoView() throws Exception {
        var fileId = UUID.randomUUID();
        var folderId = UUID.randomUUID();
        var mockFile = new MockMultipartFile("file", "doc.pdf", "application/pdf", "pdf content".getBytes());

        when(filesService.uploadFile(eq("doc.pdf"), any(InputStream.class), eq(folderId)))
                .thenReturn(new FileInfo(fileId, "doc.pdf", 11L, folderId));

        mockMvc.perform(multipart("/api/v1/files/upload").file(mockFile)
                        .param("folderId", folderId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fileId.toString()))
                .andExpect(jsonPath("$.folderId").value(folderId.toString()));

        verify(filesService).uploadFile(eq("doc.pdf"), any(InputStream.class), eq(folderId));
    }

    @Test
    @DisplayName("GET /api/v1/files/{id} downloads file")
    void downloadFile_whenFileExists_thenReturnsStreamingResponse() throws Exception {
        var fileId = UUID.randomUUID();

        when(filesService.getFileInfo(fileId))
                .thenReturn(new FileInfo(fileId, "report.pdf", 1024L, null));

        doAnswer(invocation -> {
            var os = invocation.getArgument(1, java.io.OutputStream.class);
            os.write("fake content".getBytes());
            return null;
        }).when(filesService).sendTo(eq(fileId), any());

        mockMvc.perform(get("/api/v1/files/{id}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"report.pdf\""));

        verify(filesService).getFileInfo(fileId);
    }

    @Test
    @DisplayName("GET /api/v1/files returns paginated file list")
    void getFiles_whenFilesExist_thenReturnsPageOfFiles() throws Exception {
        var files = new PageImpl<>(
                java.util.List.of(new FileInfo(UUID.randomUUID(), "a.txt", 1L, null)),
                PageRequest.of(0, 10), 1
        );
        when(filesService.getFiles(any())).thenReturn(files);

        mockMvc.perform(get("/api/v1/files")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("a.txt"))
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(filesService).getFiles(any());
    }

    @Test
    @DisplayName("GET /api/v1/files returns empty page when no files")
    void getFiles_whenNoFiles_thenReturnsEmptyPage() throws Exception {
        var files = new PageImpl<FileInfo>(java.util.List.of(), PageRequest.of(0, 10), 0);
        when(filesService.getFiles(any())).thenReturn(files);

        mockMvc.perform(get("/api/v1/files")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.numberOfElements").value(0));

        verify(filesService).getFiles(any());
    }
}
