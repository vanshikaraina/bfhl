package com.bajaj.bfhl.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import com.bajaj.bfhl.service.OpenAIService;

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
    public ResponseEntity<Map<String, Object>> bfhl(
            @RequestBody(required = false) Map<String, Object> body) {

        Map<String, Object> res = new HashMap<>();
        res.put("official_email", EMAIL);

        try {

            if (body == null || body.size() != 1) {
                return badRequest(res);
            }

            if (body.containsKey("fibonacci")) {
                Object val = body.get("fibonacci");
                if (!(val instanceof Number)) return badRequest(res);
                int n = ((Number) val).intValue();
                if (n < 0 || n > 1000) return badRequest(res);

                res.put("is_success", true);
                res.put("data", fibonacci(n));
                return ResponseEntity.ok(res);
            }

            if (body.containsKey("prime")) {
                List<Integer> nums = parseList(body.get("prime"));
                res.put("is_success", true);
                res.put("data", primes(nums));
                return ResponseEntity.ok(res);
            }

            if (body.containsKey("lcm")) {
                List<Integer> nums = parseList(body.get("lcm"));
                res.put("is_success", true);
                res.put("data", lcm(nums));
                return ResponseEntity.ok(res);
            }

            if (body.containsKey("hcf")) {
                List<Integer> nums = parseList(body.get("hcf"));
                res.put("is_success", true);
                res.put("data", hcf(nums));
                return ResponseEntity.ok(res);
            }

            if (body.containsKey("AI")) {
                Object val = body.get("AI");
                if (!(val instanceof String)) return badRequest(res);

                res.put("is_success", true);
                res.put("data", openAIService.askAI(val.toString()));
                return ResponseEntity.ok(res);
            }

            return badRequest(res);

        } catch (Exception e) {
            return badRequest(res);
        }
    }

    private ResponseEntity<Map<String, Object>> badRequest(Map<String, Object> res) {
        res.put("is_success", false);
        res.put("data", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    private List<Integer> parseList(Object obj) {
        if (!(obj instanceof List)) throw new IllegalArgumentException();
        List<?> raw = (List<?>) obj;
        if (raw.isEmpty() || raw.size() > 1000) throw new IllegalArgumentException();

        List<Integer> nums = new ArrayList<>();
        for (Object o : raw) {
            if (!(o instanceof Number)) throw new IllegalArgumentException();
            nums.add(((Number) o).intValue());
        }
        return nums;
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
            ans = Math.abs(ans / gcd(ans, nums.get(i)) * nums.get(i));
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
        a = Math.abs(a);
        b = Math.abs(b);
        if (b == 0) return a;
        return gcd(b, a % b);
    }
}