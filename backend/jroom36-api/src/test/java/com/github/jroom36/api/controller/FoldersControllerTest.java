package com.github.jroom36.api.controller;

import java.util.List;
import java.util.UUID;

import com.github.jroom36.api.dto.CreateFolderView;
import com.github.jroom36.storage.folders.Folder;
import com.github.jroom36.storage.folders.FolderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoldersController.class)
class FoldersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FolderService folderService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /api/v1/folders returns folder tree")
    void getFolders_whenFoldersExist_thenReturnsFolderCollection() throws Exception {
        var folders = List.of(
                new Folder(UUID.randomUUID(), "root", null, List.of(
                        new Folder(UUID.randomUUID(), "child", UUID.randomUUID(), List.of())
                ))
        );
        when(folderService.buildFoldersTree()).thenReturn(folders);

        mockMvc.perform(get("/api/v1/folders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("root"))
                .andExpect(jsonPath("$[0].nestedFolders[0].name").value("child"));

        verify(folderService).buildFoldersTree();
    }

    @Test
    @DisplayName("GET /api/v1/folders returns empty list when no folders")
    void getFolders_whenNoFolders_thenReturnsEmptyCollection() throws Exception {
        when(folderService.buildFoldersTree()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/folders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/folders creates folder without parent")
    void createFolder_whenValidInputWithoutParent_thenCreatesFolderAndReturnsFolders() throws Exception {
        var folders = List.of(
                new Folder(UUID.randomUUID(), "new-folder", null, List.of())
        );
        when(folderService.buildFoldersTree()).thenReturn(folders);

        var request = new CreateFolderView("new-folder");

        mockMvc.perform(post("/api/v1/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("new-folder"));

        verify(folderService).createFolder("new-folder");
    }

    @Test
    @DisplayName("POST /api/v1/folders/{parentFolderId} creates folder with parent")
    void createFolder_whenValidInputWithParent_thenCreatesFolderInParentAndReturnsFolders() throws Exception {
        var parentId = UUID.randomUUID();
        var folders = List.of(
                new Folder(UUID.randomUUID(), "child-folder", parentId, List.of())
        );
        when(folderService.buildFoldersTree()).thenReturn(folders);

        var request = new CreateFolderView("child-folder");

        mockMvc.perform(post("/api/v1/folders/{parentFolderId}", parentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("child-folder"));

        verify(folderService).createFolder("child-folder", parentId);
    }
}
