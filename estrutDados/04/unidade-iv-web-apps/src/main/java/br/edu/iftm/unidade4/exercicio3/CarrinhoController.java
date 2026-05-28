package br.edu.iftm.unidade4.exercicio3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final ServicoCarrinho servicoCarrinho;

    public CarrinhoController(ServicoCarrinho servicoCarrinho) {
        this.servicoCarrinho = servicoCarrinho;
    }

    @PostMapping("/{usuarioId}/item")
    public ResponseEntity<Void> adicionarItem(@PathVariable String usuarioId,
                                              @RequestBody ItemCarrinhoDto dto) {
        servicoCarrinho.adicionarItem(usuarioId, dto.getProdutoId(), dto.getQuantidade());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Map<String, Integer>> buscarCarrinho(@PathVariable String usuarioId) {
        return ResponseEntity.ok(servicoCarrinho.buscarCarrinho(usuarioId));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> limparCarrinho(@PathVariable String usuarioId) {
        servicoCarrinho.limparCarrinho(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
