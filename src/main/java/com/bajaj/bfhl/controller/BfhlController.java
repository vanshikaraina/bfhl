package com.bajaj.bfhl.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import com.bajaj.bfhl.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class BfhlController {

    private static final String EMAIL = "vanshika0995.be23@chitkara.edu.in";
    @Autowired
    private OpenAIService openAIService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> res = new HashMap<>();
        res.put("is_success", true);
        res.put("official_email", EMAIL);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/bfhl")
    public ResponseEntity<Map<String, Object>> bfhl(@RequestBody(required = false) Map<String, Object> body) {

        Map<String, Object> res = new HashMap<>();
        res.put("official_email", EMAIL);

        try {
            if (body == null || body.isEmpty()) {
                res.put("is_success", false);
                res.put("data", "Request body is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            if (body.size() != 1) {
                res.put("is_success", false);
                res.put("data", "Only one input key is allowed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            if (body.containsKey("fibonacci")) {
                int n = (int) body.get("fibonacci");
                if (n < 0) throw new IllegalArgumentException("Invalid fibonacci input");
                res.put("is_success", true);
                res.put("data", fibonacci(n));
            }

            else if (body.containsKey("prime")) {
                List<Integer> nums = (List<Integer>) body.get("prime");
                res.put("is_success", true);
                res.put("data", primes(nums));
            }

            else if (body.containsKey("lcm")) {
                List<Integer> nums = (List<Integer>) body.get("lcm");
                res.put("is_success", true);
                res.put("data", lcm(nums));
            }

            else if (body.containsKey("hcf")) {
                List<Integer> nums = (List<Integer>) body.get("hcf");
                res.put("is_success", true);
                res.put("data", hcf(nums));
            }

            else if (body.containsKey("AI")) {
                String question = body.get("AI").toString();
                String answer = openAIService.askAI(question);
                res.put("is_success", true);
                res.put("data", answer);
            }

            else {
                res.put("is_success", false);
                res.put("data", "Invalid input key");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.put("is_success", false);
            res.put("data", "Invalid input format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }

    private List<Integer> fibonacci(int n) {
        List<Integer> list = new ArrayList<>();
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            list.add(a);
            int c = a + b;
            a = b;
            b = c;
        }
        return list;
    }

    private List<Integer> primes(List<Integer> nums) {
        List<Integer> res = new ArrayList<>();
        for (int n : nums) {
            if (isPrime(n)) res.add(n);
        }
        return res;
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int lcm(List<Integer> nums) {
        int ans = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            ans = ans * nums.get(i) / gcd(ans, nums.get(i));
        }
        return ans;
    }

    private int hcf(List<Integer> nums) {
        int ans = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            ans = gcd(ans, nums.get(i));
        }
        return ans;
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
}