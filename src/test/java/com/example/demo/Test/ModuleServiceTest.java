package com.example.demo.Test;

import com.example.demo.Models.Course;
import com.example.demo.Models.Module;
import com.example.demo.Repository.ModuleRepository;
import com.example.demo.Service.ModuleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModuleServiceTest {

    private final ModuleRepository moduleRepository = Mockito.mock(ModuleRepository.class);
    private final ModuleService moduleService = new ModuleService(moduleRepository);

    @Test
    void testGetAllModules() {
        Module m1 = new Module();
        Module m2 = new Module();

        when(moduleRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Module> modules = moduleService.getAllModules();

        assertEquals(2, modules.size());
        verify(moduleRepository, times(1)).findAll();
    }

    @Test
    void testSaveModule() {
        Module module = new Module();
        module.setId(1L);
        module.setTitle("Intro to Java");
        module.setDescription("Basics of Java");

        when(moduleRepository.save(module)).thenReturn(module);

        Module saved = moduleService.saveModule(module);

        assertNotNull(saved);
        assertEquals("Intro to Java", saved.getTitle());
        verify(moduleRepository, times(1)).save(module);
    }

    @Test
    void testGetModuleById_Found() {
        Module module = new Module();
        module.setId(1L);
        module.setTitle("Module 1");

        when(moduleRepository.findById(1L)).thenReturn(Optional.of(module));

        Module result = moduleService.getModuleById(1L);

        assertNotNull(result);
        assertEquals("Module 1", result.getTitle());
        verify(moduleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetModuleById_NotFound() {
        when(moduleRepository.findById(999L)).thenReturn(Optional.empty());

        Module result = moduleService.getModuleById(999L);

        assertNull(result);
        verify(moduleRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateModule_WhenFound() {
        Module existing = new Module();
        existing.setId(1L);
        existing.setTitle("Old Module");
        existing.setDescription("Old Desc");
        existing.setCourse(new Course());

        Module updated = new Module();
        updated.setTitle("Updated Module");
        updated.setDescription("Updated Desc");
        updated.setCourse(new Course());

        when(moduleRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(moduleRepository.save(existing)).thenReturn(existing);

        Module result = moduleService.updateModule(1L, updated);

        assertNotNull(result);
        assertEquals("Updated Module", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        verify(moduleRepository).findById(1L);
        verify(moduleRepository).save(existing);
    }

    @Test
    void testUpdateModule_WhenNotFound() {
        Module updated = new Module();
        updated.setTitle("Missing");
        updated.setDescription("No Module");

        when(moduleRepository.findById(999L)).thenReturn(Optional.empty());

        Module result = moduleService.updateModule(999L, updated);

        assertNull(result);
        verify(moduleRepository, never()).save(any());
    }

    @Test
    void testDeleteModule() {
        doNothing().when(moduleRepository).deleteById(1L);

        moduleService.deleteModule(1L);

        verify(moduleRepository, times(1)).deleteById(1L);
    }
}
