package com.example.thymeleaf_demo.controller.admin;

import com.example.thymeleaf_demo.dto.CategoryDto;
import com.example.thymeleaf_demo.entity.Category;
import com.example.thymeleaf_demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String search(Model model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        Page<Category> resultPage;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 2);
            int end = Math.min(page + 2, totalPages);

            if (totalPages > 5) {
                if (end == totalPages) start = end - 4;
                else if (start == 1) end = start + 4;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        return "admin/categories/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        CategoryDto dto = new CategoryDto();
        dto.setIsEdit(false);
        model.addAttribute("category", dto);
        return "admin/categories/addOrEdit";
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(Model model, @PathVariable("categoryId") Long categoryId, RedirectAttributes redirectAttributes) {
        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isPresent()) {
            CategoryDto dto = new CategoryDto();
            BeanUtils.copyProperties(opt.get(), dto);
            dto.setIsEdit(true);
            model.addAttribute("category", dto);
            return "admin/categories/addOrEdit";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy danh mục!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(Model model,
                               @Valid @ModelAttribute("category") CategoryDto dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/categories/addOrEdit";
        }

        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);

        categoryService.save(entity);

        String message = dto.getIsEdit() ? "Cập nhật danh mục thành công!" : "Thêm mới danh mục thành công!";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Long categoryId, RedirectAttributes redirectAttributes) {
        if (categoryService.findById(categoryId).isPresent()) {
            categoryService.deleteById(categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy danh mục để xóa!");
        }
        return "redirect:/admin/categories";
    }
}

