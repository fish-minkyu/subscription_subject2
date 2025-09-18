package com.subject2.subscription.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PatchMapping("/{username}")
    public void updateAuthorities(@PathVariable String username, @RequestBody Map<String, String> requestBody) {
        String newRole = requestBody.get("newRole");
        String authoritiesToAssign = requestBody.get("authoritiesToAssign");
        adminService.updateAuthorities(username, newRole, authoritiesToAssign);
    }
}
