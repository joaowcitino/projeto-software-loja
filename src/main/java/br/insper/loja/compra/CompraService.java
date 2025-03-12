package br.insper.loja.compra;

import br.insper.loja.evento.EventoService;
import br.insper.loja.produto.Produto;
import br.insper.loja.produto.ProdutoService;
import br.insper.loja.usuario.Usuario;
import br.insper.loja.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final EventoService eventoService;

    @Autowired
    public CompraService(CompraRepository compraRepository, UsuarioService usuarioService, ProdutoService produtoService, EventoService eventoService) {
        this.compraRepository = compraRepository;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
        this.eventoService = eventoService;
    }

    public Compra salvarCompra(Compra compra) {
        Usuario usuario = usuarioService.getUsuario(compra.getUsuario());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        double valorCompra = 0;

        for (String produtoId : compra.getProdutos()) {
            Produto produto = produtoService.getProduto(produtoId);
            if (produto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID " + produtoId + " não encontrado.");
            }
            valorCompra += produto.getPreco();
        }

        for (String produtoId : compra.getProdutos()) {
            produtoService.diminuirQuantidade(produtoId, 1);
        }

        compra.setNome(usuario.getNome());
        compra.setDataCompra(LocalDateTime.now());
        eventoService.salvarEvento(usuario.getEmail(), "Compra realizada com sucesso no valor de R$ " + valorCompra);

        return compraRepository.save(compra);
    }

    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }
}