package dev.franke.felipe.compras.compras.api.mapper;

import dev.franke.felipe.compras.compras.api.dto.in.ProdutoINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.ProdutoOUTDTO;
import dev.franke.felipe.compras.compras.api.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper INSTANCIA = Mappers.getMapper(ProdutoMapper.class);

    @Mapping(source = "nome", target = "nomeProduto")
    @Mapping(source = "preco", target = "precoProduto")
    ProdutoINDTO produtoParaProdutoINDTO(Produto produto);

    @Mapping(source = "nomeProduto", target = "nome")
    @Mapping(source = "precoProduto", target = "preco")
    Produto produtoINDTOParaProduto(ProdutoINDTO produtoINDTO);

    @Mapping(source = "id", target = "idProduto")
    @Mapping(source = "nome", target = "nomeProduto")
    @Mapping(source = "preco", target = "precoProduto")
    @Mapping(source = "dataCriacao", target = "criadoEm")
    @Mapping(source = "dataModificacao", target = "modificadoEm")
    ProdutoOUTDTO produtoParaProdutoOUTDTO(Produto produto);

    @Mapping(source = "idProduto", target = "id")
    @Mapping(source = "nomeProduto", target = "nome")
    @Mapping(source = "precoProduto", target = "preco")
    @Mapping(source = "criadoEm", target = "dataCriacao")
    @Mapping(source = "modificadoEm", target = "dataModificacao")
    Produto produtoOUTDTOParaProduto(ProdutoOUTDTO produtoOUTDTO);

}
