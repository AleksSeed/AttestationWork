package ru.skypro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.characteristic.Socks;
import ru.skypro.service.SocksService;

@RequestMapping("/sock")
@RestController
@Tag(name = "Носки ", description = "CRUD - операции и другие эндпоинты для отпуска со склада, удаление брачных, и добавления носков")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping
    @Operation(description = "Добавление носков на склад ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Носки были добавлены",
                  content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Socks.class))}
            )
    })
    public ResponseEntity<Socks> createSocks(@RequestBody Socks socks) {
        socksService.addSocks(socks);
        return ResponseEntity.ok(socks);
    }

    @GetMapping
    @Operation(description = "Запрос на кол-во носков на складе")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Носки были найдены",
                  content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Socks.class))}
        )
    })
    public ResponseEntity<Socks> getSock(@RequestParam int sockId) {
        Socks socks = socksService.getSocks(sockId);
        if (socks == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(socks);
    }

    @PutMapping("/{id}")
    @Operation(description = "Забрать товар со склада ")
    public ResponseEntity<Socks> editSock(@PathVariable long id, @RequestBody Socks newSocks) {
        if (socksService.getSocks(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Socks socks = socksService.editSocks(id, newSocks);
        return ResponseEntity.ok(socks);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Списание брака по товару")
    public ResponseEntity<Void> deleteSock(@PathVariable long id) {
        if (socksService.deleteSock(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
