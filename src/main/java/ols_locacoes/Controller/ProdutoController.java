package ols_locacoes.Controller;

import ols_locacoes.model.Produto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Esta classe será responsável por receber requisições e devolver respostas.
@RequestMapping("/produtos") //Define o endereço principal deste controller:


public class ProdutoController {

    @GetMapping //Este método responde a uma requisição HTTP do tipo GET.
    public Produto buscarProduto() {

        Produto furadeira = new Produto(
                "Furadeira Bosch",
                "Ferramentas elétricas",
                5
        );

        return furadeira;
    }

}


