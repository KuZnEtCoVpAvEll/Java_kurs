package com.example.demo.Service;

import com.example.demo.Models.Module;
import com.example.demo.Repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Module saveModule(Module module) {
        return moduleRepository.save(module);
    }

    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public Module updateModule(Long id, Module updatedModule) {
        Module existing = moduleRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(updatedModule.getTitle());
            existing.setDescription(updatedModule.getDescription());
            existing.setCourse(updatedModule.getCourse());
            return moduleRepository.save(existing);
        }
        return null;
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }
}

