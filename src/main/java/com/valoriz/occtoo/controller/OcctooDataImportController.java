package com.valoriz.occtoo.controller;

import com.valoriz.occtoo.service.OcctooDataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/occtoo")
public class OcctooDataImportController {

    @Autowired OcctooDataImportService occtooDataImportService;

    @GetMapping()
    public ResponseEntity<String> importDataToOcctoo() {
        occtooDataImportService.importDataToOcctoo();
         return ResponseEntity.ok("Successfully Imported Data!!") ;
    }

}
